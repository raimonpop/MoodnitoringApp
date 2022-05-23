package raimon.example.moodnitoringapp.ui.recaida.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import raimon.example.moodnitoringapp.Constants
import raimon.example.moodnitoringapp.model.Notes

class DatabaseHelper(context: Context):SQLiteOpenHelper(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE ${Constants.ENTITY_NOTE} (" +
                "${Constants.PROPERTY_ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                "${Constants.PROPERTY_TEXT} VARCHAR(60)," +
                "${Constants.PROPERTY_HIPO_DEPR} BOOLEAN)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {

    }

    fun getAllNotes(): MutableList<Notes> {
        val notes: MutableList<Notes> = mutableListOf()

        val database = this.readableDatabase
        val query = "SELECT *FROM ${Constants.ENTITY_NOTE}"

        val result = database.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val note = Notes()
                note.id = result.getLong(result.getColumnIndex(Constants.PROPERTY_ID))
                note.text =
                    result.getString(result.getColumnIndex(Constants.PROPERTY_TEXT))
                note.hipo_depr =
                    result.getInt(result.getColumnIndex(Constants.PROPERTY_HIPO_DEPR)) == Constants.TRUE//true(1) es hipo y false(0) es depre
                notes.add(note)
            } while (result.moveToNext())
        }

        return notes
    }
    fun inserNote(note: Notes): Long {
        val database = this.writableDatabase
        val contentValues = ContentValues().apply {
            put(Constants.PROPERTY_TEXT, note.text)
            put(Constants.PROPERTY_HIPO_DEPR, note.hipo_depr)
        }

        val resultId = database.insert(
            Constants.ENTITY_NOTE,
            null,
            contentValues
        )
        return resultId
    }
}