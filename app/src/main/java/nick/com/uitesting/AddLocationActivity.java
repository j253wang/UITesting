package nick.com.uitesting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Jianing Wang on 2/21/2017.
 */

public class AddLocationActivity extends AppCompatActivity{
    private ListView myList;
    private ItemsListAdapter myAdapter;
    private ImageView myImage;
    public ArrayList<ListItem> myItems = new ArrayList<ListItem>();

    class ViewHolder
    {
        TextView text;
        EditText captionEditText;
        Button addButton;
        Button deleteButton;
        TimePicker arrivalTime;
        Calendar calendar;
    }

    class ListItem {
        String textdata;
        String caption;
        Integer hour;
        Integer minute;

        ListItem(String text, String capshun){
            caption = capshun;
            textdata = text;
        }

        String getCaption()
        {
            return caption;
        }

        String getTextdata()
        {
            return textdata;
        }

        Integer getHour()
        {
            return hour;
        }

        Integer getMinute()
        {
            return minute;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes_drawer);

        myList = (ListView) findViewById(R.id.RouteList);
        myImage = (ImageView) findViewById(R.id.randomImage1);

        initItems();
        myAdapter = new ItemsListAdapter(this, myItems);
        myList.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();

        // Is this needed?
        myList.setOnItemClickListener(new OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(AddLocationActivity.this,
                        ((ListItem)(parent.getItemAtPosition(position))).textdata,
                        Toast.LENGTH_LONG).show();
            }});

        /*
        Intent sendLocationIntent = new Intent(this, DrawerActivity.class);
        sendLocationIntent.putExtra(ApplicationMain.Key_Locations, getLocations());
        startActivity(sendLocationIntent);
        */
    }

    @Override
    public void onBackPressed()
    {
        Intent sendLocationIntent = new Intent(this, DrawerActivity.class);
        sendLocationIntent.putExtra(ApplicationMain.Key_Locations, getLocations());
        startActivity(sendLocationIntent);

        SaveContent(getLocations());
        super.onBackPressed();
    }

    public void SaveContent(String[] writable)
    {
        JSONArray JsonArray = new JSONArray();
        for (String loc : writable)
        {
            JsonArray.put(loc);
        }

        FileOutputStream fos;
        try {
            fos = openFileOutput(ApplicationMain.StorageFile, Context.MODE_PRIVATE);
            // Clear contents of file
            Log.i("Writing to file", JsonArray.toString());
            fos.write (JsonArray.toString().getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ItemsListAdapter extends BaseAdapter {

        private Context context;
        private List<ListItem> list;
        private LayoutInflater mInflater; //

        ItemsListAdapter(Context c, List<ListItem> l){
            context = c;
            list = l;
            mInflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE); //
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public ListItem getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowView = convertView;
            final ViewHolder viewHolder;
            // reuse views
            if (rowView == null) {
                LayoutInflater inflater = ((Activity) context).getLayoutInflater();
                rowView = inflater.inflate(R.layout.route_adder_drawer, null);
                viewHolder = new ViewHolder();
                //viewHolder.icon = (ImageView) rowView.findViewById(R.id.rowImageView);
                viewHolder.text = (TextView) rowView.findViewById(R.id.textView1);
                viewHolder.captionEditText = (EditText) rowView.findViewById(R.id.LocationEdit);
                viewHolder.addButton = (Button) rowView.findViewById(R.id.buttonAdd);
                viewHolder.deleteButton = (Button) rowView.findViewById(R.id.buttonRemove);
                viewHolder.arrivalTime = (TimePicker) rowView.findViewById(R.id.ArriveBy);
                rowView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.text.setTag(position);
            viewHolder.captionEditText.setTag(position);
            viewHolder.captionEditText.setText(getItem(position).caption);
            viewHolder.addButton.setTag(position);
            viewHolder.deleteButton.setTag(position);
            viewHolder.arrivalTime.setTag(position);
            
            viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    int tag = (Integer) view.getTag();
                    //if (tag != (myItems.size() - 1)) {
                    myItems.remove(tag);
                    Log.d("GCM", "Item removed from " + tag);
                    myAdapter.notifyDataSetChanged();
                }
            });

            viewHolder.addButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int tag = (Integer) view.getTag();
                    ListItem listItem = new ListItem("HueHueHue", "@");
                    myItems.add(tag+1, listItem);

                    /*
                     * Log.d("GCM", holder.captionEditText.getText()
                     * .toString()); myItems.get((Integer)
                     * view.getTag()).caption = holder.captionEditText
                     * .getText().toString();
                     */
                    myAdapter.notifyDataSetChanged();
                    // myList.setSelection(myAdapter.getCount() - 1);
                    // holder.captionEditText.setFocusable(true);
                    // holder.captionEditText.requestFocus();
                }
            });

            viewHolder.captionEditText.addTextChangedListener(new TextWatcher() {

                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    // TODO Auto-generated method stub
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // TODO Auto-generated method stub
                }

                public void afterTextChanged(Editable s) {
                    // if(position < myItems.size())
                    // getItem(position).caption = s.toString();

                    myItems.get((Integer) viewHolder.captionEditText.getTag()).caption = viewHolder.captionEditText
                            .getText().toString();
                }
            });

            viewHolder.arrivalTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                @Override
                public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                    //Display the new time to app interface
                    myItems.get((Integer) view.getTag()).hour = hourOfDay;
                    myItems.get((Integer) view.getTag()).minute = minute;
                }
            });

            if (position != (myItems.size() - 1)) {
                //viewHolder.addOrDeleteButton.setBackgroundResource(R.drawable.ic_remove_route);
            } else {
                //viewHolder.addOrDeleteButton.setBackgroundResource(R.drawable.ic_add_route);
                viewHolder.text.setFocusable(true);
                viewHolder.captionEditText.setFocusable(true);
                viewHolder.text.requestFocus();
                viewHolder.captionEditText.requestFocus();
            }

            ///
            ViewHolder holder = (ViewHolder) rowView.getTag();
            //holder.icon.setImageDrawable(list.get(position).ItemDrawable);
            holder.text.setText(list.get(position).textdata);

            return rowView;
        }

        public List<ListItem> getList(){
            return list;
        }
    }

    // TODO nick, remove this thing later to only have one route
    private void initItems(){
        myItems = new ArrayList<ListItem>();

        TypedArray arrayText = getResources().obtainTypedArray(R.array.restext);

        for(int i=0; i<arrayText.length(); i++){
            String s = arrayText.getString(i);
            ListItem item = new ListItem("Item", s);
            myItems.add(item);
        }

        arrayText.recycle();
    }

    private String[] getLocations()
    {
        ArrayList<String> locations = new ArrayList<String>();
        Iterator<ListItem> it = myItems.iterator();

        while (it.hasNext())
        {
            locations.add(it.next().getCaption());
        }
        return locations.toArray(new String[locations.size()]);
    }
}
