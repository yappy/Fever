package gamelib.resource;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;


import ec.util.MersenneTwisterFast;
import gamelib.util.FileInputStreamEx;
import gamelib.util.Version;

/**
 * (+0)int Version<br>
 * (+4)int FileCount<br>
 * (+8)(char FileName[60], int Pos, int Size)[FileCount] (+128*FileCount+8)Data
 * @author yappy
 */
public class ArchiveResourceSystem implements ResourceSystem {

	private MersenneTwisterFast rand;
	private File archiveFile;
	private DataInputStream in;

	private String[] nameList;
	private int[] posList;
	private int[] sizeList;

	public ArchiveResourceSystem(File archiveFile, long key) throws IOException {
		rand = new MersenneTwisterFast(key);
		this.archiveFile = archiveFile;
		in = new DataInputStream(new FileInputStream(archiveFile));

		int version = in.readInt();
		version ^= rand.nextInt();
		if(version != Version.getVersion())
			throw new IOException("Invalid archive file: " + archiveFile);

		int fileCount = in.readInt() ^ rand.nextInt();
		nameList = new String[fileCount];
		posList = new int[fileCount];
		sizeList = new int[fileCount];
		for(int i = 0; i < fileCount; i++){
			StringBuilder name = new StringBuilder();
			for(int k = 0; k < 60; k++){
				char c = (char)(in.readChar() ^ rand.nextInt());
				if(c != 0)
					name.append((char)c);
			}
			nameList[i] = name.toString();
			posList[i] = in.readInt() ^ rand.nextInt();
			sizeList[i] = in.readInt() ^ rand.nextInt();
		}
	}

	@Override
	public InputStream getInputStream(String name) throws IOException {
		int ind = Arrays.binarySearch(nameList, name);
		if(ind == -1)
			throw new FileNotFoundException(name);
		return new FileInputStreamEx(archiveFile, posList[ind], sizeList[ind]);
	}

}
