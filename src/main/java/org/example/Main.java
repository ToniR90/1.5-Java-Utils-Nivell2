package org.example;

import java.io.*;
import java.util.Arrays;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {
        Properties properties = new Properties();
        try (InputStream input = new FileInputStream("src/main/resources/config.properties.txt")) {
            properties.load(input);

            String directory = properties.getProperty("directory.to.read");
            String outputFileName = properties.getProperty("output.file.name");

            saveDirectoryTreeToFile(directory, outputFileName);
            System.out.println("Output saved to " + outputFileName);
        } catch (IOException ex) {
            System.out.println("Error reading configuration: " + ex.getMessage());
        }
    }

    public static void saveDirectoryTreeToFile(String directory, String outputFileName) {
        File path = new File(directory);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            listDirectoryTree(path, writer, 0);
        } catch (IOException e) {
            System.out.println("Error writing to file: " + e.getMessage());
        }
    }

    public static void listDirectoryTree(File path, BufferedWriter writer, int level) throws IOException {
        if (path.exists() && path.isDirectory()) {
            String[] files = path.list();
            if (files != null) {
                Arrays.sort(files, String.CASE_INSENSITIVE_ORDER);
                for (String file : files) {
                    File currentFile = new File(path, file);
                    String type = currentFile.isDirectory() ? "D" : "F";
                    writer.write("  ".repeat(level) + type + " " + file);
                    writer.newLine();
                    // Recursive call if it's a directory
                    if (currentFile.isDirectory()) {
                        listDirectoryTree(currentFile, writer, level + 1);
                    }
                }
            } else {
                writer.write("The directory is empty");
                writer.newLine();
            }
        } else {
            System.out.println("The directory " + path + " doesn't exist");
        }
    }
}
