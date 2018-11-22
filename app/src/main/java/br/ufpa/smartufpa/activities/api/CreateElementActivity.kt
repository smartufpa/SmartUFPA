package br.ufpa.smartufpa.activities.api

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.design.widget.TabLayout
import android.support.v7.app.AppCompatActivity
import android.util.Log
import br.ufpa.smartufpa.R
import br.ufpa.smartufpa.adapters.CreateElementTabsAdapter
import br.ufpa.smartufpa.dialogs.CommentDialog
import br.ufpa.smartufpa.fragments.forms.FormBasicData
import br.ufpa.smartufpa.models.overpass.Element
import kotlinx.android.synthetic.main.activity_create_element.*
import kotlinx.android.synthetic.main.fragment_form_basic_data.*
import kotlinx.android.synthetic.main.fragment_form_extra_info.*

class CreateElementActivity : AppCompatActivity(), CommentDialog.CommentDelegate{


    private var tabsAdapter: CreateElementTabsAdapter? = null
    private val element : Element = Element()

    object Teste{
        var nome : String? = null
        var localNome : String? = null
    }

    private val teste : Teste = Teste



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

            Log.d("asd", teste.nome)
            Log.d("asd", teste.localNome)
            // todo, processar o singleton para element
            openCommentDialog()
        }

    }

    private fun openCommentDialog() {
        val commentDialog = CommentDialog()
        commentDialog.show(supportFragmentManager, CommentDialog.DIALOG_TAG)
    }

    // Btn Enviar foi pressionado
    override fun delegateComment(commentText: String) {
//        startUploadFlow(commentText)
    }



}
