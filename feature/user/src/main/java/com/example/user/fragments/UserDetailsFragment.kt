package com.example.user.fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.bumptech.glide.Glide
import com.example.user.R
import com.example.user.databinding.PageUserDetailsBinding
import com.example.user.viewModels.UserDetailUIState
import com.example.user.viewModels.UserDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class UserDetailsFragment  : Fragment() {
    private val viewModel: UserDetailsViewModel by viewModels()
    val packageManager: PackageManager
        get() = requireContext().packageManager

    private var _binding: PageUserDetailsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = PageUserDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.email.setOnClickListener{
            val email = viewModel.uiState.value.user?.email ?: return@setOnClickListener
            val emailUri = Uri.parse("mailto:${email}")
            val emailIntent = Intent(Intent.ACTION_SENDTO,emailUri)
            try {
                startActivity(emailIntent)
            }
            catch (ex: ActivityNotFoundException){
                Log.e("UserDetailsFragment", ex.toString())
            }
        }

        binding.location.setOnClickListener{
            val coordinates = viewModel.uiState.value.user?.location?.coordinates ?: return@setOnClickListener
            val geoUri = Uri.parse("geo:${coordinates.latitude},${coordinates.longitude}")
            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
            try {
                startActivity(mapIntent)
            }
            catch (ex: ActivityNotFoundException){
                Log.e("UserDetailsFragment", ex.toString())
            }
        }

        binding.phone.setOnClickListener{
            val phone = viewModel.uiState.value.user?.phone ?: return@setOnClickListener
            val phoneUrl = Uri.parse("tel:$phone")
            val dialIntent = Intent(Intent.ACTION_DIAL,phoneUrl)
            try {
                startActivity(dialIntent)
            }
            catch (ex: ActivityNotFoundException){
                Log.e("UserDetailsFragment", ex.toString())
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiState.collect{
                    it.user?.let {
                        setTitle("${it.name.title} ${it.name.last}")
                    }
                    setupView(it)
                }
            }
        }
    }

    private fun setupView(userUIState: UserDetailUIState){
        val user = userUIState.user ?: return
        Glide.with(this)
            .load(user.picture.large)
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.error)
            .into(binding.image)

        binding.name.text =  "${user.name.title} ${user.name.first} ${user.name.last}"
        binding.age.text = "${user.dob.date} (${user.dob.age})"
        binding.gender.text = user.gender
        binding.email.text = user.email
        binding.phone.text = user.phone
        binding.location.text = "${user.location.country}, ${user.location.city}"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setTitle(string: String){
        (requireActivity() as AppCompatActivity).supportActionBar?.title = string
    }
}