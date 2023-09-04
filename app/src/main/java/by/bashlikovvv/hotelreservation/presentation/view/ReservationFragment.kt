package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentReservationBinding
import by.bashlikovvv.hotelreservation.domain.model.Reservation
import by.bashlikovvv.hotelreservation.domain.model.TouristInfo
import by.bashlikovvv.hotelreservation.presentation.contract.PhoneTextViewListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.ReservationViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReservationFragment : Fragment() {

    private val binding: FragmentReservationBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: ReservationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.navigation_icon)
            title = getString(R.string.reservation)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addProgressBar()
        addBookingInfo()

        setUpPhoneNumberEditText()
        addTouristListener()
        setUpEmailEditText()
    }

    private fun setUpEmailEditText() {
        binding.aboutBuyerLayout.emailAddress.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val flag = android.util.Patterns.EMAIL_ADDRESS.matcher(
                        binding.aboutBuyerLayout.emailAddress.text.toString()
                    ).matches()
                    if (!flag) {
                        val dr = ResourcesCompat.getDrawable(resources, R.drawable.te_error_backgroud, resources.newTheme())
                        binding.aboutBuyerLayout.emailAddress.background = dr
                    } else {
                        val dr = ResourcesCompat.getDrawable(resources, R.drawable.te_background, resources.newTheme())
                        binding.aboutBuyerLayout.emailAddress.background = dr
                    }
                }
                true
            }
        }
    }

    private fun setUpPhoneNumberEditText() {
        binding.aboutBuyerLayout.phoneNumber.apply {
            setOnTouchListener { v, _ ->
                if (text?.contentEquals("") == true) {
                    append(PhoneTextViewListener.INITIAL_VALUE)
                }
                v.performClick()
                false
            }
            imeOptions = EditorInfo.IME_ACTION_NEXT
            binding.addTextChangedListener()
        }
    }

    private fun addBookingInfo() {
        lifecycleScope.launch {
            viewModel.reservation.collectLatest {
                setBookingInfo(it)
            }
        }
    }

    private fun addProgressBar() {
        lifecycleScope.launch {
            viewModel.updateVisibility.collectLatest {
                binding.progressCircular.visibility = if (it.value) {
                    setVisible()
                    View.VISIBLE
                } else {
                    setInvisible()
                    View.GONE
                }
            }
        }
    }

    private fun addTouristListener() {
        val stringArray = resources.getStringArray(R.array.numbers)
        viewModel.updateFirsTouristName(getTextRes(R.string.tourist_number, stringArray[0]))

        lifecycleScope.launch {
            viewModel.tourists.collectLatest {
                binding.aboutBuyerLayout.touristsInfoLayout.linearLayout.removeAllViews()
                it.forEach { touristInfo ->
                    addTourist(touristInfo)
                }
            }
        }

        binding.aboutBuyerLayout.touristsInfoLayout.addTouristImage.setOnClickListener {
            val newId = viewModel.getLastTouristId() + 1
            if (newId > stringArray.lastIndex) return@setOnClickListener
            viewModel.addTourist(TouristInfo(
                name = getTextRes(R.string.tourist_number, stringArray[newId]),
                id = newId
            ))
        }
    }

    private fun addTourist(touristInfo: TouristInfo) {
        if (touristInfo.isEmpty()) return
        val layout = layoutInflater.inflate(
            R.layout.expandable_content_layout,
            binding.aboutBuyerLayout.touristsInfoLayout.linearLayout,
            false
        )
        layout.findViewById<TextView>(R.id.touristCount).text = touristInfo.name
        addNameTextChangedListener(layout, touristInfo)
        addSurnameTextChangedListener(layout, touristInfo)
        addBirthDateTextChangedListener(layout, touristInfo)
        addNameCitizenshipTextChangedListener(layout, touristInfo)
        addPassportNumberTextChangedListener(layout, touristInfo)
        addValidityPeriodTextChangedListener(layout, touristInfo)
        binding.aboutBuyerLayout.touristsInfoLayout.linearLayout.addView(
            layout,
            touristInfo.id
        )
    }

    private fun addValidityPeriodTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.validityPeriod).apply {
            text?.append(touristInfo.validityPeriod)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        validityPeriod = it.toString()
                    )
                )
            }
        }
    }

    private fun addPassportNumberTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.passportNumber).apply {
            text?.append(touristInfo.passportNumber)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        passportNumber = it.toString()
                    )
                )
            }
        }
    }

    private fun addNameCitizenshipTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.citizenship).apply {
            text?.append(touristInfo.citizenship)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        citizenship = it.toString()
                    )
                )
            }
        }
    }

    private fun addBirthDateTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.date_of_birth).apply {
            text?.append(touristInfo.dateOfBirth)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        dateOfBirth = it.toString()
                    )
                )
            }
        }
    }

    private fun addSurnameTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.surname).apply {
            text?.append(touristInfo.surname)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        surname = it.toString()
                    )
                )
            }
        }
    }

    private fun addNameTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.name).apply {
            text?.append(touristInfo.inputName)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        inputName = it.toString()
                    )
                )
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
        val finalCost = reservation.tourPrice + reservation.fuelCharge + reservation.serviceCharge
        binding.tourCost.text = getTextRes(R.string.currency, reservation.tourPrice.toString())
        binding.fuelFee.text = getTextRes(R.string.currency, reservation.fuelCharge.toString())
        binding.serviceFee.text = getTextRes(R.string.currency, reservation.serviceCharge.toString())
        binding.toPayment.text = getTextRes(R.string.currency, finalCost.toString())
        binding.navButton.selectRoomBtn.apply {
            text = getTextRes(R.string.payment, finalCost.toString())
            setOnClickListener {
                findNavController().navigate(R.id.action_reservationFragment_to_successFragment)
            }
        }
    }

    private fun getTextRes(@StringRes id: Int, data: String): String {
        return requireContext().getString(id, data)
    }

    private fun setVisible() {
        binding.include.linearLayout.visibility = View.GONE
        binding.bookingInfoLayout.visibility = View.GONE
        binding.aboutBuyerLayout.constraintLayout.visibility = View.GONE
    }

    private fun setInvisible() {
        binding.include.linearLayout.visibility = View.VISIBLE
        binding.bookingInfoLayout.visibility = View.VISIBLE
        binding.aboutBuyerLayout.constraintLayout.visibility = View.VISIBLE
    }
}