package application.common;

import static java.lang.annotation.ElementType.CONSTRUCTOR;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Target(value = {CONSTRUCTOR})
@Retention(value = RUNTIME)
public @interface Default {
}
