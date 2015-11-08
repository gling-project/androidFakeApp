package be.gling.android.view.widget.technical;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.EditText;

import java.lang.annotation.Annotation;
import java.util.Date;

import be.gling.android.model.util.annotation.NotNull;
import be.gling.android.model.util.annotation.NumberLimit;
import be.gling.android.model.util.annotation.Pattern;
import be.gling.android.model.util.annotation.Size;
import be.gling.android.model.util.exception.MyException;

/**
 * Created by florian on 15/10/15.
 */
public class FieldEditText extends EditText implements InputField {

    public FieldEditText(Context context) {
        this(context, null);
    }

    public FieldEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setValue(Object value) {
        if (value != null) {
            setText(value.toString());
        } else {
            setText("");
        }
    }

    @Override
    public Object getValue(Class<?> type) {
        Editable content = getText();
        if (content != null) {

            if (type == String.class) {
                return content.toString();
            } else if (type == Long.class) {
                return Long.parseLong(content.toString());
            } else if (type == Integer.class) {
                return Integer.parseInt(content.toString());
            } else if (type == Double.class) {
                return Double.parseDouble(content.toString());
            } else if (type == Date.class) {
                return ((DateView) content).getDate();
            }
        }
        return content.toString();
    }

    @Override
    public Integer control(Annotation[] declaredAnnotations) {

        //brows annotation
        Integer error = null;
        if (declaredAnnotations != null) {
            for (Annotation annotation : declaredAnnotations) {

                // order by priority
                if (error == null && annotation instanceof NotNull) {
                    //control
                    if (getText() == null) {
                        error = ((NotNull) annotation).message();
                    }
                }
                if (error == null && annotation instanceof Size) {
                    int min = ((Size) annotation).min();
                    int max = ((Size) annotation).max();
                    Editable text = getText();
                    if ((min > 0 && (text == null || text.toString().length() < min)) ||
                            text != null && text.toString().length() > max) {
                        error = ((Size) annotation).message();
                    }
                }
                if (error == null && annotation instanceof Pattern) {
                    Editable text = getText();
                    String textInString = "";
                    if (text != null) {
                        textInString = text.toString();
                    }
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(((Pattern) annotation).regex());
                    if (!pattern.matcher(textInString).find()) {
                        error = ((Pattern) annotation).message();
                    }
                }
                if (error == null && annotation instanceof NumberLimit) {
                    Editable text = getText();
                    try {
                        Double number = Double.parseDouble(text.toString());
                        if (number == null || number < ((NumberLimit) annotation).min() || number > ((NumberLimit) annotation).max()) {
                            error = ((NumberLimit) annotation).message();
                        }
                    } catch (NumberFormatException e) {
                        error = ((NumberLimit) annotation).message();
                    }
                }
            }
        }

        return error;
    }

    @Override
    public void saveToInstanceState(Integer name, Bundle savedInstanceState) {
        if (getText() != null && !getText().toString().equals("")) {
            savedInstanceState.putString(name.toString(), getText().toString());
        }
    }

    @Override
    public void restoreFromInstanceState(Integer name, Bundle savedInstanceState) {
        if (savedInstanceState.containsKey(name.toString())) {
            setValue(savedInstanceState.getString(name.toString()));
        }
    }

    public void addSetInputs(Integer inputType, Class<?> type, boolean multiline) throws MyException {
        //add param to editText
        if (inputType != null) {
            setInputType(inputType);
        } else {
            if (type.isAssignableFrom(Integer.class)) {
                setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (type.isAssignableFrom(Long.class) ||
                    type.isAssignableFrom(Double.class)) {
                setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
            } else if (type.isAssignableFrom(String.class)) {
                if (multiline) {
                    setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                    setLines(3);
                    setGravity(Gravity.TOP);
                } else {
                    setInputType(InputType.TYPE_CLASS_TEXT);
                }
            } else {
                throw new MyException("cannot threat type " + type);
            }
        }
    }
}
