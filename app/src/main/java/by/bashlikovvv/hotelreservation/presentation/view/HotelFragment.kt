package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentHotelBinding
import by.bashlikovvv.hotelreservation.databinding.HotelDetailedInfoBinding
import by.bashlikovvv.hotelreservation.databinding.HotelInfoBinding
import by.bashlikovvv.hotelreservation.databinding.ImagesListItemBinding
import by.bashlikovvv.hotelreservation.domain.model.Description
import by.bashlikovvv.hotelreservation.domain.model.Hotel
import by.bashlikovvv.hotelreservation.domain.model.HotelItem
import by.bashlikovvv.hotelreservation.presentation.contract.SnapOnScrollListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.HotelFragmentViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.abs

@AndroidEntryPoint
class HotelFragment : Fragment() {

    //hotel_detailed_info, hotel_info, fragment_hotel

    private val binding: FragmentHotelBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    private val viewModel: HotelFragmentViewModel by viewModels()

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
        binding.testFragmentRV.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        binding.testFragmentRV.addItemDecoration(decorator)
        viewModel.loadHotel(0)
        val adapters = ListDelegationAdapter(
            hotelInfoAdapter(),
            hotelDetailsInfoAdapter()
        )
        lifecycleScope.launch {
            collectProgressBarVisibility()
        }
        lifecycleScope.launch {
            collectHotel(adapters)
        }
    }

    private suspend fun collectProgressBarVisibility() {
        viewModel.updateVisibility.collect {
            if (it.value) {
                setVisible()
            } else {
                setInvisible()
            }
        }
    }

    private suspend fun collectHotel(adapters: ListDelegationAdapter<List<HotelItem>>) {
        viewModel.hotel.collectLatest { hotel ->
            if (hotel.isEmpty()) return@collectLatest
            adapters.items = listOf(hotel, hotel.description)
            binding.testFragmentRV.adapter = adapters
            binding.include.selectRoomBtn.setOnClickListener {
                val args = bundleOf(RoomFragment.HOTEL_NAME to hotel.name)
                findNavController().navigate(
                    resId = R.id.action_hotelFragment_to_roomFragment,
                    args = args
                )
            }
        }
    }

    private fun hotelInfoAdapter(): AdapterDelegate<List<HotelItem>> =
        adapterDelegateViewBinding<Hotel, HotelItem, HotelInfoBinding>(
            { layoutInflater, parent ->
                HotelInfoBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            binding.imagesRecyclerView.layoutManager = LinearLayoutManager(
                requireContext(), RecyclerView.HORIZONTAL, false
            )
            binding.imagesRecyclerView.onFlingListener = null
            val snapHelper = PagerSnapHelper()
            snapHelper.attachToRecyclerView(binding.imagesRecyclerView)

            bind {
                binding.imagesRecyclerView.adapter = ListDelegationAdapter(
                    imagesListAdapter()
                ).apply { items = item.imagesUrls }
                var idx = 0
                repeat(item.imagesUrls.size) {
                    binding.addDotIndicator(idx)
                    idx++
                }
                binding.imagesRecyclerView.addOnScrollListener(binding.scrollListener(snapHelper, item))
                val starsText = "${item.rating} ${item.ratingName}"
                binding.starsText.text = starsText
                binding.hotelName.text = item.name
                binding.hotelLocation.text = item.address
                binding.hotelPrice.text = getString(R.string.currency, item.minimalPrice.toString())
                binding.additionalTitle.text = item.priceForIt
            }
        }

    private fun imagesListAdapter(): AdapterDelegate<List<Any>> =
        adapterDelegateViewBinding<String, Any, ImagesListItemBinding>(
            { layoutInflater, parent ->
                ImagesListItemBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                Glide.with(binding.image)
                    .load(item)
                    .centerCrop()
                    .into(binding.image)
            }
        }

    private fun hotelDetailsInfoAdapter(): AdapterDelegate<List<HotelItem>> =
        adapterDelegateViewBinding<Description, HotelItem, HotelDetailedInfoBinding>(
            { layoutInflater, parent ->
                HotelDetailedInfoBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            bind {
                item.peculiarities.onEach {
                    binding.addUsability(it)
                }
                binding.hotelDescription.text = item.description
            }
        }

    private fun HotelInfoBinding.addDotIndicator(idx: Int) {
        val layout = layoutInflater.inflate(
            R.layout.dot_indicator_item,
            dotsLinearLayout,
            false
        )

        dotsLinearLayout.addView(layout, idx)
    }

    private fun HotelDetailedInfoBinding.addUsability(usability: String) {
        val layout = layoutInflater.inflate(R.layout.usability_item, chipGroup, false)

        val textView = layout.findViewById<Chip>(R.id.usabilityText)
        textView.text = usability

        chipGroup.addView(layout)
    }

    private fun HotelInfoBinding.scrollListener(
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

    private fun setVisible() {
        binding.testFragmentRV.visibility = View.GONE
        binding.include.selectRoomBtn.visibility = View.GONE
        binding.progressCircular.visibility = View.VISIBLE
    }

    private fun setInvisible() {
        binding.testFragmentRV.visibility = View.VISIBLE
        binding.include.selectRoomBtn.visibility = View.VISIBLE
        binding.progressCircular.visibility = View.GONE
    }
}