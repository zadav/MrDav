package com.david.framework.desktop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import com.david.framework.FileIO;

public class DesktopFileIO implements FileIO {
    String storageDir;

    public DesktopFileIO() {
        this.storageDir = System.getProperty("user.home") + File.separator;
    }

    @Override
    public InputStream readAsset(String fileName) throws IOException {
        InputStream in = DesktopFileIO.class.getResourceAsStream("/" + fileName);
        if (in == null)
            throw new IOException("Asset not found: " + fileName);
        return in;
    }

    @Override
    public InputStream readFile(String fileName) throws IOException {
        return new FileInputStream(storageDir + fileName);
    }

    @Override
    public OutputStream writeFile(String fileName) throws IOException {
        return new FileOutputStream(storageDir + fileName);
    }
}
