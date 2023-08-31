package by.bashlikovvv.hotelreservation.data

open class Exceptions : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)
}

class HotelNotFoundException : Exceptions {

    constructor() : super()

    constructor(message: String) : super(message)
}