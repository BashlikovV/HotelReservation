package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentReservationBinding
import by.bashlikovvv.hotelreservation.domain.model.Reservation
import by.bashlikovvv.hotelreservation.presentation.contract.PhoneTextViewListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.ReservationViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReservationFragment : Fragment() {

    private val binding: FragmentReservationBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: ReservationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.reservation.collectLatest {
                setBookingInfo(it)
            }
        }

        binding.aboutBuyerLayout.phoneNumber.apply {
            append(PhoneTextViewListener.INITIAL_VALUE)
            imeOptions = EditorInfo.IME_ACTION_NEXT
            binding.addTextChangedListener()
        }
        binding.aboutBuyerLayout.emailAddress.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnEditorActionListener { v, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    onFocusChangeListener.onFocusChange(v, false)

                }
                true
            }
            setOnFocusChangeListener { v, hasFocus ->
                if (!hasFocus) {
                    if (binding.aboutBuyerLayout.emailAddress.text?.contains('.') == false) {
                        v.background = R.color.error.toDrawable()
                    }
                }
            }
            addTextChangedListener {
                if (binding.aboutBuyerLayout.emailAddress.text?.contains('.') == false) {
                    binding.aboutBuyerLayout.emailAddress.background = R.color.error.toDrawable()
                } else {
                    binding.aboutBuyerLayout.emailAddress.background = R.color.background.toDrawable()
                }
            }
        }
    }

    private fun FragmentReservationBinding.addTextChangedListener() {
        var currentValue = PhoneTextViewListener.INITIAL_VALUE
        var currentNew: Char

        aboutBuyerLayout.phoneNumber.addTextChangedListener(
            PhoneTextViewListener { _, old, new, _ ->
                val newText = StringBuilder(currentValue)
                if (new in "0".."9") {
                    currentNew = new.toCharArray().first()
                    val idx = newText.indexOf(PhoneTextViewListener.ASTERISK)
                    if (idx != -1) {
                        newText[idx] = currentNew
                        currentValue = newText.toString()

                        aboutBuyerLayout.phoneNumber.setText(newText)
                    } else {
                        aboutBuyerLayout.phoneNumber.setText(newText)
                    }
                }
                if (old in "0".."9") {
                    val idx = getLastNumberPosition(newText.toString()) - 1
                    if (idx != -1) {
                        newText[idx] = PhoneTextViewListener.ASTERISK
                        currentValue = newText.toString()

                        aboutBuyerLayout.phoneNumber.setText(newText)
                    } else {
                        aboutBuyerLayout.phoneNumber.setText(newText)
                    }
                } else {
                    aboutBuyerLayout.phoneNumber.setText(currentValue)
                }
                val lnp = getLastNumberPosition(aboutBuyerLayout.phoneNumber.text.toString())
                aboutBuyerLayout.phoneNumber.setSelection(lnp)
            }
        )
    }

    private fun getLastNumberPosition(str: String): Int {
        for (i in str.lastIndex downTo 0) {
            if (i > 3 && str[i] in '0'..'9') {
                return i + 1
            }
        }

        return 4
    }

    private fun setBookingInfo(reservation: Reservation) {
        binding.bookingInfoLayout.removeAllViews()
        val infoArray = listOf(
            reservation.departure,
            reservation.arrivalCountry,
            reservation.tourDateStart + "–" + reservation.tourDateStop,
            reservation.numberOfNights.toString() + getString(R.string.nights),
            reservation.hotelName,
            reservation.room,
            reservation.nutrition
        )
        val stringArray = resources.getStringArray(R.array.booking_info)
        var idx = 0

        stringArray.forEach {
            val layout = layoutInflater.inflate(R.layout.booking_info_item, binding.bookingInfoLayout, false)

            layout.findViewById<TextView>(R.id.textViewLeft).text = it
            layout.findViewById<TextView>(R.id.textViewRight).text = infoArray[idx]

            binding.bookingInfoLayout.addView(layout)
            idx++
        }
    }

    companion object {

        const val ARG_ROOM = "room"
    }
}