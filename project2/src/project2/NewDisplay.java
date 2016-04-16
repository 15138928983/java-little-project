package project2;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.*;
import javax.swing.table.TableModel;

import java.awt.Color;
import java.awt.EventQueue;
public class NewDisplay extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2297879244509983788L;
	ArrangeCourse pDoc2;
	static JTable mainTable2;
	JTable rowTable2;
	JScrollPane tableFrame2;
//	private Object change;
	
	NewDisplay(ArrangeCourse doc)
	{
		setResizable(false);
		getContentPane().setFont(new Font("����", Font.BOLD, 9));
		pDoc2=doc;
		createTable();
		setBounds(0,0,840,480);
		setTitle("�����ſ�ϵͳ-�α�չʾ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void createTable(){
		Object name[]={"����һ","���ڶ�","������","������","������"};
		Object A[][]=new Object[4][5];
		mainTable2=new JTable(A,name);
		tableFrame2=new JScrollPane(mainTable2);
		tableFrame2.setBounds(0, 76, 824, 311);
		mainTable2.putClientProperty("Quaqua.Table.style", "striped");
		getContentPane().setLayout(null);
		mainTable2.setRowHeight(70);
		getContentPane().add(tableFrame2);
		int i,j = 0;
		for(i=0;i<4;i++)
			{for(j=0;j<5;j++)
				{mainTable2.getModel().setValueAt("",i, j);}}
					   
			    		   	
		JLabel label = new JLabel("\u667A\u80FD\u6392\u8BFE\u7CFB\u7EDF");
		label.setBounds(247, 0, 335, 69);
		label.setHorizontalAlignment(SwingConstants.CENTER);
		label.setForeground(new Color(30, 144, 255));
		label.setFont(new Font("����", Font.BOLD, 40));
		getContentPane().add(label);
		
		JLabel label_1 = new JLabel("\u5C0F\u7EC4\u6210\u5458\uFF1A\u5F20\u6D0B \u738B\u96EA\u742A \u8FB9\u7433\u7433 \u6210\u660A\u822A");
		label_1.setBounds(247, 397, 328, 45);
		label_1.setFont(new Font("����", Font.BOLD, 16));
		label_1.setHorizontalAlignment(SwingConstants.CENTER);
		getContentPane().add(label_1);
		
		JButton button = new JButton("\u5B89\u5168\u9000\u51FA");
		button.setFont(new Font("����", Font.PLAIN, 12));
		button.setBounds(736, 0, 88, 29);
		getContentPane().add(button);
		button.addMouseListener(new MouseAdapter() {    // ��button��ť��Ӽ����¼�
		    public void mouseClicked(MouseEvent e) {    // �������ʱ
		       Logout.main3();   // �˳�����	
		      setVisible(false);       
		    }
		});
	}
	
	public static void main() throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		ArrangeCourse doc2=new ArrangeCourse();
		NewDisplay mainFrame2=new NewDisplay(doc2);
		doc2.mainFrame2=mainFrame2;
		doc2.mergeNewDisplay("13-�ƿ�-1");
		
		int j = 0;
		int p = 0;
		if(ChangeWindow.a.equals("����һ")){j=0;
		if(ChangeWindow.b.equals("����һ"))p=0;
		if(ChangeWindow.b.equals("���ڶ�"))p=1;
		if(ChangeWindow.b.equals("������"))p=2;
		if(ChangeWindow.b.equals("������"))p=3;
		if(ChangeWindow.b.equals("������"))p=4;}
		else if(ChangeWindow.a.equals("���ڶ�")){j=1;
		if(ChangeWindow.b.equals("����һ"))p=0;
		if(ChangeWindow.b.equals("���ڶ�"))p=1;
		if(ChangeWindow.b.equals("������"))p=2;
		if(ChangeWindow.b.equals("������"))p=3;
		if(ChangeWindow.b.equals("������"))p=4;}
		else if(ChangeWindow.a.equals("������")){j=2;
		if(ChangeWindow.b.equals("����һ"))p=0;
		if(ChangeWindow.b.equals("���ڶ�"))p=1;
		if(ChangeWindow.b.equals("������"))p=2;
		if(ChangeWindow.b.equals("������"))p=3;
		if(ChangeWindow.b.equals("������"))p=4;}
		else if(ChangeWindow.a.equals("������")){j=3;
		if(ChangeWindow.b.equals("����һ"))p=0;
		if(ChangeWindow.b.equals("���ڶ�"))p=1;
		if(ChangeWindow.b.equals("������"))p=2;
		if(ChangeWindow.b.equals("������"))p=3;
		if(ChangeWindow.b.equals("������"))p=4;}
		else if(ChangeWindow.a.equals("������")){j=4;
		if(ChangeWindow.b.equals("����һ"))p=0;
		if(ChangeWindow.b.equals("���ڶ�"))p=1;
		if(ChangeWindow.b.equals("������"))p=2;
		if(ChangeWindow.b.equals("������"))p=3;
		if(ChangeWindow.b.equals("������"))p=4;}  
	int i = 0;
	int q = 0;
	if(ChangeWindow.c.equals("��һ��")){i=0;
		if(ChangeWindow.d.equals("��һ��"))q=0;
		if(ChangeWindow.d.equals("�ڶ���"))q=1;
		if(ChangeWindow.d.equals("������"))q=2;
		if(ChangeWindow.d.equals("���Ľ�"))q=3;}
	else if(ChangeWindow.c.equals("�ڶ���")){i=1;
	if(ChangeWindow.d.equals("��һ��"))q=0;
	if(ChangeWindow.d.equals("�ڶ���"))q=1;
	if(ChangeWindow.d.equals("������"))q=2;
	if(ChangeWindow.d.equals("���Ľ�"))q=3;}
	else if(ChangeWindow.c.equals("������")){i=2;
	if(ChangeWindow.d.equals("��һ��"))q=0;
	if(ChangeWindow.d.equals("�ڶ���"))q=1;
	if(ChangeWindow.d.equals("������"))q=2;
	if(ChangeWindow.d.equals("���Ľ�"))q=3;}
	else if(ChangeWindow.c.equals("���Ľ�")){i=3;
	if(ChangeWindow.d.equals("��һ��"))q=0;
	if(ChangeWindow.d.equals("�ڶ���"))q=1;
	if(ChangeWindow.d.equals("������"))q=2;
	if(ChangeWindow.d.equals("���Ľ�"))q=3;}
		String change=mainTable2.getModel().getValueAt(i, j).toString(); 
		String change2=mainTable2.getModel().getValueAt(q, p).toString(); 
		   String str="ԭ�α�˴��ް���,�޷�����!";
		   if(change.isEmpty())
			   mainTable2.getModel().setValueAt(str,i,j);
		   else{ mainTable2.getModel().setValueAt(change2,i,j);
		   mainTable2.getModel().setValueAt(change,q,p);}
		//doc.mergeNewDisplay("13-�ƿ�-2");
		//doc.mergeNewDisplay("13-�ƿ�-3");
		mainFrame2.setVisible(true);
	}

}
