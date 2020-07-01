package com.thinking.machines.sqlDomain;
import java.util.*;
import com.thinking.machines.utils.*;
public class Table
{
String tableName;
String tableType;
public Map<String,Attribute> attributeMap;
//list of primary key so dont have to search for primary key again and again
List<String> primaryKey;
//Foreignkey Object contain information about current class column name fk and primary class table name and column name whome it is referenced with
public Map<String,ForeignKey> foreignKeys;
public Map<String,List<Validators>> insertValidators; 
public Map<String,List<Validators>> updateValidators; 
public boolean is_primary_key_and_auto_increment=false;
public Set<String> referenceTables;
public Validators updatePkvalidator;
public DeleteValidator deleteValidator;
String selectAllPreparedStatement;
String selectRowPreparedStatement;
String deleteAllPreparedStatement;
String deleteRowPreparedStatement;
String insertPreparedStatement;
String updatePreparedStatement;
public Table()
{
tableName="";
tableType="";
attributeMap=new LinkedHashMap<>();
primaryKey=new LinkedList<>();
foreignKeys=new LinkedHashMap<>();
referenceTables=new HashSet<>();
insertValidators=new LinkedHashMap<>();
updateValidators=new LinkedHashMap<>();
}
public void setTableName(String tableName)
{
	this.tableName=tableName;
}
public String getTableName()
{
	return tableName;
}
public void setTableType(String tableType)
{
	this.tableType=tableType;
}
public String getTableType()
{
	return tableType;
}
public void addAttribute(String name,Attribute attribute)
{
	this.attributeMap.put(name,attribute);
}
public Map<String,Attribute> getAttributeMap()
{
	return attributeMap;
}

public void addPrimaryKey(String primaryKey)
{
	this.primaryKey.add(primaryKey);
}
public List<String> getPrimaryKey()
{
	return primaryKey;
}
public void addForeignKey(String name,ForeignKey fk)
{
	this.foreignKeys.put(name,fk);
}
public Map<String,ForeignKey> getForeignKeys()
{
	return foreignKeys;
}


public void  createSelectAllPreparedStatement()
{
	selectAllPreparedStatement="select * from "+this.getTableName()+";";
}
public void  createSelectRowPreparedStatement()
{
	
	StringBuilder sb=new StringBuilder();
	sb.append("select * from ");
	sb.append(this.getTableName());
	if(primaryKey.size()>0)
	{
	sb.append(" where ");
	for(int i=0;i<primaryKey.size();i++)
	{   sb.append(" ");
		if(i!=0)
		{
		sb.append("and ");
		}
		sb.append(primaryKey.get(i));

		Attribute a=attributeMap.get(primaryKey.get(i));
		String type=a.getDataType();
		
	     sb.append("=?");
	    
	}
}
/*else
	{
    if(foreignKeys.size()>0)
    {	
     sb.append(" where ");
	for(int i=0;i<foreignKeys.size();i++)
	{   sb.append(" ");
if(i!=0)
		{
		sb.append("and ");
		}
		sb.append(foreignKeys.get(i).getColumnName());
		Attribute a=attributeMap.get(foreignKeys.get(i).getColumnName());
		String type=a.getDataType();
		
		
	     sb.append("=?");
	    
	}
    }
	}
*/
	sb.append(";");
	selectRowPreparedStatement=sb.toString();
	
	
}
public void  createDeleteAllPreparedStatement()
{

	
deleteAllPreparedStatement="delete from "+this.getTableName()+";";

}
public void  createDeleteRowPreparedStatement()
{
StringBuilder sb=new StringBuilder();
	sb.append("delete from ");
	sb.append(this.getTableName());
	if(primaryKey.size()>0)
	{
	sb.append(" where ");
	for(int i=0;i<primaryKey.size();i++)
	{   sb.append(" ");
if(i!=0)
		{
		sb.append("and ");
		}
		sb.append(primaryKey.get(i));
		Attribute a=attributeMap.get(primaryKey.get(i));
		String type=a.getDataType();
		
		
	     sb.append("=?");
	    
	}
}
/*else
	{
    if(foreignKeys.size()>0)
    {	
     sb.append(" where ");
	for(int i=0;i<foreignKeys.size();i++)
	{   sb.append(" ");
        if(i!=0)
		{
		sb.append("and ");
		}
		sb.append(foreignKeys.get(i).getColumnName());
		Attribute a=attributeMap.get(foreignKeys.get(i).getColumnName());
		String type=a.getDataType();
		
		
	     sb.append("=?");
	    
	}
    }
	}
	*/
	sb.append(";");
	deleteRowPreparedStatement=sb.toString();
	

}
public void  createInsertPreparedStatement()
{

StringBuilder sb=new StringBuilder();
StringBuilder sb2=new StringBuilder();
 	sb.append("insert into ");
	sb.append(this.getTableName());
	sb.append("(");
	sb2.append(" values(");
	int i=0;
	
	for(Map.Entry<String,Attribute> entry:attributeMap.entrySet())
	{
	
		Attribute a=entry.getValue();

     if(a.is_autoincrement())
     {
     	
     	i++;
     	continue;
     }
     sb.append(a.getAttributeName());
     String type=a.getDataType();
     
     
     sb2.append("?");
     
     if(i!=attributeMap.size()-1)
     {
     
      sb.append(",");
      sb2.append(",");
     }

     i++;
	}
	
	sb.append(")");
	sb2.append(")");
	sb2.append(";");
	insertPreparedStatement=sb.toString()+sb2.toString();
	
}
public void  createUpdatePreparedStatement()
{

StringBuilder sb=new StringBuilder();
 	sb.append("update ");
	sb.append(this.getTableName());
	sb.append(" set ");
	
	int i=0;
	for(Map.Entry<String,Attribute> entry:attributeMap.entrySet())
	{
		Attribute a=entry.getValue();

     if(a.is_primary_key())
     {
     	i++;
     	continue;
     }
     sb.append(a.getAttributeName()+"=");
     String type=a.getDataType();
     
     
     sb.append("?");
     
     if(i!=attributeMap.size()-1)
     {
      sb.append(",");
     }
     i++;
	}
	if(primaryKey.size()>0)
	{
	sb.append(" where ");
	for(i=0;i<primaryKey.size();i++)
	{   sb.append(" ");
        if(i!=0)
		{
		sb.append("and ");
		}
		sb.append(primaryKey.get(i));
		Attribute a=attributeMap.get(primaryKey.get(i));
		String type=a.getDataType();
		
		
	     sb.append("=?");
	    
	}
	}
	/*else
	{
    if(foreignKeys.size()>0)
    {	
     sb.append(" where ");
	for(i=0;i<foreignKeys.size();i++)
	{   sb.append(" ");
         if(i!=0)
		{
		sb.append("and ");
		}
		sb.append(foreignKeys.get(i).getColumnName());
		Attribute a=attributeMap.get(foreignKeys.get(i).getColumnName());
		String type=a.getDataType();
		
		
	     sb.append("=?");
	    
	}
    }
	}
	*/
	sb.append(";");
	updatePreparedStatement=sb.toString();


}

public String getSelectAllPreparedStatement()
{
	return selectAllPreparedStatement;
}
public String getSelectRowPreparedStatement()
{
	return selectRowPreparedStatement;
}
public String getDeleteAllPreparedStatement()
{
	return deleteAllPreparedStatement;
}
public String getDeleteRowPreparedStatement()
{
	return deleteRowPreparedStatement;
}
public String getInsertPreparedStatement()
{
	return insertPreparedStatement;
}
public String getUpdatePreparedStatement()
{
	return updatePreparedStatement;
}
}