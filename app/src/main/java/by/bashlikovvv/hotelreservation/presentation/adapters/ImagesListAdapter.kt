package by.bashlikovvv.hotelreservation.presentation.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import by.bashlikovvv.hotelreservation.databinding.ImagesListItemBinding
import com.bumptech.glide.Glide

class ImagesListAdapter : RecyclerView.Adapter<ImagesListAdapter.ImagesListHolder>() {

    private var images = emptyList<String>()

    class ImagesListHolder(
        val binding: ImagesListItemBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesListHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ImagesListItemBinding.inflate(inflater, parent, false)

        return ImagesListHolder(binding)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: ImagesListHolder, position: Int) {
        val imageUrl = images[position]
        with(holder.binding) {
            setImageWithGlide(imageUrl, image)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setImages(images: List<String>) {
        this.images = images
        notifyDataSetChanged()
    }

    private fun setImageWithGlide(url: String, view: ImageView) {
        Glide.with(view)
            .load(url)
            .centerCrop()
            .into(view)
    }
}