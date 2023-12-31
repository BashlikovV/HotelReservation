package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.domain.model.Reservation
import by.bashlikovvv.domain.model.TouristInfo
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentReservationBinding
import by.bashlikovvv.hotelreservation.presentation.adapters.TouristsInfoAdapter
import by.bashlikovvv.hotelreservation.presentation.contract.PhoneTextViewListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.ReservationViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.text.set

class ReservationFragment : Fragment() {

    private val binding: FragmentReservationBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: ReservationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreateView(inflater, container, savedInstanceState)

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
            viewModel.reservationStates.collectLatest { reservationStates ->
                binding.progressCircular.visibility = if (reservationStates.updateVisibility) {
                    binding.scrollView.visibility = View.GONE
                    View.VISIBLE
                } else {
                    binding.scrollView.visibility = View.VISIBLE
                    View.GONE
                }
                val alertDialog = networkExceptionAlertDialog()
                if (reservationStates.errorVisibility) {
                    alertDialog.show()
                } else {
                    alertDialog.cancel()
                }
            }
        }
    }

    private fun networkExceptionAlertDialog() = AlertDialog.Builder(
        this@ReservationFragment.requireContext(), R.style.MyAlertDialogStyle
    )
        .setTitle("Network error")
        .setMessage("You can not load reservation without internet connection!")
        .setPositiveButton(
            R.string.alert_dialog_positive_btn_text
        ) { dialogInterface, _ ->
            dialogInterface.cancel()
            viewModel.loadReservation()
        }
        .setNegativeButton(
            R.string.alert_dialog_negative_btn_text
        ) { dialogInterface, _ ->
            dialogInterface.cancel()
            onNavigateBack()
        }.create()

    private fun addTouristListener() {
        binding.touristsInfoRV.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        val stringArray = resources.getStringArray(R.array.numbers)
        viewModel.updateFirsTouristName(getString(R.string.tourist_number, stringArray[0]))
        val adapters = ListDelegationAdapter(
            TouristsInfoAdapter(
                context = requireContext(),
                updateTouristCallback = { viewModel.updateTourist(it) }
            ).touristsInfoAdapter()
        )
        binding.touristsInfoRV.adapter = adapters

        lifecycleScope.launch {
            viewModel.tourists.collectLatest { tourists ->
                adapters.items = tourists
                adapters.notifyItemChanged(adapters.items?.indexOf(tourists.last()) ?: 0)
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

    private fun FragmentReservationBinding.addTextChangedListener() {
        var currentValue = viewModel.phoneValue.value.currentValue
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
                    val idx = viewModel.getLastNumberPosition(newText.toString()) - 1
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
                val lnp = viewModel.getLastNumberPosition(phoneNumber.text.toString())
                phoneNumber.setSelection(lnp)
            }
        )
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
        binding.tourCost.text = getString(R.string.currency, parsePrice(reservation.tourPrice.toString()))
        binding.fuelFee.text = getString(R.string.currency, parsePrice(reservation.fuelCharge.toString()))
        binding.serviceFee.text = getString(R.string.currency, parsePrice(reservation.serviceCharge.toString()))
        binding.toPayment.text = getString(R.string.currency, parsePrice(finalCost.toString()))
        binding.navButton.selectRoomBtn.apply {
            text = getString(R.string.payment, parsePrice(finalCost.toString()))
            setOnClickListener { onClickListener() }
        }
        binding.hotelName.text = reservation.hotelName
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.updatePhone(binding.phoneNumber.text.toString())
    }

    private fun parsePrice(price: String): String {
        val arr = price.toMutableList()
        var count = 0
        for (i in arr.lastIndex downTo  0) {
            if (count == 2 && i != 0) {
                arr.add(i, ' ')
                count = -1
            }

            count++
        }

        return arr.joinToString("")
    }

    private fun onClickListener() {
        if (
            viewModel.checkPhoneAndEmail(binding, resources) && viewModel.checkTourists() ==
            ReservationViewModel.TOURIST_NOT_FOUND
            ) {
            findNavController().navigate(R.id.action_reservationFragment_to_successFragment)
        }
    }

    private fun onNavigateBack() {
        findNavController().popBackStack()
    }
}