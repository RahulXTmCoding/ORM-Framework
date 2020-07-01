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
 	}catch(Exception e)
 	{
 		torm.rollback();
 		e.printStackTrace();
 	}
 	}
 }