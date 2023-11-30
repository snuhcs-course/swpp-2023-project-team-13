package com.team13.fooriend.ui.screen

import android.content.Context
import android.widget.Toast

interface ReviewCountSubject {
    fun addObserver(observer: ReviewCountObserver)
    fun removeObserver(observer: ReviewCountObserver)
    fun notifyObservers()
}

class ReviewCountManager : ReviewCountSubject {
    private val observers: MutableList<ReviewCountObserver> = ArrayList()
    private var myReviewCount: Int = 0

    fun updateReviewCount(count: Int) {
        myReviewCount = count
        if (myReviewCount % 3 == 0 && myReviewCount > 0) {
            notifyObservers()
        }
    }

    override fun addObserver(observer: ReviewCountObserver) {
        observers.add(observer)
    }

    override fun removeObserver(observer: ReviewCountObserver) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        for (observer in observers) {
            observer.update(myReviewCount)
        }
    }
}

interface ReviewCountObserver {
    fun update(reviewCount: Int)
}

class ToastObserver(private val context: Context) : ReviewCountObserver {
    override fun update(reviewCount: Int) {
        Toast.makeText(context, "리뷰가 $reviewCount 개 등록되었습니다. 축하드립니다", Toast.LENGTH_SHORT).show()
    }
}