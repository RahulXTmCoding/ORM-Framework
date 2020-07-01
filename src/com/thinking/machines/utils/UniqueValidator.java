package com.thinking.machines.utils;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
import com.thinking.machines.sqlDomain.*;
import java.lang.reflect.*;
import java.sql.*;
import java.util.*;
public class UniqueValidator implements Validators
{
	public String selectByUniqueValue;
    public Connection c;
    public int valType;
    public List<String> pks;
    // valtype==1 for insert
   // valtype==2 for update

public void validate(Table t,Attribute a,Map<String,Field> fieldMap,Object o) throws ORMException,Exception
{

PreparedStatement ps=c.prepareStatement(selectByUniqueValue);
ps.setObject(1,fieldMap.get(a.getAttributeName()).get(o));
ResultSet rs=ps.executeQuery(); 
if(valType==1)
{
if(rs.next())
{
	throw new ORMException("Table :"+t.getTableName()+" ,"+a.getAttributeName()+"  unique constraint is applied,Please provide a unique value for the following");
}
}
if(valType==2)
{
	if(rs.next())
	{

		for(int i=0;i<pks.size();i++)
		{
			String pk=pks.get(i);
			if(rs.getObject(pk).equals(fieldMap.get(pk).get(o))==false)
			{
				throw new ORMException("Table :"+t.getTableName()+" , "+a.getAttributeName()+"  unique constraint is applied,Given value is used by some other record");

			}
		}
	}
}
}
}