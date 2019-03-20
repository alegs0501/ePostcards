package es.kapok.alegs0501.epostcards

import android.content.Intent
import android.graphics.*
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

        next_button.setOnClickListener{
            val intent = Intent(this, BackListActivity::class.java)
            startActivity(intent)
            this.finish()
        }

    }


    //Matrix color filter
    private fun getGrayscale(src: Bitmap): Bitmap {

        //Custom color matrix to convert to GrayScale
        val matrix = floatArrayOf(1f, 0f, 0f, 0f, 0f, 0f, 2f, 0f, 0f, 0f, 0f, 0f, 0f, 0.5f, 0f, 0f, 0f, 0f, 1f, 0f)

        val dest = Bitmap.createBitmap(
                src.width,
                src.height,
                src.config)

        val canvas = Canvas(dest)
        val paint = Paint()
        val filter = ColorMatrixColorFilter(matrix)
        paint.setColorFilter(filter)
        canvas.drawBitmap(src, 0f, 0f, paint)

        return dest
    }

}
