package com.thinking.machines.annotations;
import java.lang.annotation.*;
@Target({ElementType.TYPE,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface TableName
{
	String value();
}