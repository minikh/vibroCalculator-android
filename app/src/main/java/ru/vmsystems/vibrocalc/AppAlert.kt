package ru.vmsystems.vibrocalc

import android.support.design.widget.Snackbar
import android.widget.TextView

class AppAlert {
    companion object {
        fun showErrorAlert(message : String, view: TextView){
            Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                    .setAction("AppAlert", null).show()
        }
    }
}
