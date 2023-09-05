package by.bashlikovvv.hotelreservation.di

import by.bashlikovvv.domain.repository.IHotelRepository
import by.bashlikovvv.domain.repository.IReservationRepository
import by.bashlikovvv.domain.repository.IRoomsRepository
import by.bashlikovvv.domain.usecase.GetHotelByIdUseCase
import by.bashlikovvv.domain.usecase.GetReservationUseCase
import by.bashlikovvv.domain.usecase.GetRoomsUseCase
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