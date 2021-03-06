package net.wuffy.core.database.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import net.wuffy.core.Core;
import net.wuffy.core.database.Storage;

public final class SQLStorage extends Storage<SQLStorage> {

	protected final SQLConfig config;

	private Connection connection;
	private SQLConnectionWrapper wrapperConnection;

	public SQLStorage(Core core, SQLConfig config) {
		super(core);
		if (!config.uri.startsWith("jdbc:mysql://"))
			throw new IllegalArgumentException("Invalid mysql connection uri");
		this.config = config;
	}

	@Override
	protected void connect() {
		try {
			Class.forName(this.config.driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			this.connection = DriverManager.getConnection(this.config.uri);
			this.wrapperConnection = new SQLConnectionWrapper(this.connection);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isOpen() {
		try {
			return connection != null && !connection.isClosed();
		} catch (SQLException e) {
			return false;
		}
	}

	@Override
	protected void disconnect() {
		if (this.isOpen()) {
			try {
				this.connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public Connection getConnection() {
		return this.wrapperConnection;
	}

	public String getPrefix() {
		return this.config.prefix;
	}
}