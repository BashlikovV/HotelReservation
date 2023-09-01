package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentRoomBinding
import by.bashlikovvv.hotelreservation.domain.model.RoomItem
import by.bashlikovvv.hotelreservation.presentation.adapters.RoomsListAdapter
import by.bashlikovvv.hotelreservation.presentation.viewmodel.RoomFragmentViewModel
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RoomFragment : Fragment() {

    private val binding: FragmentRoomBinding by viewBinding(CreateMethod.INFLATE)

    private val viewModel: RoomFragmentViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = RoomsListAdapter { onOpenClickListener(it) }
        binding.roomsList.adapter = adapter
        val dividerItemDecoration = DividerItemDecoration(
            binding.roomsList.context,
            RecyclerView.VERTICAL
        )
        binding.roomsList.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.roomsList.addItemDecoration(dividerItemDecoration)
        lifecycleScope.launch {
            viewModel.rooms.collectLatest {
                adapter.setRooms(it.value)
            }
        }
    }

    private fun onOpenClickListener(roomItem: RoomItem) {
        val args = bundleOf(ReservationFragment.ARG_ROOM to roomItem)
        findNavController().navigate(
            resId = R.id.action_roomFragment_to_reservationFragment,
            args = args
        )
    }
}