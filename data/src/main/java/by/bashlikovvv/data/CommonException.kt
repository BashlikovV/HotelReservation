package by.bashlikovvv.data

sealed class CommonException(cause: Throwable) : RuntimeException(cause) {

    class HotelNotFoundException(cause: Throwable = Throwable()) : CommonException(cause)

    class RoomsNotFoundException(cause: Throwable = Throwable()) : CommonException(cause)

    class ReservationNotFoundException(cause: Throwable = Throwable()) : CommonException(cause)
}