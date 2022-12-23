package com.example.gamelist.ui.presentation

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamelist.GameListApplication
import com.example.gamelist.R
import com.example.gamelist.databinding.GameListDetailsFragmentBinding
import com.example.gamelist.model.Game
import com.example.gamelist.ui.viewmodel.GameListViewModel
import com.example.gamelist.ui.viewmodel.GameListViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class GameListDetailsFragment : Fragment() {

    private val navigationArgs: GameListDetailsFragmentArgs by navArgs()

    private val viewModel: GameListViewModel by activityViewModels {
        GameListViewModelFactory(
            (activity?.application as GameListApplication).database.gameDao()
        )
    }

    private lateinit var game: Game
    private var _binding: GameListDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GameListDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
        viewModel.getGame(id).observe(this.viewLifecycleOwner) { selectedGame ->
            game = selectedGame
            bindGame()
        }

    }

    private fun bindGame() {
        binding.apply {
            nameTextView.text = game.gameName
            detailsTextView.text = game.gameDetails
            detailsGenreTextView.text = game.gameGenre
            ratingTextView.text = game.gameRating.toString()

            floatingActionButtonEdit.setOnClickListener {
                val action = GameListDetailsFragmentDirections
                    .actionGameListDetailsFragmentToGameAddFragment(game.id)
                findNavController().navigate(action)
            }

            floatingActionButtonDelete.setOnClickListener {
                showConfirmationDialog()
            }
        }
    }

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.delete_question))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                deleteGame(game)
            }
            .show()
    }

    private fun deleteGame(game: Game) {
        viewModel.deleteGame(game)
        findNavController().navigate(
            R.id.action_gameListDetailsFragment_to_gameListFragment
        )
    }
}