package es.kapok.alegs0501.epostcards.models

import android.hardware.Camera

class AllFilters {

    companion object {
        val list: ArrayList<Filter> = arrayListOf(
                Filter(Camera.Parameters.EFFECT_NONE),
                Filter(Camera.Parameters.EFFECT_AQUA),
                Filter(Camera.Parameters.EFFECT_BLACKBOARD),
                Filter(Camera.Parameters.EFFECT_MONO),
                Filter(Camera.Parameters.EFFECT_NEGATIVE),
                Filter(Camera.Parameters.EFFECT_SOLARIZE),
                Filter(Camera.Parameters.EFFECT_POSTERIZE),
                Filter(Camera.Parameters.EFFECT_WHITEBOARD),
                Filter(Camera.Parameters.EFFECT_SEPIA)
        )
    }

}