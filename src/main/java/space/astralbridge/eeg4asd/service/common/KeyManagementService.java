package space.astralbridge.eeg4asd.service.common;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.SecretKey;

import org.springframework.stereotype.Service;

@Service
public class KeyManagementService {
    public SecretKey generateHmacKey() throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance("HmacSHA256").generateKey();
        return secretKey;
    }

    private byte[] HmacSHA256(byte[] data, SecretKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        Mac mac = Mac.getInstance("HmacSHA256");
        mac.init(key);
        return mac.doFinal(data);
    }

    public byte[] HmacSHA256(String data, SecretKey key) throws NoSuchAlgorithmException, InvalidKeyException {
        return HmacSHA256(data.getBytes(), key);
    }

}
