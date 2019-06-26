package com.sharding.client.router.parser;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.sqlserver.visitor.SQLServerSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.sharding.client.config.model.TableConfig;
import com.sharding.client.router.util.ColumnRoutePair;
import com.sharding.client.router.util.RangeValue;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DefaultDruidParser
 *
 * @author lixch
 */
public abstract class DefaultDruidParser implements DruidParser {
    protected List<ColumnRoutePair> routePairs;

    public void visitorParse(SQLStatement stmt, TableConfig tc) throws SQLException {
        SQLServerSchemaStatVisitor visitor = new SQLServerSchemaStatVisitor();
        stmt.accept(visitor);
        routePairs = buildRouteCalculateUnits(visitor.getConditions(), tc);
    }

    private List<ColumnRoutePair> buildRouteCalculateUnits(List<TableStat.Condition> conditionList, TableConfig tc) {
        List<ColumnRoutePair> retList = new ArrayList<>();
        String shardingColumn = tc.getRule().getColumn();
        //find partition column in condition
        for (TableStat.Condition condition : conditionList) {
            if (!condition.getColumn().getName().equalsIgnoreCase(shardingColumn)) continue;
            List<Object> values = condition.getValues();
            if (values.size() == 0) {
                continue;
            }
            if (checkConditionValues(values)) {
                String operator = condition.getOperator();

                //execute only between ,in and =
                if (operator.equals("between")) {
                    RangeValue rv = new RangeValue(values.get(0), values.get(1), RangeValue.EE);
                    retList.add(new ColumnRoutePair((RangeValue) values));
                } else if (operator.equals("=") || operator.toLowerCase().equals("in")) {
                    for (Object value : values) {
                        retList.add(new ColumnRoutePair(value.toString()));
                    }
                }
            }
        }
        return retList;
    }

    private boolean checkConditionValues(List<Object> values) {
        for (Object value : values) {
            if (value != null && !value.toString().equals("")) {
                return true;
            }
        }
        return false;
    }
}
