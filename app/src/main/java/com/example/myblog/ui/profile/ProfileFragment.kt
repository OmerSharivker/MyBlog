package com.example.myblog.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.myblog.databinding.FragmentProfileBinding
import com.example.myblog.ui.main.home.PostAdapter

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()

        profileViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.profileName.text = it.name
                Glide.with(this).load(it.profileImageUrl).into(binding.profileImage)
            }
        }

        profileViewModel.userPosts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
        }

        profileViewModel.loadProfile()
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter { postId, liked ->
            // Placeholder for toggleLike
        }
        binding.postsRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = postAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}