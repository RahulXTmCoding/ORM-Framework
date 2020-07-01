package com.thinking.machines.annotations;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface ForeignKey
{
	Class base();
	String columnName();
	String baseColumnName();
}