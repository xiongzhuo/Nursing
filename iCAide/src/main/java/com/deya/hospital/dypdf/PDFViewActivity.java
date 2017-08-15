/**
 * Copyright 2014 Joan Zapata
 *
 * This file is part of Android-pdfview.
 *
 * Android-pdfview is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Android-pdfview is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Android-pdfview.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.deya.hospital.dypdf;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.deya.acaide.R;
import com.deya.hospital.base.BaseActivity;
import com.deya.hospital.bizimp.RequestInterface;
import com.deya.hospital.util.CommonTopView;
import com.im.sdk.dy.common.utils.ToastUtil;

import org.json.JSONObject;

import java.io.File;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;


public abstract class PDFViewActivity extends BaseActivity implements RequestInterface {


    public static final int CLICK_COLLECT_SUCESS = 0x6000437;
    PDFViewPager pdfView;
    CommonTopView topView;

    String pdfName = "";

    Integer pageNumber = 0;
    private PDFPagerAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_pdf_view);
        initView();
    }



    protected String getPdfPathOnSDCard() {
        File f = new File(getExternalFilesDir("pdf"), "adobe.pdf");
        return f.getAbsolutePath();
    }
    protected void initView() {


        initOwnViews();
        //layout.addView(mButtonsView);
        pdfView = (PDFViewPager) findViewById(R.id.pdfViewPager);

        topView=findView(R.id.topView);
        topView.init(this);
        getFile();
    }

    public abstract void initOwnViews();



    protected void display(File assetFileName, boolean jumpToFirstPage) {
        if (jumpToFirstPage) pageNumber = 1;
        setTitle(pdfName = assetFileName.toString());

//        pdfView.fromAsset(assetFileName)
//                .defaultPage(pageNumber)
//                .onPageChange(this)
//                .load();
//        pdfView.fromFile(assetFileName).defaultPage(pageNumber)
//                .onPageChange(this)
//                .pages(0, 2, 1, 3, 3, 3)
//                .defaultPage(0)
//                .enableSwipe(true)
//                .onPageChange(this)
//                .load();

    }



    @Override
    public void onBackPressed() {
            super.onBackPressed();
    }

    protected boolean displaying(String fileName) {
        return fileName.equals(pdfName);
    }

    public void getFile(){
        Intent intent = getIntent();
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            Uri uri = intent.getData();
            try {
                adapter = new PDFPagerAdapter(this, uri+"");
            }catch (Exception e){
                e.printStackTrace();
            }

            pdfView.setAdapter(adapter);

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

        adapter.close();
    }


    @Override
    public void onRequestSucesss(int code, JSONObject jsonObject) {

    }

    @Override
    public void onRequestErro(String message) {
        ToastUtil.showMessage(message);

    }

    @Override
    public void onRequestFail(int code) {

    }
}
