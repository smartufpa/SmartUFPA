package br.ufpa.smartufpa.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.activities.api.CreateElementActivity
import br.ufpa.smartufpa.activities.api.old_CreateElementActivity
import br.ufpa.smartufpa.adapters.SelectCategoryAdapter
import kotlinx.android.synthetic.main.activity_select_category.*
import kotlinx.android.synthetic.main.custom_header.*

class SelectCategoryActivity : AppCompatActivity() {

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    companion object {
        val KEY_LATITUDE = "lat"
        val KEY_LONGITUDE = "long"
        val TAG = SelectCategoryActivity::class.java.name
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_category)
        // Create and attach a LayoutManager to the RecyclerView
        getIntentExtras()

        val layoutManager = LinearLayoutManager(this)
        val selectCategoryAdapter = createAdapter()

        configRecyclerView(selectCategoryAdapter, layoutManager)
        configHeader()

    }

    private fun configHeader() {
        txtHeaderTitle.text = getString(R.string.title_select_category)
        txtHeaderSubtitle.text = getString(R.string.subtitle_select_category)
        txtHeaderExtraInfo.visibility = View.GONE
    }

    private fun configRecyclerView(selectCategoryAdapter: SelectCategoryAdapter, layoutManager: LinearLayoutManager) {
        rvSelectCategories.adapter = selectCategoryAdapter
        rvSelectCategories.layoutManager = layoutManager
    }

    private fun createAdapter(): SelectCategoryAdapter {
        val selectCategoryAdapter = SelectCategoryAdapter(this)

        selectCategoryAdapter.setOnItemClickListener { _, position ->
            val placeCategories = selectCategoryAdapter.placeCategories
            val category = placeCategories[position]

            val intent = Intent(this, CreateElementActivity::class.java)
            with(intent) {
                putExtra(old_CreateElementActivity.ARG_LATITUDE, latitude)
                putExtra(old_CreateElementActivity.ARG_LONGITUDE, longitude)
                putExtra(old_CreateElementActivity.ARG_CATEGORY, category.category.toString())
                putExtra(old_CreateElementActivity.ARG_CATEGORY_NAME, category.name)
            }
            startActivity(intent)
            finish()
        }
        return selectCategoryAdapter
    }

    private fun getIntentExtras() {
        latitude = intent.getDoubleExtra(KEY_LATITUDE, 0.0)
        longitude = intent.getDoubleExtra(KEY_LONGITUDE, 0.0)
    }

}
