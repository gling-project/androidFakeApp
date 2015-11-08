package be.gling.android.model.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by florian on 18/11/14.
 * limit the size if string
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Size {

    public int min() default 0;

    public int max() default 255;

    public int message();

}
