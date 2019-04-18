package co.idwall.iddog

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.toolbox.NetworkImageView
import kotlinx.android.synthetic.main.view_dog.view.*


class ListDogsAdapter(private val context: Activity, private val dogs: Array<String>) : RecyclerView.Adapter<ListDogsAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dog = dogs[position]
        // Set the NetworkImageView image
        holder.image.setImageUrl(dog, API.getInstance(context).imageLoader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.view_dog, parent, false)
        return ViewHolder(context, view)
    }

    override fun getItemCount(): Int {
        return dogs.size
    }


    class ViewHolder(itemContext: Activity, itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {

        var context: Activity = itemContext
        var image: NetworkImageView = itemView.image

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            // Zoom image
            Util.zoomImageFromThumb(context, image)
        }

    }

}
