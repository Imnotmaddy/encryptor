package alifanov.m.crypto.aes128.instances;

import alifanov.m.crypto.aes128.AES128.AES128Encryptor;
import alifanov.m.crypto.aes128.fileManagers.FileReader;
import alifanov.m.crypto.aes128.fileManagers.FileWriter;

import java.io.IOException;

public class EncryptionInstance extends AESInstance {

    public EncryptionInstance(String inputFilePath, String outputFilePath, int serialNumber, int[][] key) {
        this.inputFilePath = inputFilePath;
        this.outputFilePath = outputFilePath;
        this.serialNumber = serialNumber;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                this.key[j][i] = key[j][i];
            }
        }
    }

    @Override
    final public void startThread() {
        try {
            FileReader fileReader = new FileReader(inputFilePath);
            block = fileReader.readBlock(serialNumber * blockSize);
            arrayToMatrix();
            AES128Encryptor aes128Encryptor = new AES128Encryptor(data, key);
            aes128Encryptor.encryption();
            FileWriter fileWriter = new FileWriter(outputFilePath);
            fileWriter.writeBlock(serialNumber * blockSize, data);
        } catch (IOException e) {
            System.out.println("File error");
        }
    }

    public int[][] getKey() {
        return this.key;
    }
}
