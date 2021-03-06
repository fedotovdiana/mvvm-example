package com.itis.group11801.fedotova.mvvmexample.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.itis.group11801.fedotova.mvvmexample.R
import com.itis.group11801.fedotova.mvvmexample.di.AppInjector
import com.itis.group11801.fedotova.mvvmexample.navigation.Navigator.Companion.KEY_NEWS
import com.itis.group11801.fedotova.mvvmexample.viewmodel.DetailsViewModel
import kotlinx.android.synthetic.main.activity_details.*
import javax.inject.Inject

class DetailsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: DetailsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AppInjector.initDetailsComponent()
        AppInjector.injectDetailsActivity(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        subscribe()
    }

    private fun subscribe() {
        viewModel.newsLiveData.observe(this, Observer {
            with(it) {
                tv_title.text = title
                tv_description.text = description
                tv_source.text = source
                tv_time.text = publishedAt
                tv_content.text = content
            }
            val requestOptions =
                RequestOptions()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerCrop()

            Glide.with(this)
                .load(it.urlToImage)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade())
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        @Nullable e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_load_photo.visibility = View.GONE
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        progress_load_photo.visibility = View.GONE
                        return false
                    }
                })
                .into(img_news)
        })
        viewModel.getConcreteNews(intent.getStringExtra(KEY_NEWS) ?: "")
    }
}
