package com.eduardomanrique.tsrd.datasource;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;

/**
 * Created by emanrique on 18/06/17.
 */
@Slf4j
public class DirectorySource {

    @Getter
    private File sourceDir;

    @Getter
    private File destDir;

    public DirectorySource(String sourceDirPath, String destDirPath) {
        this.sourceDir = new File(sourceDirPath.replace("{WORKING_DIR}", System.getProperty("user.dir")));
        prepareDir(this.sourceDir, "Source");
        this.destDir = new File(destDirPath.replace("{WORKING_DIR}", System.getProperty("user.dir")));
        prepareDir(this.destDir, "Destination");
    }

    //if the dir not exists, create. If exists but it is not a dir, abort.
    private void prepareDir(File dir, String type) {
        if (!dir.exists()) {
            log.info(type + " dir " + dir.getAbsolutePath() + " does not exist. Creating folder");
            dir.mkdirs();
        }
        if (!dir.isDirectory()) {
            log.error(type + " dir " + dir.getAbsolutePath() + " is not a directory. Exiting with error");
            System.exit(-1);
        }
    }
}
