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

import com.superlucky.R;
import conexionSiabra.ConexionSiabra;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class MainActivity extends FragmentActivity {

	String verificador;
	ConexionSiabra com;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		com = new ConexionSiabra(this);
		if (com.check()) { //Check for tokens
				Intent intent = new Intent(this, MenuActivity.class);
				startActivity(intent);
				this.finish();
		}
	}

	public void actualizarValidador(String verificador) {
		if (com.obtenerAccessToken(verificador)) {
			Intent intent = new Intent(this, MenuActivity.class);
			startActivity(intent);
			this.finish();
		}
		else{
			 Dialog dialogoError= new Dialog(this);
			 dialogoError.setTitle(R.string.VerificadorIncorrecto);
			 dialogoError.show();
			 
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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

	/*
	 * When the button "Siguiente" is clicked, the app opens the navigator with the authorize url. 
	 */
	public void goToAutorizar(View view) {
		String url = new String();
		url = com.autorizacion();
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		this.startActivity(intent);
		VerificadorDialog myDialogFragment = new VerificadorDialog();
		myDialogFragment.show(getFragmentManager(), "myDialogFragment");
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
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
