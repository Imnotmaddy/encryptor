package alifanov.m.crypto.aes128.fileManagers;


import java.io.IOException;
import java.io.RandomAccessFile;

public class FileReader {
    private String filePath;
    private long fileSize;
    private static int blockSize = 16;

    public FileReader(String filePath) {
        this.filePath = filePath;
    }

    public byte[] readBlock(int offset) throws IOException {
        byte[] data = new byte[blockSize];
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        long currentPointer = file.getFilePointer();
        file.seek(currentPointer + offset);
        file.read(data, 0, blockSize);
        file.close();
        return data;
    }

    public long getFileSize() throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "r");
        this.fileSize = file.length();
        return fileSize;
    }


}
