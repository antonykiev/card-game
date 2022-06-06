package com.photoframe.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.databinding.FragmentNinepuzzleCongratulationsBinding

class FragmentCongratulation: Fragment(R.layout.fragment_ninepuzzle_congratulations) {

    private val binding by viewBinding(FragmentNinepuzzleCongratulationsBinding::bind)

    private val viewModelGame by activityViewModels<ViewModelCard>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        viewModelGame.calculateScore().observe(viewLifecycleOwner) {
//            binding.tvResult.text = "$it"
//        }

        binding.btnNext.setOnClickListener {

        }
    }

}