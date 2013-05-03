package com.jfinal.plugin.activerecord;

import static com.jfinal.plugin.activerecord.DbKit.NULL_PARA_ARRAY;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import com.jfinal.plugin.activerecord.cache.ICache;
import com.jfinal.plugin.activerecord.dialect.Dialect;

@SuppressWarnings("rawtypes")
public class ExtDb extends Db {

	public static Record findFirst(DataSource dataSource, String sql, Object... paras) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			List<Record> result = find(conn, sql, paras);
			return result.size() > 0 ? result.get(0) : null;
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeIgnoreThreadLocal(conn);
		}
	}

	public static Record findFirstCache(String cacheName, Object key, DataSource dataSource, String sql, Object... paras) {
		ICache cache = DbKit.getCache();
		Record record = cache.get(cacheName, key);
		if (record == null) {
			record = findFirst(dataSource, sql, paras);
			cache.put(cacheName, key, record);
		}
		return record;
	}

	public static List<Record> findByCache(String cacheName, Object key, DataSource dataSource, String sql, Object... paras) {
		ICache cache = DbKit.getCache();
		List<Record> result = cache.get(cacheName, key);
		if (result == null) {
			result = find(dataSource, sql, paras);
			cache.put(cacheName, key, result);
		}
		return result;
	}

	static Page<Record> paginate(Connection conn, Dialect dialect, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) throws SQLException {
		if (pageNumber < 1 || pageSize < 1)
			throw new ActiveRecordException("pageNumber and pageSize must be more than 0");

		if (dialect.isTakeOverDbPaginate())
			return dialect.takeOverDbPaginate(conn, pageNumber, pageSize, select, sqlExceptSelect, paras);

		long totalRow = 0;
		int totalPage = 0;
		List result = query(conn, "select count(*) " + DbKit.replaceFormatSqlOrderBy(sqlExceptSelect), paras);
		int size = result.size();
		if (size == 1)
			totalRow = ((Number) result.get(0)).longValue();
		else if (size > 1)
			totalRow = result.size();
		else
			return new Page<Record>(new ArrayList<Record>(0), pageNumber, pageSize, 0, 0);

		totalPage = (int) (totalRow / pageSize);
		if (totalRow % pageSize != 0) {
			totalPage++;
		}

		// --------
		StringBuilder sql = new StringBuilder();
		dialect.forPaginate(sql, pageNumber, pageSize, select, sqlExceptSelect);
		List<Record> list = find(conn, sql.toString(), paras);
		return new Page<Record>(list, pageNumber, pageSize, totalPage, (int) totalRow);
	}

	public static Page<Record> paginate(Dialect dialect, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
		Connection conn = null;
		try {
			conn = DbKit.getConnection();
			return paginate(conn, dialect, pageNumber, pageSize, select, sqlExceptSelect, paras);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.close(conn);
		}
	}

	public static Page<Record> paginate(DataSource dataSource, Dialect dialect, int pageNumber, int pageSize, String select, String sqlExceptSelect, Object... paras) {
		Connection conn = null;
		try {
			conn = dataSource.getConnection();
			return paginate(conn, dialect, pageNumber, pageSize, select, sqlExceptSelect, paras);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			DbKit.closeIgnoreThreadLocal(conn);
		}
	}

	public static Page<Record> paginate(DataSource dataSource, Dialect dialect, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
		return paginate(dataSource, dialect, pageNumber, pageSize, select, sqlExceptSelect, NULL_PARA_ARRAY);
	}

	private static int[] batch(Connection conn, List<String> sqlList, List<Object[]> paraList) throws SQLException {
		if (sqlList == null || sqlList.size() == 0)
			throw new IllegalArgumentException("The sqlList length must more than 0.");
		if (paraList == null || paraList.size() == 0)
			throw new IllegalArgumentException("The paras length must more than 0.");
		if (sqlList.size() != paraList.size())
			throw new IllegalArgumentException("The sqlList length must equal with paras length.");

		int size = sqlList.size();
		int[] result = new int[size];
		PreparedStatement pst = null;
		for (int i = 0; i < size; i++) {
			pst = conn.prepareStatement(sqlList.get(i));
			// DbKit.dialect.fillStatement(pst, paras);
			Object[] paras = paraList.get(i);
			for (int j = 0; j < paras.length; j++) {
				pst.setObject(j + 1, paras[j]);
			}
			result[i] = pst.executeUpdate();
		}
		DbKit.closeQuietly(pst);
		conn.commit();
		return result;
	}

	public static int[] batch(List<String> sqlList, List<Object[]> paraList) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = DbKit.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sqlList, paraList);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.closeIgnoreThreadLocal(conn);
		}
	}

	public static int[] batch(DataSource dataSource, List<String> sqlList, List<Object[]> paraList) {
		Connection conn = null;
		Boolean autoCommit = null;
		try {
			conn = dataSource.getConnection();
			autoCommit = conn.getAutoCommit();
			conn.setAutoCommit(false);
			return batch(conn, sqlList, paraList);
		} catch (Exception e) {
			throw new ActiveRecordException(e);
		} finally {
			if (autoCommit != null)
				try {
					conn.setAutoCommit(autoCommit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			DbKit.closeIgnoreThreadLocal(conn);
		}
	}
}
