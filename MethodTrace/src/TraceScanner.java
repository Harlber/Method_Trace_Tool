import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class TraceScanner {
	private File file;
	private String packageName = "";
	private AnalysisListener listener;

	public TraceScanner(File file) {
		super();
		this.file = file;
		System.out.println("file£∫" + file);
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public void setListener(AnalysisListener listener) {
		this.listener = listener;
	}

	public HashMap<String, InfoBean> convertFile(String path) {
		ArrayList<String> index = new ArrayList<String>();
		HashMap<String, InfoBean> result = new HashMap<String, InfoBean>();
		String osName = System.getProperty("os.name");

		System.out.println("packageName£∫" + packageName);
		index.add("PackageName: " + packageName + "\n");
		index.add("start dmtracedump\n");
		String oName = packageName.replaceAll("[.]", "/");
		if (listener != null) {
			listener.startAnalysis();
		}
		if (!file.exists()) {
			System.out.println("File does not exist");
		} else {

			Runtime runtime = Runtime.getRuntime();
			BufferedReader br = null;
			try {
				Process p;
				if (osName.contains("Windows") || osName.contains("windows")) {
					p = Runtime.getRuntime().exec(Utils.getWinCommand(path));

				} else {
					// mac or linux
					p = Runtime.getRuntime().exec(Utils.getMacCommand(path));
				}

				br = new BufferedReader(new InputStreamReader(
						p.getInputStream()));
				String lineTxt = null;
				while ((lineTxt = br.readLine()) != null) {
					if (lineTxt.contains(packageName)
							|| lineTxt.contains(oName)) {
						System.out.println(lineTxt);
						result = Utils.convert2Tab(result, lineTxt);
						index.add(lineTxt + "\n");
					}
				}
			} catch (Exception e) {
				result = Utils.convert2Tab(result,
						"unknown ent 0 exec dmtracedump exception");
				index.add("÷¥––dmtracedump√¸¡Ó“Ï≥£\n");
				e.printStackTrace();
			} finally {

				if (br != null) {
					try {
						br.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				index.add("end dmtracedump\n");
				System.out.println("cmd end");
				if (listener != null) {
					listener.afterAnalysis();
				}
			}
		}
		return result;

	}

	public interface AnalysisListener {
		void startAnalysis();

		void afterAnalysis();
	}

}
