package alifanov.m.crypto.aes128.instances;

public abstract class AESInstance implements Runnable {
    String inputFilePath;
    String outputFilePath;

    int serialNumber;

    int[][] data = new int[matrixSize][matrixSize];
    int[][] key = new int[matrixSize][matrixSize];
    byte[] block = new byte[blockSize];
    static int blockSize = 16;
    private static int matrixSize = 4;

    public void run() {
        this.startThread();
    }

    void startThread() {
    }

    void arrayToMatrix() {
        int k = 0;
        for (int i = 0; i < matrixSize; i++) {
            for (int j = 0; j < matrixSize; j++, k++) {
                data[j][i] = block[k] & 0xff;
            }
        }
    }

}
