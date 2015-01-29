package com.example.stockexample;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by dreesj1 on 12/17/14.
 */
public class MyAdapter extends ArrayAdapter<MyCompany> {

    private final Context context;
    private final ArrayList<MyCompany> itemsArrayList;

    public MyAdapter(Context context, ArrayList<MyCompany> itemsArrayList) {
        super(context, R.layout.company_row, itemsArrayList);
        this.context = context;
        this.itemsArrayList = itemsArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // create inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // get rowView from inflater
        View rowView = inflater.inflate(R.layout.company_row, parent, false);
        // get the text views from the rowView
        TextView symbolView = (TextView) rowView.findViewById(R.id.company_symbol_row);
        TextView nameView = (TextView) rowView.findViewById(R.id.company_name_row);
        TextView stockView = (TextView) rowView.findViewById(R.id.company_stock_row);
        // set text for textView
        symbolView.setText(itemsArrayList.get(position).getCompanySymbol());
        nameView.setText(itemsArrayList.get(position).getCompanyName());
        stockView.setText(itemsArrayList.get(position).getCompanyStock());
        // set color for stock based on positive or negative change
        if (itemsArrayList.get(position).getCompanyStock().charAt(0) == '+')
            stockView.setTextColor(Color.GREEN);
        else if (itemsArrayList.get(position).getCompanyStock().charAt(0) == '-')
            stockView.setTextColor(Color.RED);
        else
            stockView.setTextColor(Color.YELLOW);

        return rowView;
    }
}
