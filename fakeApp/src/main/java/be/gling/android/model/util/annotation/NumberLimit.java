package be.gling.android.model.util.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by florian on 15/10/15.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NumberLimit {

    public int min();

    public int max();

    public int message();
}
