package com.example.musicapp.activities.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.*
import com.example.musicapp.activities.details.AlbumDetailsActivity
import com.example.musicapp.databinding.ActivityAlbumDetailsBinding
import com.example.musicapp.databinding.ActivityMainBinding
import com.example.musicapp.helpers.BASE_URL
import com.example.musicapp.helpers.RetrofitInterface
import kotlinx.coroutines.DelicateCoroutinesApi
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

    @DelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Setting up recyclerView
        recyclerView = binding.albumList
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        //List to store data
        albumListDet = arrayListOf()

        //Insert album details to list
        if (isConnected()) {
            getAlbumData()
        }else{
            Toast.makeText(this,"No internet Connection", Toast.LENGTH_SHORT).show()
        }
    }

    @DelicateCoroutinesApi
    private fun getAlbumData() {

        //Retrofit API to set api
        val api = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(RetrofitInterface::class.java)

        //Coroutines Implementation to get data
        GlobalScope.launch(Dispatchers.Main){
            try{
                val response = api.getAlbumDetails()

                    Log.d("MAIN", "${response.feed.results.size}")
                    for(i in response.feed.results.indices){
                        val feed = response.feed.results[i]
                        albumListDet.add(AlbumListItem(feed.artistName, feed.name, feed.artworkUrl100, feed.url, feed.contentAdvisoryRating, feed.copyright, 0))
                        recyclerView.adapter?.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                    }

            }catch (e: Exception){
                Log.e("Main", "Error ${e.message}")
            }
        }

        //Call RecyclerViewAdapter with values
        recyclerView.adapter = RecyclerViewAdapter(albumListDet, this)
    }

    //Overriding item click from RecyclerView Item Click
    override fun onItemClick(item: AlbumListItem, position: Int) {
        val intent = Intent(this, AlbumDetailsActivity::class.java)
        intent.putExtra("det", item)
        startActivity(intent)
    }

    //Check if there is internet connection
    @RequiresApi(Build.VERSION_CODES.M)
    private fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetwork
            if (networkInfo != null) {
                val nc = connectivityManager.getNetworkCapabilities(networkInfo)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
    }
}
