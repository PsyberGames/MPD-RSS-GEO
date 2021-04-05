/**
 *  Matric Number - s1920031
 *  
 */
package com.example.ocallaghan_james_s1920031;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity  extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {
    //set the context and a getter to allow background processing.
    public static Context s_mContext;
    //returns this context
    public static Context getContext() {
        return s_mContext;
    }
    //current button selected
    Button currButton;
    //string to set text on the date button from the information from the datepicker dialog.
    String dateFrom;
    String dateTo;
    //menu selection items
    MenuItem m_item_lv;
    MenuItem m_item_nesw;
    MenuItem m_item_dns;
    MenuItem m_item_mag;

    //onCreate runs at the begining of the activity or the point of creation
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        s_mContext = this;
        setContentView(R.layout.activity_main);
        //set all the Application variables
        ApplicationClass.s_magChange = 0;
        ApplicationClass.s_lv_Rss = findViewById(R.id.lv_Rss);
        ApplicationClass.s_titles = new ArrayList<String>();
        ApplicationClass.s_dateTitles = new ArrayList<String>();
        ApplicationClass.s_links = new ArrayList<String>();
        ApplicationClass.s_descript = new ArrayList<String>();
        ApplicationClass.s_geoLat = new ArrayList<String>();
        ApplicationClass.s_geoLng = new ArrayList<String>();
        ApplicationClass.s_magChanges = new ArrayList<Integer>();
        ApplicationClass.a_map = new Intent(this, eq_gps_map.class);
        ApplicationClass.a_inspection = new Intent(this, activity_inspect.class);

        //adds a on click listener onto all of the list items
        ApplicationClass.s_lv_Rss.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            ApplicationClass.s_lat = Double.parseDouble(ApplicationClass.s_geoLat.get(position));
            ApplicationClass.s_lng = Double.parseDouble(ApplicationClass.s_geoLng.get(position));
            ApplicationClass.s_magChange = 50 - ApplicationClass.s_magChanges.get(position);
            ApplicationClass.s_currDescript = ApplicationClass.s_descript.get(position);
            startActivity(ApplicationClass.a_inspection);

            }
        });
        //Initialize the background processing.
        ApplicationClass.s_bgp = new BackgroundProcessing();
        ApplicationClass.s_bgp.execute();
    }
    //adding menu option
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.listmenu, menu);
        m_item_lv = menu.getItem(0);
        m_item_nesw = menu.getItem(1);
        m_item_dns = menu.getItem(2);
        m_item_mag = menu.getItem(3);
        return super.onCreateOptionsMenu(menu);
    }

    //if the menu option are selected this will trigger
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id)
        {
            case R.id.lv:
                m_item_lv.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                m_item_nesw.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_dns.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_mag.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                try {
                    ApplicationClass.s_bgp.Date_Sort(dateFrom, dateTo);
                } catch (ParseException exc) {
                    exc.printStackTrace();
                }
                break;
            case R.id.nesw:
                m_item_lv.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_nesw.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                m_item_dns.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_mag.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                ApplicationClass.s_bgp.Most_NESW();
                break;
            case R.id.dns:
                m_item_lv.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_nesw.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_dns.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                m_item_mag.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                ApplicationClass.s_bgp.Shallowest_Deepest();
                break;
            case R.id.large_mag:
                int highestMag = 0;
                m_item_lv.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_nesw.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_dns.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);
                m_item_mag.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_IF_ROOM);
                ApplicationClass.s_bgp.Highest_Mag();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    public void onWhatButton(Button inButton,View findViewId)
    {
        if(inButton.getId() == findViewById(R.id.b_from).getId())
        {
            DialogFragment dp = new DatePicker();
            dp.show(getSupportFragmentManager(), "date from");
            currButton = inButton;
        }

    }
    @Override
    public void onClick(View v) {
        Button b = (Button) v;

        if(b.getId() == findViewById(R.id.b_from).getId())
        {
            DialogFragment dp = new DatePicker();
            dp.show(getSupportFragmentManager(), "date from");
            currButton = b;

        }
        if(b.getId() == findViewById(R.id.b_to).getId())
        {
            DialogFragment dp = new DatePicker();
            dp.show(getSupportFragmentManager(), "date To");
            currButton = b;
        }
        if(b.getId() == findViewById(R.id.b_setDate).getId())
        {
            if(dateFrom != null)
            {
                try {
                    ApplicationClass.s_bgp.Date_Sort(dateFrom, dateTo);
                } catch (ParseException exc) {
                    exc.printStackTrace();
                }
            }



        }
    }

    @Override
    public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        if(currButton.getId() == findViewById(R.id.b_from).getId())
        {
        dateFrom = DateFormat.getDateInstance(DateFormat.DEFAULT).format(cal.getTime());
        dateTo = dateFrom;
        if(dateFrom.length() < 11)
        {
            dateFrom = "0" + DateFormat.getDateInstance(DateFormat.DEFAULT).format(cal.getTime());
        }
            currButton.setText(dateFrom);
            Button qb = findViewById(R.id.b_to);
            qb.setText(dateTo);
        }else if(currButton.getId() == findViewById(R.id.b_to).getId())
        {
        dateTo = DateFormat.getDateInstance(DateFormat.DEFAULT).format(cal.getTime());
            if(dateTo.length() < 11)
            {
                dateTo = "0" + DateFormat.getDateInstance(DateFormat.DEFAULT).format(cal.getTime());
            }
            currButton.setText(dateTo);
        }


    }
}