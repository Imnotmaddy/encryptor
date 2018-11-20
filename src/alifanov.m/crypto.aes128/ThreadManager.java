package alifanov.m.crypto.aes128;


import alifanov.m.crypto.aes128.fileManagers.FileReader;
import alifanov.m.crypto.aes128.instances.DecryptionInstance;
import alifanov.m.crypto.aes128.instances.EncryptionInstance;

import java.io.IOException;

public class ThreadManager {
    private long fileSize;
    private static int maxThreadAmount = 4;
    private int currentNumberOfThreads;
    private long extraSymbols;
    private FileReader fileReader;
    private Thread[] threads = new Thread[maxThreadAmount];
    private EncryptionInstance instance;
    private long rounds;
    private String inputFilePath;
    private int[][] key;

    ThreadManager(String inputFilePath, int[][] key) {
        currentNumberOfThreads = 0;
        this.inputFilePath = inputFilePath;
        this.key = key;
    }

    private void calculateFileSize() throws IOException {
        fileReader = new FileReader(inputFilePath);
        fileSize = fileReader.getFileSize();
    }

    private void calculateRounds() {
        rounds = fileSize / 16;
        extraSymbols = fileSize % 16;
        if (extraSymbols > 0) rounds++;
    }

    public int[][] startEncryption(String outputFilePath) throws IOException {
        for (int i = 0; i < maxThreadAmount; i++) {
            threads[i] = new Thread(new EncryptionInstance(inputFilePath, outputFilePath, 0, key));
        }
        calculateFileSize();
        calculateRounds();
        int serialNumberCounter = 0;
        while (rounds > 0) {
            checkEncryptionThreads(outputFilePath, key, serialNumberCounter).start();
            serialNumberCounter++;
            rounds--;
        }
        return getNewKey(outputFilePath, key, serialNumberCounter);
    }

    public void startDecryption(String outputFilePath) throws IOException {
        for (int i = 0; i < maxThreadAmount; i++) {
            threads[i] = new Thread(new DecryptionInstance(inputFilePath, outputFilePath, 0, key));
        }
        calculateFileSize();
        calculateRounds();
        int serialNumberCounter = 0;
        while (rounds > 0) {
            checkDecryptionThreads(outputFilePath, key, serialNumberCounter).start();
            serialNumberCounter++;
            rounds--;
        }
    }

    private Thread checkDecryptionThreads(String outputFilePath, int[][] key, int serialNumber) {
        for (int i = 0; i < threads.length; i++) {
            if (!threads[i].isAlive()) {
                threads[i] = new Thread(new DecryptionInstance(inputFilePath, outputFilePath, serialNumber, key));
                currentNumberOfThreads++;
                return threads[i];
            }
        }
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            System.out.println("thread fail");
        }
        threads[0] = new Thread(new DecryptionInstance(inputFilePath, outputFilePath, serialNumber, key));
        currentNumberOfThreads++;
        return threads[0];
    }

    private Thread checkEncryptionThreads(String outputFilePath, int[][] key, int serialNumber) {
        for (int i = 0; i < threads.length; i++) {
            if (!threads[i].isAlive()) {
                threads[i] = new Thread(new EncryptionInstance(inputFilePath, outputFilePath, serialNumber, key));
                currentNumberOfThreads++;
                return threads[i];
            }
        }
        try {
            threads[0].join();
        } catch (InterruptedException e) {
            System.out.println("thread fail");
        }
        threads[0] = new Thread(new EncryptionInstance(inputFilePath, outputFilePath, serialNumber, key));
        currentNumberOfThreads++;
        return threads[0];
    }

    private int[][] getNewKey(String outputFilePath, int[][] key, int serialNumber) {
        this.instance = new EncryptionInstance(inputFilePath, outputFilePath, serialNumber, key);
        instance.startThread();
        return instance.getKey();
    }
}




