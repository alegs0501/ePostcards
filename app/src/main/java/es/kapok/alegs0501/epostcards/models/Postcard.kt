package es.kapok.alegs0501.epostcards.models

class Postcard {

    lateinit var front: ByteArray
    lateinit var back: ByteArray
    var id = 0

    constructor(front: ByteArray, back: ByteArray) {
        this.front = front
        this.back = back
    }

    constructor(front: ByteArray, back: ByteArray, id: Int) {
        this.front = front
        this.back = back
        this.id = id
    }


}