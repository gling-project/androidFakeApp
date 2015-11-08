package be.gling.android.view.widget.technical;

import android.os.Bundle;

import java.lang.annotation.Annotation;

/**
 * Created by florian on 15/10/15.
 */
public interface InputField {

    public void setValue(Object value);

    Object getValue(Class<?> type);

    void setEnabled(boolean enabled);

    Integer control(Annotation[] declaredAnnotations);

    void saveToInstanceState(Integer name, Bundle savedInstanceState);

    void restoreFromInstanceState(Integer name, Bundle savedInstanceState);
}
