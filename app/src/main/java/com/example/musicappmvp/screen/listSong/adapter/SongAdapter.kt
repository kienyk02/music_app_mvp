package com.example.musicappmvp.screen.listSong.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.musicappmvp.data.model.Song
import com.example.musicappmvp.databinding.ItemSongBinding

class SongAdapter(private var list: List<Song>) : RecyclerView.Adapter<SongAdapter.ViewHolder>() {
    var onItemClick: ((Int) -> Unit)? = null
    inner class ViewHolder(private val binding: ItemSongBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(song: Song) {
            binding.apply {
                txtSongName.text = song.name
                txtSongAuthor.text = song.author
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(list: List<Song>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSongBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(position)
        }
    }

    override fun getItemCount() = list.size
}