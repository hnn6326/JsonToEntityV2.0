package jsontoentity.handlers;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class CreateEntityFrame extends JFrame implements ActionListener {
	private JButton buttonCreate;
	private JTextField classNameView,jsonView;
	private JTextArea consoleView;
	private ScrollPane scrollPane;
	public CreateEntityFrame(String title)
	{
		super(title);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(400,500);
		setLocation(getWindowScreenSize().width / 2 - getWidth()/2, getWindowScreenSize().height / 2 - getHeight()/2);
		setLayout(new BorderLayout(5,5)); 
		initViews();
	}
	private void initViews() {
		
		//北边 
		
		JPanel northPanel=new JPanel(new BorderLayout());
		JPanel classNamePanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		JPanel jsonDataPanel=new JPanel(new FlowLayout(FlowLayout.CENTER));
		
		classNameView=new JTextField(10);
		classNamePanel.add(new JLabel("类名："));
		classNamePanel.add(classNameView);
		
		jsonView=new JTextField(10);
		jsonDataPanel.add(new JLabel("json："));
		jsonDataPanel.add(jsonView);
		
		northPanel.add(classNamePanel,BorderLayout.NORTH);
		northPanel.add(jsonDataPanel,BorderLayout.CENTER);

		
		
		buttonCreate=new JButton("生成");
		buttonCreate.addActionListener(this);
		
		
		scrollPane=new ScrollPane();
		consoleView=new JTextArea();
		scrollPane.add(consoleView);
		
		getContentPane().add(BorderLayout.NORTH, northPanel);
		getContentPane().add(BorderLayout.CENTER,scrollPane);
		getContentPane().add(BorderLayout.SOUTH,buttonCreate);
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String className=classNameView.getText();
		String jsonStr=jsonView.getText();
//		String data=AutoEntityFile.createFileFromJson(jsonStr, className,false);
		String data=AutoEntityFile.getEntityContent(jsonStr, className);
		setSystemClipboard(data);
		consoleView.setText(data);
	}
	
	private static Dimension getWindowScreenSize() {
		return Toolkit.getDefaultToolkit().getScreenSize();
	}
	public static void setSystemClipboard(String refContent){   
		//设置为static是为了直接使用，不用new一个该类的实例即可直接使用,即定义的: 类名.方法名  
		    String vc = refContent.trim();  
		    StringSelection ss = new StringSelection(vc);  
		      
		    Clipboard sysClb=null;  
		    sysClb = Toolkit.getDefaultToolkit().getSystemClipboard();  
		    sysClb.setContents(ss,null);  
		       
		}
}
