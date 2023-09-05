package by.bashlikovvv.data

sealed class Exceptions : RuntimeException {

    constructor() : super()

    class HotelNotFoundException : Exceptions()

    class ReservationNotFoundException : Exceptions()
}