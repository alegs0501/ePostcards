package es.kapok.alegs0501.epostcards

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_edit_back.*

class EditBackActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_back)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        back_image_container.setImageResource(intent.getIntExtra("back_image", 0))

    }
}
