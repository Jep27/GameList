package com.example.gamelist.ui.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.gamelist.databinding.GameListItemBinding
import com.example.gamelist.model.Game

class GameListAdapter(
    private val clickListener: (Game) -> Unit
) : ListAdapter<Game, GameListAdapter.GameViewHolder>(DiffCallback) {

    class GameViewHolder(
        private var binding: GameListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(game: Game) {
            binding.gameNameTextView.text = game.gameName
            binding.gameGenreTextView.text = game.gameGenre
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<Game>() {
        override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return GameViewHolder(
            GameListItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val forageable = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(forageable)
        }
        holder.bind(forageable)
    }
}