package es.kapok.alegs0501.epostcards.models

import java.io.File

class PostcardReference {

    //Singleton to store postcard data
    companion object {
        lateinit var front: ByteArray
        lateinit var back: ByteArray
    }
}