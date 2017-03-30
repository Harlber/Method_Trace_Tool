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

	public static String getPackageName(String str) {
		if (null == str || str.equals("")) {
			return "";
		}
		String result = "";
		if (System.getProperty("os.name").contains("Windows")
				|| System.getProperty("os.name").contains("windows")) {
			int start = str.lastIndexOf("\\");
			int end = str.indexOf("_");
			result = str.substring(start + 1, end);
			return result;
		} else {
			int start = str.lastIndexOf("//");
			int end = str.indexOf("_");
			result = str.substring(start + 1, end);
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
			keyInfo.put(key.toString(), bean);
		}
		return keyInfo;

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

}
