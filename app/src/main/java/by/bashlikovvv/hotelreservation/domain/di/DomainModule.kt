package by.bashlikovvv.hotelreservation.domain.di

import by.bashlikovvv.hotelreservation.domain.repository.IHotelRepository
import by.bashlikovvv.hotelreservation.domain.repository.IReservationRepository
import by.bashlikovvv.hotelreservation.domain.repository.IRoomsRepository
import by.bashlikovvv.hotelreservation.domain.usecase.GetHotelByIdUseCase
import by.bashlikovvv.hotelreservation.domain.usecase.GetReservationUseCase
import by.bashlikovvv.hotelreservation.domain.usecase.GetRoomsUseCase
import org.koin.dsl.module

val domainModule = module {

    factory<GetHotelByIdUseCase> {
        val hotelRepository: IHotelRepository = get()

        GetHotelByIdUseCase(hotelRepository)
    }

    factory<GetRoomsUseCase> {
        val roomsRepository: IRoomsRepository = get()

        GetRoomsUseCase(roomsRepository)
    }

    factory<GetReservationUseCase> {
        val reservationRepository: IReservationRepository = get()

        GetReservationUseCase(reservationRepository)
    }

}