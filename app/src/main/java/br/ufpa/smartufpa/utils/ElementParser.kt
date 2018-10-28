package br.ufpa.smartufpa.utils

import br.ufpa.smartufpa.models.overpass.Element

object ElementParser {

    fun getName(element: Element): String? {
        element.tags.let {
            return it?.name
        }
    }

    fun getShortName(element: Element): String? {
        element.tags.let {
            return it?.shortName
        }
    }

    fun getLocalName(element: Element): String? {
        element.tags.let {
            return it?.locName
        }
    }

}