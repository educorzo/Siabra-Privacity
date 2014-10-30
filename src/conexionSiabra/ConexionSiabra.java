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

import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;
import android.util.Pair;

public class ConexionSiabra {
	private Oauth oauth;
	private DataBaseHandler db;
	private String url_check="http://siabra.pythonanywhere.com/check/";
	private String url_obtener_lista_perfiles="http://siabra.pythonanywhere.com/perfil/list/";
	private String url_obtener_informacion_otro_usuario="http://siabra.pythonanywhere.com/user/other/";
	private String url_obtener_perfil="http://siabra.pythonanywhere.com/perfil/show/";
	private String url_cancelar_perfil="http://siabra.pythonanywhere.com/perfil/cancel/";
	private String url_agregar_perfil="http://siabra.pythonanywhere.com/perfil/new/";
	private String url_obtener_permisos_por_codigo="http://siabra.pythonanywhere.com/permisos/show/code/";
	private String url_obtener_permisos_por_palabras="http://siabra.pythonanywhere.com/permisos/show/words/";
	private String url_modificar_permisos_por_palabras="http://siabra.pythonanywhere.com/permisos/modify/words/";
	private String url_modificar_permisos_por_codigo="http://siabra.pythonanywhere.com/permisos/modify/code/";
	
	public ConexionSiabra(Context context){
		db= new DataBaseHandler(context);
		if (db.exiteOauth())
    		oauth = db.getOauth();
		else{
			oauth=new Oauth();
		}
	}
	
	public boolean check(){
		boolean result=false;
		Pair<String, String> elemento = new Pair<String, String>("", "");
		if(!db.exiteOauth()){
			Log.wtf("Token", "No existe");
			result=false;
		}
		else{
			JSONObject jsonObject = oauth.peticionGet(elemento,url_check);
			if (jsonObject.has("Exito")) {
				result=true;
			} else if (jsonObject.has("detail")) {
				Log.wtf("Token", "Caducado");
				db.eraseOauth();
				result=false;
			} else if (jsonObject.has("Error")) {
				Log.wtf("Conexion", "Fallo");
				result=false;
			}
		}
		return result;
	}
	
	public void borrarBaseDeDatos(){
		db.eraseOauth();
	}
	
	public String autorizacion(){
		String aux = oauth.authorization();
		return aux;
	}
	
	public boolean obtenerAccessToken(String verificador){
		boolean result=false;
		verificador = verificador.trim(); // Le quitamos espacios finales ya que
		// a veces los navegadores los
		// insertan al hacer copy
		if (oauth.obtainAccessToken(verificador)) {
			db.addOauth(oauth);
			Log.wtf("exito", "en DB");
			result=true;
		}
		
		return result;
	}
	
	public JSONObject obternerPemisosPorPalabras(){
		Pair<String, String> elemento= new Pair<String, String>("","");
		return oauth.peticionGet(elemento, url_obtener_permisos_por_palabras);
	}
	public JSONObject obtenerPermisosPorCodigo(){
		Pair<String, String> elemento= new Pair<String, String>("","");
		return oauth.peticionGet(elemento, url_obtener_permisos_por_codigo);
	}
	
	public JSONObject modificarPermisosPorCodigo(String permisos){
		Pair<String, String> elemento= new Pair<String, String>("permisos",permisos);
		ArrayList<Pair <String, String> > elementos = new ArrayList<Pair<String, String>>();
		elementos.add(elemento);
		return oauth.peticionPost(elementos, url_modificar_permisos_por_codigo);
	}
	
	public JSONObject modificarPermisosPorPalabras(List<String> permisos){
		String sPermisosLista="[";
		for(int i=0;i<permisos.size();i++){
			sPermisosLista=sPermisosLista.concat(""+permisos.get(i)+",");
		}
		sPermisosLista=sPermisosLista.concat("]");
		sPermisosLista=sPermisosLista.replace(",]", "]");
		Pair<String, String> elemento= new Pair<String, String>("permisos",sPermisosLista);
		ArrayList<Pair <String, String> > elementos = new ArrayList<Pair<String, String>>();
		elementos.add(elemento);
		return oauth.peticionPost(elementos,url_modificar_permisos_por_palabras);
	}
	
	public JSONObject cancelarPerfil(String codigo){
		Pair<String, String> elemento= new Pair<String, String>("codigo",codigo);
		ArrayList<Pair <String, String> > elementos = new ArrayList<Pair<String, String>>();
		elementos.add(elemento);
		return oauth.peticionPost(elementos, url_cancelar_perfil);
	}
	
	public JSONObject agregarPerfil(String codigo){
		Pair<String, String> elemento= new Pair<String, String>("codigo",codigo);
		ArrayList<Pair <String, String> > elementos = new ArrayList<Pair<String, String>>();
		elementos.add(elemento);
		return oauth.peticionPost(elementos, url_agregar_perfil);
	}
	
	public JSONObject getPerfil(String codigo){
		Pair<String, String> elemento = new Pair<String, String>("codigo", codigo);
		return oauth.peticionGet(elemento,url_obtener_perfil);
	}
	
	public JSONObject getListaPerfiles(){
		Pair<String, String> elemento = new Pair<String, String>("", "");
		return oauth.peticionGet(elemento,url_obtener_lista_perfiles);
	}

	public JSONObject getInformacionOtroUsuario(String username){
		Pair<String, String> elemento = new Pair<String, String>("username", username);
		return oauth.peticionGet(elemento,url_obtener_informacion_otro_usuario);
	}
}
