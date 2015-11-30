package io.askcuix.pomodoro.util;

import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;

import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Vector;

/**
 * Created by Chris on 15/11/30.
 */
public class StringUtils {
    private static final String TAG = "StringUtils";

    public static final boolean IGNORE_CASE = true;
    public static final boolean IGNORE_WIDTH = true;

    public static boolean isNullOrEmpty(String str) {
        return FP.empty(str);
    }

    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isAllWhitespaces(String str) {
        boolean ret = true;
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                ret = false;
                break;
            }
        }
        return ret;
    }

    public static boolean isAllDigits(String str) {
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }

    public static boolean equal(String s1, String s2) {
        return equal(s1, s2, false);
    }

    public static boolean equal(String s1, String s2, boolean ignoreCase) {
        if (s1 != null && s2 != null) {
            if (ignoreCase) {
                return s1.equalsIgnoreCase(s2);
            } else {
                return s1.equals(s2);
            }
        } else {
            return ((s1 == null && s2 == null) ? true : false);
        }
    }

    public static Vector<String> parseMediaUrls(String str, String beginTag, String endTag) {
        Vector<String> list = new Vector<String>();
        if (!isNullOrEmpty(str)) {
            int beginIndex = str.indexOf(beginTag, 0);
            int endIndex = str.indexOf(endTag, 0);
            while ((beginIndex != -1 && endIndex != -1) && (endIndex > beginIndex)) {
                beginIndex += beginTag.length();
                String imgUrl = str.substring(beginIndex, endIndex);
                if (!isNullOrEmpty(imgUrl) && imgUrl.charAt(0) != '[') {
                    list.add(imgUrl);
                }
                endIndex += endIndex + endTag.length();
                beginIndex = str.indexOf(beginTag, endIndex);
                endIndex = str.indexOf(endTag, endIndex);
            }
        }
        return list;
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive ver.
     */
    public static int find(String pattern, String s) {
        return find(pattern, s, !IGNORE_CASE);
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive can be parameterized
     */
    public static int find(String pattern, String s, boolean ignoreCase) {
        return find(pattern, s, ignoreCase, !IGNORE_WIDTH);
    }

    /**
     * Safe string finding (indexOf) even the arguments are empty Case sentive and Full/Half width ignore can
     * be parameterized
     */
    public static int find(String pattern, String s, boolean ignoreCase, boolean ignoreWidth) {
        if (FP.empty(s))
            return -1;
        pattern = FP.ref(pattern);
        if (ignoreCase) {
            pattern = pattern.toLowerCase();
            s = s.toLowerCase();
        }
        if (ignoreWidth) {
            pattern = narrow(pattern);
            s = narrow(s);
        }
        return s.indexOf(pattern);
    }

    public static String narrow(String s) {
        if (FP.empty(s))
            return "";
        char[] cs = s.toCharArray();
        for (int i = 0; i < cs.length; ++i)
            cs[i] = narrow(cs[i]);
        return new String(cs);
    }

    public static char narrow(char c) {
        int code = c;
        if (code >= 65281 && code <= 65373)// Interesting range
            return (char) (code - 65248); // Full-width to half-width
        else if (code == 12288) // Space
            return (char) (code - 12288 + 32);
        else if (code == 65377)
            return (char) (12290);
        else if (code == 12539)
            return (char) (183);
        else if (code == 8226)
            return (char) (183);
        else
            return c;
    }

    public static int ord(char c) {
        if ('a' <= c && c <= 'z')
            return (int) c;
        if ('A' <= c && c <= 'Z')
            return c - 'A' + 'a';
        return 0;
    }

    public static int compare(String x, String y) {
        return FP.ref(x).compareTo(FP.ref(y));
    }

    private final static int SHA1_LENGTH = 40; //SHA1 digest consists of 40 hex digits, total 160 bits

    public static String getHashIfPassIsPlainText(String password) {
        //if password is plain text, it's length will be shorter than SHA1_LENGTH
        if (!StringUtils.isNullOrEmpty(password) && password.length() < SHA1_LENGTH) {
            return sha1(password);
        } else {
            return password;
        }
    }

    public static String sha1(String str) {
        StringBuffer sb = new StringBuffer();
        try {
            java.security.MessageDigest sha1 = java.security.MessageDigest
                    .getInstance("SHA1");
            byte[] digest = sha1.digest(str.getBytes());
            sb.append(bytesToHexString(digest));
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.getMessage());
        }
        return sb.toString();
    }

    public static String bytesToHexString(byte[] bytes) {
        if (bytes == null) {
            return "";
        }
        StringBuffer sb = new StringBuffer();
        for (byte b : bytes) {
            int val = b & 0xff;
            if (val < 0x10) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString();
    }

    public static boolean isValidMobileNumber(String phone) {
        if (phone == null || phone.length() != 11 || !phone.startsWith("1"))
            return false;

        if (!isAllDigits(phone))
            return false;

        return true;
    }

    public static boolean isNameMatchMobilePattern(String name) {
        return (name != null && name.matches("1\\d{10}(y*|s*)"));
    }

    public static String getMobileFromName(String name) {
        Log.d(TAG, "mobile user name: " + name);
        if (name == null) {
            return "";
        }
        if (name.startsWith("1") && name.length() >= 11) {
            String mobile = name.substring(0, 11);
            if (isValidMobileNumber(mobile)) {
                return mobile;
            }
        }
        return "";
    }

    public static boolean isIpv4Addr(String addr) {
        return (addr != null && addr.matches("(\\d{1,3}\\.){3}\\d{1,3}"));
    }

    public static <A, B> String fromPair(Pair<A, B> p) {
        return new StringBuilder().append(p.first).append(":").append(p.second).toString();
    }

    public static <A, B> String join(CharSequence delim, List<Pair<A, B>> xs) {
        return TextUtils.join(delim, FP.map(new FP.UnaryFunc<String, Pair<A, B>>() {
            @Override
            public String apply(Pair<A, B> p) {
                return fromPair(p);
            }
        }, xs));
    }

    public static <E> String join(CharSequence delim, SparseArray<E> xs) {
        return join(delim, FP.toList(xs));
    }

    public static String join(CharSequence delim, SparseIntArray xs) {
        return join(delim, FP.toList(xs));
    }

    public static boolean isValidEmail(String email) {
        String email_reg = "[a-zA-Z0-9_-]+@\\w+\\.[a-z]+(\\.[a-z]+)?";
        return email.matches(email_reg);
    }
}
