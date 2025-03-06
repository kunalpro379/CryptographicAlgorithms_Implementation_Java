import java.util.Arrays;

public class DESManual {
    
    // Initial Permutation Table
    private static final int[] IP = {
        58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
    };

    // Final Permutation Table
    private static final int[] FP = {
        40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25
    };

    // Expansion Table
    private static final int[] E = {
        32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11,
        12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21,
        22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
    };

    // P-Box Permutation
    private static final int[] P = {
        16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25
    };

    // S-Box Tables (Only showing 1 for example, full DES has 8)
    private static final int[][] S_BOX = {
        {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7},
        {0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8},
        {4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0},
        {15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13}
    };

    // Perform Initial Permutation
    public static int[] initialPermutation(int[] input) {
        int[] output = new int[64];
        for (int i = 0; i < 64; i++) {
            output[i] = input[IP[i] - 1];
        }
        return output;
    }

    // Perform Final Permutation
    public static int[] finalPermutation(int[] input) {
        int[] output = new int[64];
        for (int i = 0; i < 64; i++) {
            output[i] = input[FP[i] - 1];
        }
        return output;
    }

    // Expansion Function (E-Box)
    public static int[] expand(int[] R) {
        int[] expandedR = new int[48];
        for (int i = 0; i < 48; i++) {
            expandedR[i] = R[E[i] - 1];
        }
        return expandedR;
    }

    // S-Box Substitution (Example for 1 S-Box)
    public static int[] sBoxSubstitution(int[] input) {
        int row = (input[0] << 1) | input[5]; // First and last bit for row
        int col = (input[1] << 3) | (input[2] << 2) | (input[3] << 1) | input[4]; // Middle 4 bits for column
        int value = S_BOX[row][col]; // Get value from S-Box
        return new int[]{(value >> 3) & 1, (value >> 2) & 1, (value >> 1) & 1, value & 1}; // Convert to 4-bit array
    }

    // Feistel Function (One Round)
    public static int[] feistelFunction(int[] R, int[] subKey) {
        int[] expandedR = expand(R); // Expand R to 48 bits
        int[] xorResult = new int[48];

        // XOR with subKey
        for (int i = 0; i < 48; i++) {
            xorResult[i] = expandedR[i] ^ subKey[i];
        }

        // S-Box Substitution
        int[] substituted = new int[32];
        for (int i = 0; i < 8; i++) {
            int[] sBoxInput = Arrays.copyOfRange(xorResult, i * 6, (i + 1) * 6);
            int[] sBoxOutput = sBoxSubstitution(sBoxInput);
            System.arraycopy(sBoxOutput, 0, substituted, i * 4, 4);
        }

        // P-Box Permutation
        int[] permuted = new int[32];
        for (int i = 0; i < 32; i++) {
            permuted[i] = substituted[P[i] - 1];
        }

        return permuted;
    }

    public static void main(String[] args) {
        int[] plaintext = new int[64]; // Example 64-bit input (all zeros for simplicity)
        int[] permutedText = initialPermutation(plaintext); // Initial Permutation
        System.out.println("After Initial Permutation: " + Arrays.toString(permutedText));

        int[] finalText = finalPermutation(permutedText);
        System.out.println("After Final Permutation: " + Arrays.toString(finalText));
    }
}
