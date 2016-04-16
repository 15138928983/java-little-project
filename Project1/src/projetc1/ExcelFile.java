package projetc1;
import java.util.Iterator;
import java.util.Map;
import java.util.HashMap;
import java.io.*;

import org.apache.poi.hssf.extractor.ExcelExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
/**
 //����poi��ȡexcel�ļ�����
 
 */
public class ExcelFile {

        /**
         * @param args
         */
//��Ա����

	//д��Excel���ݣ����޸� ��ʱ�����ǲ�������
	class StudentInfo{
		public int ID;				//ѧ��
		public String Name;			//����
		public String Spec;			//רҵ
		public String Class;		//�༶
		public int SC=0;				//Score_Course
		public int SH=0;				//Score_Homework
		public int SL=0;				//Score_Lab
		public int SP=0;				//Score_Project
		public int SE=0;				//Score_Exam	
		public int STotal=0;			//Score_TOTAL(SC+SH+SL+SP+SE)
		public boolean init=false;
		public void calcTotal()
		{
			this.STotal=this.SC+this.SH+this.SL+this.SP+this.SE;
		}
}
		Map<Integer,StudentInfo> stuTable=new HashMap<Integer,StudentInfo>();			//OUT : ScoreList
		//��ȡ�������
		public String[] TablestuStr=new String[20];
		//����������
		public static  String[] Memory=new String[20];
		public int NumOfStu=0;
//д���ļ�
       public void writeExcel(String filename) throws FileNotFoundException
       {
    	   File file=new File(filename);
    	   FileOutputStream fOut=null;
    	   FileInputStream in=new FileInputStream(file);
    		  try{  
    			  HSSFWorkbook workbook=new HSSFWorkbook(in);
       		   HSSFSheet sheet=workbook.getSheetAt(0);
       		   //�Ӽ�¼���кŸ�����һ��
       		   int RowNum=Integer.parseInt(Memory[0]);
       		   int ColNum;
       		   //����ǰ��
       		   HSSFRow  row=sheet.getRow(RowNum);
         		   int lastCellNum=row.getLastCellNum();
       		   HSSFCell cell;
       		   for(ColNum=4;ColNum<lastCellNum;ColNum++){
       			   //��ȡ��ǰλ�õ�Ԫ��
       			   cell=row.getCell(ColNum);
       			switch (cell.getCellType()) {
       		   case HSSFCell.CELL_TYPE_NUMERIC: // ����
       			   			double s=Double.parseDouble(Memory[ColNum]);
       			   			cell.setCellValue(s);
       			   		System.out.println("here1 "+s);
       			   			break;
       		   case HSSFCell.CELL_TYPE_STRING: // �ַ���
       			             cell.setCellValue(Memory[ColNum]);
       			          System.out.println("here2 "+Memory[ColNum]);
       			          System.out.println("col:"+cell.getColumnIndex() + "row: "+cell.getRowIndex()+"value"+cell.getStringCellValue());
       			          break;
       		  default:
       			  System.out.println("here3");
       			            	 break;
             	}
       			  
       			  //cell.setCellValue(Memory[0]);
       			   //cell=cell.setCellValue();
       		   }
    		   //�½�һ���ļ������
    		   fOut=new FileOutputStream(file);
    		   //����Ӧ��excel����������
    		   workbook.write(fOut);
    		   fOut.flush();
    		   fOut.close();//�����������ر��ļ�
    	   }catch(Exception e){
    		   System.out.println("����ʧ��"+e);
    	   }finally{
    		   if(fOut!=null){
    			   try{
    				   fOut.close();
    			   }catch(IOException e1){
    		 
    			   }
    		   }
    	   }
    		  }
       //��Excel�ļ�   ע���ַ����Լ����ֱ��������
       public void readExcel(String filename,String text)
       {
    	   File file=new File(filename);//�ο�
    	   FileInputStream in=null;
    	 //  HSSFWorkbook workbook=new HSSFWorkbook(FileUtils.)
    	   try{
    		   //����Excel������������
    		   in=new FileInputStream(file);
    		   HSSFWorkbook workbook=new HSSFWorkbook(in); //�����Թ����������
    		   HSSFSheet sheet=workbook.getSheetAt(0); //��Excel�������У���һ���������Ĭ��������0
    		  
    		   StudentInfo CurrentStu=new StudentInfo(); //�½�һ��ѧ����
    		   
    		 
    		   System.out.println("��ȡ�����"+file.getAbsolutePath()+"������");  //��ȡExcel
    		   HSSFRow row=null;
    		   HSSFCell cell=null;
    		   int rowNum=0;   //�� ��
    		   int colNum=0;
    		   int CurrentStuID=0;
    		   //��ȡsheet�����һ���к�
    		   int lastRowNum=sheet.getLastRowNum();
    		   for(rowNum=1;rowNum<lastRowNum;rowNum++){
    			   //��ȡ��
    			    row=sheet.getRow(rowNum);
    			   //��ȡ��ǰ�еĵ�һ��
    			   HSSFCell cell1=row.getCell(0);
    			cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
					String str1=text;
					String s=cell1.getStringCellValue();
					
	    			   int lastCellNum=row.getLastCellNum();
	    			    if(str1.equals(s)){
	    			    	//��¼�´�ʱ���к�
	    			    	TablestuStr[15]=String.valueOf(rowNum);
    				   System.out.println(rowNum);
    			    	 for(colNum=0;colNum<lastCellNum;colNum++){
     				   cell=row.getCell(colNum);
     					   switch (cell.getCellType())   {
        		                case HSSFCell.CELL_TYPE_NUMERIC: // ����
        		                	//��һ��Ϊѧ�����ݣ�Ӧ�����ַ����ķ�ʽ���
        		                	switch(colNum){
        		                	
        		                	   case 4:System.out.printf("%.2f     ",cell.getNumericCellValue());
        		                	            CurrentStu.SC=(int)(cell.getNumericCellValue());
                                                   break;
        		                	   case 5:System.out.printf("%.2f     ",cell.getNumericCellValue());
        		                	           CurrentStu.SH=(int)(cell.getNumericCellValue());
        		                	    	    	break;
        		                	   case 6:System.out.printf("%.2f     ",cell.getNumericCellValue());
        		                	            CurrentStu.SL=(int)(cell.getNumericCellValue());
        		                	   			   break;
        		                	   case 7:System.out.printf("%.2f     ",cell.getNumericCellValue());
        		                	             CurrentStu.SP=(int)(cell.getNumericCellValue());
        		                	              break;
        		                	   case 8:System.out.printf("%.2f     ",cell.getNumericCellValue());
        		                	             CurrentStu.SE=(int)(cell.getNumericCellValue());
        		                	                break;
        		                	   case 9:System.out.printf("%.2f     ",cell.getNumericCellValue());
        		                	              CurrentStu.calcTotal();
               		                            break;
        		                	  // case 10:System.out.printf("%.2f     ",cell.getNumericCellValue());
        		                	          //    break;
        		                	}
        		                	break;
        		             
        		                	
        		                case HSSFCell.CELL_TYPE_STRING: // �ַ���
        		                	switch(colNum){
        		                	case 0:
        		                		     System.out.print(cell.getStringCellValue()  + "\t");
        		                	         CurrentStuID=Integer.valueOf(cell.getStringCellValue()).intValue();
        		                	        CurrentStu.ID=CurrentStuID;
        		                	        break;
        		                	case 1:System.out.print(cell.getStringCellValue()  + "\t");
        		                	           CurrentStu.Name=cell.getStringCellValue();
        		                	           break;
        		                	case 2:System.out.print(cell.getStringCellValue()  + "\t");
        		                	           CurrentStu.Spec=cell.getStringCellValue();
        		                	           break;
        		                	case 3:
        		                	        System.out.print(cell.getStringCellValue()  + "\t");
        		                	        CurrentStu.Class=cell.getStringCellValue();
        		                	        break;
        		                	}
        		                    break;
        		                default:
        		                    System.out.print("δ֪����   ");
        		                    break;
        		            }
 					   }
     				  
     		        }
	    			    CurrentStu.init=true;
	    				stuTable.put(CurrentStuID,CurrentStu);
	    				NumOfStu++;
	    				TablestuStr[0]=String.valueOf(CurrentStu.ID);
	    				TablestuStr[1]=CurrentStu.Name;
	    				TablestuStr[2]=CurrentStu.Spec;
	    				TablestuStr[3]=CurrentStu.Class;
	    				TablestuStr[4]=String.valueOf(CurrentStu.SC);
	    				TablestuStr[5]=String.valueOf(CurrentStu.SH);
	    				TablestuStr[6]=String.valueOf(CurrentStu.SL);
	    				TablestuStr[7]=String.valueOf(CurrentStu.SP);
	    				TablestuStr[8]=String.valueOf(CurrentStu.SE);
	    				TablestuStr[9]=String.valueOf(CurrentStu.STotal);
    		}   
    		   in.close();
    	   }catch (Exception e){
    		   e.printStackTrace();
    	   }finally{
    		   if(in!=null){
    			   try{
    				   in.close();
    			   }catch(IOException e1){
    				   
    			   }
    		   }
    	   }
       }
}
