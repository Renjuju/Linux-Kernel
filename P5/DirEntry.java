import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

public class DirEntry {
	File imageStream;
	private int startAddress;
	boolean deleted;

	private byte[] name = new byte[9];
	private byte[] extension = new byte[4];
	private byte[] attributes = new byte[1];
	private byte[] reserved = new byte[10];
	private byte[] time = new byte[2];
	private byte[] date = new byte[2];
	private byte[] startingCluster = new byte[2];
	private byte[] fileSize = new byte[4];

	public DirEntry(File image, int start) {
		imageStream = image;
		startAddress = start;
		try {
			readDirEntry();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void readDirEntry() throws IOException {
		try {
			RandomAccessFile raf = new RandomAccessFile(imageStream, "r");
			raf.seek(startAddress);
			raf.readFully(name, 0, 8);
			raf.readFully(extension, 0, 3);
			raf.readFully(attributes, 0, 1);
			raf.readFully(reserved, 0, 10);
			raf.readFully(time, 0, 2);
			raf.readFully(date, 0, 2);
			raf.readFully(startingCluster, 0, 2);
			raf.readFully(fileSize, 0, 4);

			name[8] = '\0';
			extension[3] = '\0';

			deleted = ((byte)name[0] == (byte)0xE5);
			if ((byte)name[0] == (byte)0x05) {
				name[0] = (byte) 0xE5;
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean isVolumeLabel() {
		return (attributes[0] & 0x08) == 0x08;
	}

	public byte[] getName() {
		return name;
	}

	public byte[] getExtension() {
		return extension;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public int getStartingCluster(){
		return (startingCluster[1] << 8) + startingCluster[0];
	}

	public int getFileSize() {
		ByteBuffer wrapped = ByteBuffer.wrap(fileSize);
		int num = wrapped.order(java.nio.ByteOrder.LITTLE_ENDIAN).getInt();
		return num;
	}

	public int getMonth() {
		return ((date[1] & 0x01) << 3) + ((date[0] & 0xE0) >> 5);
	}

	public int getDay() {
		return date[0] & 0x1F;
	}

	public int getYear() {
		return 1980 + (date[1] >> 1);
	}

	public int getHour() {
		return time[1] >> 3 & 0x1F;
	}

	public int getMinute() {
		return ((time[1] & 0x07) << 3) + ((time[0] & 0xE0) >> 5);
	}

	public int getSecond() {
		return time[0] & 0x1F;
	}

}
