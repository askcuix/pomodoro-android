package io.askcuix.pomodoro.model;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;

import io.askcuix.pomodoro.util.CipherHelper;

/**
 * Created by Chris on 15/11/30.
 */
public class LoginInfo {

    public static int PhoneLoginType = -100;
    private final String accountKey = "NewAccount";
    private final String passwordKey = "NewPassword";
    private final String typeKey = "NewAccountType";
    private final String uidKey = "NewUid";
    private final String nickKey = "Nick";
    private final String countryCodeKey = "CountryCode";
    private final String testKey = "Test";
    private final String scoreKey = "Score";
    private final String isInternationalKey = "isInternational";
    private final String accountFile = "PomodoroAccount";
    private final String internationalAccountFile = "PomodoroInternationalAccount";
    private final String internationalConfigFile = "PomodoroInternationalConfig";
    private final String haveShowTwoVersionDiffTipKey = "haveShowTwoVersionDiffTip";
    private final CipherHelper mCipher = new CipherHelper();

    private String account = "";
    private String password = "";
    private boolean isLogin = false;
    private String nick = "";
    private long uid;
    private int loginType; //phone、sina、qq、weixin
    private String token = "";
    private String countryCode = "86";
    private float score;
    private int test;

    public void init(Context context) {
        loadLastAccountInfo(context);
    }

    public void loadLastAccountInfo(Context context) {
        SharedPreferences sp = context.getSharedPreferences(accountFile, Context.MODE_PRIVATE);

        if (sp != null) {
            loginType = mCipher.decryptInt(sp.getString(typeKey, ""), PhoneLoginType);
            uid = mCipher.decryptLong(sp.getString(uidKey, ""), 0);
            nick = mCipher.decryptString(sp.getString(nickKey, ""));
            account = mCipher.decryptString(sp.getString(accountKey, ""));
            password = mCipher.decryptString(sp.getString(passwordKey, ""));
            countryCode = mCipher.decryptString(sp.getString(countryCodeKey, ""));
            score = sp.getFloat(scoreKey, 0.0f);
            if (countryCode == null || countryCode.length() == 0) {
                countryCode = "86";
            }

            if (uid != 0) {
                isLogin = true;
            }
        }
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getLoginType() {
        return loginType;
    }

    public void setLoginType(int loginType) {
        this.loginType = loginType;
    }

    public void setIsLogin(boolean login) {
        this.isLogin = login;
    }

    public boolean isLogin() {
        return isLogin;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String mNick) {
        if (mNick == null || mNick.length() == 0) {
            return;
        }
        this.nick = mNick;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCountryCode() {
        if (countryCode == null || countryCode.length() == 0) {
            countryCode = "86";
        }
        return countryCode;
    }

    public void setCountryCode(String countCode) {
        this.countryCode = countCode;
    }

    public float getscore() {
        BigDecimal b = new BigDecimal(score);
        score = b.setScale(1, BigDecimal.ROUND_HALF_UP).floatValue();
        return score;
    }

    public void setscore(float score) {
        this.score = score;
    }

    public void setTest(int test) {
        this.test = test;
    }

    public int getTest() {
        return test;
    }

    public void saveAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(accountFile, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();
        editor.putString(typeKey, mCipher.encryptInt(loginType));
        editor.putString(uidKey, mCipher.encryptLong(uid));
        editor.putString(nickKey, mCipher.encryptString(nick));
        editor.putFloat(scoreKey, score);
        editor.putInt(testKey, test);
        if (loginType == PhoneLoginType) {
            editor.putString(accountKey, mCipher.encryptString(account));
            editor.putString(passwordKey, mCipher.encryptString((password)));
            editor.putString(countryCodeKey, mCipher.encryptString(getCountryCode()));
        }
        editor.commit();
    }

    public void reset(Context context) {
        isLogin = false;
        uid = 0;
        saveAccount(context);
    }
}
