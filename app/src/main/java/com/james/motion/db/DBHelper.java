package com.james.motion.db;

import com.james.motion.commmon.bean.SportMotionRecord;
import com.james.motion.commmon.bean.UserAccount;

import java.util.List;

public interface DBHelper {

    /**
     * 增加 运动数据
     *
     * @param record 运动数据
     */
    void insertSportRecord(SportMotionRecord record);

    /**
     * 删除 运动数据
     *
     * @param record 运动数据
     */
    void deleteSportRecord(SportMotionRecord record);

    /**
     * 删除 全部运动数据
     */
    void deleteSportRecord();

    /**
     * 获取 某人的运动数据
     *
     * @param master 登陆者
     */
    List<SportMotionRecord> queryRecordList(int master);

    /**
     * 获取 某人的运动数据
     *
     * @param master  登陆者
     * @param dateTag 时间标记
     */
    List<SportMotionRecord> queryRecordList(int master, String dateTag);

    /**
     * 获取 全部的运动数据
     */
    List<SportMotionRecord> queryRecordList();

    /**
     * 获取 运动数据
     *
     * @param master    登陆者
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    SportMotionRecord queryRecord(int master, long startTime, long endTime);

    /**
     * 获取 运动数据
     *
     * @param master  登陆者
     * @param dateTag 时间标记
     */
    SportMotionRecord queryRecord(int master, String dateTag);

    /**
     * 关闭数据库
     */
    void closeRealm();

    /**
     * 增加 账号
     *
     * @param account 账号
     */
    void insertAccount(UserAccount account);

    /**
     * 获取 账号
     *
     * @param account 账号
     */
    UserAccount queryAccount(String account);

    /**
     * 查詢 账号
     *
     * @param account 账号
     * @param psd     密码
     */
    boolean checkAccount(String account, String psd);

    /**
     * 查詢 账号
     *
     * @param account 账号
     */
    boolean checkAccount(String account);

    /**
     * 获取 全部的账号
     */
    List<UserAccount> queryAccountList();

}
