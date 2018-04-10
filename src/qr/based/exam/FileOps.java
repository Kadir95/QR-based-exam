/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package qr.based.exam;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Queue;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author mzp7
 */


public class FileOps {
    public static File filechooser(String title, String desc, String[] types){
        Stage stage = new Stage();
        FileChooser fc = new FileChooser();
        fc.setTitle(title);
        fc.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter(desc, types)
            );
        return fc.showOpenDialog(stage);
    }
}

class FileRead<E> {
    /**
     * findFile function finds the given file as an argument which is named as fileName by breadth first search algorithm.
     * @param file top direction for the search.
     * @param fileName name of the searching file.
     * @return if function find the file, returns that file as File, if doesn't returns null.
     */
    public static File findFile(File file, String fileName) {
        Queue<File> fileQ = new LinkedList<>();
        fileQ.add(file);
        File temp;
        while (!fileQ.isEmpty()){
            temp = fileQ.poll();

            if(temp.isFile()){
                String[] s = temp.getName().split("/");
                if(s[s.length - 1].equals(fileName)){
                    return temp;
                }
            } else {
                File[] list = temp.listFiles();
                for(File f : list){
                    fileQ.add(f);
                }
            }
        }
        return null;
    }

    public E readBinaryFile(File file) throws IOException, ClassNotFoundException {
        if(file == null){
            System.err.println("There isn't any binary file to read!");
            return null;
        }

        FileInputStream fis = new FileInputStream(file);
        ObjectInputStream ois = new ObjectInputStream(fis);
        E object = (E) ois.readObject();

        return object;
    }

    public void writeListToBinaryFile(File file, E e){
        if (file == null)
            throw new NoSuchElementException("No File to Write!");

        try{
            FileOutputStream fos = new FileOutputStream(file.getAbsolutePath(), false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(e);
            oos.flush();
            oos.close();
        } catch (FileNotFoundException x){
            System.err.println(x);
        } catch (IOException x){
            System.err.println(x);
        }
    }
}