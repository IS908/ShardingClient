package com.sharding.client.router.util;

/**
 * @Description: 字符串匹配识别SQL类型
 * @Auther: kun
 * @Date: 2019-04-02 22:36
 */
public class SQLRecognizer {
    private SQLRecognizer() {
    }

    public static final int INSERT = 11;
    public static final int DELETE = 12;
    public static final int UPDATE = 13;
    public static final int SELECT_FOR_UPDATE = 21;
    public static final int SELECT_INTO = 22;

    // / 大于等于 100 的直接路由到 DDF 执行
    public static final int SELECT = 99;

    public static final int TRUNCATE = 102;
    public static final int EXPLAIN = 103;

    public static final int CREATE = 104;
    public static final int ALTER = 105;
    public static final int DROP = 106;
    public static final int SET = 107;

    public static final int GRANT = 108;
    public static final int REVOKE = 108;
    public static final int SHOW = 108;
    public static final int OTHER = 108;
    public static final int WARN = 108;

    public static final int _FOR = 1001;
    public static final int _UPDATE = 1002;
    public static final int _INTO = 1003;

    public static boolean needRouter(int sqlType) {
        return sqlType < 100;
    }

    public static boolean needRouter(String sql) {
        int sqlType = parse(sql);
        return needRouter(sqlType);
    }

    /**
     * 通过字符串匹配初步识别SQL类型
     *
     * @param sql
     * @return SQL类型编号
     */
    public static int parse(String sql) {
        int rs = OTHER;
        // TODO: 识别 insert、delete、update、select 等语句类型
        return rs;
    }
}
