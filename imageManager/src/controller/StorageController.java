//This work done by Zhiyang Liu and Yanxiao He
package controller;

import model.ImageFile;

import java.io.*;
import java.util.ArrayList;

public class StorageController {
    private static String filename="data/images.data";
    private static int nextId=0;
    public static int getNextId(){
        return nextId++;
    }
    public static void init(){
        File file=new File(filename);
        if(!file.exists()){
            ArrayList<ImageFile> users=new ArrayList<>();
            save(users);
        }
        getAllImages();
    }
    public static void save(ArrayList<ImageFile> images){
        try{
            ObjectOutputStream outputStream=new ObjectOutputStream(new FileOutputStream(filename));
            outputStream.writeObject(images);
            outputStream.close();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public static ArrayList<ImageFile> getAllImages(){
        ArrayList<ImageFile> images = new ArrayList<>();
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(filename))) {
            Object object = inputStream.readObject();
            images = (ArrayList<ImageFile>) object;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        if(images.size()==0){
            nextId=0;
        }
        else{
            nextId=images.get(images.size()-1).getId()+1;
        }
        return images;
    }

    public static void deleteImageFile(int id) {
        ArrayList<ImageFile> images=getAllImages();
        for(int i=0;i<images.size();i++){
            if(images.get(i).getId()==id){
                images.remove(images.get(i));
                break;
            }
        }
        save(images);
    }
}
