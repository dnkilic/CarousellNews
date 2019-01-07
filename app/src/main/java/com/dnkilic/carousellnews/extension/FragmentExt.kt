package com.dnkilic.carousellnews.extension

import android.content.Context
import androidx.fragment.app.Fragment

/**
 *  Fragment's extension functions & properties.
 */

val Fragment.fragmentContext: Context
    get() = context!!
