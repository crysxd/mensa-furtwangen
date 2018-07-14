package de.crysxd.hfumensa.view.selectcanteen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.model.Canteen
import de.crysxd.hfumensa.persistence.CanteenRepository
import de.crysxd.hfumensa.view.ErrorDialogHelper
import kotlinx.android.synthetic.main.fragment_select_canteen.*

class SelectCanteenFragment : Fragment() {

    var shownAlertDialog: AlertDialog? = null
    val adapter = CanteenAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_select_canteen, container, false)

    override fun onStart() {
        super.onStart()

        recyclerView.adapter = adapter

        val (result, error) = CanteenRepository().getCanteens()
        result.observe(this, Observer<List<Canteen>> {
            adapter.canteens = it
        })
        error.observe(this, Observer<Exception> {
            shownAlertDialog?.dismiss()
            shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, it, R.string.ui_error_unable_to_load_data)
        })
    }
}