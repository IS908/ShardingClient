package com.sharding.client.jdbc.unsupported;

import com.sharding.client.jdbc.proxy.BaseDDFProxy;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * Unsupported {@code Datasource} methods.
 *
 * @author zhangliang
 */
public abstract class AbstractUnsupportedOperationDataSource extends BaseDDFProxy implements DataSource {

    @Override
    public final int getLoginTimeout() throws SQLException {
        throw new SQLFeatureNotSupportedException("unsupported getLoginTimeout()");
    }

    @Override
    public final void setLoginTimeout(final int seconds) throws SQLException {
        throw new SQLFeatureNotSupportedException("unsupported setLoginTimeout(int seconds)");
    }
}
