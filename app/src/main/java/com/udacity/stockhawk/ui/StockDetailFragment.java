package com.udacity.stockhawk.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.udacity.stockhawk.R;

import java.util.ArrayList;
import java.util.List;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;


public class StockDetailFragment extends Fragment {
    private static final String ARG_STOCK_NAME = "stock_ticker_name";

    private LineChart mChart;

    private String mStockName;

    private List<HistoricalQuote> mStockHistory;

    public StockDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param stockName The stock ticker name.
     * @return A new instance of fragment StockDetailFragment.
     */
    public static StockDetailFragment newInstance(final String stockName) {
        StockDetailFragment fragment = new StockDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_STOCK_NAME, stockName);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String stockSymbol = getArguments().getString(ARG_STOCK_NAME);
            try {
                Stock stock = YahooFinance.get(stockSymbol);
                mStockName = stock.getName();
                mStockHistory = stock.getHistory();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stock_detail, container, false);
        mChart = (LineChart) view.findViewById(R.id.stock_history_chart);
        configureChart();
        return view;
    }

    /**
     * Configure the graph to be displayed.
     */
    private void configureChart() {
        if (mStockHistory != null) {
            List<Entry> entries = new ArrayList<>();
            for (HistoricalQuote quote : mStockHistory) {
                entries.add(new Entry(quote.getDate().getTimeInMillis(),
                        quote.getClose().floatValue()));
            }
            LineDataSet entrySet = new LineDataSet(entries, mStockName);
            LineData data = new LineData(entrySet);
            mChart.setData(data);
            mChart.invalidate();
        }
    }
}
