package com.eduardomanrique.tsrd.datasource;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.*;

/**
 * Created by emanrique on 18/06/17.
 */
@Component
@Slf4j
public class EventEmitter {

    private DirectorySource directorySource;

    @Autowired
    private List<Algorithm> algorithmList;

    @Autowired
    private List<Filter> filterList;

    @Autowired
    private List<Modifier> modifierList;

    @Value("${source.dir}")
    private String sourceDirPath;

    @Value("${dest.dir}")
    private String destDirPath;

    private PrintStream output;

    private void processFile(File file) {
        log.info("Processing file " + file.getName());

        FileIterable iterable = new FileIterable(file);

        //map modifiers
        ConnectableObservable<TsrdEvent> connectableObservable = applyModifiers(applyFilters(Observable.fromIterable(iterable)))
                .publish();

        //subscribe algorithms
        algorithmList.stream().forEach(algorithm -> algorithm.connect(connectableObservable, output));

        connectableObservable.connect();

        moveFile(file);
    }

    @SneakyThrows
    public void startProcessing() {
        startProcessing(this.sourceDirPath, this.destDirPath, System.out, true);
    }

    @SneakyThrows
    public void startProcessing(String sourceDir, String destDir, PrintStream output, boolean startServiceThread) {
        this.output = output;
        this.directorySource = new DirectorySource(sourceDir, destDir);
        if (startServiceThread) {
            //start watch dir service
            Thread watcherThread = new Thread(() -> {
                String waitMessage = "\n========================================\nWaiting for files to be available at " + directorySource.getSourceDir().getAbsolutePath();
                log.info(waitMessage);
                while (!Thread.currentThread().isInterrupted()) {

                    if (processAvailableFiles()) {
                        log.info(waitMessage);
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (Exception e) {
                    }
                }
            });
            watcherThread.start();
        } else {
            processAvailableFiles();
        }
    }

    private boolean processAvailableFiles() {
        //get files to process
        FileFilter filter = file -> !file.isDirectory();
        File[] files = directorySource.getSourceDir().listFiles(filter);

        if (files.length > 0) {
            Arrays.stream(files).forEach(file -> processFile(file));
            return true;
        }
        return false;
    }

    private void moveFile(File file) {
        File moveTo = new File(directorySource.getDestDir().getAbsolutePath() + "/" + file.getName());
        log.info("\n============================\nFile " + file.getName() + " processed succesfully! Moving it to " + moveTo.getAbsolutePath() + "\n");
        file.renameTo(moveTo);
    }

    private Observable<TsrdEvent> applyFilters(Observable<TsrdEvent> observable) {
        return applyFilters(observable, filterList.iterator());
    }

    private Observable<TsrdEvent> applyFilters(Observable<TsrdEvent> observable, Iterator<Filter> it) {
        if (it.hasNext()) {
            return applyFilters(observable.filter(it.next()::filter), it);
        } else {
            return observable;
        }
    }

    private Observable<TsrdEvent> applyModifiers(Observable<TsrdEvent> observable) {
        return applyModifiers(observable, modifierList.iterator());
    }

    private Observable<TsrdEvent> applyModifiers(Observable<TsrdEvent> observable, Iterator<Modifier> it) {
        if (it.hasNext()) {
            return applyModifiers(observable.map(it.next()::map), it);
        } else {
            return observable;
        }
    }
}
