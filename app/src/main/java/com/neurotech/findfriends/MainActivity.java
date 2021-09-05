package com.neurotech.findfriends;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.hbb20.CountryCodePicker;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;
import com.onesignal.OneSignal;

import java.util.ArrayList;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {

    private CountryCodePicker ccp;
    private EditText editTextCarrierNumber;
    private Button createContacts;

    private InterstitialAd mInterstitialAd;

    private EditText numberOfContacts;
    private Button deleteCreatedContacts;
    private Button tutorial;
    private ImageView playIcon;
    private TextView privacyPolicty;

    private String userNumber;
    private String mNumberOfContacts;

    private String actualNumber;

    private  Random rand;

    private String privacyPolictyLink = "http://www.google.com";

    private ArrayList<String> numberList;
    private ArrayList<String> tempNumberList;
    private ArrayList<String> newNumbersList;

    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    private DeletingContactsDialog deletingContactsDialog;

    private LoadingAdDialog loadingAdDialog;

    final String appPackageName = "com.EniferTech.familytree";  //your app package name here


    private String[] permissions = {Manifest.permission.READ_CONTACTS,
            Manifest.permission.WRITE_CONTACTS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindViews();
        initialize();

        requestPermissions(permissions);

        initializeOneSignal();
      //  editTextCarrierNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);


    }

    private void bindViews(){

        ccp = (CountryCodePicker) findViewById(R.id.ccp);
        editTextCarrierNumber = (EditText) findViewById(R.id.editText_carrierNumber);
        numberOfContacts = findViewById(R.id.number_of_contacts_edit_text);
        createContacts  = findViewById(R.id.create_Contacts);
        deleteCreatedContacts = findViewById(R.id.delete_created_contacts);
        tutorial = findViewById(R.id.tutorial);
        playIcon = findViewById(R.id.play_icon);
        privacyPolicty = findViewById(R.id.privacy_policy_textview);

        maleRadioButton = findViewById(R.id.male_radio_button);
        femaleRadioButton = findViewById(R.id.female_radio_button);
        femaleRadioButton.setChecked(true);


        ccp.registerCarrierNumberEditText(editTextCarrierNumber);
        createContacts.setClickable(false);


    }


    private void initialize(){
        initializingAds();
        deletingContactsDialog  = new DeletingContactsDialog(this);
        loadingAdDialog  = new LoadingAdDialog(this);
        rand = new Random();
        ccp.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                editTextCarrierNumber.setText("");
                editTextCarrierNumber.requestFocus();
            }
        });


        editTextTouchListener();

        maleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleRadioButton.setChecked(true);
                femaleRadioButton.setChecked(false);

            }
        });

        femaleRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                maleRadioButton.setChecked(false);
                femaleRadioButton.setChecked(true);
            }
        });


        createContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(ccp.isValidFullNumber()){

                    userNumber = ccp.getFullNumberWithPlus();
                    mNumberOfContacts = numberOfContacts.getText().toString().trim();

                    if(!mNumberOfContacts.isEmpty() && !mNumberOfContacts.equals("0")){

                        newNumbersList= new ArrayList<>();
                        String finalNumber = generateNumber(userNumber);


                        newNumbersList();
                        loadingAdDialog.show();
                        Timer t = new Timer(false);
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {
                                        loadingAdDialog.dismiss();


                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();

                                            deletingContactsDialog.show();

                                        Timer t = new Timer(false);
                        t.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    public void run() {



                                        for (int i=0;i<newNumbersList.size();i++){

                                            ArrayList<ContactNumber> contactNumbers= DatabaseHelper.getInstance(getBaseContext()).getAllNumber();

                                            if (i<contactNumbers.size()){
                                                if(contactNumbers.get(i).getmDisplayName().equals("NewFriend "+String.valueOf(i))){
                                                    Log.d(Constants.DEBUG_TAG," this name already exist");

                                                    DatabaseHelper.getInstance(getBaseContext()).addContact(new ContactNumber("NewFriend "+String.valueOf(getRandomString(4)),newNumbersList.get(i)));

                                                    addContact("NewFriend "+String.valueOf(getRandomString(4)),newNumbersList.get(i));


                                                }else{
                                                    Log.d(Constants.DEBUG_TAG," this name already Not exist");
                                                }

                                            }else{
                                                DatabaseHelper.getInstance(getBaseContext()).addContact(new ContactNumber("NewFriend "+String.valueOf(i),newNumbersList.get(i)));
                                                addContact("NewFriend "+String.valueOf(i),newNumbersList.get(i));
                                                Log.d(Constants.DEBUG_TAG,"add new numbers here"+i);
                                            }


                                            //

                                        }


                                        Intent intent = new Intent(MainActivity.this, NumbersListActivity.class);
                                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);

                                        deletingContactsDialog.dismiss();
                                       // Toast.makeText(getBaseContext(),"All Numbers Are Successfully Deleted",Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }, 2000);

                                        } else {
                                            Log.d("TAG", "The interstitial wasn't loaded yet.");
                                        }


                                    }
                                });
                            }
                        }, 2000);



                    }else{
                        numberOfContacts.setError("Required");
                        numberOfContacts.requestFocus();
                    }
                }else{
                    Log.d(Constants.DEBUG_TAG," invalid number");
                }
            }
        });


        ccp.setPhoneNumberValidityChangeListener(new CountryCodePicker.PhoneNumberValidityChangeListener() {
            @Override
            public void onValidityChanged(boolean isValidNumber) {
                // your code

                if(isValidNumber){
                    editTextCarrierNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    createContacts.setClickable(true);
                }else{
                    createContacts.setClickable(false);
                    editTextCarrierNumber.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_red_warning_24dp, 0);
                }

            }
        });


        deleteCreatedContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!DatabaseHelper.getInstance(getBaseContext()).checkIfNodeExist()){

                    Log.d(Constants.DEBUG_TAG,"Their is No Number ");
                    Toast.makeText(getBaseContext(),"Their Is No Number To Delete",Toast.LENGTH_LONG).show();


                }else{
                    final ArrayList<ContactNumber> contactNumbers  = DatabaseHelper.getInstance(getBaseContext()).getAllNumber();

//                    Toast.makeText(getBaseContext(),String.valueOf(contactNumbers.size()),Toast.LENGTH_LONG).show();


                                    deletingContactsDialog.show();

                    Timer t = new Timer(false);
                    t.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {



                                    for (int i=0;i<contactNumbers.size();i++)
                                    {

                                        Log.d(Constants.DEBUG_TAG,getContactName(getBaseContext(),contactNumbers.get(i).getmPhoneNumber()));

                                        ContactHelper.deleteContact(getContentResolver(),
                                                contactNumbers.get(i).getmPhoneNumber());


                                    }

                                    DatabaseHelper.getInstance(getBaseContext()).deleteAll();

                                    deletingContactsDialog.dismiss();
                                    Toast.makeText(getBaseContext(),"All Numbers Are Successfully Deleted",Toast.LENGTH_LONG).show();



                                }
                            });
                        }
                    }, 2000);







                }
            }
        });

        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HelpActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        playIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //getPackageName(); // getPackageName() from Context or Activity object
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            }
        });

        privacyPolicty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(privacyPolictyLink); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
    }

    private void newNumbersList() {
        for (int i=0;i<Integer.parseInt(mNumberOfContacts);i++){

            newNumbersList.add(generateNumber(userNumber));

          //  Log.d(Constants.DEBUG_TAG,"number->"+String.valueOf(i)+"--Added To the Liset");


        }
    }

    private String generateNumber(String userNumber) {
        numberList= new ArrayList<>();
        tempNumberList= new ArrayList<>();

        String numberFirstHalf = getUserNumberFirstHalf(userNumber);
        String numberSecondHalf = getUserNumberSecondHalf(userNumber);

        String finalNumber = numberFirstHalf;


        for (char ch : numberSecondHalf.toCharArray()) {
            tempNumberList.add(String.valueOf(ch));
        }


        for (int i=0;i<tempNumberList.size();i++){

            numberList.add(String.valueOf(randInt(Integer.parseInt(tempNumberList.get(i)),9)));

        }

        for (int i=0;i<numberList.size();i++){

            finalNumber+=numberList.get(i) ;

        }




        return finalNumber;

    }

    private String getUserNumberFirstHalf(String userNumber){
        final int mid = userNumber.length() / 2; //get the middle of the String
        String[] parts = {userNumber.substring(0, mid),userNumber.substring(mid)};
        String partOne = parts[0];
        String partTwo =parts[1];

       // Log.d(Constants.DEBUG_TAG," Part 1------------"+partOne);
        return partOne;
    }

    private String getUserNumberSecondHalf(String userNumber){
        final int mid = userNumber.length() / 2; //get the middle of the String
        String[] parts = {userNumber.substring(0, mid),userNumber.substring(mid)};
        String partOne = parts[0];
        String partTwo =parts[1];

      //  Log.d(Constants.DEBUG_TAG," Part 2------------"+partTwo);

        return partTwo;
    }

    private void editTextTouchListener() {


            editTextCarrierNumber.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_TOP = 1;
                    final int DRAWABLE_RIGHT = 2;
                    final int DRAWABLE_BOTTOM = 3;
                    try {
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (event.getRawX() >= (editTextCarrierNumber.getRight() - editTextCarrierNumber.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            // your action here
                            editTextCarrierNumber.setError("Enter Valid Number");
                            return true;
                        }
                    }

                }catch (Exception e){
                    Toast.makeText(getBaseContext(),"Error",Toast.LENGTH_LONG).show();
                }
                    return false;
                }
            });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_bar, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.rate_us_button){
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
           Log.d(Constants.DEBUG_TAG,"rate us button");

        }


        if(item.getItemId() == R.id.share_button){

            Log.d(Constants.DEBUG_TAG,"share button");

            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Check This App This Is Awesome https://play.google.com/store/apps/details?id="+appPackageName;
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share via"));

        }


        return super.onOptionsItemSelected(item);
    }



    public int randInt(int min, int max) {
        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }


    private void requestPermissions(String[] permissions) {
        Permissions.check(this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
            @Override
            public void onGranted() {
                // do your task.
                initialize();
            }

            @Override
            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
                Toast.makeText(getBaseContext(),"denied",Toast.LENGTH_LONG).show();

                finish();
            }
        });
    }


    private void addContact(String displayName,String userNumber){
        String DisplayName = displayName;
        String MobileNumber = userNumber;
        String HomeNumber = "";
        String WorkNumber = "";
        String emailID = "";
        String company = "";
        String jobTitle = "";

        ArrayList < ContentProviderOperation > ops = new ArrayList <ContentProviderOperation> ();

        ops.add(ContentProviderOperation.newInsert(
                ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        //------------------------------------------------------ Names
        if (DisplayName != null) {
            ops.add(ContentProviderOperation.newInsert(
                    ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(
                            ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            DisplayName).build());
        }

        //------------------------------------------------------ Mobile Number
        if (MobileNumber != null) {
            ops.add(ContentProviderOperation.
                    newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, MobileNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                    .build());
        }

        //------------------------------------------------------ Home Numbers
        if (HomeNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, HomeNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_HOME)
                    .build());
        }

        //------------------------------------------------------ Work Numbers
        if (WorkNumber != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, WorkNumber)
                    .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                            ContactsContract.CommonDataKinds.Phone.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Email
        if (emailID != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Email.DATA, emailID)
                    .withValue(ContactsContract.CommonDataKinds.Email.TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                    .build());
        }

        //------------------------------------------------------ Organization
        if (!company.equals("") && !jobTitle.equals("")) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, company)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, jobTitle)
                    .withValue(ContactsContract.CommonDataKinds.Organization.TYPE, ContactsContract.CommonDataKinds.Organization.TYPE_WORK)
                    .build());
        }

        // Asking the Contact provider to create a new contact
        try {
            getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Exception: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }


    private void getContactList(ArrayList<ContactNumber> contactNumbers) {

        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));

                        Log.i(Constants.DEBUG_TAG, "Name: " + name);
                        Log.i(Constants.DEBUG_TAG, "Phone Number: " + phoneNo);

                        /*for (int i=0;i<contactNumbers.size();i++)
                            {

                                if (name.equals(contactNumbers.get(i).getmDisplayName()) && phoneNo.equals(contactNumbers.get(i).getmPhoneNumber())) {
                                    Log.i(Constants.DEBUG_TAG, "Phone Number Matched With Name --: " + name + "---and Number--" + phoneNo);
                                } else {
                                    Log.i(Constants.DEBUG_TAG, "do Nothing: " + name);
                                }


                            }*/







                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }

    private static final String ALLOWED_CHARACTERS ="qwertyuiopasdfghjklzxcvbnm";

    private String getRandomString(final int sizeOfRandomString)
    {
        final Random random=new Random();
        final StringBuilder sb=new StringBuilder(sizeOfRandomString);
        for(int i=0;i<sizeOfRandomString;++i)
            sb.append(ALLOWED_CHARACTERS.charAt(random.nextInt(ALLOWED_CHARACTERS.length())));
        return sb.toString();
    }


    public static String getContactName(Context context, String phoneNumber) {
        ContentResolver cr = context.getContentResolver();
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
        Cursor cursor = cr.query(uri, new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null);
        if (cursor == null) {
            return null;
        }
        String contactName = null;
        if(cursor.moveToFirst()) {
            contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
        }

        if(cursor != null && !cursor.isClosed()) {
            cursor.close();
        }

        return contactName;
    }

    private void initializeOneSignal(){
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    private void initializingAds(){

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

    }
}
