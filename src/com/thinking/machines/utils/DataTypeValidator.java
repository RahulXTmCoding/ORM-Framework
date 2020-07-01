package com.thinking.machines.utils;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
import com.thinking.machines.sqlDomain.*;
import java.lang.reflect.*;
import java.util.*;
public class DataTypeValidator implements Validators
{
public void validate(Table t,Attribute a,Map<String,Field> fieldMap,Object o) throws ORMException,Exception
{
String dt=a.getDataType();
Field f=fieldMap.get(a.getAttributeName());
String fName=f.getType().getName();
if(dt.equalsIgnoreCase("INT") && (fName.equalsIgnoreCase("int")==false && fName.equalsIgnoreCase("java.lang.Integer")==false))
{
	
throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	
	
}
else
if(dt.equalsIgnoreCase("bigint")  && (fName.equalsIgnoreCase("long")==false && fName.equalsIgnoreCase("java.lang.Long")==false))
{
throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	
	
}
else
if((dt.equalsIgnoreCase("Char")||dt.equalsIgnoreCase("varchar")||dt.equalsIgnoreCase("VARBINARY")||dt.equalsIgnoreCase("BINARY")) && (fName.equalsIgnoreCase("char")==false && fName.equalsIgnoreCase("java.lang.Character")==false && fName.equalsIgnoreCase("java.lang.String")==false) )
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}
else
if(dt.equalsIgnoreCase("float")  && (fName.equalsIgnoreCase("float")==false && fName.equalsIgnoreCase("java.lang.Float")==false))
{
throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}
else
if(dt.equalsIgnoreCase("double")  && (fName.equalsIgnoreCase("double")==false && fName.equalsIgnoreCase("java.lang.Double")==false))
{
throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}
else
if(dt.equalsIgnoreCase("date")  && (fName.equalsIgnoreCase("String")==false && fName.equalsIgnoreCase("java.util.Date")==false))
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}
else
if(dt.equalsIgnoreCase("datetime") && (fName.equalsIgnoreCase("String")==false && fName.equalsIgnoreCase("java.util.Date")==false))
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}
else
if(dt.equalsIgnoreCase("time") && (fName.equalsIgnoreCase("String")==false && fName.equalsIgnoreCase("java.util.Date")==false))
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}
else
if((dt.equalsIgnoreCase("bool")||dt.equalsIgnoreCase("boolean")||dt.equalsIgnoreCase("bit")) && (fName.equalsIgnoreCase("boolean")==false && fName.equalsIgnoreCase("java.lang.Boolean")==false))
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}
else
if((dt.equalsIgnoreCase("decimal")||dt.equalsIgnoreCase("dec")) && (fName.equalsIgnoreCase("decimal")==false && fName.equalsIgnoreCase("java.math.BigDecimal")==false))
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" has type "+dt+" But Value found of type "+f.getType().getName());	

}

}

}