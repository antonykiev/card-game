package com.photoframe.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.databinding.FragmentCardOperationBinding

class FragmentCardOperation: Fragment(R.layout.fragment_card_operation) {

    private val binding by viewBinding(FragmentCardOperationBinding::bind)
    private val viewModelGame by activityViewModels<ViewModelCard>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelGame.selectedCard.observe(viewLifecycleOwner) {
            binding.card.setImageResource(it.idImg)
        }
    }
}