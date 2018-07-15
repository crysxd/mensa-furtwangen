package de.crysxd.hfumensa.view.selectcanteen

import android.os.Bundle
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.PagerSnapHelper
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.persistence.CanteenRepository
import de.crysxd.hfumensa.view.ErrorDialogHelper
import kotlinx.android.synthetic.main.fragment_select_canteen.*
import androidx.recyclerview.widget.RecyclerView



class SelectCanteenFragment : Fragment(), OnMapReadyCallback {

    var shownAlertDialog: AlertDialog? = null
    val adapter = CanteenAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_select_canteen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()

        recyclerView.adapter = adapter
        val snapHelper = object: PagerSnapHelper() {
            override fun findTargetSnapPosition(layoutManager: RecyclerView.LayoutManager, velocityX: Int, velocityY: Int): Int {
                val snapPosition = super.findTargetSnapPosition(layoutManager, velocityX, velocityY)
                onCanteenSelected(snapPosition)
                return snapPosition
            }
        }
        snapHelper.attachToRecyclerView(recyclerView)

        val (result, error) = CanteenRepository().getCanteens()
        result.observe(this, Observer {
            adapter.canteens = it.sortedBy {
                it.name
            }
            onCanteenSelected(0)
        })
        error.observe(this, Observer {
            shownAlertDialog?.dismiss()
            shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, it, R.string.ui_error_unable_to_load_data)
        })
    }

    fun onCanteenSelected(position: Int) {
        TransitionManager.beginDelayedTransition(view as ViewGroup)
        buttonNext.visibility = if (adapter.itemCount == 0 || position >= adapter.itemCount - 1) {
            View.GONE
        } else {
            View.VISIBLE
        }
        buttonPrevious.visibility = if (adapter.itemCount == 0 || position <= 0) {
            View.GONE
        } else {
            View.VISIBLE
        }
        buttonContinue.visibility =  if (adapter.itemCount == 0) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    override fun onMapReady(map: GoogleMap) {
        val sydney = LatLng(-34.0, 151.0)
        map.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        map.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }
}