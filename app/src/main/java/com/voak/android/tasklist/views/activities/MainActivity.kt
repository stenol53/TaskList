package com.voak.android.tasklist.views.activities

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.FrameLayout
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
    private lateinit var frame1: FrameLayout
    private lateinit var frame2: FrameLayout
    private lateinit var frame3: FrameLayout
    private lateinit var frame4: FrameLayout


    override fun getLayoutRes(): Int {
        return R.layout.activity_main
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutRes())


        if (savedInstanceState == null) {
            viewRouter.setUpMainActivityFragments()
        }

        addTaskBtn = findViewById(R.id.addTaskFab)
        addTaskBtn.tag = "addTaskBtn_tag"

        frame1 = findViewById(R.id.frame_1)
        frame2 = findViewById(R.id.frame_2)
        frame3 = findViewById(R.id.frame_3)
        frame4 = findViewById(R.id.frame_4)

        frame1.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DROP -> {
                    presenter.onDropActionTrue(Constants.IMPORTANT_URGENT)
                    return@setOnDragListener true
                }
                DragEvent.ACTION_DRAG_STARTED, DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_LOCATION, DragEvent.ACTION_DRAG_ENDED, DragEvent.ACTION_DRAG_EXITED-> {
                    return@setOnDragListener true
                }
                else -> {
                    presenter.onDropActionFalse()
                    return@setOnDragListener false
                }
            }
        }

        frame2.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DROP -> {
                    presenter.onDropActionTrue(Constants.IMPORTANT_NOT_URGENT)
                    return@setOnDragListener true
                }
                DragEvent.ACTION_DRAG_STARTED, DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_LOCATION, DragEvent.ACTION_DRAG_ENDED, DragEvent.ACTION_DRAG_EXITED-> {
                    return@setOnDragListener true
                }
                else -> {
                    presenter.onDropActionFalse()
                    return@setOnDragListener false
                }
            }
        }

        frame3.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DROP -> {
                    Log.i(this::class.simpleName, "dropped")
                    presenter.onDropActionTrue(Constants.NOT_IMPORTANT_URGENT)
                    return@setOnDragListener true
                }
                DragEvent.ACTION_DRAG_STARTED, DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_LOCATION, DragEvent.ACTION_DRAG_ENDED, DragEvent.ACTION_DRAG_EXITED-> {
                    return@setOnDragListener true
                }
                else -> {
                    presenter.onDropActionFalse()
                    return@setOnDragListener false
                }
            }
        }

        frame4.setOnDragListener { view, dragEvent ->
            when (dragEvent.action) {
                DragEvent.ACTION_DROP -> {
                    Log.i(this::class.simpleName, "dropped")
                    presenter.onDropActionTrue(Constants.NOT_IMPORTANT_NOT_URGENT)
                    return@setOnDragListener true
                }
                DragEvent.ACTION_DRAG_STARTED, DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_LOCATION, DragEvent.ACTION_DRAG_ENDED, DragEvent.ACTION_DRAG_EXITED-> {
                    return@setOnDragListener true
                }
                else -> {
                    presenter.onDropActionFalse()
                    return@setOnDragListener false
                }
            }
        }

        addTaskBtn.setOnTouchListener { view, motionEvent ->
            val item = ClipData.Item(view.tag as CharSequence)

            // Create a new ClipData using the tag as a label, the plain text MIME type, and
            // the already-created item. This will create a new ClipDescription object within the
            // ClipData, and set its MIME type entry to "text/plain"
            val mimeTypes = arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN)
            val data = ClipData(view.tag.toString(), mimeTypes, item)

            // Instantiates the drag shadow builder.
            val dragshadow = View.DragShadowBuilder(view)

            presenter.onAddTaskBtnAction()
            // Starts the drag
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                view.startDragAndDrop(data, dragshadow, view, 0)
            } else {
                view.startDrag(data // data to be dragged
                    , dragshadow // drag shadow
                    , view // local data about the drag and drop operation
                    , 0 // flags set to 0 because not using currently
                )
            }

            return@setOnTouchListener true
        }


    }

    override fun changeAddBtnVisibility(visibility: Boolean) {
        if (visibility) {
            addTaskBtn.visibility = View.VISIBLE
        } else {
            addTaskBtn.visibility = View.GONE
        }
    }

    override fun openTaskActivity(type: Int) {
        startActivity(TaskActivity.newIntent(this, type))
    }
}