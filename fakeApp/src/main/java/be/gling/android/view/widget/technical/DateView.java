package be.gling.android.view.widget.technical;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by florian on 17/01/15.
 */
public class DateView extends Button {

    private Date date;
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    final TimePickerFragment newFragment = new TimePickerFragment();

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DateView(final Activity activity) {
        super(activity);
        initialization(activity);
    }

    public void initialization(final Activity activity) {
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                newFragment.show(activity.getFragmentManager(), "timePicker");
            }
        });
        newFragment.setSetDateInterface(new SetDateInterface() {
            @Override
            public void setDate(Date date) {
                DateView.this.setDate(date, true);
            }
        });
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        setDate(date, false);
    }

    private void setDate(Date date, boolean fromFragment) {
        this.date = date;
        final SimpleDateFormat format1 = new SimpleDateFormat(DATE_FORMAT);

        this.setText(format1.format(date));
        if (!fromFragment) {
            newFragment.setDate(date);
        }
    }
}
