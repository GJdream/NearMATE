package com.nearmate.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;

public class MyDialogBuilder 

{
    private Context context;

public AlertDialog getMyDialog(Context c,String message)
{
    this.context=c;
    AlertDialog.Builder builder = new AlertDialog.Builder((Activity)context);
    builder.setMessage(message).setTitle("Alert");                               

    builder.setPositiveButton("OK", new OnClickListener() {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			
		}
    });

    builder.setNegativeButton("Cancel", new OnClickListener() {

        @Override
        public void onClick(DialogInterface dialog, int which) 
        {
        //  do work on negative button press
        }
    });
    AlertDialog dialog = builder.create();
    return dialog;

}

}