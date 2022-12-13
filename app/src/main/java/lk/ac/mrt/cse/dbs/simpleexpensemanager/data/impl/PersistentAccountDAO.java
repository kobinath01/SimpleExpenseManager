package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DatabaseHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private DatabaseHelper databaseHelper;

    public PersistentAccountDAO(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public List<String> getAccountNumbersList() {
        List<String> accNumList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT "+ DatabaseHelper.accNo +" FROM " + DatabaseHelper.TABLE_ACCOUNT, null);

        if (cursor.getCount()==0) {
            do {
                accNumList.add(cursor.getString(0));
            } while (cursor.moveToNext());
        }

        cursor.close();
        return accNumList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accList = new ArrayList<>();

        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ACCOUNT, null);


        if (cursor.moveToFirst()) {
            do {
                Account account = new Account(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        Double.parseDouble(cursor.getString(3))
                );
                accList.add(account);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return accList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_ACCOUNT + " WHERE " + DatabaseHelper.accNo +" = " + accountNo, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(
                    cursor.getString(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    Double.parseDouble(cursor.getString(3))
            );
            cursor.close();
            return account;
        }
        String alert = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(alert);
    }
    @Override
    public void addAccount(Account account) {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.accNo, account.getAccountNo());
        values.put(DatabaseHelper.bankName, account.getBankName());
        values.put(DatabaseHelper.accHolderName, account.getAccountHolderName());
        values.put(DatabaseHelper.balance, account.getBalance());
        db.insert(DatabaseHelper.TABLE_ACCOUNT, null, values);
        db.close(); // Close database connection
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_ACCOUNT, DatabaseHelper.accNo + " = ?",
                new String[] { accountNo });
        db.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        switch (expenseType) {
            case EXPENSE:
                values.put(DatabaseHelper.balance, account.getBalance() - amount);
                break;
            case INCOME:
                values.put(DatabaseHelper.balance, account.getBalance() + amount);
                break;

        }
        db.update(DatabaseHelper.TABLE_ACCOUNT, values, DatabaseHelper.accNo + " = ?",
                new String[] { accountNo });
    }

}
