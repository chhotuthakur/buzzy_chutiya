package com.video.buzzy.fragment;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.tabs.TabLayout;
import com.video.buzzy.R;
import com.video.buzzy.adapter.ViewPagerAdapter;
import com.video.buzzy.databinding.FragmentSearchUserhashBinding;
import com.video.buzzy.viewmodel.SearchUserHashModel;
import com.video.buzzy.viewmodel.ViewModelFactory;

public class SearchUserHashFragment extends BaseFragment {

    FragmentSearchUserhashBinding binding;
    SearchUserHashModel searchUserHashModel;
    String str;
    NavController navController;


    public SearchUserHashFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_userhash, container, false);
        initView();
        return binding.getRoot();
    }

    private void initView() {
        navController = Navigation.findNavController(requireActivity(), R.id.host);
        searchUserHashModel = ViewModelProviders.of(this, new ViewModelFactory(new SearchUserHashModel()).createFor()).get(SearchUserHashModel.class);
        setTabLyt();
        settab();


        binding.remove.setOnClickListener(v -> {
            binding.etSearch.setText("");
//            onSearch();
        });


        binding.back.setOnClickListener(v -> {
            navController.popBackStack();
        });

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence s, int i, int i1, int i2) {
                if (binding.tablayout.getTabAt(0).isSelected()) {
                    searchUserHashModel.searchString.postValue(s.toString());
                    searchUserHashModel.isUser.postValue(true);
                } else if (binding.tablayout.getTabAt(1).isSelected()) {
                    searchUserHashModel.searchString.postValue(s.toString());
                    searchUserHashModel.isUser.postValue(false);
                }
                // binding.etSearch.setText("");

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }

        });

    }



    private void setTabLyt() {
        if (getActivity() != null) {
            binding.tablayout.setupWithViewPager(binding.pager);
            binding.pager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));


            binding.tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    View v = tab.getCustomView();
                    if (v != null) {
                        View tv = (View) v.findViewById(R.id.view);
                        View tv1 = (View) v.findViewById(R.id.view2);
                        TextView textView = (TextView) v.findViewById(R.id.tvTab);
                        tv1.setVisibility(View.GONE);
                        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.white));
                        tv.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.pink));
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                    View v = tab.getCustomView();
                    if (v != null) {
                        View tv = (View) v.findViewById(R.id.view);
                        View tv1 = (View) v.findViewById(R.id.view2);
                        tv1.setVisibility(View.GONE);
                        TextView textView = (TextView) v.findViewById(R.id.tvTab);
                        textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.icon_color));
                        tv.setBackgroundTintList(ContextCompat.getColorStateList(getActivity(), R.color.app_color));
                    }
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }

            });
        }
    }


    private void settab() {
        binding.tablayout.setTabGravity(TabLayout.GRAVITY_START);
        binding.tablayout.removeAllTabs();

//        for (int i = 0; i < category.size(); i++) {
//            binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView(category.get(i).getName())));
//        }
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("User")));
        binding.tablayout.addTab(binding.tablayout.newTab().setCustomView(createCustomView("Hashtag")));

        final ViewGroup test = (ViewGroup) (binding.tablayout.getChildAt(0));
        int tabLen = test.getChildCount();

        for (int i = 0; i < tabLen; i++) {
            View v = test.getChildAt(i);
            v.setPadding(5, 0, 5, 0);
        }
    }


    private View createCustomView(String s) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.custom_tabhorizontol, null);
        TextView tv = (TextView) v.findViewById(R.id.tvTab);
        tv.setText(s);
        tv.setTextColor(ContextCompat.getColor(getActivity(), R.color.icon_color));
        return v;

    }


}
