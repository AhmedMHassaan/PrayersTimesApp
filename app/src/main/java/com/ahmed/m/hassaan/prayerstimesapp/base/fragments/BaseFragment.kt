package com.ahmed.m.hassaan.prayerstimesapp.base.fragments

import android.content.DialogInterface
import android.graphics.Color
import android.graphics.Typeface
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.core.fragments.CoreFragment
import com.ahmed.m.hassaan.prayerstimesapp.R

abstract class BaseFragment() : CoreFragment() {








    fun showAlertDialog(
        title: String = getString(R.string.alert),
        msg: String,
        positiveText: String? = null, positiveListener: DialogInterface.OnClickListener? = null,
        negativeText: String? = null, negativeListener: DialogInterface.OnClickListener? = null,
        neutralText: String? = null, neutralListener: DialogInterface.OnClickListener? = null
    ) {
        activity?.let {
            AlertDialog.Builder(it)
                .apply {
                    setTitle(title)
                    setMessage(msg)
                    positiveText?.let {
                        setPositiveButton(it, positiveListener)
                    }
                    negativeText?.let {
                        setNegativeButton(it, negativeListener)
                    }
                    neutralText?.let {
                        setNeutralButton(it, neutralListener)
                    }

                    val dialog = this.create()
                    dialog.show()

                    dialog.getButton(AlertDialog.BUTTON_POSITIVE).apply {
                        setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
                        typeface = Typeface.DEFAULT
                    }
                    dialog.getButton(AlertDialog.BUTTON_NEGATIVE).apply {
                        setTextColor(Color.RED)
                        typeface = Typeface.DEFAULT
                    }
                    dialog.getButton(AlertDialog.BUTTON_NEUTRAL).apply {
                        setTextColor(Color.BLACK)
                        typeface = Typeface.DEFAULT
                    }


                }

        }

    }





}