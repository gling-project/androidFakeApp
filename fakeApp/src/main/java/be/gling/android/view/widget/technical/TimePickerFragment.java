package be.gling.android.view.widget.technical;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by florian on 22/11/14.
 */
public class TimePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private Date date;
    private SetDateInterface setDateInterface;

    public TimePickerFragment() {
        this.date = new Date();
    }

    public void setSetDateInterface(SetDateInterface setDateInterface) {
        this.setDateInterface = setDateInterface;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker

        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(date.getTime());
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        int hour = c.get(Calendar.HOUR_OF_DAY);

        // Create a new instance of TimePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);


    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (setDateInterface != null) {
            setDateInterface.setDate(date);
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        date = cal.getTime();
    }
}
