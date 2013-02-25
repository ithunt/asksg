package edu.rit.asksg.common;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Custom @Logger annotation *
 */
@Retention(RUNTIME)
@Target(FIELD)
@Documented
public @interface Log {
}