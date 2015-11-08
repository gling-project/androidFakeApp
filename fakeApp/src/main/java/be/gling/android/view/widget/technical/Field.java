package be.gling.android.view.widget.technical;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import be.gling.android.R;
import be.gling.android.model.util.exception.MyException;

/**
 * Created by florian on 17/11/14.
 */
public class Field extends LinearLayout {

    private final Activity activity;
    private FieldProperties fieldProperties;

    public Field(final Activity activity, FieldProperties fieldProperties) {
        this(activity, fieldProperties, null);
    }

    public Field(final Activity activity, FieldProperties fieldProperties, Object defaultValue) {
        super(activity);

        //activity
        this.activity = activity;
        this.fieldProperties = fieldProperties;

        //build field
        try {
            buildField();

            //assign default value
            if (defaultValue != null) {
                setValue(defaultValue);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * set a value into the field
     *
     * @param value
     */
    public void setValue(Object value) {
        fieldProperties.getInputView().setValue(value);
    }

    /**
     * build the inputView type
     *
     * @throws MyException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private void buildField() throws MyException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        //build layout
        setOrientation(VERTICAL);
        setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        //create inflater
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//(LinearLayout) findViewById(R.id.ppp);

        // **** BUILD WIDGET ****

        //for each field : create a view and insert it
        View view = inflater.inflate(R.layout.form_line, null);
        addView(view);
        View errorView = view.findViewById(R.id.field_error_message);
        //hide the errorView message
        errorView.setVisibility(GONE);

        ViewGroup contentView = (ViewGroup) view.findViewById(R.id.insert_point);

        //text view
        TextView labelText = (TextView) view.findViewById(R.id.label);//(TextView) view.findViewById(R.id.label));
        labelText.setText(fieldProperties.translationId);

        //build the input
        //the inputView can already be created => nothing to do.
        if (fieldProperties.inputView == null) {

            //date
            if (fieldProperties.field.getType().isAssignableFrom(Date.class)) {

                fieldProperties.inputView = new FieldDateView(activity);

            } else if (fieldProperties.field.getType().isAssignableFrom(Boolean.class)) {

                //build the view
                fieldProperties.inputView = new FieldCheckBox(activity);
            } else {

                // **** OTHER **** => use edit text
                //build edit text
                fieldProperties.inputView = (InputField) inflater.inflate(R.layout.field_text, null);

                ((FieldEditText) fieldProperties.inputView).addSetInputs(fieldProperties.inputType, fieldProperties.field.getType(), fieldProperties.isMultiline());
            }
        }

        //add to the map
        fieldProperties.errorView = errorView;
        fieldProperties.questionView = view;

        contentView.addView((View) fieldProperties.inputView);
    }


    /**
     * control the value
     *
     * @return
     */
    public boolean control() {

        boolean valid = true;

        if(getVisibility() != VISIBLE){
            return true;
        }

        // **** CONTROL ****
        Integer error = null;
        if (fieldProperties.field != null) {
            error = fieldProperties.inputView.control(fieldProperties.field.getDeclaredAnnotations());
        }
        else if(fieldProperties.errorController!=null){
            error = fieldProperties.errorController.test(fieldProperties.inputView.getValue(fieldProperties.expectedType));
        }

        if (error != null) {
            ((TextView) fieldProperties.errorView.findViewById(R.id.field_error_message)).setText(activity.getString(error));
            fieldProperties.errorView.findViewById(R.id.field_error_message).setVisibility(VISIBLE);
            valid = false;
        } else {
            fieldProperties.errorView.findViewById(R.id.field_error_message).setVisibility(GONE);
        }
        return valid;
    }

    public Object getValue() {

        if (!control()) {
            return null;
        }

        return fieldProperties.inputView.getValue(fieldProperties.expectedType);
    }

    public void setEnabled(boolean enabled) {
        fieldProperties.inputView.setEnabled(enabled);
    }

    public FieldProperties getFieldProperties() {
        return fieldProperties;
    }

    public void saveToInstanceState(Bundle savedInstanceState) {
        this.fieldProperties.inputView.saveToInstanceState(this.fieldProperties.translationId, savedInstanceState);
    }

    public void restoreFromInstanceState(Bundle savedInstanceState) {
        this.fieldProperties.inputView.restoreFromInstanceState(this.fieldProperties.translationId, savedInstanceState);
    }


    public static class FieldProperties {

        private java.lang.reflect.Field field;
        private Class<?> expectedType;
        private final int translationId;
        public boolean listMultipleResponse;


        //private elements
        private Integer inputType = null;
        private View questionView;
        private View errorView;
        private InputField inputView;
        private boolean multiline = false;
        private ErrorController errorController;

        public FieldProperties(InputField inputView, Class<?> expectedType, int translationId, ErrorController errorController) {
            this.inputView = inputView;
            this.expectedType = expectedType;
            this.translationId = translationId;
            this.errorController = errorController;
        }

        public FieldProperties(java.lang.reflect.Field field, int translationId) {
            this.field = field;
            this.translationId = translationId;
        }

        public FieldProperties(java.lang.reflect.Field field, int translationId, Integer inputType) {
            this.field = field;
            this.translationId = translationId;
            this.inputType = inputType;
        }

        public FieldProperties(java.lang.reflect.Field field, int translationId, InputField inputView) {
            this.field = field;
            this.translationId = translationId;
            this.inputView = inputView;
        }

        public int getTranslationId() {
            return translationId;
        }

        public java.lang.reflect.Field getField() {
            return field;
        }

        public InputField getInputView() {
            return inputView;
        }

        public boolean isMultiline() {
            return multiline;
        }

        public void setMultiline(boolean multiline) {
            this.multiline = multiline;
        }


        @Override
        public String toString() {
            return "FieldProperties{" +
                    ", translationId=" + translationId +
                    ", listMultipleResponse=" + listMultipleResponse +
                    ", inputType=" + inputType +
                    ", questionView=" + questionView +
                    ", errorView=" + errorView +
                    ", inputView=" + inputView +
                    '}';
        }

        /*
        accept current value of the field, return a translation value if not valid
         */
        public static interface ErrorController{
            Integer test(Object value);
        }
    }


}
