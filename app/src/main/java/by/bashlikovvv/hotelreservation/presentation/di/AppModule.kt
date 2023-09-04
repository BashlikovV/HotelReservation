package by.bashlikovvv.hotelreservation.presentation.di

import by.bashlikovvv.hotelreservation.presentation.viewmodel.HotelFragmentViewModel
import by.bashlikovvv.hotelreservation.presentation.viewmodel.ReservationViewModel
import by.bashlikovvv.hotelreservation.presentation.viewmodel.RoomFragmentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel<HotelFragmentViewModel> {
        HotelFragmentViewModel(
            getHotelByIdUseCase = get()
        )
    }

    viewModel<RoomFragmentViewModel> {
        RoomFragmentViewModel(
            getRoomsUseCase = get()
        )
    }

    viewModel<ReservationViewModel> {
        ReservationViewModel(
            getReservationUseCase = get()
        )
    }
}