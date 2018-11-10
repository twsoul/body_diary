package com.example.a1013c.body_sns;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.fingerprint.FingerprintManager;
import android.nfc.Tag;
import android.os.Build;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static android.graphics.Color.RED;

public class login_body extends AppCompatActivity {


    private static final String TAG = login_body.class.getSimpleName();

    private static final String DIALOG_FRAGMENT_TAG = "myFragment";
    private static final String SECRET_MESSAGE = "Very secret message";
    private static final String KEY_NAME_NOT_INVALIDATED = "key_not_invalidated";
    static final String DEFAULT_KEY_NAME = "default_key";

    private KeyStore mKeyStore;
    private KeyGenerator mKeyGenerator;
    private SharedPreferences mSharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_the_body);

        //휴대폰 고유 번호로 받아와서 사용자 구별 <--> 닉네임 으로 구별.  닉네임 설정할 때 중복 검사
//        @SuppressLint("HardwareIds") String idByANDROID_ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        Toast.makeText(this,idByANDROID_ID,Toast.LENGTH_SHORT).show();

        Button login = findViewById(R.id.login_button);
        final Button membership = (Button) findViewById(R.id.join_member);

        //핸드폰 고유번호로 구별하자
//        TelephonyManager id = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        id.getDeviceId();
//        Toast.makeText(getApplicationContext(), (CharSequence) id,Toast.LENGTH_SHORT).show();

//        시작할때 로딩 화면 1초
        Intent intent = new Intent(this, loding_activity.class);
        startActivity(intent);


        // 자동 로그인 // 체크 박스 저장 되있는거 검사.
        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        if(pref.getBoolean("auto_login",false)){
            Intent i = new Intent(login_body.this, Navigation_activity.class);
            startActivityForResult(i,1000);
            Toast.makeText(getApplicationContext(),"자동 시작 성공",Toast.LENGTH_SHORT).show();
            finish();
        }



        //<비밀 번호 설정>을 눌렀을 때 동작
        membership.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //클릭했을때  인텐트를 생성해서  로그인 페이지 --> 회원가입 페이지
                AlertDialog.Builder builder = new AlertDialog.Builder(login_body.this);
                final View view = LayoutInflater.from(login_body.this)
                        .inflate(R.layout.login_the_body_membership, null, false);
                builder.setView(view);


                final TextView user =view.findViewById(R.id.text_id);
                final EditText user_id = view.findViewById(R.id.user_id); // 현재 패스워드 입력란
                final EditText user_pw = view.findViewById(R.id.user_pw); // 패스워드 입력란
                final EditText user_pw_ag = view.findViewById(R.id.user_pw_agree); // 패스워드 확인 입력란

                Button cancel = view.findViewById(R.id.cancel); //취소 버튼
                Button join = view.findViewById(R.id.join); //저장 버튼

                //한번이라도 저장 --> 현재 비밀번호 창
                final SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                if(pref.getBoolean("now_pw",false)){
                    //현재 비밀 번호 창 보이게 한다.
                    user_id.setVisibility(View.VISIBLE);
                    user.setVisibility(View.VISIBLE);
                }


                //일치 불일치 나타내는 텍스트


                final AlertDialog dialog = builder.create();

                // pw <--> pw확인  일치 하는지 확인.
                user_pw_ag.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }
                    //틀리면 색변화.
                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                        String ed_pw = user_pw.getText().toString();
                        String ed_pw1= user_pw_ag.getText().toString();

                        if(ed_pw.equals(ed_pw1)){
                            TextView  a = view.findViewById(R.id.ox);
                            a.setText("비밀 번호가 일치 합니다.");
                            a.setTextColor(Color.BLUE);
                        }else{
                            TextView  a = view.findViewById(R.id.ox);
                            a.setText("비밀 번호가 일치 하지 않습니다.");
                            a.setTextColor(RED);
                        }
                    }
                    @Override
                    public void afterTextChanged(Editable s) {
                    }
                });

                //저장(가입) 버튼  눌렀을때, 쉐어드에 비밀번호 저장
                join.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(user_pw.length()!=4){
                            Toast.makeText(login_body.this, "4자리를 입력하세요.", Toast.LENGTH_SHORT).show();
                            user_pw.requestFocus();
                            return;
                        }if(user_pw_ag.length()!=4){
                            Toast.makeText(login_body.this, "4자리를 입력하세요.", Toast.LENGTH_SHORT).show();
                            user_pw_ag.requestFocus();
                            return;
                        }
                        if(pref.getBoolean("now_pw",false)){ //  true 이면 통과
                            if(!user_id.getText().toString().equals(pref.getString("pw",null))){ // 현재 pw 와 입력한 pw 다르면 못함.
                                Toast.makeText(login_body.this, "비밀번호가 다릅니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }

                        SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();

                        editor.putBoolean("now_pw",true); // 현재 비밀번호 창 보여 줘라 이제 true

                        EditText user_pw1 = view.findViewById(R.id.user_pw);
                        editor.putString("pw", String.valueOf(user_pw1.getText())); // 입력한 비밀 번호 저장.
                        editor.commit(); // 저장


                        Toast.makeText(login_body.this, "pw 저장 완료.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });

                //취소 버튼
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
//

                    dialog.show();// 다이얼로그 보여줌

            }
        });

        //<시작 버튼>을 눌렀을 때 동작
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //비밀번호 액티비티
                SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                EditText pw = findViewById(R.id.get_pw);

//                Log.d("pw",pref.getString("pw",null));
                //비밀번호 일치.하면 로그인..!
                if (pw.getText().toString().equals(pref.getString("pw",null))) {
                    Intent i = new Intent(login_body.this, Navigation_activity.class);
                    startActivityForResult(i,1000);
                    Toast.makeText(getApplicationContext(),"로그인 성공",Toast.LENGTH_SHORT).show();

                    // 자동 로그인  // 체크박스 값 받아서 저장.
                    CheckBox auto = findViewById(R.id.auto_login);

                    SharedPreferences.Editor editor = pref.edit();

                    editor.putBoolean("auto_login",auto.isChecked());
                    editor.apply(); // 저장

                    finish();    //로그인 성공하면 액티비티 destroy 시킴
                } else{
                    Toast.makeText(getApplicationContext(),"비밀 번호를 다시 확인 하세요.",Toast.LENGTH_SHORT).show();

                }
            }
        });


        //앱 실행시 환영인사
        String version;

        try {

            PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);

            version = i.versionName;

        } catch (PackageManager.NameNotFoundException e) {

            version = "";

        }



        SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다

        editor.putString("check_version", version); // 저장할 값들을 입력합니다.
        editor.commit(); // 저장합니다.



        String check_version = pref.getString("check_version", ""); //버전

        String check_status = pref.getString("check_status", ""); // 다시 보지 않음 상태

        if (!check_version.equals(check_status)) {

            AlertDialog alert = new AlertDialog.Builder(this).setIcon(R.drawable.body_logo)

                    .setTitle("Body Diary")
                    .setMessage("1. 운동 후, 몸 사진을 저장하세요.\n2. 식후 무엇을 먹었는지 저장하세요.\n3. 기록들을 보며 반성하세요. ")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }

                    })
                    .setNegativeButton("다시 보지 않음", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) { // 다시보지않음 선택
                            String version;
                            try {
                                PackageInfo i = getPackageManager().getPackageInfo(getPackageName(), 0);
                                version = i.versionName;
                            } catch (PackageManager.NameNotFoundException e) {
                                version = "";
                            }
                            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = pref.edit(); // Editor를 불러온다
                            editor.putString("check_status", version);
                            editor.commit(); // 저장
                            dialog.cancel();

                        }

                    }).show();

        }



//ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

        try {
            mKeyStore = KeyStore.getInstance("AndroidKeyStore");
        } catch (KeyStoreException e) {
            throw new RuntimeException("Failed to get an instance of KeyStore", e);
        }
        try {
            mKeyGenerator = KeyGenerator
                    .getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
            throw new RuntimeException("Failed to get an instance of KeyGenerator", e);
        }
        Cipher defaultCipher;
        Cipher cipherNotInvalidated;
        try {
            defaultCipher = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
            cipherNotInvalidated = Cipher.getInstance(KeyProperties.KEY_ALGORITHM_AES + "/"
                    + KeyProperties.BLOCK_MODE_CBC + "/"
                    + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get an instance of Cipher", e);
        }
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        KeyguardManager keyguardManager = getSystemService(KeyguardManager.class);
        FingerprintManager fingerprintManager = getSystemService(FingerprintManager.class);
        Button purchaseButton = findViewById(R.id.purchase_button);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

        } else {
//            // Hide the purchase button which uses a non-invalidated key
//            // if the app doesn't work on Android N preview
//            purchaseButtonNotInvalidated.setVisibility(View.GONE);
//            findViewById(R.id.purchase_button_not_invalidated_description).setVisibility(View.GONE);
        }

        //지문 등록 안되어있으면
        if (!keyguardManager.isKeyguardSecure()) {
            // Show a message that the user hasn't set up a fingerprint or lock screen.
            Toast.makeText(this,
                    "지문 등록을 하세요. 설정 -> 보안 -> 지문등록",
                    Toast.LENGTH_LONG).show();
            purchaseButton.setEnabled(false);

            return;
        }

        // Now the protection level of USE_FINGERPRINT permission is normal instead of dangerous.
        // See http://developer.android.com/reference/android/Manifest.permission.html#USE_FINGERPRINT
        // The line below prevents the false positive inspection from Android Studio
        assert fingerprintManager != null;
        // noinspection ResourceType

        //지문 등록 안되어있으면
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            purchaseButton.setEnabled(false);
            // This happens when no fingerprints are registered.
            Toast.makeText(this,
                    "Go to 'Settings -> Security -> Fingerprint' and register at least one" +
                            " fingerprint",
                    Toast.LENGTH_LONG).show();
            return;
        }
        createKey(DEFAULT_KEY_NAME, true);
        createKey(KEY_NAME_NOT_INVALIDATED, false);
        purchaseButton.setEnabled(true);
        purchaseButton.setOnClickListener(
                new PurchaseButtonClickListener(defaultCipher, DEFAULT_KEY_NAME));


        //지문 인식
        //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
    }



    /**
     * Initialize the {@link Cipher} instance with the created key in the
     * {@link #createKey(String, boolean)} method.
     *
     * @param keyName the key name to init the cipher
     * @return {@code true} if initialization is successful, {@code false} if the lock screen has
     * been disabled or reset after the key was generated, or if a fingerprint got enrolled after
     * the key was generated.
     */
    private boolean initCipher(Cipher cipher, String keyName) {
        try {
            mKeyStore.load(null);
            SecretKey key = (SecretKey) mKeyStore.getKey(keyName, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return true;
        } catch (KeyPermanentlyInvalidatedException e) {
            return false;
        } catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException
                | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    /**
     * Proceed the purchase operation
     *
     * @param withFingerprint {@code true} if the purchase was made by using a fingerprint
     * @param cryptoObject the Crypto object
     */
    public void onPurchased(boolean withFingerprint,
                            @Nullable FingerprintManager.CryptoObject cryptoObject) {
        if (withFingerprint) {
            // If the user has authenticated with fingerprint, verify that using cryptography and
            // then show the confirmation message.
            assert cryptoObject != null;
            tryEncrypt(cryptoObject.getCipher());
        } else {
            // Authentication happened with backup password. Just show the confirmation message.
            showConfirmation(null);
        }
    }

    // Show confirmation, if fingerprint was used show crypto information.
    private void showConfirmation(byte[] encrypted) {
//        findViewById(R.id.confirmation_message).setVisibility(View.VISIBLE);
        if (encrypted != null) { // 로그인 성공
            Intent i =new Intent(login_body.this,Navigation_activity.class);
            startActivity(i);

            // 자동 로그인  // 체크박스 값 받아서 저장.
            CheckBox auto = findViewById(R.id.auto_login);

            SharedPreferences pref = getSharedPreferences("pref", Activity.MODE_PRIVATE); // UI 상태를 저장합니다.
            SharedPreferences.Editor editor = pref.edit(); // Editor를 불러옵니다

            editor.putBoolean("auto_login",auto.isChecked());
            editor.apply(); // 저장

            finish();

//            TextView v = findViewById(R.id.encrypted_message);
//            v.setVisibility(View.VISIBLE);
//            v.setText(Base64.encodeToString(encrypted, 0 /* flags */));
        }
    }

    /**
     * Tries to encrypt some data with the generated key in {@link #createKey} which is
     * only works if the user has just authenticated via fingerprint.
     */
    private void tryEncrypt(Cipher cipher) {
        try {
            byte[] encrypted = cipher.doFinal(SECRET_MESSAGE.getBytes());
            showConfirmation(encrypted);
        } catch (BadPaddingException | IllegalBlockSizeException e) {
            Toast.makeText(this, "Failed to encrypt the data with the generated key. "
                    + "Retry the purchase", Toast.LENGTH_LONG).show();
            Log.e(TAG, "Failed to encrypt the data with the generated key." + e.getMessage());
        }
    }

    /**
     * Creates a symmetric key in the Android Key Store which can only be used after the user has
     * authenticated with fingerprint.
     *
     * @param keyName the name of the key to be created
     * @param invalidatedByBiometricEnrollment if {@code false} is passed, the created key will not
     *                                         be invalidated even if a new fingerprint is enrolled.
     *                                         The default value is {@code true}, so passing
     *                                         {@code true} doesn't change the behavior
     *                                         (the key will be invalidated if a new fingerprint is
     *                                         enrolled.). Note that this parameter is only valid if
     *                                         the app works on Android N developer preview.
     *
     */
    public void createKey(String keyName, boolean invalidatedByBiometricEnrollment) {
        // The enrolling flow for fingerprint. This is where you ask the user to set up fingerprint
        // for your flow. Use of keys is necessary if you need to know if the set of
        // enrolled fingerprints has changed.
        try {
            mKeyStore.load(null);
            // Set the alias of the entry in Android KeyStore where the key will appear
            // and the constrains (purposes) in the constructor of the Builder

            KeyGenParameterSpec.Builder builder = new KeyGenParameterSpec.Builder(keyName,
                    KeyProperties.PURPOSE_ENCRYPT |
                            KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                    // Require the user to authenticate with a fingerprint to authorize every use
                    // of the key
                    .setUserAuthenticationRequired(true)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7);

            // This is a workaround to avoid crashes on devices whose API level is < 24
            // because KeyGenParameterSpec.Builder#setInvalidatedByBiometricEnrollment is only
            // visible on API level +24.
            // Ideally there should be a compat library for KeyGenParameterSpec.Builder but
            // which isn't available yet.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                builder.setInvalidatedByBiometricEnrollment(invalidatedByBiometricEnrollment);
            }
            mKeyGenerator.init(builder.build());
            mKeyGenerator.generateKey();
        } catch (NoSuchAlgorithmException | InvalidAlgorithmParameterException
                | CertificateException | IOException e) {
            throw new RuntimeException(e);
        }
    }


    //필요 없는듯..
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
////        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
////        int id = item.getItemId();
////
////        if (id == R.id.action_settings) {
////            Intent intent = new Intent(this, SettingsActivity.class);
////            startActivity(intent);
////            return true;
////        }
//        return super.onOptionsItemSelected(item);
//    }

    private class PurchaseButtonClickListener implements View.OnClickListener {

        Cipher mCipher;
        String mKeyName;

        PurchaseButtonClickListener(Cipher cipher, String keyName) {
            mCipher = cipher;
            mKeyName = keyName;
        }

        @Override
        public void onClick(View view) {
//            findViewById(R.id.confirmation_message).setVisibility(View.GONE);
//            findViewById(R.id.encrypted_message).setVisibility(View.GONE);

            // Set up the crypto object for later. The object will be authenticated by use
            // of the fingerprint.
            if (initCipher(mCipher, mKeyName)) {

                // Show the fingerprint dialog. The user has the option to use the fingerprint with
                // crypto, or you can fall back to using a server-side verified password.
                FingerprintAuthenticationDialogFragment fragment
                        = new FingerprintAuthenticationDialogFragment();
                fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                boolean useFingerprintPreference = mSharedPreferences
                        .getBoolean(getString(R.string.use_fingerprint_to_authenticate_key),
                                true);
                if (useFingerprintPreference) {
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.FINGERPRINT);
                } else {
                    fragment.setStage(
                            FingerprintAuthenticationDialogFragment.Stage.PASSWORD);
                }
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            } else {
                // This happens if the lock screen has been disabled or or a fingerprint got
                // enrolled. Thus show the dialog to authenticate with their password first
                // and ask the user if they want to authenticate with fingerprints in the
                // future
                FingerprintAuthenticationDialogFragment fragment
                        = new FingerprintAuthenticationDialogFragment();
                fragment.setCryptoObject(new FingerprintManager.CryptoObject(mCipher));
                fragment.setStage(
                        FingerprintAuthenticationDialogFragment.Stage.NEW_FINGERPRINT_ENROLLED);
                fragment.show(getFragmentManager(), DIALOG_FRAGMENT_TAG);
            }
        }
    }



    //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ







    private EditText id;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        id=findViewById(R.id.login_id);
//        if(requestCode == 1000 && resultCode == RESULT_OK) {
//        id.setText(data.getStringExtra("ID"));
//        }
//        data.getStringExtra("NICK");
//        a= findViewById(R.id.a);
//        a.setText(data.getStringExtra("NICK"));

    }


    //가입하면 닉네임 넘기기
    private void getPreferences(){
//        EditText t = findViewById(R.id.test);
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
//        pref.getString("hi", "");

//        t.setText(pref.getString("hi", ""));
        if(pref.getString("flag", "").equals("1")){
        Toast.makeText(getApplicationContext(),"닉네임:"+pref.getString("nick","")+
                " 메인:"+pref.getString("main","")+" 직업:"+pref.getString("career",""),Toast.LENGTH_LONG).show();

            SharedPreferences pref1 = getSharedPreferences("pref", MODE_PRIVATE);
            SharedPreferences.Editor editor = pref1.edit();
            editor.putString("flag", "2");
            editor.commit();
        }else{

        }

    }



    //백스페이스 2번 누르면 종료
    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){ //2초안에 한번더 백을 누르면 생명주기 끝냄.
            finish(); // onDestroy()
        }
    }



}

