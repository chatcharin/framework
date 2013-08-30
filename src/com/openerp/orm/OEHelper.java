/*
 * OpenERP, Open Source Management Solution
 * Copyright (C) 2012-today OpenERP SA (<http:www.openerp.com>)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:www.gnu.org/licenses/>
 * 
 */
package com.openerp.orm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

import com.openerp.support.JSONDataHelper;
import com.openerp.support.OEArgsHelper;
import com.openerp.support.OpenERPServerConnection;
import com.openerp.support.UserObject;
import com.openerp.support.listview.OEListViewRows;

// TODO: Auto-generated Javadoc
/**
 * The Class OEHelper.
 */
public class OEHelper extends OpenERP {

	/** The user context. */
	UserObject userContext = null;

	/** The m context. */
	Context mContext = null;

	HashMap<String, List<HashMap<String, Object>>> deleted_rows = new HashMap<String, List<HashMap<String, Object>>>();

	/**
	 * Instantiates a new oE helper.
	 * 
	 * @param context
	 *            the context
	 * @param base_url
	 *            the base_url
	 * @throws ClientProtocolException
	 *             the client protocol exception
	 * @throws JSONException
	 *             the jSON exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public OEHelper(Context context, String base_url)
			throws ClientProtocolException, JSONException, IOException {
		super(base_url);
		this.mContext = context;
		// TODO Auto-generated constructor stub
	}

	/**
	 * Instantiates a new oE helper.
	 * 
	 * @param context
	 *            the context
	 * @param data
	 *            the data
	 * @throws ClientProtocolException
	 *             the client protocol exception
	 * @throws JSONException
	 *             the jSON exception
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public OEHelper(Context context, UserObject data)
			throws ClientProtocolException, JSONException, IOException {
		super(data.getHost(), OpenERPServerConnection
				.isNetworkAvailable(context));
		this.mContext = context;
		// TODO Auto-generated constructor stub
		this.userContext = this.login(data.getUsername(), data.getPassword(),
				data.getDatabase(), data.getHost());

	}

	/**
	 * Gets the user context.
	 * 
	 * @return the user context
	 */
	public UserObject getUserContext() {
		return this.userContext;
	}

	/**
	 * Login.
	 * 
	 * @param username
	 *            the username
	 * @param password
	 *            the password
	 * @param database
	 *            the database
	 * @param serverURL
	 *            the server url
	 * @return the user object
	 */
	public UserObject login(String username, String password, String database,
			String serverURL) {
		UserObject userObj = null;
		JSONObject domain = new JSONObject();
		try {
			JSONObject response = this.authenticate(username, password,
					database);
			int userId = 0;
			if (response.getString("uid") != "false") {
				userId = response.getInt("uid");

				JSONObject fields = new JSONObject();
				fields.accumulate("fields", "partner_id");
				fields.accumulate("fields", "tz");
				fields.accumulate("fields", "image");
				domain.accumulate("domain", new JSONArray("[[\"id\",\"=\","
						+ String.valueOf(userId) + "]]"));
				JSONObject res = this
						.search_read("res.users", fields, domain, 0, 0, null,
								null).getJSONArray("records").getJSONObject(0);

				userObj = new UserObject();
				userObj.setAvatar(res.getString("image"));

				userObj.setDatabase(database);
				userObj.setHost(serverURL);
				userObj.setIsactive(true);
				userObj.setAndroidName(this.generateAndroidName(username,
						database));
				userObj.setPartner_id(res.getJSONArray("partner_id").getString(
						0));
				userObj.setTimezone(res.getString("tz"));
				userObj.setUser_id(String.valueOf(userId));
				userObj.setUsername(username);
				userObj.setPassword(password);

			}
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// e.printStackTrace();
			Toast.makeText(mContext, "No Connection to OpenERP Server ! ",
					Toast.LENGTH_LONG).show();
		}
		return userObj;
	}

	/**
	 * Generate android name.
	 * 
	 * @param username
	 *            the username
	 * @param database
	 *            the database
	 * @return the string
	 */
	public String generateAndroidName(String username, String database) {
		StringBuffer str = new StringBuffer();
		str.append(username);
		str.append("[");
		str.append(database);
		str.append("]");
		return str.toString();
	}

	/**
	 * Gets the all ids.
	 * 
	 * @param db
	 *            the db
	 * @return the all ids
	 */
	public int[] getAllIds(BaseDBHelper db) {
		int[] ids = null;
		HashMap<String, Object> result = db.search(db);

		if (Integer.parseInt(result.get("total").toString()) > 0) {
			List<HashMap<String, String>> records = (List<HashMap<String, String>>) result
					.get("records");
			ids = new int[Integer.parseInt(result.get("total").toString())];
			int i = 0;
			for (HashMap<String, String> row : records) {
				ids[i] = Integer.parseInt(row.get("id").toString());
				i++;
			}
		}
		return ids;
	}

	/**
	 * Find all name.
	 * 
	 * @param dbHelper
	 *            the db helper
	 * @return the list
	 * @throws NullPointerException
	 *             the null pointer exception
	 */
	public List<String> findAllName(BaseDBHelper dbHelper)
			throws NullPointerException {
		List<String> names = new ArrayList<String>();
		HashMap<String, Object> result = dbHelper.search(dbHelper);

		if (Integer.parseInt(result.get("total").toString()) > 0) {
			List<HashMap<String, String>> records = (List<HashMap<String, String>>) result
					.get("records");
			for (HashMap<String, String> row : records) {
				names.add(row.get("name").toString());
			}
		}
		return names;
	}

	/**
	 * Call server method.
	 * 
	 * @param db
	 *            the db
	 * @param method
	 *            the method
	 * @param arguments
	 *            the arguments
	 * @param values
	 *            the values
	 * @param ids
	 *            the ids
	 * @return true, if successful
	 */
	public boolean callServerMethod(BaseDBHelper db, String method,
			JSONArray arguments, ContentValues values, int[] ids) {
		boolean flag = false;
		try {
			call_kw(db.getModelName(), method, arguments);

			for (int id : ids) {
				flag = db.write(db, values, id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	/**
	 * Common method to Sync data with server.
	 * 
	 * It will take database helper and fetch data from server as given columns.
	 * If any column with many2many relation found it will automatic sync
	 * related table data.
	 * 
	 * @param db
	 *            the BaseDBHelper instance
	 * @return true, if successful
	 */
	public boolean syncWithServer(BaseDBHelper db) {
		deleted_rows = new HashMap<String, List<HashMap<String, Object>>>();
		boolean success = false;
		try {
			JSONObject result = getDataFromServer(db, null);
			if (result != null) {
				success = handleServerData(db, result);
			}
		} catch (Exception e) {
		}
		return success;
	}

	/**
	 * Handle server data.
	 * 
	 * @param db
	 *            the db
	 * @param res
	 *            the res
	 * @return true, if successful
	 */
	private boolean handleServerData(BaseDBHelper db, JSONObject res) {
		boolean success = false;
		HashMap<String, Object> m2oCols = db.getMany2OneColumns();
		HashMap<String, Object> m2mCols = db.getMany2ManyColumns();
		HashMap<String, List<Integer>> m2oColsIds = new HashMap<String, List<Integer>>();
		HashMap<String, List<Integer>> m2mColsIds = new HashMap<String, List<Integer>>();
		List<String> serverIds = new ArrayList<String>();
		try {
			int total = res.getInt("length");
			for (int i = 0; i < total; i++) {
				ContentValues values = new ContentValues();
				JSONObject row = res.getJSONArray("records").getJSONObject(i);
				int row_id = row.getInt("id");
				serverIds.add(String.valueOf(row_id));
				for (String key : db.columnListToStringArray(db.getColumns())) {
					if (row.has(key)) {
						// Handling many2one columns.
						if (m2oCols.containsKey(key)) {
							if (!row.getString(key).equals("false")) {
								int m2oid = Integer.parseInt(db
										.many2oneRecord(row.getJSONArray(key)));
								if (m2oColsIds.containsKey(key)) {
									m2oColsIds.get(key).add(m2oid);
									m2oColsIds.put(key, m2oColsIds.get(key));
								} else {
									List<Integer> list = new ArrayList<Integer>();
									list.add(m2oid);
									m2oColsIds.put(key, list);
								}
								// m2oIds.add(db.many2oneRecord(row
								// .getJSONArray(key)));
							}
						}
						// Handling many2manycolumns
						if (m2mCols.containsKey(key)) {
							int[] ref_ids = JSONDataHelper
									.jsonArrayTointArray(new JSONArray(row
											.getString(key)));
							m2mColsIds.put(key, intArrayToList(ref_ids));
						}

						values.put(key, row.getString(key));
					}

				}
				if (!db.hasRecord(db, row_id)) {
					int newId = db.create(db, values);
				} else {
					// Updating data of row
					db.write(db, values, row_id, true);
				}

			}

			// Handling many2One reference table. :)
			for (String key : m2oCols.keySet()) {
				if (m2oColsIds.containsKey(key)) {
					Many2One many2one = (Many2One) m2oCols.get(key);
					if (many2one.isM2OObject()) {
						syncReferenceTables(
								(BaseDBHelper) many2one.getM2OObject(),
								m2oColsIds.get(key));
					}
				}
			}

			// Handling many2many reference table. :)
			for (String key : m2mCols.keySet()) {
				if (m2mColsIds.containsKey(key)) {
					Many2Many many2many = (Many2Many) m2mCols.get(key);
					if (many2many.isM2MObject()) {
						syncReferenceTables(
								(BaseDBHelper) many2many.getM2mObject(),
								m2mColsIds.get(key));
					}
				}
			}

			/**
			 * Comparing server Ids with local id. If local id is not present in
			 * server Ids than deleting local record.
			 */
			List<HashMap<String, Object>> del_rows = new ArrayList<HashMap<String, Object>>();
			if (total > 1) {
				for (int id : db.localIds(db)) {
					if (serverIds.size() > 0) {
						if (!serverIds.contains(String.valueOf(id))) {
							// Delete record with id.
							HashMap<String, Object> del_row = db.search(db,
									new String[] { "id = ?" },
									new String[] { String.valueOf(id) });

							if (db.delete(db, id, true)) {
								del_rows.add(del_row);
							}

						}
					}

				}
			}

			deleted_rows.put(db.getModelName(), del_rows);

			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	public HashMap<String, List<HashMap<String, Object>>> getDeletedRows() {

		return deleted_rows;
	}

	/**
	 * Gets the data from server.
	 * 
	 * @param db
	 *            the db
	 * @param domain
	 *            the domain
	 * @return the data from server
	 */
	private JSONObject getDataFromServer(BaseDBHelper db, JSONObject domain) {
		JSONObject fields = getFieldsFromCols(getSyncCols(db.getServerColumns()));
		String model = db.getModelName();
		try {
			return search_read(model, fields, domain, 0, 0, null, null);
		} catch (Exception e) {
		}
		return null;

	}

	/**
	 * Gets the fields from cols.
	 * 
	 * @param cols
	 *            the cols
	 * @return the fields from cols
	 */
	private JSONObject getFieldsFromCols(String[] cols) {
		JSONObject fields = new JSONObject();
		for (String col : cols) {
			try {
				fields.accumulate("fields", col);
			} catch (Exception e) {
			}
		}
		return fields;
	}

	/**
	 * Gets the sync cols.
	 * 
	 * @param columns
	 *            the columns
	 * @return the sync cols
	 */
	private String[] getSyncCols(List<Fields> columns) {
		String[] fields = new String[columns.size()];
		int i = 0;
		for (Fields col : columns) {

			// if (!(col.getType() instanceof Many2Many)) {
			fields[i] = col.getName();
			i++;
			// }
		}
		return fields;
	}

	/**
	 * Sync reference tables.
	 * 
	 * @param db
	 *            the db
	 * @param list
	 *            the list
	 * @return true, if successful
	 */
	public boolean syncReferenceTables(BaseDBHelper db, List<Integer> list) {
		boolean success = false;
		try {
			OEArgsHelper ids = new OEArgsHelper();
			for (int id : list) {
				ids.addArg(id);
			}

			OEArgsHelper args = new OEArgsHelper();
			args.addArgCondition("id", "in", ids.getArgs());

			JSONObject domain = new JSONObject();
			domain.accumulate("domain", new JSONArray().put(args.getArgs()));

			JSONObject result = getDataFromServer(db, domain);
			if (result != null) {
				success = handleServerData(db, result);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return success;
	}

	/**
	 * Int array to list.
	 * 
	 * @param ids
	 *            the ids
	 * @return the list
	 */
	private List<Integer> intArrayToList(int[] ids) {
		List<Integer> list = new ArrayList<Integer>();
		for (int id : ids) {
			list.add(id);
		}
		return list;
	}

	/**
	 * Search_data from server.
	 * 
	 * @param db
	 *            the db
	 * @param domain
	 *            the domain
	 * @param offset
	 *            the offset
	 * @param limit
	 *            the limit
	 * @return the list
	 */
	public List<OEListViewRows> search_data(BaseDBHelper db, JSONObject domain,
			int offset, int limit) {
		List<OEListViewRows> record_lists = new ArrayList<OEListViewRows>();
		try {
			OEHelper oe = db.getOEInstance();
			JSONObject fields = fieldsToOEFields(db.getServerColumns());
			JSONObject result = oe.search_read(db.getModelName(), fields,
					domain, offset, limit, null, null);
			if (result.getJSONArray("records").length() > 0) {
				for (int i = 0; i < result.getJSONArray("records").length(); i++) {
					JSONObject row = result.getJSONArray("records")
							.getJSONObject(i);
					HashMap<String, Object> oe_datarow = jsonDataToMap(row);
					int row_id = row.getInt("id");

					OEListViewRows oe_row = new OEListViewRows(row_id,
							oe_datarow);
					record_lists.add(oe_row);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return record_lists;
	}

	/**
	 * Json data to hasmap object.
	 * 
	 * @param data
	 *            the data
	 * @return the hash map
	 */
	public HashMap<String, Object> jsonDataToMap(JSONObject data) {
		HashMap<String, Object> map_data = new HashMap<String, Object>();
		Iterator<String> keys = data.keys();
		try {
			while (keys.hasNext()) {
				String key = keys.next();
				map_data.put(key, data.get(key));
			}
		} catch (Exception e) {
		}

		return map_data;
	}

	/**
	 * Fields to JSONObject fields Accumulates fields.
	 * 
	 * @param fields
	 *            the fields
	 * @return the jSON object
	 */
	public JSONObject fieldsToOEFields(List<Fields> fields) {
		JSONObject oeFields = new JSONObject();
		try {
			for (Fields field : fields) {
				oeFields.accumulate("fields", field.getName());
			}
		} catch (Exception e) {
		}
		return oeFields;
	}
}
