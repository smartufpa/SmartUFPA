package br.ufpa.smartufpa.models

import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.utils.enums.ElementCategories
import br.ufpa.smartufpa.utils.enums.ElementCategories.*
import java.io.Serializable

/**
 * Created by kaeuchoa on 13/12/2017.
 */

class ElementCategoryItem(category: ElementCategories) : Serializable {

    var name: String? = null
    var drawable: Int = 0

    init {
        when (category) {
            FOODPLACE -> {
                this.drawable = R.drawable.ic_marker_food_place
                this.name = "Refeições"
            }
            AUDITORIUM -> {
                this.drawable = R.drawable.ic_marker_auditorium
                this.name = "Auditório"
            }
            TOILETS -> {
                this.drawable = R.drawable.ic_marker_restroom
                this.name = "Banheiro"
            }
            COPYSHOP -> {
                this.drawable = R.drawable.ic_marker_copyshop
                this.name = "Xerox"
            }
            LIBRARY ->{
                this.drawable = R.drawable.ic_marker_library
                this.name = "Biblioteca"
            }
            DRINKING_FOUNTAIN -> {
                this.drawable = R.drawable.ic_mtrl_chip_close_circle
                this.name = "Bebedouro"
            }
        }
    }


}
