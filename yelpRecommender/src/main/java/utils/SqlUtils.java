package utils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SqlUtils {

	public static long getGeneratedIdFromResultSet(ResultSet resultSet)
			throws SQLException {
		long orderId = -1;
		while (resultSet != null && resultSet.next()) {
			orderId = resultSet.getLong(1);
		}
		return orderId;
	}

	public static void rollbackTransaction(Connection dbConnection) {
		if (dbConnection != null) {
			try {
				dbConnection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeDbConnection(Connection dbConnection) {
		try {
			if (dbConnection != null && !dbConnection.isClosed()) {
				dbConnection.close();
			}
			dbConnection = null;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}