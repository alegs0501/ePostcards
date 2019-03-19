package es.kapok.alegs0501.epostcards

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Camera
import android.net.Uri
import android.opengl.Visibility
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.WindowManager
import android.widget.FrameLayout
import android.view.Display
import android.view.View
import android.widget.ImageButton
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
import android.widget.AdapterView
import android.widget.Toast
import es.kapok.alegs0501.epostcards.data.FilterListAdapter
import es.kapok.alegs0501.epostcards.models.*
import kotlinx.android.synthetic.main.filter_card.*
import kotlin.collections.ArrayList


class CameraActivity : AppCompatActivity() {

    private var mCamera: Camera? = null
    private var mPreview: CameraPreview? = null

    private val MEDIA_TYPE_IMAGE = 1
    private val MEDIA_TYPE_VIDEO = 2

    //Flash bar state
    private var hideBar = true

    //Filters bar state
    private var hideFilterBar = true

    //Filters
    private var adapter: FilterListAdapter? = null
    private var filterList: ArrayList<Filter>? = null
    private var layoutManager: RecyclerView.LayoutManager? = null


    private val mPicture = Camera.PictureCallback { data, _ ->
        //this block was used to saved image on SD card
        /**val pictureFile: File = getOutputMediaFile(MEDIA_TYPE_IMAGE) ?: run {
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
        }*/

        //Release camera when pic is saved
        releaseCamera()
        //Removes all views from preview to prevent app freeze
        camera_preview.removeAllViews()
        //reload camera preview
        preview()

        //Putting picture byte array in singleton
        PictureReference.data = data

        //Launching Preview Activity
        val intent: Intent = Intent(this, PreviewActivity::class.java)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)

        //Hiding status bar
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        //Hiding filters bar
        myRecyclerView.visibility = View.GONE

        //Hiding flash selectors
        flash_container.visibility = View.GONE

       if (checkCameraHardware(this)){

           //Starting camera preview
            preview()

           //Setting flash selected image
           if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)){
               setFlashImage()
           }else flash.visibility = View.GONE

            //Adapter and layout for filter
           filterList = ArrayList()
           layoutManager = LinearLayoutManager(this)
           adapter = FilterListAdapter(filterList!!, this)


           //Card view listener
           adapter!!.setOnclickListener(object: FilterListAdapter.OnItemClickListener{
               override fun onItemClick(position: Int) {
                   CameraPreferences.colorEffect = filterList!![position].name_filter!!
                   changeCameraParameters()
               }
           })

           myRecyclerView.layoutManager = LinearLayoutManager(this, OrientationHelper.HORIZONTAL, false)
           myRecyclerView.adapter = adapter

           //Supported camera filters
           var availableFilters = mCamera?.parameters!!.supportedColorEffects

           //if exists filters
           if (availableFilters.size > 0){
               for (i:Int in 0 until availableFilters.size){
                   //select index from filter
                   val index = findFilter(AllFilters.list, availableFilters[i])
                   //if filter is available
                   if (index != -1 && findFilter(filterList!!, availableFilters[i]) == -1){
                       //add filter to use
                       filterList?.add(AllFilters.list[index])
                   }
               }
           }

            adapter!!.notifyDataSetChanged()



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

           /**show/hide filters bar*/
           filter_show_button.setOnClickListener{
               showFilterBar()
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
        var animation: Animation = if (showing) {
            //Animate from right to left
            TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        } else {    //Animate left  to right
            TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f)
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

    /**Animate filters bar*/
    private fun animateFilterPanel(showing: Boolean) {
        val set = AnimationSet(true)
        var animation: Animation = if (showing) {
            //Animate from right to left
            TranslateAnimation(Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        } else {    //Animate left  to right
            TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 1.0f, Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f)
        }

        animation.duration = 500
        set.addAnimation(animation)
        val controller = LayoutAnimationController(set, 0.25f)

        myRecyclerView.layoutAnimation = controller
        myRecyclerView.startAnimation(animation)
    }

    /**Flash bar show/hide*/
    private fun showFilterBar(){
        animateFilterPanel(hideFilterBar)
        if (hideFilterBar){
            myRecyclerView.visibility = View.VISIBLE
        }else {
            myRecyclerView.visibility = View.GONE
        }
        hideFilterBar = !hideFilterBar
    }

    /**Change Camera parameters*/
    fun changeCameraParameters(){
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

    /**Finding a filter in a list*/
    private fun findFilter(list: ArrayList<Filter>, name: String): Int{
        var index = -1
        var counter = 0
        var found = false

        while (counter < list.size && !found){
            if (list[counter].name_filter == name){
                found = true
                index = counter
            }
            counter ++
        }
        return index
    }





}

