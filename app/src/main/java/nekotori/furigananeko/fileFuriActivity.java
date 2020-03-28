package nekotori.furigananeko;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;

import nekotori.furigananeko.Furi.TextFuri;

public class fileFuriActivity extends AppCompatActivity {
    private static final int WRITE_FILE_CODE = 43;
    private static final String TAG ="this" ;
    //  private static final int EDIT_REQUEST_CODE = 44;
    private String processText ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_furi);
        Uri uri = Uri.parse(getIntent().getStringExtra("fileProcessCall"));
        try {
            FuriFile(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /*文件处理*/
    protected void FuriFile(Uri uri) throws IOException {
        try (InputStream inputStream =
                     getContentResolver().openInputStream(uri);
             BufferedReader reader = new BufferedReader(
                     new InputStreamReader(Objects.requireNonNull(inputStream)))) {
            String line;

            while ((line = reader.readLine()) != null) {
                TextFuri tf=new TextFuri(line);
               this.processText = this.processText.concat(tf.furiKanji()+"\n");
            }
            /*创建文件*/
            createFile("text/plain",uri.toString().
                    substring(uri.toString().
                            lastIndexOf("%") + 1,uri.toString().length()-4)+
                    "furi.txt");
        }
    }
    /*创建文件，方便写入*/
    private void createFile(String mimeType, String fileName) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fileName);
        startActivityForResult(intent, WRITE_FILE_CODE);
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == WRITE_FILE_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                assert uri != null;
                Log.i(TAG, "Uri: " + uri.toString());
                Context context = getApplicationContext();
                CharSequence text = this.getString(R.string.file_process_put);
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                /*Intent intent = new Intent(fileFuriActivity.this,MainActivity.class);
                intent.putExtra("outputUri",uri);
                intent.putExtra("outputContent",this.processText);
                startActivity(intent);*/

                try {
                    ParcelFileDescriptor pfd = this.getContentResolver().
                            openFileDescriptor(uri, "w");
                    assert pfd != null;
                    FileOutputStream fileOutputStream =
                            new FileOutputStream(pfd.getFileDescriptor());
                    fileOutputStream.write((processText +
                           "\n").getBytes());
                    // Let the document provider know you're done by closing the stream.
                    fileOutputStream.close();
                    pfd.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }

               Intent intent = new Intent(fileFuriActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }

    /*编辑文件内容
    private void editDocument() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, EDIT_REQUEST_CODE);
    }*/
}