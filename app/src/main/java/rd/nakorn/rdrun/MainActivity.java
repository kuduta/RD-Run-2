package rd.nakorn.rdrun;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    //Expilcit
    private ImageView imageView;
    private EditText useEditText, passwordEditText;
    private String userString, passwordString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bind widget
        imageView = (ImageView) findViewById(R.id.imageView6);
        useEditText = (EditText) findViewById(R.id.editText5);
        passwordEditText = (EditText) findViewById(R.id.editText6);

        //Load Image from Server
        Picasso.with(this).load("http://swiftcodingthai.com/rd/Image/rd_logo.png")
                .resize(150,150).into(imageView);
    } // Main Method


    //create Inner Class ทำงานเรื่อยๆ
    private class SynUser extends AsyncTask<Void, Void, String> {

        //Explicit
        private Context context;
        private String myUserString, myPasswordString, truePasswordString,
                nameString, surnameString, idString, avataString;
        private static final String urlJSON = "http://swiftcodingthai.com/rd/get_user_master.php";
        private boolean statusABoolean = true;


        //constructor name same name class
        public SynUser(Context context, String myUserString, String myPasswordString) {
            this.context = context;
            this.myUserString = myUserString;
            this.myPasswordString = myPasswordString;

        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                OkHttpClient okHttpClient = new OkHttpClient();
                Request.Builder builder = new Request.Builder();
                Request request = builder.url(urlJSON).build();
                Response response = okHttpClient.newCall(request).execute();
                return response.body().string();

            } catch (Exception e) {

                Log.d("31AugV2", "e doInBack ==> " + e.toString());
                return null;
            }


        }//doInBack

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            Log.d("31AugV3", "JSON ==> " + s);
            try {

                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (myUserString.equals(jsonObject.getString("User"))) {

                        statusABoolean = false;
                        truePasswordString = jsonObject.getString("Password");
                        nameString = jsonObject.getString("Name");
                        surnameString = jsonObject.getString("Surname");
                        idString = jsonObject.getString("id");
                        avataString = jsonObject.getString("Avata");


                    }//if
                }//for

                if (statusABoolean) {
                    //user false
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(context, R.drawable.kon48,
                            "User false",
                            "ไม่มี " + myUserString + " ในฐานข้อมูลของเรา");
                } else if (myPasswordString.equals(truePasswordString)) {

                    Intent intent = new Intent(MainActivity.this, ServiceActivity.class);

                    intent.putExtra("id",idString);
                    intent.putExtra("Avata",avataString);
                    intent.putExtra("User",userString);
                    intent.putExtra("Surname",surnameString);




                    startActivity(intent);

                    Toast.makeText(context,"Welcome"+ nameString +" "+ surnameString,Toast.LENGTH_SHORT).show();

                } else {
                    //Password True
                    MyAlert myAlert = new MyAlert();
                    myAlert.myDialog(context, R.drawable.bird48,
                            "Password false",
                            "PLS Try Again password false!!!");
                }

            } catch (Exception e) {
                Log.d("31AugV4", "e onPost ==>" + e.toString());
            }

        }
    }//SynUser Class


    /////End create Inner Class


    public void clickSignInMain(View view) {

        userString = useEditText.getText().toString().trim();
        passwordString = passwordEditText.getText().toString().trim();

        //check space
        if (userString.equals("") ||  passwordString.equals("")) {

            //have space
            MyAlert myAlert = new MyAlert();
            myAlert.myDialog(this, R.drawable.rat48,"Have Space","PLS Fill all Every Blank" );

        } else {
            //No Space
            SynUser synUser = new SynUser(this,userString,passwordString);
            synUser.execute();


        }

    }

    // Get Event from Click button SignUp

    public void ClickSignUpMain(View view){
        startActivity(new Intent(MainActivity.this, SignUpActivity.class));
    }

}   // Main Class นี่คือ คลาสหลัก
