package br.ufpa.smartufpa.models;

import android.graphics.drawable.Drawable;

/**
 * Created by kaeuc on 07/12/2017.
 */

public class AboutUsOption {

    private String title;
    private String subtitle;
    private Drawable icon;

    public AboutUsOption(String title, String subtitle, Drawable icon) {
        this.title = title;
        this.subtitle = subtitle;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}
