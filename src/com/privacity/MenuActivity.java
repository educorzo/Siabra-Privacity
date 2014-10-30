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
package com.privacity;
import org.json.JSONException;
import org.json.JSONObject;

import com.superlucky.R;

import conexionSiabra.ConexionSiabra;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MenuActivity extends Activity {
	private ConexionSiabra comm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_menu);
		comm = new ConexionSiabra(this);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	/*
	 * It Obtains grants and goes to PermisosActivity
	 */
	public void gotoVisualizar(View view){
		
		Intent intent = new Intent(this, PermisosActivity.class);
		String permisos= new String();
		JSONObject jsonObject=comm.obternerPemisosPorPalabras();
		
		if(!jsonObject.has("Error") & !jsonObject.has("detail")){ //Si no existen errores
			try {
				permisos = new String(jsonObject.get("permisos").toString());
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
			intent.putExtra("permisos", permisos);
			startActivity(intent);
		}//IF
		else{
			Dialog dialogoError = new Dialog(this);
			dialogoError.setTitle(getResources().getString(R.string.errorDeConexion));
			dialogoError.show();
		}
		
	}
	

	public void gotoBuscarPerfil(View view){
		Intent intent = new Intent(this, BuscarPerfilActivity.class);
			startActivity(intent);
	}
	
	public void gotoListaPerfiles(View view){
		Intent intent = new Intent(this, ListaPerfilActivity.class);
		startActivity(intent);
	}
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			comm.borrarBaseDeDatos(); //The tokens from the database are erased.
			this.finish(); //The app is closed
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_menu, container,
					false);
			return rootView;
		}
	}
	

}
