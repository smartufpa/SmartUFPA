package br.ufpa.smartufpa.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.ufpa.smartufpa.fragments.forms.ElementBasicDataForm
import br.ufpa.smartufpa.fragments.forms.FormFoodPlace
import br.ufpa.smartufpa.models.PlaceCategory

class CreateElementTabsAdapter(fm: FragmentManager,
                                 private val category: String?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return ElementBasicDataForm.newInstance(null)
            1 -> return getFragmentByCategory()
        }
        return null
    }

    private fun getFragmentByCategory(): Fragment? {
        when (category) {
            PlaceCategory.Categories.FOODPLACE.toString() -> {
                return FormFoodPlace.newInstance()
            }

            PlaceCategory.Categories.BUILDING.toString() -> {
//                return FormBuilding.newInstance()
            }
        }
        return null
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}