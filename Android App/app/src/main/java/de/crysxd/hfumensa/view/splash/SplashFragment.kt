package de.crysxd.hfumensa.view.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import de.crysxd.hfumensa.R
import de.crysxd.hfumensa.view.ErrorDialogHelper
import timber.log.Timber


class SplashFragment : Fragment() {

    var shownAlertDialog: AlertDialog? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.fragment_splash, container, false)

    override fun onStart() {
        super.onStart()

        login {
            view?.let {
                Navigation.findNavController(it).navigate(R.id.action_splashFragment_to_selectCanteenFragment)
            }
        }
    }

    private fun login(completedAction: () -> Unit) {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            completedAction()
        } else {
            auth.signInAnonymously().addOnCompleteListener {
                if (it.isSuccessful) {
                    Timber.d("signInAnonymously:success")
                    completedAction()
                } else {
                    it.exception?.let {
                        handleError(it)
                    }
                }
            }
        }
    }

    private fun handleError(error: Exception) {
        Timber.e(error)
        shownAlertDialog?.dismiss()
        shownAlertDialog = ErrorDialogHelper.showErrorDialog(context, error, R.string.ui_error_unable_to_load_data, {
            activity?.finish()
        })
    }
}