package by.bashlikovvv.hotelreservation.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.bashlikovvv.hotelreservation.databinding.ActivityMainBinding
import by.kirich1409.viewbindingdelegate.CreateMethod
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by viewBinding(createMethod = CreateMethod.INFLATE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}