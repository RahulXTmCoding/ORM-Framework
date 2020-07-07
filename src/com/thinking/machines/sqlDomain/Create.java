package com.thinking.machines.sqlDomain;
import java.util.*;
import java.sql.*;
import java.lang.reflect.*;
import java.lang.*;
import com.thinking.machines.annotations.*;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
import com.thinking.machines.framework.*;
public class Create
{
private String tableName;
private String createStatement;
private boolean columnsExecuted=false;
private TMORMFramework orm;
public Connection connection;

public List<String> columns=new LinkedList<>();
public Create(String tableName,TMORMFramework orm) throws ORMException,Exception{
connection=orm.c;
this.tableName=tableName;
this.orm=orm;
prepareInitialStatement();
}

public void prepareInitialStatement() throws ORMException,Exception
{
Statement s=connection.createStatement();
try
{s.executeQuery("select * from "+tableName);
}catch(Exception e)
{
	throw new ORMException(e.toString());
}
createStatement="create table "+tableName+"(";
}


public Create addColumn(String name)throws ORMException
{
if(columns.contains(name)==true) throw new ORMException("Cannot assign Same column name to more then one column");
if(columnsExecuted==true) createStatement+=" ,";
columns.add(name);
createStatement+=name+" ";
return this;
}
public Create type(String type)
{
	createStatement+=type+" ";
	return this;
}
public Create notNull()
{

createStatement+="not null ";
return this;
}
public Create auto_increment()
{
	createStatement+="auto increment ";
	return this;

}
public Create unique()
{
	createStatement+="unique ";
	return this;
}

public Create primaryKey(String keys)
{

createStatement+=",";
createStatement+= " PRIMARY KEY (";
createStatement+=keys;
createStatement+=")";
return this;

}
public Create foreignKey(String keys,String ptName,String ptcNames)
{
	createStatement+="FOREIGN KEY ("+keys+")"+" REFERENCES "+ptName+"("+ptcNames+")";
	return this;
}
public void create()throws ORMException
{
try{
createStatement+=");";

Statement s=connection.createStatement();
s.executeUpdate(createStatement);
orm.addToDs(tableName);

}catch(SQLException se)
{
	throw new ORMException(se.toString());
}
}

}
