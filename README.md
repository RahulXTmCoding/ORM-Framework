# ORM-Framework

Java-based Framework for Object Relational Mapping. The framework sets you SQL free. It does all the SQL operations 'under the hood' without letting you know.
User Just Create a object of TMORMframework and all the work can be done using this object.User can execute most of the sql Queries without even knowing any sql.

This Framework Free the user from all the work of managing database and learning sql Update/Query Statements. This Orm Framework is Specially for Mysql only.

Benifits of using ORM FrameWork:-
======================================

1)No need to know about Sql.

2)No Need to write sql statements or performing curd sql Operations.

3)Object Table Relation using Reflection api.

4)Create Tables using Java classes.

5)Multiple Way to perform single type of operations to give user the power to do more.

6)[update] Now user can use the ConfigTool to configure dataBase information.

7)User can also use this Tool to Create equivalent Java Classes for tables in dataBase.

8)Now User dont Have to write java class files,ConfigTool will do that for them in just one click;



Getting Started.(steps to use the Framework)
=============================================
1)Download the this repository.

2)place it anywhere you want.

3)Run the configTool from tools.

Use This Tool to configure file according to your DataBase.

![alt text](https://github.com/RahulXTmCoding/images/blob/master/db.jpg)

4)Just change Details in above tool and click save ,will create a file like this:-
``` markdow
     {
          "driver":"com.mysql.cj.jdbc.Driver",
          "connection_url":"jdbc:mysql://localhost:3306/",
          "database":"orm",
          "username":"rahul",
          "password":"password"
    }
   ``` 
5)Now you can simply use this ormFramework in your projects.

tutorials:-
===========================
This tutorial is divided into 2 parts

1)Annotations Description

2)Actual Implementation(how to perform operations)

Annotations User can use are:
---------------------
1)TableName Annotation:-
User can use this annotation to link sql table to java class,user can simply apply this annotation above class.This annotation tells ORM Framework which sql table should be linked to this table.
Example:-
``` markdow
       @TableName(value="student")
       public class Stud {
       }
```
This tells that stud class should be linked with student table (thus object Relation model).

2)ColumnName Annotation:-
user can use this annotation to link class fields with sql tables columns.Thus Creating Relation between object and sql.
This annotation can only be applied to class fields.
Example:-
``` markdow
        @ColumnName(value="roll_number")
        @Auto_Increment()
        @PrimaryKey()
        private int rollNumber;
```
3)PrimaryKey Annotation:-
Annotation tells the Framework which field should be refered to as primary key of the sql table.
Example:-as shown above.

4)NotNull,Unique,Auto_increment annotations:-
Annotations gives additional information about table columns.
Example:-
``` markdow
         @ColumnName("pancard")
         @Unique()
         @NotNull()
         private String pancard;
  ```      
5)ForeignKey Annotation:-
Annotation can be applied to fields which are linked to columns that are referenced from some another sql Tables.
Example:-
``` markdow
        @Foreignkey(base="city",columnName="c_id",baseColumnName="city_id")
        private int cityId;
```
        
6)ColumnType Annotations:-
Can be used to specify the type of the column in table;
example:-
``` markdow
         @ColumnType(value="varchar(30)")
         private String name;
```
         
Implementation(how to perform operations) :
-------------------------------------------

1)First Example will show you how to do save /delete operations.
``` markdow
   import com.thinking.machines.framework.*;
   class test
   {

    public static void main(String[] args) throws Exception{
 		
 	  City c=new City();
 	  Department d=new Department();
 	  d.name="manager";
 	  c.name="bhopal";
 	  City c2=new City();
 	  c2.id=11;
    TMORMFramework torm=TMORMFramework.getInstance();
    try{
     

     torm.begin();
     torm.save(c);
     torm.commit();
     torm.save(d);
     torm.commit();
     torm.delete(c2);
     torm.commit();
     System.out.println("yes success");
 	  }
    catch(ORMException e)
 	  {
 		torm.rollback();
 		e.printStackTrace();
 	 }
 	 }
   }

```

2)Second Example will show you how to perform Advance operations on Framework:-

This Example shop how to update the table data.
This example also show case how user can simply use the advance Features of Framework to perform Select operation just by calling some methods as shown in below example.
``` markdow

import com.thinking.machines.framework.*;
import java.math.*;
import java.util.*;
import com.thinking.machines.exceptions.*;
public class test2
{
public static void main(String args[]) throws Exception
{
Student s = new Student();
 
 TMORMFramework torm=TMORMFramework.getInstance();
torm.begin();
try{
s.setClazz("8A");
s.name = "sanju baba kumar ";
s.gender = "Male";
s.setPercentage(new BigDecimal(95.0));
s.setPancard("d5kabcvbnjk");

s.setCourseID(1);
s.departmentID = 1;

s.setDateOfBirth(new Date());
s.rollNumber=2;
torm.update(s);
torm.commit();
List<Student> list=torm.select(Student.class).where("rollNumber").eq(1).and().where("name").eq("Rahul singh").query();
torm.commit();
List<Student> list1=torm.select(Student.class).where("rollNumber").eq(1).or().where("name").eq("Rahul singh").query();
torm.commit();
List<Student> list2=torm.select(Student.class).orderBy("name").query();
torm.commit();
List<Student> list5=torm.select(Student.class).where("rollNumber").gt(1).orderBy("name").query();
torm.commit();
List<Student> list3=torm.select(Student.class).where("rollNumber").gt(1).orderBy("name").descending().orderBy("rollNumber").query();
torm.commit();
for(Student ss:list3)
{
	System.out.println("roll_no::"+ss.getRollNumber()+"   ,Name::"+ss.name+"   ,gender::"+ss.gender+"   ,pannumber::"+ss.getPancard());
}
List<Student> list4=torm.select(Student.class).where("rollNumber").gt(1).orderBy("name").descending().query();
torm.commit();
}catch(ORMException o)
{
	torm.rollBack();
	System.out.println(o.toString());
}
}
}

```

3)THis Example will show you you can create new Table inn sql database using this Framework and perform all those operation shown above.

``` markdow
  Just Create a java class apply all the annotation as per your need and just call create method of TMORMFramework.
  
  TMORMFramework torm=TMORMFramework.getInstance();
  torm.begin();
  torm.create(student.class);
  To create table student with details as applied in student class by annotations;
```
Second Method to create table is:-


``` markdow
  
  TMORMFramework torm=TMORMFramework.getInstance();
  torm.begin();
  
  torm.create("student").addColumn("rollnumber").type("int").auto_increment().addColumn("Name").type("varchar(30)").notNull().create().primaryKey("rollnumber");
  
  this will create a table Named Student with 2 column rollnumber,name with type int and  varchar.
  User can call these method to add more information:-
  
  unique()
  
  notNull();
  
  Foreignkey("c_id","city",city_id); first argument to specify all the columns of current table that refrence from Table with name as of second argument and 3rd argument to specify all the column of base table that are referd to this table .first argument can contain multiple columns name as "id,name,c_id" ,same goes for 3rd argument.
  
  PrimaryKey("id,name"); to specify all the columns that make a composite primary key.
  
  To create table student with details as applied in student class by annotations;
```

Lastly dont forget to give path to framework lib because it contains some files on which framework is dependent on;


Some More Images:-
------------------



![alt text](https://github.com/RahulXTmCoding/images/blob/master/Screenshot%20from%202020-07-07%2023-41-35.png)





![alt text](https://github.com/RahulXTmCoding/images/blob/master/Screenshot%20from%202020-07-07%2023-41-40.png)
