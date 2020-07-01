package com.thinking.machines.utils;
import com.thinking.machines.sqlDomain.*;
import java.lang.reflect.*;
import com.thinking.machines.exceptions.*;
import java.util.*;
public interface Validators
{
public void validate(Table t,Attribute a,Map<String,Field> f,Object o)throws ORMException,Exception;
}