package project2;

import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.*;
import java.awt.Color;
public class DisplayS extends JFrame{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2297879244509983788L;
	ArrangeCourse pDoc;
	JTable mainTable,rowTable;
	JScrollPane tableFrame;
	DisplayS(ArrangeCourse doc)
	{
		setResizable(false);
		getContentPane().setFont(new Font("����", Font.BOLD, 9));
		pDoc=doc;
		createTable();
		setBounds(0,0,840,480);
		setTitle("�����ſ�ϵͳ-�α�չʾ");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	private void createTable(){
		Object name[]={"����һ","���ڶ�","������","������","������"};
		Object a[][]=new Object[4][5];
		mainTable=new JTable(a,name);
		tableFrame=new JScrollPane(mainTable);
		tableFrame.setBounds(0, 76, 824, 311);
		getContentPane().setLayout(null);
		mainTable.setRowHeight(70);
		getContentPane().add(tableFrame);
		for(int i=0;i<4;i++)
			for(int j=0;j<5;j++)
				mainTable.getModel().setValueAt("",i, j);
		
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
	public static void main(String args[]) throws IOException, UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException{
		ArrangeCourse doc=new ArrangeCourse();
		DisplayS mainFrame=new DisplayS(doc);
		doc.mainFrame=mainFrame;
		doc.mergeDisplay("13-�ƿ�-1");
		//doc.mergeDisplay("13-�ƿ�-2");
		//doc.mergeDisplay("13-�ƿ�-3");
		mainFrame.setVisible(true);
	}

}
	

