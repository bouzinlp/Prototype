package com.example.nthucs.prototype;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

/**
 * Created by NTHUCS on 2016/7/1.
 */

public class GalleryActivity extends AppCompatActivity {

    // 讀取外部儲存設備授權請求代碼
    private static final int REQUEST_READ_EXTERNAL_STORAGE_PERMISSION = 101;
    private static final int SELECT_FILE = 1;

    // Picture's original name and image view
    private String fileName;
    private ImageView picture;

    // Picture's file, uri, urlLink;
    private File picFile;
    private Uri picUri;
    private String imageUrl;

    // Search by word
    private String resultText;

    // Food storage
    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();
        String action = intent.getAction();

        // 取得顯示照片的ImageView元件
        picture = (ImageView) findViewById(R.id.picture);

        food = new Food(resultText, fileName);

        if (action.equals("com.example.nthucs.prototype.TAKE_PHOTO")) {
            // new food
            requestStoragePermission();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                onSelectFromGalleryResult(data);
            }
        }
    }

    // 覆寫請求授權後執行的方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_READ_EXTERNAL_STORAGE_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                galleryIntent();
            } else {
                Toast.makeText(this, R.string.read_external_storage_denied,
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        File file = configFileName("P", ".jpg");
        //picFile = file;

        if (file.exists()) {
            // 顯示照片元件
            //picture.setVisibility(View.VISIBLE);
            // 設定照片
            //FileUtil.fileToImageView(file.getAbsolutePath(), picture);
            //System.out.println("//////test/////");
        }
        //System.out.println(file.getName());
    }

    public void onSubmit(View view) {
        if (view.getId() == R.id.search_item) {
            // Use Async Task to open httpUrlConnection for upload picture
            String responseString = new String();

            // Use Async Task
            try{
                AsyncTaskConnect asyncTaskConnect = new AsyncTaskConnect(picFile, getImagePath(picUri));
                responseString =  asyncTaskConnect.execute().get();
            } catch (InterruptedException e) {
                System.out.println("Interrupted exception");
            } catch (ExecutionException e) {
                System.out.println("Execution exception");
            }

            // Parse response string
            imageUrl = getParseString(responseString, "data", "img_url");

            // output test
            System.out.println(imageUrl);
        }
        finish();
    }

    private void requestStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int hasPermission = checkSelfPermission(
                    Manifest.permission.READ_EXTERNAL_STORAGE);

            if (hasPermission != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READ_EXTERNAL_STORAGE_PERMISSION);
                return;
            }
        }

        galleryIntent();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        File pictureFile = configFileName("P", ".jpg");
        Uri uri = Uri.fromFile(pictureFile);
        picUri = uri;

        System.out.println(uri.getPath());

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private File configFileName(String prefix, String extension) {
        if (fileName == null) {
            fileName = FileUtil.getUniqueFileName();
        }

        return new File(FileUtil.getExternalStorageDir(FileUtil.APP_DIR),
                prefix + fileName + extension);
    }

    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //create a file to write bitmap data
        /*picFile = new File(fileName);
        try {
            picFile.createNewFile();
        } catch (IOException e) {
            System.out.println("IO exception");
        }*/

        //Convert bitmap to byte array
        /*ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();*/

        //write the bytes in file
        /*FileOutputStream fos;
        try {
            fos = new FileOutputStream(picFile);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        picture.setImageBitmap(bitmap);
        picture.setVisibility(View.VISIBLE);
    }

    private String getImagePath(Uri paramUri) {
        return paramUri.getPath();
    }

    private String getParseString(String jsonStr, String target1, String target2) {
        String imageUrl = new String();
        try {
            // Get Json object twice with two target
            JSONObject jsonObject = new JSONObject(jsonStr);
            JSONObject data = jsonObject.getJSONObject(target1);

            // add /t/ to get tinny picture link
            String originUrl = data.getString(target2);
            int idx = 0;
            for (int  i = 0 ; i < originUrl.length()-1 ; i++) {
                if (originUrl.charAt(i) == '/' && originUrl.charAt(i+1) != '/')
                    idx = i;
            }
            imageUrl = originUrl.substring(0, idx) + "/t/" + originUrl.substring(idx+1, originUrl.length());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return imageUrl;
    }
}