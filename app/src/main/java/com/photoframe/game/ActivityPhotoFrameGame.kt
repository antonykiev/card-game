package com.photoframe.game

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class ActivityPhotoFrameGame : AppCompatActivity(R.layout.activity_photoframe_game) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.beginTransaction()
            .replace(R.id.root, FragmentPhotoFrameRules())
            .commit()
    }
}