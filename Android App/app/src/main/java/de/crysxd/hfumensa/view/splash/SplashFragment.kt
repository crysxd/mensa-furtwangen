package de.crysxd.hfumensa.view.splash

import android.content.Context
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.SELECTED_MENSA_SETTING
import de.crysxd.hfumensa.view.ToolbarMode
import de.crysxd.hfumensa.view.setToolbarMode
import de.crysxd.hfumensa.view.utils.ErrorDialogHelper
import timber.log.Timber


class SplashFragment : Fragment() {

    var shownAlertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onStart() {
        super.onStart()

        activity?.setToolbarMode(ToolbarMode.FULLSCREEN_LOADING, false)

        login { alreadyLoggedIn ->
            view?.let {
                // Upgrade the preferences, id used to be int in v1
                upgradePrefsToV2(it.context)

                if (alreadyLoggedIn) {
                    activity?.setToolbarMode(ToolbarMode.LOADING, false)
                }

                // Let user select a canteen if not already set
                val prefs = getPreferences(it.context)
                if (prefs.contains(SELECTED_MENSA_SETTING)) {
                    Navigation.findNavController(it).navigate(R.id.action_splashFragment_to_menuFragment)
                } else {
                    Navigation.findNavController(it).navigate(R.id.action_splashFragment_to_selectCanteenFragment)
                }
            }
        }
    }

    private fun login(completedAction: (Boolean) -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            completedAction(true)
        } else {
            auth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("signInAnonymously:success")
                    completedAction(false)
                } else {
                    it.exception?.let {
                        handleError(it)
                    }
                }
            }
        }
    }

    private fun getPreferences(context: Context) = PreferenceManager.getDefaultSharedPreferences(context)

    private fun upgradePrefsToV2(context: Context) {
        val prefs = getPreferences(context)
        if (prefs.all[SELECTED_MENSA_SETTING] is Int) {
            prefs.edit().putString(SELECTED_MENSA_SETTING, prefs.getInt(SELECTED_MENSA_SETTING, 0).toString()).apply()
        }
    }

    private fun handleError(error: Exception) {
        Timber.e(error)
        shownAlertDialog?.dismiss()
        shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, error, R.string.ui_error_unable_to_load_data) {
            activity?.finish()
        }
    }
}