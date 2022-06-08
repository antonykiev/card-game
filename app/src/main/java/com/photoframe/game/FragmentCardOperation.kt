package com.photoframe.game

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import by.kirich1409.viewbindingdelegate.viewBinding
import com.photoframe.game.databinding.FragmentCardOperationBinding
import permissions.dispatcher.ktx.constructPermissionsRequest
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.OutputStream

class FragmentCardOperation: Fragment(R.layout.fragment_card_operation) {

    private val folderName = "Budynek"

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
        }

        binding.btnShare.setOnClickListener {
            val imgScreenShot: Bitmap = takeScreenShot(binding.card)
            shareBitmap(imgScreenShot)
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
            saveImage(imgScreenShot, requireContext(), folderName)
        }
        request.launch()
    }

    private fun shareBitmap(imgScreenShot: Bitmap) {

    }

    private fun saveImage(bitmap: Bitmap, context: Context, folderName: String) {
        if (android.os.Build.VERSION.SDK_INT >= 29) {
            val values = contentValues()
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + folderName)
            values.put(MediaStore.Images.Media.IS_PENDING, true)
            // RELATIVE_PATH and IS_PENDING are introduced in API 29.

            val uri: Uri? = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            if (uri != null) {
                saveImageToStream(bitmap, context.contentResolver.openOutputStream(uri))
                values.put(MediaStore.Images.Media.IS_PENDING, false)
                context.contentResolver.update(uri, values, null, null)
            }
        } else {
            val directory = File(Environment.getExternalStorageDirectory().toString() + separator + folderName)
            // getExternalStorageDirectory is deprecated in API 29

            if (!directory.exists()) {
                directory.mkdirs()
            }
            val fileName = System.currentTimeMillis().toString() + ".png"
            val file = File(directory, fileName)
            saveImageToStream(bitmap, FileOutputStream(file))
            if (file.absolutePath != null) {
                val values = contentValues()
                values.put(MediaStore.Images.Media.DATA, file.absolutePath)
                // .DATA is deprecated in API 29
                context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            }
        }
    }

    private fun contentValues() : ContentValues {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png")
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        return values
    }

    private fun saveImageToStream(bitmap: Bitmap, outputStream: OutputStream?) {
        if (outputStream != null) {
            try {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

}