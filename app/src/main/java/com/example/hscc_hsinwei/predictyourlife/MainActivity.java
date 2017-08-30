package com.example.hscc_hsinwei.predictyourlife;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.AlgoPrefixSpan;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.SequentialPattern;
import ca.pfv.spmf.algorithms.sequentialpatterns.prefixspan.SequentialPatterns;

public class MainActivity extends AppCompatActivity {

    private Button selectFile;
    private TextView filePath;
    private TextView fileContent;
    private Button prefixSpanB;
    private EditText minSup;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        selectFile = (Button)findViewById(R.id.selectFile);
        filePath = (TextView)findViewById(R.id.path);
        fileContent = (TextView)findViewById(R.id.fileContent);
        prefixSpanB = (Button)findViewById(R.id.prefixSpanButton);
        minSup = (EditText)findViewById(R.id.minSupEdit);

        selectFile.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                // 建立 "選擇檔案 Action" 的 Intent
                Intent intent = new Intent( Intent.ACTION_GET_CONTENT );
                // 過濾檔案格式
                //intent.setType( "image/*" );

                // 建立 "檔案選擇器" 的 Intent  (第二個參數: 選擇器的標題)
                Intent destIntent = Intent.createChooser( intent, "選擇檔案" );
                // 切換到檔案選擇器 (它的處理結果, 會觸發 onActivityResult 事件)
                startActivityForResult( destIntent, 0 );
            }
        });

        prefixSpanB.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view) {
                //new一個intent物件，並指定Activity切換的class
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ResultActivity.class);

                //new一個Bundle物件，並將要傳遞的資料傳入
                Bundle bundle = new Bundle();
                bundle.putString("result", PrefixSpan(filePath.getText().toString(),Double.parseDouble(minSup.getText().toString())));

                //將Bundle物件assign給intent
                intent.putExtras(bundle);

                //切換Activity
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        // 有選擇檔案
        if ( resultCode == RESULT_OK )
        {
            // 取得檔案的 Uri
            Uri uri = data.getData();
            if( uri != null )
            {
                filePath.setText(uri.getPath());
                fileContent.setText(readFile(uri));
            }
            else
            {
                filePath.setText("無效的檔案路徑 !!");
            }
        }
        else
        {
            filePath.setText("取消選擇檔案 !!");
        }
    }

    public String readFile(Uri uri){
        File file = new File(uri.getPath());
        //Read text from file

        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here

            Log.d("MyApp",e.toString());
        }
        return text.toString();
    }

    public String PrefixSpan(String uri,double minSup){
        // input file
        String inputFile = uri;
        // Create an instance of the algorithm
        AlgoPrefixSpan algo = new AlgoPrefixSpan();
        algo.setMaximumPatternLength(400);
        // if you set the following parameter to true, the sequence ids of the sequences where
        // each pattern appears will be shown in the result
        algo.setShowSequenceIdentifiers(true);
        // execute the algorithm with minsup = 50 %
        String result="";
        try {
            SequentialPatterns patterns = algo.runAlgorithm(inputFile, minSup, null);
            result += " == PATTERNS FOUND ==\n";
            for(List<SequentialPattern> level : patterns.levels) {
                for(SequentialPattern pattern : level){
                    result += (pattern + " support : " + pattern.getAbsoluteSupport()+"\n");
                    //System.out.println(pattern + " support : " + pattern.getAbsoluteSupport());
                }
            }
            // print statistics
            result+=algo.printStatistics();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
