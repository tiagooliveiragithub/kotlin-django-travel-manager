package online.tripguru.tripguruapp.views.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import online.tripguru.tripguruapp.R

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val photoItems = mutableListOf<Any>()

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.box_image, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = photoItems[position]
        if (item is String) {
            Glide.with(holder.imageView.context).load(item).into(holder.imageView)
        } else if (item is Uri) {
            Glide.with(holder.imageView.context).load(item).into(holder.imageView)
        }
    }

    override fun getItemCount(): Int {
        return photoItems.size
    }

    fun setPhotoItems(items: List<Any>?) {
        photoItems.clear()
        if (items != null) photoItems.addAll(items)
        notifyDataSetChanged()
    }

    fun addPhotoItem(item: Any) {
        photoItems.add(item)
        notifyItemInserted(photoItems.size - 1)
    }
}