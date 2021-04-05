/**
 *  Matric Number - s1920031
 */
package com.example.ocallaghan_james_s1920031;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TableLayout;
import android.widget.TextView;

public class activity_inspect extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inspect);
        ApplicationClass.s_tl_Desc = findViewById(R.id.s_tl_Desc);
        TextView dt_Title = findViewById(R.id.dt_Title);
        TextView dnt = findViewById(R.id.dnt);
        TextView loc_Title = findViewById(R.id.loc_Title);
        TextView loc = findViewById(R.id.loc);
        TextView lnl_Title = findViewById(R.id.lnl_Title);
        TextView latnlng = findViewById(R.id.latnlng);
        TextView d_Title = findViewById(R.id.d_Title);
        TextView dpth = findViewById(R.id.dpth);
        TextView m_Title = findViewById(R.id.m_Title);
        TextView mag = findViewById(R.id.mag);
        //split up my string to display the appropriate text at correct text views
        String[] splitter = ApplicationClass.s_currDescript.split(";",5);
        String[] varyingSplit;
        for (int s =0; s < splitter.length; s++)
        {
            switch(s)
            {
            case 0:
                varyingSplit = splitter.clone()[s].split(":", 2);
                dt_Title.setText(varyingSplit.clone()[0]);
                dnt.setText(varyingSplit.clone()[1]);
                break;
            case 1:
                varyingSplit = splitter.clone()[s].split(":", 2);
                loc_Title.setText(varyingSplit.clone()[0]);
                loc.setText(varyingSplit.clone()[1]);
                break;
            case 2:
                varyingSplit = splitter.clone()[s].split(":", 2);
                lnl_Title.setText(varyingSplit.clone()[0]);
                latnlng.setText(varyingSplit.clone()[1]);
                break;
            case 3:
                varyingSplit = splitter.clone()[s].split(":", 2);
                d_Title.setText(varyingSplit.clone()[0]);
                dpth.setText(varyingSplit.clone()[1]);
                break;
            case 4:
                varyingSplit = splitter.clone()[s].split(":", 2);
                m_Title.setText(varyingSplit.clone()[0]);
                mag.setText(varyingSplit.clone()[1]);
                break;
            }
        }

        ApplicationClass.s_tl_Desc.setOnClickListener(new AdapterView.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(ApplicationClass.a_map);
            }
        });
    }

}