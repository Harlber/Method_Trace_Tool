import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.LayoutManager;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractButton;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ScrollPaneLayout;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.table.TableColumn;

public class Trace {

	static JLabel JlPath;
	public static void main(String[] args) {
		String packageName = null;

		JFrame window = new JFrame("App method trace analysis");
		window.setSize(1280, 600);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);// 窗体大小不可变

		JMenuBar menubar = new JMenuBar();

		JMenu menuFile = new JMenu("File");
		menubar.add(menuFile);
		JMenuItem itemOpen = new JMenuItem("Open");
		menuFile.add(itemOpen);

		JMenu menuAbout = new JMenu("About");
		JMenuItem itemAbout = new JMenuItem("About Me");
		menuAbout.add(itemAbout);
		menubar.add(menuAbout);

		window.setJMenuBar(menubar);

		JPanel panel = new JPanel();
		JPanel root = new JPanel();
		window.setContentPane(root);
		root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
		panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5,
				true, false));

		JPanel path = new JPanel();
		path.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT, 10, 5));
		path.add(new JLabel("File Path:"));
		JlPath = new JLabel("........................................................................");
		path.add(JlPath);
		panel.add(path);
		drag();//启用拖拽

		JPanel top = new JPanel();
		top.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT, 10, 5));
		top.add(new JLabel("Filter Package Name:"));
		JTextField jpName = new JTextField("package name", 30);// com.jushi.trading
		jpName.setBounds(10, 10, 100, 20);
		// 设置文本的水平对齐方式
		jpName.setHorizontalAlignment(JTextField.CENTER);
		top.add(jpName, BorderLayout.PAGE_START);
		JButton jGo = new JButton("analysis");
		top.add(jGo);
		panel.add(top);

		JPanel jpStatus = new JPanel();
		jpStatus.setLayout((LayoutManager) new FlowLayout(FlowLayout.LEFT, 10,
				5));
		JLabel jlStatus = new JLabel("Info:");
		jpStatus.add(jlStatus);
		panel.add(jpStatus);
		jpStatus.setVisible(false);

		root.add(panel, BorderLayout.PAGE_START);

		JTable table = new JTable(new MethodTabModel());
		JScrollPane scrollPane = new JScrollPane(table);
		root.add(scrollPane, BorderLayout.CENTER);
		table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() ==2){//
					System.out.println("双击");
				}

			}
		});

		jGo.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (JlPath.getText().equals("........................................................................")) {
					JOptionPane.showMessageDialog(root, "请导入trace文件", "提示消息",
							JOptionPane.WARNING_MESSAGE);
				} else if (!JlPath.getText().endsWith("trace")) {
					JOptionPane.showMessageDialog(root, "请导入有效的trace文件",
							"提示消息", JOptionPane.WARNING_MESSAGE);
				} else if (jpName.getText().equals("package name")
						|| jpName.getText().equals("")) {
					JOptionPane.showMessageDialog(root, "请输入需过滤的包名", "提示消息",
							JOptionPane.WARNING_MESSAGE);
				} else {

					TraceScanner scanner = new TraceScanner(new File(JlPath
							.getText()));
					// 赋值
					scanner.setPackageName(jpName.getText());
					jlStatus.setText("analysis...");
					MethodTabModel model = (MethodTabModel) table.getModel();
					ArrayList<InfoBean> tableArray = Utils
							.mapConvert2Array(scanner.convertFile(JlPath
									.getText()));
					ArrayList<InfoBean> xmlBean = new ArrayList<InfoBean>();
					xmlBean.addAll(tableArray);
					model.setContent(tableArray);
					Utils.fitTableColumns(table);
					for (int i = 0; i < xmlBean.size(); i++) {
						System.out.println("");
					}
				}
			}
		});

		itemAbout.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				Object[] options = { "Go", "No！" };
				int n = JOptionPane
						.showOptionDialog(null,
								"Visit me :https://github.com/Harlber",
								"About Me", JOptionPane.YES_NO_OPTION,
								JOptionPane.QUESTION_MESSAGE, null, options,
								options[0]);
				if (n == 0) {
					try {
						Utils.browse("https://github.com/Harlber");
					} catch (Exception e1) {
						e1.printStackTrace();
					}
				}
			}
		});

		itemOpen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				JFileChooser jfc = new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
				jfc.showDialog(new JLabel(), "选择");
				File file = jfc.getSelectedFile();
				if (file.isDirectory()) {
					System.out.println("文件夹:" + file.getAbsolutePath());
				} else if (file.isFile()) {
					System.out.println("文件:" + file.getAbsolutePath());
				}
				JlPath.setText(file.getAbsolutePath());
				jpName.setText(Utils.getPackageName(file.getAbsolutePath()));
			}
		});
		window.setVisible(true);
	}



	public static void drag()//定义的拖拽方法
	{
		//panel表示要接受拖拽的控件
		new DropTarget(JlPath, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter()
		{
			public void drop(DropTargetDropEvent dtde)//重写适配器的drop方法
			{
				try
				{
					if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
					{
						dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
						List<File> list =  (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
						String temp="";
						for(File file:list){
							temp=file.getAbsolutePath();
							((JLabel) JlPath).setText(temp);
							break;
						}
						//JOptionPane.showMessageDialog(null, temp);
						dtde.dropComplete(true);//指示拖拽操作已完成
					}
					else
					{
						dtde.rejectDrop();//否则拒绝拖拽来的数据
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}
