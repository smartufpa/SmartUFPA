package br.ufpa.smartufpa.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import br.ufpa.smartufpa.fragments.forms.*
import br.ufpa.smartufpa.utils.enums.ElementCategories
import br.ufpa.smartufpa.utils.enums.ElementCategories.*

class CreateElementTabsAdapter(fm: FragmentManager,
                               private val category: ElementCategories) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? {
        when (position) {
            0 -> return FormBasicData.newInstance(null)
            1 -> return getFragmentByCategory()
            2 -> return FormExtraInfo.newInstance()
        }
        return  FormBlank.newInstance()
    }

    private fun getFragmentByCategory(): Fragment {
        return when (category) {
            FOODPLACE -> FormFoodPlace.newInstance()
            AUDITORIUM -> FormAuditorium.newInstance()
            COPYSHOP -> FormCopyShop.newInstance()
            LIBRARY -> FormLibrary.newInstance()
            TOILETS -> FormToilets.newInstance()
            DRINKING_WATER -> FormDrinkingFountain.newInstance()
        }
    }

    override fun getCount(): Int {
        // Show 3 total pages.
        return 3
    }
}