import java.io.FileNotFoundException;
import java.io.DataOutputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;

import java.io.IOException;
import java.io.RandomAccessFile;

public class test {
    public static void main(String[] args) throws FileNotFoundException, IOException {
//        RandomAccessFile f = new RandomAccessFile("example", "rw");
//        f.writeInt(1); //write a "1" into the first four bytes of the file
//        f.writeInt(2); //write the value "2" after the value "1"
//        f.writeInt(3); //write the value "3" after the value "2"
//        f.seek(4); //file pointer now between fourth and fifth byte
//        System.out.println("Read four bytes as an int value " + f.readInt());
//        System.out.println("The file is " + f.length() + " bytes long");

        RandomAccessFile f1 = new RandomAccessFile("example2","rw");
        DataOutputStream d = new DataOutputStream(
                new BufferedOutputStream(new FileOutputStream(f1.getFD())));
        d.writeInt(1); //write calls now only store primitive ints in memory
        d.writeInt(2);
        d.writeInt(3);
        d.flush(); //force the contents to be written to the disk. Important!
//        f.seek(8);
//        RandomAccessFile f2 = new RandomAccessFile("example2","rw");
//
//        DataOutputStream d2 = new DataOutputStream(
//                new BufferedOutputStream(new FileOutputStream(f2.getFD())));
////        f2.seek(8);
//        System.out.println(f.readInt());
//        System.out.println(f2.readInt());
//
//        d2.writeInt(7);
//        d2.flush();
////        f2.seek(12);
        System.out.println(f1.length());
        for(int i=0; i<f1.length(); i++) {
            System.out.println(f1.readInt());
        }

//        System.out.println("Read four bytes as an int value "+f1.readInt());
//        System.out.println("The file is "+f1.length()+" bytes long");
    }
}