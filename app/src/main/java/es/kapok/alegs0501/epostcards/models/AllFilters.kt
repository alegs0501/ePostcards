package es.kapok.alegs0501.epostcards.models

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.hardware.Camera
import es.kapok.alegs0501.epostcards.R

/**Unused class*/
class AllFilters {

    companion object {
        val list: ArrayList<Filter> = arrayListOf(
                Filter(Camera.Parameters.EFFECT_NONE, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_none)),
                Filter(Camera.Parameters.EFFECT_AQUA, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_aqua)),
                Filter(Camera.Parameters.EFFECT_BLACKBOARD, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_blackboard)),
                Filter(Camera.Parameters.EFFECT_MONO, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_mono)),
                Filter(Camera.Parameters.EFFECT_NEGATIVE, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_negative)),
                Filter(Camera.Parameters.EFFECT_POSTERIZE, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_posterize)),
                Filter(Camera.Parameters.EFFECT_WHITEBOARD, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_whiteboard)),
                Filter(Camera.Parameters.EFFECT_SEPIA, BitmapFactory.decodeResource(Resources.getSystem(), R.drawable.filter_sepia))
        )
    }

}