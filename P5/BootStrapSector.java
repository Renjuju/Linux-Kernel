import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class BootStrapSector {
	private File imageStream;
	
	private byte[] firstInstruction = new byte[3];
	private byte[] OEM = new byte[8];
	private byte[] numBytesPerSector = new byte[2];
	private byte[] numSectorsPerCluster = new byte[1];
	private byte[] numReservedSectors = new byte[2];
	private byte[] numCopiesFat = new byte[1];
	private byte[] numEntriesRootDir = new byte[2];
	private byte[] numSectors = new byte[2];
	private byte[] mediaDescriptor = new byte[1];
	private byte[] numSectorsInFat = new byte[2];
	private byte[] numSectorsPerTrack = new byte[2];
	private byte[] numSides = new byte[2];
	private byte[] numHiddenSectors = new byte[2];
	private byte[] formatType = new byte[9];
	private byte[] hex55AA = new byte[2];
	private byte[] volumeLabel = new byte[12];
	private byte[] volumeSN = new byte[5];
	
	public BootStrapSector(File image) {
		imageStream = image;
		try {
			readBootStrapSector();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readBootStrapSector() throws IOException {
		try {
			RandomAccessFile raf = new RandomAccessFile(imageStream, "r");
			raf.read(firstInstruction);
			raf.read(OEM);
			raf.read(numBytesPerSector);
			raf.read(numSectorsPerCluster);
			raf.read(numReservedSectors);
			raf.read(numCopiesFat);
			raf.read(numEntriesRootDir);
			raf.read(numSectors);
			raf.read(mediaDescriptor);
			raf.read(numSectorsInFat);
			raf.read(numSectorsPerTrack);
			raf.read(numSides);
			raf.read(numHiddenSectors);

			raf.seek(0x36);
			raf.read(formatType);
			formatType[8] = '\0';
			
			raf.seek(0x2B);
			raf.read(volumeLabel);
			volumeLabel[11] = '\0';
			
			raf.seek(0x27);
			raf.read(volumeSN);
			volumeSN[4] = '\0';
			
			raf.seek(0x1FE);
			raf.read(hex55AA);
			

			raf.close();
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public byte[] getVolumeSerialNumber() {
		return volumeSN;
	}
	
	public int getNumBytesInReservedSectors() {
		int reservedSectors = (numReservedSectors[1] << 8) + numReservedSectors[0];
		int sectorBytes = (numBytesPerSector[1] << 8) + numBytesPerSector[0];
		
		return reservedSectors * sectorBytes;
	}
	
	public int getNumBytesInFat() {
		int fatSectors = (numSectorsInFat[1] << 8) + numSectorsInFat[0];
		int sectorBytes = (numBytesPerSector[1] << 8) + numBytesPerSector[0];
		
		return fatSectors * sectorBytes;
	}
	
	public int getNumCopiesFat() {
		return numCopiesFat[0];
	}
	
	public int getNumEntriesInRootDir() {
		return (numEntriesRootDir[1] << 8) + numEntriesRootDir[0];
	}
	
	public int getNumBytesPerCluster() {
		int sectorBytes = (numBytesPerSector[1] << 8) + numBytesPerSector[0];
		return numSectorsPerCluster[0] * sectorBytes;
	}
	
}
