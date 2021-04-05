/**
 *  Matric Number - s1920031
 */
package com.example.ocallaghan_james_s1920031;
import android.app.Application;
import android.content.Intent;
import android.widget.ListView;
import android.widget.TableLayout;
import java.util.ArrayList;

public class ApplicationClass extends Application {

    //Background process
    public static BackgroundProcessing s_bgp;

    //Application activities
    public static Intent a_map;
    public static Intent a_inspection;

    //application variables
    public static Double s_lat;
    public static Double s_lng;
    public static ListView s_lv_Rss;
    public static TableLayout s_tl_Desc;
    public static ArrayList<String> s_titles;
    public static ArrayList<String> s_dateTitles;
    public static ArrayList<String> s_links;
    public static ArrayList<String> s_descript;
    public static ArrayList<String> s_geoLat;
    public static ArrayList<String> s_geoLng;
    public static Integer s_magChange;
    public static String s_currDescript;
    public static ArrayList<Integer> s_magChanges;


    @Override
    public void onCreate() {
        super.onCreate();
        s_lat = 0.0;
        s_lng = 0.0;
    }

    public static Integer Get_s_magChange(int in) {
        return s_magChange = in;
    }
    //starting activties like this
    //Uri uri = Uri.parse(ApplicationClass.s_links.get(position));
    //Intent intent = new Intent(Intent.ACTION_VIEW, uri);
    //startActivity(intent);
}
