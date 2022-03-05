/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.verisoft.fw.utils.internal;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Contains some methods to list files and folders from a directory
 *
 * @author Loiane Groner
 * http://loiane.com (Portuguese)
 * http://loianegroner.com (English)
 */
public class ListFilesUtil {


    /**
     * List all the files and folders from a directory
     *
     * @param directoryName to be listed
     */
    public static List<File> listFilesAndFolders(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        return new ArrayList<File>(Arrays.asList(directory.listFiles()));

    }


    /**
     * List all the files under a directory
     *
     * @param directoryName to be listed
     */
    public static List<File> listFiles(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        List<File> fList = new ArrayList<File>(Arrays.asList(directory.listFiles()));
        for (File file : fList) {
            if (!file.isFile()) {
                fList.remove(file);
            }
        }
        return fList;
    }


    /**
     * List all the folder under a directory
     *
     * @param directoryName to be listed
     */
    public static void listFolders(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        List<File> fList = new ArrayList<File>(Arrays.asList(directory.listFiles()));
        for (File file : fList) {
            if (!file.isDirectory()) {
                fList.remove(file);
            }
        }
    }


    /**
     * List all files from a directory and its subdirectories
     *
     * @param directoryName to be listed
     */
    public static List<File> listFilesAndFilesSubDirectories(String directoryName) {
        File directory = new File(directoryName);
        //get all the files from a directory
        File[] fList = directory.listFiles();
        List<File> fileCollection = new ArrayList<File>();
        for (File file : fList) {
            if (file.isFile()) {
                fileCollection.add(file);
            } else if (file.isDirectory()) {
                fileCollection.addAll(listFilesAndFilesSubDirectories(file.getAbsolutePath()));
            }
        }
        return new ArrayList<File>(fileCollection);
    }


    public static void copyDirectory(File srcDir, File destDir, boolean createSubDirectories) throws IOException {

        if (createSubDirectories) {
            FileUtils.copyDirectory(srcDir, destDir);
        } else {
            List<File> files = listFilesAndFilesSubDirectories(srcDir.getAbsolutePath());
            for (File file : files)
                FileUtils.copyFileToDirectory(file, destDir);
        }
    }


    /**
     * search file in specific folder
     *
     * @param directoryUrl to search there
     * @param fileToSearch
     * @return file if found, otherwise null
     */
    public static File searchInFolder(String directoryUrl, String fileToSearch) {
        File folder = new File(directoryUrl);
        File[] listOfFiles = folder.listFiles();
        File f = null;

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().equals(fileToSearch)) {
                f = file;
                break;
            }
        }

        return f;
    }
}
