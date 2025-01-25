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
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.myblog.R
import com.example.myblog.databinding.FragmentCreatePostBinding
import com.example.myblog.ui.base.BaseFragment
import kotlinx.coroutines.launch

class CreatePostFragment : BaseFragment() {

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

            // הצגת ה-loader בזמן העלאת הפוסט
            showLoader(binding.loader, "Uploading post...")

            lifecycleScope.launch {
                createPostViewModel.uploadPost(selectedImageUri!!, description, requireContext()) { success, message ->
                    hideLoader(binding.loader) // הסתרת ה-loader לאחר שהמשימה מסתיימת

                    if (success) {
                        showToast(requireContext(), "Post uploaded successfully")
                        findNavController().navigate(R.id.action_createPostFragment_to_homeFragment)
                    } else {
                        showToast(requireContext(), "Failed to upload post: $message")
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