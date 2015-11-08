package be.gling.android.model.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import be.gling.android.R;

/**
 * Created by florian on 18/11/14.
 * control if the field is not null
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNull {
    public int message() default R.string.g_error_not_null_default;
}
