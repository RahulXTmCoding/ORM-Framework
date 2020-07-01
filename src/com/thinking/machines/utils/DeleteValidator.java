package com.thinking.machines.utils;
import com.thinking.machines.sqlDomain.*;
import java.lang.reflect.*;
import com.thinking.machines.exceptions.*;
import java.util.*;
import java.sql.*;

public class DeleteValidator
{
	public List<String> psList=new LinkedList<>();
	public List<String> refTables=new LinkedList<>();
	public List<String> pks;
	public Connection c;
public void validate(Table t,Map<String,Field> f,Object o)throws ORMException,Exception
{
for(int i=0;i<psList.size();i++)
{

String ps=psList.get(i);
PreparedStatement pss=c.prepareStatement(ps);
for(int j=0;j<pks.size();j++)
{
	pss.setObject(j+1,f.get(pks.get(j)).get(o));
} 
ResultSet rs=pss.executeQuery();
if(rs.next())
{
	throw new ORMException("Table :"+t.getTableName()+", Referenced By table "+ refTables.get(i)+ " ,so following record cannot be deleted");	

}
}

}
}