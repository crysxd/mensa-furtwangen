package de.crysxd.hfumensa.view.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.persistence.MenuRepository
import de.crysxd.hfumensa.view.ErrorDialogHelper
import java.util.*

class MenuFragment : Fragment() {

    var shownAlertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_menu, container, false)

    override fun onStart() {
        super.onStart()

        val (result, error) = MenuRepository().getMenu("630", Date())
        result.observe(this, Observer {
            Toast.makeText(context, "${it.size} dishes loaded", Toast.LENGTH_LONG).show()
        })
        error.observe(this, Observer {
            shownAlertDialog?.dismiss()
            shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, it, R.string.ui_error_unable_to_load_data)
        })

    }
}