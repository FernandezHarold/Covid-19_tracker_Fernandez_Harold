package com.example.covid_19_tracker_fernandezharold;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.example.covid_19_tracker_fernandezharold.api.ApiUtilities;
import com.example.covid_19_tracker_fernandezharold.api.CountryData;

import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    TextView text_total_confirm,text_total_active,text_total_recovered,text_totaldeceased,text_total_test;
    TextView text_today_confirm,text_today_recovered,text_today_deceased;
    TextView text_date;
    PieChart pieChart;

    private List<CountryData> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = new ArrayList<>();

        text_total_confirm = findViewById(R.id.txtview_totalconfirmed);
        text_total_active = findViewById(R.id.txtview_activecases);
        text_total_recovered = findViewById(R.id.txtview_totalrecovered);
        text_totaldeceased = findViewById(R.id.txtview_totaldeceased);
        text_total_test = findViewById(R.id.txtview_totaltested);
        text_today_confirm = findViewById(R.id.txtview_todayconfirmed);
        text_today_recovered = findViewById(R.id.txtview_todayrecovered);
        text_today_deceased = findViewById(R.id.txtview_todaydeceased);
        text_date = findViewById(R.id.txtview_date);
        pieChart = findViewById(R.id.piechart);

        ApiUtilities.getApiInterface().getCountryData()
                .enqueue(new Callback<List<CountryData>>() {
                    @Override
                    public void onResponse(Call<List<CountryData>> call, Response<List<CountryData>> response) {
                        list.addAll(response.body());

                        for (int i = 0; i<list.size(); i++){
                            if(list.get(i).getCountry().equals("Philippines")){
                                int total_confirm = Integer.parseInt(list.get(i).getCases());
                                int total_death = Integer.parseInt(list.get(i).getDeaths());
                                int total_recovered = Integer.parseInt(list.get(i).getRecovered());
                                int total_active = Integer.parseInt(list.get(i).getActive());
                                int total_tests = Integer.parseInt(list.get(i).getTests());

                                int today_confirm = Integer.parseInt(list.get(i).getTodayCases());
                                int today_deaths = Integer.parseInt(list.get(i).getTodayDeaths());
                                int today_recovered = Integer.parseInt(list.get(i).getTodayRecovered());

                                text_total_confirm.setText(NumberFormat.getInstance().format(total_confirm));
                                text_total_active.setText(NumberFormat.getInstance().format(total_active));
                                text_total_recovered.setText(NumberFormat.getInstance().format(total_recovered));
                                text_totaldeceased.setText(NumberFormat.getInstance().format(total_death));
                                text_total_test.setText(NumberFormat.getInstance().format(total_tests));

                                text_today_confirm.setText(NumberFormat.getInstance().format(today_confirm));
                                text_today_deceased.setText(NumberFormat.getInstance().format(today_deaths));
                                text_today_recovered.setText(NumberFormat.getInstance().format(today_recovered));

                                setText(list.get(i).getUpdated());

                                pieChart.addPieSlice(new PieModel("Confirmed", total_confirm, Color.parseColor("#E91E63")));
                                pieChart.addPieSlice(new PieModel("Active", total_active, Color.parseColor("##3F51B5")));
                                pieChart.addPieSlice(new PieModel("Recovered", total_recovered, Color.parseColor("#FFEB3B")));
                                pieChart.addPieSlice(new PieModel("Deceased", total_death, Color.parseColor("##009688")));
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<List<CountryData>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "ERROR ACQUIRING DATA", Toast.LENGTH_SHORT).show();

                    }
                });

    }

    private void setText(String updated){
        DateFormat format = new SimpleDateFormat("MMM dd, yyyy");

        long milliseconds = Long.parseLong(updated);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliseconds);
        text_date.setText("Updated at "+format.format(calendar.getTime()));
    }
}