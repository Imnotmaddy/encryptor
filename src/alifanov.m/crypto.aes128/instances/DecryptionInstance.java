package alifanov.m.crypto.aes128.instances;

import alifanov.m.crypto.aes128.AES128.AES128Decryptor;
import alifanov.m.crypto.aes128.fileManagers.FileReader;
import alifanov.m.crypto.aes128.fileManagers.FileWriter;

import java.io.IOException;

public class DecryptionInstance extends AESInstance {


    public DecryptionInstance(String inputFilePath, String outputFilePath, int serialNumber, int[][] key) {
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
    final void startThread() {
        try {
            FileReader fileReader = new FileReader(inputFilePath);
            block = fileReader.readBlock(serialNumber * blockSize);
            arrayToMatrix();
            AES128Decryptor aes128Decryptor = new AES128Decryptor(data, key);
            aes128Decryptor.decryption();
            FileWriter fileWriter = new FileWriter(outputFilePath);
            fileWriter.writeBlock(serialNumber * blockSize, data);
        } catch (IOException e) {
            System.out.println("File error");
        }
    }


}
