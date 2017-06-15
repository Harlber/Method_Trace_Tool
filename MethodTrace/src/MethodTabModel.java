import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

/**
 * 内容显示面板Tab栏
 */
public class MethodTabModel extends AbstractTableModel {
    private ArrayList<InfoBean> content = new ArrayList<InfoBean>();
    private String[] columns = new String[]{"Thread", "Time(μs)", "Class", "Method"};

    public MethodTabModel() {

    }

    @Override
    public int getRowCount() {// 行数
        return content.size();
    }

    @Override
    public int getColumnCount() {// 列数
        return columns.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InfoBean bean = this.content.get(rowIndex);
        switch (columnIndex) {// 每个列显示的内容定制
            case 0:
                return bean.gettId();
            case 1:
                if (bean.getXitTime() == 0) {
                    return "unknown";
                } else {
                    return bean.getXitTime() - bean.getEntTime();
                }
            case 2:
                return bean.getFullClassName();
            case 3:
                return bean.getMethodName();

        }
        return null;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    public void setContent(List<InfoBean> content) {
        this.content.clear();
        this.content.addAll(content);
        fireTableDataChanged();
    }

}
