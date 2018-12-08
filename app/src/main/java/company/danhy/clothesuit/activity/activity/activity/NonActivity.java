package company.danhy.clothesuit.activity.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import company.danhy.clothesuit.R;
import company.danhy.clothesuit.activity.activity.adapter.NonAdapter;
import company.danhy.clothesuit.activity.activity.model.Sanpham;
import company.danhy.clothesuit.activity.activity.ultil.Server;
import company.danhy.clothesuit.activity.activity.ultil.checkconnect;

public class NonActivity extends AppCompatActivity {
    Toolbar tbnon;
    ListView lvnon;
    NonAdapter nonAdapter;
    ArrayList<Sanpham> mangnon;
    int idnon=0;
    int page=1;
    View footerview;
    boolean limitdata=false;
    boolean Loading=false;
    NonActivity.mHandler mHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non);
        if(checkconnect.isNetworkAvailable(getApplicationContext())){
            anhxa();
            GetIdloaisp();
            ActionToolbar();
            getData(page);
            LoadMoreData();
        }
        else{
            checkconnect.ShowToast_Short(getApplicationContext(),"Bạn vui lòng kiểm tra lại Internet");
            finish();
        }


    }
    private void LoadMoreData() {
        lvnon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent=new Intent(getApplicationContext(),ChiTietSanPham.class);
                intent.putExtra("thongtinsanpham",mangnon.get(position));
                startActivity(intent);
            }
        });
        lvnon.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if(firstVisibleItem+visibleItemCount==totalItemCount &&totalItemCount!=0 && Loading ==false && limitdata ==false){
                    Loading=true;
                    NonActivity.ThreadData threadData=new NonActivity.ThreadData();
                    threadData.start();
                }
            }
        });
    }

    private void anhxa() {
        tbnon =findViewById(R.id.toolbarnon);
        lvnon=findViewById(R.id.listviewnon);
        mangnon=new ArrayList<>();
        nonAdapter=new NonAdapter(getApplicationContext(),mangnon);
        lvnon.setAdapter(nonAdapter);
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        footerview =inflater.inflate(R.layout.projectbar,null);
        mHandler=new NonActivity.mHandler();
    }
    private void GetIdloaisp() {
        idnon=getIntent().getIntExtra("idLoaiSanPham",-1);

    }

    private void ActionToolbar() {

        setSupportActionBar(tbnon);
        ActionBar actionBar=getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        tbnon.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    private void getData(int Page) {
        RequestQueue requestQueue=Volley.newRequestQueue(getApplicationContext());
        String duongdan=Server.duongDanGiay + String.valueOf(Page);
        StringRequest stringRequest=new StringRequest(Request.Method.POST, duongdan, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                int id=0;
                String tennon="";
                int gianon=0;
                String hinhanhnon="";
                String motanon="";
                int idspnon=0;
                if(response!=null  && response.length()!=2){
                    lvnon.removeFooterView(footerview);
                    try {
                        JSONArray jsonArray =new JSONArray(response);
                        for(int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            id=jsonObject.getInt("id");
                            tennon=jsonObject.getString("tensp");
                            gianon=jsonObject.getInt("giasp");
                            hinhanhnon=jsonObject.getString("hinhanhsp");
                            motanon=jsonObject.getString("motasp");
                            idspnon=jsonObject.getInt("idsanpham");
                            mangnon.add(new Sanpham(id,tennon,gianon,hinhanhnon,motanon,idspnon));
                            nonAdapter.notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else{
                    limitdata=true;
                    lvnon.removeFooterView(footerview);
                    checkconnect.ShowToast_Short(getApplicationContext(),"Đã hết dữ liệu");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String,String> param=new HashMap<String,String>();
                param.put("idLoaiSanPham",String.valueOf(idnon));
                return param;
            }
        };
        requestQueue.add(stringRequest);
    }
    public class mHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    lvnon.addFooterView(footerview);
                    break;
                case 1:
                    getData(++page);
                    Loading=false;
                    break;
            }
            super.handleMessage(msg);
        }
    }
    public class ThreadData extends Thread{
        @Override
        public void run() {
            mHandler.sendEmptyMessage(0);
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message message=mHandler.obtainMessage(1);
            mHandler.sendMessage(message);
            super.run();
        }
    }
}
