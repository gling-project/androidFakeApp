package be.gling.android.view.widget.technical;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Objects;

import be.gling.android.R;
import be.gling.android.view.widget.spinner.SingleSelectionSpinner;
import be.gling.android.view.widget.spinner.Writable;

/**
 * Created by florian on 15/10/15.
 */
public class FieldSelect<T> extends Spinner implements InputField {

    private List<T> keys;

    public FieldSelect(Context context, int arrayString, List<T> keys) {
        this(context, arrayString, keys, null);
    }

    public FieldSelect(Context context, int arrayString, List<T> keys, AttributeSet attrs) {
        super(context, attrs);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                arrayString, android.R.layout.simple_spinner_item);

        this.setAdapter(adapter);

        this.keys = keys;
    }


    @Override
    public void setValue(Object key) {
        super.setSelection(keys.indexOf(key));
    }

    @Override
    public Object getValue(Class<?> type) {
        return keys.get(super.getSelectedItemPosition());
    }

    @Override
    public Integer control(Annotation[] declaredAnnotations) {
//        int selectedItemPosition = super.getSelectedItemPosition();
//        Object selectedItem = super.getSelectedItem();
//        long selectedItemId = super.getSelectedItemId();
//        if ( super.getSelectedItemPosition()== 0) {
//            return R.string.error_select_something;
//        }
        return null;
    }

    @Override
    public void saveToInstanceState(Integer name, Bundle savedInstanceState) {
//        if (super.getSelectedItemPosition() != 0) {
            savedInstanceState.putInt(name.toString(), super.getSelectedItemPosition());
//        }
    }

    @Override
    public void restoreFromInstanceState(Integer name, Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(name.toString())) {
            super.setSelection(savedInstanceState.getInt(name.toString()));
        }
    }

    public static class Content {
        private String key;
        private int trans;

        public Content(String key, int trans) {
            this.key = key;
            this.trans = trans;
        }

    }
}
