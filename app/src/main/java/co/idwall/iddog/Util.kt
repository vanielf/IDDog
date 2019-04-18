package co.idwall.iddog

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.content.Context
import android.graphics.Point
import android.graphics.Rect
import android.preference.PreferenceManager
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.ImageView


class Util {

    companion object {

        private var tokenHolder: String? = null

        /**
         * Save user token
         *
         * @param context The application context to use
         * @param token The token to save
         */
        fun setToken(context: Context, token: String?) {
            tokenHolder = token
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            with(preferences.edit()) {
                putString("token", token)
                apply()
            }
        }

        /**
         * Get user token
         *
         * @param context The application context to use
         */
        fun getToken(context: Context): String? {
            if (tokenHolder == null) {
                tokenHolder = PreferenceManager.getDefaultSharedPreferences(context).getString("token", null)
            }
            return tokenHolder
        }


        /**
         * Shows the progress UI and hides the view informed.
         *
         * @param context The application context to use
         * @param show To show or hide progress
         * @param loading The loading view to show/hide
         * @param [viewToHide] The view to hide while loading
         */
        fun showProgress(context: Context, show: Boolean, loading: View, viewToHide: View?) {

            val shortAnimTime = context.resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            viewToHide?.visibility = if (show) View.GONE else View.VISIBLE
            viewToHide?.animate()
                ?.setDuration(shortAnimTime)
                ?.alpha((if (show) 0 else 1).toFloat())
                ?.setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        viewToHide.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            loading.visibility = if (show) View.VISIBLE else View.GONE
            loading.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        loading.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
        }


        /**
         * Show the expanded image with effect
         * Adapted from: https://developer.android.com/training/animation/zoom
         *
         * @param context The activity view
         * @param thumbView The image to expand
         */
        fun zoomImageFromThumb(context: Activity, thumbView: ImageView) {

            val shortAnimationDuration = 300

            // Load the high-resolution "zoomed-in" image.
            val expandedContainer: View = context.findViewById(R.id.expanded_container)
            val expandedImageView: ImageView = context.findViewById(R.id.expanded_image)
            expandedImageView.setImageDrawable(thumbView.drawable)

            val startBounds = Rect()
            val finalBounds = Rect()
            val globalOffset = Point()

            thumbView.getGlobalVisibleRect(startBounds)
            expandedContainer.getGlobalVisibleRect(finalBounds, globalOffset)

            startBounds.offset(-globalOffset.x, -globalOffset.y)
            finalBounds.offset(-globalOffset.x, -globalOffset.y)

            val startScale = startBounds.width().toFloat() / finalBounds.width()
            val startHeight = startScale * finalBounds.height()
            val deltaHeight = (startHeight - startBounds.height()) / 2
            startBounds.top -= deltaHeight.toInt()
            startBounds.bottom += deltaHeight.toInt()

            expandedImageView.pivotX = 0f
            expandedImageView.pivotY = 0f

            thumbView.alpha = 0f
            expandedContainer.visibility = View.VISIBLE
            AnimatorSet().apply {
                play(
                    ObjectAnimator.ofFloat(
                        expandedImageView,
                        View.X,
                        startBounds.left.toFloat(),
//                        finalBounds.left.toFloat()
                        (70).toFloat()
                    )
                )
                    .with(
                        ObjectAnimator.ofFloat(
                            expandedImageView,
                            View.Y,
                            startBounds.top.toFloat(),
                            finalBounds.top.toFloat()
                        )
                    )
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale, 1f))
                    .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale, 1f))

                duration = shortAnimationDuration.toLong()
                interpolator = DecelerateInterpolator()
                start()
            }


            expandedContainer.setOnClickListener {
                AnimatorSet().apply {
                    play(ObjectAnimator.ofFloat(expandedImageView, View.X, startBounds.left.toFloat()))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.Y, startBounds.top.toFloat()))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_X, startScale))
                        .with(ObjectAnimator.ofFloat(expandedImageView, View.SCALE_Y, startScale))
                    duration = shortAnimationDuration.toLong()
                    interpolator = DecelerateInterpolator()
                    addListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            thumbView.alpha = 1f
                            expandedContainer.visibility = View.GONE
                        }

                        override fun onAnimationCancel(animation: Animator) {
                            thumbView.alpha = 1f
                            expandedContainer.visibility = View.GONE
                        }
                    })
                    start()
                }
            }
        }

    }

}