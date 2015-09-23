package com.shaposhnikov.util;

import java.util.Arrays;
import java.util.List;

public class Constants {

    public static class TerminalArguments {
        public static final String SOURCE_PATH_ARG = "-sourcePath";
        public static final String TARGET_PATH_ARG = "-targetPath";
        public static final String CLASS_NAME_ARG = "-className";

        public static final List<String> ARGUMENTS = Arrays.asList(SOURCE_PATH_ARG, TARGET_PATH_ARG, CLASS_NAME_ARG);
    }
}
