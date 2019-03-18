package es.kapok.alegs0501.epostcards

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.LayoutAnimationController
import android.view.animation.TranslateAnimation
import android.widget.SeekBar
import kotlinx.android.synthetic.main.activity_camera.*
import kotlinx.android.synthetic.main.activity_edit_back.*

class EditBackActivity : AppCompatActivity() {

    private var hiddenPanel = true
    var red = 0
    var green = 0
    var blue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_back)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Setting image from extra
        back_image_container.setImageResource(intent.getIntExtra("back_image", 0))

        //Hiding editor panel
        editor_container.visibility = View.GONE

        //Moving text on image
        sample_text.setOnTouchListener(View.OnTouchListener{ v: View?, event: MotionEvent? ->

            var lastX: Float = 0f
            var lastY: Float = 0f

            when (event!!.action){
                MotionEvent.ACTION_DOWN ->{
                    lastX = event.x
                    lastY = event.y
                }
                MotionEvent.ACTION_MOVE ->{
                    val dx = event.x - lastX
                    val dy = event.y - lastY
                    val finalX = v!!.x + dx - v.width /2
                    val finalY = v.y + dy - v.height /2
                    v.x = finalX
                    v.y = finalY
                }
            }
            return@OnTouchListener true
        })

        //Showing edit panel
        edit_text_button.setOnClickListener{
            if (hiddenPanel){
                animatePanel(false)
                editor_container.visibility = View.VISIBLE
                hiddenPanel = false
            }
        }

        //Hiding edit panel
        ok_button.setOnClickListener{
            if (!hiddenPanel){
                animatePanel(true)
                editor_container.visibility = View.GONE
                hiddenPanel = true
            }
        }

        //Editing text
        edit_sample_text.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isNotEmpty())
                    sample_text.text = edit_sample_text.text.toString()
                else sample_text.text = ""
            }

        })

        //Font size control
        seekBarSize.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sample_text.textSize = progress.toFloat()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        //Font spacing control
        seekBarSpacing.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                sample_text.setLineSpacing(progress.toFloat(),1f)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        //Red control
        seekBarR.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                red = progress
                sample_text.setTextColor(Color.rgb(red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        //Green control
        seekBarG.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                green = progress
                sample_text.setTextColor(Color.rgb(red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

        //Blue control
        seekBarB.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                blue = progress
                sample_text.setTextColor(Color.rgb(red, green, blue))
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }

        })

    }

    /**Animate text editor panel*/
    private fun animatePanel(showing: Boolean) {
        val set = AnimationSet(true)
        var animation: Animation = if (showing) {
            //Animate from up to down
            TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f)
        } else {    //Animate from down to up
            TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        }

        animation.duration = 500
        set.addAnimation(animation)
        val controller = LayoutAnimationController(set, 0.25f)

        editor_container.layoutAnimation = controller
        editor_container.startAnimation(animation)
    }

}
