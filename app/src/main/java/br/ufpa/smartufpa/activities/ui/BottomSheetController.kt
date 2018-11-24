package br.ufpa.smartufpa.activities.ui

import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.*
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.view.View
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.utils.FragmentHelper
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.bottom_sheet.view.*
import kotlinx.android.synthetic.main.custom_header.view.*

class BottomSheetController(private val view: View, private val fragmentHelper: FragmentHelper) : BottomSheetBehavior.BottomSheetCallback() {

    private val bottomSheetBehavior = from(view.bottom_sheet_container)

    init {
        bottomSheetBehavior.setBottomSheetCallback(this)
        view.fab_close_bsheet.setOnClickListener {
            hide()
            fragmentHelper.clearBackStack()
        }
        hide()
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {
    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
    }

    fun setTitle(title: String) {
        with(view.txtHeaderTitle) {
            setText(title)
            visibility = View.VISIBLE
        }

    }

    fun setSubTitle(subtitle: String) {
        with(view.txtHeaderSubtitle) {
            setText(subtitle)
            visibility = View.VISIBLE
        }
    }

    fun setExtraInfo(extraInfo: String) {
        with(view.txtHeaderExtraInfo) {
            setText(extraInfo)
            visibility = View.VISIBLE
        }

    }

    private fun clearBottomSheetFragment() {

    }

    fun isVisible(): Boolean {
        return bottomSheetBehavior.state == STATE_COLLAPSED || bottomSheetBehavior.state == STATE_EXPANDED
    }

    fun hide() {
        bottomSheetBehavior.state = STATE_HIDDEN
        view.fab_close_bsheet.visibility = View.GONE
    }

    fun collapse() {
        bottomSheetBehavior.state = STATE_COLLAPSED
    }

    fun expand() {
        bottomSheetBehavior.state = STATE_EXPANDED
        view.fab_close_bsheet.visibility = View.VISIBLE
    }
}