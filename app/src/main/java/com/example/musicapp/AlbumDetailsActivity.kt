package com.example.musicapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.musicapp.databinding.ActivityAlbumDetailsBinding
import com.squareup.picasso.Picasso


class AlbumDetailsActivity : AppCompatActivity() {


    //View Binding
    lateinit var binding: ActivityAlbumDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlbumDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Set album details to new activity
        val albumDet:AlbumListItem = intent.getSerializableExtra("det") as AlbumListItem
        binding.albumNameDet.text = albumDet.name
        binding.artistNameDet.text = albumDet.artistName
        binding.streamUrl.setOnClickListener{
            val i = Intent(Intent.ACTION_VIEW)
            i.data = Uri.parse(albumDet.url)
            startActivity(i)
        }
        Picasso.get().load(albumDet.artworkUrl100).into(binding.albumArtDet)

        //set back button functionality
        val actionBar = supportActionBar
        actionBar!!.title = "Album Details"
        actionBar.setDisplayHomeAsUpEnabled(true)

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }
}