package rd.nakorn.rdrun;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by nakornmalai on 9/2/2016 AD.
 */
public class MyManage {

    //Explicit
    private MyOpenHelper myOpenHelper;
    private SQLiteDatabase sqLiteDatabase;



    public MyManage(Context context) {

        myOpenHelper = new MyOpenHelper(context);
        sqLiteDatabase = myOpenHelper.getWritableDatabase();


    }//constructor
}//main class
