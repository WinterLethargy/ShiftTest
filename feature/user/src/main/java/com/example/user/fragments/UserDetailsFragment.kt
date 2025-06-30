package com.example.user.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.user.databinding.PageUserDetailsBinding
import com.example.user.viewModels.UsersViewModel

class UserDetailsFragment  : Fragment() {
    private val viewModel: UsersViewModel by viewModels()

    private var _binding: PageUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PageUserDetailsBinding.inflate(inflater, container, false)
        (requireActivity() as AppCompatActivity).supportActionBar?.title = "Пользователь"
        return binding.root
    }
}