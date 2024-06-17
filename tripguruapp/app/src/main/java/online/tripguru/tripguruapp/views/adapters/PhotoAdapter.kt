package online.tripguru.tripguruapp.views.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import online.tripguru.tripguruapp.R

class PhotoAdapter : RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder>() {

    private val photoUrls = mutableListOf<String>()

    class PhotoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.box_image, parent, false)
        return PhotoViewHolder(view)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val url = photoUrls[position]
        Glide.with(holder.imageView.context).load(url).into(holder.imageView)
    }

    override fun getItemCount(): Int {
        return photoUrls.size
    }

    fun setPhotoUrls(urls: List<String>?) {
        photoUrls.clear()
        if(urls != null) photoUrls.addAll(urls)
        notifyDataSetChanged()
    }
}