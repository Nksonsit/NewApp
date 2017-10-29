package com.myapp.newapp.ui;

import android.Manifest;
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

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.myapp.newapp.R;
import com.myapp.newapp.adapter.CategorySpinnerAdapter;
import com.myapp.newapp.api.call.AddNews;
import com.myapp.newapp.api.model.News;
import com.myapp.newapp.api.model.Publisher;
import com.myapp.newapp.custom.SelectCategoryDialog;
import com.myapp.newapp.custom.TfButton;
import com.myapp.newapp.custom.TfEditText;
import com.myapp.newapp.custom.TfEditTextOld;
import com.myapp.newapp.custom.TfTextView;
import com.myapp.newapp.helper.Functions;
import com.myapp.newapp.helper.MyApplication;
import com.myapp.newapp.helper.PrefUtils;
import com.myapp.newapp.helper.ProgressBarHelper;
import com.nguyenhoanglam.imagepicker.model.Config;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.nguyenhoanglam.imagepicker.ui.imagepicker.ImagePicker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import it.sauronsoftware.ftp4j.FTPAbortedException;
import it.sauronsoftware.ftp4j.FTPClient;
import it.sauronsoftware.ftp4j.FTPDataTransferException;
import it.sauronsoftware.ftp4j.FTPDataTransferListener;
import it.sauronsoftware.ftp4j.FTPException;
import it.sauronsoftware.ftp4j.FTPIllegalReplyException;

public class AddNewsActivity extends AppCompatActivity {

    private TfTextView txtTitle;
    private Toolbar toolbar;
    private TfEditTextOld edtTitle;
    private TfEditTextOld edtDesc;
    private TfEditTextOld edtUrl;
    private TfTextView txtSelectCategory;
    private TfTextView txtSelectImage;
    private ImageView imgNews;
    private Context context;
    private SelectCategoryDialog selectCategoryDialog;
    private String selectedCategory = "";
    private List<Publisher> publisherList;
    private AppCompatSpinner spinner;
    private CategorySpinnerAdapter spinnerAdapter;
    private ProgressBarHelper progressBar;
    private TfButton btnSubmit;
    private SimpleDateFormat sdf;
    private String imagePath;
    private TfTextView txtCategory;
    private FTPClient ftpClient;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_news);
        context = this;
        init();

        TedPermission.with(context).setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .setPermissionListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {

                    }
                }).check();
    }

    private void init() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        initToolbar();
        progressBar = new ProgressBarHelper(context, false);
        btnSubmit = (TfButton) findViewById(R.id.btnSubmit);
        imgNews = (ImageView) findViewById(R.id.imgNews);
        txtCategory = (TfTextView) findViewById(R.id.txtCategory);
        txtSelectImage = (TfTextView) findViewById(R.id.txtSelectImage);
        txtSelectCategory = (TfTextView) findViewById(R.id.txtSelectCategory);
        edtUrl = (TfEditTextOld) findViewById(R.id.edtUrl);
        edtDesc = (TfEditTextOld) findViewById(R.id.edtDesc);
        edtTitle = (TfEditTextOld) findViewById(R.id.edtTitle);
        txtTitle = (TfTextView) findViewById(R.id.txtTitle);
        this.spinner = (AppCompatSpinner) findViewById(R.id.spinner);

        ftpClient = new FTPClient();

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
                    Functions.showToast(context, "Please enter description");
                    return;
                }
                if (edtUrl.getText().toString().trim().length() == 0) {
                    Functions.showToast(context, "Please enter Url");
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

                fileName = System.currentTimeMillis() + "" + imagePath.substring(imagePath.lastIndexOf("."));

                Log.e("f", fileName);

                News news = new News();
                news.setTitle(edtTitle.getText().toString().trim());
                news.setImage("https://crpost.in/mobileapi/upload/" + fileName);
                news.setDescription(edtDesc.getText().toString().trim());
                news.setLink(edtTitle.getText().toString().trim().replace(" ", "-").replace(",", "-").replace("'", "-"));
                news.setUrl(edtUrl.getText().toString().trim());
                news.setTitleTag(edtTitle.getText().toString().trim());
                news.setMetaDes(edtTitle.getText().toString().trim());
                news.setAuthorId(PrefUtils.getUserFullProfileDetails(context).getId() + "");
                news.setType("contributor");
                news.setPublisherId(publisherList.get(spinner.getSelectedItemPosition()).getId());
                news.setShareTitle(edtTitle.getText().toString().trim().replace(" ", "-").replace(",", "-").replace("'", "-"));
                news.setCategory(selectedCategory);
                news.setCreatedAt(sdf.format(new Date()));
                news.setUpdatedAt(sdf.format(new Date()));
                news.setIsPushNotification(0);

                Log.e("news", MyApplication.getGson().toJson(news));

                new AddNews(context, news, new AddNews.OnSuccess() {
                    @Override
                    public void onSuccess(String data) {
                        Functions.showToast(context, data);
                        finish();
                    }

                    @Override
                    public void onFail(String s) {
                        Functions.showToast(context, s);
                    }
                });
                uploadFile(new File(imagePath));
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
        Log.e("image", imagePath);
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

    public void uploadFile(File file) {
        try {

            Log.e("fileName", fileName.toString());
            String output = copyFile(file.getParent(), file.getName(), fileName);

            if (!ftpClient.isConnected()) {
                ftpClient.connect("ftp.fiplindia.com", 21);
                ftpClient.login("mobileapi@crpost.in", "mobileapi!@android");
//            ftpClient.changeWorkingDirectory(serverRoad);
                ftpClient.setType(FTPClient.TYPE_BINARY);
                ftpClient.changeDirectory("/upload/");
            }


            try {
                ftpClient.upload(new File(output), new FTPDataTransferListener() {
                    @Override
                    public void started() {

                    }

                    @Override
                    public void transferred(int i) {

                        Log.e("transfer", "" + i);
                    }

                    @Override
                    public void completed() {

                        Log.e("complete", "complete");

                    }

                    @Override
                    public void aborted() {

                        Log.e("abort", "abort");
                    }

                    @Override
                    public void failed() {

                        Log.e("fail", "fail");
                    }
                });
            } catch (FTPDataTransferException e) {
                Log.e("error1", e.toString());
                e.printStackTrace();
            } catch (FTPAbortedException e) {
                Log.e("error2", e.toString());
                e.printStackTrace();
            }
        } catch (IOException e) {
            Log.e("error3", e.toString());
            e.printStackTrace();
        } catch (FTPIllegalReplyException e) {
            Log.e("error4", e.toString());
            e.printStackTrace();
        } catch (FTPException e) {
            Log.e("error5", e.toString());
            e.printStackTrace();
        }

    }

    private String copyFile(String inputPath, String inputFile, String outputFile) {
        Log.e(inputPath, inputFile);
        Log.e(inputPath, outputFile);
        InputStream in = null;
        OutputStream out = null;
        try {

            //create output directory if it doesn't exist
            File dir = new File(inputPath, outputFile);
            if (!dir.exists()) {
                dir.createNewFile();
            }

            in = new FileInputStream(inputPath + "/" + inputFile);
            out = new FileOutputStream(inputPath + "/" + outputFile);

            byte[] buffer = new byte[1024];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            in.close();
            in = null;

            // write the output file (You have now copied the file)
            out.flush();
            out.close();
            out = null;

        } catch (FileNotFoundException fnfe1) {
            Log.e("tag", fnfe1.getMessage());
        } catch (Exception e) {
            Log.e("tag", e.getMessage());
        }

        return inputPath + "/"  + outputFile;
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
/*    public class MyTransferListener implements FTPDataTransferListener {

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

    }*/

