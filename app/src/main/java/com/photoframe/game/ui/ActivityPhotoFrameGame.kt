package com.photoframe.game.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.photoframe.game.R

class ActivityPhotoFrameGame : AppCompatActivity(R.layout.activity_photoframe_game) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.root, FragmentPhotoFrameRules())
            .commit()
    }
}