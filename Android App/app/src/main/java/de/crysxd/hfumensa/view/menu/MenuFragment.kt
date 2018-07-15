package de.crysxd.hfumensa.view.menu

import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.SELECTED_MENSA_SETTING
import de.crysxd.hfumensa.persistence.MenuRepository
import de.crysxd.hfumensa.view.ToolbarMode
import de.crysxd.hfumensa.view.getToolbar
import de.crysxd.hfumensa.view.setToolbarMode
import de.crysxd.hfumensa.view.utils.ErrorDialogHelper
import java.util.*

class MenuFragment : Fragment() {

    var shownAlertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_menu, container, false)

    override fun onStart() {
        super.onStart()

        activity?.setToolbarMode(ToolbarMode.LOADING)
        activity?.getToolbar()?.inflateMenu(R.menu.menu_fragment_menu)
        activity?.getToolbar()?.setOnMenuItemClickListener {
            if (it.itemId == R.id.menuChangeCanteen) {
                showCanteenSelection()
            }
            true
        }

        val selectedCanteen = PreferenceManager.getDefaultSharedPreferences(context).getString(SELECTED_MENSA_SETTING, null)
                ?: return showCanteenSelection(false)
        activity?.title = selectedCanteen
        val (result, error) = MenuRepository().getMenu(selectedCanteen, Date())
        result.observe(this, Observer {
            activity?.setToolbarMode(ToolbarMode.IDLE)
            Toast.makeText(context, "${it.size} dishes loaded", Toast.LENGTH_LONG).show()
        })
        error.observe(this, Observer {
            activity?.setToolbarMode(ToolbarMode.IDLE)
            shownAlertDialog?.dismiss()
            shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, it, R.string.ui_error_unable_to_load_data)
        })

    }

    override fun onStop() {
        super.onStop()
        activity?.getToolbar()?.menu?.clear()
    }

    private fun showCanteenSelection(allowBackNavigation: Boolean = true) {
        view?.let {
            if (allowBackNavigation) {
                Navigation.findNavController(it).navigate(R.id.action_menuFragment_to_selectCanteenFragment)
            } else {
                Navigation.findNavController(it).navigate(R.id.action_menuFragment_to_selectCanteenFragment_no_back)
            }
        }
    }
}