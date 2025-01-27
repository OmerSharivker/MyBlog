package com.example.myblog.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myblog.R
import com.example.myblog.databinding.FragmentHomeBinding


import com.example.myblog.ui.main.home.PostAdapter

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()
    private lateinit var postAdapter: PostAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()


        homeViewModel.posts.observe(viewLifecycleOwner) { posts ->
            postAdapter.submitList(posts)
        }


        homeViewModel.loadPosts()
    }

    private fun setupRecyclerView() {
        postAdapter = PostAdapter(
            toggleLike = { postId, liked ->
                homeViewModel.toggleLike(postId, liked)
            },
            onEditPostClicked = { post ->

                val bundle = Bundle().apply {
                    putParcelable("post", post)
                }

                findNavController().navigate(R.id.action_homeFragment_to_createPostFragment, bundle)
            }
        )
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