package com.shaposhnikov;

import com.shaposhnikov.util.Constants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> classNames = new ArrayList<>();
        String sourcePath = "";
        String targetPath = "";

        String currentCommand = "";
        for (String arg : args) {
            if (Constants.TerminalArguments.ARGUMENTS.contains(arg)) {
                currentCommand = arg;
            } else {
                switch (currentCommand) {
                    case Constants.TerminalArguments.SOURCE_PATH_ARG:
                        sourcePath = arg;
                        break;
                    case Constants.TerminalArguments.TARGET_PATH_ARG:
                        targetPath = arg;
                        break;
                    case Constants.TerminalArguments.CLASS_NAME_ARG:
                        classNames.add(arg);
                }
            }
        }

        System.out.println("Creating patch started");

        for (String className : classNames) {
            String str = parseClassName(className);

            System.out.println("Search " + className);

            findClass(sourcePath, targetPath, str);
        }
    }

    private static String parseClassName(String name) {
        return name.replace(".", "\\");
    }

    private static void findClass(String sourcePasth, String targetPath, String className) throws IOException {
        File currentFile = new File(sourcePasth);
        for (File file : currentFile.listFiles()) {
            if (file.isDirectory()) {
                findClass(file.getAbsolutePath(), targetPath, className);
            } else if (file.getAbsolutePath().contains(className + ".class")) {

                System.out.println("Class " + className + " was found");

                copyClassToTargetFolder(file.getAbsolutePath(), targetPath, className);
            }
        }
    }

    private static void copyClassToTargetFolder(String sourcePath, String targetPath, String className) throws IOException {
        String[] splitClassName = className.split("\\\\");
        String newTargetPath = targetPath;
        for (int i = 0; i < splitClassName.length - 1; i++) {
            newTargetPath += "\\" + splitClassName[i];
            if (!Files.exists(Paths.get(newTargetPath))) {
                Files.createDirectory(Paths.get(newTargetPath));
            }
        }

        newTargetPath += "\\" + splitClassName[splitClassName.length - 1] + ".class";
        Files.copy(Paths.get(sourcePath), Paths.get(newTargetPath), StandardCopyOption.REPLACE_EXISTING);

        System.out.println("Class " + className + " was copied successfully");
    }
}