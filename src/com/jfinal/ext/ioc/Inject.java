package com.jfinal.ext.ioc;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;

@Inherited
@Retention(RUNTIME)
public @interface Inject {
}