package com.team11.store.config;

import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.identity.IdentityColumnSupportImpl;
import org.hibernate.dialect.identity.IdentityColumnSupport;

import java.sql.Types;

public class SQLiteDialect extends Dialect {
    public SQLiteDialect() {
        registerColumnType(Types.BIT, "boolean");
        registerColumnType(Types.INTEGER, "integer");
        registerColumnType(Types.FLOAT, "float");
        registerColumnType(Types.DOUBLE, "double");
        registerColumnType(Types.VARCHAR, "varchar");
        registerColumnType(Types.BLOB, "blob");
    }

    @Override
    public IdentityColumnSupport getIdentityColumnSupport() {
        return new IdentityColumnSupportImpl();
    }

    @Override
    public boolean hasAlterTable() {
        return false;
    }

    @Override
    public boolean dropConstraints() {
        return false;
    }

    @Override
    public String getAddColumnString() {
        return "add column";
    }
}
