package me.xemu.fragment.hook;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FragmentHookData {
	boolean required() default false;
	String name() default "";
}
