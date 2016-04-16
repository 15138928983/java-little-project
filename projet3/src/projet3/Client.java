package projet3;

import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;  
import java.awt.GridLayout;
import java.awt.Toolkit;  
import java.awt.event.ActionEvent;  
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;  
import java.awt.event.WindowEvent;  
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;  
import java.io.InputStreamReader;  
import java.io.PrintWriter;  
import java.io.File;
import java.net.Socket;  
import java.util.List;
import java.util.HashMap;  
import java.util.Map;  
import java.util.StringTokenizer;  
 import java.net.*;
 import �ļ�.Send_file;

import javax.swing.DefaultListModel;  
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;  
import javax.swing.JLabel;  
import javax.swing.JList;  
import javax.swing.JOptionPane;  
import javax.swing.JPanel;  
import javax.swing.JScrollPane;  
import javax.swing.JSplitPane;  
import javax.swing.JTextArea;  
import javax.swing.JTextField;  
import javax.swing.border.TitledBorder;



import javax.swing.SwingConstants;  
  
public class Client{  
  
    private JFrame frame;  
    private JList userList;  
    private JTextArea textArea;  
    private JTextField textField;  
    private JTextField txt_port;  
    private JTextField txt_hostIp;  
    private JTextField txt_name;  
    private JButton btn_start;  
    private JButton btn_stop;  
    private JButton btn_send; 
    private JButton btn_file;
    private JPanel northPanel;  
    private JPanel southPanel;  
    private JScrollPane rightScroll;  
    private JScrollPane leftScroll;  
    private JSplitPane centerSplit;  
  
    public	List to = null;
    private static String filePath;
    private static JPanel contentPane;
    
    private DefaultListModel listModel;  
    private boolean isConnected = false;  
  
    private Socket socket;  
    private PrintWriter writer;  
    private BufferedReader reader;  
    private MessageThread messageThread;// ���������Ϣ���߳�  
    private Map<String, User> onLineUsers = new HashMap<String, User>();// ���������û�  
  
	DataInputStream in = null;
	DataOutputStream out = null;
    // ���췽��  
    public Client() {  
        textArea = new JTextArea();  
        textArea.setEditable(false);  
        textArea.setForeground(new Color(107,142,35));  
        textField = new JTextField();
       // textField.setHorizontalAlignment(SwingConstants.LEFT);
        txt_port = new JTextField(" ");  //�˿ںŲ�һ��������ip��ַ��һ����
        txt_hostIp = new JTextField("127.0.0.1");  
        txt_name = new JTextField("123");  
        btn_start = new JButton("����");  
        btn_stop = new JButton("�Ͽ�");  
        btn_send = new JButton("����");  
         btn_file=new JButton("�ļ�");
        listModel = new DefaultListModel();  
        userList = new JList(listModel);  
  
        northPanel = new JPanel();  
        northPanel.setLayout(new GridLayout(1, 7));  
        northPanel.add(new JLabel("         �˿�"));  
        northPanel.add(txt_port);  
        northPanel.add(new JLabel("     ������IP"));  
        northPanel.add(txt_hostIp);  
        northPanel.add(new JLabel("         ����"));  
        northPanel.add(txt_name);  
        northPanel.add(btn_start);  
        northPanel.add(btn_stop);  
        northPanel.setBorder(new TitledBorder("������Ϣ"));  
  
        rightScroll = new JScrollPane(userList);  
        rightScroll.setBorder(new TitledBorder("��ǰ�����û�"));  
        leftScroll = new JScrollPane(textArea);  
        leftScroll.setBorder(new TitledBorder("��Ϣ��ʾ��"));  
       
        southPanel = new JPanel();
        southPanel.setLayout(new GridLayout(1, 3)); 
        southPanel.add(textField); 
        southPanel.add(btn_file);
        southPanel.add(btn_send);  
        southPanel.setBorder(new TitledBorder("д��Ϣ"));  
  
        centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll,  
                rightScroll);  
        centerSplit.setDividerLocation(450);  
  
        frame = new JFrame("�ͻ���  ��ע�Ᵽ��������������");  
        frame.getContentPane().setLayout(new BorderLayout());  
        frame.getContentPane().add(northPanel, "North");  
        frame.getContentPane().add(centerSplit, "Center");  
        frame.getContentPane().add(southPanel, "South");  
        frame.setSize(623, 423);  
        frame.setLocation(200, 50); 
        frame.setVisible(true);  
  
        // ������Ϣ�س��¼�
        textField.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent arg0) {  
                send();  
            }  
        });  
  
        // �������Ͱ�ťʱ�¼�  
        btn_send.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
           	List to = userList.getSelectedValuesList();
    				
    				if (to.size() < 1) {
       					System.out.println("1111");
       					//to.clear();
    				   send();  
    				}
    				else{
    					//textField.setText("����"+to+":");
    					//System.out.println("shuchu ");
    					//to.clear();
    					sendtwo();
    					
    				}
                  
            }  
        });  
        Send_file listener=new Send_file();
        String ip="127.0.0.1";
        listener.get_ip(ip);
        btn_file.addActionListener(listener);
        
        
    
        // �������Ӱ�ťʱ�¼�  
        btn_start.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                int port;  
                if (isConnected) {  
                    JOptionPane.showMessageDialog(frame, "�Ѵ���������״̬����Ҫ�ظ�����!",  
                            "����", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    try {  
                        port = Integer.parseInt(txt_port.getText().trim());  
                    } catch (NumberFormatException e2) {  
                        throw new Exception("�˿ںŷ�ΧΪ0-65535��������");  
                    }  
                    if (port < 0) {  
                        throw new Exception("�˿ںŷ�ΧΪ0-65535��");  
                    } 
                    String hostIp = txt_hostIp.getText().trim();  
                    String name = txt_name.getText().trim();  
                    if (name.equals("") || hostIp.equals("")) {  
                        throw new Exception("������������IP����Ϊ��!");  
                    }  
                    boolean flag = connectServer(port, hostIp, name);  
                    if (flag == false) {  
                        throw new Exception("�����������ʧ��!");  
                    }  
                    frame.setTitle(name);  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "����", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
  
        // �����Ͽ���ťʱ�¼�  
        btn_stop.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                if (!isConnected) {  
                    JOptionPane.showMessageDialog(frame, "�Ѵ��ڶϿ�״̬����Ҫ�ظ��Ͽ�!",  
                            "����", JOptionPane.ERROR_MESSAGE);  
                    return;  
                }  
                try {  
                    boolean flag = closeConnection();// �Ͽ�����  
                    if (flag == false) {  
                        throw new Exception("�Ͽ����ӷ����쳣��");  
                    }  
                    JOptionPane.showMessageDialog(frame, "�ɹ��Ͽ�!");  
                } catch (Exception exc) {  
                    JOptionPane.showMessageDialog(frame, exc.getMessage(),  
                            "����", JOptionPane.ERROR_MESSAGE);  
                }  
            }  
        });  
  
        // �رմ���ʱ�¼�  
        frame.addWindowListener(new WindowAdapter() {  
            public void windowClosing(WindowEvent e) {  
                if (isConnected) {  
                    closeConnection();// �ر�����  
                }  
                System.exit(0);// �˳�����  
            }  
        });  
      //�б�ѡ��˽��
      userList.addMouseListener(new MouseAdapter() {  
        	   public void mouseClicked(MouseEvent e) {
        			List to = userList.getSelectedValuesList();
        		//	if (e.getClickCount() == 1) {
        				//System.out.println("1111");
       			// }
    				if (e.getClickCount() == 2) {
    				    System.out.println("2222");
    				    
    			 }
        
       }
    }   );
      
    }
  

    // ִ�з���  
    public void send() {  
        if (!isConnected) {  
            JOptionPane.showMessageDialog(frame, "��û�����ӷ��������޷�������Ϣ��", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        String message = textField.getText().trim();  
        System.out.println("11111"+message);
        if (message == null || message.equals("")) {  
            JOptionPane.showMessageDialog(frame, "�������ݲ���Ϊ�գ����������룡", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        sendMessage(frame.getTitle() + "@" + "ALL" + "@" + message);  
        textField.setText(null);  
    }  
    
    
    
  //һ���µ�send
    public void sendtwo() {  
        if (!isConnected) {  
            JOptionPane.showMessageDialog(frame, "��û�����ӷ��������޷�������Ϣ��", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        String message = textField.getText().trim(); 
        System.out.println("222222"+message);
        if (message == null || message.equals("")) {  
            JOptionPane.showMessageDialog(frame, "�������ݲ���Ϊ�գ����������룡", "����",  
                    JOptionPane.ERROR_MESSAGE);  
            return;  
        }  
        sendMessagetwo(frame.getTitle() + "@" + "to" + "@" + message);  
        textField.setText(null);  
    }  
    
    public boolean connectServer(int port, String hostIp, String name) {  
        // ���ӷ�����  
        try {  
            socket = new Socket(hostIp, port);// ���ݶ˿ںźͷ�����ip��������  
            writer = new PrintWriter(socket.getOutputStream());  
            reader = new BufferedReader(new InputStreamReader(socket  
                    .getInputStream()));  
            // ���Ϳͻ����û���Ϣ
            sendMessage(name + "@" + socket.getLocalAddress().toString());
            messageThread = new MessageThread(reader, textArea);  
            messageThread.start();  
            isConnected = true;  
            return true;  
        } catch (Exception e) {  
            textArea.append("��˿ں�Ϊ��" + port + "    IP��ַΪ��" + hostIp  
                    + "   �ķ���������ʧ��!" + "\r\n");  
            isConnected = false;// δ������  
            return false;  
        }  
    }  
  
   //������Ϣ  
    public void sendMessage(String message) {  
        writer.println(message);  
        writer.flush();  
    }  
    
    //����������һ��send
    public void sendMessagetwo(String message) {  
        writer.println(frame.getTitle() + "to" + message);  
        writer.flush();  
    }  
  
    //�ͻ��������ر�����  
    @SuppressWarnings("deprecation")  
    public synchronized boolean closeConnection() {  
        try {  
            sendMessage("CLOSE");// ���ͶϿ����������������  
            messageThread.stop();// ֹͣ������Ϣ�߳�  
            listModel.removeAllElements();
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }  
            isConnected = false;  
            return true;  
        } catch (IOException e1) {  
            e1.printStackTrace();  
            isConnected = true;  
            return false;  
        }  
    }  
  
    // ������Ϣ���߳�  
    class MessageThread extends Thread {  
        private BufferedReader reader;  
        private JTextArea textArea;  
  
        public MessageThread(BufferedReader reader, JTextArea textArea) {  
            this.reader = reader;  
            this.textArea = textArea;  
        }  
  
        // �����Ĺر�����  
        public synchronized void closeCon() throws Exception {  
            listModel.removeAllElements();  
            if (reader != null) {  
                reader.close();  
            }  
            if (writer != null) {  
                writer.close();  
            }  
            if (socket != null) {  
                socket.close();  
            }  
            isConnected = false; 
        }  
  
        public void run() {  
            String message = "";  
            while (true) {  
                try {  
                    message = reader.readLine();  
                    StringTokenizer stringTokenizer = new StringTokenizer(  
                            message, "/@");  
                    String command = stringTokenizer.nextToken();// ����  
                    if (command.equals("CLOSE"))// �������ѹر�����  
                    {  
                        textArea.append("�������ѹر�!\r\n");  
                        closeCon();  
                        return;// �����߳�  
                    } else if (command.equals("ADD")) {// ���û����߸��������б�  
                        String username = "";  
                        String userIp = "";  
                        if ((username = stringTokenizer.nextToken()) != null  
                                && (userIp = stringTokenizer.nextToken()) != null) {  
                            User user = new User(username, userIp);  
                            onLineUsers.put(username, user);  
                            listModel.addElement(username);  
                        }  
                    } else if (command.equals("DELETE")) {// ���û����߸��������б�  
                        String username = stringTokenizer.nextToken();  
                        User user = (User) onLineUsers.get(username);  
                        onLineUsers.remove(user);  
                        listModel.removeElement(username);  
                    } else if (command.equals("USERLIST")) {// ���������û��б�  
                        int size = Integer  
                                .parseInt(stringTokenizer.nextToken());  
                        String username = null;  
                        String userIp = null;  
                        for (int i = 0; i < size; i++) {  
                            username = stringTokenizer.nextToken();  
                            userIp = stringTokenizer.nextToken();  
                            User user = new User(username, userIp);  
                            onLineUsers.put(username, user);  
                            listModel.addElement(username);  
                        }  
                    } else if (command.equals("MAX")) {// �����Ѵ�����  
                        textArea.append(stringTokenizer.nextToken()  
                                + stringTokenizer.nextToken() + "\r\n");  
                        closeCon();// �����Ĺر�����  
                        JOptionPane.showMessageDialog(frame, "������������", "����",  
                                JOptionPane.ERROR_MESSAGE);  
                        return;// �����߳�  
                    } else {// ��ͨ��Ϣ  
                        textArea.append(message + "\r\n");  
                    }  
                } catch (IOException e) {  
                    e.printStackTrace();  
                } catch (Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
    
      
   public static void main(String[] args) {  
        new Client();  
    } 
    
}  