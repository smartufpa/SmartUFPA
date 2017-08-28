package com.example.kaeuc.smartufpa.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.example.kaeuc.smartufpa.utils.MD5Hashing;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kaeuc on 10/5/2016.
 */

public class LoginDAO extends SQLiteOpenHelper {
    // Constantes com o nome de colunas e do banco de dados
    private static final String DB_NAME = "login";
    private static final String USERNAME_COLUMN = "username";
    private static final String PASSWORD_COLUMN = "password";
    private static final String PROFILE_COLUMN = "profile";

    private static final String SALT_COLUMN = "salt";
    private final int KEY_LENGHT=128;
    private final int SALT_LENGHT= KEY_LENGHT/8;
    private byte[] salt;
    private static int VERSION = 1;


    public LoginDAO(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    // Inicialização do banco de dados
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DB_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE , " +
                USERNAME_COLUMN +" TEXT NOT NULL ,"+ PASSWORD_COLUMN +" TEXT NOT NULL ,"
                +PROFILE_COLUMN+ " TEXT NOT NULL ," + SALT_COLUMN +" TEXT NOT NULL );");
    }

    // Método para atualização do banco
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // Método público para adicionar um usuário ao banco

    public void addUser(String username, String password,String profile, Context context){
        if(username.isEmpty()){
            // TODO (POSTPONED): 1. Transferir essa checagem para o formulário
            Toast.makeText(context, "Campo de usuário em branco, tente novamente.", Toast.LENGTH_LONG).show();
        }else{
            // Recupera o banco para poder inserir a o conteúdo
            SQLiteDatabase db = getWritableDatabase();
            ContentValues values = new ContentValues();
            SecureRandom random = new SecureRandom();

            salt = new byte[SALT_LENGHT];
            random.nextBytes(salt);
            String stringSalt = salt.toString();

            values.put(SALT_COLUMN,stringSalt);
            values.put(USERNAME_COLUMN, username);
            // adiciona o hash à senha
            password = MD5Hashing.applyHash(password+salt);
            values.put(PASSWORD_COLUMN,password);
            values.put(PROFILE_COLUMN,profile);
            long insert = db.insert(DB_NAME, null, values);

            // TODO POSTPONED): 2. Tratar resultados vindo do SQL
            if(insert != -1){
                Toast.makeText(context, "Usuário Criado com Sucesso!", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(context, "Falha", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public int checkLogin(String username, String password){
        String checkUserName, checkPassword, checkSalt;
        ArrayList<Map<String, String>> users = listUsers();

        if (users.size() == 0){
            return 0;
        }
        for (Map<String, String> map : users) {
            checkUserName = map.get(USERNAME_COLUMN);
            checkPassword = map.get(PASSWORD_COLUMN);
            checkSalt = map.get(SALT_COLUMN);

            final String hash = MD5Hashing.applyHash(password+checkSalt);
            if (checkUserName.equals(username) && checkPassword.equals(hash)){
                return 1;
            }
        }


        return -1;
    }

    // Método publico para recuperar os usuários do banco de dados
    private ArrayList<Map<String, String>> listUsers(){
        // Acha o banco de onde os dados serão lidos
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Map<String, String>> users = new ArrayList<>();

        // Cria a SELECT QUERY
        Cursor cursor = db.query(DB_NAME,
                new String[]{USERNAME_COLUMN, PASSWORD_COLUMN,PROFILE_COLUMN,SALT_COLUMN},
                null,null,null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount()!= 0) {
            for (int i = 0; i < cursor.getCount(); i++) {
                // Cria um mapa para guardar informação do usuário
                Map<String, String> item = new HashMap<>();

                String username = cursor.getString(0);
                String password = cursor.getString(1);
                String profile = cursor.getString(2);
                String salt = cursor.getString(3);


                item.put(USERNAME_COLUMN, username);
                item.put(PASSWORD_COLUMN, password);
                item.put(PROFILE_COLUMN,profile);
                item.put(SALT_COLUMN,salt);


                users.add(item);
                cursor.moveToNext();

            }
        }
        cursor.close();
        return users;
    }

    // Método que retorna a id do usuário
    public int getId(String username){
        SQLiteDatabase db = getReadableDatabase();
        int userID = 0;
        final String[] selectColumn = {"_id"};
        String whereClause = USERNAME_COLUMN + " =?";
        String [] whereArgs = {username};
        Cursor cursor = db.query(DB_NAME,selectColumn,whereClause,whereArgs,null,null,null);
        cursor.moveToFirst();
        if (cursor.getCount() != 0) {
            userID = cursor.getInt(0);
            cursor.close();

        }
        return userID;
    }


}
