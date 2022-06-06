package com.photoframe.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.databinding.FragmentCardInfoBinding

class FragmentCardInfo: Fragment(R.layout.fragment_card_info) {

    private val binding by viewBinding(FragmentCardInfoBinding::bind)
    private val viewModelGame by activityViewModels<ViewModelCard>()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelGame.selectedCard.observe(viewLifecycleOwner) {
            binding.card.setImageResource(it.idImg)
            binding.title.setText(it.title)
            binding.content.setText(it.content)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.btnNext.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .add(R.id.root, FragmentCardOperation())
                .addToBackStack("")
                .commit()
        }
    }

}