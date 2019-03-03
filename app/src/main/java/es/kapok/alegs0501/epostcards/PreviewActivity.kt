package es.kapok.alegs0501.epostcards

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import es.kapok.alegs0501.epostcards.models.PictureReference
import kotlinx.android.synthetic.main.activity_preview.*
import java.io.File

class PreviewActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Getting data from singleton class
        val data = PictureReference.data
        //Encoding byte array to bitmap
        val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
        //Setting bitmap on imageview
        imageView.setImageBitmap(bitmap)

    }
}
