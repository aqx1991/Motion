package com.james.motion.db;

import com.james.motion.commmon.bean.SportMotionRecord;
import com.james.motion.commmon.bean.UserAccount;

import java.util.List;

public class DataManager implements DBHelper {

    private RealmHelper realmHelper;

    private DataManager() {

    }

    public DataManager(RealmHelper helper) {
        realmHelper = helper;
    }

    @Override
    public void insertSportRecord(SportMotionRecord record) {
        realmHelper.insertSportRecord(record);
    }

    @Override
    public void deleteSportRecord(SportMotionRecord record) {
        realmHelper.deleteSportRecord(record);
    }

    @Override
    public void deleteSportRecord() {
        realmHelper.deleteSportRecord();
    }

    @Override
    public List<SportMotionRecord> queryRecordList(int master) {
        return realmHelper.queryRecordList(master);
    }

    @Override
    public List<SportMotionRecord> queryRecordList(int master, String dateTag) {
        return realmHelper.queryRecordList(master, dateTag);
    }

    @Override
    public List<SportMotionRecord> queryRecordList() {
        return realmHelper.queryRecordList();
    }

    @Override
    public SportMotionRecord queryRecord(int master, long startTime, long endTime) {
        return realmHelper.queryRecord(master, startTime, endTime);
    }

    @Override
    public SportMotionRecord queryRecord(int master, String dateTag) {
        return realmHelper.queryRecord(master, dateTag);
    }

    @Override
    public void closeRealm() {
        realmHelper.closeRealm();
    }

    @Override
    public void insertAccount(UserAccount account) {
        realmHelper.insertAccount(account);
    }

    @Override
    public UserAccount queryAccount(String account) {
        return realmHelper.queryAccount(account);
    }

    @Override
    public boolean checkAccount(String account, String psd) {
        return realmHelper.checkAccount(account, psd);
    }

    @Override
    public boolean checkAccount(String account) {
        return realmHelper.checkAccount(account);
    }

    @Override
    public List<UserAccount> queryAccountList() {
        return realmHelper.queryAccountList();
    }
}
