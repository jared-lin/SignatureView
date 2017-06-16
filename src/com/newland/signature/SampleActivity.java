//package com.newland.signature;
//
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//
//
///**
// * Created by jklh on 2016/3/24.
// */
//public class SampleActivity extends Activity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_signature_demo);
//        initView();
//    }
//
//    private void initView(){
//
//        final ImageView imageView = (ImageView)findViewById(R.id.display_iv);
//        final SignatureView signatureView = (SignatureView)findViewById(R.id.signature_view);
//        Button buttonDisplay = (Button)findViewById(R.id.btn_display);
//
//        buttonDisplay.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                imageView.setImageBitmap(signatureView.getOriginalBitmap());
//            }
//        });
//
//        Button buttonClear = (Button)findViewById(R.id.btn_clear);
//
//        buttonClear.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                signatureView.clear();
//                imageView.setImageBitmap(signatureView.getOriginalBitmap());
//            }
//        });
//
//    }
//
//}
