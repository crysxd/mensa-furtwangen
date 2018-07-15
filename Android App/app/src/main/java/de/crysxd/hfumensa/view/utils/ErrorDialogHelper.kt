package de.crysxd.hfumensa.view.utils

import android.content.Context
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog

object ErrorDialogHelper {

    fun showErrorDialog(context: Context?, exception: Exception?, @StringRes suggestedErrorMessage: Int, okAction: () -> Unit = {}) = context?.let {
        showErrorDialog(context, exception, context.getText(suggestedErrorMessage), okAction)
    }

    fun showErrorDialog(context: Context?, exception: Exception?, suggestedErrorMessage: CharSequence, okAction: () -> Unit = {}) = context?.let {
        AlertDialog.Builder(context)
                .setMessage(suggestedErrorMessage)
                .setPositiveButton(android.R.string.ok, { _, _ ->
                    okAction()
                })
                .show()
    }
}
