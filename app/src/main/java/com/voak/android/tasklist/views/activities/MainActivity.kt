package com.voak.android.tasklist.views.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.voak.android.tasklist.R
import com.voak.android.tasklist.base.BaseActivity
import com.voak.android.tasklist.presenters.MainActivityPresenter
import com.voak.android.tasklist.utils.Constants
import com.voak.android.tasklist.views.interfaces.IMainView

class MainActivity : BaseActivity(), IMainView {

    @InjectPresenter
    lateinit var presenter: MainActivityPresenter

    private lateinit var addTaskBtn: FloatingActionButton
    private lateinit var redBtn: FloatingActionButton
    private lateinit var greenBtn: FloatingActionButton
    private lateinit var orangeBtn: FloatingActionButton
    private lateinit var blueBtn: FloatingActionButton
    private lateinit var darkLayout: LinearLayout

    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())

        if (savedInstanceState == null) {
            viewRouter.setUpMainActivityFragments()
        }

        addTaskBtn = findViewById(R.id.addTaskFab)
        redBtn = findViewById(R.id.red_btn)
        greenBtn = findViewById(R.id.green_btn)
        orangeBtn = findViewById(R.id.orange_btn)
        blueBtn = findViewById(R.id.blue_btn)
        darkLayout = findViewById(R.id.faded_layout)


        addTaskBtn.setOnClickListener {
            presenter.onAddTaskBtnAction()
        }

        redBtn.setOnClickListener {
            presenter.onColoredBtnAction(Constants.IMPORTANT_URGENT)
        }

        greenBtn.setOnClickListener {
            presenter.onColoredBtnAction(Constants.IMPORTANT_NOT_URGENT)
        }

        orangeBtn.setOnClickListener {
            presenter.onColoredBtnAction(Constants.NOT_IMPORTANT_URGENT)
        }

        blueBtn.setOnClickListener {
            presenter.onColoredBtnAction(Constants.NOT_IMPORTANT_NOT_URGENT)
        }

    }

    override fun startAnimation(needFade: Boolean) {
        if (needFade) {
            val fade = ObjectAnimator.ofFloat(darkLayout, "alpha", 0.0f, 1.0f)
                .setDuration(500)

            val f1 = ObjectAnimator.ofFloat(redBtn, "alpha", 0.0f, 1.0f)
                .setDuration(500)
            val f2 = ObjectAnimator.ofFloat(greenBtn, "alpha", 0.0f, 1.0f)
                .setDuration(500)
            val f3 = ObjectAnimator.ofFloat(orangeBtn, "alpha", 0.0f, 1.0f)
                .setDuration(500)
            val f4 = ObjectAnimator.ofFloat(blueBtn, "alpha", 0.0f, 1.0f)
                .setDuration(500)

            val fabRotate = ObjectAnimator.ofFloat(addTaskBtn, "rotation", 0f, 45f)
                .setDuration(500)

            darkLayout.visibility = View.VISIBLE
            redBtn.visibility = View.VISIBLE
            greenBtn.visibility = View.VISIBLE
            orangeBtn.visibility = View.VISIBLE
            blueBtn.visibility = View.VISIBLE

            val anim = AnimatorSet()
            anim.play(fade)
                .with(fabRotate)
                .with(f1)
                .with(f2)
                .with(f3)
                .with(f4)
            anim.start()
        } else {
            val fade = ObjectAnimator.ofFloat(darkLayout, "alpha", 1.0f, 0.0f)
                .setDuration(500)

            val fabRotate = ObjectAnimator.ofFloat(addTaskBtn, "rotation", 45f, 90f)
                .setDuration(500)

            val f1 = ObjectAnimator.ofFloat(redBtn, "alpha", 1.0f, 0.0f)
                .setDuration(500)
            val f2 = ObjectAnimator.ofFloat(greenBtn, "alpha", 1.0f, 0.0f)
                .setDuration(500)
            val f3 = ObjectAnimator.ofFloat(orangeBtn, "alpha", 1.0f, 0.0f)
                .setDuration(500)
            val f4 = ObjectAnimator.ofFloat(blueBtn, "alpha", 1.0f, 0.0f)
                .setDuration(500)

            val f1h = ObjectAnimator.ofInt(redBtn, "visibility", View.VISIBLE, View.GONE)
            val f2h = ObjectAnimator.ofInt(greenBtn, "visibility", View.VISIBLE, View.GONE)
            val f3h = ObjectAnimator.ofInt(orangeBtn, "visibility", View.VISIBLE, View.GONE)
            val f4h = ObjectAnimator.ofInt(blueBtn, "visibility", View.VISIBLE, View.GONE)


            val anim = AnimatorSet()
            anim.play(fade)
                .with(fabRotate)
                .with(f1)
                .with(f2)
                .with(f3)
                .with(f4)
                .before(f1h)
                .before(f2h)
                .before(f3h)
                .before(f4h)
            anim.start()
        }
    }

    override fun openTaskActivity(type: Int) {
        startActivity(TaskActivity.newIntent(this, type))
    }
}