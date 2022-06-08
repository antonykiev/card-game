package com.photoframe.game.ui

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
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


        binding.imgFrame.setOnClickListener {
            binding.lSelector.visibility = View.INVISIBLE
            binding.lSelectorFrame.visibility = View.VISIBLE

        }

        binding.btnDone.setOnClickListener {
            binding.lSelector.visibility = View.INVISIBLE
            binding.lSelectorFrame.visibility = View.INVISIBLE
            binding.btnDone.visibility = View.INVISIBLE

            binding.btnShare.visibility = View.VISIBLE
            binding.btnSave.visibility = View.VISIBLE
        }

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

                binding.lSelector.visibility = View.VISIBLE
                binding.lSelectorFrame.visibility = View.INVISIBLE

                if (selectedFrameIndex == 0) {
                    binding.lSelector.visibility = View.VISIBLE
                    binding.lSelectorFrame.visibility = View.INVISIBLE
                }
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
            onPermissionDenied = { Toast.makeText(requireContext(), "onPermissionDenied", Toast.LENGTH_SHORT).show() },
            onNeverAskAgain = { Toast.makeText(requireContext(), "onNeverAskAgain", Toast.LENGTH_SHORT).show() }
        ) {
            SaveImageProvider.save(imgScreenShot, requireContext())
        }
        request.launch()
    }

    private fun shareBitmap(imgScreenShot: Bitmap) {
        val uri = BitmapToUriProvider.invoke(requireActivity(), imgScreenShot)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, "Share Image"))
    }
}