package com.oslash.integration.plugin.constant;

/**
 * Enum : File Type and their associated mime types
 *
 * @author amitchaudhari228@gmail.com
 * Sept-2022
 */

public enum FileType {
    PDF("application/pdf");

    private String fileFormat;

    FileType(String fileFormat) {
        this.fileFormat = fileFormat;
    }

    public String getFileFormat() {
        return fileFormat;
    }
}