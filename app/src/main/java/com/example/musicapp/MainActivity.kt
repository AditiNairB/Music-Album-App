package com.example.musicapp

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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

                if(null != response){
                    for(i in response.feed.results.indices){
                        var feed = response.feed.results[i]
                        albumListDet.add(AlbumListItem(feed.artistName, feed.name, feed.artworkUrl100, feed.url))
                        recyclerView.adapter?.notifyDataSetChanged()
                        binding.progressBar.visibility = View.GONE
                    }
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
    private fun isConnected(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkInfo = connectivityManager.activeNetwork
            if (networkInfo != null) {
                val nc = connectivityManager.getNetworkCapabilities(networkInfo)
                //It will check for both wifi and cellular network
                return nc!!.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)
            }
            return false
        } else {
            val netInfo = connectivityManager.activeNetworkInfo
            return netInfo != null && netInfo.isConnectedOrConnecting
        }
    }
}