package io.askcuix.pomodoro.util;

import android.util.Base64;
import android.util.Log;

import java.nio.ByteBuffer;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Chris on 15/11/30.
 */
public class CipherHelper {
    private static final String TAG = "CipherHelper";

    private DESedeCipher mCipher;
    private ByteBuffer mScratchBuffer = ByteBuffer.allocate(8);

    //also called 3DES
    public static class DESedeCipher {
        private Cipher mEncrypt;
        private Cipher mDecrypt;

        private static final String ALGORITHM = "DESede/ECB/PKCS5Padding";

        private static final byte[] CIPHER_KEY = {0x61, 0x65, 0x66, 0x64, 0x40, 0x39, 0x33, 0x66,
                0x31, 0x2D, 0x35, 0x24, 0x61, 0x38, 0x34, 0x21, 0x65, 0x61, 0x32, 0x23, 0x39, 0x33, 0x31, 0x66};

        public DESedeCipher() {
            initCipher(CIPHER_KEY);
        }

        private void initCipher(byte[] key) {
            try {
                SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                mEncrypt = cipher;
            } catch (Exception e) {
                Log.e("DESedeCipher", e.toString());
            }

            try {
                SecretKeySpec skeySpec = new SecretKeySpec(key, ALGORITHM);
                Cipher cipher = Cipher.getInstance(ALGORITHM);
                cipher.init(Cipher.DECRYPT_MODE, skeySpec);
                mDecrypt = cipher;
            } catch (Exception e) {
                Log.e("DESedeCipher", e.toString());
            }
        }

        public byte[] encrypt(byte[] data) {
            if (mEncrypt != null) {
                try {
                    return mEncrypt.doFinal(data);
                } catch (Exception e) {
                    Log.e("DESedeCipher", e.toString());
                }
            }

            return data;
        }

        public byte[] decrypt(byte[] data) {
            if (mDecrypt != null) {
                try {
                    return mDecrypt.doFinal(data);
                } catch (Exception e) {
                    Log.e("DESedeCipher", e.toString());
                }
            }

            return data;
        }

        //the length of input and output may not equal
        public byte[] encrypt(byte[] data, int start, int count) {
            if (mEncrypt != null) {
                try {
                    return mEncrypt.doFinal(data, start, count);
                } catch (Exception e) {
                    Log.e("DESedeCipher", e.toString());
                }
            }

            return null;
        }

        public byte[] decrypt(byte[] data, int start, int count) {
            if (mDecrypt != null) {
                try {
                    return mDecrypt.doFinal(data, start, count);
                } catch (Exception e) {
                    Log.e("DESedeCipher", e.toString());
                }
            }

            return null;
        }

    }


    public CipherHelper() {
        mCipher = new DESedeCipher();
    }

    public synchronized String encryptString(String plain) {
        if (StringUtils.isNullOrEmpty(plain)) {
            return plain;
        }

        byte[] content = plain.getBytes();
        byte[] cipherByte = mCipher.encrypt(content);
        return Base64.encodeToString(cipherByte, Base64.NO_WRAP);
    }

    public synchronized String encryptInt(int plain) {
        mScratchBuffer.clear();
        mScratchBuffer.putInt(plain);
        byte[] cipherByte = mCipher.encrypt(mScratchBuffer.array(), 0, 4);
        return new String(Base64.encode(cipherByte, Base64.NO_WRAP));
    }

    public synchronized String encryptLong(long plain) {
        mScratchBuffer.clear();
        mScratchBuffer.putLong(plain);
        byte[] cipherByte = mCipher.encrypt(mScratchBuffer.array(), 0, 8);
        return new String(Base64.encode(cipherByte, Base64.NO_WRAP));
    }

    public synchronized String decryptString(String cipher) {
        if (StringUtils.isNullOrEmpty(cipher)) {
            return cipher;
        }

        byte[] content = cipher.getBytes();
        byte[] cipherByte = Base64.decode(content, Base64.NO_WRAP);
        return new String(mCipher.decrypt(cipherByte));
    }

    public synchronized int decryptInt(String cipher, int defaultVal) {
        if (StringUtils.isNullOrEmpty(cipher)) {
            return defaultVal;
        }

        byte[] cipherByte = Base64.decode(cipher.getBytes(), Base64.NO_WRAP);
        byte[] plainByte = mCipher.decrypt(cipherByte);

        if (plainByte.length > 4) {
            Log.e(TAG, "decrypt int error, byte length: " + plainByte.length);
            return defaultVal;
        }

        mScratchBuffer.clear();
        mScratchBuffer.put(plainByte);
        mScratchBuffer.flip();
        return mScratchBuffer.getInt();
    }

    public synchronized long decryptLong(String cipher, long defaultVal) {
        if (StringUtils.isNullOrEmpty(cipher)) {
            return defaultVal;
        }

        byte[] cipherByte = Base64.decode(cipher.getBytes(), Base64.NO_WRAP);
        byte[] plainByte = mCipher.decrypt(cipherByte);

        if (plainByte.length > 8) {
            Log.e(TAG, "decrypt long error, byte length: " + plainByte.length);
            return defaultVal;
        }

        mScratchBuffer.clear();
        mScratchBuffer.put(plainByte);
        mScratchBuffer.flip();
        return mScratchBuffer.getLong();
    }
}
