package com.example.socialmediaapp

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialmediaapp.daos.PostDao
import com.example.socialmediaapp.models.Post
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), IPostLiked {

    private lateinit var adapter: PostAdapter
    private lateinit var postDao: PostDao
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        val currentUserId = auth.currentUser!!.uid

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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.signOut -> {
                signOut()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }



    private fun signOut() {
        val builder = AlertDialog.Builder(this)
            .setTitle("SIGN OUT")
            .setMessage("Are you sure yout want to sign out?")
            .setIcon(R.drawable.ic_baseline_error_24)
            .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                auth.signOut()
                startActivity(Intent(this, SigninActivity::class.java))
                finish()
                dialog.dismiss()
            })
            .setNegativeButton("No", DialogInterface.OnClickListener{ dialog, which ->
                dialog.dismiss()
            })

        val dialog = builder.create()
        dialog.show()
    }
}