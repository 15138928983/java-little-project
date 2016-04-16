package project2;

import java.util.Vector;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class cTask{
	String cID,cName;
	String major,Class;
	int hour,praticedhour;
	String tID,tName;
	Vector<cPos> tuplec; //MAX_SIZE=2
	public cTask(){tuplec=new Vector<cPos>();}
	int start,end;
	public String rname;
}
class cTable{  //3D: (Week,Day,Course)
	int weekNum=25,dayNum=5,courseNum=4;
	Vector<Vector<Vector<String> > > table;
	public cTable(){
		table=new Vector<Vector<Vector<String> > >();
		table.setSize(weekNum+1);
		for(int i=0;i<=weekNum;i++){
			table.set(i, new Vector<Vector<String>>());
			table.get(i).setSize(dayNum+1);
			for(int j=0;j<=dayNum;j++){
				table.get(i).set(j, new Vector<String>());
				table.get(i).get(j).setSize(courseNum+1);}
		}
	}
}
class tSchedule extends cTable{
	public tSchedule(){
		table=new Vector<Vector<Vector<String> > >();
		table.setSize(weekNum+1);
		for(int i=0;i<=weekNum;i++){
			table.set(i, new Vector<Vector<String>>());
			table.get(i).setSize(dayNum+1);
			for(int j=0;j<=dayNum;j++){
				table.get(i).set(j, new Vector<String>());
				table.get(i).get(j).setSize(courseNum+1);
				for(int k=0;k<=courseNum;k++) table.get(i).get(j).set(k,"����");
			}
		}
	}
}
class cPos{
	int day,pos;
	public cPos(int day,int pos){this.day=day;this.pos=pos;
	}
	}

public class ArrangeCourse {
	   Map<String,Vector<cTask> > task=new HashMap<String,Vector<cTask> >();
	   Map<String,cTable> cSchedule=new HashMap<String,cTable>();
	   Map<String,tSchedule> tSchedule=new HashMap<String,tSchedule>();
	   Map<String,LinkedList> Q1=new HashMap<String,LinkedList>();
	   Map<String,LinkedList> Q2=new HashMap<String,LinkedList>();
	   static Map<String,LinkedList> display=new HashMap<String,LinkedList>();
	   public Excel fileEditor;
	   DisplayS mainFrame;
	   public NewDisplay mainFrame2;
	protected static Object doc;
	   ArrangeCourse() throws IOException
	   {
		   fileEditor=new Excel();
		   Serialize();//��Java �������л�Ϊ�������ļ������л�����
		   loadTask();
	   }
	   public cTask getCourseFromMainQ(int week,int day,int pos,String cName){  //����������ȡ����һ�����������ĿΣ���������ʦ����
		   LinkedList mQ=Q1.get(cName);
		   cTask nowTask;
		   tSchedule nowTeacher;
		   for(int i=0;i<mQ.size();i++){
			   nowTask=(cTask) mQ.get(i);
			   nowTeacher=tSchedule.get(nowTask.tName);
			   if(nowTeacher.table.get(week).get(day).get(pos).equals("����")){
				   nowTeacher.table.get(week).get(day).set(pos,cName);
				   mQ.remove(i);
				   nowTask.start=week; //�״γ��У������ʼ�ܱ�ǣ��޸��������ͽ�ʦ�ƻ�
				   Q1.put(cName, mQ);
				   tSchedule.put(nowTask.tName, nowTeacher);
				   return nowTask;
			   }
		   }
		   nowTask=new cTask();
		   nowTask.hour=-1;
		   return nowTask;
	   }
	   public cTask getCourseFromSubQ(int week,int day,int pos,String cName,Map<String,Integer> status){  //�Ӵζ�����ȡ����һ�����������ĿΣ���������ʦ���У����ܴ�������2
		   LinkedList sQ=Q2.get(cName);
		   cTask nowTask;
		   tSchedule nowTeacher;
		   for(int i=0;i<sQ.size();i++){
			   nowTask=(cTask) sQ.get(i);
			   //���ÿε������
			   if(status.containsKey(nowTask.cName)&&status.get(nowTask.cName)>=2){ //��ȡ�����ܶ��У��ܾ��ſ�
				   continue;
			   }
			   boolean offsetFlag=true;
			   if(nowTask.tuplec.size()==2){ //��ʼλ�������꣬��ʼִ��λ�ü��
				   for(int x=0;x<2;x++){
					   if(nowTask.tuplec.get(x).day==day&&nowTask.tuplec.get(x).pos==pos)
						   offsetFlag=false;
				   }
				   if(offsetFlag) continue;
			   }
			   nowTeacher=tSchedule.get(nowTask.tName);
			   if(nowTeacher.table.get(week).get(day).get(pos).equals("����")){
				   nowTeacher.table.get(week).get(day).set(pos,cName);
				   sQ.remove(i);
				   if(nowTask.tuplec.size()<2){ //��ʼλ��δ������
					   nowTask.tuplec.add(new cPos(day,pos));
				   }
				   //�޸Ĵζ����ͽ�ʦ�ƻ�
				   Q2.put(cName, sQ);
				   tSchedule.put(nowTask.tName, nowTeacher);
				   return nowTask;
			   }
		   }
		   nowTask=new cTask();
		   nowTask.hour=-1;
		   return nowTask;
	   }
	   public void changeCourseStatus(Map<String,Integer> status,String cName){
		   if(!status.containsKey(cName)) status.put(cName, 1);
		   else{
			   Integer x=status.get(cName);
			   status.put(cName, x+1);
		   }
	   }
	   public void weekNext(String cName,int week){ 
		   Map<String,Integer> courseStatus=new HashMap<String,Integer>();
		   cTable table3D=cSchedule.get(cName);
		   LinkedList  mQ=new LinkedList(),sQ=new LinkedList();
		   //������ԣ��ȴ���ζ��У�����ζ��з���ĳ�ſο�ʱ���꣬������ӣ����Ҵ���������ȡ��һ���¿�
		   
		   for(int i=1;i<=table3D.dayNum;i++){
				   for(int j=1;j<=table3D.courseNum;j++){
					   cTask c=getCourseFromSubQ(week,i,j,cName,courseStatus);
					   sQ=Q2.get(cName);  //��ȡ���´�����
					   if(c.hour!=-1){  //�Ŵο�
						   c.hour--;
						   if(c.hour==0){  //��ʱΪ0,����������ȡ���¿Σ����浱ǰλ�ã����ֵ�һ�ܵĽṹ����
							   c.end=week;//��ӽ����ܱ��
							   if(!display.containsKey(cName)){ //������ĿΣ�������ʾ����
								   display.put(cName,new LinkedList());
								   display.get(cName).add(c);//
							   }else display.get(cName).add(c);
							   cTask x=getCourseFromMainQ(week+1,i,j,cName);//�ܶ�λ����һ��
							   if(x.hour==-1) continue;
							   changeCourseStatus(courseStatus,x.cName);
							   changeCourseStatus(courseStatus,x.cName);
							   x.tuplec=c.tuplec; //�γ�λ�ò���
							   sQ.add(x);Q2.put(cName, sQ); //���´�����
						   }
						   else{ //��ʱ��Ϊ0����������
							   sQ.add(c);Q2.put(cName, sQ);
							   changeCourseStatus(courseStatus,c.cName);
						   }
					   }
				   }
			   }
	   }
	   public void weekInit(String cName){    //��һ�ܣ���ʼ������
		   cTable table3D=cSchedule.get(cName);
		   Vector<Vector<String>> table2D=table3D.table.get(1);
		   LinkedList  mQ=new LinkedList(),sQ=new LinkedList();
		   //��ʼ��������
		   for(int i=0;i<task.get(cName).size();i++){ 
			   mQ.addLast(task.get(cName).get(i));
		   }
		   Q1.put(cName, mQ);
		   Map<String,Integer> courseStatus=new HashMap<String,Integer>();
		   //��ʼ����һ��
		   for(int i=1;i<=table3D.dayNum;i++){
			   switch(i){
			   case 1:  //��һ�������¿�
				   int cnt=0;
				   for(int j=1;j<=table3D.courseNum&&cnt<3;j++){
					   cTask c=getCourseFromMainQ(1,i,j,cName);
					   if(c.hour==-1) continue;
					   //mainFrame.mainTable.getModel().setValueAt(c.courseName, j-1, i-1);
					   c.hour--;cnt++;
					   if(c.tuplec.size()<2){c.tuplec.add(new cPos(i,j));System.out.printf("%d %d\n",i,j);}  
					   sQ.add(c);Q2.put(cName, sQ); //���´�����
					   changeCourseStatus(courseStatus,c.cName);
				   }
				   break; 
			   case 2:   //�ܶ���������¿�
				   for(int j=1;j<=table3D.courseNum;j++){
					   Q2.get(cName);  //��ȡ���´�����
					   cTask c=getCourseFromMainQ(1,i,j,cName);
					   if(c.hour==-1) continue;
					   //mainFrame.mainTable.getModel().setValueAt(c.courseName, j-1, i-1);
					   c.hour--;
					   if(c.tuplec.size()<2){c.tuplec.add(new cPos(i,j));}  
					   sQ.add(c);Q2.put(cName, sQ);
					   changeCourseStatus(courseStatus,c.cName);
				   }
				   break;
			   case 3:   //��������������¿Σ����ڴο�
				   //�ȴ���ο�,�����¿�
				   int nCnt=0,oCnt=0;
				   for(int j=1;j<=table3D.courseNum;j++){
					   if(oCnt<2){  //�Ĵο�
						   cTask c=getCourseFromSubQ(1,i,j,cName,courseStatus);
						   sQ=Q2.get(cName);  //��ȡ���´�����
						   if(c.hour!=-1){  //�Ŵο�
							   //mainFrame.mainTable.getModel().setValueAt(c.courseName, j-1, i-1);
							   c.hour--;oCnt++;
							   sQ.add(c);Q2.put(cName, sQ);
							   changeCourseStatus(courseStatus,c.cName);
							   continue;
						   }
					   }
					   else{  //������
						   if(nCnt<2){
							   cTask c=getCourseFromMainQ(1,i,j,cName);
							   if(c.hour==-1) continue;
							   //mainFrame.mainTable.getModel().setValueAt(c.courseName, j-1, i-1);
							   c.hour--;
							   if(c.tuplec.size()<2){c.tuplec.add(new cPos(i,j));System.out.printf("%d %d\n",i,j);} 
							   sQ.add(c);Q2.put(cName, sQ);
							   changeCourseStatus(courseStatus,c.cName);
						   }
					   }
				   }
				   break;
			   case 4://ȫ�Ǵο�
				   for(int j=1;j<=table3D.courseNum;j++){
						   cTask c=getCourseFromSubQ(1,i,j,cName,courseStatus);
						   sQ=Q2.get(cName);  //��ȡ���´�����
						   if(c.hour!=-1){  //�Ŵο�
							   //mainFrame.mainTable.getModel().setValueAt(c.courseName, j-1, i-1);
							   c.hour--;
							   sQ.add(c);Q2.put(cName, sQ);
							   changeCourseStatus(courseStatus,c.cName);
						   }
					   }
				   break;
			   case 5: //ȫ�Ǵο�
				   for(int j=1;j<=table3D.courseNum;j++){
					   cTask c=getCourseFromSubQ(1,i,j,cName,courseStatus);
					   sQ=Q2.get(cName);  //��ȡ���´�����
					   if(c.hour!=-1){  //�Ŵο�
						   //mainFrame.mainTable.getModel().setValueAt(c.courseName, j-1, i-1);
						   c.hour--;
						   sQ.add(c);Q2.put(cName, sQ);
						   changeCourseStatus(courseStatus,c.cName);
					   }
				   }
			   break;
		   }
		   }
	   }
	   public void loadTask(){
		   Sheet st=fileEditor.book.getSheetAt(0);
			for(int i=1;i<=st.getLastRowNum();i++)
			{
				Row row=st.getRow(i);
				if(row==null) continue;
				cTask task=new cTask();
				for(int j=0;j<row.getLastCellNum();j++){
					Cell cell=row.getCell(j);
					switch (j){
					case 0:  //�γ�ID
						task.cID=cell.getStringCellValue();
						break;
					case 1:  //�γ�Name
						task.cName=cell.getStringCellValue();
						break;
					case 2:  //רҵ
						task.major=cell.getStringCellValue();
						break;
					case 3:  //�༶
						task.Class=cell.getStringCellValue();
						initClass(task.Class);
						break;
					case 4:  //���ۿ�ʱ
						task.hour=(int) cell.getNumericCellValue();
						task.hour/=2;
						break;
					case 5:  //ʵ���ʱ
						task.praticedhour=(int) cell.getNumericCellValue();
						task.praticedhour/=2;
						break;
					case 6:  //��ʦID
						task.tID=cell.getStringCellValue();
						break;
					case 7:  //��ʦ����
						task.tName=cell.getStringCellValue();
						initTeacher(task.tName);
						break;
		            }
		        }
				if(!this.task.containsKey(task.Class)){
					this.task.put(task.Class, new Vector<cTask>());
					this.task.get(task.Class).add(task);
				}
				else this.task.get(task.Class).add(task);
			}
	   }
	   public void mergeDisplay(String className){
		   String str;
		   String str2=null;
		   LinkedList Q=display.get(className);
		   for(int i=0;i<Q.size();i++)
		   {
			   cTask c= (cTask)Q.get(i);
			   if(c.Class.equals("13-�ƿ�-1"))
			   {
				   for(int x=0;x<2;x++){
				   int day=c.tuplec.get(x).day-1,pos=c.tuplec.get(x).pos-1;
				   String pstr=mainFrame.mainTable.getModel().getValueAt(pos, day).toString();
				   if(i==0)
					   str2="�°�ѧ��401";
				   else if(i>1 && i<10)
					   str2="�°�ѧ��40"+i;
				   else if(i>9)
					   str2="�°�ѧ��4"+i;
			   str=c.cName+"["+str2+"("+String.valueOf(c.start)+"-"
					   		+String.valueOf(c.end)+"��)"+"]";
				   mainFrame.mainTable.getModel().setValueAt
			   					(pstr.isEmpty()?"<html>"+str:pstr+"<br>"+str, pos,day);
			   }
			   }
			   if(c.Class.equals("13-�ƿ�-2"))
			   {
				   for(int x=0;x<2;x++){
				   int day=c.tuplec.get(x).day-1,pos=c.tuplec.get(x).pos-1;
				   String pstr=mainFrame.mainTable.getModel().getValueAt(pos, day).toString();
				   if(i==0)
					   str2="�°�ѧ��401";
				   else if(i>1 && i<10)
					   str2="�°�ѧ��40"+i;
				   else if(i>9)
					   str2="�°�ѧ��4"+i;
			   str=c.cName+"["+str2+"("+String.valueOf(c.start)+"-"
					   		+String.valueOf(c.end)+"��)"+"]";
				   mainFrame.mainTable.getModel().setValueAt
			   					(pstr.isEmpty()?"<html>"+str:pstr+"<br>"+str, pos,day);
			   }		   
			   }
			   if(c.Class.equals("13-�ƿ�-3"))
			   {
				   for(int x=0;x<2;x++){
				   int day=c.tuplec.get(x).day-1,pos=c.tuplec.get(x).pos-1;
				   String pstr=mainFrame.mainTable.getModel().getValueAt(pos, day).toString();
				   if(i==0)
					   str2="�°�ѧ��401";
				   else if(i>1 && i<10)
					   str2="�°�ѧ��40"+i;
				   else if(i>9)
					   str2="�°�ѧ��4"+i;
			   str=c.cName+"["+str2+"("+String.valueOf(c.start)+"-"
					   		+String.valueOf(c.end)+"��)"+"]";
				   mainFrame.mainTable.getModel().setValueAt
			   					(pstr.isEmpty()?"<html>"+str:pstr+"<br>"+str, pos,day);
				   }
			   }
		   }
	   }
	   
	   public void mergeNewDisplay(String className){
		   String str;
		   String str2=null;
		   LinkedList Q=display.get(className);
		   for(int i=0;i<Q.size();i++)
		   {
			   cTask c= (cTask)Q.get(i);
			   if(c.Class.equals("13-�ƿ�-1"))
			   {
				   for(int x=0;x<2;x++){
				   int day=c.tuplec.get(x).day-1,pos=c.tuplec.get(x).pos-1;
				   String pstr=mainFrame2.mainTable2.getModel().getValueAt(pos, day).toString();
				   if(i==0)
					   str2="�°�ѧ��301";
				   else if(i>1 && i<10)
					   str2="�°�ѧ��30"+i;
				   else if(i>9)
					   str2="�°�ѧ��3"+i;
			   str=c.cName+"["+str2+"("+String.valueOf(c.start)+"-"
					   		+String.valueOf(c.end)+"��)"+"]";
				   mainFrame2.mainTable2.getModel().setValueAt
			   					(pstr.isEmpty()?"<html>"+str:pstr+"<br>"+str, pos,day);
			   }
			   }
			   if(c.Class.equals("13-�ƿ�-2"))
			   {
				   for(int x=0;x<2;x++){
				   int day=c.tuplec.get(x).day-1,pos=c.tuplec.get(x).pos-1;
				   String pstr=mainFrame2.mainTable2.getModel().getValueAt(pos, day).toString();
				   if(i==0)
					   str2="�°�ѧ��301";
				   else if(i>1 && i<10)
					   str2="�°�ѧ��30"+i;
				   else if(i>9)
					   str2="�°�ѧ��3"+i;
			   str=c.cName+"["+str2+"("+String.valueOf(c.start)+"-"
					   		+String.valueOf(c.end)+"��)"+"]";
				   mainFrame2.mainTable2.getModel().setValueAt
			   					(pstr.isEmpty()?"<html>"+str:pstr+"<br>"+str, pos,day);
			   }		   
			   }
			   if(c.Class.equals("13-�ƿ�-3"))
			   {
				   for(int x=0;x<2;x++){
				   int day=c.tuplec.get(x).day-1,pos=c.tuplec.get(x).pos-1;
				   String pstr=mainFrame2.mainTable2.getModel().getValueAt(pos, day).toString();
				   if(i==0)
					   str2="�°�ѧ��301";
				   else if(i>1 && i<10)
					   str2="�°�ѧ��30"+i;
				   else if(i>9)
					   str2="�°�ѧ��3"+i;
			   str=c.cName+"["+str2+"("+String.valueOf(c.start)+"-"
					   		+String.valueOf(c.end)+"��)"+"]";
				   mainFrame2.mainTable2.getModel().setValueAt
			   					(pstr.isEmpty()?"<html>"+str:pstr+"<br>"+str, pos,day);
				   }
			   }		 
		   }
	   }
	   
	   public void Serialize() throws IOException
	   {
		   if(!fileEditor.load("course.xlsx"))
		   {
			   fileEditor.create("course.xlsx");
			   fileEditor.load("course.xlsx");
		   }
	   }
	   public void initTeacher(String tName){
		   if(!tSchedule.containsKey(tName)){
			   tSchedule.put(tName, new tSchedule());
		   }
	   }
	   public void initClass(String cName){
		   if(!cSchedule.containsKey(cName)){
			   cSchedule.put(cName, new cTable());
		   }
	   }
	
	   public static void main(String args[]) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException{
			ArrangeCourse doc=new ArrangeCourse();
			DisplayS mainFrame=new DisplayS(doc);
			mainFrame.setResizable(false);
			doc.mainFrame=mainFrame;
			mainFrame.setTitle("�����ſ�ϵͳ-"+"13-�ƿ�-1"+"��-�α�չʾ");
			//mainFrame.setTitle("�����ſ�ϵͳ-"+"13-�ƿ�-2"+"��-�α�չʾ");
			//mainFrame.setTitle("�����ſ�ϵͳ-"+"13-�ƿ�-3"+"��-�α�չʾ");
			
			JButton button = new JButton("\u8C03\u6574\u8BFE\u8868");
			button.setFont(new Font("����", Font.PLAIN, 12));
			button.setBounds(555, 27, 82, 25);
			mainFrame.getContentPane().add(button);
			button.addMouseListener(new MouseAdapter() {    // ��button��ť��Ӽ����¼�
			    public void mouseClicked(MouseEvent e) {    // �������ʱ
			       ChangeWindow.main(args);
			       mainFrame.dispose();
			    }
			});
			doc.weekInit("13-�ƿ�-1");
			for(int i=2;i<22;i++) 
				doc.weekNext("13-�ƿ�-1", i);
		        doc.mergeDisplay("13-�ƿ�-1");
		        
			doc.weekInit("13-�ƿ�-2");
			for(int i=2;i<22;i++) 
				doc.weekNext("13-�ƿ�-2", i);
			  //doc.mergeDisplay("13-�ƿ�-2");
			
			doc.weekInit("13-�ƿ�-3");
			for(int i=2;i<22;i++) 
				doc.weekNext("13-�ƿ�-3", i);
			  //doc.mergeDisplay("13-�ƿ�-3");
			mainFrame.setVisible(true);
		}   
}




