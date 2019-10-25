package com.james.motion.commmon.utils;

import android.text.TextUtils;

import com.james.motion.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 描述: 工具类
 * 作者: james
 * 日期: 2018-09-18 15:03
 * 类名: TabUtils
 */

public class Utils {

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188、170
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    添加新号段:第二位新增4、6、7、9
    */
        String num = "[1][3456789]\\d{9}";//"[1]"代表第1位为数字1，"[3578]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (!isString(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 手机号用****号隐藏前面中间数字
     */
    public static String settingPhone(String phone) {
        String phones = phone.substring(0, 3) + "****" + phone.substring(7);
        return phones;
    }

    /**
     * 验证邮箱格式
     */
    public static boolean isEmail(String email) {
        String emailMatch = "^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$";
        if (TextUtils.isEmpty(email)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return email.matches(emailMatch);
        }
    }

    /**
     * 判断是否为整数
     *
     * @param str 传入的字符串
     * @return 是整数返回true, 否则返回false
     */
    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    /**
     * 邮箱用****号隐藏前面的字母
     */
    public static String settingEmail(String email) {
        String emails = email.replaceAll("(\\w?)(\\w+)(\\w)(@\\w+\\.[a-z]+(\\.[a-z]+)?)", "$1****$3$4");
        return emails;
    }

    /**
     * 验证身份证号是否符合规则
     *
     * @param IDNumber 身份证号
     */
    public static boolean personIdValidation(String IDNumber) {

        if (!isString(IDNumber))
            return false;
        // 定义判别用户身份证号的正则表达式（15位或者18位，最后一位可以为字母）
        String regularExpression = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
                "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";
        //假设18位身份证号码:41000119910101123X  410001 19910101 123X
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //(18|19|20)                19（现阶段可能取值范围18xx-20xx年）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十七位奇数代表男，偶数代表女）
        //[0-9Xx] 0123456789Xx其中的一个 X（第十八位为校验值）
        //$结尾

        //假设15位身份证号码:410001910101123  410001 910101 123
        //^开头
        //[1-9] 第一位1-9中的一个      4
        //\\d{5} 五位数字           10001（前六位省市县地区）
        //\\d{2}                    91（年份）
        //((0[1-9])|(10|11|12))     01（月份）
        //(([0-2][1-9])|10|20|30|31)01（日期）
        //\\d{3} 三位数字            123（第十五位奇数代表男，偶数代表女），15位身份证不含X
        //$结尾
        return IDNumber.matches(regularExpression);
    }

    /**
     * 把Runnable 方法提交到主线程运行
     */
    public static void runOnUiThread(Runnable runnable) {
        // 在主线程运行
        if (android.os.Process.myTid() == android.os.Process.myTid()) {
            runnable.run();
        } else {
            //获取handler
            MyApplication.getHandler().post(runnable);
        }
    }

    /**
     * 判断s字符串是否为空
     */
    public static String isEmpry(String s) {
        if (null == s) {
            return "";
        } else if (s.equals("")) {
            return "";
        } else if (s.equals("null")) {
            return "";
        } else {
            return s;
        }
    }

    /**
     * 判断s字符串是String
     */
    public static boolean isString(String s) {
        if (null == s) {
            return false;
        } else if (s.equals("")) {
            return false;
        } else if (s.equals("null")) {
            return false;
        } else return s.length() > 0;
    }

    /**
     * 判断s字符串是String
     */
    public static boolean isString(String... s) {
        if (s.length <= 0)
            return false;
        for (String str : s) {
            if (null == str) {
                return false;
            } else if (str.equals("")) {
                return false;
            } else if (str.equals("null")) {
                return false;
            } else if (str.length() <= 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * 从字符串中提取数字
     */
    public static String getStrNum(String str) {
        if (isString(str)) {
            String regEx = "[^0-9]";
            Pattern p = Pattern.compile(regEx);
            Matcher m = p.matcher(str);
            return m.replaceAll("").trim();
        } else {
            return "";
        }
    }

    /**
     * 获取Realm数据库64位秘钥
     *
     * @param key 秘钥字符
     * @return 秘钥
     */
    public static byte[] getRealmKey(String key) {
        StringBuilder newKey = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            newKey.append(key);
        }
        return newKey.toString().getBytes();
    }

}
