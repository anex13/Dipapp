package com.anex13.dipapp;
import android.support.v7.widget.Toolbar;
import android.widget.ImageButton;

/**
 * From http://stackoverflow.com/a/28280347/1463533
 */

// TODO: 23.09.2016 разобраться нахуя ано мне надо было ?
public class UIUtils {
    public static ImageButton getNavButtonView(Toolbar toolbar) {
        for (int i = 0; i < toolbar.getChildCount(); i++)
            if (toolbar.getChildAt(i) instanceof ImageButton)
                return (ImageButton) toolbar.getChildAt(i);
        return null;
    }
}
