package com.brant.weatherapp.utils

import android.app.Activity
import android.app.AlertDialog
import android.view.View
import com.brant.weatherapp.R
import com.google.android.material.snackbar.Snackbar

object DialogManager {

  private var progressBar: AlertDialog? = null

  fun toggleProgressBar(activity: Activity, show: Boolean) {
    if (show) {
      val view = View.inflate(activity, R.layout.view_progress_bar, null)
      if (progressBar == null) {
        progressBar = AlertDialog.Builder(activity, R.style.ProgressDialogTheme).create().apply {
          setView(view)
          setCanceledOnTouchOutside(false)
          setCancelable(false)
        }
      }
      if (progressBar?.isShowing == false) {
        progressBar?.show()
      }
    } else {
      progressBar?.dismiss()
    }
  }

  fun showSnackbar(activity: Activity, message: String) {
    val root = activity.findViewById<View>(android.R.id.content)
    if (root != null) {
      Snackbar.make(root, message, Snackbar.LENGTH_LONG).show()
    }
  }
}