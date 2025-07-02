package com.example.user.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.user.UserAdapter
import com.example.user.UserViewHolder
import com.example.user.databinding.PageUsersBinding
import com.example.user.viewModels.UsersViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UsersFragment : Fragment() {
    private val viewModel: UsersViewModel by viewModels()
    private var firstVisibleUserAdapterPosition: Int? = null

    private var _binding: PageUsersBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PageUsersBinding.inflate(inflater, container, false)
        firstVisibleUserAdapterPosition = savedInstanceState?.getInt(FIRST_VISIBLE_ADAPTER_POSITION)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = UserAdapter()
        binding.list.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getPagingDataFlow(firstVisibleUserAdapterPosition).collectLatest { pagingData ->
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
            ?.itemView
            ?.let { recyclerView.getChildAdapterPosition(it) }
            ?.let { outState.putInt(FIRST_VISIBLE_ADAPTER_POSITION, it) }
    }

    companion object{
        const val FIRST_VISIBLE_ADAPTER_POSITION = "FIRST_VISIBLE_ADAPTER_POSITION"
    }
}