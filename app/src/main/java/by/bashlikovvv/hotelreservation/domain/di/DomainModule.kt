package by.bashlikovvv.hotelreservation.domain.di

import by.bashlikovvv.hotelreservation.domain.repository.IHotelRepository
import by.bashlikovvv.hotelreservation.domain.repository.IRoomsRepository
import by.bashlikovvv.hotelreservation.domain.usecase.GetHotelByIdUseCase
import by.bashlikovvv.hotelreservation.domain.usecase.GetRoomsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideGetHotelByIdUseCase(hotelRepository: IHotelRepository): GetHotelByIdUseCase {
        return GetHotelByIdUseCase(hotelRepository)
    }

    @Provides
    fun provideGetRoomsUseCase(roomsRepository: IRoomsRepository): GetRoomsUseCase {
        return GetRoomsUseCase(roomsRepository)
    }
}