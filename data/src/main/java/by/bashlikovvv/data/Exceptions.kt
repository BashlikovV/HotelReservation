package by.bashlikovvv.data

sealed class Exceptions : RuntimeException {

    constructor() : super()

    constructor(message: String) : super(message)

    class HotelNotFoundException : Exceptions()

    class ReservationNotFoundException : Exceptions()
}