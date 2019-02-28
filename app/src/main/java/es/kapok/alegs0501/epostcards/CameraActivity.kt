package es.kapok.alegs0501.epostcards

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import es.kapok.alegs0501.epostcards.models.CameraPreview
import android.view.Display
import android.widget.Button
import android.widget.ImageButton
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class CameraActivity : AppCompatActivity() {

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    private val MEDIA_TYPE_IMAGE = 1
    private val MEDIA_TYPE_VIDEO = 2


    private val mPicture = Camera.PictureCallback { data, _ ->
        val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
            Log.d("TAG", ("Error creating media file, check storage permissions"))
            return@PictureCallback
        }

        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
        } catch (e: FileNotFoundException) {
            Log.d("TAG", "File not found: ${e.message}")
        } catch (e: IOException) {
            Log.d("TAG", "Error accessing file: ${e.message}")
        }
    }


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

           /** take picture action**/
           val captureButton: ImageButton = findViewById(R.id.button_capture)
           captureButton.setOnClickListener {
               // get an image from the camera
               mCamera?.takePicture(null, null, mPicture)
           }
       }

    }

    override fun onPause() {
        super.onPause()
        releaseCamera() // release the camera immediately on pause event
    }

    private fun releaseCamera() {
        mCamera?.release() // release the camera for other applications
        mCamera = null
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


    /** Create a file Uri for saving an image or video */
    private fun getOutputMediaFileUri(type: Int): Uri {
        return Uri.fromFile(getOutputMediaFile(type))
    }

    /** Create a File for saving an image or video */
    private fun getOutputMediaFile(type: Int): File? {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        val mediaStorageDir = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "ePostcard"
        )
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        mediaStorageDir.apply {
            if (!exists()) {
                if (!mkdirs()) {
                    Log.d("ePostcard", "failed to create directory")
                    return null
                }
            }
        }

        // Create a media file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        return when (type) {
            MEDIA_TYPE_IMAGE -> {
                File("${mediaStorageDir.path}${File.separator}IMG_$timeStamp.jpg")
            }
            MEDIA_TYPE_VIDEO -> {
                File("${mediaStorageDir.path}${File.separator}VID_$timeStamp.mp4")
            }
            else -> null
        }
    }







}

