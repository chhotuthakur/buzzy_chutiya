package com.video.buzzy.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;


public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {

    private final Context mContext;
    //    private final List<VideoFilter> mFilters = Arrays.asList(VideoFilter.values());
    private List<String> mFilters = new ArrayList<>();

    private OnFilterSelectListener mListener;
//    int[] thumblist = new int[]{R.drawable.thumb1, R.drawable.thumb2,R.drawable.thumb3,R.drawable.thumb4,R.drawable.thumb5,
//                                  R.drawable.thumb6,R.drawable.thumb7,R.drawable.thumb8,R.drawable.thumb9,R.drawable.thumb10,R.drawable.thumb11,R.drawable.thumb12,R.drawable.thumb13,R.drawable.thumb14};


//    public FilterAdapter(Context context, Bitmap thumbnail) {
//        mContext = context;
//        mThumbnail = thumbnail;
//    }


    public FilterAdapter(Context mContext, List<String> filterlist) {
        this.mContext = mContext;
        this.mFilters = filterlist;
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
        //holder.setIsRecyclable(false);
        // holder.image.setImage(mThumbnail);
        return holder;
    }

    @Override
    @SuppressLint("SetTextI18n")
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {

        Glide.with(mContext).load(mFilters.get(position)).into(holder.image);


//        final VideoFilter filter = mFilters.get(position);
//        switch (filter) {
//            case BRIGHTNESS: {
//                GPUImageBrightnessFilter glf = new GPUImageBrightnessFilter();
//                glf.setBrightness(0.2f);
//                holder.image.setFilter(glf);
//                break;
//            }
//            case EXPOSURE:
//                holder.image.setFilter(new GPUImageExposureFilter());
//                break;
//            case GAMMA: {
//                GPUImageGammaFilter glf = new GPUImageGammaFilter();
//                glf.setGamma(2f);
//                holder.image.setFilter(glf);
//                break;
//            }
//            case GRAYSCALE:
//                holder.image.setFilter(new GPUImageGrayscaleFilter());
//                break;
//            case HAZE: {
//                GPUImageHazeFilter glf = new GPUImageHazeFilter();
//                glf.setSlope(-0.5f);
//                holder.image.setFilter(glf);
//                break;
//            }
//            case INVERT:
//                holder.image.setFilter(new GPUImageColorInvertFilter());
//                break;
//            case MONOCHROME:
//                holder.image.setFilter(new GPUImageMonochromeFilter());
//                break;
//            case PIXELATED: {
//                GPUImagePixelationFilter glf = new GPUImagePixelationFilter();
//                glf.setPixel(5);
//                holder.image.setFilter(glf);
//                break;
//            }
//            case POSTERIZE:
//                holder.image.setFilter(new GPUImagePosterizeFilter());
//                break;
//            case SEPIA:
//                holder.image.setFilter(new GPUImageSepiaToneFilter());
//                break;
//            case SHARP: {
//                GPUImageSharpenFilter glf = new GPUImageSharpenFilter();
//                glf.setSharpness(1f);
//                holder.image.setFilter(glf);
//                break;
//            }
//            case SOLARIZE:
//                holder.image.setFilter(new GPUImageSolarizeFilter());
//                break;
//            case VIGNETTE:
//                holder.image.setFilter(new GPUImageVignetteFilter());
//                break;
//            default:
//                holder.image.setFilter(new GPUImageFilter());
//                break;
        //}

        // String name = filter.name().toLowerCase(Locale.US);
        //holder.name.setText(name.substring(0, 1).toUpperCase() + name.substring(1));
        holder.itemView.setOnClickListener(view -> {
//            if (mListener != null) {
//                mListener.onSelectFilter(filter);
//            }
        });
    }

    public void setListener(OnFilterSelectListener listener) {
        mListener = listener;
    }

    public interface OnFilterSelectListener {

        void onSelectFilter(VideoFilter filter);
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;

        public FilterViewHolder(@NonNull View root) {
            super(root);
            image = root.findViewById(R.id.image);
            name = root.findViewById(R.id.name);
        }
    }
}
