package com.voak.android.tasklist.views.activities

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import com.voak.android.tasklist.R
import com.voak.android.tasklist.adapters.ViewPagerAdapter
import com.voak.android.tasklist.base.BaseActivity
import com.voak.android.tasklist.presenters.DetailedTaskListPresenter
import com.voak.android.tasklist.utils.Constants
import com.voak.android.tasklist.views.fragments.ActiveTaskListFragment
import com.voak.android.tasklist.views.fragments.FinishedTaskListFragment
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IActivityCallback
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IDetailedTaskListView
import com.voak.android.tasklist.views.interfaces.detailed_task_list.IFragmentCallback

class DetailedTaskListActivity : BaseActivity(), IDetailedTaskListView, IActivityCallback {

    private lateinit var toolbar: MaterialToolbar
    private lateinit var viewPager: ViewPager
    private lateinit var tabLayout: TabLayout
    private lateinit var titleCount: TextView
    private lateinit var redBtn: View
    private lateinit var greenBtn: View
    private lateinit var orangeBtn: View
    private lateinit var blueBtn: View
    private lateinit var title: TextView

    private var activeTaskListCallback: IFragmentCallback? = null
    private var finishedTaskListCallback: IFragmentCallback? = null

    @InjectPresenter
    lateinit var presenter: DetailedTaskListPresenter

    override fun getLayoutRes(): Int {
        return R.layout.activity_detailed_task_list
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())

        toolbar = findViewById(R.id.toolbar)
        viewPager = findViewById(R.id.view_pager)
        tabLayout = findViewById(R.id.tabLayout)
        titleCount = findViewById(R.id.item_count)
        redBtn = findViewById(R.id.red_btn)
        greenBtn = findViewById(R.id.green_btn)
        orangeBtn = findViewById(R.id.orange_btn)
        blueBtn = findViewById(R.id.blue_btn)
        title = findViewById(R.id.toolbar_title)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        redBtn.setOnClickListener {
            presenter.onColoredBtnClicked(Constants.IMPORTANT_URGENT)
        }

        greenBtn.setOnClickListener {
            presenter.onColoredBtnClicked(Constants.IMPORTANT_NOT_URGENT)
        }

        orangeBtn.setOnClickListener {
            presenter.onColoredBtnClicked(Constants.NOT_IMPORTANT_URGENT)
        }

        blueBtn.setOnClickListener {
            presenter.onColoredBtnClicked(Constants.NOT_IMPORTANT_NOT_URGENT)
        }

        val type = intent.getIntExtra(EXTRA_TYPE, -1)

        val adapter = ViewPagerAdapter(supportFragmentManager, 0)

        val activeFragment = ActiveTaskListFragment.newInstance(type)
        activeFragment.setActivityCallback(this)
        activeTaskListCallback = activeFragment

        val finishedFragment = FinishedTaskListFragment.newInstance(type)
        finishedFragment.setActivityCallback(this)
        finishedTaskListCallback = finishedFragment


        adapter.addFragment(activeFragment, "Текущие")
        adapter.addFragment(finishedFragment, "Выполненные")

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0) {
                    presenter.onNeedUpdateTitleCount(activeTaskListCallback?.getTasksCount())
                } else {
                    presenter.onNeedUpdateTitleCount(finishedTaskListCallback?.getTasksCount())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        presenter.onCreate(type)
    }

    override fun updateFragmentsType(type: Int) {
        activeTaskListCallback?.updateType(type)
        finishedTaskListCallback?.updateType(type)
    }

    override fun setupViews(type: Int) {
        when (type) {
            Constants.IMPORTANT_URGENT -> {
                title.text = resources.getString(R.string.important_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed))
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorRed))
                tabLayout.setTabTextColors(
                    ContextCompat.getColor(this, android.R.color.white),
                    ContextCompat.getColor(this, R.color.colorRed)
                )
                redBtn.scaleX = 0.7f
                redBtn.scaleY = 0.7f
            }
            Constants.IMPORTANT_NOT_URGENT -> {
                title.text = resources.getString(R.string.important_not_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorGreen))
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorGreen))
                tabLayout.setTabTextColors(
                    ContextCompat.getColor(this, android.R.color.white),
                    ContextCompat.getColor(this, R.color.colorGreen)
                )
                greenBtn.scaleX = 0.7f
                greenBtn.scaleY = 0.7f
            }
            Constants.NOT_IMPORTANT_URGENT -> {
                title.text = resources.getString(R.string.not_important_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorOrange))
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorOrange))
                tabLayout.setTabTextColors(
                    ContextCompat.getColor(this, android.R.color.white),
                    ContextCompat.getColor(this, R.color.colorOrange)
                )
                orangeBtn.scaleX = 0.7f
                orangeBtn.scaleY = 0.7f
            }
            Constants.NOT_IMPORTANT_NOT_URGENT -> {
                title.text = resources.getString(R.string.not_important_not_urgent)
                toolbar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue))
                tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(this, R.color.colorBlue))
                tabLayout.setTabTextColors(
                    ContextCompat.getColor(this, android.R.color.white),
                    ContextCompat.getColor(this, R.color.colorBlue)
                )
                blueBtn.scaleX = 0.7f
                blueBtn.scaleY = 0.7f
            }
        }
    }

    override fun updateButtonsScale(type: Int) {
        when (type) {
            Constants.IMPORTANT_URGENT -> {
                redBtn.scaleX = 1f
                redBtn.scaleY = 1f
            }
            Constants.IMPORTANT_NOT_URGENT -> {
                greenBtn.scaleX = 1f
                greenBtn.scaleY = 1f
            }
            Constants.NOT_IMPORTANT_URGENT -> {
                orangeBtn.scaleX = 1f
                orangeBtn.scaleY = 1f
            }
            Constants.NOT_IMPORTANT_NOT_URGENT -> {
                blueBtn.scaleX = 1f
                blueBtn.scaleY = 1f
            }
        }
    }

    override fun updateTitleCount(count: Int?) {
        titleCount.text = count.toString()
    }

    override fun onActiveTasksLoaded(count: Int) {
        if (tabLayout.selectedTabPosition == 0) {
            updateTitleCount(count)
        }
    }

    override fun onFinishedTasksLoaded(count: Int) {
        if (tabLayout.selectedTabPosition == 1) {
            updateTitleCount(count)
        }
    }

    override fun detachCallback() {
        activeTaskListCallback = null
        finishedTaskListCallback = null
    }

    companion object {
        private const val EXTRA_TYPE =
            "com.voak.android.tasklist.activities.detailedtasklistactivity.type"

        fun newIntent(context: Context, type: Int): Intent {
            val intent = Intent(context, DetailedTaskListActivity::class.java)
            intent.putExtra(EXTRA_TYPE, type)

            return intent
        }
    }

}