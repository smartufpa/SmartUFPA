package br.ufpa.smartufpa.activities.ui

import android.content.Context
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetBehavior.*
import android.view.View
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.fragments.PlaceDetailsFragment
import br.ufpa.smartufpa.fragments.SearchResultFragment
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.FragmentHelper
import br.ufpa.smartufpa.utils.UIHelper
import kotlinx.android.synthetic.main.activity_main.view.*

class BottomSheetView(private val context: Context, view: View, private val fragmentHelper: FragmentHelper) : BottomSheetBehavior.BottomSheetCallback() {

    private val bottomSheetBehavior = from(view.bottom_sheet_container)

    init {
        bottomSheetBehavior.setBottomSheetCallback(this)
        hide()
    }


    fun showPlaceDetailsFragment(elements: List<Element>) {
        val placeDetailsFragment = PlaceDetailsFragment.newInstance(elements[0])
        fragmentHelper.loadWithReplace(R.id.bottom_sheet_container, placeDetailsFragment, PlaceDetailsFragment.FRAGMENT_TAG)
        expand()
    }

    fun showSearchResultFragment(elements: List<Element>) {
        val searchResultFragment = SearchResultFragment.newInstance(ArrayList(elements))
        fragmentHelper.loadWithReplace(R.id.bottom_sheet_container, searchResultFragment, SearchResultFragment.FRAGMENT_TAG)
        expand()
    }

    override fun onSlide(bottomSheet: View, slideOffset: Float) {

    }

    override fun onStateChanged(bottomSheet: View, newState: Int) {
        when (newState) {
            STATE_HIDDEN -> {
                clearBottomSheetFragment()
            }
        }
    }

    private fun clearBottomSheetFragment() {
        UIHelper.showToastLong(context, "Clear BottomSheet")
    }

    fun isVisible(): Boolean {
        return bottomSheetBehavior.state == STATE_COLLAPSED || bottomSheetBehavior.state == STATE_EXPANDED
    }

    fun hide() {
        bottomSheetBehavior.state = STATE_HIDDEN
    }

    fun collapse() {
        bottomSheetBehavior.state = STATE_COLLAPSED
    }

    fun expand() {
        bottomSheetBehavior.state = STATE_EXPANDED
    }
}