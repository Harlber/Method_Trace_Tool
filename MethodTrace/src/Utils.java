import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class Utils {

	public static String getWinCommand(String path) {
		return "dmtracedump -o " + path;
	}

	public static String[] getMacCommand(String path) {
		String dumpCmd = "dmtracedump -o " + path;
		String[] result = { "bash", "-c", dumpCmd };
		return result;
	}

	@SuppressWarnings("finally")
	public static String getPackageName(String str) {
		if (null == str || str.equals("")) {
			return "";
		}
		String result = "";
		int start = 0;
		try {
			if (System.getProperty("os.name").contains("Windows")
					|| System.getProperty("os.name").contains("windows")) {
				start = str.lastIndexOf("\\");
			} else {
				start = str.lastIndexOf("//");
			}
			int end = str.indexOf("_");
			result = str.substring(start + 1, end);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return result;
		}
	}

	public static String[] removeArrayEmptyTextBackNewArray(String[] strArray) {
		List<String> strList = Arrays.asList(strArray);
		List<String> strListNew = new ArrayList<>();
		for (int i = 0; i < strList.size(); i++) {
			if (strList.get(i) != null && !strList.get(i).equals("")) {
				strListNew.add(strList.get(i));
			}
		}
		String[] strNewArray = strListNew
				.toArray(new String[strListNew.size()]);
		return strNewArray;
	}

	public static HashMap<String, InfoBean> convert2Tab(
			HashMap<String, InfoBean> keyInfo, String index) {

		String[] indexStr = index.split(" ");// replaceAll("[.]+", "")
		String[] str = removeArrayEmptyTextBackNewArray(indexStr);
		if (str.length > 6) {
			for (int i = 0; i < str.length; i++)
				System.out.println(str[i]);
		}
		/*
		 * System.out.println("[0]:" + str[0] + "[1]:" + str[1] + "[3]:" +
		 * str[3] + "length:" + str.length);
		 */
		StringBuffer key = new StringBuffer();
		for (int i = 3; i < str.length; i++) {
			key.append(str[i]);
			key.append(" ");
		}

		if (keyInfo.containsKey(key.toString())) {//
			InfoBean bean = keyInfo.get(key.toString());
			if (str[1].equals("ent")) {
				bean.setXitTime(Integer.valueOf(str[2]));
			} else {
				bean.setXitTime(Integer.valueOf(str[2]));
			}
			fillClassInfo(index, bean);
			keyInfo.replace(key.toString(), bean);
		} else {
			InfoBean bean = new InfoBean();
			bean.setKey(key.toString());
			bean.settId(str[0]);
			bean.setType(str[1]);
			if (str[1].equals("ent")) {
				bean.setEntTime(Integer.valueOf(str[2]));
			} else {
				bean.setXitTime(Integer.valueOf(str[2]));
			}
			fillClassInfo(index, bean);
			keyInfo.put(key.toString(), bean);
		}
		return keyInfo;

	}

	/* 坑点：内部类 adapter$1.ViewHolder */
	static void fillClassInfo(String index, InfoBean info) {
		// unknown ent 0 exec dmtracedump exception
		if (index.indexOf("java") < 0 && index.contains("dmtracedump")) {
			info.setClsName("Exception");
			info.setMethodName(index);
		} else {
			String[] names = index.split("\t");
			String classInfo = names[names.length - 1];
			String className = classInfo.substring(0, classInfo.indexOf("."));
			String class_pot = className + ".";
			String class_$ = className + "$";

			String methodName = "";
			String methodStr = names[names.length - 2];

			int dexPot = methodStr.indexOf(class_pot);
			int dex$ = methodStr.indexOf(class_$);
			methodName = methodStr.substring(Math.max(dexPot, dex$),
					methodStr.length());

			info.setClsName(className);
			info.setMethodName(methodName);
			info.setFullClassName(info.getKey().substring(0,
					info.getKey().indexOf(className))
					+ className);
		}
	}

	public static ArrayList<InfoBean> mapConvert2Array(
			HashMap<String, InfoBean> map) {
		ArrayList<InfoBean> result = new ArrayList<InfoBean>();
		for (InfoBean value : map.values()) {
			result.add(value);
		}
		return result;
	}

	public static void fitTableColumns(JTable myTable) {

		JTableHeader header = myTable.getTableHeader();
		int rowCount = myTable.getRowCount();
		Enumeration columns = myTable.getColumnModel().getColumns();
		while (columns.hasMoreElements()) {
			TableColumn column = (TableColumn) columns.nextElement();
			int col = header.getColumnModel().getColumnIndex(
					column.getIdentifier());
			int width = (int) myTable
					.getTableHeader()
					.getDefaultRenderer()
					.getTableCellRendererComponent(myTable,
							column.getIdentifier(), false, false, -1, col)
					.getPreferredSize().getWidth();
			for (int row = 0; row < rowCount; row++) {
				int preferedWidth = (int) myTable
						.getCellRenderer(row, col)
						.getTableCellRendererComponent(myTable,
								myTable.getValueAt(row, col), false, false,
								row, col).getPreferredSize().getWidth();
				width = Math.max(width, preferedWidth);
			}
			header.setResizingColumn(column); // 此行很重要
			column.setWidth(width + myTable.getIntercellSpacing().width + 4);// 使表格看起来不是那么拥挤，起到间隔作用
		}
	}

	public static void browse(String url) throws Exception {
		// 获取操作系统的名字
		String osName = System.getProperty("os.name", "");
		if (osName.startsWith("Mac OS")) {
			// 苹果的打开方式
			Class fileMgr = Class.forName("com.apple.eio.FileManager");
			Method openURL = fileMgr.getDeclaredMethod("openURL",
					new Class[] { String.class });
			openURL.invoke(null, new Object[] { url });
		} else if (osName.startsWith("Windows")) {
			// windows的打开方式。
			Runtime.getRuntime().exec(
					"rundll32 url.dll,FileProtocolHandler " + url);
		} else {
			// Unix or Linux的打开方式
			String[] browsers = { "firefox", "opera", "konqueror", "epiphany",
					"mozilla", "netscape" };
			String browser = null;
			for (int count = 0; count < browsers.length && browser == null; count++)
				// 执行代码，在brower有值后跳出，
				// 这里是如果进程创建成功了，==0是表示正常结束。
				if (Runtime.getRuntime()
						.exec(new String[] { "which", browsers[count] })
						.waitFor() == 0)
					browser = browsers[count];
			if (browser == null)
				throw new Exception("Could not find web browser");
			else
				// 这个值在上面已经成功的得到了一个进程。
				Runtime.getRuntime().exec(new String[] { browser, url });
		}
	}

}
