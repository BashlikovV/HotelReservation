package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import by.bashlikovvv.hotelreservation.R
import by.bashlikovvv.hotelreservation.databinding.FragmentSuccessBinding
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Random

@AndroidEntryPoint
class SuccessFragment : Fragment() {

    private val binding: FragmentSuccessBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        (requireActivity() as AppCompatActivity).supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.navigation_icon)
            title = getString(R.string.success)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val orderNum = Random().nextInt(Int.MAX_VALUE)
        binding.successText.text = getString(R.string.order_text, orderNum.toString())
        binding.include.selectRoomBtn.apply {
            text = getString(R.string.super_btn_text)
            setOnClickListener {
                findNavController().popBackStack(R.id.hotelFragment, true)
//                findNavController().navigate(R.id.action_successFragment_to_testFragment)
            }
        }
    }
}