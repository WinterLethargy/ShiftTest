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
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.core.di.Dispatcher
import com.example.user.UserAdapter
import com.example.user.UserViewHolder
import com.example.user.databinding.PageUsersBinding
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PageUsersBinding.inflate(inflater, container, false)
        firstVisibleUserId = savedInstanceState?.getLong(FIRST_VISIBLE_USER_ID)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = UserAdapter()
        binding.list.adapter = adapter
        binding.list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPagingDataFlow(firstVisibleUserId).collectLatest { pagingData ->
                    adapter.submitData(pagingData)
                }
            }
        }
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
        val recyclerView = binding.list

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