package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentHotelBinding
import by.bashlikovvv.hotelreservation.domain.model.Hotel
import by.bashlikovvv.hotelreservation.presentation.adapters.ImagesListAdapter
import by.bashlikovvv.hotelreservation.presentation.contract.SnapOnScrollListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.HotelFragmentViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class HotelFragment : Fragment() {

    private val binding: FragmentHotelBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val viewModel: HotelFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.navigation_icon)
            title = getString(R.string.hotel)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loadHotel(/*expected id*/0)
        binding.imagesRecyclerView.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        viewModel.loadHotel(0)
        lifecycleScope.launch { collectProgressBarVisibility() }
        lifecycleScope.launch { collectHotel() }
        binding.navButton.selectRoomBtn.setOnClickListener {
            onClickListener(viewModel.hotel.value)
        }
    }

    private fun onClickListener(hotel: Hotel) {
        val args = bundleOf(RoomFragment.HOTEL_NAME to hotel.name)
        findNavController().navigate(
            resId = R.id.action_hotelFragment_to_roomFragment,
            args = args
        )
    }

    private suspend fun collectProgressBarVisibility() {
        viewModel.updateVisibility.collect {
            if (it.value) {
                binding.scrollView.visibility = View.GONE
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.scrollView.visibility = View.VISIBLE
                binding.progressCircular.visibility = View.GONE
            }
        }
    }

    private suspend fun collectHotel() {
        viewModel.hotel.collectLatest { hotel ->
            setUpImagesRecyclerView(hotel)
            hotel.description.peculiarities.onEach {
                binding.addUsability(it)
            }
        }
    }


    private fun setUpImagesRecyclerView(hotel: Hotel) {
        binding.imagesRecyclerView.apply {
            var idx = 0
            hotel.imagesUrls.forEach { _ ->
                binding.addDotIndicator(idx)
                idx++
            }
            onFlingListener = null
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(this)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = ListDelegationAdapter(
                ImagesListAdapter().imagesListAdapter()
            ).apply { items = hotel.imagesUrls }
            binding.imagesRecyclerView.addOnScrollListener(binding.scrollListener(snapHelper, hotel))
        }
    }

    private fun FragmentHotelBinding.addDotIndicator(idx: Int) {
        val layout = layoutInflater.inflate(
            R.layout.dot_indicator_item,
            dotsLinearLayout,
            false
        )

        dotsLinearLayout.addView(layout, idx)
    }

    private fun FragmentHotelBinding.addUsability(usability: String) {
        val layout = layoutInflater.inflate(R.layout.usability_item, chipGroup, false)

        val textView = layout.findViewById<Chip>(R.id.usabilityText)
        textView.text = usability

        chipGroup.addView(layout)
    }

    private fun FragmentHotelBinding.scrollListener(
        snapHelper: PagerSnapHelper,
        hotel: Hotel
    ) = SnapOnScrollListener(snapHelper) {
        val n = hotel.imagesUrls.size
        for (i in 0 until n) {
            if (i == it) {
                dotsLinearLayout[it].alpha = 1f
            } else {
                dotsLinearLayout[i].alpha = calcAlpha(n, it, i)
            }

        }
    }

    private fun calcAlpha(n: Int, pos: Int, i: Int): Float {
        return 0.2f + (n - 1 - abs(pos - i)) * (1 - 0.2f) / (n - 1)
    }
}