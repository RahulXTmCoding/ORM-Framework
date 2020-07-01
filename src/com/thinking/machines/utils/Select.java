import java.util.*;
import java.sql.*;
import java.lang.reflect.*;
import com.tm.orm.util.*;
import com.tm.orm.exception.*;
import com.tm.orm.annotation.*;

public class Select {

private String whereValue;
private String orderByValue;
private String tableName;

//Key -> fieldName
//Value -> ColName
private Map<String, String> map;

private Class clazz;

public Select(Class<?> clazz) {
this.clazz = clazz;
map = new HashMap<>();
analyseClass();
}

public Select where(String val) {
if(val == null || val.isEmpty()) return this;
//Prevent calling 'where' for more then one time
if(!(whereValue == null || whereValue.isEmpty())) return this;

String value = map.get(val);
if(value == null || value.isEmpty()) return this;

else whereValue = " WHERE " + value;

return this;
}

public Select eq(Object value) {
if(value == null) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

if(value.getClass() == boolean.class || value.getClass() == java.lang.Boolean.class) whereValue += (" = " + value);
else whereValue += (" = '" + value + "'");

return this;
}

public Select le(Object value) {
if(value == null) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

whereValue += (" <= '" + value + "'");

return this;
}

public Select lt(Object value) {
if(value == null) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

whereValue += (" < '" + value + "'");

return this;
}

public Select gt(Object value) {
if(value == null) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

whereValue += (" > '" + value + "'");

return this;
}

public Select ge(Object value) {
if(value == null) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

whereValue += (" >= '" + value + "'");

return this;
}

public Select ne(Object value) {
if(value == null) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

if(value.getClass() == boolean.class || value.getClass() == java.lang.Boolean.class) whereValue += (" = " + value);
else whereValue += (" != '" + value + "'");

return this;
}

public Select orderBy(String val) {
if(val == null || val.isEmpty()) return this;

String value = map.get(val);

if(value == null || value.isEmpty()) return this;
if(orderByValue == null || orderByValue.isEmpty()) {
orderByValue = " ORDER BY " + value;
return this;
}

orderByValue += (", " + value); //if orderby is called more then once
return this;
}

public Select ascending() {
if(orderByValue == null || orderByValue.isEmpty()) return this;

orderByValue += " asc";
return this;
}

public Select descending() {
if(orderByValue == null || orderByValue.isEmpty()) return this;

orderByValue += " desc";
return this;
}


public Select and(String value) {
if(value == null || value.isEmpty()) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

whereValue += (" AND " + value);

return this;
}

public Select or(String value) {
if(value == null || value.isEmpty()) return this;
if(whereValue == null || whereValue.isEmpty()) return this;

whereValue += (" OR " + value);

return this;
}


private String generateQueryString() {

String q = "SELECT * FROM " + tableName;

if(!(whereValue == null || whereValue.isEmpty())) {
q += whereValue;
}

if(!(orderByValue == null || orderByValue.isEmpty())) {
q += orderByValue;
}

System.out.println(q);

return q;

}

public List query() throws ORMException {

List list = new LinkedList<>();

try {

String q = generateQueryString();

PreparedStatement preparedStatement = ORMConnection.prepareStatement(q);

ResultSet resultSet = preparedStatement.executeQuery();
Object object;

while(resultSet.next()) {
object = clazz.newInstance();
for(Map.Entry<String, String> entry : map.entrySet()) {
String fieldName = entry.getKey();
String column = entry.getValue();
Field field = clazz.getDeclaredField(fieldName);
field.setAccessible(true);
field.set(object, resultSet.getObject(column));
}

list.add(object);
}

} catch(Exception exception) {
exception.printStackTrace();
throw new ORMException(exception.getMessage());
}

return list;

}

private void analyseClass() {
try {
com.tm.orm.annotation.MapTable mTable = (com.tm.orm.annotation.MapTable) clazz.getAnnotation(com.tm.orm.annotation.MapTable.class);

if(mTable == null) return; //User didn't mention table name.

this.tableName = mTable.value().trim();

if(tableName == null) return; //the table user mentioned does not exists.
Field[] fields = clazz.getDeclaredFields();

String mappedColumn;

for(Field field : fields) {
mappedColumn = getMappedColumn(field);
if(mappedColumn != null) map.put(field.getName(), mappedColumn);
}
} catch(Exception exception) {
exception.printStackTrace();
}
}

private String getMappedColumn(Field field) {
com.tm.orm.annotation.MapColumn mCol = field.getAnnotation(com.tm.orm.annotation.MapColumn.class);
return mCol == null ? null : mCol.value();
}
}

