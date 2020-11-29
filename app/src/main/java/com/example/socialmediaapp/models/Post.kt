package com.example.socialmediaapp.models

data class Post(
    val text: String = "",
    val createdBy: User = User(),
    val createdAt: Long = 0L,
    val LikedBy: ArrayList<String> = ArrayList()
)
