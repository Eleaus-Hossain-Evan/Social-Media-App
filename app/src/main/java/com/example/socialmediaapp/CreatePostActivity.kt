package com.example.socialmediaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.socialmediaapp.daos.PostDao
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_create_post.*

class CreatePostActivity : AppCompatActivity() {
    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        postDao = PostDao()

        btnPost.setOnClickListener {
            val input = etPost.text.toString().trim()
            if (input.isNotEmpty()){
                postDao.addPost(input)
                finish()
            }
        }
    }
}