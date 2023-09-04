package by.bashlikovvv.hotelreservation.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.hotelreservation.domain.model.Rooms
import by.bashlikovvv.hotelreservation.domain.usecase.GetRoomsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RoomFragmentViewModel(
    getRoomsUseCase: GetRoomsUseCase
) : ViewModel() {

    private var _rooms = MutableStateFlow(HotelFragmentViewModel.OnChange(Rooms()))
    val rooms = _rooms.asStateFlow()

    init {
        viewModelScope.launch {
            _rooms.update { HotelFragmentViewModel.OnChange(getRoomsUseCase.execute()) }
        }
    }
}
