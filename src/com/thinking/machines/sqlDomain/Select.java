package com.thinking.machines.sqlDomain;
import java.util.*;
import java.sql.*;
import java.lang.reflect.*;
import java.lang.*;
import com.thinking.machines.annotations.*;
import com.thinking.machines.utils.*;
import com.thinking.machines.exceptions.*;
public class Select
{

private Map<String,String> mapping;
private Class<?> tableCLass;
private String selectQuery;
private boolean whereExceuted;
private boolean orderByExcecuted;
public Connection connection;

public Select(Class<?> c,Connection con) throws ORMException,Exception{
//this.clazz = clazz;
//map = new HashMap<>();
//analyseClass();
mapping=new HashMap<>();
whereExceuted=false;
orderByExcecuted=false;
tableCLass=c;
connection=con;
populateMap();
prepareInitialStatement();
}

public void prepareInitialStatement() throws ORMException,Exception
{

if(tableCLass.isAnnotationPresent(TableName.class)==false)
{
throw new ORMException("Class::"+tableCLass.getName()+" does not have TableName Annotation,so it cannot be linked with any sql Table");
}
String Val=((TableName)tableCLass.getAnnotation(TableName.class)).value();
selectQuery="select * from "+Val+" ";
}
public void populateMap()
{
Field fields[]=tableCLass.getDeclaredFields();

for(Field f:fields)
{
	f.setAccessible(true);
	if(f.isAnnotationPresent(ColumnName.class))
	{

		mapping.put(f.getName(),f.getAnnotation(ColumnName.class).value());
	}
}

}
public List query()throws ORMException,Exception
{
	selectQuery+=";";
	System.out.println(selectQuery);
PreparedStatement ps=connection.prepareStatement(selectQuery);
ResultSet rs=ps.executeQuery();
Object object;
List list=new LinkedList<>();
while(rs.next())
{
object=tableCLass.newInstance();
for(Map.Entry<String,String> entry:mapping.entrySet())
{

String fieldName=entry.getKey();
String Column=entry.getValue();

Field f=tableCLass.getDeclaredField(fieldName);
f.setAccessible(true);
f.set(object,rs.getObject(Column));


}
list.add(object);
}
return list;
}

public Select where(String element)throws ORMException,Exception
{
if(element==null||element.equals("")) return this;
if(whereExceuted==false)
{
selectQuery+="where ";
whereExceuted=true;
}
selectQuery+=mapping.get(element);
return this;
}
public Select eq(Object val)throws ORMException,Exception
{

if(val==null) throw new ORMException("Class::"+tableCLass.getName()+", Does not support null val for comparision");
if(whereExceuted==false) return this;
if((val.getClass()==java.lang.String.class)||(val.getClass()==java.lang.Character.class)||(val.getClass()==java.util.Date.class)) selectQuery+="='"+val+"'";
else selectQuery+="="+val;
return this;
}
public Select lt(Object val)throws ORMException,Exception
{
	if(val==null) throw new ORMException("Class::"+tableCLass.getName()+", Does not support null val for comparision");
    if(whereExceuted==false) return this;
   if(val.getClass()==java.lang.String.class||val.getClass()==java.lang.Character.class||val.getClass()==java.util.Date.class) selectQuery+="<'"+val+"'";
else selectQuery+="<"+val;
return this;
}
public Select gt(Object val)throws ORMException,Exception
{
	if(val==null) throw new ORMException("Class::"+tableCLass.getName()+", Does not support null val for comparision");
    if(whereExceuted==false) return this;
   if(val.getClass()==java.lang.String.class||val.getClass()==java.lang.Character.class||val.getClass()==java.util.Date.class) selectQuery+=">'"+val+"'";
else selectQuery+=">"+val;
return this;
}



public Select ltet(Object val)throws ORMException,Exception
{
	if(val==null) throw new ORMException("Class::"+tableCLass.getName()+", Does not support null val for comparision");
    if(whereExceuted==false) return this;
   if(val.getClass()==java.lang.String.class||val.getClass()==java.lang.Character.class||val.getClass()==java.util.Date.class) selectQuery+="<='"+val+"'";
else selectQuery+="<="+val;
return this;
}
public Select gtet(Object val)throws ORMException,Exception
{
	if(val==null) throw new ORMException("Class::"+tableCLass.getName()+", Does not support null val for comparision");
    if(whereExceuted==false) return this;
   if(val.getClass()==java.lang.String.class||val.getClass()==java.lang.Character.class||val.getClass()==java.util.Date.class) selectQuery+=">='"+val+"'";
else selectQuery+=">="+val;
return this;
}
public Select ne(Object val)throws ORMException,Exception
{
	if(val==null) throw new ORMException("Class::"+tableCLass.getName()+", Does not support null val for comparision");
    if(whereExceuted==false) return this;
   if(val.getClass()==java.lang.String.class||val.getClass()==java.lang.Character.class||val.getClass()==java.util.Date.class) selectQuery+="!='"+val+"'";
else selectQuery+="!="+val;
return this;
}
public Select orderBy(String orderBy)
{
if(orderBy==null) return this;
if(orderByExcecuted==false)
{
selectQuery+=" ORDER BY ";
orderByExcecuted=true;
}
else
{
	selectQuery+=" , ";
}
selectQuery+=mapping.get(orderBy)+" ";
return this;
}
public Select descending()throws ORMException,Exception
{
	if(orderByExcecuted==false) throw new ORMException("cannot apply decending before Order BY");
selectQuery+=" Desc ";
	return this;
}
public Select ascending()throws ORMException,Exception
{
	if(orderByExcecuted==false) throw new ORMException("cannot apply decending before Order BY");
selectQuery+=" ASC ";
	return this;
}
public Select and()
{
	if(whereExceuted==false) return this;

	selectQuery+=" and ";
return this;
}

public Select or()
{
	if(whereExceuted==false) return this;

	selectQuery+=" or ";
	return this;
}

public Select openParen()
{
if(whereExceuted==false) return this;
selectQuery=" ( ";
return this;
}
public Select closeParen()
{
if(whereExceuted==false) return this;
selectQuery=" ) ";
return this;
}

}
