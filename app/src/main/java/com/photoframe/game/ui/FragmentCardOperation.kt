package com.photoframe.game.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.photoframe.game.utils.BitmapToUriProvider
import com.photoframe.game.R
import com.photoframe.game.utils.SaveImageProvider
import com.photoframe.game.viewmodels.ViewModelCard
import com.photoframe.game.databinding.FragmentCardOperationBinding
import permissions.dispatcher.ktx.constructPermissionsRequest

class FragmentCardOperation: Fragment(R.layout.fragment_card_operation) {

    private val binding by viewBinding(FragmentCardOperationBinding::bind)
    private val viewModelGame by activityViewModels<ViewModelCard>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelGame.selectedCard.observe(viewLifecycleOwner) {
            binding.card.setImg(it.idImg)
        }

        viewModelGame.selectedFrame.observe(viewLifecycleOwner) {
            binding.card.setFrame(it.value)
        }

        binding.btnBack.setOnClickListener {
            requireActivity().onBackPressed()
        }

        binding.imgEdTxt.doOnTextChanged { text, start, before, count ->
            viewModelGame.onSelectMessage(text.toString())
            binding.card.setMessageText(text.toString())
        }

//        binding.imgEdTxt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
//            if (hasFocus) {
//                editViewState()
//            } else {
//                initialViewState()
//            }
//        }

        binding.imgFrame.setOnClickListener {
            binding.lSelector.visibility = View.INVISIBLE
            binding.lSelectorFrame.visibility = View.VISIBLE

            binding.title.visibility = View.INVISIBLE
            binding.btnDone.visibility = View.INVISIBLE
            binding.btnBack.visibility = View.VISIBLE
            binding.btnDone2.visibility = View.VISIBLE
        }

        binding.btnDone2.setOnClickListener(::onDoneClicked)
        binding.btnDone.setOnClickListener(::onDoneClicked)

        binding.btnSave.setOnClickListener {
            val imgScreenShot: Bitmap = takeScreenShot(binding.card)
            saveImg(imgScreenShot)
            viewModelGame.onSaved()
        }

        binding.btnShare.setOnClickListener {
            val imgScreenShot: Bitmap = takeScreenShot(binding.card)
            shareBitmap(imgScreenShot)
            viewModelGame.onShared()
        }

        listOf(
            binding.cancel,
            binding.frame1,
            binding.frame2,
            binding.frame3,
            binding.frame4,
        ).forEach {
            it.setOnClickListener {
                val selectedFrameIndex = runCatching { it.tag.toString().toInt() }
                    .getOrElse { 0 }
                viewModelGame.onSelectFrame(ViewModelCard.Frame.values()[selectedFrameIndex])

                initialViewState()

//                binding.lSelector.visibility = View.VISIBLE
//                binding.lSelectorFrame.visibility = View.INVISIBLE
//
//                if (selectedFrameIndex == 0) {
//                    binding.lSelector.visibility = View.VISIBLE
//                    binding.lSelectorFrame.visibility = View.INVISIBLE
//                }
            }
        }
    }

    private fun takeScreenShot(view: View): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun saveImg(imgScreenShot: Bitmap) {
        val request = constructPermissionsRequest(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            onShowRationale = { it.proceed() },
            onPermissionDenied = { Toast.makeText(requireContext(), getString(R.string.on_permission_denied), Toast.LENGTH_SHORT).show() },
            onNeverAskAgain = { Toast.makeText(requireContext(), getString(R.string.on_never_ask_again), Toast.LENGTH_SHORT).show() }
        ) {
            SaveImageProvider.save(imgScreenShot, requireContext())
            Snackbar.make(
                binding.root,
                getString(R.string.success_saving),
                Snackbar.LENGTH_SHORT
            ).show()
        }
        request.launch()
    }

    private fun shareBitmap(imgScreenShot: Bitmap) {
        val uri = BitmapToUriProvider.invoke(requireActivity(), imgScreenShot)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, getString(R.string.share_image)))
    }

    private fun onDoneClicked(view: View) {
        if (binding.btnShare.isVisible) {
            requireActivity().supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, FragmentCongratulation())
                .commit()
        } else {
            doneState()
        }
    }

    private fun initialViewState() {
        binding.run {
            title.visibility = View.VISIBLE
            btnDone.visibility = View.VISIBLE
            lSelector.alpha = 1F
            lSelector.visibility = View.VISIBLE

            lSelectorFrame.visibility = View.INVISIBLE
            btnDone2.visibility = View.INVISIBLE
            btnBack.visibility = View.INVISIBLE
            btnDone2.visibility = View.INVISIBLE
        }
    }

    private fun editViewState() {
        binding.run {
            title.visibility = View.VISIBLE
            btnDone.visibility = View.VISIBLE
            lSelectorFrame.visibility = View.INVISIBLE
            lSelector.alpha = 0F
        }
    }

    fun doneState() {
        binding.run {
            title.visibility = View.INVISIBLE
            lSelectorFrame.visibility = View.INVISIBLE
            lSelector.visibility = View.INVISIBLE
            btnDone2.visibility = View.INVISIBLE
            btnBack.visibility = View.INVISIBLE

            btnDone.visibility = View.VISIBLE
            btnShare.visibility = View.VISIBLE
            btnSave.visibility = View.VISIBLE
        }
    }
}