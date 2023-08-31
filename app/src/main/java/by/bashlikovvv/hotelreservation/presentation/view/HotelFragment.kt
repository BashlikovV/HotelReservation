package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentHotelBinding
import by.bashlikovvv.hotelreservation.domain.model.Hotel
import by.bashlikovvv.hotelreservation.presentation.adapters.ImagesListAdapter
import by.bashlikovvv.hotelreservation.presentation.viewmodel.HotelFragmentViewModel
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HotelFragment : Fragment() {

    private var _binding: FragmentHotelBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HotelFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHotelBinding.inflate(layoutInflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentHotelBinding.bind(view)

        lifecycleScope.launch {
            viewModel.updateVisibility.collect {
                binding.progressCircular.visibility = if (it.value) {
                    binding.setVisible()
                    View.VISIBLE
                } else {
                    binding.setInvisible()
                    View.GONE
                }
            }
        }
        viewModel.loadHotel(/*expected id*/0)
        lifecycleScope.launch {
            viewModel.hotel.collectLatest {
                setUpImagesRecyclerView(it)
                setUpFragment(it)
            }
        }
    }

    private fun setUpFragment(hotel: Hotel) {
        with(binding) {
            val starsText = "${hotel.rating} ${hotel.ratingName}"
            rootLayout.starsText.text = starsText
            hotelName.text = hotel.name
            hotelLocation.text = hotel.address
            hotelPrice.text = getStringRes(R.string.from_string, hotel.minimalPrice.toString())
            additionalTitle.text = hotel.priceForIt
            hotel.description.peculiarities.forEach {
                addUsability(it)
            }
            hotelDescription.text = hotel.description.description
            selectRoomBtn.setOnClickListener {
                val args = bundleOf(RoomFragment.HOTEL_NAME to hotel.name)
                findNavController().navigate(
                    resId = R.id.action_hotelFragment_to_roomFragment
                )
            }
        }
    }

    private fun setUpImagesRecyclerView(hotel: Hotel) {
        binding.hotelImages.recyclerView.apply {
            onFlingListener = null
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ImagesListAdapter().also {
                it.setImages(hotel.imagesUrls)
            }
        }
    }

    private fun addUsability(usability: String) {
        val layout = layoutInflater.inflate(R.layout.usability_item, binding.chipGroup, false)

        val textView = layout.findViewById<Chip>(R.id.usabilityText)
        textView.text = usability

        binding.chipGroup.addView(layout)
    }

    private fun getStringRes(@StringRes resId: Int, data: String): String {
        return requireContext().getString(resId, data)
    }

    private fun FragmentHotelBinding.setVisible() {
        hotelImages.recyclerView.visibility = View.GONE
        starsLayout.visibility = View.GONE
        rootLayout.linearLayout.visibility = View.GONE
        hotelName.visibility = View.GONE
        hotelLocation.visibility = View.GONE
        hotelPrice.visibility = View.GONE
        additionalTitle.visibility = View.GONE
        aboutHotel.visibility = View.GONE
        chipGroup.visibility = View.GONE
        hotelDescription.visibility = View.GONE
        hotelInfoLayout.visibility = View.GONE
        selectRoomBtn.visibility = View.GONE
    }

    private fun FragmentHotelBinding.setInvisible() {
        hotelImages.recyclerView.visibility = View.VISIBLE
        starsLayout.visibility = View.VISIBLE
        rootLayout.linearLayout.visibility = View.VISIBLE
        hotelName.visibility = View.VISIBLE
        hotelLocation.visibility = View.VISIBLE
        hotelPrice.visibility = View.VISIBLE
        additionalTitle.visibility = View.VISIBLE
        aboutHotel.visibility = View.VISIBLE
        chipGroup.visibility = View.VISIBLE
        hotelDescription.visibility = View.VISIBLE
        hotelInfoLayout.visibility = View.VISIBLE
        selectRoomBtn.visibility = View.VISIBLE
    }
}