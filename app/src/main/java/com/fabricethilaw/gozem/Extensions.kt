package com.fabricethilaw.gozem

import android.view.View
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun View.showMessage(titleRes: Int, message: String) {
    MaterialAlertDialogBuilder(context)
        .setTitle(resources.getString(titleRes))
        .setMessage(message)
        .setNeutralButton(
            R.string.ok
        ) { dialog, _ ->
            dialog.dismiss()
        }
        .show()
}