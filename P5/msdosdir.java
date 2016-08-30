import java.io.File;
import java.net.URL;

public class msdosdir {
	public static void main(String[] args) {
		BootStrapSector boot;

		if (args.length < 1) {
			System.out.println("No file specified.");
		}

		//String fullPath = System.getProperty("user.dir") + "/src/" + args[0]; -- IGNORE THIS, This is only cuz I'm developing in eclipse
		File f = new File(args[0]);

		boot = new BootStrapSector(f);

		byte[] sn = boot.getVolumeSerialNumber();
		int rootDirAddress = boot.getNumBytesInReservedSectors() + (boot.getNumBytesInFat() * boot.getNumCopiesFat());


		int numEntries = 224;/*boot.getNumEntriesInRootDir();*/
		DirEntry[] entries = new DirEntry[numEntries];
		byte[] volumeLabel = null;
		byte[] volumeLabelExt = null;

		for (int i = 0; i < numEntries; i++) {
			entries[i] = new DirEntry(f, rootDirAddress + (i * 32));

			if (entries[i].isVolumeLabel()) {
				volumeLabel = entries[i].getName();
				volumeLabelExt = entries[i].getExtension();
			}
		}

		System.out.println(String.format("Volume name is %s%s", new String(volumeLabel), new String(volumeLabelExt)));
		System.out.println(String.format("Volume Serial Number is %X%X-%X%X\n", sn[3], sn[2], sn[1], sn[0]));

		int totalFiles = 0;
		int totalSize = 0;
		for (int i = 0; i < numEntries; i++) {
			if (!entries[i].isDeleted() && entries[i].getName()[0] != 0x00 && !entries[i].isVolumeLabel()) {
				System.out.println(String.format("%s %s  %7d  %02d-%02d-%d  %02d:%02d:%02d",
						new String(entries[i].getName()),
						new String(entries[i].getExtension()),
						entries[i].getFileSize(),
						entries[i].getMonth(),
						entries[i].getDay(),
						entries[i].getYear(),
						entries[i].getHour(),
						entries[i].getMinute(),
						entries[i].getSecond()
				));
				totalFiles++;
				totalSize += entries[i].getFileSize();
			}
		}

		System.out.println(String.format("\n %3d file(s)  %7d bytes\n", totalFiles, totalSize));
	}
}
