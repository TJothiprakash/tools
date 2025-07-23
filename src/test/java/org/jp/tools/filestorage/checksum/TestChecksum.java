package org.jp.tools.filestorage.checksum;

import org.jp.tools.filestorage.checksum.SHA256ChecksumService;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestChecksum {

    private final SHA256ChecksumService checksumService = new SHA256ChecksumService();

    // 🔁 Update this path with your test file
    private static final String TEST_FILE_PATH = "E:/reactjs_namasthe/java_collections_/unnamed.png";

    // 🔁 You must precompute this using your checksum logic or tool (in Base64)
    private static final String EXPECTED_BASE64_CHECKSUM = "a7725c3eadd26a4572181f779d2312ae3e5216096c77cd8aae02c29825fb6a9d"; // replace with correct Base64!
//a7725c3eadd26a4572181f779d2312ae3e5216096c77cd8aae02c29825fb6a9d
    @Test
    void testComputeChecksum() throws Exception {
        File file = new File(TEST_FILE_PATH);
        try (InputStream inputStream = new FileInputStream(file)) {
            String checksum = checksumService.computeChecksum(inputStream);

            // ✅ For testing, print it to verify
            System.out.println("Computed Checksum (Base64): " + checksum);

            // ✅ Just check it is not null or empty
            assertTrue(checksum != null && !checksum.isEmpty());
        }
    }

    @Test
    void testChecksumValidation() throws Exception {
        File file = new File(TEST_FILE_PATH);
        try (InputStream inputStream = new FileInputStream(file)) {
            // ⚠️ Only works if EXPECTED_BASE64_CHECKSUM is correct
            boolean isValid = checksumService.validateChecksum(inputStream, EXPECTED_BASE64_CHECKSUM);
            assertTrue(isValid, "Checksum should match");
        }
    }
        @Test
        void testChecksumMatch() throws Exception {
            File file = new File("E:/reactjs_namasthe/java_collections_/unnamed.png"); // must be non-empty

            SHA256ChecksumService service = new SHA256ChecksumService();
            String actual = service.computeChecksum(new FileInputStream(file));

            // Precomputed checksum from client
            String expected = "a7725c3eadd26a4572181f779d2312ae3e5216096c77cd8aae02c29825fb6a9d";

            assertEquals(expected, actual);
        }

}

