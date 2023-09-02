package by.bashlikovvv.hotelreservation.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.RoomsListItemBinding
import by.bashlikovvv.hotelreservation.domain.model.RoomItem
import by.bashlikovvv.hotelreservation.domain.model.Rooms
import by.bashlikovvv.hotelreservation.presentation.contract.SnapOnScrollListener
import com.google.android.material.chip.Chip
import kotlin.math.abs

class RoomsListAdapter(
    private val onClick: (RoomItem) -> Unit
) : RecyclerView.Adapter<RoomsListAdapter.RoomsListHolder>() {

    private lateinit var layoutInflater: LayoutInflater

    private var rooms: Rooms = Rooms()

    inner class RoomsListHolder(
        private val binding: RoomsListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(roomItem: RoomItem) {
            binding.navLayout.selectRoomBtn.text = getStringRes(R.string.about_room, "")
            binding.roomName.text = roomItem.name
            binding.roomPrice.text = getStringRes(R.string.currency, roomItem.price.toString())
            binding.additionalTitle.text = roomItem.pricePer
            binding.navLayout.selectRoomBtn.setOnClickListener { onClick(roomItem) }
            roomItem.peculiarities.forEach { addUsability(it) }
            setUpImagesRecyclerView(roomItem)
        }

        private fun getStringRes(@StringRes resId: Int, data: String): String {
            return layoutInflater.context.getString(resId, data)
        }

        private fun addUsability(usability: String) {
            val layout = layoutInflater.inflate(R.layout.usability_item, binding.chipGroup, false)

            val textView = layout.findViewById<Chip>(R.id.usabilityText)
            textView.text = usability

            binding.chipGroup.addView(layout)
        }

        private fun setUpImagesRecyclerView(room: RoomItem) {
            binding.recyclerView.recyclerView.apply {
                var idx = 0
                room.imageUrls.forEach { _ ->
                    addDotIndicator(idx)
                    idx++
                }
                onFlingListener = null
                val snapHelper = PagerSnapHelper()
                snapHelper.attachToRecyclerView(this)
                layoutManager =
                    LinearLayoutManager(layoutInflater.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ImagesListAdapter().also {
                    it.setImages(room.imageUrls)
                }
                binding.recyclerView.recyclerView.addOnScrollListener(scrollListener(snapHelper, room))
            }
        }

        private fun scrollListener(
            snapHelper: PagerSnapHelper,
            room: RoomItem
        ) = SnapOnScrollListener(snapHelper) {
            val n = room.imageUrls.size
            for (i in 0 until n) {
                if (i == it) {
                    binding.recyclerView.dotsLinearLayout[it].alpha = 1f
                } else {
                    binding.recyclerView.dotsLinearLayout[i].alpha = calcAlpha(n, it, i)
                }

            }
        }

        private fun calcAlpha(n: Int, pos: Int, i: Int): Float {
            return 0.2f + (n - 1 - abs(pos - i)) * (1 - 0.2f) / (n - 1)
        }

        private fun addDotIndicator(idx: Int) {
            val layout = layoutInflater.inflate(
                R.layout.dot_indicator_item,
                binding.recyclerView.dotsLinearLayout,
                false
            )

            binding.recyclerView.dotsLinearLayout.addView(layout, idx)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomsListHolder {
        layoutInflater = LayoutInflater.from(parent.context)

        val binding = RoomsListItemBinding.inflate(layoutInflater, parent, false)

        return RoomsListHolder(binding)
    }

    override fun getItemCount(): Int = rooms.rooms.size

    override fun onBindViewHolder(holder: RoomsListHolder, position: Int) {
        val room = rooms.rooms[position]
        holder.onBind(room)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setRooms(rooms: Rooms) {
        this.rooms = rooms
        notifyDataSetChanged()
    }
}