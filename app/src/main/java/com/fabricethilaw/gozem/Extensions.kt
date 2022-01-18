package com.fabricethilaw.gozem

import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun View.showMessage(
    titleRes: Int,
    message: String,
    buttonText: Int = R.string.ok,
    action: (() -> Unit)? = null
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(resources.getString(titleRes))
        .setMessage(message)
        .setNeutralButton(
            buttonText
        ) { dialog, _ ->
            dialog.dismiss()
            action?.invoke()
        }
        .show()
}

 fun View.showMessage( messageRes: Int, onPositiveAction: () -> Unit) {

    MaterialAlertDialogBuilder(context)
        .setMessage(messageRes)
        .setCancelable(false)
        .setPositiveButton(R.string.yes) { dialog, _ ->
            dialog.dismiss()
            onPositiveAction()
        }
        .setNegativeButton(R.string.no) { dialog, _ ->
            dialog.cancel()
        }
        .show()
}