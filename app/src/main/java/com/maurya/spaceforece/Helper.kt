package com.maurya.spaceforece

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.view.View
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.ln
import kotlin.math.pow


fun formatDuration(duration: Long): String {
    val minutes = TimeUnit.MINUTES.convert(duration, TimeUnit.MILLISECONDS)
    val seconds = TimeUnit.SECONDS.convert(duration, TimeUnit.MILLISECONDS) -
            TimeUnit.MINUTES.toSeconds(minutes)

    return String.format("%02d:%02d", minutes, seconds)
}

//song or songs count
fun updateTextViewWithItemCount(itemCount: Int): String {
    val itemCountText = if (itemCount == 1 || itemCount == 0) {
        "$itemCount song"
    } else {
        "$itemCount songs"
    }
    return itemCountText
}

//folder or folders count
fun updateTextViewWithFolderCount(itemCount: Int): String {
    val itemCountText = if (itemCount == 1 || itemCount == 0) {
        "$itemCount folder"
    } else {
        "$itemCount folders"
    }
    return itemCountText
}


//bottom nav animation
fun animationLayout(layout: LinearLayout) {
    val scaleAnimation = ScaleAnimation(
        0.8f,
        1.0f,
        1f,
        1f,
        Animation.RELATIVE_TO_SELF,
        0.0f,
        Animation.RELATIVE_TO_SELF,
        0.0f
    )
    scaleAnimation.duration = 200
    scaleAnimation.fillAfter = true
    layout.startAnimation(scaleAnimation)
}


//bottom Nav visibility after switching
fun setVisibility(vararg views: View) {
    for (view in views) {
        view.visibility = View.GONE
    }
}

//bottom nav drawableColor
fun drawableColorWhenUnSelecting(
    drawableId: Int,
    image: AppCompatImageView,
    layout: LinearLayout,
    context: Context
) {

    val drawable = ContextCompat.getDrawable(context, drawableId)
    val color = ContextCompat.getColor(context, R.color.ImageViewAndTextViewColour)
    when (drawable) {
        is ShapeDrawable -> {
            drawable.paint.color = color
        }

        is GradientDrawable -> {
            drawable.setStroke(5, color)
            drawable.setColor(color)
        }

        else -> {
            drawable?.setTint(color)
        }
    }
    image.setImageDrawable(drawable)
    layout.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
}

fun drawableColor(
    drawableId: Int,
    iconColor: Int,
    bgColor: Int,
    image: AppCompatImageView,
    layout: LinearLayout,
    text: TextView, context: Context
) {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    val color = ContextCompat.getColor(context, iconColor)
    when (drawable) {
        is ShapeDrawable -> {
            drawable.paint.color = color
        }

        is GradientDrawable -> {
            drawable.setStroke(5, color)
            drawable.setColor(color)
        }

        else -> {
            drawable?.setTint(color)
        }
    }
    image.setImageDrawable(drawable)
    val drawableBG = ContextCompat.getDrawable(context, R.drawable.background_nav_items)
    val colorBG = ContextCompat.getColor(context, bgColor)
    when (drawableBG) {
        is ShapeDrawable -> {
            drawableBG.paint.color = colorBG
        }

        else -> {
            drawableBG?.setTint(color)
        }
    }
    layout.setBackgroundResource(R.drawable.background_nav_items)
    text.visibility = View.VISIBLE
}

fun setBackgroundColor(context: Context, vararg views: View) {
    for (view in views) {
        view.setBackgroundColor(ContextCompat.getColor(context, R.color.transparent))
    }
}

fun fragmentManagerFun(supportFragmentManager: FragmentManager, fragment: Fragment) {
    supportFragmentManager.beginTransaction()
        .setReorderingAllowed(true)
        .replace(R.id.container, fragment::class.java, null)
        .commit()
}


//toast
fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


@SuppressLint("UseCompatTextViewDrawableApis")
fun setTextViewColorsForChangingSelection(
    context: Context,
    textViews: Array<TextView>,
    textColorId: Int,
    clickable: Boolean
) {
    val redColor = ContextCompat.getColor(context, textColorId)
    textViews.forEachIndexed { _, textView ->
        textView.setTextColor(redColor)
        textView.compoundDrawableTintList = ColorStateList.valueOf(redColor)
        textView.isClickable = clickable
    }
}

fun getFormattedDate(epochTime: Long): String {
    val date = Date(epochTime * 1000)
    val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())

    return dateFormat.format(date)
}