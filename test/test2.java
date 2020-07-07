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
s.setRollNumber(2);
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
