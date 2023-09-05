package by.bashlikovvv.data

sealed class Exceptions : RuntimeException() {

    class HotelNotFoundException : Exceptions()

    class ReservationNotFoundException : Exceptions()
}