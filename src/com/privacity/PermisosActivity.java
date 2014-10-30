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
import java.util.List;
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
import android.widget.CheckBox;

public class PermisosActivity extends Activity {
	private CheckBox chkApellidos, chkComentario,chkDireccion, chkDNI, chkEmail, chkEmpresa, chkEstatus, chkFacebook, chkLinkedin;
	private CheckBox chkNombre,chkNacimiento, chkPais,chkTelefono, chkProfesion, chkTwitter, chkWebPersonal, chkWebProfesional;
	private String permisos;
	private ConexionSiabra comm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_permisos);
		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		comm = new ConexionSiabra(this);
		chkApellidos = (CheckBox) findViewById(R.id.chkApellidos);
		chkComentario = (CheckBox) findViewById(R.id.chkComentario);
		chkDireccion = (CheckBox) findViewById(R.id.chkDireccion);
		chkDNI = (CheckBox) findViewById(R.id.chkDNI);
		chkEmail = (CheckBox) findViewById(R.id.chkEmail);
		chkEmpresa = (CheckBox) findViewById(R.id.chkEmpresa);
		chkEstatus = (CheckBox) findViewById(R.id.chkEstatus);
		chkFacebook = (CheckBox) findViewById(R.id.chkFacebook);
		chkLinkedin = (CheckBox) findViewById(R.id.chkLinkedin);
		chkNombre = (CheckBox) findViewById(R.id.chkNombre);
		chkNacimiento = (CheckBox) findViewById(R.id.chkNacimiento);
		chkPais = (CheckBox) findViewById(R.id.chkPais);
		chkTelefono = (CheckBox) findViewById(R.id.chkTelefono);
		chkProfesion = (CheckBox) findViewById(R.id.chkProfesion);
		chkTwitter = (CheckBox) findViewById(R.id.chkTwitter);
		chkWebPersonal = (CheckBox) findViewById(R.id.chkWebPersonal);
		chkWebProfesional = (CheckBox) findViewById(R.id.chkWebProfesional);
		List<String> lista=lconversor();
		if(lista.isEmpty())
			permisos=new String(this.getIntent().getStringExtra("permisos"));
		marcarCheckButtons();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.permisos, menu);
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

	public void validar(View view) {
		JSONObject jsonObject=comm.modificarPermisosPorPalabras(lconversor());
		Dialog dialogo= new Dialog(this);
		if(jsonObject.has("Error")){
			dialogo.setTitle(R.string.errorDeConexion);
		}else{
			dialogo.setTitle(R.string.permisosModificados);
		}
		dialogo.show();
		}
	
	
	
	//It fills the checkbuttons
	public void marcarCheckButtons(){
		if(permisos.contains("apellidos"))
			chkApellidos.setChecked(true);
		else
			chkApellidos.setChecked(false);
		
		if(permisos.contains("comentario"))
			chkComentario.setChecked(true);
		else
			chkComentario.setChecked(false);
		
		if(permisos.contains("direccion"))
			chkDireccion.setChecked(true);
		else
			chkDireccion.setChecked(false);	
		if(permisos.contains("dni"))
			chkDNI.setChecked(true);
		else
			chkDNI.setChecked(false);
		if(permisos.contains("email"))
			chkEmail.setChecked(true);
		else
			chkEmail.setChecked(false);
		if(permisos.contains("empresa"))
			chkEmpresa.setChecked(true);
		else
			chkEmpresa.setChecked(false);
		if(permisos.contains("estatus"))
			chkEstatus.setChecked(true);
		else
			chkEstatus.setChecked(false);
		if(permisos.contains("facebook"))
			chkFacebook.setChecked(true);
		else
			chkFacebook.setChecked(false);
		if(permisos.contains("linkedin"))
			chkLinkedin.setChecked(true);
		else
			chkLinkedin.setChecked(false);
		if(permisos.contains("nacimiento"))
			chkNacimiento.setChecked(true);
		else
			chkNacimiento.setChecked(false);
		if(permisos.contains("nombre"))
			chkNombre.setChecked(true);
		else
			chkNombre.setChecked(false);
		if(permisos.contains("pais"))
			chkPais.setChecked(true);
		else
			chkPais.setChecked(false);
		
		if(permisos.contains("profesion"))
			chkProfesion.setChecked(true);
		else
			chkProfesion.setChecked(false);
		if(permisos.contains("telefono"))
			chkTelefono.setChecked(true);
		else
			chkTelefono.setChecked(false);

		if(permisos.contains("twitter"))
			chkTwitter.setChecked(true);
		else
			chkTwitter.setChecked(false);
		if(permisos.contains("webPersonal"))
			chkWebPersonal.setChecked(true);
		else
			chkWebPersonal.setChecked(false);
		if(permisos.contains("webProfesional"))
			chkWebProfesional.setChecked(true);
		else
			chkWebProfesional.setChecked(false);		
		
	}
	
	//it makes a list with checked elements
	public List<String> lconversor(){
		List<String> lista= new ArrayList<String>();
		permisos="";
		if (chkApellidos.isChecked()){
			lista.add("apellidos");
			permisos=permisos.concat("apellidos");
		}	
		if (chkComentario.isChecked()){
			lista.add("comentario");
			permisos=permisos.concat("comentario");
		}
		if (chkDireccion.isChecked()){
			lista.add("direccion");
			permisos=permisos.concat("direccion");
		}
		if (chkDNI.isChecked()){
			lista.add("dni");
			permisos=permisos.concat("dni");
		}
		if (chkEmail.isChecked()){
			lista.add("email");
			permisos=permisos.concat("email");
		}
		if (chkEmpresa.isChecked()){
			lista.add("empresa");
			permisos=permisos.concat("empresa");
		}
		if (chkEstatus.isChecked()){
			lista.add("estatus");
			permisos=permisos.concat("estatus");
		}
		if (chkFacebook.isChecked()){
			lista.add("facebook");
			permisos=permisos.concat("facebook");
		}
		if (chkLinkedin.isChecked()){
			lista.add("linkedin");
			permisos=permisos.concat("linkedin");	
		}
		if (chkNombre.isChecked()){
			lista.add("nacimiento");
			permisos=permisos.concat("nacimiento");
		}
		if (chkNombre.isChecked()){
			lista.add("nombre");
			permisos=permisos.concat("nombre");
		}
		if (chkPais.isChecked()){
			lista.add("pais");
			permisos=permisos.concat("pais");
		}
		if (chkProfesion.isChecked()){
			lista.add("profesion");
			permisos=permisos.concat("profesion");
		}
		if (chkTelefono.isChecked()){
			lista.add("telefono");
			permisos=permisos.concat("telefono");
		}
		if (chkTwitter.isChecked()){
			lista.add("twitter");
			permisos=permisos.concat("twitter");
		}
		if (chkWebPersonal.isChecked()){
			lista.add("webPersonal");
			permisos=permisos.concat("webPersonal");
		}
		if (chkWebProfesional.isChecked()){
			lista.add("webProfesional");
			permisos=permisos.concat("webProfesional");
		}
		return lista;
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
			View rootView = inflater.inflate(R.layout.fragment_permisos,
					container, false);
			return rootView;
		}
	}


}
