package es.kapok.alegs0501.epostcards.models

import android.hardware.Camera
import java.util.*

class CameraPreferences {


    //Singleton to store camera preferences
    companion object {
        var flashMode = Camera.Parameters.FLASH_MODE_OFF
        var focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        var colorEffect = Camera.Parameters.EFFECT_NONE
    }

}