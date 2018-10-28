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
import kotlinx.android.synthetic.main.bottom_sheet.view.*

class BottomSheetController(private val context: Context, private val view: View, private val fragmentHelper: FragmentHelper) : BottomSheetBehavior.BottomSheetCallback() {

    private val bottomSheetBehavior = from(view.bottom_sheet_container)

    init {
        bottomSheetBehavior.setBottomSheetCallback(this)
        view.fab_close_bsheet.setOnClickListener {
            hide()
        }
        hide()
    }


    fun showPlaceDetailsFragment(element: Element) {
        val placeDetailsFragment = PlaceDetailsFragment.newInstance(element)
//        fragmentHelper.loadWithReplace(R.id.bottom_sheet, placeDetailsFragment, PlaceDetailsFragment.FRAGMENT_TAG)
        expand()
    }

    fun showSearchResultFragment(elements: List<Element>) {
        val searchResultFragment = SearchResultFragment.newInstance(elements)
        setTitle("Resultados Encontrados")
        setSubTitle("Número de resultados")
        setExtraInfo("(${elements.size})")
        fragmentHelper.loadWithReplace(R.id.frame_fragment_container, searchResultFragment, SearchResultFragment.FRAGMENT_TAG)
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

    private fun setTitle(title: String) {
        with(view.txt_bsheet_title) {
            setText(title)
            visibility = View.VISIBLE
        }

    }

    private fun setSubTitle(subtitle: String) {
        with(view.txt_bsheet_subtitle) {
            setText(subtitle)
            visibility = View.VISIBLE
        }
    }

    private fun setExtraInfo(extraInfo: String) {
        with(view.txt_bsheet_extra_info){
            setText(extraInfo)
            visibility = View.VISIBLE
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