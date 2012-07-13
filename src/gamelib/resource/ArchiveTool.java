package gamelib.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import ec.util.MersenneTwisterFast;
import gamelib.util.Version;

public final class ArchiveTool {

	private ArchiveTool() {
	}

	/**
	 * Archive tool.
	 * 
	 * @param args
	 *            Key(long) DestFile Directory
	 */
	public static void main(String[] args) throws Exception {
		long key = Long.parseLong(args[0]);
		MersenneTwisterFast rand = new MersenneTwisterFast(key);
		try (RandomAccessFile out = new RandomAccessFile(args[1], "rw")) {
			out.writeInt(Version.getVersion() ^ rand.nextInt());

			File root = new File(args[2]);
			SortedMap<String, File> list = new TreeMap<String, File>();
			listAllFiles(list, "", root);
			out.writeInt(list.size() ^ rand.nextInt());

			int sizesum = 0;
			final int dataOffset = 8 + 128 * list.size();
			for (Map.Entry<String, File> entry : list.entrySet()) {
				String name = entry.getKey();
				File file = entry.getValue();
				char[] strName = Arrays.copyOf(name.toCharArray(), 60);
				if (strName.length == name.length()) {
					throw new Exception("Name too long: " + name);
				}
				for (char c : strName)
					out.writeChar(c ^ rand.nextInt());
				out.writeInt((dataOffset + sizesum) ^ rand.nextInt());
				out.writeInt((int) file.length() ^ rand.nextInt());

				System.out.println("Store: " + file.getName());
				long prevPos = out.getFilePointer();
				out.seek(dataOffset + sizesum);
				FileInputStream in = new FileInputStream(file);
				int len;
				byte[] buf = new byte[1024];
				while ((len = in.read(buf)) != -1) {
					out.write(buf, 0, len);
				}
				in.close();
				out.seek(prevPos);

				sizesum += (int) file.length();
			}
		}
	}

	private static void listAllFiles(SortedMap<String, File> list, String name,
			File dir) {
		for (File child : dir.listFiles()) {
			if (child.isHidden())
				continue;
			else if (child.isDirectory()) {
				listAllFiles(list, name + child.getName() + "/", child);
			} else if (child.isFile()) {
				list.put(name + child.getName(), child);
			} else {
				System.err.println("?: " + child.getName());
			}
		}
	}

}
