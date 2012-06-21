package com.chadwick.athleticfields;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class DataAccess {

	private class TeamInfo {
		private String Team;
		private String URL;
		public void setTeam(String team) {
			Team = team;
		}
		public String getTeam() {
			return Team;
		}
		public void setURL(String uRL) {
			URL = uRL;
		}
		public String getURL() {
			return URL;
		}
	}
	
   private static final String DATABASE_NAME = "Soccer.db";
   private static final int DATABASE_VERSION = 1;
   private static final String TABLE_NAME = "Team";

   private Context context;
   private SQLiteDatabase db;

   private SQLiteStatement insertStmt;
   private static final String INSERT = "insert into " + TABLE_NAME + "(TeamName, URL) values (?)";

   public DataAccess(Context context) {
      this.context = context;
      OpenHelper openHelper = new OpenHelper(this.context);
      this.db = openHelper.getWritableDatabase();
      this.insertStmt = this.db.compileStatement(INSERT);
   }

   public long insert(String name, String URL) {
      this.insertStmt.bindString(1, name);
      this.insertStmt.bindString(1, URL);
      return this.insertStmt.executeInsert();
   }

   public void deleteAll() {
      this.db.delete(TABLE_NAME, null, null);
   }

   public List<TeamInfo> selectAll() {
      List<TeamInfo> list = new ArrayList<TeamInfo>();
      Cursor cursor = this.db.query(TABLE_NAME, new String[] { "TeamName", "URL" }, 
        null, null, null, null, "name desc");
      if (cursor.moveToFirst()) {
         do {
        	 TeamInfo temp = new TeamInfo();
        	 temp.setTeam(cursor.getString(0));
        	 temp.setURL(cursor.getString(1));
        	 list.add(temp);
         } while (cursor.moveToNext());
      }
      if (cursor != null && !cursor.isClosed()) {
         cursor.close();
      }
      return list;
   }

   private static class OpenHelper extends SQLiteOpenHelper {

      OpenHelper(Context context) {
         super(context, DATABASE_NAME, null, DATABASE_VERSION);
      }

      @Override
      public void onCreate(SQLiteDatabase db) {
         db.execSQL("CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY, TeamName TEXT, URL TEXT)");
      }

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         Log.w("Example", "Upgrading database, this will drop tables and recreate.");
         db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
         onCreate(db);
      }
   }
}
