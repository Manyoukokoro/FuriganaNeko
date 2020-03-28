package nekotori.furigananeko;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import nekotori.furigananeko.Furi.TextFuri;

public class MainActivity extends AppCompatActivity {
    private static final int READ_FILE_CODE = 42;

    private static final String TAG = "this";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*定义布局元素*/
        EditText inputText = findViewById(R.id.inputText);
        TextView outputText = findViewById(R.id.outputText);
        Button furiButton = findViewById(R.id.furi_Button);
        Button fileButton = findViewById(R.id.file_Button);
        /*直接转换*/
        furiButton.setOnClickListener((view) -> {
            TextFuri tf = new TextFuri(inputText.getText().toString());
            outputText.setText(tf.furiKanji());
        });
        /*文件转换*/
        fileButton.setOnClickListener((view) -> getFile());

       /* Uri uri = Uri.parse(getIntent().getStringExtra("outputUri"));
        String content = getIntent().getStringExtra("outputContent");
        try {
            ParcelFileDescriptor pfd = this.getContentResolver().
                    openFileDescriptor(uri, "w");
            assert pfd != null;
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            String processText;
            fileOutputStream.write((content +
                    "\n").getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();

        } catch (IOException e) {
            e.printStackTrace();

        }*/
    }

    /*通过SAF获取文件*/
    protected void getFile() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(intent, READ_FILE_CODE);
    }

    @Override
    /*处理获取文件结果*/
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (requestCode == READ_FILE_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                assert uri != null;
                Log.i(TAG, "Uri: " + uri.toString());

                Context context = getApplicationContext();
                CharSequence text = this.getString(R.string.file_process_get);
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                //跳转文件处理Activity
                Intent intent = new Intent(MainActivity.this, fileFuriActivity.class);
                intent.putExtra("fileProcessCall", uri.toString());
                startActivity(intent);

               /* try {
                    FuriFile(uri);
                }catch (IOException e){
                    Toast toast1 =Toast.makeText(context,"IO exception",duration);
                    toast1.show();
                    e.fillInStackTrace();
                }*/

            }
        }

    }
}