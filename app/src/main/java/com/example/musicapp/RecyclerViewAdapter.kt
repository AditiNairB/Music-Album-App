package com.example.musicapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicapp.databinding.AlbumItemBinding
import com.squareup.picasso.Picasso

class RecyclerViewAdapter(val albumList: ArrayList<AlbumListItem>, var clickListener: OnItemClickListener): RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>(){

    class MyViewHolder(val binding: AlbumItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(AlbumItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentAlbum = albumList[position]
        holder.binding.albumName.text = currentAlbum.name
        holder.binding.artistName.text = currentAlbum.artistName
        Picasso.get().load(currentAlbum.artworkUrl100).into(holder.binding.albumArt)

        holder.binding.root.setOnClickListener{
            Log.d("MAIN", "Clicked")
            clickListener.onItemClick(currentAlbum, position)
        }

    }
    override fun getItemCount(): Int {
        return albumList.size
    }
}

interface OnItemClickListener{
    fun onItemClick(item: AlbumListItem, position: Int)
}
