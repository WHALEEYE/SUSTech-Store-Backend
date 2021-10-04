package tech.whaleeye.misc.constants;

import lombok.Getter;

// upload file types
@Getter
public enum UploadFileType {
    AVATAR(200 * 1024, 400, "avatar"), DESC_PIC(2 * 1024 * 1024, 1080, "desc");
    // Max file size (Bytes)
    private final long fileSizeLimit;
    private final long heightLimit;
    private final String relFilePath;

    UploadFileType(long fileSizeLimit, long heightLimit, String relFilePath) {
        this.fileSizeLimit = fileSizeLimit;
        this.heightLimit = heightLimit;
        this.relFilePath = relFilePath;
    }
}
