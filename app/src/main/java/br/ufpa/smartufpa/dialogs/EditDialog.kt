package br.ufpa.smartufpa.dialogs

import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.res.Resources
import android.support.design.widget.TextInputEditText
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.models.overpass.Element
import br.ufpa.smartufpa.utils.ElementParser
import br.ufpa.smartufpa.utils.UIHelper
import kotlinx.android.synthetic.main.dialog_edit_view.view.*
import java.util.*

class EditDialog(private val context: Context, private val viewGroup: ViewGroup, private val resources: Resources){


    companion object {
        val TAG = EditDialog::class.simpleName
    }
    private val elementParser : ElementParser = ElementParser

    private val editViewLayout = createLayout()
    private val spinnerOpeningDay = editViewLayout.spinnerOpeningDay
    private val spinnerClosingDay = editViewLayout.spinnerClosingDay
    private val inputOpeningTime = editViewLayout.input_openingtime
    private val inputClosingTime = editViewLayout.input_closingtime
    private val inputName = editViewLayout.input_name
    private val inputWebsite = editViewLayout.input_website
    private val inputDescription = editViewLayout.input_description


    fun open(element : Element){
        val title = resources.getString(R.string.title_edit) + "${elementParser.getName(element)}"
        setupDialog(title, element)
        setupSpinnersFields()
        setupOpeningTimeField()
        setupClosingTimeField()

    }

    private fun setupSpinnersFields() {
        val daysOfWeekAdapter = ArrayAdapter.createFromResource(context, R.array.days_of_week, android.R.layout.simple_spinner_dropdown_item)
        spinnerOpeningDay.adapter = daysOfWeekAdapter
        spinnerClosingDay.adapter = daysOfWeekAdapter
    }

    private fun setupOpeningTimeField() {
        inputOpeningTime.setOnClickListener {
            showTimePicker(it as TextInputEditText)
        }
    }

    private fun setupClosingTimeField() {
        inputClosingTime.setOnClickListener {
            showTimePicker(it as TextInputEditText)
        }
    }

    private fun showTimePicker(input : TextInputEditText){
        val c = Calendar.getInstance()
        val mHour = c[Calendar.HOUR_OF_DAY]
        val mMinute = c[Calendar.MINUTE]
       TimePickerDialog(context, TimePickerDialog
               .OnTimeSetListener { _, hourOfDay, minute -> input.setText("${hourOfDay}:$minute") },
               mHour, mMinute, true).show()

    }

    private fun setupDialog(title: String, element: Element) {
        val website = elementParser.getWebsite(element)
        val description = elementParser.getDescription(element)
        inputName.setText(elementParser.getName(element))

        if(website != null) inputWebsite.setText(website)
        if (description != null) inputDescription.setText(description)



        AlertDialog.Builder(context)
                .setTitle(title)
                .setPositiveButton(resources.getString(R.string.btn_confirm)) { _, _ ->
                    val closingTimeText = inputClosingTime.text.toString()
                    val openingTimeText = inputOpeningTime.text.toString()
                    val nameText = inputName.text.toString()
                    val descriptionText = inputDescription.text.toString()
                    val websiteText = inputWebsite.text.toString()
                    val openingDayText = spinnerOpeningDay.selectedItem.toString()
                    val closingDayText = spinnerClosingDay.selectedItem.toString()

                    Log.d(TAG,"[$closingDayText,$openingDayText,$websiteText,$descriptionText," +
                            "$nameText, $openingDayText, $openingTimeText, $closingTimeText]")

                }
                .setNegativeButton(resources.getString(R.string.btn_cancel), null)
                .setView(editViewLayout)
                .show()
    }

    private fun createLayout() : View {
        val editView = LayoutInflater.from(context)
                        .inflate(R.layout.dialog_edit_view, viewGroup, false)
        return editView
    }
}