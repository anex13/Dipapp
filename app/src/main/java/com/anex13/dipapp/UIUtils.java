package com.anex13.dipapp;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

/**
 * From http://stackoverflow.com/a/28280347/1463533
 */
public class UIUtils {
    public static ImageButton getNavButtonView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return (ImageButton) toolbar.getChildAt(i);
        return null;
    }
}
