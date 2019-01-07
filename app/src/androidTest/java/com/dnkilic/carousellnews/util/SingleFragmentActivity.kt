package com.dnkilic.carousellnews.util

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SingleFragmentActivity : AppCompatActivity() {

    fun setFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
                .add(android.R.id.content, fragment)
                .commit()
    }
}