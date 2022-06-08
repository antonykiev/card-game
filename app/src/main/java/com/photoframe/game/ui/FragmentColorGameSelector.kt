package com.photoframe.game.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.R
import com.photoframe.game.databinding.FragmentNinepuzzleColorSelectorGameBinding

class FragmentColorGameSelector: Fragment(R.layout.fragment_ninepuzzle_color_selector_game) {

    private val binding by viewBinding(FragmentNinepuzzleColorSelectorGameBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnNext.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, FragmentCongratulation())
                .commit()
        }
    }

}