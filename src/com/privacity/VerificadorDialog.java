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

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class VerificadorDialog extends  DialogFragment {
	public VerificadorDialog(){}
 
	static VerificadorDialog newInstance() {	
		String info = "Introducir verficador";
        VerificadorDialog ab = new VerificadorDialog();
        Bundle args = new Bundle();
        args.putString("inf", info);
        ab.setArguments(args);
        return ab;
    }
	    
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	 // Seleccionar y mostrar el layout a mostrar
	        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
	        final View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_verficador, null);
	        alertDialogBuilder.setView(view);
	        //Seteamos el t??tulo
	        alertDialogBuilder.setTitle("Ingrese verificador");
	        //El bot??n cancelar
	        alertDialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	            @Override
	            public void onClick(DialogInterface dialog, int which) {
	                // Pasar un valor nulo al m??todo en la actividad principal
	                // y cerrar el dialogo
	                ((MainActivity) getActivity()).actualizarValidador("");
	                dialog.cancel();
	            }
	        });
	        //El bot??n aceptar
	        alertDialogBuilder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
	             @Override
	              public void onClick(DialogInterface dialog, int which) {
	                  //Obtener el texto ingresado, llamar al m??todo
	                  //en la actividad principal y cerrar el di??logo
	                  TextView txtv = (TextView) view.findViewById(R.id.editVerificador);
	                  ((MainActivity) getActivity()).actualizarValidador(txtv.getText().toString());
	                  dialog.dismiss();
	              }
	        });
	        return alertDialogBuilder.create();

	    }

}
