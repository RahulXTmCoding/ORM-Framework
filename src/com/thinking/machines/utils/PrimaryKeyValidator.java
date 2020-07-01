package com.thinking.machines.utils;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
import com.thinking.machines.sqlDomain.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
public class PrimaryKeyValidator implements Validators
{
	public String selectByPrimaryKey;
    public Connection c;
    public int valType;
   // valtype==1 for insert
   // valtype==2 for update
public void validate(Table t,Attribute a,Map<String,Field> fieldMap,Object o) throws ORMException,Exception
{
PreparedStatement ps=c.prepareStatement(selectByPrimaryKey);

List<String> pk=t.getPrimaryKey();

for(int j=0;j<pk.size();j++)
{
Field f=fieldMap.get(pk.get(j));
f.setAccessible(true);
ps.setObject(j+1,f.get(o));
}
ResultSet rs=ps.executeQuery();

if(valType==1)
{
if(rs.next())
{ 
	throw new ORMException(a.getAttributeName()+" is a part of Primary Key,Please provide a unique value for the following");
}
}
if(valType==2)
{
if(rs.next())
{
	return;
}
	throw new ORMException("No record found,Please provide valid Primary Key to update");
	
}
}
}