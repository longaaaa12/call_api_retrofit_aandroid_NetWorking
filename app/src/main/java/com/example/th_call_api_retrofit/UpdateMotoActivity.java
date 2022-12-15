package com.example.th_call_api_retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.th_call_api_retrofit.data.ApiInterface;
import com.example.th_call_api_retrofit.model.Moto;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateMotoActivity extends AppCompatActivity {
    TextView tvName, tvPrice, tvTime, tvcolor;
    ImageView imageView;
    Moto moto;
    Button update, delete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_moto);

        moto = (Moto) getIntent().getSerializableExtra("data");
        


        tvName=findViewById(R.id.edit_text_name);
        tvName.setText(moto.getName());
        tvPrice=findViewById(R.id.edit_text_price);
        tvPrice.setText(moto.getPrice());

        tvcolor=findViewById(R.id.edit_text_color);
        tvcolor.setText(moto.getColor());

        imageView=findViewById(R.id.image_view);
        Glide.with(UpdateMotoActivity.this).load(moto.getImage()).placeholder(R.drawable.ic_launcher_background).into(imageView);
        update=findViewById(R.id.btnUdate);
        delete=findViewById(R.id.btndelete);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://60af714e5b8c300017decbb5.mockapi.io/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Call<Moto> objCall = apiInterface.UpdateMoto(moto.getId()+"",tvName.getText().toString(), Integer.parseInt(tvPrice.getText().toString()),tvcolor.getText().toString());

                objCall.enqueue(new Callback<Moto>() {
                    @Override
                    public void onResponse(Call<Moto> call, Response<Moto> response) {

                        if(response.isSuccessful()){
                            Moto moto = response.body();
                            Toast.makeText(UpdateMotoActivity.this,"update thanh cong",Toast.LENGTH_SHORT).show();
                        }else{
                        }
                    }
                    @Override
                    public void onFailure(Call<Moto> call, Throwable t) {

                    }
                });
            }
        });

    }




}