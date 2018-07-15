package de.crysxd.hfumensa.view.selectcanteen

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import de.crysxd.hfumensa.model.Canteen
import kotlinx.android.synthetic.main.item_canteen.*
import timber.log.Timber

class CanteenAdapter(val clickAction: (View, Int) -> Unit) : RecyclerView.Adapter<CanteenViewHolder>() {

    var canteens: List<Canteen> = emptyList()
        set(value) {
            Timber.d("New data received with ${value.size} items")
            field = value
            notifyDataSetChanged()
        }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = CanteenViewHolder(parent)

    override fun getItemCount() = canteens.size

    override fun getItemId(position: Int) = canteens[position].id.toLong()

    override fun onBindViewHolder(holder: CanteenViewHolder, position: Int) {
        holder.textViewName.text = canteens[position].name
        holder.textViewPlace.text = canteens[position].place
        holder.itemView.setOnClickListener {
            clickAction(holder.itemView, position)
        }
        Glide.with(holder.itemView.context)
                .load(canteens[position].iconUrl)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(holder.imageLogo)
    }
}