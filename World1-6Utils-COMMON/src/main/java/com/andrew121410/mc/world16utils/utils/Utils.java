package com.andrew121410.mc.world16utils.utils;

public class Utils extends com.andrew121410.ccutils.utils.Utils {

    // Used for centering text on signs.
    public static String centerText(String text, int max) {
        if (text.length() > max)
            return text.substring(0, max);
        else {
            int pad = max - text.length();
            StringBuilder sb = new StringBuilder(text);
            for (int i = 0; i < pad; i++)
                if (i % 2 == 0)
                    sb.insert(0, " ");
                else
                    sb.append(" ");
            return sb.toString();
        }
    }
}
