/**
 *  Matric Number - s1920031
 */
package com.example.ocallaghan_james_s1920031;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class BackgroundProcessing extends AsyncTask<Integer, Void, Exception> {
    ProgressDialog Dialog;
    Exception exception = null;
    public static ArrayList<String> dateTitles;
    public static ArrayList<String> nesw;
    public static ArrayList<String> hghMag;
    public static ArrayList<String> lowHigh;


    public BackgroundProcessing() {
        Dialog = new ProgressDialog(MainActivity.getContext());
    }

    public InputStream getInputStream(URL url) {
        try {
            //tries to open a connection to the input url
            return url.openConnection().getInputStream();
        } catch (IOException exc) {
            return null;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //Bearly even noticable but before the listview is populated this is displayed to notify user what is happening
        Dialog.setMessage("Loading Latest Feeds... Please Wait ...");
        Dialog.show();
    }

    @Override
    protected Exception doInBackground(Integer... integers) {

        try {
            boolean enteredItem = false;
            URL url = new URL("http://quakes.bgs.ac.uk/feeds/MhSeismology.xml");
            XmlPullParserFactory PPFactory = XmlPullParserFactory.newInstance();
            PPFactory.setNamespaceAware(false);
            XmlPullParser _parsePuller = PPFactory.newPullParser();
            int PPTag = _parsePuller.getEventType();
            //read the utf formatted file
            _parsePuller.setInput(getInputStream(url), "UTF_8");
           do{
               // keep that tag in order as the parsed data otherwise may apply the incorrect data to the wrong string
               if (PPTag == XmlPullParser.START_TAG) {
                   switch (_parsePuller.getName())
                   {
                       case "item":
                           enteredItem = true;
                           break;
                       case "title":
                           if (enteredItem)
                           {
                               //drop the uneccessary part of the string that isn't required with assign specifications.
                               String[] splitter = _parsePuller.nextText().split(":", 3);
                               String[] split = splitter.clone()[2].split(",", 2);
                               ApplicationClass.s_titles.add(splitter.clone()[1] + split.clone()[0]);
                           }
                           break;
                       case "link":
                           if (enteredItem)
                           {
                               ApplicationClass.s_links.add(_parsePuller.nextText());
                           }
                           break;
                       case "description":
                           if (enteredItem)
                           {
                               ApplicationClass.s_descript.add(_parsePuller.nextText());
                           }
                           break;
                       case "geo:lat":
                           if (enteredItem)
                           {
                               ApplicationClass.s_geoLat.add(_parsePuller.nextText());
                           }
                           break;
                       case "geo:long":
                           if (enteredItem)
                           {
                               ApplicationClass.s_geoLng.add(_parsePuller.nextText());
                           }
                           break;
                   }

               } else if (PPTag == XmlPullParser.END_TAG && _parsePuller.getName().equalsIgnoreCase("item")) {

                   enteredItem = false;
               }
               PPTag = _parsePuller.next();
           }while (PPTag != XmlPullParser.END_DOCUMENT);

        } catch (MalformedURLException exc) {
            //url exception
            exception = exc;
        } catch (XmlPullParserException exc) {
            //parser exception
            exception = exc;
        } catch (IOException exc) {
            //Java IO exception
            exception = exc;
        }
        return exception;
    }

    @Override
    protected void onPostExecute(Exception exc) {
        super.onPostExecute(exc);
        ArrayAdapter<String> adapter = InitListView();
        ApplicationClass.s_lv_Rss.setAdapter(adapter);
        Dialog.dismiss();
    }

    public void Shallowest_Deepest(){
        lowHigh = new ArrayList<String>();
        lowHigh.add("");
        lowHigh.add("");
        int lowest = 0;
        int highest = 0;
        if(BackgroundProcessing.dateTitles.size() > 1)
        {
            for (int d = 0; d < BackgroundProcessing.dateTitles.size(); d++)
            {
                for (int t = 0; t < ApplicationClass.s_titles.size(); t++)
                {
                    if(BackgroundProcessing.dateTitles.get(d) == ApplicationClass.s_titles.get(t))
                    {
                        String[] splitter = ApplicationClass.s_descript.get(t).split(":",5);
                        String[] splitter2 = splitter.clone()[4].split(":",0);
                        splitter2 = splitter2.clone()[2].split(";", 0);
                        splitter2 = splitter2.clone()[0].split(" km", 0);
                        String result = splitter2.clone()[0].replaceAll(" ", "");
                        if(Integer.parseInt(result) > highest)
                        {
                            highest = Integer.parseInt(result);
                            lowest = highest;
                            lowHigh.remove(0);
                            lowHigh.add(ApplicationClass.s_titles.get(t));
                        }else{
                            if(Integer.parseInt(result) < lowest)
                            {
                                lowest = Integer.parseInt(result);
                                lowHigh.remove(1);
                                lowHigh.add(ApplicationClass.s_titles.get(t));
                            }
                        }
                    }
                }
            }
            ApplicationClass.s_lv_Rss.setAdapter(new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, lowHigh) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    ColorCode(lowHigh, position, view);
                    return view;
                }
            });
        }else{
            ApplicationClass.s_lv_Rss.setAdapter(new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, dateTitles) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    ColorCode(dateTitles,position,view);
                    return view;
                }
            });
        }



    }
    public void Most_NESW(){
    nesw = new ArrayList<String>();
    ArrayList<String> NS = new ArrayList<String>();
    if(BackgroundProcessing.dateTitles.size() > 5)
    {

        for (int entries = 0; entries < dateTitles.size(); entries++)
        {
            for(int c = 0; c < ApplicationClass.s_titles.size(); c++)
            {
                if(ApplicationClass.s_titles.get(c) ==  dateTitles.get(entries) )
                {
                    nesw.add(ApplicationClass.s_geoLat.get(c) + dateTitles.get(entries) + " : Most ");
                }
            }

        }
        Collections.sort(nesw);
        nesw.set(0, nesw.get(0) + "Northernly.");
        while(nesw.size() > 2)
        {
            nesw.remove(1);
        }
        nesw.set(1, nesw.get(1) + "Southernly.");
        NS.add(nesw.get(0));
        NS.add(nesw.get(1));
        nesw.clear();
        for (int entries = 0; entries < dateTitles.size(); entries++)
        {
            for(int c = 0; c < ApplicationClass.s_titles.size(); c++)
            {
                if(ApplicationClass.s_titles.get(c) == dateTitles.get(entries))
                {
                    nesw.add(ApplicationClass.s_geoLng.get(c) + dateTitles.get(entries) + " : Most " );
                }
            }

        }
        Collections.sort(nesw);
        nesw.set(0,nesw.get(0) + "Easterly.");
        while (nesw.size()> 2)
        {
            nesw.remove(1);
        }
        nesw.set(1,nesw.get(1) + "Westerly.");
        nesw.add(NS.get(0));
        nesw.add(NS.get(1));


        //above will be sorting the most north east south and westerly earthquakes
        ApplicationClass.s_lv_Rss.setAdapter(new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, nesw) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                ColorCode(dateTitles, position, view);
                return view;
            }
        });
    }else{
        ApplicationClass.s_lv_Rss.setAdapter(new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, dateTitles) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                ColorCode(dateTitles,position,view);
                return view;
            }
        });
    }
    }
    public void Highest_Mag(){
        hghMag = new ArrayList<String>();
        hghMag.add("");
        int highestMag = 0;
        if(BackgroundProcessing.dateTitles.size() > 0)
        {

            for (int d = 0; d < BackgroundProcessing.dateTitles.size(); d++)
            {
                for (int t = 0; t < ApplicationClass.s_titles.size(); t++)
                {
                    if(BackgroundProcessing.dateTitles.get(d) == ApplicationClass.s_titles.get(t))
                    {
                        if(ApplicationClass.s_magChanges.get(t).intValue() > highestMag)
                        {
                            highestMag  = ApplicationClass.s_magChanges.get(t).intValue();
                            hghMag.remove(0);
                            hghMag.add(ApplicationClass.s_titles.get(t));
                            ApplicationClass.s_magChanges.get(t);
                        }


                    }
                }
            }
            ApplicationClass.s_lv_Rss.setAdapter(new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, hghMag) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);
                    ColorCode(hghMag, position, view);
                    return view;
                }
            });
        }else{
            ApplicationClass.s_lv_Rss.setAdapter(new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, dateTitles) {
                @NonNull
                @Override
                public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                    View view = super.getView(position, convertView, parent);

                    ColorCode(dateTitles,position,view);
                    return view;
                }
            });
        }



    }
    public void Date_Sort(String dateFrom, String dateTo) throws ParseException {
        dateTitles = new ArrayList<String>();
        Calendar formatter = Calendar.getInstance();
        Date dateF = DateFormat.getDateInstance().parse(dateFrom);
        Date dateT = DateFormat.getDateInstance().parse(dateTo);
        formatter.setTime(dateF);

        for(int i = 0; i < ApplicationClass.s_titles.size(); i++)
        {
            String split = ApplicationClass.s_descript.get(i);
            String[] Splitter = split.split(":", 4);
            String[] Split2 = Splitter.clone()[1].split(",", 2);
            String[] split3 = Split2.clone()[1].split(" ", 2);
            if(Split2.clone()[1].length() > 11)
            {

                Date nowDate = DateFormat.getDateInstance().parse(split3.clone()[1]);
                if(nowDate.getTime() >= dateF.getTime() && nowDate.getTime() <= dateT.getTime())
                {
                    dateTitles.add(ApplicationClass.s_titles.get(i));
                }

            }

        }

        ApplicationClass.s_lv_Rss.setAdapter(new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, dateTitles) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                ColorCode(dateTitles,position,view);
                return view;
            }
        });
    }
    public ArrayAdapter InitListView() {

        return new ArrayAdapter<String>(MainActivity.getContext(), android.R.layout.simple_list_item_1, ApplicationClass.s_titles) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

                View view = super.getView(position, convertView, parent);
                ColorCode(ApplicationClass.s_titles,position,view);
                return view;
            }
        };
    }
    public void ColorCode(ArrayList<String> inList,int pos,View inView){
        String titleString = inList.get(pos);
        String s = String.valueOf(titleString.charAt(4)) + titleString.charAt(6);
        Integer i = Integer.parseInt(s);
        ApplicationClass.s_magChanges.add(ApplicationClass.Get_s_magChange(i.intValue()));
        inView.setBackgroundColor(0xffffff00 * (4 * ApplicationClass.Get_s_magChange(i.intValue())));
    }
}
