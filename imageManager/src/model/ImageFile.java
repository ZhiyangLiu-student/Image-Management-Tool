package model;

import java.io.Serial;
import java.io.Serializable;

public class ImageFile implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private String filename;
    private int id;
    private int width;
    private int height;
    private String camera;
    private String location;

    public ImageFile(String filename, int id, int width, int height, String camera, String location) {
        this.filename = filename;
        this.id = id;
        this.width = width;
        this.height = height;
        this.camera = camera;
        this.location = location;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
