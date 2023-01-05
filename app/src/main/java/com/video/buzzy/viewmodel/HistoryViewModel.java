package com.video.buzzy.viewmodel;

import androidx.lifecycle.ViewModel;

import com.video.buzzy.adapter.AdsCoinHistoryAdapter;
import com.video.buzzy.adapter.CoinHistoryAdapter;
import com.video.buzzy.adapter.HistoryAdapter;
import com.video.buzzy.adapter.PurchaseHistoryAdapter;


public class HistoryViewModel extends ViewModel {

    public HistoryAdapter historyAdapter = new HistoryAdapter();
    public CoinHistoryAdapter coinHistoryAdapter = new CoinHistoryAdapter();
    public PurchaseHistoryAdapter purchaseHistoryAdapter = new PurchaseHistoryAdapter();
    public AdsCoinHistoryAdapter adsCoinHistory = new AdsCoinHistoryAdapter();


}
