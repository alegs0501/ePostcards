package es.kapok.alegs0501.epostcards

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_edit_back.*

class EditBackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_back)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Setting image from extra
        back_image_container.setImageResource(intent.getIntExtra("back_image", 0))

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
                    val finalX = v!!.x + dx - v.width
                    val finalY = v.y + dy - v.height
                    v.x = finalX
                    v.y = finalY
                }
            }
            return@OnTouchListener true
        })

    }
}
