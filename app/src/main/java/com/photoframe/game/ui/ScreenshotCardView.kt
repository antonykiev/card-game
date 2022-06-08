package com.photoframe.game.ui

import android.content.Context
import androidx.core.view.setPadding
import android.graphics.Bitmap
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.photoframe.game.databinding.ViewCardBinding

class ScreenshotCardView: ConstraintLayout {

    private val binding = ViewCardBinding.inflate(LayoutInflater.from(context), this, true)

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)

    fun setFrame(padding: Int) {
        binding.card.setPadding(padding * 10)
    }

    fun setImg(id: Int) {
        binding.card.setImageResource(id)

    }

    fun setMessageText(msg: String) {
        binding.cardMessage.text = msg
    }

    fun takeScreenShot(): Bitmap {
        val bitmap: Bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        return bitmap
    }

}