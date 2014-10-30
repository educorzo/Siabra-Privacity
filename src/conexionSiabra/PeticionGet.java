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
import java.util.concurrent.ExecutionException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import android.os.AsyncTask;
import android.util.Log;

public class PeticionGet implements Peticion {
	String url;
	String result;

	PeticionGet(String urlEntrada) {
		url = new String(urlEntrada);
		result = new String();
	}

	public String Ejecutar() {
		AsyncTask<String, String, String> variable = new PeticionHttp()
				.execute(url, "nada", result);
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

	private class PeticionHttp extends AsyncTask<String, String, String> {

		@Override
		protected String doInBackground(String... params) {
			String resultado = new String();
			try { // Make the http conection
				HttpClient httpclient = new DefaultHttpClient();
				HttpResponse response = httpclient.execute(new HttpGet(
						params[0]));
				HttpEntity entity = response.getEntity();
				ToStringInputStream contenido = new ToStringInputStream(
						entity.getContent());
				resultado = contenido.streamToString();
				contenido.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				resultado = "{ \"Error\": \"Error de conexion.\"}";
				e.printStackTrace();
				Log.wtf("ERROR CONECTION", e);
			}
			return resultado;
		}

	}

}
