package de.crysxd.hfumensa.view.selectcanteen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import androidx.recyclerview.widget.RecyclerView
import de.crysxd.hfumensa.R
import kotlinx.android.extensions.LayoutContainer

class CanteenViewHolder(parent: ViewGroup) :
        RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_canteen, parent, false)), LayoutContainer {
    override val containerView = itemView
}