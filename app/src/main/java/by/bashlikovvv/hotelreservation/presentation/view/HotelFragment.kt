package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.get
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
import by.bashlikovvv.hotelreservation.presentation.contract.SnapOnScrollListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.HotelFragmentViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HotelFragment : Fragment() {

    private val binding: FragmentHotelBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val viewModel: HotelFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
            bottomLayout.selectRoomBtn.setOnClickListener {
                findNavController().navigate(resId = R.id.action_hotelFragment_to_roomFragment)
            }
        }
    }

    private fun setUpImagesRecyclerView(hotel: Hotel) {
        binding.hotelImages.recyclerView.apply {
            var idx = 0
            hotel.imagesUrls.forEach { _ ->
                addDotIndicator(idx)
                idx++
            }
            onFlingListener = null
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ImagesListAdapter().also {
                it.setImages(hotel.imagesUrls)
            }
            binding.hotelImages.recyclerView.addOnScrollListener(scrollListener(snapHelper, hotel))
        }
    }

    private fun scrollListener(
        snapHelper: PagerSnapHelper,
        hotel: Hotel
    ) = SnapOnScrollListener(snapHelper) {
        val n = hotel.imagesUrls.size
        for (i in 0 until n) {
            if (i == it) {
                binding.hotelImages.dotsLinearLayout[it].alpha = 1f
            } else {
                binding.hotelImages.dotsLinearLayout[i].alpha = calcAlpha(n, it, i)
            }

        }
    }

    private fun calcAlpha(n: Int, pos: Int, i: Int): Float {
        return 0.2f + (n - 1 - abs(pos - i)) * (1 - 0.2f) / (n - 1)
    }

    private fun addUsability(usability: String) {
        val layout = layoutInflater.inflate(R.layout.usability_item, binding.chipGroup, false)

        val textView = layout.findViewById<Chip>(R.id.usabilityText)
        textView.text = usability

        binding.chipGroup.addView(layout)
    }

    private fun addDotIndicator(idx: Int) {
        val layout = layoutInflater.inflate(
            R.layout.dot_indicator_item,
            binding.hotelImages.dotsLinearLayout,
            false
        )

        binding.hotelImages.dotsLinearLayout.addView(layout, idx)
    }

    private fun getStringRes(@StringRes resId: Int, data: String): String {
        return requireContext().getString(resId, data)
    }

    private fun FragmentHotelBinding.setVisible() {
        hotelImages.recyclerView.visibility = View.GONE
        hotelImages.dotsLinearLayout.visibility = View.GONE
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
        bottomLayout.selectRoomBtn.visibility = View.GONE
    }

    private fun FragmentHotelBinding.setInvisible() {
        hotelImages.recyclerView.visibility = View.VISIBLE
        hotelImages.dotsLinearLayout.visibility = View.VISIBLE
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
        bottomLayout.selectRoomBtn.visibility = View.VISIBLE
    }
}