package com.example.encryptionanddecryption;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EncryptActivity extends AppCompatActivity {

    private TextInputEditText msgEdt;
    private TextView encryptDataTV;
    private Button encryptBtn, copyBtn;
    private String key = "ABCDEFGHIGHLKMNO";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt);
        msgEdt = findViewById(R.id.idEdtMsg);
        encryptBtn = findViewById(R.id.idBtnEncrypt);
        copyBtn = findViewById(R.id.idBtnCopy);
        encryptDataTV = findViewById(R.id.idTVEncryptedData);
        copyBtn.setEnabled(false);
        SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), "AES");
        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(msgEdt.getText().toString().isEmpty()) {
                    Toast.makeText(EncryptActivity.this, "Please enter message to encrypt!!", Toast.LENGTH_SHORT).show();
                }else{
                    copyBtn.setEnabled(true);
                    try {
                        encryptDataTV.setText(encryptMsg(msgEdt.getText().toString(),secretKeySpec));
                    } catch (Exception e) {
                        Toast.makeText(EncryptActivity.this, "Fail to Encrypt Data : "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }

                }
            }
        });
        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label",encryptDataTV.getText());
                clipboardManager.setPrimaryClip(clip);
                Toast.makeText(EncryptActivity.this, "Copied to Clipboard!!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String encryptMsg(String message, SecretKey secretKey) throws  Exception {
        Cipher cipher = null;
        cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE,secretKey);
        byte[] cipherText = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(cipherText,Base64.NO_WRAP);
    }
}