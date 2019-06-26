package com.sharding.client.jdbc.util;

/**
 * @Description: 常量在此定义
 * @Auther: kun
 * @Date: 2019-04-03 09:49
 */
public interface Constants {

    String ddfStatementKey = "ddfStatementKey";

    // Connection Attributes
    String auto_commit = "ddfAutoCommit";
    String holdability = "ddfHoldability";
    String readonly = "ddfReadOnly";
    String transaction_isolation = "ddfTransactionIsolation";

    // Statement Attributes
    String close_on_completion = "ddfCloseOnCompletion";
    String max_field_size = "ddfMaxFieldSize";
    String max_rows = "ddfMaxRows";
    String escape_processing = "ddfEscapeProcessing";
    String query_timeout = "ddfQueryTimeout";
    String cursor_name = "ddfCursorName";
    String fetch_direction = "ddfFetchDirection";
    String fetch_size = "ddfFetchSize";
    String result_set_concurrency = "ddfResultSetConcurrency";
    String result_set_type = "ddfResultSetType";
    String result_set_holdability = "ddfResultSetHoldability";
    String poolable = "ddfPoolable";

}
