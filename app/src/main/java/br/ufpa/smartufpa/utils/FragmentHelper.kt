package br.ufpa.smartufpa.utils

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentTransaction

class FragmentHelper(private val fragmentManager: FragmentManager){
    fun loadWithReplace(containerId : Int, fragment : Fragment, tag: String){
        val ft = fragmentManager.beginTransaction()
        ft.replace(containerId, fragment, tag)
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit()
    }

    fun removeFragmentByTag(fragmentTag: String): Boolean {
        val fragment = findFragmentByTag(fragmentTag)
        if (fragment != null){
            fragmentManager.beginTransaction()
                    .remove(fragment)
                    .commit()
            return true
        }

        return false

    }

    fun clearBackStack(){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
    }

    fun findFragmentByTag(fragmentTag : String) = fragmentManager.findFragmentByTag(fragmentTag);
}