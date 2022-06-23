package com.photoframe.game.ui

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.addCallback
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.snackbar.Snackbar
import com.photoframe.game.R
import com.photoframe.game.databinding.FragmentCardOperationBinding
import com.photoframe.game.utils.BitmapToUriProvider
import com.photoframe.game.utils.SaveImageProvider
import com.photoframe.game.viewmodels.ViewModelCard
import permissions.dispatcher.ktx.constructPermissionsRequest


class FragmentCardOperation: Fragment(R.layout.fragment_card_operation) {

    private val binding by viewBinding(FragmentCardOperationBinding::bind)
    private val viewModelGame by activityViewModels<ViewModelCard>()

    val actionOnTextChanged: (text: CharSequence?, start: Int, before: Int, count: Int) -> Unit = { text, start, before, count ->
        viewModelGame.onSelectMessage(text.toString())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModelGame.selectedMessage.observe(viewLifecycleOwner) {
            binding.card.setMessageText(it)
        }

        viewModelGame.selectedCard.observe(viewLifecycleOwner) {
            binding.card.setImg(it.idImg)
        }

        viewModelGame.selectedFrame.observe(viewLifecycleOwner) {
            binding.card.setFrame(it.value)
        }

        binding.imgEdTxt.doOnTextChanged(actionOnTextChanged)


        binding.btnBack.setOnClickListener {
            onBackPressed()
        }

        requireActivity().onBackPressedDispatcher.addCallback {
            onBackPressed()
        }

        binding.vBack.setOnClickListener {
            hideKeyboard(binding.root)
        }

        binding.imgEdTxt.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                editViewState()
            } else {
                initialViewState()
            }
        }

        binding.imgFrame.setOnClickListener {
            binding.lSelectorFrame.visibility = View.VISIBLE

            binding.btnBack.visibility = View.VISIBLE
            binding.btnDone2.visibility = View.VISIBLE

            binding.lSelector.visibility = View.INVISIBLE
            binding.title.visibility = View.INVISIBLE
            binding.btnDone.visibility = View.INVISIBLE
            binding.imgFrame.visibility = View.INVISIBLE
            binding.imgText.visibility = View.INVISIBLE
            binding.textView.visibility = View.INVISIBLE
            binding.textView2.visibility = View.INVISIBLE
        }

        binding.btnDone2.setOnClickListener {
            initialViewState()
        }
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
            binding.btnDone.visibility = View.VISIBLE
        }
        request.launch()
    }

    private fun shareBitmap(imgScreenShot: Bitmap) {
        val uri = BitmapToUriProvider.invoke(requireActivity(), imgScreenShot)

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, getString(R.string.share_image)))
        binding.btnDone.visibility = View.VISIBLE

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

    private fun hideKeyboard(view: View) {
        val imm = getSystemService(requireContext(), InputMethodManager::class.java)
        imm?.hideSoftInputFromWindow(view.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModelGame.resetAll()
    }

    private fun onBackPressed() {
        if (binding.imgEdTxt.hasFocus()) {
            hideKeyboard(binding.root)
            binding.imgEdTxt.clearFocus()

            binding.imgEdTxt.setText("")
            viewModelGame.resetMessage()
            return
        }

        if (binding.lSelectorFrame.isVisible) {
            viewModelGame.resetFrame()
            initialViewState()
            return
        }
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun initialViewState() {
        binding.run {
            card.setEditMode(false)

            title.visibility = View.VISIBLE
            btnDone.visibility = View.VISIBLE
            lSelector.alpha = 1F
            lSelector.visibility = View.VISIBLE
            imgFrame.visibility = View.VISIBLE
            imgText.visibility = View.VISIBLE
            textView.visibility = View.VISIBLE
            textView2.visibility = View.VISIBLE

            lSelectorFrame.visibility = View.INVISIBLE
            btnDone2.visibility = View.INVISIBLE
            btnBack.visibility = View.INVISIBLE
            btnDone2.visibility = View.INVISIBLE
        }
    }

    private fun editViewState() {
        binding.run {
            card.setEditMode(true)

            btnDone2.visibility = View.VISIBLE
            btnBack.visibility = View.VISIBLE

            btnDone.visibility = View.INVISIBLE
            title.visibility = View.INVISIBLE
            imgFrame.visibility = View.INVISIBLE
            imgText.visibility = View.INVISIBLE
            textView.visibility = View.INVISIBLE
            textView2.visibility = View.INVISIBLE
        }
    }

    fun doneState() {
        binding.run {
            card.setEditMode(false)

            title.visibility = View.INVISIBLE
            lSelectorFrame.visibility = View.INVISIBLE
            lSelector.visibility = View.INVISIBLE
            btnDone2.visibility = View.INVISIBLE
            btnBack.visibility = View.INVISIBLE

            imgFrame.visibility = View.INVISIBLE
            imgText.visibility = View.INVISIBLE
            textView.visibility = View.INVISIBLE
            textView2.visibility = View.INVISIBLE

            btnDone.visibility = View.GONE
            btnShare.visibility = View.VISIBLE
            btnSave.visibility = View.VISIBLE
        }
    }
}