package com.thinking.machines.sqlDomain;
public class ForeignKey
{
String columnName;
String primaryTableName;
String primaryColumnName;
public ForeignKey()
{
	columnName="";
	primaryColumnName="";
	primaryTableName="";
}
public void setColumnName(String columnName)
{
	this.columnName=columnName;
}
public String getColumnName()
{
	return columnName;
} 
public void setPrimaryColumnName(String primaryColumnName)
{
	this.primaryColumnName=primaryColumnName;
}
public String getPrimaryColumnName()
{
	return primaryColumnName;
}
public void setPrimaryTableName(String primaryTableName)
{
	this.primaryTableName=primaryTableName;
}
public String getPrimaryTableName()
{
	return primaryTableName;
}
}