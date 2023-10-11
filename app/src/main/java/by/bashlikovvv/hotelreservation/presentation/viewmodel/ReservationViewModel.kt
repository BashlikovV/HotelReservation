package by.bashlikovvv.hotelreservation.presentation.viewmodel

import android.content.res.Resources
import android.graphics.drawable.Drawable
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import by.bashlikovvv.data.CommonException
import by.bashlikovvv.domain.model.Phone
import by.bashlikovvv.domain.model.Reservation
import by.bashlikovvv.domain.model.TouristInfo
import by.bashlikovvv.domain.usecase.GetReservationUseCase
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentReservationBinding
import by.bashlikovvv.hotelreservation.presentation.contract.PhoneTextViewListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ReservationViewModel(
    getReservationUseCase: GetReservationUseCase
) : ViewModel() {

    private var _reservation = MutableStateFlow(Reservation())
    val reservation = _reservation.asStateFlow()

    private var _tourists = MutableStateFlow(listOf(TouristInfo(id = 0)))
    val tourists = _tourists.asStateFlow()

    private var _phoneValue = MutableStateFlow(Phone(
        currentValue = PhoneTextViewListener.INITIAL_VALUE
    ))
    val phoneValue = _phoneValue.asStateFlow()

    private var _updateVisibility = MutableStateFlow(HotelFragmentViewModel.OnChange(true))
    val updateVisibility = _updateVisibility.asStateFlow()


    init {
        viewModelScope.launch(Dispatchers.IO) {
            _reservation.update {
                try {
                    getReservationUseCase.execute()
                } catch (e: CommonException.ReservationNotFoundException) {
                    Reservation()
                }
            }
        }.invokeOnCompletion { setUpdateVisibility(false) }
    }

    fun addTourist(touristInfo: TouristInfo) {
        if (touristInfo.isEmpty()) return
        val newList = _tourists.value.toMutableList()
        newList.add(touristInfo)
        _tourists.update { newList }
    }

    fun updateFirsTouristName(name: String) {
        val newList = _tourists.value
        newList[0].name = name
        _tourists.update { newList }
    }

    fun updateTourist(newValue: TouristInfo) {
        val newList = _tourists.value
        newList.onEach {
            if (it.name == newValue.name) {
                it.inputName = newValue.inputName
                it.surname = newValue.surname
                it.citizenship = newValue.citizenship
                it.dateOfBirth = newValue.dateOfBirth
                it.passportNumber = newValue.passportNumber
                it.validityPeriod = newValue.validityPeriod
                it.expanded = newValue.expanded
            }
        }
        _tourists.update { newList }
    }

    fun getLastTouristId() = _tourists.value.last().id

    fun checkTourists(): Long {
        var idx = TOURIST_NOT_FOUND
        val newList = mutableListOf<TouristInfo>()
        _tourists.value.forEach {
            if (!it.isCorrect()) {
                idx = it.id
                newList.add(it.copy(hasError = true))
            } else {
                if (it.hasError) {
                    newList.add(it.copy(hasError = false))
                } else {
                    newList.add(it)
                }
            }
            _tourists.update { newList }
        }

        return idx
    }

    fun checkPhoneAndEmail(
        binding: FragmentReservationBinding,
        resources: Resources
    ): Boolean {
        val emailFlag = android.util.Patterns.EMAIL_ADDRESS.matcher(
            binding.emailAddress.text.toString()
        ).matches()
        if (!emailFlag) {
            binding.emailAddress.background = resources.getBackground(true)
        } else {
            binding.emailAddress.background = resources.getBackground(false)
        }
        val phoneNumberFlag = binding.phoneNumber.text?.contains("*")
        if (phoneNumberFlag == true || binding.phoneNumber.text?.isBlank() == true) {
            binding.phoneNumber.background = resources.getBackground(true)
        } else {
            binding.phoneNumber.background = resources.getBackground(false)
        }
        return phoneNumberFlag == false && emailFlag
    }

    fun updatePhone(newValue: String) {
        _phoneValue.update { Phone(newValue) }
    }

    private fun Resources.getBackground(flag: Boolean): Drawable? {
        return if(flag) {
            ResourcesCompat.getDrawable(this, R.drawable.te_error_backgroud, newTheme())
        } else {
            ResourcesCompat.getDrawable(this, R.drawable.te_background, newTheme())
        }
    }

    private fun setUpdateVisibility(value: Boolean) {
        _updateVisibility.update { HotelFragmentViewModel.OnChange(value) }
    }

    fun getLastNumberPosition(str: String): Int {
        for (i in str.lastIndex downTo 0) {
            if (i > 3 && str[i] in '0'..'9') {
                return i + 1
            }
        }

        return 4
    }

    companion object {
        const val TOURIST_NOT_FOUND = -1L
    }
}