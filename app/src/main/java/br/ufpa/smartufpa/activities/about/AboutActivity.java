package br.ufpa.smartufpa.activities.about;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.adapters.AboutUsAdapter;
import br.ufpa.smartufpa.models.AboutUsOption;

/**
 * Stable Commit (20/09)
 * Simple Activity to show pertinent information about the app
 * @author kaeuchoa
 */

public class AboutActivity extends AppCompatActivity {
    public static final String ACTION_ABOUT = "smartufpa.ACTION_ABOUT";
    public static final String CATEGORY_ABOUT = "smartufpa.CATEGORY_ABOUT";

    public static final String TAG = AboutActivity.class.getSimpleName();

    private RecyclerView rcvAboutOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        rcvAboutOptions = findViewById(R.id.rcv_about_options);
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        rcvAboutOptions.setLayoutManager(llm);

        ArrayList<AboutUsOption> optionsList = new ArrayList<>();
        // About Project
        final String titleProject = getString(R.string.about_opt_title_project);
        final String subtitleProject = getString(R.string.about_opt_subtitle_project);
        final Drawable iconProject = getResources().getDrawable((R.drawable.ic_launcher));

        final AboutUsOption optionProject = new AboutUsOption(titleProject, subtitleProject, iconProject);
        optionsList.add(optionProject);

        // About Code
        final String titleCode = getString(R.string.about_opt_title_code);
        final String subtitleCode = getString(R.string.about_opt_subtitle_code);
        final Drawable iconCode = getResources().getDrawable((R.drawable.ic_code));

        final AboutUsOption optionCode = new AboutUsOption(titleCode, subtitleCode, iconCode);
        optionsList.add(optionCode);

        AboutUsAdapter aboutUsAdapter = new AboutUsAdapter(optionsList,this);
        aboutUsAdapter.setOnItemClickListener(new AboutUsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = null;
                switch (position){
                    case 0:
                        intent = new Intent(AboutActivity.this,AboutProjectActivity.class);
                        break;
                    case 1:
                        intent = new Intent(AboutActivity.this,AboutCodeActivity.class);
                        break;
                }
                if(intent != null)
                    startActivity(intent);
            }
        });

        rcvAboutOptions.setAdapter(aboutUsAdapter);

    }
}
