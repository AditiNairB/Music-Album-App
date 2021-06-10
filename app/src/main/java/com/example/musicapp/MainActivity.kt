package com.example.musicapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), OnItemClickListener {

    //RecyclerView
    private lateinit var recyclerView: RecyclerView
    //Album List
    private lateinit var albumListDet: ArrayList<AlbumListItem>
    //View Binding
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //Set App to Night Mode
//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)


        //Setting up recyclerView
        recyclerView = binding.albumList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        albumListDet = arrayListOf()

        //Insert album details to list
        getAlbumData()

    }

    private fun getAlbumData() {

        //Retrofit API
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        //Coroutines Implementation
        GlobalScope.launch(Dispatchers.Main){
            try{
                val response = api.getAlbumDetails()

                for(i in response.feed.results.indices){
                    var feed = response.feed.results[i]
                    albumListDet.add(AlbumListItem(feed.artistName, feed.name, feed.artworkUrl100, feed.url))
                    recyclerView.adapter?.notifyDataSetChanged()
                    binding.progressBar.visibility = View.GONE
                }
                Log.d("Main", "Here Inside")
            }catch (e: Exception){
                Log.e("Main", "Error ${e.message}")
            }
        }

        //Call RecyclerViewAdapter with values
        recyclerView.adapter = RecyclerViewAdapter(albumListDet, this)
    }

    override fun onItemClick(item: AlbumListItem, position: Int) {
        val intent = Intent(this, AlbumDetailsActivity::class.java)
        intent.putExtra("det", item)
        startActivity(intent)
    }
}