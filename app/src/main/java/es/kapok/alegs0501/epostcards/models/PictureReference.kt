package es.kapok.alegs0501.epostcards.models

import java.io.File

class PictureReference {

    //Singleton to store picture data
    companion object {
        lateinit var data: ByteArray
        lateinit var file: File
    }


}