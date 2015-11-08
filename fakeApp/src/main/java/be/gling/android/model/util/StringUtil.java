package be.gling.android.model.util;

import java.text.DecimalFormat;

/**
 * Created by florian on 18/11/14.
 * Some function for string
 */
public class StringUtil {

    public static String toFirstLetterUpper(String s) {
        return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    public static String toDouble(Double value, boolean decimal) {
        DecimalFormat df;
        if (decimal) {
            df = new DecimalFormat("0.00");
        } else {
            df = new DecimalFormat("0");

        }
        return df.format(value);

    }



}
