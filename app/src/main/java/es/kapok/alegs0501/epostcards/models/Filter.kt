package es.kapok.alegs0501.epostcards.models

import android.graphics.Bitmap

class Filter {
    var name_filter: String? = null
    var image: Bitmap? = null

    constructor(name_filter: String?, image: Bitmap) {
        this.name_filter = name_filter
        this.image = image
    }
}

