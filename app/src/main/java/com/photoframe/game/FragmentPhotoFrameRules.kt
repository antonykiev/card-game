package com.photoframe.game

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.databinding.FragmentPhotoframeRulesBinding

class FragmentPhotoFrameRules: Fragment(R.layout.fragment_photoframe_rules) {

    private val binding by viewBinding(FragmentPhotoframeRulesBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {

        }

        binding.btnNext.setOnClickListener {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, FragmentCardSelectorGame())
                .addToBackStack("")
                .commit()
        }
    }
}