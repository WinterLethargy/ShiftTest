package com.example.user.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.di.Dispatcher
import com.example.user.UserAdapter
import com.example.user.UserItemDecoration
import com.example.user.UserViewHolder
import com.example.user.UsersMenuProvider
import com.example.user.databinding.PageUsersBinding
import com.example.user.navigation.UserDetailsRoute
import com.example.user.viewModels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private val viewModel: UsersViewModel by viewModels()
    private var firstVisibleUserId: Long? = null

    private var _binding: PageUsersBinding? = null
    private val binding get() = _binding!!

    private var adapter: UserAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let{
            firstVisibleUserId = it.getLong(FIRST_VISIBLE_USER_ID)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PageUsersBinding.inflate(inflater, container, false)
        setupMenuProvider()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = UserAdapter(this::navigateToUser)
        binding.list.adapter = adapter
        binding.list.addItemDecoration(UserItemDecoration())
        startCollectPagingData()
        scrollToFirstVisibleUser()
    }

    private fun navigateToUser(id: Long){
        firstVisibleUserId = id
        findNavController().navigate(UserDetailsRoute(id))
    }

    private fun startCollectPagingData(){
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPagingDataFlow(firstVisibleUserId).collectLatest { pagingData ->
                    adapter?.submitData(pagingData)
                }
            }
        }
    }

    private fun scrollToFirstVisibleUser(){
        val localUserId = firstVisibleUserId
        if (localUserId == null)
            return

        val localAdapter = adapter
        if(localAdapter == null)
            return

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                val loadStateListener = object : (CombinedLoadStates) -> Unit {
                    override fun invoke(loadStates: CombinedLoadStates) {
                        if (loadStates.refresh !is LoadState.NotLoading)
                            return

                        val snapshot = localAdapter.snapshot()
                        snapshot.firstOrNull { user -> user?.id == localUserId }
                            ?.let{ user ->
                                val index = snapshot.indexOf(user)
                                binding.list.scrollToPosition(index)
                                localAdapter.removeLoadStateListener(this)
                            }
                    }
                }
                localAdapter.addLoadStateListener(loadStateListener)
            }
        }
    }

    private fun setupMenuProvider(){
        requireActivity().addMenuProvider(UsersMenuProvider(this::refresh), viewLifecycleOwner)
    }

    private fun refresh(){
        adapter?.refresh()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        saveLastVisibleItemAdapterPosition(outState)
    }

    private fun saveLastVisibleItemAdapterPosition(outState: Bundle) {

        val recyclerView = _binding?.list
        val localUserId = firstVisibleUserId
        if(recyclerView == null && localUserId != null){
            outState.putLong(FIRST_VISIBLE_USER_ID, localUserId)
            return
        }

        if(recyclerView == null){
            return
        }

        val visibleLayoutPosition = recyclerView.layoutManager
            .let { it as? LinearLayoutManager }
            ?.let{ it.findFirstVisibleItemPosition() }

        if (visibleLayoutPosition == null || visibleLayoutPosition == RecyclerView.NO_POSITION)
            return

        recyclerView.findViewHolderForLayoutPosition(visibleLayoutPosition)
            ?.let { it as? UserViewHolder }
            ?.user
            ?.let { outState.putLong(FIRST_VISIBLE_USER_ID, it.id) }
    }

    companion object{
        const val FIRST_VISIBLE_USER_ID = "FIRST_VISIBLE_USER_ID"
    }
}