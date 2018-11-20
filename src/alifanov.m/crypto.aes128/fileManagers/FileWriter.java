package alifanov.m.crypto.aes128.fileManagers;

import java.io.IOException;
import java.io.RandomAccessFile;

public class FileWriter {
    private String filePath;
    private int[] block = new int[blockSize];
    private static int matrixSize = 4;
    private static int blockSize = 16;

    public FileWriter(String filePath) {
        this.filePath = filePath;
    }

    public void writeBlock(int offset, int[][] data) throws IOException {
        RandomAccessFile file = new RandomAccessFile(filePath, "rw");
        matrixToArray(data);
        file.seek(file.getFilePointer() + offset);
        for (int i = 0; i < blockSize; i++) {
            file.write(block[i]);
        }
        file.close();
    }

    private void matrixToArray(int[][] data) {
        int k = 0;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++, k++) {
                block[k] = data[j][i];
            }
        }
    }


}
