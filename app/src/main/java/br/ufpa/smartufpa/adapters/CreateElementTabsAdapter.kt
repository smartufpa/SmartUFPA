package br.ufpa.smartufpa.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.ufpa.smartufpa.fragments.forms.FormBasicData
import br.ufpa.smartufpa.fragments.forms.FormExtraInfo
import br.ufpa.smartufpa.fragments.forms.FormFoodPlace
import br.ufpa.smartufpa.utils.enums.ElementCategories

class CreateElementTabsAdapter(fm: FragmentManager,
                                 private val category: String?) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return FormBasicData.newInstance(null)
            1 -> return getFragmentByCategory()
            2 -> return FormExtraInfo.newInstance()
        }
        return null
    }

    private fun getFragmentByCategory(): Fragment? {
        when (category) {
            ElementCategories.FOODPLACE.toString() -> {
                return FormFoodPlace.newInstance()
            }

            ElementCategories.AUDITORIUM.toString() -> {
//                return FormBuilding.newInstance()
            }

            ElementCategories.COPYSHOP.toString() -> {

            }

            ElementCategories.LIBRARY.toString() -> {

            }

            ElementCategories.TOILETS.toString() -> {

            }


        }
        return null
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}