package ca.daly.android.heart;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class DeleteDialog extends DialogFragment { // implements DialogInterface.OnClickListener {
  private View dia=null;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {
    //dia = getActivity().getLayoutInflater().inflate(R.layout.delete_dialog, null);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

    //return(builder.setTitle(R.string.delete_dlg_title).setView(dia)
    return(builder.setMessage(R.string.delete_dlg_title)
                  .setPositiveButton(android.R.string.ok, (DialogInterface.OnClickListener)getActivity().getSupportFragmentManager().findFragmentById(R.id.editfrag)) //this)
		  .setNegativeButton(android.R.string.cancel, null).create());
  }

}