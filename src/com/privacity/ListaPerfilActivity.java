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

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.superlucky.R;

import conexionSiabra.ConexionSiabra;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;

public class ListaPerfilActivity extends ListActivity {

	ArrayList<HashMap<String, String>> Eventos;
	String[] from = new String[] { "username", "codigo" };
	int[] to = new int[] { R.id.nombreAutor, R.id.codigo };
	private ConexionSiabra com;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lista_perfil);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}

/*
 * Ask and create a list of profiles
 * 
 */
	@Override
	protected void onResume() {
		super.onResume();
		
		Eventos = new ArrayList<HashMap<String, String>>();
		com = new ConexionSiabra(this);
		JSONObject jsonObject = com.getListaPerfiles();
		
		String[] perfiles = this.getIntent().getStringArrayExtra("perfiles");
		String[] perfilesCodigo = this.getIntent().getStringArrayExtra("perfilesCodigo");

		if (!jsonObject.has("Error") && !jsonObject.has("detail")) {
			try {
				perfiles = new String[jsonObject.length()];
				perfilesCodigo = new String[jsonObject.length()];
				for (int i = 0; i < jsonObject.length(); i++) {
					JSONArray array = jsonObject.getJSONArray(String.valueOf(i));
					perfiles[i] = array.getString(0);
					perfilesCodigo[i] = array.getString(1);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Log.wtf("Error", e);
			}

			for (int i = 0; i < perfiles.length; i++) {
				HashMap<String, String> datosEvento = new HashMap<String, String>();
				datosEvento.put("username", perfiles[i]);
				datosEvento.put("codigo", perfilesCodigo[i]);
				Eventos.add(datosEvento);
			}
			
		}// IF no hay error
		else {
			Dialog dialogo = new Dialog(this);
			dialogo.setTitle(getResources().getString(R.string.errorDeConexion));
			dialogo.show();
		}

		// PINTA TODAS LAS FILAS
		SimpleAdapter ListadoAdapter = new SimpleAdapter(this, Eventos,
				R.layout.row, from, to);
		setListAdapter(ListadoAdapter);
	}

	
	/*
	 * Obtain a profile information, when a row is clicked.
	 */
	protected void onListItemClick(android.widget.ListView l, View v,
			int position, long id) {
		super.onListItemClick(l, v, position, id);
		Intent intent = new Intent(this, PerfilActivity.class);
		JSONObject jsonObject = com.getPerfil( Eventos.get(position).get("codigo"));

		try {
			intent.putExtra("codigo", Eventos.get(position).get("codigo"));
			intent.putExtra("descripcion", jsonObject.getString("descripcion"));
			intent.putExtra("usuario", jsonObject.getString("usuario"));
			intent.putExtra("permisos", jsonObject.getString("permisos"));
			intent.putExtra("agregado", jsonObject.getString("agregado"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		startActivity(intent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.lista_perfil, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
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
			View rootView = inflater.inflate(R.layout.fragment_lista_perfil,
					container, false);
			return rootView;
		}
	}

}
