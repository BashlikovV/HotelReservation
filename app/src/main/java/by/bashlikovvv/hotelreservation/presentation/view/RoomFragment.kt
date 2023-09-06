package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.domain.model.Item
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentRoomBinding
import by.bashlikovvv.hotelreservation.presentation.adapters.RoomsListAdapter
import by.bashlikovvv.hotelreservation.presentation.viewmodel.RoomFragmentViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import com.hannesdorfmann.adapterdelegates4.ListDelegationAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

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

        binding.roomFragmentRV.layoutManager = LinearLayoutManager(
            requireContext(), RecyclerView.VERTICAL, false
        )
        val adapters = ListDelegationAdapter(
            RoomsListAdapter()
                .roomsListAdapter { onOpenClickListener() }
        )
        collectRooms(adapters)
        collectProgressVisibility()
    }

    private fun collectProgressVisibility() {
        lifecycleScope.launch {
            viewModel.updateVisibility.collectLatest {
                if (it) {
                    binding.roomFragmentRV.visibility = View.GONE
                    binding.progressCircular.visibility = View.VISIBLE
                } else {
                    binding.roomFragmentRV.visibility = View.VISIBLE
                    binding.progressCircular.visibility = View.GONE
                }
            }
        }
    }

    private fun collectRooms(adapters: ListDelegationAdapter<List<Item>>) {
        lifecycleScope.launch {
            viewModel.rooms.collectLatest { rooms ->
                adapters.items = rooms.rooms.filter { !it.isEmpty() }
                binding.roomFragmentRV.adapter = adapters
            }
        }
    }

    private fun onOpenClickListener() {
        findNavController().navigate(resId = R.id.action_roomFragment_to_reservationFragment)
    }

    companion object {
        const val HOTEL_NAME = "hotel_name"
    }
}