package com.example.gamelist.ui.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.gamelist.GameListApplication
import com.example.gamelist.R
import com.example.gamelist.databinding.GameListFragmentBinding
import com.example.gamelist.ui.adapter.GameListAdapter
import com.example.gamelist.ui.viewmodel.GameListViewModel
import com.example.gamelist.ui.viewmodel.GameListViewModelFactory


class GameListFragment : Fragment() {

    private val viewModel: GameListViewModel by activityViewModels()  {
        GameListViewModelFactory(
            (activity?.application as GameListApplication).database.gameDao()
        )
    }

    private var _binding: GameListFragmentBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = GameListFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = GameListAdapter { game ->
            val action = GameListFragmentDirections
                .actionGameListFragmentToForageableDetailFragment(game.id)
            view.findNavController().navigate(action)
        }

        viewModel.allGames.observe(this.viewLifecycleOwner) { allGames ->
            adapter.submitList(allGames)
        }

        binding.apply {
            binding.gameListRecyclerView.adapter = adapter
            floatingActionButtonAddGame.setOnClickListener {
                findNavController().navigate(
                R.id.action_gameListFragment_to_gameAddFragment
                )
            }
        }
    }
}