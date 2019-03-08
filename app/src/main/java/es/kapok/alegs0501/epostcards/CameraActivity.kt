package es.kapok.alegs0501.epostcards

import android.content.Context
import android.content.Intent
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
import android.view.View
import android.widget.ImageButton
import es.kapok.alegs0501.epostcards.models.CameraPreferences
import es.kapok.alegs0501.epostcards.models.PictureReference
import kotlinx.android.synthetic.main.activity_camera.*
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import android.view.animation.LayoutAnimationController
import android.view.animation.Animation
import android.view.animation.TranslateAnimation
import android.view.animation.AnimationSet




class CameraActivity : AppCompatActivity() {

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    private val MEDIA_TYPE_IMAGE = 1
    private val MEDIA_TYPE_VIDEO = 2

    //Flash bar state
    private var hideBar = true


    private val mPicture = Camera.PictureCallback { data, _ ->
        val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
            Log.d("TAG", ("Error creating media file, check storage permissions"))
            return@PictureCallback
        }

        try {
            val fos = FileOutputStream(pictureFile)
            fos.write(data)
            fos.close()
            //Release camera when pic is saved
            releaseCamera()
            //Removes all views from preview to prevent app freeze
            camera_preview.removeAllViews()
            //reload camera preview
            preview()

            //Putting picture byte array in singleton
            PictureReference.data = data
            PictureReference.file = pictureFile

            //Launching Preview Activity
            val intent: Intent = Intent(this, PreviewActivity::class.java)
            startActivity(intent)

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

        //Hiding flash selectors
        flash_container.visibility = View.GONE

       if (checkCameraHardware(this)){

           //Starting camera preview
            preview()
           //Setting flash selected image
            setFlashImage()

           /** take picture action**/
           val captureButton: ImageButton = findViewById(R.id.button_capture)
           captureButton.setOnClickListener {
               // get an image from the camera
               mCamera?.takePicture(null, null, mPicture)

           }

           /**show/hide flash bar*/
           flash_selected.setOnClickListener{
               showFlashBar()
           }

           /**flash mode selection*/
           flash.setOnClickListener{
               setFlashMode(1)
               setFlashImage()
           }
           flash_auto.setOnClickListener{
               setFlashMode(2)
               setFlashImage()
           }
           flash_none.setOnClickListener{
               setFlashMode(3)
               setFlashImage()
           }

       }

    }



    override fun onPause() {
        super.onPause()
        camera_preview.removeAllViews()
        releaseCamera()
    }

    override fun onRestart(){
        super.onRestart()
        preview()
    }

    /**Release camera*/
    private fun releaseCamera() {
       mCamera?.apply {
           mCamera?.stopPreview()
           mCamera?.release()
           mCamera = null
       }
    }

    /**Create a camera instance and preview picture*/
    private fun preview(){
        //create an instance of camera
        mCamera = getCameraInstance()

        //Camera feature
        mCamera?.apply {
            parameters?.also {
                it.flashMode = CameraPreferences.flashMode
                it.focusMode = CameraPreferences.focusMode
                it.colorEffect = CameraPreferences.colorEffect
                parameters = it
            }
        }

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

    /**Animate flash panel selector*/
    private fun animatePanel(showing: Boolean) {
        val set = AnimationSet(true)
        var animation: Animation? = null
        if (showing) {
            //Animate from right to left
            animation = TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        } else {    //Animate left  to right
            animation = TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        }

        animation.duration = 500
        set.addAnimation(animation)
        val controller = LayoutAnimationController(set, 0.25f)

        flash_container.layoutAnimation = controller
        flash_container .startAnimation(animation)
    }

    /**Flash bar show/hide*/
    private fun showFlashBar(){
        animatePanel(hideBar)
        if (hideBar){
            flash_container.visibility = View.VISIBLE
        }else {
            flash_container.visibility = View.GONE
        }
        hideBar = !hideBar
    }

    /**Change Camera parameters*/
    private fun changeCameraParameters(){
        mCamera?.apply {
            parameters?.also {
                it.flashMode = CameraPreferences.flashMode
                it.focusMode = CameraPreferences.focusMode
                it.colorEffect = CameraPreferences.colorEffect
                parameters = it
            }
        }
    }

    /**Set flash mode*/
    private fun setFlashMode(mode: Int){
        when (mode){
            1 -> {
                CameraPreferences.flashMode = Camera.Parameters.FLASH_MODE_ON
                changeCameraParameters()
                showFlashBar()
            }
            2 -> {
                CameraPreferences.flashMode = Camera.Parameters.FLASH_MODE_AUTO
                changeCameraParameters()
                showFlashBar()
            }
            3 -> {
                CameraPreferences.flashMode = Camera.Parameters.FLASH_MODE_OFF
                changeCameraParameters()
                showFlashBar()
            }
            else -> {
                CameraPreferences.flashMode = Camera.Parameters.FLASH_MODE_ON
                changeCameraParameters()
                showFlashBar()
            }
        }
    }

    /**Image flash selected*/
    private fun setFlashImage(){

        when(CameraPreferences.flashMode){
            Camera.Parameters.FLASH_MODE_ON -> flash_selected.setImageResource(R.drawable.flash)
            Camera.Parameters.FLASH_MODE_AUTO -> flash_selected.setImageResource(R.drawable.flash_auto)
            Camera.Parameters.FLASH_MODE_OFF -> flash_selected.setImageResource(R.drawable.flash_none)
            else -> flash_selected.setImageResource(R.drawable.flash)
        }
    }





}

