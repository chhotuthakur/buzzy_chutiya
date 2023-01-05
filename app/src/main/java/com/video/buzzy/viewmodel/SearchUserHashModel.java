package com.video.buzzy.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.video.buzzy.adapter.HashtagListAdapter;
import com.video.buzzy.adapter.UserListAdapter;
import com.video.buzzy.design.Hashtag;
import com.video.buzzy.design.UserList;

import java.util.ArrayList;
import java.util.List;

public class SearchUserHashModel extends ViewModel {
   public HashtagListAdapter hashtagListAdapter = new HashtagListAdapter();
   public UserListAdapter userListAdapter = new UserListAdapter();
   public List<String> hashlist = new ArrayList<>();
   public List<UserList> userlist = new ArrayList<>();
   public List<Hashtag> hashtagList = new ArrayList<>();
   public MutableLiveData<String> searchString = new MutableLiveData<>();
   public MutableLiveData<Boolean> isUser = new MutableLiveData<>(false);

   @Override
   protected void onCleared() {
      userListAdapter.clear();
      hashtagListAdapter.clear();
      super.onCleared();
   }


}
