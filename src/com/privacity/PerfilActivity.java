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
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class PerfilActivity extends Activity {
	ConexionSiabra com;
	String usuario;
	String codigo;
	String descripcion;
	String permisos;
	String agregado;
	View botonAceptar;
	View botonCancelar;
	
	/*
	 * It seek and show the profile of one user.
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_perfil_view);
		com = new ConexionSiabra(this);
		
		descripcion= this.getIntent().getStringExtra("descripcion");
		codigo = this.getIntent().getStringExtra("codigo");
		usuario=this.getIntent().getStringExtra("usuario");
		permisos=this.getIntent().getStringExtra("permisos");
		agregado=this.getIntent().getStringExtra("agregado");
		botonCancelar = findViewById(R.id.cancelarBtt);
		botonAceptar = findViewById(R.id.aceptarBtt);
		//Hacemos invisible los botones que no queremos utilizar.
		if(agregado.equals("True"))
			botonCancelar.setVisibility(View.VISIBLE);
		else
			botonAceptar.setVisibility(View.VISIBLE);
				
		TextView txtUsuario = (TextView) findViewById(R.id.autorTxt);
	    txtUsuario.setText(usuario);
	    TextView txtCodigo = (TextView) findViewById(R.id.codigoTxt);  
	    txtCodigo.setText(codigo);
	    TextView txtDescripcion = (TextView) findViewById(R.id.descripcionTxt);  
	    txtDescripcion.setText(descripcion);
	    TextView txtPermisos = (TextView) findViewById(R.id.permisosTxt); 
	   // txtPermisos.setText(permisosToString());
		txtPermisos.setText(permisosToReadable());
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	/*
	 * it return a string with the elements that user can see from other users.
	 */
	private String permisosToReadable(){
		permisos=permisos.replace("[", "");

		if(permisos.contains(",")){
		permisos=permisos.substring(0, permisos.lastIndexOf(','))
				+" y "
				+permisos.substring( permisos.lastIndexOf(",")+1,permisos.lastIndexOf("]"));
		}

		permisos=permisos.replace("\",\"" ,", ");
		permisos=permisos.replace("\"","");
		permisos=permisos.replace("]", "");
		//Replace words in order to have different languages.
		permisos=permisos.replace("nombre",getResources().getString(R.string.dnombre));
		permisos=permisos.replace("apellidos",getResources().getString(R.string.dapellidos));
		permisos=permisos.replace("pais",getResources().getString(R.string.dpais));
		permisos=permisos.replace("telefono", getResources().getString(R.string.dtelefono));
		permisos=permisos.replace("direccion",getResources().getString(R.string.ddireccion));
		permisos=permisos.replace("linkedin",getResources().getString(R.string.dlinkedin));
		permisos=permisos.replace("facebook",getResources().getString(R.string.dfacebook));
		permisos=permisos.replace("twitter",getResources().getString(R.string.dtwitter));
		permisos=permisos.replace("estatus",getResources().getString(R.string.destatus));
		permisos=permisos.replace("pais",getResources().getString(R.string.dpais));
		permisos=permisos.replace("webPersonal",getResources().getString(R.string.dpersonal));
		permisos=permisos.replace("webProfesional",getResources().getString(R.string.dprofesional));
		permisos=permisos.replace("nacimiento",getResources().getString(R.string.dnacimiento));
		permisos=permisos.replace("empresa",getResources().getString(R.string.dempresa));
		permisos=permisos.replace("email",getResources().getString(R.string.demail));
		permisos=permisos.replace("comentario",getResources().getString(R.string.dcomentario));
		permisos=permisos.replace("profesion",getResources().getString(R.string.dprofesion));
		permisos=permisos.concat(".");
		return permisos;
	}

	public void agregarPerfil(View view){
		JSONObject jsonObject = com.agregarPerfil(codigo);
		Dialog dialogo= new Dialog(this);		
		//Tratamiento de errores
		if(jsonObject.has("Error")){
			dialogo.setTitle(getResources().getString(R.string.errorDeConexion));
			try {
				String error = jsonObject.getString("Error");
				dialogo.setTitle(error);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}		
		}//IF
		else{ //Si ha tenido exito
			dialogo.setTitle(getResources().getString(R.string.perfilAgregado));
			botonAceptar.setVisibility(View.INVISIBLE);
			botonCancelar.setVisibility(View.VISIBLE);
		}
		dialogo.show();
		
	}
	
	public void cancelarPerfil(View view){
		JSONObject jsonObject= com.cancelarPerfil(codigo);
		Dialog dialogo= new Dialog(this);
		//Tratamiento de errores
				if(jsonObject.has("Error") || jsonObject.has("detail")){
					dialogo.setTitle(getResources().getString(R.string.errorDeConexion));
					try {
						String error = jsonObject.getString("Error");
						dialogo.setTitle(error);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}		
				}//IF
				else{ //Si ha tenido exito
					dialogo.setTitle((getResources().getString(R.string.perfilCancelado)));
					botonCancelar.setVisibility(View.INVISIBLE);
					botonAceptar.setVisibility(View.VISIBLE);
				}
				dialogo.show();
	}
	
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.perfil_view, menu);
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
			View rootView = inflater.inflate(R.layout.fragment_perfil_view,
					container, false);
			return rootView;
		}
	}

}
