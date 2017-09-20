package br.ufpa.smartufpa.utils.apptutorial;

import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import br.ufpa.smartufpa.R;
import br.ufpa.smartufpa.utils.Constants;

import com.github.amlcurran.showcaseview.OnShowcaseEventListener;
import com.github.amlcurran.showcaseview.ShowcaseView;

import java.util.ArrayList;

/**
 * Stable Commit (20/09)
 * Class responsible to create the tutorial that runs at the first use of the app
 * @author kaeuchoa
 */

public class AppTutorial implements OnShowcaseEventListener {
    private static final String TAG = AppTutorial.class.getSimpleName();
    private int itemsShown;
    private ShowcaseView sv;
    private ArrayList<ShowcaseHolder> caseHolders;
    private AppCompatActivity context;

    public AppTutorial(ArrayList<ShowcaseHolder> caseHolders, AppCompatActivity context) {
        this.caseHolders = caseHolders;
        itemsShown = 0;
        this.context = context;
        sv = new ShowcaseView.Builder(context)
                .withMaterialShowcase()
                .setStyle(R.style.CustomShowcaseTheme2)
                .setTarget(caseHolders.get(itemsShown).getTarget())
                .setShowcaseEventListener(this)
                .setContentText(caseHolders.get(itemsShown).getText())
                .build();
        if (caseHolders.get(itemsShown).getBtnPosition() == Constants.TUTORIAL_BTN_LEFT) {
            sv.setButtonPosition(positionButtonLeft());
        }
    }

    @Override
    public void onShowcaseViewHide(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewDidHide(ShowcaseView showcaseView) {
        sv = null;
        itemsShown ++;
        if (itemsShown < caseHolders.size()) {
            sv =  new ShowcaseView.Builder(context)
                    .withMaterialShowcase()
                    .setStyle(R.style.CustomShowcaseTheme2)
                    .setTarget(caseHolders.get(itemsShown).getTarget())
                    .setShowcaseEventListener(this)
                    .setContentText(caseHolders.get(itemsShown).getText())
                    .build();
            if (caseHolders.get(itemsShown).getBtnPosition() == Constants.TUTORIAL_BTN_LEFT) {
                sv.setButtonPosition(positionButtonLeft());
            }
        }
    }

    @Override
    public void onShowcaseViewShow(ShowcaseView showcaseView) {

    }

    @Override
    public void onShowcaseViewTouchBlocked(MotionEvent motionEvent) {

    }

    private RelativeLayout.LayoutParams positionButtonLeft() {
        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        int margin = ((Number) (context.getResources().getDisplayMetrics().density * 12)).intValue();
        lps.setMargins(margin, margin, margin, margin);

        return lps;
    }


}