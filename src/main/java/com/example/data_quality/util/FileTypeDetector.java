package com.example.data_quality.util;

import com.example.data_quality.core.FileType;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

public final class FileTypeDetector {
    private static final Tika TIKA = new Tika();

    private FileTypeDetector() {
    }

    public static FileType detect(MultipartFile file) {
        try {
            String mime = TIKA.detect(file.getInputStream(), file.getOriginalFilename());

            if (mime == null) return FileType.UNKNOWN;
            if (mime.contains("csv") || file.getOriginalFilename().endsWith(".csv")) return FileType.CSV;
            if (mime.contains("json")) return FileType.JSON;
            if (mime.contains("xml")) return FileType.XML;
            if (mime.contains("sheet") || file.getOriginalFilename().endsWith(".xlsx")) return FileType.EXCEL;

            return FileType.UNKNOWN;
        } catch (Exception e) {
            return FileType.UNKNOWN;
        }

    }
}
