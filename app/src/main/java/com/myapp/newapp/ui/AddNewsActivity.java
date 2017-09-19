package com.myapp.newapp.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.myapp.newapp.R;
import com.myapp.newapp.adapter.CategorySpinnerAdapter;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.api.model.Publisher;
import com.myapp.newapp.custom.SelectCategoryDialog;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.PrefUtils;
import com.myapp.newapp.helper.ProgressBarHelper;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;

public class AddNewsActivity extends AppCompatActivity {

    private TextView txtTitle;
    private Toolbar toolbar;
    private EditText edtTitle;
    private EditText edtDesc;
    private EditText edtUrl;
    private EditText edtMetaDesc;
    private TextView txtSelectCategory;
    private TextView txtSelectImage;
    private ImageView imgNews;
    private Context context;
    private SelectCategoryDialog selectCategoryDialog;
    private String selectedCategory = "";
    private List<Publisher> publisherList;
    private AppCompatSpinner spinner;
    private CategorySpinnerAdapter spinnerAdapter;
    private ProgressBarHelper progressBar;
    private Button btnSubmit;
    private SimpleDateFormat sdf;
    private String imagePath;
    private Switch pushNoti;
    private TextView txtCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        context = this;
        init();
    }

    private void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();
        pushNoti = (Switch) findViewById(R.id.pushNoti);
        progressBar = new ProgressBarHelper(context, false);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        imgNews = (ImageView) findViewById(R.id.imgNews);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtSelectImage = (TextView) findViewById(R.id.txtSelectImage);
        txtSelectCategory = (TextView) findViewById(R.id.txtSelectCategory);
        edtMetaDesc = (EditText) findViewById(R.id.edtMetaDesc);
        edtUrl = (EditText) findViewById(R.id.edtUrl);
        edtDesc = (EditText) findViewById(R.id.edtDesc);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        this.spinner = (AppCompatSpinner) findViewById(R.id.spinner);

        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        selectCategoryDialog = new SelectCategoryDialog(context, new SelectCategoryDialog.OnOkClick() {
            @Override
            public void onOkClick(String output, String output2) {
                selectedCategory = output;
                txtCategory.setText(output2);
                txtCategory.setVisibility(View.VISIBLE);

            }
        });
        publisherList = new ArrayList<>();
        Publisher publisher = new Publisher();
        publisher.setName("Select Publisher");
        publisher.setId("0");
        publisherList.add(publisher);
        publisherList.addAll(PrefUtils.getPublisher(context));
        spinnerAdapter = new CategorySpinnerAdapter(context, R.layout.spinner_item, publisherList);
        spinner.setAdapter(spinnerAdapter);
        txtSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        txtSelectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectCategoryDialog.show();
            }
        });


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!Functions.isConnected(context)) {
                    Functions.showToast(context, "Please check your internet connection");
                    return;
                }
                if (edtTitle.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter title");
                    return;
                }
                if (edtDesc.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter title");
                    return;
                }
                if (edtUrl.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter Url");
                    return;
                }

                if (edtMetaDesc.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter Meta description");
                    return;
                }
                if (spinner.getSelectedItemPosition() == 0) {
                    Functions.showToast(context, "Please select publisher");
                    return;
                }
                if (selectedCategory == null || selectedCategory.trim().length() == 0) {
                    Functions.showToast(context, "Please select category");
                    return;
                }
                if (imagePath == null || imagePath.trim().length() == 0) {
                    Functions.showToast(context, "Please select image");
                    return;
                }

                News news = new News();
                news.setTitle(edtTitle.getText().toString().trim());
                news.setImage("https://crpost.in/mobileapi/upload/"+new File(imagePath).getName());
                news.setDescription(edtDesc.getText().toString().trim());
                news.setLink(edtTitle.getText().toString().trim().replace(" ", "-").replace(",", "-").replace("'", "-"));
                news.setUrl(edtUrl.getText().toString().trim());
                news.setTitleTag(edtTitle.getText().toString().trim());
                news.setMetaDes(edtMetaDesc.getText().toString().trim());
                news.setAuthorId(PrefUtils.getUserFullProfileDetails(context).getId() + "");
                news.setType("contributor");
                news.setPublisherId(publisherList.get(spinner.getSelectedItemPosition()).getId());
                news.setShareTitle(edtTitle.getText().toString().trim().replace(" ", "-").replace(",", "-").replace("'", "-"));
                news.setCategory(selectedCategory);
                news.setCreatedAt(sdf.format(new Date()));
                news.setUpdatedAt(sdf.format(new Date()));
                news.setIsPushNotification(pushNoti.isChecked() ? 1 : 0);

                Log.e("news", MyApplication.getGson().toJson(news));
            }
        });
    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void selectImage() {
        ImagePicker.with((Activity) context)        //  Initialize ImagePicker with activity or fragment context
                .setToolbarColor("#212121")         //  Toolbar color
                .setStatusBarColor("#000000")       //  StatusBar color (works with SDK >= 21  )
                .setToolbarTextColor("#FFFFFF")     //  Toolbar text color (Title and Done button)
                .setToolbarIconColor("#FFFFFF")     //  Toolbar icon color (Back and Camera button)
                .setProgressBarColor("#4CAF50")     //  ProgressBar color
                .setBackgroundColor("#212121")      //  Background color
                .setCameraOnly(false)               //  Camera mode
                .setMultipleMode(false)              //  Select multiple images or single image
                .setFolderMode(true)                //  Folder mode
                .setShowCamera(true)                //  Show camera button
                .setFolderTitle("Albums")           //  Folder title (works with FolderMode = true)
                .setImageTitle("Galleries")         //  Image title (works with FolderMode = false)
                .setDoneTitle("Done")               //  Done button title
                .setMaxSize(1)                      //  Max images can be selected
                .setSavePath("ImagePicker")         //  Image capture folder name
                .start();
    }

    public void setImage(List<Image> list) {
//        this.list = list;
        imagePath = list.get(0).getPath();
        Log.e("image",imagePath);
        imgNews.setImageURI(Uri.fromFile(new File(list.get(0).getPath())));
        imgNews.setVisibility(View.VISIBLE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Config.RC_PICK_IMAGES && resultCode == RESULT_OK && data != null) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Config.EXTRA_IMAGES);
            setImage(images);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void uploadFile(File fileName) {


        FTPClient client = new FTPClient();

        try {
            Log.e("host name", InetAddress.getByName("").getHostName());
            client.connect(InetAddress.getByName("").getHostName(), 21);
            client.login("", "");
            client.setType(FTPClient.TYPE_BINARY);
            client.changeDirectory("");

            client.upload(fileName, new MyTransferListener());

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("error 1", e.toString());
            try {
                client.disconnect(true);
            } catch (Exception e2) {
                e2.printStackTrace();
                Log.e("error 2", e2.toString());
            }
        }

    }

    public class MyTransferListener implements FTPDataTransferListener {

        public void started() {

            progressBar.showProgressDialog();
            // Transfer started
            //System.out.println(" Upload Started ...");
        }

        public void transferred(int length) {
            Log.e("transfered", length + "");
            // Yet other length bytes has been transferred since the last time this
            // method was called
            //System.out.println(" transferred ..." + length);
        }

        public void completed() {
            progressBar.hideProgressDialog();
            // Transfer completed

            //System.out.println(" completed ..." );
        }

        public void aborted() {

            progressBar.hideProgressDialog();
            // Transfer aborted
            //System.out.println(" aborted ..." );
        }

        public void failed() {

            progressBar.hideProgressDialog();
            // Transfer failed
        }

    }
}