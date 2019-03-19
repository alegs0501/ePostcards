package es.kapok.alegs0501.epostcards.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import es.kapok.alegs0501.epostcards.models.Postcard

class PostcardDBAdapter(_context: Context) {

    private val DATABASE_NAME: String = "epostcards"
    private var mContext: Context? = null
    private var mDbHelper: DBHelper? = null
    private var mSqliteDatabase: SQLiteDatabase? = null
    private val DATABASE_VERSION = 1


    init {
        this.mContext = _context
        mDbHelper = DBHelper(_context, DATABASE_NAME, null, DATABASE_VERSION)
    }

    public fun open(){
        mSqliteDatabase = mDbHelper?.writableDatabase
    }

    public fun insertPostcard(front: ByteArray, back: ByteArray){
        open()
        val cv = ContentValues()
        cv.put("front", front)
        cv.put("back", back)
        mSqliteDatabase?.insert("postcard", null, cv)
    }

    public fun selectAllPostcards(): ArrayList<Postcard>{
        open()
        var allPostcards = ArrayList<Postcard>()
        var cursor: Cursor? = mSqliteDatabase?.query("postcard", null, null, null, null, null, null)

        if (cursor != null) {

            if (cursor.moveToFirst()) {
                do {
                    val postcard = Postcard(cursor.getBlob(1), cursor.getBlob(2))
                    allPostcards.add(postcard)
                } while (cursor.moveToNext())
            }
        }

        return allPostcards
    }

    public fun deletePostcard(id: Int){
        val query = "DELETE FROM postcard WHERE id= $id;"
        mSqliteDatabase?.execSQL(query)
    }


    inner class DBHelper(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?,
                         version: Int): SQLiteOpenHelper(context, name, factory, version){

        override fun onCreate(db: SQLiteDatabase?) {
            val query = "CREATE TABLE postcard(id integer primary key autoincrement, front blob, back blob);"
            db?.execSQL(query)
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            val query = "DROP TABLE IF EXISTS postcard;"
            db?.execSQL(query)
            onCreate(db)
        }

    }

}