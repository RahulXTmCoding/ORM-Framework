
CREATE TABLE department1(
did int primary key,
dname varchar(100) not null
);
CREATE TABLE course(
cid int primary key,
cname varchar(100) not null
);
CREATE table student(
roll_number int AUTO_INCREMENT,
name varchar(200) not null,
gender varchar(6) not null,
class char(3) not null,
pancard varchar(30) unique not null,
age int not null,
course_id int not null,
department_id int not null,
percentage numeric(5,2) not null,
indian boolean not null,
dob Date not null,
primary key(roll_number, class),
foreign key(course_id) REFERENCES course(cid),
foreign key(department_id) REFERENCES department1(did)
);

