package org.mapleir.dot4j.helper.utils;


import java.awt.*;

public class Theme {

    public static Color WINDOW_COLOR = Color.white;
    public static Color ENABLED = Color.white;

    public static Color UNFOCUSED_TEXT_COLOR = Color.white;
    public static Color DISABLED_TEXT_COLOR = Color.white;

    public static Color SETTINGS_BG = Color.white;
    public static Color SETTINGS_HEADER = Color.white;

    public static Color MODULE_ENABLED_BG_HOVER = Color.white;
    public static Color MODULE_DISABLED_BG_HOVER = Color.white;

    public static Color MODULE_ENABLED_BG = Color.white;
    public static Color MODULE_DISABLED_BG = Color.white;
    public static Color MODULE_COLOR = Color.white;
    public static Color MODULE_TEXT = Color.white;

    public static Color TOGGLE_BUTTON_BG = Color.white;
    public static Color TOGGLE_BUTTON_FILL = Color.white;

    public static Color NORMAL_TEXT_COLOR = Color.white;

    public static Color MODE_SETTING_BG = Color.white;
    public static Color MODE_SETTING_FILL = Color.white;

    public static Color SLIDER_SETTING_BG = Color.white;

    public static Color CONFIG_EDIT_BG = Color.white;

    public static void dark() {
        WINDOW_COLOR = new Color(21, 22, 25);
        ENABLED = new Color(51, 112, 203);

        UNFOCUSED_TEXT_COLOR = new Color(108, 109, 113);
        //DISABLED_TEXT_COLOR = ;

        SETTINGS_BG = new Color(32, 31, 35);
        SETTINGS_HEADER = new Color(39, 38, 42);

        MODULE_ENABLED_BG_HOVER = new Color(43, 41, 45);
        MODULE_DISABLED_BG_HOVER = new Color(35, 35, 35);

        MODULE_ENABLED_BG = new Color(36, 34, 38);
        MODULE_DISABLED_BG = new Color(32, 31, 33);
        MODULE_COLOR = new Color(37, 35, 39);
        MODULE_TEXT = new Color(94, 95, 98);

        TOGGLE_BUTTON_BG = new Color(59, 60, 65);
        TOGGLE_BUTTON_FILL = new Color(29, 27, 31);

        NORMAL_TEXT_COLOR = Color.white;

        MODE_SETTING_BG = new Color(46, 45, 48);
        MODE_SETTING_FILL = new Color(32, 31, 35);

        SLIDER_SETTING_BG = new Color(73, 72, 76);

        CONFIG_EDIT_BG = new Color(20, 20, 25);
    }

    public static void darkclear() {
        WINDOW_COLOR = new Color(30, 30, 30,192);
        ENABLED = new Color(0, 0, 0);

        UNFOCUSED_TEXT_COLOR = new Color(128,128,128);
        //DISABLED_TEXT_COLOR = ;

        SETTINGS_BG = new Color(32, 31, 35,100);
        SETTINGS_HEADER = new Color(39, 38, 42,75);

        MODULE_ENABLED_BG_HOVER = new Color(43, 41, 45,150);
        MODULE_DISABLED_BG_HOVER = new Color(35, 35, 35,150);

        MODULE_ENABLED_BG = new Color(36, 34, 38,100);
        MODULE_DISABLED_BG = new Color(32, 31, 33,100);
        MODULE_COLOR = new Color(0,0,0,92);
        MODULE_TEXT = new Color(94, 95, 98);

        TOGGLE_BUTTON_BG = new Color(59, 60, 65);
        TOGGLE_BUTTON_FILL = new Color(29, 27, 31);

        NORMAL_TEXT_COLOR = new Color(255,255,255);

        MODE_SETTING_BG = new Color(46, 45, 48,150);
        MODE_SETTING_FILL = new Color(32, 31, 35,150);

        SLIDER_SETTING_BG = new Color(73, 72, 76);

        CONFIG_EDIT_BG = new Color(20, 20, 25,100);
    }
}
