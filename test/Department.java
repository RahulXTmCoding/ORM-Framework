import com.thinking.machines.annotations.*;
@TableName(value="department1")
public class Department
{   
	@PrimaryKey()
	@Auto_Increment
	@ColumnName(value="did")
	public int id;

	@ColumnName(value="dname")
	public String name;
}