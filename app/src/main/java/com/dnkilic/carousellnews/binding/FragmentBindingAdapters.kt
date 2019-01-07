package com.dnkilic.carousellnews.binding

import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import android.widget.ImageView

import com.bumptech.glide.request.RequestOptions
import com.dnkilic.carousellnews.utils.glide.GlideApp

import javax.inject.Inject

class FragmentBindingAdapters @Inject constructor(val fragment: Fragment) {
    @BindingAdapter("imageUrl")
    fun bindImage(imageView: ImageView, url: String?) {
        GlideApp.with(fragment).load(url)
                .apply(RequestOptions().centerCrop())
                .into(imageView)
    }

}
