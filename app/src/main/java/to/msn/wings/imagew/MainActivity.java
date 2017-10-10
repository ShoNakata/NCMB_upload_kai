package to.msn.wings.imagew;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nifty.cloud.mb.core.DoneCallback;
import com.nifty.cloud.mb.core.FetchFileCallback;
import com.nifty.cloud.mb.core.NCMB;
import com.nifty.cloud.mb.core.NCMBAcl;
import com.nifty.cloud.mb.core.NCMBException;
import com.nifty.cloud.mb.core.NCMBFile;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStream;

//import static to.msn.wings.imagew.R.id.imageView;
import static android.R.attr.bitmap;
import static android.R.attr.contextClickable;
import static android.R.attr.data;
import static android.R.attr.path;
import static android.R.attr.tunerCount;
import static to.msn.wings.imagew.R.id.imageView;
import static to.msn.wings.imagew.R.id.up;

public class MainActivity extends Activity {

    private static final int READ_REQUEST_CODE = 1000000000;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        NCMB.initialize(getApplication(), "b18d561e7aa78c63abe4cd7a0bab2693b84cb975fe627e805281aaf9a2cfd82b", "80c9d743a613b53e9c09f104371d32cfdf7d79449ca1ed2b8b2e89dd31de5f72");

        Button btn1 = (Button)findViewById(R.id.button);
        imageView = (ImageView)findViewById(R.id.imageView);


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
//                intent.setType("image/*");
//
//                startActivityForResult(intent, READ_REQUEST_CODE);



                Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent1.setType("image/*");

                startActivityForResult(intent1, READ_REQUEST_CODE);

            }
        });

    }


    protected void onActivityResult(int requesutCode, int resultCode, Intent resultData) {

        if (requesutCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {


            //File filepath = new File(String.valueOf(path));



            Uri uri = null;


            if (resultData != null) {

                uri = resultData.getData();

                try {



                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 0, byteArrayOutputStream);
                    byte[] dataByte = byteArrayOutputStream.toByteArray();



                    //読み込み:可 , 書き込み:可
                    NCMBAcl acl = new NCMBAcl();
                    acl.setPublicReadAccess(true);
                    acl.setPublicWriteAccess(true);


                    String name = new File(uri.getPath()).getName();

                    //通信処理デース
                    final NCMBFile file = new NCMBFile( name + ".png" , dataByte, acl);
                    file.saveInBackground(new DoneCallback() {
                        @Override
                        public void done(NCMBException e) {

                            //String result;

                            if (e != null) {

                            } else {
                                imageView.setImageBitmap(bitmap);
                            }

                        }
                    });



                } catch (IOException e) {

                    e.printStackTrace();

                }
            }
        }

    }


//    @Override
//    public void onActivityResult(int requesutCode, int resultCode, Intent resultData) {
//
//        if (requesutCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//
//            Uri uri = null;
//
//            if (resultData != null) {
//
//                uri = resultData.getData();
//
//                try {
//
//                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
//                    imageView.setImageBitmap(bitmap);
//
//                } catch (IOException e) {
//
//                    e.printStackTrace();
//
//                }
//            }
//        }
//
//    }


}