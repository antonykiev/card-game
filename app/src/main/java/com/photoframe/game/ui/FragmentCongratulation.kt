package com.photoframe.game.ui

import android.os.Bundle
import android.view.View
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.R
import com.photoframe.game.databinding.FragmentCardCongratulationsBinding
import com.photoframe.game.viewmodels.ViewModelCard

class FragmentCongratulation: Fragment(R.layout.fragment_card_congratulations) {

    private val binding by viewBinding(FragmentCardCongratulationsBinding::bind)

    private val viewModelGame by activityViewModels<ViewModelCard>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().onBackPressedDispatcher.addCallback {
            //do nothing
        }

        viewModelGame.scoreCounter.observe(viewLifecycleOwner) {
            binding.tvResult.text = "${it.size}"
        }

        binding.btnNext.setOnClickListener {

        }
    }

}