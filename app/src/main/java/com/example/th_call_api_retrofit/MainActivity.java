package com.example.th_call_api_retrofit;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.th_call_api_retrofit.data.ApiInterface;
import com.example.th_call_api_retrofit.model.Moto;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private static final String BASE_URL = "https://60af714e5b8c300017decbb5.mockapi.io/";
    private List<Moto> list = new ArrayList<>();
    ImageView imgAdd;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imgAdd = findViewById(R.id.imgAdd);
        lv = findViewById(R.id.lv);
        RetrofitGETALL();
        imgAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,PostMotoActivity.class);
                startActivity(i);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(MainActivity.this, UpdateMotoActivity.class)
                        .putExtra("data", list.get(position)));
                return true;
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        RetrofitGETALL();
    }

    public class MotoAdapter extends BaseAdapter {
        private List<Moto> motoList;
        private Context context;
        private LayoutInflater inflater;

        public MotoAdapter(List<Moto> motoList, Context context) {
            this.motoList = motoList;
            this.context = context;
            this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return motoList.size();
        }

        @Override
        public Object getItem(int position) {
            return motoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.item_moto, parent, false);
            }
            ImageView img = convertView.findViewById(R.id.img_moto);
            TextView txt_name_manga = convertView.findViewById(R.id.txt_name);

            txt_name_manga.setText(motoList.get(position).getName());
            Glide.with(context)
                    .load(motoList.get(position).getImage())
                    .into(img);
            ImageView imgDelete = convertView.findViewById(R.id.img_delete);
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Delete item");
                    builder.setMessage("do you want item this delete?");
                    builder.setPositiveButton("yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            delete(list.get(position).getId());
                            Toast.makeText(getApplicationContext(), "Delete Success", Toast.LENGTH_SHORT).show();
                            RetrofitGETALL();
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.show();
                }
            });
            return convertView;
        }
    }

    void RetrofitGETALL() {
        // tạo đối tượng Gson để hỗ trợ chuyển đổi dữ liệu từ DTO qua lại với JSON
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);

        Call<List<Moto>> objGetMOto = apiInterface.getAllMoto();
        // thực hiện gọi
        objGetMOto.enqueue(new Callback<List<Moto>>() {
            @Override
            public void onResponse(Call<List<Moto>> call, Response<List<Moto>> response) {

                // viết code xử lý kết quả trả về ở đây
                if (response.isSuccessful()) {
                    List<Moto> ds_moto = response.body();
                    for (int i = 0; i < ds_moto.size(); i++) {

                        Moto objMoto = ds_moto.get(i);

                        list = response.body();
                        MotoAdapter motoAdapter = new MotoAdapter(list, MainActivity.this);
                        lv.setAdapter(motoAdapter);

                        Log.d("zzzzz", "onResponse: phần tử thứ " + i + " = " + objMoto.getName());
                    }

                } else {
                    // lỗi
                    Log.e("zzzzzz", "onResponse: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<List<Moto>> call, Throwable t) {
                // lỗi kết nối internet hoặc lỗi chết server thì viết ở đây….
            }
        });
    }

    public void delete(int id) {
// tạo đối tượng Gson để hỗ trợ chuyển đổi dữ liệu từ DTO qua lại với JSON
        Gson gson = new GsonBuilder().setLenient().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        ApiInterface apiInterface = retrofit.create(ApiInterface.class);
        Call<Moto> objDelete = apiInterface.deleteMoto(id);

        objDelete.enqueue(new Callback<Moto>() {
            @Override
            public void onResponse(Call<Moto> call, Response<Moto> response) {
                if (!response.isSuccessful()) {
                    return;
                }
                Toast.makeText(getApplicationContext(), "Delete succcessfully" + response.code(), Toast.LENGTH_SHORT).show();
                onResume();
            }

            @Override
            public void onFailure(Call<Moto> call, Throwable t) {

            }
        });
    }


}