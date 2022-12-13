package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {
    private DatabaseHelper databaseHelper;

    public PersistentTransactionDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void logTransaction(Date date, String accNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.accNo, accNo);
        values.put(DatabaseHelper.expenseType, expenseType.name()); // Bank Name
        values.put(DatabaseHelper.amount, amount); // Holder Name
        values.put(DatabaseHelper.date, new SimpleDateFormat("yyyy-MM-dd").format(date)); // Date
        db.insert(DatabaseHelper.TABLE_TRANSACTION, null, values);
        db.close();
        Log.d("Came Here", values.toString());
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TRANSACTION, null, null, null, null, null, null);



        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    transactionList.add(transaction);
                } catch (ParseException e1) {
                    e1.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_TRANSACTION, null, null, null, null, null, null, limit + "");

        if (cursor.moveToFirst()) {
            do {
                Date date;

                try {
                    date = new SimpleDateFormat("yyyy-MM-dd").parse(cursor.getString(1));
                    Transaction transaction = new Transaction(
                            date,
                            cursor.getString(2),
                            ExpenseType.valueOf(cursor.getString(3)),
                            Double.parseDouble(cursor.getString(4))
                    );
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }
        cursor.close();
        return transactionList;
    }
}