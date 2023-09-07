package by.bashlikovvv.hotelreservation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.data.CommonException
import by.bashlikovvv.domain.model.Hotel
import by.bashlikovvv.domain.usecase.GetHotelByIdUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HotelFragmentViewModel(
    private val getHotelByIdUseCase: GetHotelByIdUseCase
) : ViewModel() {

    private var _updateVisibility: MutableStateFlow<OnChange<Boolean>> = MutableStateFlow(OnChange(true))
    val updateVisibility = _updateVisibility.asStateFlow()

    private var _hotel = MutableStateFlow(Hotel())
    val hotel = _hotel.asStateFlow()

    fun loadHotel(id: Long) {
        _updateVisibility.update { OnChange(true) }
        val job = viewModelScope.launch(Dispatchers.IO) {
            _hotel.update {
                try {
                    getHotelByIdUseCase.execute(id)
                } catch (e: CommonException.HotelNotFoundException) {
                    Hotel()
                }
            }
        }
        job.invokeOnCompletion {
            _updateVisibility.update { OnChange(false) }
        }
    }

    class OnChange<T>(val value: T)
}