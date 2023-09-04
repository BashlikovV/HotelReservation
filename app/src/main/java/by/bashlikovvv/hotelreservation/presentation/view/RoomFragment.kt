package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentRoomBinding
import by.bashlikovvv.hotelreservation.databinding.ImagesListItemBinding
import by.bashlikovvv.hotelreservation.databinding.RoomInfoBinding
import by.bashlikovvv.hotelreservation.domain.model.Item
import by.bashlikovvv.hotelreservation.domain.model.RoomItem
import by.bashlikovvv.hotelreservation.presentation.contract.SnapOnScrollListener
import by.bashlikovvv.hotelreservation.presentation.viewmodel.RoomFragmentViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.abs

class RoomFragment : Fragment() {

    private val binding: FragmentRoomBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: RoomFragmentViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val hotelName = requireArguments().getString(HOTEL_NAME) ?: "unknown"
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.navigation_icon)
            title = hotelName
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.testFragmentRV.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        val decorator = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
        decorator.setDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.divider)!!)
        binding.testFragmentRV.addItemDecoration(decorator)
        val adapters = ListDelegationAdapter(
            roomsAdapter { onOpenClickListener() }
        )
        lifecycleScope.launch {
            viewModel.rooms.collectLatest { rooms ->
                adapters.items = rooms.rooms.filter { !it.isEmpty() }
                binding.testFragmentRV.adapter = adapters
            }
        }
    }

    private fun roomsAdapter(onClickListener: (RoomItem) -> Unit): AdapterDelegate<List<Item>> =
        adapterDelegateViewBinding<RoomItem, Item, RoomInfoBinding>(
            { layoutInflater, parent ->
                RoomInfoBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            binding.navLayout.selectRoomBtn.setOnClickListener {
                onClickListener(item)
            }
            val snapHelper = PagerSnapHelper()
            binding.roomImagesRV.apply {
                layoutManager = LinearLayoutManager(
                    requireContext(), RecyclerView.HORIZONTAL, false
                )
                onFlingListener = null
                snapHelper.attachToRecyclerView(this)
            }

            bind {
                binding.roomImagesRV.adapter = ListDelegationAdapter(
                    imagesListAdapter()
                ).apply { items = item.imageUrls }
                var idx = 0
                repeat(item.imageUrls.size) {
                    binding.addDotIndicator(idx)
                    idx++
                }
                binding.roomImagesRV.addOnScrollListener(binding.scrollListener(snapHelper, item))
                item.peculiarities.onEach { binding.addUsability(it) }
                binding.navLayout.selectRoomBtn.text = getString(R.string.about_room, "")
                binding.roomName.text = item.name
                binding.roomPrice.text = getString(R.string.currency, item.price.toString())
                binding.additionalTitle.text = item.pricePer
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

    private fun RoomInfoBinding.addDotIndicator(idx: Int) {
        val layout = layoutInflater.inflate(
            R.layout.dot_indicator_item,
            dotsLinearLayout,
            false
        )

        dotsLinearLayout.addView(layout, idx)
    }

    private fun RoomInfoBinding.addUsability(usability: String) {
        val layout = layoutInflater.inflate(R.layout.usability_item, chipGroup, false)

        val textView = layout.findViewById<Chip>(R.id.usabilityText)
        textView.text = usability

        chipGroup.addView(layout)
    }

    private fun RoomInfoBinding.scrollListener(
        snapHelper: PagerSnapHelper,
        roomItem: RoomItem
    ) = SnapOnScrollListener(snapHelper) {
        val n = roomItem.imageUrls.size
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

    private fun onOpenClickListener() {
        findNavController().navigate(resId = R.id.action_roomFragment_to_reservationFragment)
    }

    companion object {
        const val HOTEL_NAME = "hotel_name"
    }
}