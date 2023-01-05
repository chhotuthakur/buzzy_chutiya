package com.video.buzzy.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Editable;
import android.widget.EditText;

import com.google.android.material.color.MaterialColors;
import com.otaliastudios.autocomplete.Autocomplete;
import com.otaliastudios.autocomplete.AutocompleteCallback;
import com.otaliastudios.autocomplete.CharPolicy;
import com.video.buzzy.R;
import com.video.buzzy.modelRetrofit.HashtagVideoRoot;
import com.video.buzzy.modelRetrofit.SearchUserRoot;
import com.video.buzzy.util.autocomplete.HashtagPresenter;
import com.video.buzzy.util.autocomplete.UserPresenter;


final public class AutocompleteUtil {

    public static void setupForHashtags(Context context, EditText input) {
        int color = MaterialColors.getColor(context, R.attr.colorSurface, Color.BLACK);
        Autocomplete.<HashtagVideoRoot.HashtagItem>on(input)
                .with(5)
                .with(new ColorDrawable(color))
                .with(new CharPolicy('#'))
                .with(new HashtagPresenter(context))
                .with(new AutocompleteCallback<HashtagVideoRoot.HashtagItem>() {

                    @Override
                    public boolean onPopupItemClicked(Editable editable, HashtagVideoRoot.HashtagItem item) {
                        int[] range = CharPolicy.getQueryRange(editable);
                        if (range == null) {
                            return false;
                        }

                        editable.replace(range[0], range[1], item.getHashtag());
                        return true;
                    }

                    @Override
                    public void onPopupVisibilityChanged(boolean shown) {
                    }
                })
                .build();
    }

    public static void setupForUsers(Context context, EditText input) {
        int color = MaterialColors.getColor(context, R.attr.colorSurface, Color.BLACK);
        Autocomplete.<SearchUserRoot.SearchItem>on(input)
                .with(5)
                .with(new ColorDrawable(color))
                .with(new CharPolicy('@'))
                .with(new UserPresenter(context))
                .with(new AutocompleteCallback<SearchUserRoot.SearchItem>() {

                    @Override
                    public boolean onPopupItemClicked(Editable editable, SearchUserRoot.SearchItem item) {
                        int[] range = CharPolicy.getQueryRange(editable);
                        if (range == null) {
                            return false;
                        }

                        editable.replace(range[0], range[1], item.getUsername());
                        return true;
                    }

                    @Override
                    public void onPopupVisibilityChanged(boolean shown) {
                    }
                })
                .build();
    }
}
