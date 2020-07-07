package com.thinking.machines.utils;
import com.thinking.machines.sqlDomain.*;
import java.util.*;
import java.sql.*;
import java.sql.Types.*;
import com.google.gson.*;
import java.lang.reflect.*;
import java.io.*;
import java.math.*;
import javafx.util.Pair; 
import com.thinking.machines.sqlDomain.*;
import com.thinking.machines.beans.*;
import com.thinking.machines.annotations.*;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
public class DataBaseAnalizer
{
public static Map<String,Table> getDataBaseMetaData(Connection c,String dbName,String tName){
Map<String,Table> tables=new HashMap<String,Table>();
try
{

DatabaseMetaData dbmd=c.getMetaData();

ResultSet tablesResultset=dbmd.getTables(dbName,null,null,null);
while(tablesResultset.next())
{
Table table=new Table();
table.setTableName(tablesResultset.getString("TABLE_NAME"));
table.setTableType(tablesResultset.getString("TABLE_TYPE"));



ResultSet PK = dbmd.getPrimaryKeys(dbName,null,table.getTableName());
while(PK.next())
{
table.addPrimaryKey(PK.getString("COLUMN_NAME"));
}
PK.close();
ResultSet FK = dbmd.getImportedKeys(dbName, null,table.getTableName());
while(FK.next())
{
com.thinking.machines.sqlDomain.ForeignKey fk=new com.thinking.machines.sqlDomain.ForeignKey();
fk.setColumnName(FK.getString("FKCOLUMN_NAME"));
fk.setPrimaryTableName(FK.getString("PKTABLE_NAME"));
fk.setPrimaryColumnName(FK.getString("PKCOLUMN_NAME"));
table.addForeignKey(FK.getString("FKCOLUMN_NAME"),fk);
table.referenceTables.add(FK.getString("PKTABLE_NAME"));
}
FK.close();



ResultSet columns=dbmd.getColumns(dbName,null,table.getTableName(),null);


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

if(attribute.is_primary_key() && attribute.is_autoincrement())
{
	table.is_primary_key_and_auto_increment=true;
}
table.addAttribute(columns.getString("COLUMN_NAME"),attribute);

}



table.createSelectAllPreparedStatement();
table.createSelectRowPreparedStatement();
table.createDeleteAllPreparedStatement();
table.createDeleteRowPreparedStatement();
table.createUpdatePreparedStatement();
table.createInsertPreparedStatement();




show.close();
columns.close();
tables.put(table.getTableName(),table);
}
tablesResultset.close();
DataTypeValidator dtv=new DataTypeValidator();
for(Map.Entry<String,Table> tEntry:tables.entrySet())
{
	boolean pkVal=false;
	boolean pkValup=false;

	List<String> alreadyUserFK=new LinkedList<>();
	
	Table table=tEntry.getValue();


Map<String,Attribute> attributes=table.getAttributeMap();
int i=1;

for(Map.Entry<String,Attribute> entry:attributes.entrySet())
{
Attribute attribute=entry.getValue();
String cn=entry.getKey();
List<Validators> insertVal=new LinkedList<>();
List<Validators> updateVal=new LinkedList<>();
insertVal.add(dtv);
updateVal.add(dtv);
if(attribute.is_unique())
{
	String ps="select * from "+tEntry.getKey()+" where "+attribute.getAttributeName()+"=?;";
	UniqueValidator uv=new UniqueValidator();
	uv.selectByUniqueValue=ps;
	uv.c=c;
	uv.valType=1;
	insertVal.add(uv);

	UniqueValidator uvu=new UniqueValidator();
	uvu.selectByUniqueValue=ps;
	uvu.c=c;
	uvu.valType=2;
    uvu.pks=table.getPrimaryKey();

	updateVal.add(uvu);
}
if(attribute.is_foreign_key() && alreadyUserFK.contains(attribute.getAttributeName())==false)
{

	ForeignKeyValidator fkv=new ForeignKeyValidator();
	String ptn=table.foreignKeys.get(attribute.getAttributeName()).getPrimaryTableName();
	Table t=tables.get(ptn);
	String ps=t.getSelectRowPreparedStatement();
	List<String> keys=new LinkedList<>();
	for(Map.Entry<String,com.thinking.machines.sqlDomain.ForeignKey> entry3:table.foreignKeys.entrySet())
    {
    	if(entry3.getValue().getPrimaryTableName().equalsIgnoreCase(ptn))
    	{
    		alreadyUserFK.add(entry3.getKey());
    		keys.add(entry3.getKey());
    	}
    }
    
	fkv.parentTableSelectStatement=ps;
	fkv.keys=keys;
	fkv.c=c;
	fkv.ParentTableName=ptn;
	insertVal.add(fkv);
	updateVal.add(fkv);
}
if(attribute.is_nullable()==false)
{
Not_nullValidator nnv=new Not_nullValidator();
insertVal.add(nnv);
updateVal.add(nnv);
}
if(attribute.is_autoincrement()==false && attribute.is_primary_key()==true && table.is_primary_key_and_auto_increment==false && pkVal==false)
{
	if(table.is_primary_key_and_auto_increment==false){
	PrimaryKeyValidator pkv=new PrimaryKeyValidator();
	pkv.selectByPrimaryKey=table.getSelectRowPreparedStatement();
	pkv.c=c;
	pkv.valType=1;
	insertVal.add(pkv);
	pkVal=true;
}


}
if(attribute.is_primary_key() && pkValup==false)
{
PrimaryKeyValidator pkv=new PrimaryKeyValidator();
	pkv.selectByPrimaryKey=table.getSelectRowPreparedStatement();
	pkv.c=c;
	pkv.valType=2;
	pkValup=true;
	table.updatePkvalidator=pkv;
}

table.insertValidators.put(attribute.getAttributeName(),insertVal);

table.updateValidators.put(attribute.getAttributeName(),updateVal);

i++;
}

table.deleteValidator=new DeleteValidator();
for(Map.Entry<String,Table> loop:tables.entrySet())
{
if(loop.getValue().referenceTables.contains(table.getTableName()))
{
	
StringBuilder sb=new StringBuilder();
sb.append("select * from ");
sb.append(loop.getKey());
sb.append(" where ");
List<String> pk=table.getPrimaryKey();
for(int k=0;k<pk.size();k++)
{
	
	for(Map.Entry<String,com.thinking.machines.sqlDomain.ForeignKey> fkl:loop.getValue().foreignKeys.entrySet())
{
 if(fkl.getValue().getPrimaryColumnName().equals(pk.get(k)) &&fkl.getValue().getPrimaryTableName().equals(table.getTableName()))
 {
 	sb.append(fkl.getKey()+"=? ");
 }
}

}
sb.append(";");
table.deleteValidator.refTables.add(loop.getKey());
table.deleteValidator.psList.add(sb.toString());

}


}
table.deleteValidator.pks=table.getPrimaryKey();
table.deleteValidator.c=c;




}




}
catch(Exception e)
{
e.printStackTrace();
}

return tables;
	
}

}