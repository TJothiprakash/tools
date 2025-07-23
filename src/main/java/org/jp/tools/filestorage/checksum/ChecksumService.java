package org.jp.tools.filestorage.checksum;

import java.io.InputStream;

public interface ChecksumService {
    String computeChecksum(InputStream inputStream) throws Exception;
    boolean validateChecksum(InputStream inputStream, String expectedChecksum) throws Exception;
}
