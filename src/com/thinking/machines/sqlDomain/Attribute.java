package com.thinking.machines.sqlDomain;
public class Attribute
{
String attributeName;
String dataType;
int dataTyeInt;
int columnSize;
boolean is_nullable;
boolean is_autoincrement;
boolean is_unique;
boolean is_primary_key;
boolean is_foreign_key;
public Attribute()
{
	attributeName="";
	dataType="";
	dataTyeInt=-1;
	
	columnSize=0;
	is_nullable=false;
	is_unique=false;
	is_primary_key=false;
	is_foreign_key=false;
	is_autoincrement=false;
	
}
public void setAttributeName(String attributeName)
{
	this.attributeName=attributeName;
}
public String getAttributeName()
{
	return attributeName;
}
public void setDataType(String dataType)
{
	this.dataType=dataType;
}
public String getDataType()
{
	return dataType;
}
public void setDataTypeInt(int dataTyeInt)
{
	this.dataTyeInt=dataTyeInt;
}
public int getDataTypeInt()
{
	return dataTyeInt;
}
public void setColumnSize(int columnSize)
{
	this.columnSize=columnSize;
}
public int getColumnSize()
{
	return columnSize;
}
public void setIs_nullable(boolean is_nullable)
{
	this.is_nullable=is_nullable;
}
public boolean is_nullable()
{
return is_nullable;
}
public void setIs_autoincrement(boolean is_autoincrement)
{
this.is_autoincrement=is_autoincrement;
}
public boolean is_autoincrement()
{
	return is_autoincrement;
}

public void setIs_unique(boolean is_unique)
{
	this.is_unique=is_unique;
}
public boolean is_unique()
{
return is_unique;
}

public void setIs_primary_key (boolean is_primary_key)
{
	this.is_primary_key=is_primary_key;
}
public boolean is_primary_key()
{
return is_primary_key;
}

public void setIs_foreign_key(boolean is_foreign_key)
{
	this.is_foreign_key=is_foreign_key;
}
public boolean is_foreign_key()
{
return is_foreign_key;
}

}