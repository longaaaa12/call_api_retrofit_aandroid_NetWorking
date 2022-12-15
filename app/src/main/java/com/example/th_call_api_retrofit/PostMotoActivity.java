package com.example.th_call_api_retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.th_call_api_retrofit.data.ApiInterface;
import com.example.th_call_api_retrofit.model.Moto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PostMotoActivity extends AppCompatActivity {
    EditText edtname, edtPrice, edtcolor, edtImage;
    ProgressBar progressBar;
    Button btnSave;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_moto);
        initView();

        Button button = findViewById(R.id.btn_back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                         btn_back();
            }
        });
    }

    private void initView() {
        edtname = findViewById(R.id.edit_text_name);
        edtcolor = findViewById(R.id.edit_text_color);
        edtPrice = findViewById(R.id.edit_text_price);
        edtImage = findViewById(R.id.edit_text_image);
        btnSave = findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                POST_Retrofit();
            }
        });
    }

    //thêm oto
    void POST_Retrofit() {
        // tạo đối tượng chuyển đổi
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://60af714e5b8c300017decbb5.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        // tạo đối tượng DTO để gửi lên server
        Moto objMoto = new Moto();
        objMoto.setName(edtname.getText().toString());
        objMoto.setPrice(edtPrice.getText().toString());
        objMoto.setColor(edtcolor.getText().toString());
        objMoto.setImage(edtImage.getText().toString());

        Call<Moto> objCall = apiInterface.PostMoto(objMoto);

        objCall.enqueue(new Callback<Moto>() {
            @Override
            public void onResponse(Call<Moto> call, Response<Moto> response) {

                if (response.isSuccessful()) {
                    Moto moto = response.body();
                    Toast.makeText(PostMotoActivity.this, "them thanh cong", Toast.LENGTH_SHORT).show();
                } else {
                }
            }

            @Override
            public void onFailure(Call<Moto> call, Throwable t) {

            }
        });

    }
    public void btn_back(){
        Intent intent = new Intent(PostMotoActivity.this,MainActivity.class);
        startActivity(intent);

    }
}