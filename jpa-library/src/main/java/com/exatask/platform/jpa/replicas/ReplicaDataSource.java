package com.exatask.platform.jpa.replicas;

import com.exatask.platform.jpa.utilities.TransactionUtility;
import com.exatask.platform.utilities.properties.DataSourceSqlProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.CollectionUtils;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

public class ReplicaDataSource extends AbstractRoutingDataSource {

    private static final ThreadLocal<String> currentDataSource = new ThreadLocal<>();

    private static int secondaryCount = 0;

    public ReplicaDataSource(DataSourceSqlProperties dataSourceSqlProperties) {

        Map<Object, Object> dataSources = new HashMap<>();

        DataSource primaryDataSource = TransactionUtility.getDataSource(dataSourceSqlProperties.getUrl(), dataSourceSqlProperties.getUsername(), dataSourceSqlProperties.getPassword(), dataSourceSqlProperties);
        dataSources.put(Type.PRIMARY, primaryDataSource);

        if (!CollectionUtils.isEmpty(dataSourceSqlProperties.getSecondaryHosts())) {
            for (DataSourceSqlProperties.SecondaryHost secondaryHost : dataSourceSqlProperties.getSecondaryHosts()) {

                DataSource secondaryDataSource = TransactionUtility.getDataSource(secondaryHost.getUrl(), secondaryHost.getUsername(), secondaryHost.getPassword(), dataSourceSqlProperties);
                dataSources.put(getSecondaryLookupKey(++secondaryCount), secondaryDataSource);
            }
        }

        super.setTargetDataSources(dataSources);
        super.setDefaultTargetDataSource(primaryDataSource);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return currentDataSource.get();
    }

    public static void setReadOnly(boolean isReadOnly) {

        if (isReadOnly && secondaryCount > 0) {
            int counter = (int) (System.currentTimeMillis() % secondaryCount);
            currentDataSource.set(getSecondaryLookupKey(counter));

        } else {
            currentDataSource.set(Type.PRIMARY.toString());
        }
    }

    public static boolean isReadOnly() {
        return currentDataSource.get().startsWith(Type.SECONDARY.toString());
    }

    private static String getSecondaryLookupKey(int counter) {
        return Type.SECONDARY + "_" + counter;
    }

    private enum Type {

        PRIMARY,
        SECONDARY
    }
}
