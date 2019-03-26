package es.kapok.alegs0501.epostcards

import android.gesture.GestureOverlayView
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import es.kapok.alegs0501.epostcards.models.PostcardReference
import kotlinx.android.synthetic.main.activity_view_postcard.*
import android.graphics.Bitmap
import android.graphics.Matrix
import android.opengl.Visibility
import android.util.Log
import android.view.MotionEvent
import android.view.animation.*
import kotlinx.android.synthetic.main.back_card.*


class ViewPostcardActivity : AppCompatActivity() {

    private var showing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_postcard)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        val frontBitmap = BitmapFactory.decodeByteArray(PostcardReference.front, 0, PostcardReference.front.size)
        postcard_front.setImageBitmap(frontBitmap)
        val backBitmap = BitmapFactory.decodeByteArray(PostcardReference.back, 0, PostcardReference.back.size)
        val newBackDimension = setDimensions(backBitmap, frontBitmap.width.toFloat(), frontBitmap.height.toFloat())
        postcard_back.setImageBitmap(newBackDimension)



        back_layout.visibility = View.GONE

        postcard_front.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN)
                showFrontBack()
            return@setOnTouchListener true
        }

        postcard_back.setOnClickListener{
            showFrontBack()
        }



    }

    //Animate Postcard
    private fun animateCard(showing: Boolean){

        if(showing){
            postcard_back.visibility = View.GONE

            val setFront = AnimationSet(true)
            var controllerFront = LayoutAnimationController(setFront, 0f)


            var animationFront: Animation = if (!showing) {
                ScaleAnimation(0f, 1f, 1f, 1f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            } else {
                ScaleAnimation(1f, 0f, 1f, 1f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            }

            animationFront.duration = 500
            setFront.addAnimation(animationFront)


            front_layout.layoutAnimation = controllerFront
            front_layout.startAnimation(animationFront)

            front_layout.layoutAnimation.animation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                    Log.d("TAG", "onAnimationRepeat")
                }

                override fun onAnimationEnd(animation: Animation?) {
                    postcard_back.visibility = View.VISIBLE
                    val setBack = AnimationSet(true)
                    var controllerBack = LayoutAnimationController(setBack, 500f)
                    var animationBack: Animation = if (showing) {
                        ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    } else {
                        ScaleAnimation(1f, 0f, 1f, 1f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    }
                    animationBack.duration = 500
                    setBack.addAnimation(animationBack)
                    back_layout.layoutAnimation = controllerBack
                    back_layout.startAnimation((animationBack))
                }

                override fun onAnimationStart(animation: Animation?) {
                    Log.d("TAG", "onAnimationStart")
                }


            })
        } else {
            postcard_front.visibility = View.GONE

            val setBack = AnimationSet(true)
            var controllerBack = LayoutAnimationController(setBack, 0f)


            var animationBack: Animation = if (!showing) {
                ScaleAnimation(1f, 0f, 1f, 1f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            } else {
                ScaleAnimation(0f, 1f, 1f, 1f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            }

            animationBack.duration = 500
            setBack.addAnimation(animationBack)


            back_layout.layoutAnimation = controllerBack
            back_layout.startAnimation(animationBack)

            back_layout.layoutAnimation.animation.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationRepeat(animation: Animation?) {
                    Log.d("TAG", "onAnimationRepeat")
                }

                override fun onAnimationEnd(animation: Animation?) {
                    postcard_front.visibility = View.VISIBLE
                    val setFront = AnimationSet(true)
                    var controllerFront = LayoutAnimationController(setFront, 500f)
                    var animationFront: Animation = if (!showing) {
                        ScaleAnimation(0f, 1f, 1f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    } else {
                        ScaleAnimation(1f, 0f, 1f, 1f,  Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
                    }
                    animationFront.duration = 500
                    setFront.addAnimation(animationFront)
                    front_layout.layoutAnimation = controllerFront
                    front_layout.startAnimation((animationFront))
                }

                override fun onAnimationStart(animation: Animation?) {
                    Log.d("TAG", "onAnimationStart")
                }


            })
        }


    }

    /**Front/Back show/hide*/
    private fun showFrontBack(){
       animateCard(showing)
        if (showing){
            front_layout.visibility = View.GONE
            back_layout.visibility = View.VISIBLE

        }else {
            back_layout.visibility = View.GONE
            front_layout.visibility = View.VISIBLE
        }
        showing = !showing
    }

    fun setDimensions(mBitmap: Bitmap, newWidth: Float, newHeigth: Float): Bitmap {
        //Set dimensions
        val width = mBitmap.width
        val height = mBitmap.height
        val scaleWidth = newWidth / width
        val scaleHeight = newHeigth / height
        // create a matrix for the manipulation
        val matrix = Matrix()
        // resize the bit map
        matrix.postScale(scaleWidth, scaleHeight)
        // recreate the new Bitmap
        return Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, false)
    }


}
