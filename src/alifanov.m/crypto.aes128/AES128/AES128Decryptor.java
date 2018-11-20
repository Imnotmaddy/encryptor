package alifanov.m.crypto.aes128.AES128;

public class AES128Decryptor extends AES128Container {

    public AES128Decryptor(int[][] block, int[][] key) {
        this.dataBlock = block;
        this.keyBlock = key;
    }

    public void decryption() {
        addRoundKey();
        for (int i = Nr - 1; i > 0; i--) {
            InvKey_expansion(i);
            DecryptBlock();
        }
        InvShiftRows();
        InvSubBytes();
        InvKey_expansion(0);
        addRoundKey();
    }

    private void DecryptBlock() {
        InvShiftRows();
        InvSubBytes();
        addRoundKey();
        InvMixColumns();
    }

    private void InvSubBytes() {
        for (int i = 0; i < Nb; i++)
            InvSubWord(dataBlock[i]);
    }

    private void InvSubWord(int[] row) {
        for (int i = 0; i < Nb; i++)
            row[i] = InvSubByte(row[i]);
    }

    private int InvSubByte(int value) {
        int x = value / 16;
        int y = value % 16;
        return INV_SBOX[x][y];
    }

    private void InvShiftRows() {
        for (int i = 1; i < Nb; i++)
            InvShiftRow(dataBlock[i], i);
    }

    private void InvShiftRow(int[] row, int n) {
        for (int i = 0; i < n; i++)
            RotWordRight(row);
    }

    private void RotWordRight(int[] row) {
        int temp = row[Nb - 1];
        for (int i = Nb - 1; i > 0; i--)
            row[i] = row[i - 1];
        row[0] = temp;
    }

    private void InvMixColumns() {
        for (int i = 0; i < Nb; i++) {
            int temp_col[] = new int[Nb];
            for (int j = 0; j < Nb; j++)
                temp_col[j] = dataBlock[j][i];

            InvMixColumn(temp_col);

            for (int k = 0; k < Nb; k++)
                dataBlock[k][i] = temp_col[k];
        }
    }

    private void InvMixColumn(int[] column) {
        int[] temp_column = new int[Nb];

        for (int i = 0; i < Nb; i++)
            temp_column[i] = column[i];
        column[0] = galois_multN(temp_column[0], 3) ^ galois_multN(temp_column[0], 2) ^ galois_multx(temp_column[0]) ^ galois_multN(temp_column[1], 3) ^ galois_multN(temp_column[1], 1) ^ temp_column[1] ^ galois_multN(temp_column[2], 3) ^ galois_multN(temp_column[2], 2) ^ temp_column[2] ^ galois_multN(temp_column[3], 3) ^ temp_column[3];
        column[1] = galois_multN(temp_column[1], 3) ^ galois_multN(temp_column[1], 2) ^ galois_multx(temp_column[1]) ^ galois_multN(temp_column[2], 3) ^ galois_multN(temp_column[2], 1) ^ temp_column[2] ^ galois_multN(temp_column[3], 3) ^ galois_multN(temp_column[3], 2) ^ temp_column[3] ^ galois_multN(temp_column[0], 3) ^ temp_column[0];
        column[2] = galois_multN(temp_column[2], 3) ^ galois_multN(temp_column[2], 2) ^ galois_multx(temp_column[2]) ^ galois_multN(temp_column[3], 3) ^ galois_multN(temp_column[3], 1) ^ temp_column[3] ^ galois_multN(temp_column[0], 3) ^ galois_multN(temp_column[0], 2) ^ temp_column[0] ^ galois_multN(temp_column[1], 3) ^ temp_column[1];
        column[3] = galois_multN(temp_column[3], 3) ^ galois_multN(temp_column[3], 2) ^ galois_multx(temp_column[3]) ^ galois_multN(temp_column[0], 3) ^ galois_multN(temp_column[0], 1) ^ temp_column[0] ^ galois_multN(temp_column[1], 3) ^ galois_multN(temp_column[1], 2) ^ temp_column[1] ^ galois_multN(temp_column[2], 3) ^ temp_column[2];
    }

    private void InvKey_expansion(int round_count) {
        int[][] temp = new int[Nb][Nb];

        for (int i = 0; i < Nb; i++) {
            temp[i][1] = keyBlock[i][0] ^ keyBlock[i][1];
            temp[i][2] = keyBlock[i][1] ^ keyBlock[i][2];
            temp[i][3] = keyBlock[i][2] ^ keyBlock[i][3];
        }

        int[] temp_row = new int[Nb];

        for (int i = 0; i < Nb; i++)
            temp_row[i] = temp[i][3];

        subWord(temp_row);
        rotateWordLeft(temp_row);

        for (int i = 0; i < Nb; i++)
            temp[i][0] = temp_row[i] ^ keyBlock[i][0] ^ RCON[i][round_count];

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                keyBlock[i][j] = temp[i][j];
    }
}
