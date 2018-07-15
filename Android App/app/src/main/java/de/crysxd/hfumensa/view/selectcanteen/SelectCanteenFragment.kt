package de.crysxd.hfumensa.view.selectcanteen

import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.SELECTED_MENSA_SETTING
import de.crysxd.hfumensa.model.Canteen
import de.crysxd.hfumensa.persistence.CanteenRepository
import de.crysxd.hfumensa.view.utils.ErrorDialogHelper
import kotlinx.android.synthetic.main.fragment_select_canteen.*


class SelectCanteenFragment : Fragment(), OnMapReadyCallback {

    private var shownAlertDialog: AlertDialog? = null
    private val adapter = CanteenAdapter { view, position ->
        onCanteenSelected(position)
        storeCanteenAndContinue(view)
    }
    private val snapHelper = ControllablePagerSnapHelper {
        onCanteenSelected(it)
    }
    var map: GoogleMap? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View = inflater.inflate(R.layout.fragment_select_canteen, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onStart() {
        super.onStart()

        recyclerView.adapter = adapter
        snapHelper.attachToRecyclerView(recyclerView)

        buttonNext.setOnClickListener {
            snapHelper.snapToNext()
        }
        buttonPrevious.setOnClickListener {
            snapHelper.snapToPrevious()
        }
        buttonContinue.setOnClickListener {
            storeCanteenAndContinue(it)
        }

        val startTime = System.currentTimeMillis()
        val (result, error) = CanteenRepository().getCanteens()
        result.observe(this, Observer {
            adapter.canteens = it.sortedBy {
                it.place
            }
            onCanteenSelected(0)
        })
        error.observe(this, Observer {
            shownAlertDialog?.dismiss()
            shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, it, R.string.ui_error_unable_to_load_data)
        })
    }

    private fun storeCanteenAndContinue(view: View) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(view.context)
        prefs.edit().putString(SELECTED_MENSA_SETTING, adapter.canteens[snapHelper.snappedPosition].id).apply()
        Navigation.findNavController(view).navigate(R.id.action_selectCanteenFragment_to_menuFragment)
    }

    private fun onCanteenSelected(position: Int) {
        view?.let {
            showCanteenOnMap(adapter.canteens[position])
            TransitionManager.beginDelayedTransition(it as ViewGroup)
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
            buttonContinue.visibility = if (adapter.itemCount == 0) {
                View.GONE
            } else {
                View.VISIBLE
            }
            waveHeader.visibility = if (adapter.itemCount == 0) {
                View.VISIBLE
            } else {
                View.GONE
            }
            textViewHeading.visibility = buttonContinue.visibility
            recyclerView.visibility = buttonContinue.visibility
            if (adapter.itemCount == 0 && !waveHeader.isRunning) {
                waveHeader.start()
            } else if (adapter.itemCount != 0 && waveHeader.isRunning) {
                waveHeader.stop()
            }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        if (adapter.itemCount > 0) {
            showCanteenOnMap(adapter.canteens[snapHelper.snappedPosition], false)
        }
    }

    private fun showCanteenOnMap(canteen: Canteen, animated: Boolean = true) {
        val location = LatLng(canteen.latitude.toDouble(), canteen.longitude.toDouble())
        map?.addMarker(MarkerOptions().position(location).title(canteen.name))
        val update = CameraUpdateFactory.newLatLngZoom(location, 15f)
        if (animated) {
            map?.animateCamera(update)
        } else {
            map?.moveCamera(update)
        }
    }
}