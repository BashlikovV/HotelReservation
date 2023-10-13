package by.bashlikovvv.hotelreservation.presentation.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.core.view.updateLayoutParams
import by.bashlikovvv.hotelreservation.R

class ExpandableLayout @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : CardView(context, attrs) {

    private lateinit var titleView: View
    private lateinit var contentLayout: ViewGroup
    private lateinit var arrow: View

    private val listeners = mutableListOf<(Boolean) -> Unit>()

    init {
        this.elevation = 0f
    }

    private var expanded = false

    private var animating = false

    private var animDuration = 500L

    private val expandAnimator: ValueAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
        duration = animDuration
        addUpdateListener {
            val progress = it.animatedValue as Float
            val wrapContentHeight = contentLayout.measureWrapContentHeight()
            contentLayout.updateLayoutParams {
                height = (wrapContentHeight * progress).toInt()
            }

            arrow.rotation = progress * 180
        }
        addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                animating = true
            }

            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                animating = false
            }
        })
    }


    override fun onFinishInflate() {
        super.onFinishInflate()
        val parentLayout = getChildAt(0) as ViewGroup

        titleView = parentLayout.getChildAt(0)
        contentLayout = parentLayout.getChildAt(1) as ViewGroup
        arrow = parentLayout.findViewById(R.id.arrowImageView)

        if (expanded) {
            arrow.rotation = 180f
        } else {
            contentLayout.updateLayoutParams {
                height = 0
            }
            arrow.rotation = 0f
        }

        arrow.setOnClickListener {
            expanded = when {
                animating -> {
                    expandAnimator.reverse()
                    !expanded
                }
                expanded -> {
                    expandAnimator.reverse()
                    false
                }
                else -> {
                    expandAnimator.start()
                    true
                }
            }
            listeners.onEach { it.invoke(expanded) }
        }
    }

    private fun ViewGroup.measureWrapContentHeight(): Int {
        this.measure(
            MeasureSpec
                .makeMeasureSpec((this.parent as View).measuredWidth, MeasureSpec.EXACTLY),
            MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
        return measuredHeight
    }

    fun isExpanded() = this.expanded

    fun addOnClickListener(listener: (Boolean) -> Unit) {
        listeners.add(listener)
    }
}