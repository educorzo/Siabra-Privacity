/*
 * 
 *  Copyright (c) 2014 Eduardo Corzo
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 3 of the License, or
 *  (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 *  Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,MA 02110-1301, USA.
*/ 
package conexionSiabra;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.location.LocationListener;
import android.util.Log;

public class DataBaseHandler extends SQLiteOpenHelper{
	String sqlCreate = "CREATE TABLE Access (token TEXT PRIMARY KEY, tokenSecret TEXT)";
	public DataBaseHandler(Context contexto, String nombre,CursorFactory factory, int version) {
		super(contexto, nombre, factory, version);
	}
	
	public DataBaseHandler(LocationListener contexto, String nombre,CursorFactory factory, int version) {
		super((Context) contexto, nombre, factory, version);
	}
	
	public DataBaseHandler(Context context) {
        super(context, "BaseDeDatos", null, 1);
    }
	
	@Override
    public void onCreate(SQLiteDatabase db) {
        //Execute SQL sentence to create the table.
        db.execSQL(sqlCreate);
    }

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		 db.execSQL("DROP TABLE IF EXISTS Access");
		 db.execSQL(sqlCreate);
	}
	
	public void addOauth(Oauth conexion){
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("token", conexion.getAccessToken());
		values.put("tokenSecret", conexion.getAccessTokenSecret());
		db.insert("Access", null, values);
        db.close();
	}
	
	public Oauth getOauth(){
		Oauth conexion = new Oauth();
		SQLiteDatabase db= this.getReadableDatabase();
		String[] args = new String[] {"1"};
		Cursor cursor= db.rawQuery("SELECT * FROM Access WHERE '1'=?",args);
		if (cursor != null)
            cursor.moveToFirst();
		String token=cursor.getString(0);
		String secret=cursor.getString(1);
		conexion.setAccessTokens(token.substring(0, token.length() - 1),secret);
		return conexion;
	}
	
	public boolean exiteOauth(){
		boolean resultado=false;
		SQLiteDatabase db= this.getReadableDatabase();
		String[] args = new String[] {"1"};
		Cursor cursor= db.rawQuery("SELECT * FROM Access WHERE '1'=?",args);
		if (cursor.getCount()>0){
			resultado=true;
			cursor.moveToFirst();
		}
		
		return resultado;
	}
	
	public void eraseOauth(){
		SQLiteDatabase db = this.getWritableDatabase();
		if (db != null) {
			db.execSQL("DELETE FROM Access");
			Log.wtf("DATABASE", "Oauth fue eliminado");
			db.close();
		}
	}
	
}
