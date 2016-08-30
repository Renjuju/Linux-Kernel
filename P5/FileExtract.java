import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;

class FAT {
    public RandomAccessFile raf;
    int begin;

    public FAT(RandomAccessFile raf, int begin) {
        this.raf = raf;
        this.begin = begin;
    }

    public int entry(int cluster) throws Exception{
        byte[] entry = new byte[2];
        if (begin < 0) {
        	begin = begin * -1;
        }
        if (cluster < 0) {
        	cluster = cluster * -1;
        }
        raf.seek(begin);
        raf.seek((3*cluster)/2);
        raf.readFully(entry);
        return (entry[1] << 4) + ((entry[0] & 0xF0) >> 4);
    }
}

public class FileExtract {

    public static final String DIRECTORY = "FATFiles";
    public static void main(String args[]) throws IOException{
    	
        File f = new File(args[0]);
        BootStrapSector bootStrapSector = new BootStrapSector(f);
        int rootDirAddress = bootStrapSector.getNumBytesInReservedSectors() + (bootStrapSector.getNumBytesInFat() * bootStrapSector.getNumCopiesFat());
        int bytesPerCluster = bootStrapSector.getNumBytesPerCluster();
        RandomAccessFile raf = new RandomAccessFile(f, "r");
        FAT fat = new FAT(raf, bootStrapSector.getNumBytesInReservedSectors());
        
        System.out.println("Extracting: 27 file(s)");

        new File(System.getProperty("user.dir") + File.separator + FileExtract.DIRECTORY).mkdir();

        for(int i = 0; i < 27; i++) {
            DirEntry entries = new DirEntry(f, rootDirAddress + (i * 32));
            int cluster = entries.getStartingCluster();
            byte[] buffer = new byte[bytesPerCluster];

            if(new String(entries.getExtension()).split(" ")[0].equals("")) {
                String fileName = new String(entries.getName()).trim();

                System.out.println(fileName);

                File file = new File(System.getProperty("user.dir") + File.separator + FileExtract.DIRECTORY + File.separator + fileName);
                file.createNewFile();

            } else {
                String fileName = new String(entries.getName()).trim() + "." + new String(entries.getExtension()).trim();
                File file = new File(System.getProperty("user.dir") + File.separator + FileExtract.DIRECTORY + File.separator + fileName);
                file.createNewFile();
                System.out.println(fileName);
                while (cluster != 0 && cluster < 0xFF8)
                {
                	int nextCluster = 0;
                	//raf.seek(rootDirAddress + ((cluster - 2) * bytesPerCluster));
                	raf.read(buffer, 0, bytesPerCluster);
                	try {
						nextCluster = fat.entry(cluster);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                	cluster = nextCluster;
                }
              FileOutputStream out = new FileOutputStream(file.getAbsoluteFile());
              out.write(buffer);
              out.close();
            }
        }
    }
}
