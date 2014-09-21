package main.de.jezlynn.medialist.handler;

import main.de.jezlynn.medialist.helper.LogHelper;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michael on 24.08.2014.
 */
public class FileHandler {

    private File file;

    public FileHandler(String path) {
        file = new File(path);
    }

    public FileHandler(File file) {
        this.file = file;
    }

    private File[] getFilesList() {
        if (file == null) {
            LogHelper.error("File was null and could not be loaded");
            return new File[]{};
        }
        return file.listFiles();
    }

    public Map<String, File> getDirectorys() {
        Map<String, File> fileDirectorys = new HashMap<String, File>(1024);
        File[] directorys = this.getFilesList();

        if (directorys == null) {
            LogHelper.error("There were no files to be read");
            return fileDirectorys;
        }

        for (File directory : directorys) {
            if (directory.isDirectory()) {
                fileDirectorys.put(directory.getName(), directory);
                LogHelper.debug("Found Directory: " + directory.getName());
            }
        }
        return fileDirectorys;
    }

    public Map<String, File> getFiles() {
        Map<String, File> fileNames = new HashMap<String, File>(1024);
        File[] files = this.getFilesList();

        if (files == null) {
            LogHelper.error("There were no files to be read");
            return fileNames;
        }

        for (File file : files) {
            if (file.isFile()) {
                fileNames.put(file.getName(), file);
                LogHelper.debug("Found File: " + file.getName());
            }
        }
        return fileNames;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(1024);
        Map<String, File> files = this.getDirectorys();
        for (String fileName : files.keySet()) {
            sb.append("File with name: ").append(fileName).append(" found at: ").append(files.get(fileName).getPath()).append("\n");
        }
        return sb.toString();
    }
}
