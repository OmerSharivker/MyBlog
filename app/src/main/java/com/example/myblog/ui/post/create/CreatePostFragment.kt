package com.example.myblog.ui.post.create

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.example.myblog.databinding.FragmentCreatePostBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CreatePostFragment : Fragment() {

    private var _binding: FragmentCreatePostBinding? = null
    private val binding get() = _binding!!
    private val createPostViewModel: CreatePostViewModel by viewModels()
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            Glide.with(this).load(it).into(binding.imagePreview)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreatePostBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.selectImageButton.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }


        binding.aiGenerateButton.setOnClickListener {
            createPostViewModel.generateDescription { generatedText ->
                binding.descriptionEditText.setText(generatedText)
            }
        }


        binding.uploadPostButton.setOnClickListener {
            val description = binding.descriptionEditText.text.toString()

            if (selectedImageUri == null || description.isEmpty()) {
                showToast(requireContext(), "Please select an image and write a description")
                return@setOnClickListener
            }


            lifecycleScope.launch {
                createPostViewModel.uploadPost(selectedImageUri!!, description, requireContext()) { success, message ->

                        if (success) {
                            Log.d("CreatePostFragment", "Post uploaded successfully")
                        } else {
                         Log.d("CreatePostFragment", "Failed to upload post: $message")
                        }

                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // פונקציה להצגת Toast
    private fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}