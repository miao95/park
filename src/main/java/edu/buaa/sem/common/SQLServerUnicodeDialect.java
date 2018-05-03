package edu.buaa.sem.common;

import java.sql.Types;

import org.hibernate.dialect.SQLServerDialect;

public class SQLServerUnicodeDialect extends SQLServerDialect {
	public SQLServerUnicodeDialect() {
		super();
		this.registerColumnType(Types.CHAR, "nchar(1)");
		this.registerColumnType(Types.VARCHAR, "nvarchar($l)");
		this.registerColumnType(Types.VARCHAR, "nvarchar(max)");
		this.registerColumnType(Types.LONGVARCHAR, "ntext");
		this.registerHibernateType(-9, "string");

	}
}
