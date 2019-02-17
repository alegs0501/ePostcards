package es.kapok.alegs0501.epostcards

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Point
import android.hardware.Camera
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import es.kapok.alegs0501.epostcards.models.CameraPreview
import java.nio.file.Files.size
import android.opengl.ETC1.getHeight
import android.opengl.ETC1.getWidth
import android.view.Display
import kotlinx.android.synthetic.main.activity_camera.*
import java.util.*


class CameraActivity : AppCompatActivity() {

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

       if (checkCameraHardware(this)){
           //create an instance of camera
           mCamera = getCameraInstance()
           adjustPreview()

           mPreview = mCamera?.let {
               //Create a preview view
               CameraPreview(this,it)
           }

           //Set Preview view as rhe content of activity
           mPreview?.also {
               val preview: FrameLayout = findViewById(R.id.camera_preview)
               preview.addView(it)
           }
       }




    }

    /** Check if this device has a camera */
    private fun checkCameraHardware(context: Context): Boolean {
        return context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA)
    }

    /** A safe way to get an instance of the Camera object. */
    fun getCameraInstance(): Camera?{
        return try {
            Camera.open()
        }catch (e: Exception){
            null
        }
    }

    /** Change width of camera preview to fit image on it*/
    fun adjustPreview(){
        //Get dimension of the screen
        val display: Display = windowManager.defaultDisplay
        val displayWidth = display.width
        val displayHeight = display.height
        val camWidth = mCamera!!.parameters.previewSize.width
        val camHeight = mCamera!!.parameters.previewSize.height

        //Set camera preview width by rule of 3
        val newWidth = camWidth * displayHeight / camHeight

        if (newWidth < displayWidth){
            camera_preview.layoutParams.width = newWidth
        }

    }



}

