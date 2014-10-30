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
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Base64;
import android.util.Log;
import android.util.Pair;

public class Oauth {
	static final String CONSUMER_KEY = ""; // Introduce here your SIABRA consumer tokens
	static final String CONSUMER_SECRET = ""; //Introduce here your SIABRA consumer tokens
	static final String REQUEST_TOKEN_URL = "http://siabra.pythonanywhere.com/oauth/request_token/";
	static final String ACCESS_TOKEN_URL = "http://siabra.pythonanywhere.com/oauth/access_token/";
	static final String AUTHORIZE_URL = "http://siabra.pythonanywhere.com/oauth/authorize/";
	static final String HELLO_URL = "http://siabra.pythonanywhere.com/prueba/";
	static final String HELLOPOST_URL = "http://siabra.pythonanywhere.com/permisos/modify/";
	static final String CALLBACK_URL = "oob";
	static final String URL = "oob";
	private String TOKEN = "";
	private String TOKEN_SECRET = "";
	private String ACCESS_TOKEN = "";
	private String ACCESS_TOKEN_SECRET = "";
	private long timeStamp;

	/*
	 * Sets the time in the object
	 */
	public long setTimeStamp() {
		timeStamp = System.currentTimeMillis() / 1000;// Must be in seconds not
		return timeStamp;
	}

	/*
	 * Return a Nonce. I'm using the same Nonce.
	 */
	public String getNonce() {
		return "requestnonceoob";
	}

	public String getToken() {
		return TOKEN;
	}
	
	public String getTokenSecret() {
		return TOKEN_SECRET;
	}
	
	public String getAccessToken() {
		return ACCESS_TOKEN;
	}

	public String getAccessTokenSecret() {
		return ACCESS_TOKEN_SECRET;
	}

	public void setAccessTokens(String token, String secret) {
		ACCESS_TOKEN = token;
		ACCESS_TOKEN_SECRET = secret;
	}
	
	public void setRequestTokens(String token, String secret) {
		TOKEN = token;
		TOKEN_SECRET = secret;
	}
	
	/*
	 * Este metodo extrae el token y el token secreto de una cadena y lo almacena como ACCESS_Token o normal token
	 * La cadena result debe tener el siguiente formato oauth_token_secret=3wRIKoapff16tcew&oauth_token=e7456187a43141af8d2e0d8fa99c95b9
	 * EL segundo parametro indicara si se almacena como AccessToken o como RequestToken se indica con "Access" o con nada para el caso de Request (por defecto)
	 */
	private boolean setTokens(String result, String type) {
		String tokenAux="", token_secretAux="";
		boolean exito=false;
		if(result.contains("oauth_token")){			
			if(type=="Access"){
				tokenAux = result.substring(result.indexOf("&oauth_token=") + 13,
						result.length());
				token_secretAux = result.substring(
							result.indexOf("oauth_token_secret=") + 19,
							result.indexOf("&oauth_token="));
				
				setAccessTokens(tokenAux,token_secretAux);
			}
			else{
				tokenAux = result.substring(result.indexOf("&oauth_token=") + 13,
						result.indexOf("&oauth_callback"));
					token_secretAux = result.substring(
							result.indexOf("oauth_token_secret=") + 19,
							result.indexOf("&oauth_token="));
				setRequestTokens(tokenAux, token_secretAux);
			}
			exito=true;
		}
		return exito;
	}
	
	

	/*
	 * Create a signature with HMAC_SHA1 method
	 */
	private static String hmac_sha1(String value, String key) {
		try {
			SecretKey secretKey = null;
			byte[] keyBytes = key.getBytes();
			secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");
			Mac mac = Mac.getInstance("HmacSHA1");
			mac.init(secretKey);
			byte[] text = value.getBytes();
			return new String(Base64.encode(mac.doFinal(text), 0)).trim();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	/*
	 * This method obtains Token and token_secret from the server and return a
	 * URL for the user. The purpose of this url is to get the authorization of
	 * the user.
	 */
	public String authorization() {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String get = "";
		try {
			baseString = "GET&" + URLEncoder.encode(REQUEST_TOKEN_URL, "UTF-8")+ "&";
			/*EL ORDEN DE LOS PARAMETROS CUENTA*/
			parameters = "oauth_callback=oob"
						+"&oauth_consumer_key=" + CONSUMER_KEY 
						+ "&oauth_nonce="+ getNonce() 
						+ "&oauth_signature_method=HMAC-SHA1"
						+ "&oauth_timestamp=" + setTimeStamp()
						+ "&oauth_version=1.0";

			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			signature = URLEncoder.encode(
					hmac_sha1(baseString, CONSUMER_SECRET + "&"), "UTF-8");
			/*AQUI PARECE QUE YA NO IMPORTA*/
			get = REQUEST_TOKEN_URL + "?oauth_version=1.0&oauth_nonce="
					+ getNonce() +"&oauth_callback=oob"+ "&oauth_timestamp=" + timeStamp
					+ "&oauth_consumer_key=" + CONSUMER_KEY
					+ "&oauth_signature_method=HMAC-SHA1&oauth_signature="
					+ signature;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result;
		Peticion peticion = new PeticionGet(get);
		result= peticion.Ejecutar();
		setTokens(result,"");
		return AUTHORIZE_URL + "?oauth_token=" + TOKEN; // Return the url for
														// the user.
	}

	
	/*
	 * This method makes a petition to get access tokens and set them
	 */
	public boolean obtainAccessToken(String verifier) {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String get = "";
		try {// Builds a baseString, makes the signature and construct the get
			baseString = "GET&" + URLEncoder.encode(ACCESS_TOKEN_URL, "UTF-8")
					+ "&";
			parameters = "oauth_consumer_key=" + CONSUMER_KEY 
					+"&oauth_nonce=" + getNonce()
					+"&oauth_signature_method=HMAC-SHA1"
					+"&oauth_timestamp=" + setTimeStamp() 
					+"&oauth_token="+ TOKEN 
					+"&oauth_verifier="+verifier
					+"&oauth_version=1.0";

			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			signature = URLEncoder
					.encode(hmac_sha1(baseString, CONSUMER_SECRET + "&"
							+ TOKEN_SECRET), "UTF-8");
			get = ACCESS_TOKEN_URL + "?oauth_version=1.0&oauth_nonce="
					+ getNonce() 
					+ "&oauth_timestamp=" + timeStamp
					+ "&oauth_consumer_key=" + CONSUMER_KEY 
					+ "&oauth_token="+ TOKEN
					+"&oauth_verifier="+verifier
					+ "&oauth_signature_method=HMAC-SHA1&oauth_signature="
					+ signature;			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result;
		Log.wtf("Envio", get);
		Peticion peticion = new PeticionGet(get);
		result= peticion.Ejecutar();
		Log.wtf("Resultado Access Token", result);
		return setTokens(result,"Access");
	}


	
	/* 
	 * Realiza una peticion Get
	 */
	public JSONObject peticionGet(Pair<String, String> elemento, String url) {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String get = "";
		String parametro1="";
		String parametro2="";
		//COMPRUEBO si el nuevo elemento alfabeticamente es mayor o menor que la palabra Oauth
		//Esto es debido a que el baseString debe ser formado en orden alfabetico
		 if(elemento.first.compareTo("oauth")<0) {//Es elemento.first es menor que oauth
			 parametro1=elemento.first+"="+elemento.second+"&";
		 }
		 else{
			 parametro2="&"+elemento.first+"="+elemento.second;
		 }
			
		try {
			baseString = "GET&" + URLEncoder.encode(url, "UTF-8") + "&";
			parameters = parametro1+"oauth_consumer_key=" + CONSUMER_KEY + "&oauth_nonce="
					+ getNonce() + "&oauth_signature_method=HMAC-SHA1"
					+ "&oauth_timestamp=" + setTimeStamp() + "&oauth_token="
					+ ACCESS_TOKEN + "&oauth_version=1.0"+parametro2;//+elemento.first+"="+elemento.second;
			
			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			Log.wtf("base", baseString);
			signature = URLEncoder.encode(
					hmac_sha1(baseString, CONSUMER_SECRET + "&"+ ACCESS_TOKEN_SECRET), "UTF-8");
			get = url + "?oauth_version=1.0&oauth_nonce=" + getNonce()
					+ "&oauth_timestamp=" + timeStamp + "&oauth_consumer_key="
					+ CONSUMER_KEY + "&oauth_token=" + ACCESS_TOKEN
					+ "&oauth_signature_method=HMAC-SHA1&oauth_signature="
					+ signature+"&"+elemento.first+"="+elemento.second;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result;
		//Log.wtf("Envio", get);
		Peticion peticion = new PeticionGet(get);
		result= peticion.Ejecutar();
		Log.wtf("Resultado", result);
		
		JSONObject resultado;
		try {
			resultado = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 resultado = new JSONObject();
		}
		return resultado;
	}

	
	
	/*
	 * Realiza una peticion Post autenticada
	 */
	public JSONObject peticionPost(ArrayList<Pair<String, String> > elementos, String url) {
		String baseString = "";
		String parameters;
		String complement;
		String signature = "";
		String post = "";
		Boolean delanteFlag=false;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
		ArrayList<String> delante= new  ArrayList<String>();
		ArrayList<String> detras= new  ArrayList<String>();
		for(int i=0;i<elementos.size();i++){
			Pair<String, String> elemento=elementos.get(i);
			if(elemento.first.compareTo("oauth")<0){ //Alfabeticamente va por delante de oauth
				delanteFlag=true;
				if(delante.size()==0)
				   delante.add(elemento.first+"="+elemento.second);
				else
					delante.add("&"+elemento.first+"="+elemento.second);
			}
			else{
				detras.add("&"+elemento.first+"="+elemento.second);
			}
		}
		
		
		
		try {
			baseString = "POST&" + URLEncoder.encode(url, "UTF-8")+"&";
			String subparameters= new String();
			//Anadimos los parametros que van delante
			for(int i=0; i<delante.size();i++){
				subparameters+=delante.get(i);
			}
			parameters=subparameters;
			
			if(delanteFlag){
				parameters+="&";
			}
			parameters += "oauth_consumer_key=" + CONSUMER_KEY + "&oauth_nonce="
					+ getNonce() + "&oauth_signature_method=HMAC-SHA1"
					+ "&oauth_timestamp=" + setTimeStamp() + "&oauth_token="
					+ ACCESS_TOKEN + "&oauth_version=1.0";
			//Anadimos los parametros que van detras
			for(int i=0;i<detras.size();i++){
				parameters+=detras.get(i);
			}
			complement = URLEncoder.encode(parameters, "UTF-8");
			baseString += complement;
			//REPARACION DE COMAS Y CORCHETES
			baseString=baseString.replace("%2C", "%252C");
			baseString=baseString.replace("%5B", "%255B");
			baseString=baseString.replace("%5B", "%255B");
			baseString=baseString.replace("%5D", "%255D");
			Log.wtf("base", baseString);
			signature = URLEncoder.encode(
					hmac_sha1(baseString, CONSUMER_SECRET + "&"
							+ ACCESS_TOKEN_SECRET), "UTF-8");
			
			post = url + "?oauth_version=1.0&oauth_nonce=" + getNonce()
					+ "&oauth_timestamp=" + timeStamp + "&oauth_consumer_key="
					+ CONSUMER_KEY + "&oauth_token=" + ACCESS_TOKEN
					+ "&oauth_signature_method=HMAC-SHA1&oauth_signature="
					+ signature;
			
			nameValuePairs.add(new BasicNameValuePair("oauth_nonce", getNonce()));
			nameValuePairs.add(new BasicNameValuePair("oauth_timestamp", Long
					.toString(timeStamp)));
			nameValuePairs.add(new BasicNameValuePair("oauth_consumer_key",
					CONSUMER_KEY));
			nameValuePairs.add(new BasicNameValuePair("oauth_token",
					ACCESS_TOKEN));
			nameValuePairs.add(new BasicNameValuePair("oauth_signature_method",
					"HMAC-SHA1"));
			nameValuePairs.add(new BasicNameValuePair("oauth_signature",
					signature));
			nameValuePairs.add(new BasicNameValuePair("oauth_version", "1.0"));
			
			for(int i=0;i<elementos.size();i++){
				Pair<String, String> parametro=elementos.get(i);
				nameValuePairs.add(new BasicNameValuePair(parametro.first,parametro.second));
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result;
		Log.wtf("Envio", post);
		//PeticionPost peticion = new PeticionPost(post, nameValuePairs);
		Peticion peticion = new PeticionPost(post, nameValuePairs);
		result= peticion.Ejecutar();
		Log.wtf("Resultado", result);
		JSONObject resultado;
		try {
			resultado = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			 resultado = new JSONObject();
		}
		
		return resultado;
	}
	
	
	/*
	 * This method extracts the access token and the access token secret from
	 * one string and sets them in the object
	 */
	private void setAccessTokens(String result) {
		Log.wtf("RESULT", result);
		ACCESS_TOKEN = result.substring(12,
				result.indexOf("&oauth_token_secret="));// 12
		ACCESS_TOKEN_SECRET = result.substring(result
				.indexOf("&oauth_token_secret=") + 20); // IT INCLUDES A SPACE
														// AT THE END !!! (1
														// hour to discover it!
														// I love this game! )
		ACCESS_TOKEN_SECRET = ACCESS_TOKEN_SECRET.substring(0,
				ACCESS_TOKEN_SECRET.length() - 1); // IT ERASE THE SPACE !!
		// I feel that compiler does not consider my comments :(
	}
	
}
