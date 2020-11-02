package com.voak.android.tasklist.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import com.arellomobile.mvp.MvpAppCompatActivity
import com.voak.android.tasklist.navigation.ViewRouter
import javax.inject.Inject

abstract class BaseActivity : MvpAppCompatActivity() {

    @Inject lateinit var viewRouter: ViewRouter

    @LayoutRes
    protected open fun getLayoutRes(): Int {
        return -1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())

        BaseApp.instance.component.inject(this)

        viewRouter.currentActivity = this
    }

    override fun onDestroy() {
        super.onDestroy()
        viewRouter.currentActivity = null
    }

}