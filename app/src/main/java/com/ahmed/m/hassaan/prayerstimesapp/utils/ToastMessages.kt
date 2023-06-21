package com.ahmed.m.hassaan.prayerstimesapp.utils

import androidx.core.content.res.ResourcesCompat
import com.ahmed.m.hassaan.prayerstimesapp.R
import com.ahmed.m.hassaan.prayerstimesapp.base.App
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages.ToastTags.TOAST_ERROR
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages.ToastTags.TOAST_INFO
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages.ToastTags.TOAST_NORMAL
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages.ToastTags.TOAST_SUCCESS
import com.ahmed.m.hassaan.prayerstimesapp.utils.ToastMessages.ToastTags.TOAST_WARNING
import es.dmoral.toasty.Toasty

object ToastMessages {


    private object ToastTags {
        const val TOAST_ERROR = "error"
        const val TOAST_INFO = "info"
        const val TOAST_NORMAL = "normal"
        const val TOAST_SUCCESS = "success"
        const val TOAST_WARNING = "warning"

    }

    private fun messageWithConfig(msg: String, toastyType: String? = "info") {
        Toasty.Config.getInstance()
            .also {

                val typeface = ResourcesCompat.getFont(App.mACTIVITY, R.font.font1)
                if (typeface != null) it.setToastTypeface(typeface)

                it
                    .tintIcon(true)
                    .allowQueue(false)
                    .setTextSize(
                        App.mACTIVITY.resources.getDimension(com.intuit.sdp.R.dimen._5sdp).toInt()
                    )
                    .apply()
            }


        when (toastyType) {
            TOAST_ERROR -> Toasty.error(App.mACTIVITY, msg, Toasty.LENGTH_SHORT, true).show()
            TOAST_INFO -> Toasty.info(App.mACTIVITY, msg, Toasty.LENGTH_SHORT, true).show()
            TOAST_NORMAL -> Toasty.normal(App.mACTIVITY, msg, Toasty.LENGTH_SHORT).show()
            TOAST_SUCCESS -> Toasty.success(App.mACTIVITY, msg, Toasty.LENGTH_SHORT, true).show()
            TOAST_WARNING -> Toasty.warning(App.mACTIVITY, msg, Toasty.LENGTH_SHORT, true).show()
            else -> {}
        }
    }

    fun msg(msg: String) {
        messageWithConfig(msg, TOAST_SUCCESS)
    }

    fun error(msg: String) {
        messageWithConfig(msg, TOAST_ERROR)
    }

}