package com.thinking.machines.utils;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
import com.thinking.machines.sqlDomain.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
public class ForeignKeyValidator implements Validators
{
	 public String parentTableSelectStatement;
	 public String ParentTableName;
	 public Connection c;
	 public List<String> keys;
public void validate(Table t,Attribute a,Map<String,Field> fieldMap,Object o) throws ORMException,Exception
{
	PreparedStatement ps=c.prepareStatement(parentTableSelectStatement);
   for(int j=0;j<keys.size();j++)
  {
Field f=fieldMap.get(keys.get(j));
f.setAccessible(true);
ps.setObject(j+1,f.get(o));
}
	ResultSet rs=ps.executeQuery();

if(rs.next())
{
	return;
}
throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+" References table "+ ParentTableName+ ",No record found for given Foreign Key");	

}
}