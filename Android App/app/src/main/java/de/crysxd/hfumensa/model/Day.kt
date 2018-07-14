package de.crysxd.hfumensa.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
class Day(val date: Date, val menus: List<Dish>) : Parcelable