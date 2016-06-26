package com.shaposhnikov;

import com.shaposhnikov.util.Callable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class PatchCreater {

    private final Callable<String> callable;

    public PatchCreater(Callable<String> callable) {
        this.callable = callable;
    }

    public void create(PatchConfig config) {
        try {
            callable.call("Creating patch started");

            for (String className : config.getClassNames()) {
                String str = parseClassName(className);

                callable.call("Search " + className);

                findClass(config.getSourcePath(), config.getTargetPath(), str);
            }

            save(config);

            callable.call("Creating patch finished");
        } catch (IOException | JAXBException exp) {
            StringBuilder sb = new StringBuilder();
            for (StackTraceElement element : exp.getStackTrace()) {
                sb.append(element.toString()).append("\n");
            }
            callable.call(sb.toString());
        }
    }

    public void save(PatchConfig config) throws JAXBException {
        JAXBContext JAXB = JAXBContext.newInstance(PatchConfig.class);
        Marshaller marshaller = JAXB.createMarshaller();
        marshaller.marshal(config, new File(System.getProperty("user.dir") + "/patch_creater_config.xml"));
        callable.call("The last configuration successfully saved");
    }

    private String parseClassName(String name) {
        return name.replace(".", "\\");
    }

    private void findClass(String sourcePasth, String targetPath, String className) throws IOException {
        File currentFile = new File(sourcePasth);
        for (File file : currentFile.listFiles()) {
            if (file.isDirectory()) {
                findClass(file.getAbsolutePath(), targetPath, className);
            } else if (file.getAbsolutePath().contains(".class") && file.getAbsolutePath().contains(className)) {

                callable.call("Class " + file.getName() + " was found");

                copyClassToTargetFolder(file, targetPath, className);
            }
        }
    }

    private void copyClassToTargetFolder(File sourceFile, String targetPath, String className) throws IOException {
        String[] splitClassName = className.split("\\\\");
        String newTargetPath = targetPath;
        for (int i = 0; i < splitClassName.length - 1; i++) {
            newTargetPath += "\\" + splitClassName[i];
            if (!Files.exists(Paths.get(newTargetPath))) {
                Files.createDirectory(Paths.get(newTargetPath));
            }
        }

        newTargetPath += "\\" + sourceFile.getName();
        Files.copy(Paths.get(sourceFile.getAbsolutePath()), Paths.get(newTargetPath), StandardCopyOption.REPLACE_EXISTING);

        callable.call("Class " + sourceFile.getName() + " was copied successfully from " + sourceFile + " to " + newTargetPath);
    }
}
