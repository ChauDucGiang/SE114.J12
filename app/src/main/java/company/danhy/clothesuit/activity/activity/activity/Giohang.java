package company.danhy.clothesuit.activity.activity.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import java.text.DecimalFormat;

import company.danhy.clothesuit.R;
import company.danhy.clothesuit.activity.activity.adapter.GiohangAdapter;

public class Giohang extends AppCompatActivity {
    ListView listViewgiohang;
    TextView txtthongbao;
    TextView txttongtien;
    Button btthanhtoan,bttieptucmuahang;
    Toolbar toolbargiohang;
    GiohangAdapter giohangAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_giohang);
        anhxa();
        actionToolbar();
        checkData();
        evenUltil();
    }
    private void evenUltil() {
        long tongtien=0;
        for(int i=0;i<MainActivity.manggiohang.size();i++){
            tongtien+=MainActivity.manggiohang.get(i).getGiasp();
        }
        DecimalFormat decimalFormat =new DecimalFormat("###,###,###");
        txttongtien.setText(decimalFormat.format(tongtien)+ "Đ");
    }

    private void checkData() {
        if(MainActivity.manggiohang.size()<=0){
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.VISIBLE);
            listViewgiohang.setVisibility(View.INVISIBLE);
        }else{
            giohangAdapter.notifyDataSetChanged();
            txtthongbao.setVisibility(View.INVISIBLE);
            listViewgiohang.setVisibility(View.VISIBLE);
        }
    }

    private void actionToolbar() {
        setSupportActionBar(toolbargiohang);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbargiohang.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void anhxa() {
        listViewgiohang=findViewById(R.id.listviewgiohang);
        txtthongbao=findViewById(R.id.textviewthongbaogiohangtrong);
        txttongtien=findViewById(R.id.textviewtongtien);
        btthanhtoan=findViewById(R.id.buttonthanhtoan);
        bttieptucmuahang=findViewById(R.id.buttontieptucmuahang);
        toolbargiohang=findViewById(R.id.toolbargiohang);
        giohangAdapter=new GiohangAdapter(Giohang.this,MainActivity.manggiohang);
        listViewgiohang.setAdapter(giohangAdapter);
    }
}
