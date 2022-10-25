import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Autosort2022 {

	static String year(File f) {
		return (new SimpleDateFormat("yyyy")).format(new Date(f.lastModified()));
	}

	static String parent(File f) {
		return f.getParentFile().getName();
	}

	static String extension(File f) {
		String s = f.getName();
		int n = s.indexOf('.');
		return n >= 0 ? s.substring(n + 1).toLowerCase() : "none";
	}

	static Map<String, String> readConfig(File f) throws Exception {
		BufferedReader r = new BufferedReader(new FileReader(f));
		Map<String, String> m = new HashMap<String, String>();
		String t;
		int n;

		while ((t = r.readLine()) != null)
			if ((n = t.indexOf('=')) >= 0)
				m.put(t.substring(0, n).trim(), t.substring(n + 1).trim());

		r.close();
		return m;
	}

	static void scan(File source, File target) {
		if (source.isDirectory())
			for (File f : source.listFiles())
				scan(f, target);

		if (source.isFile()) {
			File t = new File(String.join(File.separator, target.getAbsolutePath(), year(source), extension(source),
					parent(source), source.getName()));

			if (!t.getParentFile().exists())
				t.getParentFile().mkdirs();

			int n = 0;

			while (t.exists())
				t = new File(String.join(File.separator, target.getAbsolutePath(), year(source), extension(source),
						String.format("z%03d", n++), parent(source), source.getName()));

			System.out.println(source.getAbsolutePath() + " -> " + t.getAbsolutePath());
			source.renameTo(t);
		}
	}

	public static void main(String[] args) throws Exception {
		Map<String, String> m = readConfig(new File("config.txt"));
		System.out.println(m.toString());
		scan(new File(m.get("source")), new File(m.get("target")));
	}
}
