package com.itis.group11801.fedotova.mvvmexample.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.itis.group11801.fedotova.mvvmexample.domain.NewsInteractor
import com.itis.group11801.fedotova.mvvmexample.domain.model.News
import com.itis.group11801.fedotova.mvvmexample.navigation.NewsRouter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val interactor: NewsInteractor,
    private val router: NewsRouter
) : BaseViewModel() {

    private val _newsLiveData = MutableLiveData<List<News>>()
    val newsLiveData: LiveData<List<News>> = _newsLiveData

    fun newsClicked(context: Context, newsUrl: String) {
        router.openNews(context, newsUrl)
    }

    fun getNews() {
        disposables.add(
            interactor.getNews()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    _newsLiveData.value = it
                }, {
                    it.printStackTrace()
                })
        )
    }
}
