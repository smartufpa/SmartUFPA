package br.ufpa.smartufpa.interfaces

import br.ufpa.smartufpa.models.overpass.Element

interface PlaceDetailsDelegate {
    fun showPlaceDetailsFragment(element : Element)
}