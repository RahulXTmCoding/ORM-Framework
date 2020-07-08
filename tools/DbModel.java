
import javax.swing.table.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;
import java.util.stream.Collectors; 
import java.util.stream.Stream;
import java.io.*;
import com.google.gson.*;
import java.sql.*;
import java.sql.Types.*;
import java.awt.event.*; 
import  com.thinking.machines.sqlDomain.*;
import  com.thinking.machines.utils.*;
import  com.thinking.machines.beans.*;
import  com.thinking.machines.annotations.*;
public class DbModel extends AbstractTableModel
{
private java.util.Map<String,Table> tables;
private java.util.List<Table> tablesList;
private java.util.List<String> classNames;
private String dbName;
public DbConfigBean dbobj;
public ConfigTool parent;
public DbModel(ConfigTool parent)
{
populateDs();
this.parent=parent;

}
public void filldbData()
{
	parent.dbNameField.setText(dbobj.getDatabase());
	parent.userNameField.setText(dbobj.getUsername());
	parent.passField.setText(dbobj.getPassword());
}
public void updateDbConfig(String dbName,String userName,String password)
{
	try
	{
dbobj.setDatabase(dbName);
dbobj.setUsername(userName);
dbobj.setPassword(password);

Gson g=new Gson();
String str=g.toJson(dbobj);
PrintWriter writer = new PrintWriter(DbModel.class.getResource(".").getPath()+"../config/dbConfig.json");
writer.print(str);
writer.close();
}catch(Exception e)
{
	e.printStackTrace();
}
populateDs();
}
private void populateDs()
{
try{
File f=new File(DbModel.class.getResource(".").getPath()+"../config/dbConfig.json");
FileInputStream fis = new FileInputStream(f);
byte[] data = new byte[(int) f.length()];
fis.read(data);
fis.close();
String str = new String(data, "UTF-8");	
Gson g=new Gson();
dbobj=g.fromJson(str,DbConfigBean.class);

Class.forName(dbobj.getDriver());
Connection c=DriverManager.getConnection(dbobj.getConnection_url()+dbobj.getDatabase(),dbobj.getUsername(),dbobj.getPassword());

dbName=dbobj.getDatabase();



tables=DataBaseAnalizer.getDataBaseMetaData(c,dbName,null);

tablesList=new LinkedList<>(tables.values());
classNames=new LinkedList<>();
for(int j=0;j<tablesList.size();j++)
{
Table t=tablesList.get(j);
String name=t.getTableName();

StringBuilder sb=new StringBuilder();
sb.append(name.substring(0,1).toUpperCase());
boolean symbol=false;
for(int i=1;i<name.length();i++)
{
if((name.charAt(i)>=65 && name.charAt(i)<=90) || (name.charAt(i)>=97 && name.charAt(i)<=122))
{
	if(symbol==true && (name.charAt(i)>=97 && name.charAt(i)<=122))
	{
     sb.append((char)(name.charAt(i)-32));
	}
	else
	{
	sb.append(name.charAt(i));
	}
	symbol=false;
}
else
{
	symbol=true;
}
}
classNames.add(sb.toString());
}

}catch(Exception e)
{
	e.printStackTrace();
}
}
public int getColumnCount()
{

return 5;
}
public String getColumnName(int columnindex)
{

if(columnindex==0) return "S.no";
if(columnindex==1) return "Database Entries";
if(columnindex==2) return "Type";
if(columnindex==3) return "Equivalent Java Class";
return "Button";
}
public int getRowCount()
{

return tables.size();

}
public boolean isCellEditable(int row,int column)
{
return false;
}
public Object getValueAt(int row,int column)
{
Table t=tablesList.get(row);


if(column==0) return (row+1);
if(column==1) return t.getTableName();
if(column==2) return t.getTableType();
if(column==3) return classNames.get(row);

return "Create";
}
public Class getColumnClass(int column)
{
if(column==0)return Integer.class;
return String.class;
}
public void setValueAt(Object d,int row,int column)
{

}

public void createAllClasses()
{
	try{

JFileChooser chooser=new JFileChooser();
chooser.setDialogTitle("Select Directory");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
int l=chooser.showSaveDialog(parent.f);
if(l==JFileChooser.APPROVE_OPTION)
{
for(int i=0;i<tablesList.size();i++)
{
Table t=tablesList.get(i);
String ClassName=classNames.get(i);
String path=chooser.getSelectedFile().getPath();
File f=new File(path+"/"+ClassName+".java");

RandomAccessFile raf=new RandomAccessFile(f,"rw");


raf.writeBytes("import java.util.*;\n import java.math.*; \n import com.thinking.machines.annotations.*; \n import java.text.*;\n");
raf.writeBytes("@TableName(value="+'"'+t.getTableName()+'"'+")\n");
raf.writeBytes("public class "+ClassName);
raf.writeBytes(" {\n");

Map<String,Attribute> attributeMap=t.getAttributeMap();
for(Map.Entry<String,Attribute> entry:attributeMap.entrySet())
{

Attribute a=entry.getValue();

if(a.is_nullable())
{
	raf.writeBytes("@NotNull()\n");
}
if(a.is_autoincrement())
{
	raf.writeBytes("@Auto_Increment()\n");
}
if(a.is_unique())
{
	raf.writeBytes("@Unique()\n");
}
if(a.is_primary_key())
{
	raf.writeBytes("@PrimaryKey()\n");
}
if(a.is_foreign_key())
{
	com.thinking.machines.sqlDomain.ForeignKey fk=t.foreignKeys.get(a.getAttributeName());
	raf.writeBytes("@ForeignKey(base="+'"'+fk.getPrimaryTableName()+'"'+","+"columnName="+'"'+fk.getColumnName()+'"'+","+"baseColumnName="+'"'+fk.getPrimaryColumnName()+'"'+")\n");

}
raf.writeBytes("@ColumnName(value="+'"'+a.getAttributeName()+'"'+")\n");

raf.writeBytes("@ColumnType(value="+'"'+a.getDataType()+'"'+")\n");


raf.writeBytes("public ");







String dt=a.getDataType();
String datatype="";

if(dt.equalsIgnoreCase("INT"))
{
	datatype="int ";
	
}
else
if(dt.equalsIgnoreCase("bigint"))
{
	datatype="long ";
}
else
if(dt.equalsIgnoreCase("Char")||dt.equalsIgnoreCase("varchar")||dt.equalsIgnoreCase("VARBINARY")||dt.equalsIgnoreCase("BINARY"))
{
	datatype="String ";
}
else
if(dt.equalsIgnoreCase("float"))
{
	datatype="float ";
}
else
if(dt.equalsIgnoreCase("double"))
{
datatype="double ";
}
else
if(dt.equalsIgnoreCase("date"))
{
	datatype="java.util.Date ";
	
}
else
if(dt.equalsIgnoreCase("datetime"))
{
	datatype="java.util.Date ";
}
else
if(dt.equalsIgnoreCase("time"))
{
	datatype="java.util.Date ";
}
else
if(dt.equalsIgnoreCase("numeric"))
{
	datatype="Object ";
}
else
if(dt.equalsIgnoreCase("bool")||dt.equalsIgnoreCase("boolean")||dt.equalsIgnoreCase("bit"))
{
	datatype="boolean ";
}
else
if(dt.equalsIgnoreCase("decimal")||dt.equalsIgnoreCase("dec"))
{
	datatype="java.math.BigDecimal ";
}

String atName=a.getAttributeName();
raf.writeBytes(datatype);
raf.writeBytes(atName);
raf.writeBytes(";\n");

raf.writeBytes("public void set"+atName.substring(0,1).toUpperCase()+atName.substring(1)+"("+datatype+atName+")"+"{\n");
raf.writeBytes("this."+atName+"="+atName+";\n");
raf.writeBytes("}\n");



raf.writeBytes("public "+datatype+" get"+atName.substring(0,1).toUpperCase()+atName.substring(1)+"()"+"{\n");
raf.writeBytes("return this."+atName+";\n");
raf.writeBytes("}\n");
raf.writeBytes("\n");

}




raf.close();
}
}
}catch(Exception e)
{
	e.printStackTrace();
}
}












public void createClass(int row)
{
	try{

JFileChooser chooser=new JFileChooser();
chooser.setDialogTitle("Select Directory");
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
int l=chooser.showSaveDialog(parent.f);
if(l==JFileChooser.APPROVE_OPTION)
{
Table t=tablesList.get(row);
String ClassName=classNames.get(row);
String path=chooser.getSelectedFile().getPath();
File f=new File(path+"/"+ClassName+".java");

RandomAccessFile raf=new RandomAccessFile(f,"rw");


raf.writeBytes("import java.util.*;\n import java.math.*; \n import com.thinking.machines.annotations.*; \n import java.text.*;\n");
raf.writeBytes("@TableName(value="+'"'+t.getTableName()+'"'+")\n");
raf.writeBytes("public class "+ClassName);
raf.writeBytes(" {\n");

Map<String,Attribute> attributeMap=t.getAttributeMap();
for(Map.Entry<String,Attribute> entry:attributeMap.entrySet())
{

Attribute a=entry.getValue();

if(a.is_nullable())
{
	raf.writeBytes("@NotNull()\n");
}
if(a.is_autoincrement())
{
	raf.writeBytes("@Auto_Increment()\n");
}
if(a.is_unique())
{
	raf.writeBytes("@Unique()\n");
}
if(a.is_primary_key())
{
	raf.writeBytes("@PrimaryKey()\n");
}
if(a.is_foreign_key())
{
	com.thinking.machines.sqlDomain.ForeignKey fk=t.foreignKeys.get(a.getAttributeName());
	raf.writeBytes("@ForeignKey(base="+'"'+fk.getPrimaryTableName()+'"'+","+"columnName="+'"'+fk.getColumnName()+'"'+","+"baseColumnName="+'"'+fk.getPrimaryColumnName()+'"'+")\n");

}
raf.writeBytes("@ColumnName(value="+'"'+a.getAttributeName()+'"'+")\n");

raf.writeBytes("@ColumnType(value="+'"'+a.getDataType()+'"'+")\n");


raf.writeBytes("public ");







String dt=a.getDataType();
String datatype="";

if(dt.equalsIgnoreCase("INT"))
{
	datatype="int ";
	
}
else
if(dt.equalsIgnoreCase("bigint"))
{
	datatype="long ";
}
else
if(dt.equalsIgnoreCase("Char")||dt.equalsIgnoreCase("varchar")||dt.equalsIgnoreCase("VARBINARY")||dt.equalsIgnoreCase("BINARY"))
{
	datatype="String ";
}
else
if(dt.equalsIgnoreCase("float"))
{
	datatype="float ";
}
else
if(dt.equalsIgnoreCase("double"))
{
datatype="double ";
}
else
if(dt.equalsIgnoreCase("date"))
{
	datatype="java.util.Date ";
	
}
else
if(dt.equalsIgnoreCase("datetime"))
{
	datatype="java.util.Date ";
}
else
if(dt.equalsIgnoreCase("time"))
{
	datatype="java.util.Date ";
}
else
if(dt.equalsIgnoreCase("numeric"))
{
	datatype="Object ";
}
else
if(dt.equalsIgnoreCase("bool")||dt.equalsIgnoreCase("boolean")||dt.equalsIgnoreCase("bit"))
{
	datatype="boolean ";
}
else
if(dt.equalsIgnoreCase("decimal")||dt.equalsIgnoreCase("dec"))
{
	datatype="java.math.BigDecimal ";
}

String atName=a.getAttributeName();
raf.writeBytes(datatype);
raf.writeBytes(atName);
raf.writeBytes(";\n");

raf.writeBytes("public void set"+atName.substring(0,1).toUpperCase()+atName.substring(1)+"("+datatype+atName+")"+"{\n");
raf.writeBytes("this."+atName+"="+atName+";\n");
raf.writeBytes("}\n");



raf.writeBytes("public "+datatype+" get"+atName.substring(0,1).toUpperCase()+atName.substring(1)+"()"+"{\n");
raf.writeBytes("return this."+atName+";\n");
raf.writeBytes("}\n");
raf.writeBytes("\n");

}




raf.close();
}

}catch(Exception e)
{
	e.printStackTrace();
}
}


/*
public EmployeeInterface getData(int row)
{
return list.get(row);
}

public void add(EmployeeInterface ei)throws ModelException
{
try{
emf.add(ei);
list.add(ei);
list=list.stream().sorted(Comparator.comparing(EmployeeInterface::getName,String.CASE_INSENSITIVE_ORDER).thenComparing(EmployeeInterface::getCode)).collect(Collectors.toList());
fireTableDataChanged();
}

catch(ValidationException ve)
{
new ModelException(ve.getMessage());
}
catch(processException pe)
{
new ModelException(pe.getMessage());
}

}
public void edit(EmployeeInterface ei)throws ModelException
{
try{
emf.update(ei);
int i;
for(i=0;i<list.size();i++)
{
if(list.get(i).getCode()==ei.getCode())
{
break;
}


}
list.set(i,ei);
list=list.stream().sorted(Comparator.comparing(EmployeeInterface::getName,String.CASE_INSENSITIVE_ORDER).thenComparing(EmployeeInterface::getCode)).collect(Collectors.toList());
fireTableDataChanged();
}
catch(ValidationException ve)
{
new ModelException(ve.getMessage());
}
catch(processException pe)
{
new ModelException(pe.getMessage());
}

}
public void delete(EmployeeInterface ei)throws ModelException
{
try
{
int i;
emf.delete(ei.getCode());
for(i=0;i<list.size();i++)
{
if(list.get(i).getCode()==ei.getCode())
{

break;
}


}
list.remove(i);
fireTableDataChanged();
}catch(ValidationException ve)
{
new ModelException(ve.getMessage());
}
catch(processException pe)
{
new ModelException(pe.getMessage());
}

}
*/

}