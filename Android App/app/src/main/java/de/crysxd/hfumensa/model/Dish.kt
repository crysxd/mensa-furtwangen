package de.crysxd.hfumensa.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Dish(val title: String, val descriptionHtml: String, val type: String) : Parcelable