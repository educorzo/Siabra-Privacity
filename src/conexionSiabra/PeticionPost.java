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

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;

public class PeticionPost implements Peticion {

	String url;
	ArrayList<NameValuePair> listaDePares;
	String result;

	PeticionPost(String urlEntrada, ArrayList<NameValuePair> nameValuePairs) {
		url = new String(urlEntrada);
		listaDePares = new ArrayList<NameValuePair>(2);
		listaDePares = nameValuePairs;
		result = new String();
	}

	// it Executes the POST petition
	public String Ejecutar() {

		AsyncTask<ArrayList<NameValuePair>, Void, String> variable = new PeticionHttp();
		((PeticionHttp) variable).setURL(url);
		variable.execute(listaDePares);
		try {
			result = variable.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private class PeticionHttp extends
			AsyncTask<ArrayList<NameValuePair>, Void, String> {
		String url = "";

		public void setURL(String direccion) {
			url = direccion;
		}

		@Override
		protected String doInBackground(
				ArrayList<NameValuePair>... nameValuePairs) {
			// TODO Auto-generated method stub
			String resultado = new String();
			ArrayList<NameValuePair> nvPairs = nameValuePairs[0];
			try { // Make the http conection

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(url);
				httppost.setHeader("Authorization", "Oauth"); // Set Header
				httppost.setEntity(new UrlEncodedFormEntity(nvPairs));// /Sin
																		// HTTP
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				ToStringInputStream contenido = new ToStringInputStream(
						entity.getContent());
				resultado = contenido.streamToString();
				contenido.close();


			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				resultado = "{ \"Error\": \"Error de conexion.\"}";
			}
			return resultado;
		}
	}

}
