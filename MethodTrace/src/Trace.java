import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

public class Trace {

    static JLabel JlPath;
    private static final String PATH = "........................................................";

    public static void main(String[] args) {
        String packageName = null;

        int windowWidth = 1280;
        int windowHeight = 600;
        if (Utils.isMacOS()) {
            windowWidth = 1080;
            windowHeight = 720;
        }

        // 窗口标题名
        JFrame window = new JFrame("App method trace analysis");
        window.setSize(windowWidth, windowHeight);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);// 窗体大小不可变

        JMenuBar menuBar = new JMenuBar();

        // 设置菜单--File
        JMenu menuFile = new JMenu("File");
        menuBar.add(menuFile);
        JMenuItem itemOpen = new JMenuItem("Open");
        menuFile.add(itemOpen);

        // 设置菜单--About
        JMenu menuAbout = new JMenu("About");
        JMenuItem itemAbout = new JMenuItem("About Me");
        menuAbout.add(itemAbout);
        menuBar.add(menuAbout);

        // 添加菜单
        window.setJMenuBar(menuBar);

        JPanel panel = new JPanel();
        JPanel root = new JPanel();
        window.setContentPane(root);
        root.setLayout(new BoxLayout(root, BoxLayout.Y_AXIS));
        panel.setLayout(new VerticalFlowLayout(VerticalFlowLayout.TOP, 5, 5,
                true, false));

        JPanel path = new JPanel();
        path.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        path.add(new JLabel("File Path:"));
        JlPath = new JLabel(PATH);
        path.add(JlPath);
        panel.add(path);

        drag();//启用拖拽

        JPanel top = new JPanel();
        top.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        top.add(new JLabel("Filter Package Name:"));
        JTextField jpName = new JTextField("package name", 30);// com.jushi.trading
        jpName.setBounds(10, 10, 100, 20);
        // 设置文本的水平对齐方式
        jpName.setHorizontalAlignment(JTextField.CENTER);
        top.add(jpName, BorderLayout.PAGE_START);

        // 分析按钮
        JButton jGo = new JButton("analysis");
        top.add(jGo);
        panel.add(top);

        JPanel jpStatus = new JPanel();
        jpStatus.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        JLabel jlStatus = new JLabel("Info:");
        jpStatus.add(jlStatus);
        panel.add(jpStatus);
        jpStatus.setVisible(false);

        root.add(panel, BorderLayout.PAGE_START);

        // 内容显示列表
        JTable table = new JTable(new MethodTabModel());
        // 可滑动面板
        JScrollPane scrollPane = new JScrollPane(table);
        root.add(scrollPane, BorderLayout.CENTER);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        // 点击监听
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
                if (e.getClickCount() == 2) {//
                    Log.e(Log.getTag(), "双击");
                }

            }
        });

        // 分析按钮点击监听
        jGo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Log.e(Log.getTag(), JlPath.getText());
                if (JlPath.getText().equals(PATH)) {
                    JOptionPane.showMessageDialog(root, "请导入trace文件", "提示消息",
                            JOptionPane.WARNING_MESSAGE);
                } else if (!JlPath.getText().endsWith("trace")) {
                    JOptionPane.showMessageDialog(root, "请导入有效的trace文件",
                            "提示消息", JOptionPane.WARNING_MESSAGE);
                } else if (jpName.getText().equals("package name") || jpName.getText().equals("")) {
                    JOptionPane.showMessageDialog(root, "请输入需过滤的包名", "提示消息",
                            JOptionPane.WARNING_MESSAGE);
                } else {
                    TraceScanner scanner = new TraceScanner(new File(JlPath.getText()));
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
                Object[] options = {"Go", "No！"};
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

        // File菜单中Open选项点击事件
        itemOpen.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser jfc = new JFileChooser();
                jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                jfc.showDialog(new JLabel(), "选择");
                File file = jfc.getSelectedFile();
                if (file.isDirectory()) {
                    Log.e(Log.getTag(), "文件夹:" + file.getAbsolutePath());
                } else if (file.isFile()) {
                    Log.e(Log.getTag(), "文件:" + file.getAbsolutePath());
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
        new DropTarget(JlPath, DnDConstants.ACTION_COPY_OR_MOVE, new DropTargetAdapter() {
            public void drop(DropTargetDropEvent dtde)//重写适配器的drop方法
            {
                try {
                    if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor))//如果拖入的文件格式受支持
                    {
                        dtde.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);//接收拖拽来的数据
                        List<File> list = (List<File>) (dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor));
                        String temp = "";
                        for (File file : list) {
                            temp = file.getAbsolutePath();
                            JlPath.setText(temp);
                            break;
                        }
                        //JOptionPane.showMessageDialog(null, temp);
                        dtde.dropComplete(true);//指示拖拽操作已完成
                    } else {
                        dtde.rejectDrop();//否则拒绝拖拽来的数据
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
