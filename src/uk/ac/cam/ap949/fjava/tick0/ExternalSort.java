package uk.ac.cam.ap949.fjava.tick0;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ExternalSort {
    static long fileLength;
    private static String byteToHex(byte b) {
        String r = Integer.toHexString(b);
        if (r.length() == 8) {
            return r.substring(6);
        }
        return r;
    }

    public static String checkSum(String f) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream ds = new DigestInputStream(
                    new FileInputStream(f), md);
            byte[] b = new byte[512];
            while (ds.read(b) != -1)
                ;

            String computed = "";
            for(byte v : md.digest())
                computed += byteToHex(v);

            return computed;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "<error computing checksum>";
    }

    public static void sort(String filenameA, String filenameB) throws FileNotFoundException, IOException {

        String currentInput = filenameA;
        String currentOutput = filenameB;

        RandomAccessFile inputFile1 = new RandomAccessFile(currentInput, "rw");
        RandomAccessFile inputFile2 = new RandomAccessFile(currentInput, "rw");
        RandomAccessFile outputFile = new RandomAccessFile(currentOutput, "rw");

        int intAmount = (int) inputFile1.length() / 4;

        DataInputStream inputStream1 = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile1.getFD())));
        DataInputStream inputStream2 = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile2.getFD())));
        DataOutputStream outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile.getFD())));


        for (int b = 1; b < intAmount; b *= 2) {
            int blockAmount = (int) Math.ceil(((double)intAmount) / b);
            if (blockAmount % 2 == 1) {
                blockAmount -= 1;
            }
            int leftPointer = Integer.MAX_VALUE;
            int rightPointer = Integer.MAX_VALUE;
            inputStream2.skipBytes(4*b);
            for (int i = 0; i < blockAmount / 2; i++) {//goes through each of the blocks
                int amountLeft = 0;
                int amountRight = 0;
                while (true) { //goes through each integer
                    if (leftPointer == Integer.MAX_VALUE && amountLeft != b) {
                        amountLeft++;
                        leftPointer = inputStream1.readInt();
                    }
                    if (rightPointer == Integer.MAX_VALUE && (amountRight != b && inputStream2.available() > 0)) {
                        amountRight++;
                        rightPointer = inputStream2.readInt();
                    }
                    if (leftPointer > rightPointer) {
                        outputStream.writeInt(rightPointer);
                        rightPointer = Integer.MAX_VALUE;
                    } else {
                        if (leftPointer == Integer.MAX_VALUE) {
                            break;
                        }
                        outputStream.writeInt(leftPointer);
                        leftPointer = Integer.MAX_VALUE;
                    }
                }
                inputStream1.skipBytes(4*b);
                inputStream2.skipBytes(4*b);

            }
            while (inputStream1.available() > 0) { // when odd, there will be left over integers not in block we need to copy over
                outputStream.writeInt(inputStream1.readInt());
            }

            outputStream.flush();

            inputFile1 = new RandomAccessFile(currentOutput, "rw");
            inputFile2 = new RandomAccessFile(currentOutput, "rw");
            outputFile = new RandomAccessFile(currentInput, "rw");

            String temp = currentInput;
            currentInput = currentOutput;
            currentOutput = temp;

            inputStream1 = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile1.getFD())));
            inputStream2 = new DataInputStream(new BufferedInputStream(new FileInputStream(inputFile2.getFD())));
            outputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(outputFile.getFD())));
        }

        if(currentOutput.equals(filenameA)) {
            while (inputStream1.available() > 0) {
                outputStream.writeInt(inputStream1.readInt());
            }
            outputStream.flush();
        }
    }

    public static void main(String[] args) {
        String filenameA = "test11a.dat";
        String filenameB = "test11b.dat";
        try {
            sort(filenameA, filenameB);
        } catch (FileNotFoundException fNf) {
            System.out.println("file not found");
        } catch (IOException io) {
            System.out.println(io);
        }
        System.out.println("The checksum is: " + checkSum(filenameA));

    }
}
