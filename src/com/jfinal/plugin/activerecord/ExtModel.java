package com.jfinal.plugin.activerecord;

import static com.jfinal.plugin.activerecord.DbKit.NULL_PARA_ARRAY;

import com.jfinal.plugin.activerecord.cache.ICache;

@SuppressWarnings({"serial", "rawtypes"})
public class ExtModel<M extends Model> extends Model<M> {

	public M findByIdCache(String cacheName, Object key, Object id, String columns) {
		ICache cache = DbKit.getCache();
		M model = cache.get(cacheName, key);
		if (model == null) {
			model = findById(id, columns);
			if (model != null) {
				cache.put(cacheName, key, model);
			}
		}
		return model;
	}

	public M findByIdCache(String cacheName, Object key, Object id) {
		return findByIdCache(cacheName, key, id, "*");
	}

	public <T> T findColumn(String sql, Object... paras) {
		return Db.queryColumn(sql, paras);
	}

	public <T> T findColumnCache(String cacheName, Object key, String sql, Object... paras) {
		ICache cache = DbKit.getCache();
		T temp = cache.get(cacheName, key);
		if (temp == null) {
			temp = findColumn(sql, paras);
			if (temp != null) {
				cache.put(cacheName, key, temp);
			}
		}
		return temp;
	}

	public M findFirstCache(String cacheName, Object key, String sql, Object... paras) {
		ICache cache = DbKit.getCache();
		M model = cache.get(cacheName, key);
		if (model == null) {
			model = findFirst(sql, paras);
			if (model != null) {
				cache.put(cacheName, key, model);
			}
		}
		return model;
	}

	public M findFirstCache(String cacheName, Object key, String sql) {
		return findFirstCache(cacheName, key, sql, NULL_PARA_ARRAY);
	}
}
