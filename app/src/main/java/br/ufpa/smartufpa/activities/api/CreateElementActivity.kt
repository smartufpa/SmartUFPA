package br.ufpa.smartufpa.activities.api

import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.os.Bundle

import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.adapters.CreateElementTabsAdapter
import br.ufpa.smartufpa.dialogs.CommentDialog
import kotlinx.android.synthetic.main.activity_create_element.*

class CreateElementActivity : AppCompatActivity() {

    private var tabsAdapter: CreateElementTabsAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_element)
        val category = intent.getStringExtra(old_CreateElementActivity.ARG_CATEGORY)
        val categoryName = intent.getStringExtra(old_CreateElementActivity.ARG_CATEGORY_NAME)
        val latitude = intent.getDoubleExtra(old_CreateElementActivity.ARG_LATITUDE, 0.0)
        val longitude = intent.getDoubleExtra(old_CreateElementActivity.ARG_LONGITUDE, 0.0)

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = categoryName
        tabsAdapter = CreateElementTabsAdapter(supportFragmentManager, category)

        // Set up the ViewPager with the sections adapter.
        container.adapter = tabsAdapter

        container.addOnPageChangeListener(TabLayout.TabLayoutOnPageChangeListener(tabs))
        tabs.addOnTabSelectedListener(TabLayout.ViewPagerOnTabSelectedListener(container))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

    }

    private fun openCommentDialog() {
        val commentDialog = CommentDialog()
        commentDialog.show(supportFragmentManager, CommentDialog.DIALOG_TAG)
    }



}
