package edu.birzeit.nidlibraheem.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.birzeit.nidlibraheem.finalproject.models.Note;
import edu.birzeit.nidlibraheem.finalproject.models.User;
import edu.birzeit.nidlibraheem.finalproject.utils.PasswordHash;

public class ORM extends SQLiteOpenHelper {

    private static ORM instance;
    public static DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private ORM(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized ORM getInstance(Context context) {
        if (instance == null) {
            instance = new ORM(context, "notes_app_db", null, 11);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE users (" +
                "    user_id INTEGER PRIMARY KEY," +
                "    email TEXT NOT NULL," +
                "    firstName TEXT," +
                "    lastName TEXT," +
                "    password_hash TEXT NOT NULL" +
                ");");

        sqLiteDatabase.execSQL(
                "CREATE TABLE notes (" +
                        "    note_id INTEGER PRIMARY KEY," +
                        "    title TEXT," +
                        "    content TEXT," +
                        "    creationDate DATETIME," +
                        "    isFavorite INTEGER," +
                        "    owner_id INTEGER NOT NULL REFERENCES users(user_id)," +
                        "    tags TEXT" +
                        ");");

//        insert a dummy user for testing

        String hash = PasswordHash.hashPassword("123");

        sqLiteDatabase.execSQL("INSERT INTO users (email, firstName, lastName, password_hash) VALUES ('a@b.c' , 'Nidal', 'Alyan', '" + hash + "')");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS users");

        onCreate(sqLiteDatabase);

    }

    public long insertUser(User user) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{user.getEmail()});
        if (cursor.getCount() != 0) {
            System.out.println("User with this email already exists");
            return -1;
        }
        String hashed_password = PasswordHash.hashPassword(user.getPassword());

        ContentValues contentValues = new ContentValues();
        contentValues.put("FIRSTNAME", user.getFirstName());
        contentValues.put("LASTNAME", user.getLastName());
        contentValues.put("EMAIL", user.getEmail());
        contentValues.put("password_hash", hashed_password);
        sqLiteDatabase.insert("USERS", null, contentValues);

        // Insert the user and get the inserted row ID
        long insertedRowId = sqLiteDatabase.insert("USERS", null, contentValues);

        // Close the database
        sqLiteDatabase.close();

        user.setId(insertedRowId);

        return insertedRowId;
    }

    public User logInUser(String email, String providedPassword) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if (cursor.getCount() == 0) {
            System.out.println("User with this email was not found");
            return null;
        }

        cursor.moveToFirst();


        String hashedStoredPassword = cursor.getString(4);

        if (PasswordHash.verifyPassword(providedPassword, hashedStoredPassword)) {
            System.out.println("Correct password");
            return new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        }

        System.out.println("Wrong password");
        return null;
    }

    //update user and return the updated user
    public User updateUser(String email, String firstName, String lastName, String password) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("FIRSTNAME", firstName);
        contentValues.put("LASTNAME", lastName);

        if (password != null) {
            String hashed_password = PasswordHash.hashPassword(password);
            contentValues.put("password_hash", hashed_password);
        }

        sqLiteDatabase.update("USERS", contentValues, "email = ?", new String[]{email});

        sqLiteDatabase.close();

        return getUser(email);
    }

    public User getUser(String email) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();

        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});
        if (cursor.getCount() == 0) {
            System.out.println("User with this email was not found");
            return null;
        }

        cursor.moveToFirst();

        return new User(cursor.getLong(0), cursor.getString(1), cursor.getString(2), cursor.getString(3));
    }

    public long insertNote(Note note) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("creationDate", dateFormat.format(note.getCreationDate()));
        contentValues.put("isFavorite", note.isFavorite());
        contentValues.put("owner_id", note.getOwnerId());
        contentValues.put("tags", note.getTags());

        System.out.println("TAGS: " + note.getTags());
        long insertedRowId = sqLiteDatabase.insert("notes", null, contentValues);
        sqLiteDatabase.close();
        note.setId(insertedRowId);


        return insertedRowId;
    }

    public ArrayList<Note> getAllNotes(User user) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE owner_id = ?", new String[]{String.valueOf(user.getId())});


        ArrayList<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()) {
            Note note = null;
            try {
                note = new Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        dateFormat.parse(cursor.getString(3)),
                        cursor.getInt(4) == 1,
                        (int) cursor.getLong(5),
                        null
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            notes.add(note);
        }

        return notes;
    }

    public ArrayList<Note> searchAllNotesForUser(String query, User user){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE owner_id = ? AND (title LIKE ? OR content LIKE ? OR tags LIKE ?)", new String[]{String.valueOf(user.getId()), "%" + query + "%", "%" + query + "%", "%" + query + "%"});

        ArrayList<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            Note note = null;
            try {
                note = new Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        dateFormat.parse(cursor.getString(3)),
                        cursor.getInt(4) == 1,
                        (int) cursor.getLong(5),
                        cursor.getString(6)
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            notes.add(note);
        }

        return notes;
    }

    public ArrayList<Note> searchAllFavouriteNotesForUser(String query, User user){
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE owner_id = ? AND isFavorite = 1 AND (title LIKE ? OR content LIKE ? OR tags LIKE ?)", new String[]{String.valueOf(user.getId()), "%" + query + "%", "%" + query + "%", "%" + query + "%"});

        ArrayList<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            Note note = null;
            try {
                note = new Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        dateFormat.parse(cursor.getString(3)),
                        cursor.getInt(4) == 1,
                        (int) cursor.getLong(5),
                        cursor.getString(6)
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            notes.add(note);
        }

        return notes;
    }

    public ArrayList<Note> searchAllNotesForUserSortedBy(String query, User user, boolean order_by_title ){
        // order_by_title = true -> order by title
        // order_by_title = false -> order by date

        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = null;
        if (order_by_title){
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE owner_id = ? AND (title LIKE ? OR content LIKE ? OR tags LIKE ?) ORDER BY title COLLATE NOCASE ASC;", new String[]{String.valueOf(user.getId()), "%" + query + "%", "%" + query + "%", "%" + query + "%"});
        } else {
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE owner_id = ? AND (title LIKE ? OR content LIKE ? OR tags LIKE ?) ORDER BY creationDate", new String[]{String.valueOf(user.getId()), "%" + query + "%", "%" + query + "%", "%" + query + "%"});
        }

        ArrayList<Note> notes = new ArrayList<>();

        while (cursor.moveToNext()){
            Note note = null;
            try {
                note = new Note(
                        cursor.getLong(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        dateFormat.parse(cursor.getString(3)),
                        cursor.getInt(4) == 1,
                        (int) cursor.getLong(5),
                        cursor.getString(6)
                );
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            notes.add(note);
        }

        return notes;
    }







    public void updateNote (Note note){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("tags", note.getTags());
        contentValues.put("isFavorite", note.isFavorite());
        sqLiteDatabase.update("notes", contentValues, "note_id = ?", new String[]{String.valueOf(note.getId())});
        sqLiteDatabase.close();
    }

    public void deleteNote (Note note){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete("notes", "note_id = ?", new String[]{String.valueOf(note.getId())});
        sqLiteDatabase.close();
    }

    public void updateIsFavorite(Note note){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("isFavorite", note.isFavorite());
        sqLiteDatabase.update("notes", contentValues, "note_id = ?", new String[]{String.valueOf(note.getId())});
        sqLiteDatabase.close();
    }


}

