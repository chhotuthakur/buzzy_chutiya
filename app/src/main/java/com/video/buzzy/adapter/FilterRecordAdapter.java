package com.video.buzzy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.video.buzzy.R;
import com.video.buzzy.filter.VideoFilter;
import com.video.buzzy.util.Const;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import jp.co.cyberagent.android.gpuimage.filter.GPUImageBrightnessFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageGammaFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageHazeFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImagePixelationFilter;
import jp.co.cyberagent.android.gpuimage.filter.GPUImageSharpenFilter;


public class FilterRecordAdapter extends RecyclerView.Adapter<FilterRecordAdapter.FilterViewHolder> {

    private final Context mContext;
    private final List<VideoFilter> mFilters = Arrays.asList(VideoFilter.values());
    ArrayList<String> filterThumbList = new ArrayList<>();
    private OnFilterSelectListener mListener;
    private int selectedEffect = 0;

    public FilterRecordAdapter(Context context) {
        mContext = context;
    }


    @Override
    public int getItemCount() {
        return mFilters.size();
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.item_filter, parent, false);
        FilterViewHolder holder = new FilterViewHolder(view);
        holder.setIsRecyclable(false);
        //holder.image.setImage(mThumbnail);
        return holder;
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        holder.iv_selected_effect.setVisibility(View.GONE);

        filterThumbList = Const.listFilterThumb(mContext);

        Log.d("filter", "onBindViewHolder: " + filterThumbList.get(position));
        Glide.with(mContext).load(filterThumbList.get(position)).into(holder.image);

        if (position == selectedEffect) {
            holder.iv_selected_effect.setVisibility(View.VISIBLE);
        }


        // holder.image.setImage(BitmapFactory.decodeFile(filterThumbList.get(position)));

        final VideoFilter filter = mFilters.get(position);
        switch (filter) {
            case NONE:
                break;
            case BRIGHTNESS: {
                GPUImageBrightnessFilter glf = new GPUImageBrightnessFilter();
                glf.setBrightness(0.2f);
                // holder.image.setFilter(glf);
                break;
            }
            case EXPOSURE:
                // holder.image.setFilter(new GPUImageExposureFilter());
                break;
            case GAMMA: {
                GPUImageGammaFilter glf = new GPUImageGammaFilter();
                glf.setGamma(2f);
                // holder.image.setFilter(glf);
                break;
            }
            case GRAYSCALE:
                //holder.image.setFilter(new GPUImageGrayscaleFilter());
                break;
            case HAZE: {
                GPUImageHazeFilter glf = new GPUImageHazeFilter();
                glf.setSlope(-0.5f);
                //  holder.image.setFilter(glf);
                break;
            }
            case INVERT:
                // holder.image.setFilter(new GPUImageColorInvertFilter());
                break;
            case MONOCHROME:
                // holder.image.setFilter(new GPUImageMonochromeFilter());
                break;
            case PIXELATED: {
                GPUImagePixelationFilter glf = new GPUImagePixelationFilter();
                glf.setPixel(5);
                //  holder.image.setFilter(glf);
                break;
            }
            case POSTERIZE:
                //  holder.image.setFilter(new GPUImagePosterizeFilter());
                break;
            case SEPIA:
                //  holder.image.setFilter(new GPUImageSepiaToneFilter());
                break;
            case SHARP: {
                GPUImageSharpenFilter glf = new GPUImageSharpenFilter();
                glf.setSharpness(1f);
                // holder.image.setFilter(glf);
                break;
            }
            case SOLARIZE:
                // holder.image.setFilter(new GPUImageSolarizeFilter());
                break;
            case VIGNETTE:
                // holder.image.setFilter(new GPUImageVignetteFilter());
                break;
            default:
                // holder.image.setFilter(new GPUImageFilter());
                break;
        }

        String name = filter.name().toLowerCase(Locale.US);
        holder.name.setText(name.substring(0, 1).toUpperCase() + name.substring(1));
        holder.itemView.setOnClickListener(view -> {
            selectedEffect = position;
            if (mListener != null) {
                mListener.onSelectFilter(filter);
            }
            notifyDataSetChanged();
        });
    }

    public void setListener(OnFilterSelectListener listener) {
        mListener = listener;
    }

    public interface OnFilterSelectListener {
        void onSelectFilter(VideoFilter filter);
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        public ImageView image, iv_selected_effect;
        public TextView name;

        public FilterViewHolder(@NonNull View root) {
            super(root);
            image = root.findViewById(R.id.image);
            iv_selected_effect = root.findViewById(R.id.iv_selected_effect);
            name = root.findViewById(R.id.name);
        }
    }
}
