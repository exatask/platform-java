package com.exatask.platform.mariadb.replicas;

import com.exatask.platform.mariadb.exceptions.InvalidTransactionException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class ReplicaTransactionManager implements PlatformTransactionManager {

    private final PlatformTransactionManager platformTransactionManager;

    public ReplicaTransactionManager(PlatformTransactionManager platformTransactionManager) {
        this.platformTransactionManager = platformTransactionManager;
    }

    @Override
    public TransactionStatus getTransaction(TransactionDefinition definition) throws TransactionException {

        boolean isTransactionActive = TransactionSynchronizationManager.isActualTransactionActive();
        if (isTransactionActive && ReplicaDataSource.isReadOnly() && !definition.isReadOnly()) {
            throw new InvalidTransactionException();
        }

        if (!isTransactionActive) {
            ReplicaDataSource.setReadOnly(definition.isReadOnly());
        }

        return platformTransactionManager.getTransaction(definition);
    }

    @Override
    public void commit(TransactionStatus status) throws TransactionException {
        platformTransactionManager.commit(status);
    }

    @Override
    public void rollback(TransactionStatus status) throws TransactionException {
        platformTransactionManager.rollback(status);
    }
}
