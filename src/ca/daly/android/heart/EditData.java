package ca.daly.android.heart;

import com.actionbarsherlock.app.SherlockFragment;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;
import android.os.Bundle;
import android.util.Log;
import java.util.Calendar;
import java.util.Date;

public class EditData implements DatabaseHelper.RecordListener,
		                 DataStore {

  private static final String TAG = "EditData";
  private Long id = 0L;  // current record's id (0 = not set)
  public Calendar date_time = Calendar.getInstance();  // today now
  public Integer systolic = 121;
  public Integer diastolic = 81;
  public Integer pulse = 71;
  public String notes = "";
  public Boolean location = true;
  public Boolean side = true;
  //private Long currentReqTrackNo;
  private EditFragment viewer = null;
  private Context ctxt;

  public EditData(Context ctxt, Long id, EditFragment viewer) {
    if (id != null && !id.equals(0L)) {
      DatabaseHelper.getInstance(ctxt).getRecordAsync(id, this);
    }
    this.ctxt = ctxt;
    this.viewer = viewer;
  }

  /**
   * Returns the current data field values
   */
  public ContentValues Get() {
    ContentValues rtrnVal = new ContentValues();
    Log.v(TAG,"Get");
    rtrnVal.put(DatabaseHelper.ID,id);
    rtrnVal.put(DatabaseHelper.SYSTOLIC,systolic);
    rtrnVal.put(DatabaseHelper.DIASTOLIC,diastolic);
    rtrnVal.put(DatabaseHelper.PULSE,pulse);
    rtrnVal.put(DatabaseHelper.DATE,date_time.getTimeInMillis());
    rtrnVal.put(DatabaseHelper.NOTES,notes);
    rtrnVal.put(DatabaseHelper.LOCATION,location);
    rtrnVal.put(DatabaseHelper.SIDE,side);
    return rtrnVal;
  }

  /**
   * accepts a set of record values and saves to database if needed
   */
  public void Put(ContentValues rec) { 
    if (isDirty(rec)) {
      Log.v (TAG,"Put: is dirty");
      rec.put(DatabaseHelper.ID,id);
      DatabaseHelper.getInstance(ctxt).SaveRecordAsync(this,rec);
      Toast.makeText(ctxt.getApplicationContext(), ctxt.getApplicationContext().getString(R.string.saved_entry), Toast.LENGTH_LONG).show();
      updateData(rec);
    }
  }


  /** 
   * sets data from a DatabaseHelper asyncronous return
   */
  public void setRecord(ContentValues new_rec) {
    id = new_rec.getAsLong(DatabaseHelper.ID);
    Log.v(TAG,"setRecord: id =" + id);
    updateData(new_rec);
    updateViewer();
  }

  /**
   * updates the fields from specified data
   */
  private void updateData(ContentValues new_data) {
    Log.v(TAG,"updateData: notes data = " + new_data.getAsString(DatabaseHelper.NOTES));
    date_time.setTimeInMillis(new_data.getAsLong(DatabaseHelper.DATE));
    systolic = new_data.getAsInteger(DatabaseHelper.SYSTOLIC);
    diastolic = new_data.getAsInteger(DatabaseHelper.DIASTOLIC);
    pulse = new_data.getAsInteger(DatabaseHelper.PULSE);
    notes = new_data.getAsString(DatabaseHelper.NOTES);
    location = new_data.getAsBoolean(DatabaseHelper.LOCATION);
    side = new_data.getAsBoolean(DatabaseHelper.SIDE);
  }


  /**
   * sets id field from a DatabaseHelper asyncronous return
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Delete this record from the database
   */
  public void doDelete() {
    if (id != 0) {
      DatabaseHelper.getInstance(ctxt).deleteRecordAsync(id);
    }
  }

  /**
   * Determines if the given values differ from the current field values
   */
  private boolean isDirty(ContentValues rec) {
    Log.v(TAG,"isDirty curr data: date_time:" + date_time.getTimeInMillis()
                                        + " systolic:" + systolic
					+ " diastolic: " + diastolic
					+ " pulse: " + pulse
					+ " notes: " + notes
					+ " location: " + location
					+ " side: " + side);
    Log.v(TAG,"isDirty data to save: date_time:" + rec.getAsLong(DatabaseHelper.DATE)
                                        + " systolic:" + rec.getAsInteger(DatabaseHelper.SYSTOLIC)
					+ " diastolic: " + rec.getAsInteger(DatabaseHelper.DIASTOLIC)
					+ " pulse: " + rec.getAsInteger(DatabaseHelper.PULSE)
					+ " notes: " + rec.getAsString(DatabaseHelper.NOTES)
					+ " location: " + rec.getAsBoolean(DatabaseHelper.LOCATION)
					+ " side: " + rec.getAsBoolean(DatabaseHelper.SIDE));
    boolean dirty = (date_time.getTimeInMillis() != rec.getAsLong(DatabaseHelper.DATE)
             || !systolic.equals(rec.getAsInteger(DatabaseHelper.SYSTOLIC))
	     || !diastolic.equals(rec.getAsInteger(DatabaseHelper.DIASTOLIC))
	     || !pulse.equals(rec.getAsInteger(DatabaseHelper.PULSE))
	     || !notes.equals(rec.getAsString(DatabaseHelper.NOTES))
	     || !location.equals(rec.getAsBoolean(DatabaseHelper.LOCATION))
	     || !side.equals(rec.getAsBoolean(DatabaseHelper.SIDE)));
    return dirty;
  }

  /**
   * tell my viewer that it needs to update its' values
   */
  private void updateViewer() {
    if (viewer != null) {
      viewer.updateView();
    }
  }
}
