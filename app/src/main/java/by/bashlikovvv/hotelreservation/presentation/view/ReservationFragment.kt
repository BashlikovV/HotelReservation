package by.bashlikovvv.hotelreservation.presentation.view

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.domain.model.Item
import by.bashlikovvv.domain.model.Reservation
import by.bashlikovvv.domain.model.TouristInfo
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.ExpandableContentLayoutBinding
import by.bashlikovvv.hotelreservation.databinding.FragmentReservationBinding
import by.bashlikovvv.hotelreservation.presentation.contract.PhoneTextViewListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.ReservationViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.textfield.TextInputEditText
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.text.StringBuilder
import kotlin.text.contentEquals
import kotlin.text.indexOf
import kotlin.text.isBlank
import kotlin.text.lastIndex
import kotlin.text.set
import kotlin.text.toCharArray

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

        collectProgressVisibility()
        addBookingInfo()

        setUpPhoneNumberEditText()
        addTouristListener()
        setUpEmailEditText()
    }

    private fun setUpEmailEditText() {
        binding.emailAddress.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    val flag = android.util.Patterns.EMAIL_ADDRESS.matcher(
                        binding.emailAddress.text.toString()
                    ).matches()
                    if (!flag) {
                        val dr = ResourcesCompat.getDrawable(resources, R.drawable.te_error_backgroud, resources.newTheme())
                        binding.emailAddress.background = dr
                    } else {
                        val dr = ResourcesCompat.getDrawable(resources, R.drawable.te_background, resources.newTheme())
                        binding.emailAddress.background = dr
                    }
                }
                true
            }
        }
    }

    private fun setUpPhoneNumberEditText() {
        binding.phoneNumber.apply {
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

    private fun collectProgressVisibility() {
        lifecycleScope.launch {
            viewModel.updateVisibility.collectLatest {
                binding.progressCircular.visibility = if (it.value) {
                    binding.scrollView.visibility = View.GONE
                    View.VISIBLE
                } else {
                    binding.scrollView.visibility = View.VISIBLE
                    View.GONE
                }
            }
        }
    }

    private fun addTouristListener() {
        binding.touristsInfoRV.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        val stringArray = resources.getStringArray(R.array.numbers)
        viewModel.updateFirsTouristName(getString(R.string.tourist_number, stringArray[0]))

        lifecycleScope.launch {
            viewModel.tourists.collectLatest { tourists ->
                val adapters = ListDelegationAdapter(
                    touristsInfoAdapter()
                ).apply { items = tourists }
                binding.touristsInfoRV.adapter = adapters
            }
        }

        binding.addTouristImage.setOnClickListener {
            val newId = viewModel.getLastTouristId() + 1
            if (newId > stringArray.lastIndex) return@setOnClickListener
            viewModel.addTourist(TouristInfo(
                name = getString(R.string.tourist_number, stringArray[newId.toInt()]),
                id = newId
            ))
        }
    }

    private fun touristsInfoAdapter(): AdapterDelegate<List<Item>> =
        adapterDelegateViewBinding<TouristInfo, Item, ExpandableContentLayoutBinding>(
            { layoutInflater, parent ->
                ExpandableContentLayoutBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                if (item.isEmpty()) return@bind
                binding.touristCount.text = item.name
                addNameTextChangedListener(binding.expandableLayoutContent, item)
                addSurnameTextChangedListener(binding.expandableLayoutContent, item)
                addBirthDateTextChangedListener(binding.expandableLayoutContent, item)
                addNameCitizenshipTextChangedListener(binding.expandableLayoutContent, item)
                addPassportNumberTextChangedListener(binding.expandableLayoutContent, item)
                addValidityPeriodTextChangedListener(binding.expandableLayoutContent, item)
            }
        }

    private fun addValidityPeriodTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.validityPeriod).apply {
            background = getBackground(touristInfo.hasError && touristInfo.validityPeriod.isBlank())
            text?.append(touristInfo.validityPeriod)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        validityPeriod = it.toString()
                    )
                )
                background = getBackground(touristInfo.hasError && touristInfo.validityPeriod.isBlank())
            }
        }
    }

    private fun addPassportNumberTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.passportNumber).apply {
            background = getBackground(touristInfo.hasError && touristInfo.passportNumber.isBlank())
            text?.append(touristInfo.passportNumber)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        passportNumber = it.toString()
                    )
                )
                background = getBackground(touristInfo.hasError && touristInfo.passportNumber.isBlank())
            }
        }
    }

    private fun addNameCitizenshipTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.citizenship).apply {
            background = getBackground(touristInfo.hasError && touristInfo.citizenship.isBlank())
            text?.append(touristInfo.citizenship)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        citizenship = it.toString()
                    )
                )
                background = getBackground(touristInfo.hasError && touristInfo.citizenship.isBlank())
            }
        }
    }

    private fun addBirthDateTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.date_of_birth).apply {
            background = getBackground(touristInfo.hasError && touristInfo.dateOfBirth.isBlank())
            text?.append(touristInfo.dateOfBirth)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        dateOfBirth = it.toString()
                    )
                )
                background = getBackground(touristInfo.hasError && touristInfo.dateOfBirth.isBlank())
            }
        }
    }

    private fun addSurnameTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.surname).apply {
            background = getBackground(touristInfo.hasError && touristInfo.surname.isBlank())
            text?.append(touristInfo.surname)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        surname = it.toString()
                    )
                )
                background = getBackground(touristInfo.hasError && touristInfo.surname.isBlank())
            }
        }
    }

    private fun addNameTextChangedListener(layout: View, touristInfo: TouristInfo) {
        layout.findViewById<TextInputEditText>(R.id.name).apply {
            background = getBackground(touristInfo.hasError && touristInfo.inputName.isBlank())
            text?.append(touristInfo.inputName)
            addTextChangedListener {
                viewModel.updateTourist(
                    touristInfo.copy(
                        inputName = it.toString()
                    )
                )
                background = getBackground(touristInfo.hasError && touristInfo.inputName.isBlank())
            }
        }
    }

    private fun FragmentReservationBinding.addTextChangedListener() {
        var currentValue = PhoneTextViewListener.INITIAL_VALUE
        var currentNew: Char

        phoneNumber.addTextChangedListener(
            PhoneTextViewListener { _, old, new, _ ->
                val newText = StringBuilder(currentValue)
                if (new in "0".."9") {
                    currentNew = new.toCharArray().first()
                    val idx = newText.indexOf(PhoneTextViewListener.ASTERISK)
                    if (idx != -1) {
                        newText[idx] = currentNew
                        currentValue = newText.toString()

                        phoneNumber.setText(newText)
                    } else {
                        phoneNumber.setText(newText)
                    }
                }
                if (old in "0".."9") {
                    val idx = getLastNumberPosition(newText.toString()) - 1
                    if (idx != -1) {
                        newText[idx] = PhoneTextViewListener.ASTERISK
                        currentValue = newText.toString()

                        phoneNumber.setText(newText)
                    } else {
                        phoneNumber.setText(newText)
                    }
                } else {
                    phoneNumber.setText(currentValue)
                }
                val lnp = getLastNumberPosition(phoneNumber.text.toString())
                phoneNumber.setSelection(lnp)
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
        binding.tourCost.text = getString(R.string.currency, reservation.tourPrice.toString())
        binding.fuelFee.text = getString(R.string.currency, reservation.fuelCharge.toString())
        binding.serviceFee.text = getString(R.string.currency, reservation.serviceCharge.toString())
        binding.toPayment.text = getString(R.string.currency, finalCost.toString())
        binding.navButton.selectRoomBtn.apply {
            text = getString(R.string.payment, finalCost.toString())
            setOnClickListener { onClickListener() }
        }
    }

    private fun onClickListener() {
        val idx = viewModel.checkTourists()
        if (idx == ReservationViewModel.TOURIST_NOT_FOUND) {
            findNavController().navigate(R.id.action_reservationFragment_to_successFragment)
        }
    }

    private fun getBackground(flag: Boolean): Drawable? {
        return if(flag) {
            ResourcesCompat.getDrawable(resources, R.drawable.te_error_backgroud, resources.newTheme())
        } else {
            ResourcesCompat.getDrawable(resources, R.drawable.te_background, resources.newTheme())
        }
    }
}