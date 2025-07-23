package org.jp.tools.filestorage.checksum;

import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.security.MessageDigest;

@Service
public class SHA256ChecksumService implements ChecksumService {

    private static final int BUFFER_SIZE = 8192;

    @Override
    public String computeChecksum(InputStream inputStream) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;

        while ((bytesRead = inputStream.read(buffer)) != -1) {
            digest.update(buffer, 0, bytesRead);
        }

        byte[] hash = digest.digest();
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }

        String checksum = hexString.toString();
        System.out.println("server calculated checksum: " + checksum);
        return checksum;
    }


    @Override
    public boolean validateChecksum(InputStream inputStream, String expectedChecksum) throws Exception {
        String computed = computeChecksum(inputStream);
        return computed.equals(expectedChecksum);
    }
}
