/******************************************************************************
 *  Compilation:  javac GenomeCompressor.java
 *  Execution:    java GenomeCompressor - < input.txt   (compress)
 *  Execution:    java GenomeCompressor + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *  Data files:   genomeTest.txt
 *                virus.txt
 *
 *  Compress or expand a genomic sequence using a 2-bit code.
 ******************************************************************************/

/**
 *  The {@code GenomeCompressor} class provides static methods for compressing
 *  and expanding a genomic sequence using a 2-bit code.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *  @author Zach Blick
 */
public class GenomeCompressor {
    /**
     * Reads a sequence of 8-bit extended ASCII characters over the alphabet
     * { A, C, T, G } from standard input; compresses and writes the results to standard output.
     */
    public static void compress() {
        // Get the string from the terminal and find the length
        String s = BinaryStdIn.readString();
        int n = s.length();

        // Find fraction of byte leftover
        int extra = n % 4;

        // If there is no extra then we need to add another byte
        if (extra == 0) {
            BinaryStdOut.write(0, 8); // 00000000 (starting with 00)
        }
        // If there is 1/4 of a byte (2 bits) then we need six more
        else if (extra == 1) {
            BinaryStdOut.write(16, 6); // 010000 (16 because needs to start with 01)
        }
        // If there is 1/2 of a byte (4 bits) we need four more
        else if (extra == 2) {
            BinaryStdOut.write(16, 4); // 1000 (16 because needs to start with 10)
        }
        else {
            BinaryStdOut.write(3, 2); // 11 (3 because needs to start with 11)
        }

        // Look for each of the symbols and uniquely express with 2 bits
        for (int i = 0; i < n; i++) {
            if (s.charAt(i) == 'A') {
                BinaryStdOut.write(0, 2);
            }
            else if (s.charAt(i) == 'C') {
                BinaryStdOut.write(1, 2);
            }
            else if (s.charAt(i) == 'G') {
                BinaryStdOut.write(2, 2);
            }
            else if (s.charAt(i) == 'T') {
                BinaryStdOut.write(3, 2);
            }
        }
        BinaryStdOut.close();
    }

    /**
     * Reads a binary sequence from standard input; expands and writes the results to standard output.
     */
    public static void expand() {

        // Find the header by reading the first two
        int header = BinaryStdIn.readInt(2);
        // If it is 00 then read the next 6 junk bits (because the whole header is 8 bits)
        if (header == 0) {
            BinaryStdIn.readInt(6);
        }
        // If it is 01 read next four junk bits
        else if (header == 1) {
            BinaryStdIn.readInt(4);
        }
        // If it is 10 read next 2 junk bits
        else if (header == 2) {
            BinaryStdIn.readInt(2);
        }

        // While we have input
        while (!BinaryStdIn.isEmpty()) {
            // Get the next two bits
            int curr = BinaryStdIn.readInt(2);
            // Check for which symbol based on value and write it out
            if (curr == 0) {
                BinaryStdOut.write('A'); // Code for A is 00
            }
            else if (curr == 1) {
                BinaryStdOut.write('C'); // Code for C is 01
            }
            else if (curr == 2) {
                BinaryStdOut.write('G'); // Code for G is 10
            }
            else if (curr == 3) {
                BinaryStdOut.write('T'); // Code for T is 11
            }
        }
        BinaryStdOut.close();
    }


    /**
     * Main, when invoked at the command line, calls {@code compress()} if the command-line
     * argument is "-" an {@code expand()} if it is "+".
     *
     * @param args the command-line arguments
     */
    public static void main(String[] args) {

        if      (args[0].equals("-")) compress();
        else if (args[0].equals("+")) expand();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}