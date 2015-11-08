package be.gling.android.view.widget.spinner;

/**
 * Created by florian on 22/11/14.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import be.gling.android.R;

public class MultiSelectionSpinner<T extends Writable> extends Spinner implements OnMultiChoiceClickListener {

    private final List<T> _items = new ArrayList<T>();
    boolean[] mSelection = null;

    ArrayAdapter<String> simple_adapter;

    public MultiSelectionSpinner(Context context) {
        this(context, null);
    }

    public MultiSelectionSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);

        simple_adapter = new ArrayAdapter<String>(context,
                android.R.layout.simple_spinner_item);
        super.setAdapter(simple_adapter);
    }

    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
        if (mSelection != null && which < mSelection.length) {
            mSelection[which] = isChecked;

            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        } else {
            throw new IllegalArgumentException(
                    "Argument 'which' is out of bounds.");
        }
    }

    @Override
    public boolean performClick() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setMultiChoiceItems(itemString(), mSelection, this);

        Button button = new Button(getContext());
        builder.setView(button);
        button.setText(R.string.g_close);

        final AlertDialog alertDialog = builder.show();

        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        return true;
    }

    private String[] itemString() {
        String[] listString = new String[_items.size()];

        for (int i = 0; i < _items.size(); i++) {
            listString[i] = _items.get(i).getString();
        }

        return listString;
    }

    @Override
    public void setAdapter(SpinnerAdapter adapter) {
        throw new RuntimeException(
                "setAdapter is not supported by MultiSelectSpinner.");
    }

    /*
        public void setItems(RoommateDTO[] items) {
            _items = items;
            mSelection = new boolean[_items.length];
            simple_adapter.clear();
            simple_adapter.add(_items[0].getFirstName());
            Arrays.fill(mSelection, false);
        }
    */
    public void setItems(List<T> items) {
        _items.clear();
        _items.addAll(items);
        mSelection = new boolean[_items.size()];
        simple_adapter.clear();
        Arrays.fill(mSelection, false);
    }

    /*
        public void setSelection(String[] selection) {
            for (String cell : selection) {
                for (int j = 0; j < _items.length; ++j) {
                    if (_items[j].equals(cell)) {
                        mSelection[j] = true;
                    }
                }
            }
        }

        public void setSelection(List<String> selection) {
            for (int i = 0; i < mSelection.length; i++) {
                mSelection[i] = false;
            }
            for (String sel : selection) {
                for (int j = 0; j < _items.length; ++j) {
                    if (_items[j].equals(sel)) {
                        mSelection[j] = true;
                    }
                }
            }
            simple_adapter.clear();
            simple_adapter.add(buildSelectedItemString());
        }
    */
    public void setSelection(int index) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        if (index >= 0 && index < mSelection.length) {
            mSelection[index] = true;
        } else {
            throw new IllegalArgumentException("Index " + index
                    + " is out of bounds.");
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public void setSelection(List<T> selectedIndicies) {
        for (int i = 0; i < mSelection.length; i++) {
            mSelection[i] = false;
        }
        for (int i = 0; i < selectedIndicies.size(); i++) {
            T index = selectedIndicies.get(i);
            for (T item : _items) {
                if (item.equals(index)) {
                    mSelection[i] = true;
                }
            }
        }
        simple_adapter.clear();
        simple_adapter.add(buildSelectedItemString());
    }

    public List<T> getSelectedItems() {
        List<T> selection = new LinkedList<T>();
        for (int i = 0; i < _items.size(); ++i) {
            if (mSelection[i]) {
                selection.add(_items.get(i));
            }
        }
        return selection;
    }

    /*
        public List<Integer> getSelectedIndices() {
            List<Integer> selection = new LinkedList<Integer>();
            for (int i = 0; i < _items.length; ++i) {
                if (mSelection[i]) {
                    selection.add(i);
                }
            }
            return selection;
        }
    */
    private String buildSelectedItemString() {
        StringBuilder sb = new StringBuilder();
        boolean foundOne = false;

        for (int i = 0; i < _items.size(); ++i) {
            if (mSelection[i]) {
                if (foundOne) {
                    sb.append(", ");
                }
                foundOne = true;

                sb.append(_items.get(i).getString());
            }
        }
        return sb.toString();
    }

    public String getSelectedItemsAsString() {
        return buildSelectedItemString();
    }
}
