package com.eduardomanrique.tsrd.datasource;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import java.io.*;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by emanrique on 18/06/17.
 */
@Slf4j
public class FileIterable implements Iterable<TsrdEvent> {

    private InputStream inputStream;
    private BufferedReader bufferedReader;
    private TsrdEvent currentEvent;
    private File file;

    @SneakyThrows
    FileIterable(File file) {
        this.file = file;
        this.inputStream = new FileInputStream(file);
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public Iterator<TsrdEvent> iterator() {
        return new Iterator<TsrdEvent>() {
            @Override
            public boolean hasNext() {
                if (currentEvent == null) {
                    try {
                        String line;
                        do {
                            line = bufferedReader.readLine();
                            if (StringUtils.hasLength(line)) {
                                currentEvent = parse(line);
                                break;
                            }
                        } while (line != null);
                    } catch (IOException e) {
                        log.error("Error reading line", e);
                        return false;
                    }
                }
                boolean result = currentEvent != null;
                if (!result) {
                    closeResources();
                }
                return result;
            }

            @Override
            public TsrdEvent next() {
                TsrdEvent event = currentEvent;
                currentEvent = null;
                return event;
            }
        };
    }

    private void closeResources() {
        try {
            inputStream.close();
        } catch (IOException e) {
            log.error("Error closing inputstream of " + file.getAbsolutePath());
        }
        try {
            bufferedReader.close();
        } catch (IOException e) {
        }
    }

    private DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);

    private TsrdEvent parse(String line) {
        TsrdEvent event = null;
        String[] values = line.split(",");
        Assert.isTrue(values.length == 3, "Invalid line " + line);
        try {
            event = new TsrdEvent(
                    values[0], //instrument name
                    dateFormat.parse(values[1]), //date
                    new BigDecimal(values[2]), //value
                    line //original line
            );
        } catch (ParseException e) {
            throw new RuntimeException("Error parsing line " + line + " of file " + file.getAbsolutePath(), e);
        }
        return event;
    }

}
