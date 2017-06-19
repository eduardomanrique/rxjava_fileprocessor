package com.eduardomanrique.tsrd.integratedtests;

import com.eduardomanrique.tsrd.datasource.EventEmitter;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by emanrique on 19/06/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class IntetegrationTests {

    @Autowired
    EventEmitter eventEmitter;

    @Test
    public void test() throws Exception {
        Path tempSourceDir = Files.createTempDirectory("tsrd_source_");
        Path tempDestDir = Files.createTempDirectory("tsrd_dest_");

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream output = new PrintStream(byteArrayOutputStream);

        //copy file to dest dir
        IOUtils.copy(IntetegrationTests.class.getResourceAsStream("/example_input.txt"),
                new FileOutputStream(tempSourceDir + "/input.txt"));

        eventEmitter.startProcessing(tempSourceDir.toString(), tempDestDir.toString(), output, false);

        String outputText = new String(byteArrayOutputStream.toByteArray());

        File[] processedFiles = tempDestDir.toFile().listFiles();
        assertEquals(1, processedFiles.length);

        assertTrue(outputText.contains("INSTRUMENT1\nMean value: 6.7351835"));
        assertTrue(outputText.contains("INSTRUMENT2\nMean value after november 2014: 4.6261060"));
        assertTrue(outputText.contains("INSTRUMENT3\nMAX value: 149.98\nMIN value: 75.7195"));
        assertTrue(outputText.contains("SUM of the last 10: 0"));
    }
}
