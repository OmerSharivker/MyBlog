package com.example.myblog.ui.main.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myblog.R
import com.example.myblog.data.model.Post
import com.google.firebase.auth.FirebaseAuth

class PostAdapter(
    private val toggleLike: (String, Boolean) -> Unit // פונקציה שתטפל בלייקים
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback()) {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImage: ImageView = itemView.findViewById(R.id.profileImageView)
        val userName: TextView = itemView.findViewById(R.id.userNameTextView)
        val postImage: ImageView = itemView.findViewById(R.id.postImageView)
        val postDescription: TextView = itemView.findViewById(R.id.postDescriptionTextView)
        val likeIcon: ImageView = itemView.findViewById(R.id.likeIcon) // וודא שה-ID נכון
        val likeCount: TextView = itemView.findViewById(R.id.likeCount)
        val commentCount: TextView = itemView.findViewById(R.id.commentCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(view)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)

        holder.userName.text = post.userName
        holder.postDescription.text = post.description
        holder.likeCount.text = post.likes.size.toString()
        holder.commentCount.text = post.comments.toString()

        Glide.with(holder.itemView.context).load(post.userProfileImageUrl).into(holder.profileImage)
        Glide.with(holder.itemView.context).load(post.postImageUrl).into(holder.postImage)

        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid
        val userLiked = currentUserId != null && post.likes.contains(currentUserId)
         Log.d("PostAdapter", "User liked: $userLiked")

        holder.likeIcon.isSelected = userLiked
        Log.d("PostAdapter", "Current icon state: isLikedIcon = $ holder.likeIcon")

        holder.likeIcon.setOnClickListener {
            if (currentUserId == null) {
                Toast.makeText(holder.itemView.context, "You must be logged in to like posts.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val liked = !userLiked
            toggleLike(post.id, liked)

            val context = holder.itemView.context
            Toast.makeText(
                context,
                if (liked) "You liked this post" else "You unliked this post",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    class PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
            return oldItem == newItem
        }
    }


}