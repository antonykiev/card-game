package com.photoframe.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.databinding.FragmentCardSelectorGameBinding

class FragmentCardSelectorGame: Fragment(R.layout.fragment_card_selector_game) {
    
    private val viewModelGame by activityViewModels<ViewModelCard>()
    private val binding by viewBinding(FragmentCardSelectorGameBinding::bind)



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listOf(
            binding.btnBack,
            binding.btnBack2,
        ).forEach {
            it.setOnClickListener {
                requireActivity().onBackPressed()
            }
        }

        listOf(
            binding.lancut,
            binding.ordinata,
            binding.fontanna,
        ).forEach {
            it.setOnClickListener {
                when (it.id) {
                    R.id.lancut -> viewModelGame.onSelect(ViewModelCard.Card.Lancut)
                    R.id.ordinata -> viewModelGame.onSelect(ViewModelCard.Card.Ordynata)
                    else -> viewModelGame.onSelect(ViewModelCard.Card.Fontanna)
                }

                requireActivity().supportFragmentManager
                    .beginTransaction()
                    .add(R.id.root, FragmentCardInfo())
                    .addToBackStack("")
                    .commit()
            }
        }
    }

}