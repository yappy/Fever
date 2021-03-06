package gamelib.util;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;

/**
 * {@link InputStream} implementation by {@link RandomAccessFile}.<br>
 * This class supports mark.
 * @author yappy
 */
public class FileInputStreamEx extends InputStream {

	private RandomAccessFile in;
	private long offset, size;
	private long markPos = -1;

	public FileInputStreamEx(File file) throws IOException {
		this(file, 0, file.length());
	}

	public FileInputStreamEx(File file, long offset, long size) throws IOException {
		in = new RandomAccessFile(file, "r");
		this.offset = offset;
		this.size = size;
		in.seek(offset);
	}

	@Override
	public int available() throws IOException {
		return (int)(offset + size - in.getFilePointer());
	}

	@Override
	public void close() throws IOException {
		in.close();
	}

	@Override
	public synchronized void mark(int readlimit) {
		try{
			markPos = in.getFilePointer();
		}
		catch(IOException e){
			throw new IOError(e);
		}
	}

	@Override
	public synchronized void reset() throws IOException {
		if(markPos == -1)
			throw new IOException("mark is not set");
		in.seek(markPos);
	}

	@Override
	public int read() throws IOException {
		if(in.getFilePointer() >= offset + size)
			return -1;
		else
			return in.read();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		len = (int)Math.min(len, offset + size - in.getFilePointer());
		if(len == 0)
			return -1;
		return in.read(b, off, len);
	}

	@Override
	public boolean markSupported() {
		return true;
	}

}
