package com.thinking.machines.annotations;
import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented

public @interface PrimaryKey
{
	
}