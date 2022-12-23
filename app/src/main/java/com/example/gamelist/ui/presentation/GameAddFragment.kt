package com.example.gamelist.ui.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.gamelist.GameListApplication
import com.example.gamelist.MainActivity
import com.example.gamelist.R
import com.example.gamelist.databinding.GameListAddBinding
import com.example.gamelist.model.Game
import com.example.gamelist.ui.viewmodel.GameListViewModel
import com.example.gamelist.ui.viewmodel.GameListViewModelFactory
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.util.*


class GameAddFragment : Fragment() {

    private val navigationArgs: GameAddFragmentArgs by navArgs()

    private val viewModel: GameListViewModel by activityViewModels {
        GameListViewModelFactory(
            (activity?.application as GameListApplication).database.gameDao()
        )
    }

    private var _binding: GameListAddBinding? = null

    private lateinit var game: Game

    private val binding get() = _binding!!

    private lateinit var genres : ArrayList<String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = GameListAddBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = navigationArgs.id
        genres = ArrayList<String>(Arrays.asList(*resources.getStringArray(R.array.genre)))
        setupSpinner()

            if (id > 0) {
                viewModel.getGame(id).observe(this.viewLifecycleOwner){ selectedForgable ->
                    game = selectedForgable
                    binding.editTextNameAdd.isEnabled = false
                    getActionBar()?.setTitle("Update "+ game.gameName);
                    bindGame(game)
            }
                binding.floatingActionButtonSave.setOnClickListener {
                    updateGame()
                }
            } else {
                binding.editTextNameAdd.isEnabled = true
                binding.floatingActionButtonSave.setOnClickListener {
                    addGame()
                }
            }
    }

    private fun setupSpinner(){
        val adapter = ArrayAdapter.createFromResource(requireContext(),
            R.array.genre, android.R.layout.simple_spinner_item)
        binding.spinnerGenreAdd.adapter = adapter
    }

    private fun addGame() {
        if (isValidEntry()) {
            viewModel.addGame(
                binding.editTextNameAdd.text.toString(),
                binding.editTextDetailsAdd.text.toString(),
                genres[binding.spinnerGenreAdd.selectedItemPosition],
                binding.editTextRatingAdd.text.toString().toInt()
            )
            findNavController().navigate(
                R.id.action_gameAddFragment_to_gameListFragment
            )
        }else{
            showAlertDialog(R.string.incomplete_details)
        }
    }

    private fun updateGame() {
        if (isValidEntry()) {
            viewModel.updateGame(
                id = navigationArgs.id,
                gameName = binding.editTextNameAdd.text.toString(),
                gameDetail = binding.editTextDetailsAdd.text.toString(),
                gameGenre = genres[binding.spinnerGenreAdd.selectedItemPosition],
                gameRating = binding.editTextRatingAdd.text.toString().toInt()
            )
            findNavController().navigate(
                R.id.action_gameAddFragment_to_gameListFragment
            )
        }else{
            showAlertDialog(R.string.incomplete_details)
        }
    }

    private fun bindGame(game: Game) {
        binding.apply{
            editTextNameAdd.setText(game.gameName, TextView.BufferType.SPANNABLE)
            editTextDetailsAdd.setText(game.gameDetails, TextView.BufferType.SPANNABLE)
            spinnerGenreAdd.setSelection(genres.indexOf(game.gameGenre))
            editTextRatingAdd.setText(game.gameRating.toString(), TextView.BufferType.SPANNABLE)
            floatingActionButtonSave.setOnClickListener {
                showConfirmationDialog()
            }
        }
    }

    private fun isValidEntry() = viewModel.isValidEntry(
        binding.editTextNameAdd.text.toString(),
        binding.editTextDetailsAdd.text.toString(),
        binding.editTextDetailsAdd.text.toString(),
        binding.editTextRatingAdd.text.toString(),
    )

    private fun showConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(R.string.update_question)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.cancel)) { _, _ -> }
            .setPositiveButton(getString(R.string.update)) { _, _ ->
                updateGame()
            }
            .show()
    }

    private fun showAlertDialog(message: Int) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setCancelable(false)
            .setNegativeButton(getString(R.string.close)) { _, _ -> }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getActionBar(): ActionBar? {
        return (activity as MainActivity?)?.getSupportActionBar()
    }
}

