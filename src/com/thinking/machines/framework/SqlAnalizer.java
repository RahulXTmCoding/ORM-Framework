package com.thinking.machines.framework;
import java.sql.*;
import com.google.gson.*;
import java.lang.reflect.*;
import java.io.*;
import java.util.*;
import com.thinking.machines.sqlDomain.*;
import com.thinking.machines.beans.*;
public class SqlAnalizer
{
public static Connection getDataBaseMetaData(Map<String,Table> tables){
try
{
File f=new File("./dbConfig.json");
FileInputStream fis = new FileInputStream(f);
byte[] data = new byte[(int) f.length()];
fis.read(data);
fis.close();
String str = new String(data, "UTF-8");	
Gson g=new Gson();
DbConfigBean dbobj=g.fromJson(str,DbConfigBean.class);

Class.forName(dbobj.getDriver());
Connection c=DriverManager.getConnection(dbobj.getConnection_url()+dbobj.getDatabase(),dbobj.getUsername(),dbobj.getPassword());
DatabaseMetaData dbmd=c.getMetaData();

ResultSet tablesResultset=dbmd.getTables(dbobj.getDatabase(),null,null,new String[]{"TABLE"});
while(tablesResultset.next())
{
Table table=new Table();
table.setTableName(tablesResultset.getString("TABLE_NAME"));
table.setTableType(tablesResultset.getString("TABLE_TYPE"));
ResultSet columns=dbmd.getColumns(dbobj.getDatabase(),null,table.getTableName(),null);

PreparedStatement cps=c.prepareStatement("show fields from "+table.getTableName());
ResultSet show=cps.executeQuery();
while(columns.next()&&show.next())
{

Attribute attribute=new Attribute();
attribute.setAttributeName(columns.getString("COLUMN_NAME"));
attribute.setDataType(columns.getString("TYPE_NAME"));
attribute.setDataTypeInt(columns.getInt("DATA_TYPE"));
attribute.setColumnSize(columns.getInt("COLUMN_SIZE"));
boolean temp="YES".equals(show.getString("Null"));

attribute.setIs_nullable(temp);
temp="auto_increment".equals(show.getString("Extra"));
attribute.setIs_autoincrement(temp);
temp="UNI".equals(show.getString("Key"));
attribute.setIs_unique(temp);
temp="PRI".equals(show.getString("Key"));
attribute.setIs_primary_key(temp);
temp="MUL".equals(show.getString("Key"));
attribute.setIs_foreign_key(temp);

table.addAttribute(columns.getString("COLUMN_NAME"),attribute);

}

show.close();
columns.close();
ResultSet PK = dbmd.getPrimaryKeys(dbobj.getDatabase(),null,table.getTableName());
while(PK.next())
{
table.addPrimaryKey(PK.getString("COLUMN_NAME"));
}
PK.close();
ResultSet FK = dbmd.getImportedKeys(dbobj.getDatabase(), null,table.getTableName());
while(FK.next())
{
ForeignKey fk=new ForeignKey();
fk.setColumnName(FK.getString("FKCOLUMN_NAME"));
fk.setPrimaryTableName(FK.getString("PKTABLE_NAME"));
fk.setPrimaryColumnName(FK.getString("PKCOLUMN_NAME"));
table.addForeignKey(fk);
}
FK.close();
table.createSelectAllPreparedStatement();
table.createSelectRowPreparedStatement();
table.createDeleteAllPreparedStatement();
table.createDeleteRowPreparedStatement();
table.createUpdatePreparedStatement();
table.createInsertPreparedStatement();
tables.put(table.getTableName(),table);
}
tablesResultset.close();

System.out.println("ds done");

}
catch(Exception e)
{
e.printStackTrace();
}

	
}
}