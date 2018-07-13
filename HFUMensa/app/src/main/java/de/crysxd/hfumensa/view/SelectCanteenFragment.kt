package de.crysxd.hfumensa.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.model.Canteen
import de.crysxd.hfumensa.persistence.CanteenRepository

class SelectCanteenFragment : Fragment() {

    var shownAlertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_select_canteen, container, false)

    override fun onStart() {
        super.onStart()

        val (result, error) = CanteenRepository().getCanteens()
        result.observe(this, Observer<List<Canteen>> {
            Toast.makeText(context, "${it.size} canteens loaded", Toast.LENGTH_LONG).show()
        })
        error.observe(this, Observer<Exception> {
            shownAlertDialog?.dismiss()
            shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, it, R.string.ui_error_unable_to_load_data)
        })
    }
}