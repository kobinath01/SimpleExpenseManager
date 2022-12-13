package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DATABASE_NAME = "200308F";
    //tables
    public static final String TABLE_ACCOUNT = "accounts";
    public static final String TABLE_TRANSACTION = "transactions";

    //keys
    public static final String accNo = "accountNo";
    public static final String bankName = "bankName";
    public static final String accHolderName = "accountHolderName";
    public static final String balance = "balance";
    private static final String transactionID = "id";
    public static final String expenseType = "expenseType";
    public static final String amount = "amount";
    public static final String date = "date";


    public DatabaseHelper(@Nullable Context c) {
        super(c, DATABASE_NAME, null, 7);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE TABLE_ACCOUNT (accNO VARCHAR(20) PRIMARY KEY, bankName VARCHAR(20), accountHolderName VARCHAR(20), balance DECIMAL(15,2) )");
        sqLiteDatabase.execSQL("CREATE TABLE TABLE_TRANSACTION (transactionID INTEGER PRIMARY KEY AUTOINCREMENT, accNO VARCHAR(20), date VARCHAR(20), amount DECIMAL(15,2), type VARCHAR(20) )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int k ) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TABLE_ACCOUNT");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TABLE_TRANSACTION");
        onCreate(sqLiteDatabase);
    }
}
