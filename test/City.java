import com.thinking.machines.annotations.*;
import java.lang.*;
@TableName(value="city")
public class City
{   
	@PrimaryKey()
	@Auto_Increment
	@ColumnName(value="city_id")
	public Integer id;

	@ColumnName(value="city_name")
	public String name;
}