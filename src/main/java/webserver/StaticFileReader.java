package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;

public class StaticFileReader {
    private final String BASE_URL = "src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(StaticFileReader.class);

    public byte[] staticFileHandler(String line) throws IOException {
        String[] tokens = line.split(" ");
        String path = tokens[1];
        String filepath = BASE_URL + path;

        try (FileInputStream fileInputStream = new FileInputStream(filepath)) {
            // 파일을 읽어 바이트 배열로 반환
            return fileInputStream.readAllBytes();
        }
    }
}
