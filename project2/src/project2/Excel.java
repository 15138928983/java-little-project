package project2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.WorkbookUtil; 
import org.apache.poi.ss.usermodel.WorkbookFactory;  
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class Excel 
{
	Workbook book;
	private String filename;
	public Excel() {}
	public void create(String filename) throws IOException
	{
		this.filename=filename;
		int xlspos=filename.indexOf("xls");
		if(xlspos<filename.length()) {book=new HSSFWorkbook();}
		else {book=new XSSFWorkbook();}
		FileOutputStream writeFile=new FileOutputStream(filename);
	     book.write(writeFile);
		writeFile.close();
	}
	public void save() throws IOException
	{
		FileOutputStream writeFile=new FileOutputStream(filename);
		book.write(writeFile);
		writeFile.close();
	}
	public Row getNextRow(int Sheet)
	{
		int startPos=book.getSheetAt(Sheet).getLastRowNum()+1;
		Row ret=book.getSheetAt(Sheet).createRow(startPos);
		return ret;
	}
	public Row getRow(int idxSheet,int idxRow) {return book.getSheetAt(idxSheet).getRow(idxRow);}
	public boolean load(String filename) throws IOException
	{
		this.filename=filename;
		File file=new File(filename);
		if(!file.exists()) return false;
		FileInputStream readFile=new FileInputStream(file);
		int xlspos=filename.lastIndexOf("xls");
		int xlsxpos=filename.lastIndexOf("xlsx");
		if(xlspos<filename.length()&&xlspos+3<filename.length()-1&&xlspos!=-1) {book=new HSSFWorkbook(readFile);readFile.close();return true;}
		else if(xlsxpos<filename.length()&&xlsxpos!=-1) {book=new XSSFWorkbook(readFile);readFile.close();return true;}
		readFile.close();
		return false;
	}
	public String getStringValue(Cell cell)
	{
		String ret;
		switch (cell.getCellType())
        {
             case HSSFCell.CELL_TYPE_STRING: // �ַ���
            	ret=String.valueOf(cell.getStringCellValue());
            	break;
             case HSSFCell.CELL_TYPE_NUMERIC: // ����
            	double tmp=cell.getNumericCellValue();
            	int tmp2=(int) cell.getNumericCellValue();
            	if(tmp==tmp2) ret=String.valueOf(tmp2);
            	else ret=String.valueOf(tmp);
                break;
             case HSSFCell.CELL_TYPE_FORMULA: // ��ʽ
            	ret=cell.getCellFormula();
                break;
             case HSSFCell.CELL_TYPE_BLANK: // ��ֵ
            	ret="";
                break;
             case HSSFCell.CELL_TYPE_ERROR: // ����
            	ret="";
                break;
             default:
            	ret="δ֪����";
                break;
        }
		return ret;
	}
	public void printAll(int Sheet,int rowRangeL)
	{
		Sheet st=book.getSheetAt(Sheet);
		for(int i=rowRangeL;i<=st.getLastRowNum();i++)
		{
			Row row=st.getRow(i);
			if(row==null) continue;
			for(int j=0;j<row.getLastCellNum();j++)
			{
				Cell cell=row.getCell(j);
				switch (cell.getCellType())
	            {
	                 case HSSFCell.CELL_TYPE_STRING: // �ַ���
                          System.out.print(cell.getStringCellValue()
                            + "   ");
                          break; 
	                 case HSSFCell.CELL_TYPE_NUMERIC: // ����
	                      System.out.printf("%.2f ",cell.getNumericCellValue());
	                      break;
	                 case HSSFCell.CELL_TYPE_FORMULA: // ��ʽ
                          System.out.print(cell.getCellFormula() + "   ");
                          break;
	                 case HSSFCell.CELL_TYPE_BLANK: // ��ֵ
                          System.out.println(" ");
                          break;
	                 case HSSFCell.CELL_TYPE_BOOLEAN: // Boolean
	                      System.out.println(cell.getBooleanCellValue()
	                            + "   ");
	                      break;	                
	                case HSSFCell.CELL_TYPE_ERROR: // ����
	                      System.out.println(" ");
	                      break;
	                default:
	                     System.out.print("δ֪����  ");
	                    break;
	            }
	        }
			System.out.printf("\n");
		}
	}
}
