package be.gling.android.view.widget.customer;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by florian on 14/10/15.
 */
public class CustomTextView extends TextView {


    private static Typeface typeface = null;
    private static Typeface typefaceBold = null;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont();
    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont();
    }

    private void setCustomFont() {

        if (typeface == null) {
            typeface = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(), "fonts/brandon_re.ttf");
        }
        if (typefaceBold == null) {
            typefaceBold = Typeface.createFromAsset(getContext().getApplicationContext().getAssets(), "fonts/brandon_bld.ttf");
        }


        if(this.getTypeface().isBold()){
            this.setTypeface(typefaceBold);
        }
        else{
            this.setTypeface(typeface);
        }
    }
}
