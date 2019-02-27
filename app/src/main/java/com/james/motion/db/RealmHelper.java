package com.james.motion.db;

import com.james.motion.commmon.bean.SportMotionRecord;
import com.james.motion.commmon.bean.UserAccount;
import com.james.motion.commmon.utils.Utils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class RealmHelper implements DBHelper {

    private static final String DB_SPORT = "sport_motion.realm";//数据库名
    private static final String DB_ACCOUNT = "account.realm";//数据库名
    private static final String DB_KEY = "motion@james.com";//秘钥

    private Realm sportRealm;
    private Realm accountRealm;

    public RealmHelper() {
        if (null == sportRealm)
            sportRealm = Realm.getInstance(new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库，开发时候打开
                    .schemaVersion(2)//指定数据库的版本号
//                .inMemory()// 声明数据库只在内存中持久化
                    .name(DB_SPORT)//指定数据库的名称
//                    .migration(Class<T>)//指定迁移操作的迁移类。
                    .encryptionKey(Utils.getRealmKey(DB_KEY))//指定数据库的密钥
                    .build());

        if (null == accountRealm)
            accountRealm = Realm.getInstance(new RealmConfiguration.Builder()
                    .deleteRealmIfMigrationNeeded()//声明版本冲突时自动删除原数据库，开发时候打开
                    .schemaVersion(1)//指定数据库的版本号
//                .inMemory()// 声明数据库只在内存中持久化
                    .name(DB_ACCOUNT)//指定数据库的名称
//                    .migration(Class<T>)//指定迁移操作的迁移类。
                    .encryptionKey(Utils.getRealmKey(DB_KEY))//指定数据库的密钥
                    .build());
    }

    @Override
    public void insertSportRecord(SportMotionRecord record) {
        /**
         * 当Model中存在主键的时候，推荐使用copyToRealmOrUpdate方法插入数据。
         * 如果对象存在，就更新该对象；反之，它会创建一个新的对象。
         */
        //同步操作：使用copyToRealmOrUpdate方法插入数据
        sportRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(record));

        //异步操作，可添加监听
//        sportRealm.executeTransactionAsync(realm -> {
//            realm.copyToRealmOrUpdate(record);
//        }, () -> {
//
//        }, error -> {
//
//        });
    }

    @Override
    public void deleteSportRecord(SportMotionRecord record) {
        if (record != null) {
            //同步删除
            sportRealm.executeTransaction(realm -> record.deleteFromRealm());
        }
    }

    @Override
    public void deleteSportRecord() {
        //同步删除：deleteAll()
        sportRealm.executeTransaction(realm -> realm.deleteAll());
        //异步删除：deleteAll() 需要处理realmAsyncTask => realmAsyncTask.cancel(); 避免内存泄漏
        //删除所有数据库
//        RealmAsyncTask realmAsyncTask = sportRealm.executeTransactionAsync(realm -> realm.deleteAll(), () -> {
//            //删除成功
//        }, error -> {
//            //删除失败
//        });
    }

    @Override
    public List<SportMotionRecord> queryRecordList(int master) {
        //同步查询  异步查询方法=>findAllAsync() 异步操作可添加监听
        RealmResults<SportMotionRecord> results = sportRealm.where(SportMotionRecord.class).equalTo("master", master).findAll();
        return sportRealm.copyFromRealm(results);
    }

    @Override
    public List<SportMotionRecord> queryRecordList(int master, String dateTag) {
        RealmResults<SportMotionRecord> results = sportRealm.where(SportMotionRecord.class)
                .equalTo("master", master)
                .equalTo("dateTag", dateTag)
                .findAll();
        return results;
    }

    @Override
    public List<SportMotionRecord> queryRecordList() {
        RealmResults<SportMotionRecord> results = sportRealm.where(SportMotionRecord.class).findAll();
        return sportRealm.copyFromRealm(results);
    }

    @Override
    public SportMotionRecord queryRecord(int master, long startTime, long endTime) {
        return sportRealm.where(SportMotionRecord.class)
                .equalTo("master", master)
                .equalTo("mStartTime", startTime)
                .equalTo("mEndTime", endTime)
                .findFirst();
    }

    @Override
    public SportMotionRecord queryRecord(int master, String dateTag) {
        return sportRealm.where(SportMotionRecord.class)
                .equalTo("master", master)
                .equalTo("dateTag", dateTag)
                .findFirst();
    }

    @Override
    public void closeRealm() {
        if (null != sportRealm && !sportRealm.isClosed())
            sportRealm.close();
        if (null != accountRealm && !accountRealm.isClosed())
            accountRealm.close();
    }

    @Override
    public void insertAccount(UserAccount account) {
        accountRealm.executeTransaction(realm -> realm.copyToRealmOrUpdate(account));
    }

    @Override
    public UserAccount queryAccount(String account) {
        return accountRealm.where(UserAccount.class)
                .equalTo("account", account)
                .findFirst();
    }

    @Override
    public boolean checkAccount(String account, String psd) {
        return accountRealm.where(UserAccount.class)
                .equalTo("account", account)
                .equalTo("psd", psd)
                .findFirst() != null;
    }

    @Override
    public boolean checkAccount(String account) {
        return accountRealm.where(UserAccount.class)
                .equalTo("account", account)
                .findFirst() != null;
    }

    @Override
    public List<UserAccount> queryAccountList() {
        RealmResults<UserAccount> results = accountRealm.where(UserAccount.class).findAll();
        return accountRealm.copyFromRealm(results);
    }

    /**
     * 描述:数据库查询相关操作介绍
     * sum()：对指定字段求和。
     *  average()：对指定字段求平均值。
     *  min(): 对指定字段求最小值。
     *  max() : 对指定字段求最大值。count : 求结果集的记录数量。
     *  findAll(): 返回结果集所有字段，返回值为RealmResults队列
     *  findAllSorted() : 排序返回结果集所有字段，返回值为RealmResults队列
     *  between(), greaterThan(),lessThan(), greaterThanOrEqualTo() & lessThanOrEqualTo()
     *   equalTo() & notEqualTo()
     *  contains(), beginsWith() & endsWith()
     *   isNull() & isNotNull()
     *    isEmpty()& isNotEmpty()
     */
}
