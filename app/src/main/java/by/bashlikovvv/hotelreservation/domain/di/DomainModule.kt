package by.bashlikovvv.hotelreservation.domain.di

import by.bashlikovvv.hotelreservation.domain.repository.IHotelRepository
import by.bashlikovvv.hotelreservation.domain.usecase.GetHotelByIdUseCase
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
}