
import javax.swing.*;
import java.awt.*;
import java.awt.event.*; 
import javax.swing.JTable;
import javax.swing.table.*; 
public class ConfigTool
{
public JFrame f;
public JTabbedPane tp;
public JPanel dbPanel;
public JPanel tandvPanel;
public JLabel dbName;
public JLabel userName;
public JLabel pass;
public JLabel dbHeading;
public JTextField dbNameField;
public JTextField userNameField;
public JTextField passField;
public DbModel model;
public JLabel dbData;

public JLabel Heading;
public JButton creatClasses;
public JTable tablet;
public JScrollPane scrollPane ;
public JButton saveDbInfo;
public ConfigTool()
{
f=new JFrame("Config Tool");
model=new DbModel(this);
tp=new JTabbedPane();
tp.setSize(700,600);
dbPanel=new JPanel();
tandvPanel=new JPanel();
dbPanel.setLayout(null);
dbPanel.setVisible(true);



 dbName=new JLabel("DataBase Name:");  
 dbName.setBounds(10,70,300,20);
 dbName.setFont(new Font("Serif", Font.PLAIN, 20));
 dbNameField=new JTextField();  
 dbNameField.setBounds(10,100,670,40);
 dbNameField.setFont(new Font("Serif", Font.PLAIN, 20));


userName=new JLabel("UserName:");  
 userName.setBounds(10,160,150,20);
 userName.setFont(new Font("Serif", Font.PLAIN, 20));
 userNameField=new JTextField();  
 userNameField.setBounds(10,190,670,40);
 userNameField.setFont(new Font("Serif", Font.PLAIN, 20));

dbData=new JLabel("<html>"+"This tool can be used to Config DataBase without Manually editing files .User can also create Java Classes for Tables present in DataBase using this tool in a single Click"+"</html>");  
 dbData.setBounds(10,400,670,150);
 dbData.setFont(new Font("Serif", Font.PLAIN, 16));
 

 pass=new JLabel("Password:");  
 pass.setBounds(10,250,150,20);
 pass.setFont(new Font("Serif", Font.PLAIN, 20));
 passField=new JTextField();  
 passField.setBounds(10,280,670,40);
 passField.setFont(new Font("Serif", Font.PLAIN, 20));

saveDbInfo=new JButton("Save");  
    saveDbInfo.setBounds(600,340,80,40);  
    saveDbInfo.addActionListener(new ActionListener(){  
public void actionPerformed(ActionEvent e){  
           
           model.updateDbConfig(dbNameField.getText(),userNameField.getText(),passField.getText());



        }  
    });  
creatClasses=new JButton("Create All");  
    creatClasses.setBounds(570,25,120,40);  
    creatClasses.addActionListener(new ActionListener(){  
public void actionPerformed(ActionEvent e){  
           
          model.createAllClasses();


        }  
    });  

tandvPanel.add(creatClasses);
JLabel split=new JLabel("*********************");
split.setBounds(10,400,470,20);

dbHeading=new JLabel("DataBase Information:-");
dbHeading.setBounds(10,0,400,50);
dbHeading.setFont(new Font("Serif", Font.PLAIN, 30));

Heading=new JLabel("Tables And Views:-");
Heading.setBounds(10,0,400,50);
Heading.setFont(new Font("Serif", Font.PLAIN, 30));

tandvPanel.add(Heading);

tablet = new JTable(model);
tablet.setPreferredScrollableViewportSize(new Dimension(680, 70)); 	
tablet.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
tablet.setRowHeight(40);
TableColumnModel columnModel = tablet.getColumnModel();
columnModel.getColumn(0).setPreferredWidth(85);
columnModel.getColumn(1).setPreferredWidth(200);
columnModel.getColumn(2).setPreferredWidth(100);
columnModel.getColumn(3).setPreferredWidth(200);
columnModel.getColumn(4).setPreferredWidth(85);
DefaultTableCellRenderer rendar1 = new DefaultTableCellRenderer();
    rendar1.setForeground(Color.red);

 tablet.getColumnModel().getColumn(4).setCellRenderer(rendar1);

tablet.addMouseListener(new java.awt.event.MouseAdapter()

            {

public void mouseClicked(java.awt.event.MouseEvent e)

{

int row=tablet.rowAtPoint(e.getPoint());

int col= tablet.columnAtPoint(e.getPoint());

if(col<4) return;
model.createClass(row);


}

}

);





scrollPane= new JScrollPane(tablet);
scrollPane.setBounds(10,70,680,400);
tandvPanel.add(scrollPane);
dbPanel.add(dbName);
dbPanel.add(dbNameField);
dbPanel.add(dbHeading);
dbPanel.add(userName);
dbPanel.add(userNameField);
dbPanel.add(pass);
dbPanel.add(dbData);
dbPanel.add(passField);
dbPanel.add(saveDbInfo);
dbPanel.add(split);

tandvPanel.setLayout(null);
tandvPanel.setVisible(true);
tp.add("DataBase Info",dbPanel);
tp.add("Tables and Views",tandvPanel);
tp.setVisible(true);
f.add(tp);
f.setSize(700, 600);  
f.setLocationRelativeTo(null);  
f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
f.setVisible(true);  
model.filldbData();
}
public static void main(String gg[])
{
	ConfigTool ui=new ConfigTool();
}
}
