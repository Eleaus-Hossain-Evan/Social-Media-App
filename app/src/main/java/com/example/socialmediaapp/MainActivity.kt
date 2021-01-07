package com.example.socialmediaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediaapp.daos.PostDao
import com.example.socialmediaapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IPostLiked {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        floatingActionButton.setOnClickListener{
            val intent = Intent(this,CreatePostActivity::class.java)
            startActivity(intent)
        }

        setUpRecycelerView()
    }

    private fun setUpRecycelerView() {
        postDao = PostDao()
        val postCollection = postDao.postCollections
        val query = postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOptions = FirestoreRecyclerOptions.Builder<Post>().setQuery(query, Post::class.java).build()

        adapter = PostAdapter(recyclerViewOptions, this)

        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        adapter.stopListening()
        super.onStop()
    }

    override fun onLikedClicked(postID: String) {
        postDao.updateLikes(postID)
    }
}