package com.thinking.machines.beans;
public class DbConfigBean
{
String driver;
String connection_url;
String database;
String username;
String password;
public DbConfigBean()
{
	driver="";
	connection_url="";
	database="";
	username="";
	password="";
}

public void setDriver(String driver)
{
	this.driver=driver;
}
public String getDriver()
{
	return driver;
}
public void setConnection_url(String connection_url)
{
	this.connection_url=connection_url;
}
public String getConnection_url()
{
	return connection_url;
}
public void setDatabase(String database)
{
	this.database=database;
}
public String getDatabase()
{
	return database;
}
public void setUsername(String username)
{
	this.username=username;
}
public String getUsername()
{
	return username;
}
public void setPassword(String password)
{
	this.password=password;
}
public String getPassword()
{
	return password;
}
}