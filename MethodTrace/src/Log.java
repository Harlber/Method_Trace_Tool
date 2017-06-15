/**
 * 打印Log信息，格式：
 * [类名 | 行号 | 方法名] =======:  信息
 * 例如：
 * [Utils.java | 118 | fillClassInfo]   ======:  classInfo:  Launcher.java
 * <p>
 * Created by yuchuan on 15/06/2017.
 */
public class Log {

    private final static boolean sEnablePrint = true;

    public static String getTag() {
        if (!sEnablePrint) {
            return "";
        }
        // 获取当前方法名
//        StackTraceElement traceElement = ((new Exception()).getStackTrace())[0];
        // 获取调用者方法名
        StackTraceElement traceElement = ((new Exception()).getStackTrace())[1];
        return "[" + traceElement.getFileName() + " | "
                + traceElement.getLineNumber() + " | "
                + traceElement.getMethodName() + "]";

    }

    public static void e(String tag, String log) {
        if (sEnablePrint) {
            System.out.println(tag + "   ======:  " + log);
        }
    }


}
