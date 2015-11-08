package be.gling.android.view.widget.technical;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import java.lang.annotation.Annotation;
import java.util.Date;

/**
 * Created by florian on 15/10/15.
 */
public class FieldDateView extends DateView implements InputField {

    public FieldDateView(Context context) {
        this(context, null);
    }

    public FieldDateView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            setDate(((Date) value));
        }
    }

    @Override
    public Object getValue(Class<?> type) {
        return getDate();
    }

    @Override
    public Integer control(Annotation[] declaredAnnotations) {
        return null;
    }

    @Override
    public void saveToInstanceState(Integer name, Bundle savedInstanceState) {
        if (getDate() != null) {
            savedInstanceState.putLong(name.toString(), getDate().getTime());
        }
    }

    @Override
    public void restoreFromInstanceState(Integer name, Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(name.toString())) {
            setDate(new Date(savedInstanceState.getLong(name.toString())));
        }
    }
}
