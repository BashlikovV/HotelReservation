package by.bashlikovvv.hotelreservation.presentation.adapters

import android.view.LayoutInflater
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.domain.model.Item
import by.bashlikovvv.domain.model.RoomItem
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.RoomInfoBinding
import by.bashlikovvv.hotelreservation.presentation.contract.SnapOnScrollListener
import com.google.android.material.chip.Chip
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding
import kotlin.math.abs

class RoomsListAdapter {

    private lateinit var layoutInflater: LayoutInflater

    fun roomsListAdapter(onClickListener: (RoomItem) -> Unit): AdapterDelegate<List<Item>> =
        adapterDelegateViewBinding<RoomItem, Item, RoomInfoBinding>(
            { layoutInflater, parent ->
                this.layoutInflater = layoutInflater
                RoomInfoBinding.inflate(layoutInflater, parent, false)
            }
        ) {
            binding.navLayout.selectRoomBtn.setOnClickListener {
                onClickListener(item)
            }
            val snapHelper = PagerSnapHelper()
            binding.roomImagesRV.apply {
                layoutManager = LinearLayoutManager(
                    context, RecyclerView.HORIZONTAL, false
                )
                onFlingListener = null
                snapHelper.attachToRecyclerView(this)
            }

            bind {
                binding.roomImagesRV.adapter = ListDelegationAdapter(
                    ImagesListAdapter().imagesListAdapter()
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
}