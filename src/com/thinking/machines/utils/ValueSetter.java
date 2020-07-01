package com.thinking.machines.utils;
import java.util.*;
import java.sql.*;
import java.sql.Types.*;
import com.google.gson.*;
import java.lang.reflect.*;
import java.io.*;
import java.math.*;
public class ValueSetter
{
private ValueSetter(){}
public static void setFieldValue(String dt,Field f,Object o,ResultSet rs,String aName) throws Exception
{
System.out.println(dt);
if(dt.equalsIgnoreCase("INT"))
{
	f.set(o,rs.getInt(aName));
	
}
else
if(dt.equalsIgnoreCase("bigint"))
{
	f.set(o,rs.getLong(aName));
}
else
if(dt.equalsIgnoreCase("Char")||dt.equalsIgnoreCase("varchar")||dt.equalsIgnoreCase("VARBINARY")||dt.equalsIgnoreCase("BINARY"))
{
	f.set(o,rs.getString(aName));
}
else
if(dt.equalsIgnoreCase("float"))
{
	f.set(o,rs.getFloat(aName));
}
else
if(dt.equalsIgnoreCase("double"))
{
	f.set(o,rs.getDouble(aName));
}
else
if(dt.equalsIgnoreCase("date"))
{
	if(f.getType().getName().equalsIgnoreCase("String"))
	{
	f.set(o,rs.getDate(aName).toString());
    }
	else
	if(f.getType().getName().equalsIgnoreCase("java.util.Date"))
	{
		f.set(o,new java.util.Date(rs.getDate(aName).getDate()));
	}
}
else
if(dt.equalsIgnoreCase("datetime"))
{
	if(f.getType().getName().equalsIgnoreCase("String"))
	{
    f.set(o,rs.getTimestamp(aName).toString());
    }
	else
	if(f.getType().getName().equalsIgnoreCase("java.util.Date"))
	{
		f.set(o,new java.util.Date(rs.getTimestamp(aName).getTime()));
	}
}
else
if(dt.equalsIgnoreCase("time"))
{
	if(f.getType().getName().equalsIgnoreCase("String"))
	{
   f.set(o,rs.getTime(aName).toString());
	}
	else
	if(f.getType().getName().equalsIgnoreCase("java.util.Date"))
	{
		f.set(o,new java.util.Date(rs.getTime(aName).getTime()));
	}
}
else
if(dt.equalsIgnoreCase("numeric"))
{
	f.set(o,rs.getObject(aName));
}
else
if(dt.equalsIgnoreCase("bool")||dt.equalsIgnoreCase("boolean")||dt.equalsIgnoreCase("bit"))
{
	f.set(o,rs.getBoolean(aName));
}
else
if(dt.equalsIgnoreCase("decimal")||dt.equalsIgnoreCase("dec"))
{
	f.set(o,rs.getBigDecimal(aName));
}

}




public static void setColumnValue(PreparedStatement ps,String dt,Field f,int i,Object o) throws Exception
{
System.out.println(dt);
if(dt.equalsIgnoreCase("INT"))
{
	ps.setInt(i,(Integer)f.get(o));
}
else
if(dt.equalsIgnoreCase("bigint"))
{
	ps.setLong(i,(Long)f.get(o));
}
else
if(dt.equalsIgnoreCase("Char")||dt.equalsIgnoreCase("varchar")||dt.equalsIgnoreCase("VARBINARY")||dt.equalsIgnoreCase("BINARY"))
{
	ps.setString(i,(String)f.get(o));
}
else
if(dt.equalsIgnoreCase("float"))
{
	ps.setFloat(i,(Float)f.get(o));
}
else
if(dt.equalsIgnoreCase("double"))
{
	ps.setDouble(i,(Double)f.get(o));
}
else
if(dt.equalsIgnoreCase("date"))
{
	if(f.getType().getName().equalsIgnoreCase("java.lang.String"))
	{
    ps.setDate(i,java.sql.Date.valueOf((String)f.get(o)));
	}
	else
	if(f.getType().getName().equalsIgnoreCase("java.util.Date"))
	{
		ps.setDate(i,new java.sql.Date(((java.util.Date)f.get(o)).getDate()));
	}
}
else
if(dt.equalsIgnoreCase("datetime"))
{
	if(f.getType().getName().equalsIgnoreCase("java.lang.String"))
	{
    ps.setTimestamp(i,java.sql.Timestamp.valueOf((String)f.get(o)));
	}
	else
	if(f.getType().getName().equalsIgnoreCase("java.util.Date"))
	{
		ps.setTimestamp(i,new java.sql.Timestamp(((java.util.Date)f.get(o)).getTime()));
	}
}
else
if(dt.equalsIgnoreCase("time"))
{
	if(f.getType().getName().equalsIgnoreCase("java.lang.String"))
	{
    ps.setTime(i, java.sql.Time.valueOf((String)f.get(o)));
	}
	else
	if(f.getType().getName().equalsIgnoreCase("java.util.Date"))
	{
		ps.setTime(i,new java.sql.Time(((java.util.Date)f.get(o)).getTime()));
	}
}
else
if(dt.equalsIgnoreCase("numeric"))
{
	ps.setObject(i,f.get(o),java.sql.Types.NUMERIC);
}
else
if(dt.equalsIgnoreCase("bool")||dt.equalsIgnoreCase("boolean")||dt.equalsIgnoreCase("bit"))
{
	ps.setBoolean(i,(Boolean)f.get(o));
}
else
if(dt.equalsIgnoreCase("decimal")||dt.equalsIgnoreCase("dec"))
{
	ps.setBigDecimal(i,(BigDecimal)f.get(o));
}

}
}