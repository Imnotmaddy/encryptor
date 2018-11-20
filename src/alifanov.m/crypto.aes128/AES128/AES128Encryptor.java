package alifanov.m.crypto.aes128.AES128;


public class AES128Encryptor extends AES128Container {

    public AES128Encryptor(int[][] block, int[][] key) {
        this.dataBlock = block;
        this.keyBlock = key;
    }

    public void encryption() {
        addRoundKey();
        for (int i = 0; i < Nr - 1; i++) {
            keyExpansion(i);
            encryptBlock();
        }
        subBytes();
        shiftRows();
        keyExpansion(Nr - 1);
        addRoundKey();
    }

    private void encryptBlock() {
        subBytes();
        shiftRows();
        mixColumns();
        addRoundKey();
    }

    private void subBytes() {
        for (int i = 0; i < Nb; i++)
            subWord(dataBlock[i]);
    }

    private void shiftRows() {
        for (int i = 1; i < Nb; i++)
            shiftRow(dataBlock[i], i);
    }

    private void shiftRow(int[] row, int offset) {
        for (int i = 0; i < offset; i++)
            rotateWordLeft(row);
    }

    private void mixColumns() {
        for (int i = 0; i < Nb; i++) {
            int temp_col[] = new int[Nb];
            for (int j = 0; j < Nb; j++)
                temp_col[j] = dataBlock[j][i];

            mixColumn(temp_col);

            for (int k = 0; k < Nb; k++)
                dataBlock[k][i] = temp_col[k];
        }
    }

    private void mixColumn(int[] column) {
        int temp_column[] = new int[Nb];
        for (int i = 0; i < Nb; i++)
            temp_column[i] = column[i];

        column[0] = galois_multx(temp_column[0]) ^ galois_multx(temp_column[1]) ^ temp_column[1] ^ temp_column[2] ^ temp_column[3];
        column[1] = temp_column[0] ^ galois_multx(temp_column[1]) ^ galois_multx(temp_column[2]) ^ temp_column[2] ^ temp_column[3];
        column[2] = temp_column[0] ^ temp_column[1] ^ galois_multx(temp_column[2]) ^ galois_multx(temp_column[3]) ^ temp_column[3];
        column[3] = galois_multx(temp_column[0]) ^ temp_column[0] ^ temp_column[1] ^ temp_column[2] ^ galois_multx(temp_column[3]);
    }

    private void keyExpansion(int roundCount) {
        int temp_row[] = new int[Nb];

        for (int i = 0; i < Nb; i++)
            temp_row[i] = keyBlock[i][Nb - 1];

        rotateWordLeft(temp_row);
        subWord(temp_row);

        for (int i = 0; i < Nb; i++)
            temp_row[i] = temp_row[i] ^ keyBlock[i][0] ^ RCON[i][roundCount];

        for (int i = 0; i < 4; i++)
            keyBlock[i][0] = temp_row[i];

        for (int i = 1; i < Nb; i++)
            for (int j = 0; j < Nb; j++)
                keyBlock[j][i] = keyBlock[j][i] ^ keyBlock[j][i - 1];
    }

}
