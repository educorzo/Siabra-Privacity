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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class BuscarPerfilActivity extends Activity {
	ConexionSiabra com;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_obtener_perfil);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	@Override
	protected void onResume(){
		super.onResume();
		com=new ConexionSiabra(this);
		EditText txtv = (EditText)findViewById(R.id.editEnviarCodigo);
		txtv.setText("");
	}
	
/*
 * Send a code to obtain a profile
 */
	public void enviarCodigo(View view){
		 EditText txtv = (EditText)findViewById(R.id.editEnviarCodigo);
	     String codigo=txtv.getText().toString();
		 JSONObject jsonObject =com.getPerfil(codigo);
	    
		 if(!jsonObject.has("Error") && !jsonObject.has("detail")){
			   try{
				   Intent intent = new Intent(this, PerfilActivity.class);
				   intent.putExtra("descripcion",jsonObject.getString("descripcion"));
				   intent.putExtra("permisos", jsonObject.getString("permisos"));
				   Log.wtf("PERMISOS",jsonObject.getString("permisos") );
				   intent.putExtra("usuario", jsonObject.getString("usuario"));
				   intent.putExtra("codigo",codigo);
				   intent.putExtra("agregado",jsonObject.getString("agregado"));
				   startActivity(intent);
			   } catch (JSONException e) {
				   // TODO Auto-generated catch block
				   e.printStackTrace();
				   Log.wtf("Error",e);
			   }
		   }//IF
		   else{
			   Dialog dialogoError= new Dialog(this);	  
			   try {
				String error= jsonObject.getString("Error");
				 dialogoError.setTitle(error);
			   } catch (JSONException e) {
				// TODO Auto-generated catch block
				dialogoError.setTitle(R.string.Upps);
				e.printStackTrace();
			}  
			   dialogoError.show();
		   }

	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.obtener_perfil, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_obtener_perfil,
					container, false);
			return rootView;
		}
	}

}
