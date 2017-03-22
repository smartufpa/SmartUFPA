package com.example.kaeuc.smartufpa.utils.showcaseutils;

import com.github.amlcurran.showcaseview.targets.Target;

/**
 * Created by kaeuch on 20/02/2017.
 */

public class ShowcaseHolder {
    private  Target target;
    private String text;
    private int btnPosition = 0;

    public ShowcaseHolder(Target target, String text, int btnPosition) {
        this.target = target;
        this.text = text;
        this.btnPosition = btnPosition;
    }

    public ShowcaseHolder(Target target, String text) {
        this.target = target;
        this.text = text;
    }

    public Target getTarget() {
        return target;
    }

    public String getText() {
        return text;
    }

    public int getBtnPosition() {
        return btnPosition;
    }
}
