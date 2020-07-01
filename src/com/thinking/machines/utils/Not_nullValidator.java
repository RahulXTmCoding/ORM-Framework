package com.thinking.machines.utils;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
import com.thinking.machines.sqlDomain.*;
import java.lang.reflect.*;
import java.util.*;
public class Not_nullValidator implements Validators
{
public void validate(Table t,Attribute a,Map<String,Field> fieldMap,Object o) throws ORMException,Exception
{
if(fieldMap.get(a.getAttributeName()).get(o)==null)
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" cannot be null,Please provide value for the following");
}
if((fieldMap.get(a.getAttributeName()).get(o).toString()).length()==0)
{
throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" cannot be Empty value,Please provide value for the following");	
}
}
}