package by.bashlikovvv.hotelreservation.presentation.adapters

import by.bashlikovvv.hotelreservation.databinding.ImagesListItemBinding
import com.bumptech.glide.Glide
import com.hannesdorfmann.adapterdelegates4.AdapterDelegate
import com.hannesdorfmann.adapterdelegates4.dsl.adapterDelegateViewBinding

class ImagesListAdapter {

    fun imagesListAdapter(): AdapterDelegate<List<Any>> =
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
}