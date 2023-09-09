package edu.birzeit.nidlibraheem.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import edu.birzeit.nidlibraheem.finalproject.models.Note;
import edu.birzeit.nidlibraheem.finalproject.models.User;
import edu.birzeit.nidlibraheem.finalproject.utils.PasswordHash;

public class ORM extends SQLiteOpenHelper {

    private static ORM instance;

    private ORM(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static synchronized ORM getInstance(Context context) {
        if (instance == null) {
            instance = new ORM(context, "notes_app_db", null, 7);
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
                "CREATE TABLE tags (" +
                        "    tag_id INTEGER PRIMARY KEY," +
                        "    owner_id INTEGER NOT NULL," +
                        "    tag_name TEXT NOT NULL," +
                        "    FOREIGN KEY (owner_id) REFERENCES Users(user_id)" +
                        ");");
        sqLiteDatabase.execSQL(
                "CREATE TABLE notes (" +
                        "    note_id INTEGER PRIMARY KEY," +
                        "    title TEXT," +
                        "    content TEXT," +
                        "    creationDate DATETIME," +
                        "    isFavorite INTEGER," +
                        "    owner_id INTEGER NOT NULL REFERENCES users(user_id)," +
                        "    tag_id INTEGER REFERENCES tags(tag_id)" +

                        ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS notes");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS tags");
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

    public long insertNote(Note note){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("title", note.getTitle());
        contentValues.put("content", note.getContent());
        contentValues.put("creationDate", note.getCreationDate().toString());
        contentValues.put("isFavorite", note.isFavorite());
        contentValues.put("owner_id", note.getOwnerId());
//        contentValues.put("tag_id", note.getTag().getId());
        long insertedRowId = sqLiteDatabase.insert("notes", null, contentValues);
        sqLiteDatabase.close();
        note.setId(insertedRowId);


        return insertedRowId;
    }

        public Cursor getAllNotes(User user) {
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM notes WHERE owner_id = ?", new String[]{String.valueOf(user.getId())});
        return cursor;
    }



}

