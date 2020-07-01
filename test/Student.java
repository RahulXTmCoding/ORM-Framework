import java.util.*;
import java.math.*;
import com.thinking.machines.annotations.*;
import java.text.*;

@TableName(value="student")
public class Student {

@ColumnName(value="roll_number")
@Auto_Increment()
@PrimaryKey()
private int rollNumber;

@ColumnName("name")
public String name;

@ColumnName("gender")
public String gender;

@ColumnName("class")
@PrimaryKey
private String clazz;

@ColumnName("pancard")
private String pancard;

@ColumnName("age")
public int age;

@ColumnName("course_id")

private int courseID;

@ColumnName("department_id")

public int departmentID;

@ColumnName("percentage")
private BigDecimal percentage;

@ColumnName("indian")
private boolean isIndian;

@ColumnName("dob")
public Date dateOfBirth;


public void setRollNumber(int rollNumber) {
this.rollNumber = rollNumber;
}

public int getRollNumber() {
return this.rollNumber;
}

public void setName(String name) {
this.name = name;
}

public String getName() {
return this.name;
}

public void setGender(String gender) {
this.gender = gender;
}

public String getGender() {
return this.gender;
}

public void setClazz(String clazz) {
this.clazz = clazz;
}

public String getClazz() {
return this.clazz;
}

public void setPancard(String pancard) {
this.pancard = pancard;
}

public String getPancard() {
return this.pancard;
}

public void setAge(int age) {
this.age = age;
}

public int getAge() {
return this.age;
}

public void setCourseID(int courseID) {
this.courseID = courseID;
}

public int getCourseID() {
return this.courseID;
}

public void setDepartmentID(int departmentID) {
this.departmentID = departmentID;
}

public int getDepartmentID() {
return this.departmentID;
}

public void setPercentage(BigDecimal percentage) {
this.percentage = percentage;
}

public BigDecimal getPercentage() {
return this.percentage;
}

public void setIsIndian(boolean isIndian) {
this.isIndian = isIndian;
}

public boolean getIsIndian() {
return this.isIndian;
}

public void setDateOfBirth(Date dateOfBirth) {
this.dateOfBirth = dateOfBirth;
}

public Date getDateOfBirth() {
return this.dateOfBirth;
}
}

