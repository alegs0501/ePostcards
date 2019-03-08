package es.kapok.alegs0501.epostcards.models

import android.hardware.Camera

class CameraPreferences {


    //Singleton to store camera preferences
    companion object {
        var flashMode = Camera.Parameters.FLASH_MODE_ON
        var focusMode = Camera.Parameters.FOCUS_MODE_AUTO
        var colorEffect = Camera.Parameters.EFFECT_NONE
    }

}