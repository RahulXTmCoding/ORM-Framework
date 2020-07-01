package com.thinking.machines.framework
import com.thinking.machines.sqlDomain.*;
import com.thinking.machines.framework.*;
import java.util.*;
public class TMORMFramework
{
public Map<String,Table> tables;
TMORMFramework()
{
tables=new HashMap<>();
SqlAnalizer.getDataBaseMetaData(tables);
}

}