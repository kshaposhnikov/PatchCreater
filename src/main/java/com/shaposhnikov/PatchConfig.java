package com.shaposhnikov;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "PatchConfig")
@XmlAccessorType(XmlAccessType.NONE)
public class PatchConfig {

    @XmlElement(name = "SourcePath")
    private String sourcePath;

    @XmlElement(name = "TargetPath")
    private String targetPath;

    @XmlElement(name = "ClassNames")
    private List<String> classNames;

    public PatchConfig() {
        // For JAXB
    }

    public PatchConfig(String sourcePath, String targetPath, List<String> classNames) {
        this.sourcePath = sourcePath;
        this.targetPath = targetPath;
        this.classNames = classNames;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public String getTargetPath() {
        return targetPath;
    }

    public List<String> getClassNames() {
        return classNames;
    }
}
