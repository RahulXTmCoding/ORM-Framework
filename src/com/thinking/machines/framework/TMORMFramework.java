package com.thinking.machines.framework;
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
public class TMORMFramework
{
public Map<String,Table> tables;
public Connection c;
public String dbName;
private TMORMFramework()
{
try{
File f=new File(TMORMFramework.class.getResource(".").getPath()+"../../../../../config/dbConfig.json");
FileInputStream fis = new FileInputStream(f);
byte[] data = new byte[(int) f.length()];
fis.read(data);
fis.close();
String str = new String(data, "UTF-8");	
Gson g=new Gson();
DbConfigBean dbobj=g.fromJson(str,DbConfigBean.class);

Class.forName(dbobj.getDriver());
c=DriverManager.getConnection(dbobj.getConnection_url()+dbobj.getDatabase(),dbobj.getUsername(),dbobj.getPassword());

dbName=dbobj.getDatabase();



tables=DataBaseAnalizer.getDataBaseMetaData(c,dbName,null);

}catch(Exception e)
{
	e.printStackTrace();
}


}
public static TMORMFramework getInstance()
{
	return new TMORMFramework();
}
public void save(Object o) throws ORMException,Exception
{
Class classObject=o.getClass();
if(classObject.isAnnotationPresent(TableName.class)==false)
{
	return;
}
String tableName=((TableName)classObject.getAnnotation(TableName.class)).value();
Table t=tables.get(tableName);
if(t.getTableType().equalsIgnoreCase("view"))
{
	throw new ORMException("Save Method Cannot be used on a View,View Name:-"+tableName);
}
String insertPreparedStatement=t.getInsertPreparedStatement();
PreparedStatement ps=c.prepareStatement(insertPreparedStatement,PreparedStatement.RETURN_GENERATED_KEYS);
Field allfields[]=classObject.getDeclaredFields();
Map<String,Field> fieldMap=new HashMap<>();
for(Field f:allfields)
{
	if(f.isAnnotationPresent(ColumnName.class))
	{

		fieldMap.put(f.getAnnotation(ColumnName.class).value(),f);
	}
}
Map<String,Attribute> attributes=t.getAttributeMap();
int i=1;
System.out.println(insertPreparedStatement);
Field fL=null;
for(Map.Entry<String,Attribute> entry:attributes.entrySet())
{
Attribute a=entry.getValue();
if(a.is_autoincrement())
{
	fL=fieldMap.get(entry.getKey());
	continue;
}
System.out.println(i);
String cn=entry.getKey();
Field f=fieldMap.get(cn);
f.setAccessible(true);
String dt=a.getDataType();
List<Validators> l=t.insertValidators.get(cn);
if(l!=null)
{
for(int j=0;j<l.size();j++)
{
	if(a.is_foreign_key())
	{
     //Table pTable=tables.get(t.foreignKeys.get(cn).getPrimaryTableName());
     l.get(j).validate(t,a,fieldMap,o);
	}
    else
    {
	l.get(j).validate(t,a,fieldMap,o); 
    }
}
}
ValueSetter.setColumnValue(ps,dt,f,i,o);

i++;
}


ps.executeUpdate();

ResultSet rs = ps.getGeneratedKeys();
    if(rs != null && rs.next()) {

    	if(fL!=null)
{

fL.setAccessible(true);

fL.set(o,rs.getInt(1));
}

   
}



System.out.println("save ");

}
public void delete(Object o) throws ORMException,Exception
{

Class classObject=o.getClass();
if(classObject.isAnnotationPresent(TableName.class)==false)
{
	return;
}
String tableName=((TableName)classObject.getAnnotation(TableName.class)).value();
Table t=tables.get(tableName);
if(t.getTableType().equalsIgnoreCase("view"))
{
	throw new ORMException("delete Method Cannot be used on a View,View Name:-"+tableName);
}
String deletePreparedStatement=t.getDeleteRowPreparedStatement();
PreparedStatement ps=c.prepareStatement(deletePreparedStatement);
Field allfields[]=classObject.getDeclaredFields();
Map<String,Field> fieldMap=new HashMap<>();
for(Field f:allfields)
{
	if(f.isAnnotationPresent(ColumnName.class))
	{

		fieldMap.put(f.getAnnotation(ColumnName.class).value(),f);
	}
}

Map<String,Attribute> attributes=t.getAttributeMap();
int i=1;
System.out.println(deletePreparedStatement);
List<String> pk=t.getPrimaryKey();
t.deleteValidator.validate(t,fieldMap,o);
for(int j=0;j<pk.size();j++)
{
Attribute a=attributes.get(pk.get(j));
Field f=fieldMap.get(pk.get(j));
f.setAccessible(true);
String dt=a.getDataType();
System.out.println(j);
ValueSetter.setColumnValue(ps,dt,f,j+1,o);

}
ps.executeUpdate();

System.out.println("delete ");

}
public void update(Object o)throws ORMException,Exception
{
Class classObject=o.getClass();
if(classObject.isAnnotationPresent(TableName.class)==false)
{
	return;
}
String tableName=((TableName)classObject.getAnnotation(TableName.class)).value();
Table t=tables.get(tableName);
if(t.getTableType().equalsIgnoreCase("view"))
{
	throw new ORMException(" Update Method Cannot be used on a View,View Name:-"+tableName);
}
String updatePreparedStatement=t.getUpdatePreparedStatement();
PreparedStatement ps=c.prepareStatement(updatePreparedStatement);
Field allfields[]=classObject.getDeclaredFields();
Map<String,Field> fieldMap=new HashMap<>();
for(Field f:allfields)
{
	if(f.isAnnotationPresent(ColumnName.class))
	{

		fieldMap.put(f.getAnnotation(ColumnName.class).value(),f);
	}
}
Map<String,Attribute> attributes=t.getAttributeMap();
int i=1;
System.out.println(updatePreparedStatement);
Field fL=null;
t.updatePkvalidator.validate(t,attributes.get(t.getPrimaryKey().get(0)),fieldMap,o);
for(Map.Entry<String,Attribute> entry:attributes.entrySet())
{
Attribute a=entry.getValue();
if(a.is_primary_key())
{
	continue;
}
System.out.println(i);
String cn=entry.getKey();
Field f=fieldMap.get(cn);
f.setAccessible(true);
String dt=a.getDataType();

List<Validators> l=t.updateValidators.get(cn);
if(l!=null)
{
for(int j=0;j<l.size();j++)
{
	l.get(j).validate(t,a,fieldMap,o); 
}
}


ValueSetter.setColumnValue(ps,dt,f,i,o);

i++;
}
List<String> pk=t.getPrimaryKey();
for(int j=0;j<pk.size();j++)
{
Attribute a=attributes.get(pk.get(j));
Field f=fieldMap.get(pk.get(j));
f.setAccessible(true);
String dt=a.getDataType();
System.out.println(i);
ValueSetter.setColumnValue(ps,dt,f,i,o);
i++;
}

ps.executeUpdate();
System.out.println("updated ");
}
public void select(Object o)throws ORMException,Exception
{

Class classObject=o.getClass();

if(classObject.isAnnotationPresent(TableName.class)==false)
{
	return;
}
String tableName=((TableName)classObject.getAnnotation(TableName.class)).value();
Table t=tables.get(tableName);
if(t.getTableType().equalsIgnoreCase("view"))
{
	throw new ORMException("Single row selection method cannot be used on a View,View Name:-"+tableName);
}
String selectRowPreparedStatement=t.getSelectRowPreparedStatement();
PreparedStatement ps=c.prepareStatement(selectRowPreparedStatement);
Field allfields[]=classObject.getDeclaredFields();
Map<String,Field> fieldMap=new HashMap<>();
for(Field f:allfields)
{
	if(f.isAnnotationPresent(ColumnName.class))
	{

		fieldMap.put(f.getAnnotation(ColumnName.class).value(),f);
	}
}

Map<String,Attribute> attributes=t.getAttributeMap();
int i=1;
System.out.println(selectRowPreparedStatement);
List<String> pk=t.getPrimaryKey();
for(int j=0;j<pk.size();j++)
{
Attribute a=attributes.get(pk.get(j));
Field f=fieldMap.get(pk.get(j));
f.setAccessible(true);
String dt=a.getDataType();
System.out.println(j);
ValueSetter.setColumnValue(ps,dt,f,j+1,o);

}
ResultSet resultSet=ps.executeQuery();

if(resultSet.next())
{
for(Map.Entry<String,Attribute> entry:t.getAttributeMap().entrySet())
{
	String aName=entry.getKey();
Attribute a=entry.getValue();
Field f=fieldMap.get(aName);
f.setAccessible(true);
String dt=a.getDataType();

ValueSetter.setFieldValue(dt,f,o,resultSet,aName);


}


}

}

public void createTable(Class tableC)throws ORMException
{
try{
if(tableC.isAnnotationPresent(TableName.class)==false)
{
	throw new ORMException("Cannot Create Table for the following class:"+tableC.getName()+" ,Please apply annotations to Describe Table");
}
String tableName=((TableName)tableC.getAnnotation(TableName.class)).value();

Statement s=c.createStatement();
try
{s.executeQuery("select * from "+tableName);
}catch(Exception e)
{
	throw new ORMException(e.toString());
}
String createStatement="create table "+tableName+"(";

Map<String,List<Pair<String,String>>> foreignKeyMap=new HashMap<>();
Field fields[]=tableC.getDeclaredFields();
List<Pair<String,String>> l;
List<String> pks=new LinkedList<>();
int  i=0;
for(Field f:fields)
{
if(f.isAnnotationPresent(ColumnName.class)==false ||f.isAnnotationPresent(ColumnType.class)==false)
{
	continue;
}

String columnName=f.getAnnotation(ColumnName.class).value();
createStatement+=columnName+" ";
String type=f.getAnnotation(ColumnType.class).value();
createStatement+=type+" ";
if(f.isAnnotationPresent(NotNull.class))
{
	createStatement+="not null ";
}
if(f.isAnnotationPresent(Auto_Increment.class))
{
	createStatement+="auto increment ";
}
if(f.isAnnotationPresent(Unique.class))
{
	createStatement+="Unique ";
}
if(f.isAnnotationPresent(com.thinking.machines.annotations.ForeignKey.class))
{
	String pName=f.getAnnotation(com.thinking.machines.annotations.ForeignKey.class).base();
	String cName=f.getAnnotation(com.thinking.machines.annotations.ForeignKey.class).columnName();
	String pcName=f.getAnnotation(com.thinking.machines.annotations.ForeignKey.class).baseColumnName();
	if(foreignKeyMap.get(pName)==null)
	{
		l=new LinkedList<>();
		foreignKeyMap.put(pName,l);

	}
	l=foreignKeyMap.get(pName);
    l.add(new Pair<String,String>(cName,pcName));

}
if(f.isAnnotationPresent(PrimaryKey.class))
{
pks.add(columnName);
}

if(i<fields.length-1)
{
createStatement+=",";
}
i++;
}

if(pks.size()>0)
{
createStatement+=",";
createStatement+= " PRIMARY KEY (";
for(int j=0;j<pks.size();j++)
{
	createStatement+=pks.get(j);
	if(j<pks.size()-1)
	{
	createStatement+=",";
     }
}
createStatement+=")";
}
if(foreignKeyMap.size()>0)
{
	
for(Map.Entry<String,List<Pair<String,String>>> entry:foreignKeyMap.entrySet())
{
createStatement+=", ";
String pName=entry.getKey();
List<Pair<String,String>> ll=entry.getValue();
String fkString="";
String ppkString="";
for(int k=0;k<ll.size();k++)
{
	Pair<String,String> p=ll.get(k);
   if(k!=0)
   	{
   	 fkString+=",";
   	 ppkString+=",";
   }
	fkString+=p.getKey();
	ppkString+=p.getValue();
}
createStatement+="FOREIGN KEY ("+fkString+")"+" REFERENCES "+pName+"("+ppkString+")";
     

}



}

createStatement+=");";

 s=c.createStatement();
s.executeUpdate(createStatement);



addToDs(tableName);



}catch(SQLException se)
{
	throw new ORMException(se.toString());
}
}

public Create create(String tName)throws ORMException,Exception
{
Create c=null;
c=new Create(tName,this);
return c;
}


public void addToDs(String tName)throws SQLException
{


DatabaseMetaData dbmd=c.getMetaData();

ResultSet tablesResultset=dbmd.getTables(dbName,null,tName,new String[]{"TABLE"});
Table table=null;
while(tablesResultset.next())
{
table=new Table();
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
boolean pkVal=false;
boolean pkValup=false;
List<String> alreadyUserFK=new LinkedList<>();
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
	String ps="select * from "+table.getTableName()+" where "+attribute.getAttributeName()+"=?;";
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
System.out.println("<------"+"delete val ke phle"+"---->");
table.deleteValidator=new DeleteValidator();
for(Map.Entry<String,Table> loop:tables.entrySet())
{
if(loop.getValue().referenceTables.contains(table.getTableName()))
{
	System.out.println("<------"+"table refrence ho rahi"+"---->");
StringBuilder sb=new StringBuilder();
sb.append("select * from ");
sb.append(loop.getKey());
sb.append(" where ");
List<String> pk=table.getPrimaryKey();
for(int k=0;k<pk.size();k++)
{
	System.out.println("<------"+"table refrence ho rahi"+"---->");
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
System.out.println(sb.toString()+"---->");
}


}
table.deleteValidator.pks=table.getPrimaryKey();
table.deleteValidator.c=c;



}



public Select select(Class tableClass) throws ORMException,Exception
{
	
	Select select=new Select(tableClass,c);
	return select;
}
public void deleteAll(Class tableClass)throws ORMException,Exception
{
if(tableClass.isAnnotationPresent(TableName.class)==false)
{
return;
}

String tableName=((TableName)tableClass.getAnnotation(TableName.class)).value();

Table table=tables.get(tableName);
if(table.getTableType().equalsIgnoreCase("view"))
{
	throw new ORMException(" deleteAll  method Cannot be used on a View,View Name:-"+tableName);
}

String deleteAllPreparedStatement=table.getDeleteAllPreparedStatement();
PreparedStatement ps=c.prepareStatement(deleteAllPreparedStatement);
ps.executeUpdate();

}

public List<Object> selectAll(Class tableClass)throws Exception
{
if(tableClass.isAnnotationPresent(TableName.class)==false)
{
	return null;
}
String tableName=((TableName)tableClass.getAnnotation(TableName.class)).value();

Table table=tables.get(tableName);
String selectAllPreparedStatement=table.getSelectAllPreparedStatement();
PreparedStatement ps=c.prepareStatement(selectAllPreparedStatement);
ResultSet resultSet=ps.executeQuery();
Field[] fields=tableClass.getDeclaredFields();
Map<String,Field> fieldMap=new HashMap<>();
for(Field f:fields)
{
	if(f.isAnnotationPresent(ColumnName.class))
	{

		fieldMap.put(f.getAnnotation(ColumnName.class).value(),f);
	}
}
Map<String,Attribute> attributes=table.getAttributeMap();
List<Object> elements=new LinkedList<>();
while(resultSet.next())
{
Object o=tableClass.newInstance();
for(Map.Entry<String,Attribute> entry:attributes.entrySet())
{
String name=entry.getKey();
Field f=fieldMap.get(name);
Attribute a=entry.getValue();
String dt=a.getDataType();
ValueSetter.setFieldValue(dt,f,o,resultSet,name);
}

elements.add(o);

}


return elements;
}

public void begin() throws Exception
{
c.setAutoCommit(false);
}
public void commit() throws Exception
{
c.commit();
}
public void rollBack() throws Exception
{
c.rollback();
}

public ResultSet rawSqlQuery(String sqlStatement) throws ORMException
{
try{
Statement s=c.createStatement();
ResultSet rs;
if(sqlStatement.toUpperCase().startsWith("INSERT")||sqlStatement.toUpperCase().startsWith("DELETE")||sqlStatement.toUpperCase().startsWith("UPDATE"))
{
	throw new ORMException("Cannot use rawSqlQuery method for sql Statements like update,insert,delete");
	}
else
{
	rs=s.executeQuery(sqlStatement);
}
return rs;

}
catch(Exception e)
{
  throw new ORMException(e.toString());
}

}
public int rawSqlUpdate(String sqlStatement) throws ORMException
{
try{
Statement s=c.createStatement();
int rowAffected;
if(sqlStatement.toUpperCase().startsWith("INSERT")||sqlStatement.toUpperCase().startsWith("DELETE")||sqlStatement.toUpperCase().startsWith("UPDATE"))
{
	rowAffected=s.executeUpdate(sqlStatement);
}
else
{
	throw new ORMException("Cannot use rawSqlUpdate Method for Query type sql Statement");
}

return rowAffected;
}
catch(Exception e)
{
  throw new ORMException(e.toString());
}

}


}