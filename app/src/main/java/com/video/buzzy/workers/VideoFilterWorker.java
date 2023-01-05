package com.video.buzzy.workers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.util.Size;

import androidx.annotation.NonNull;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;

import com.daasuu.mp4compose.VideoFormatMimeType;
import com.daasuu.mp4compose.composer.Mp4Composer;
import com.daasuu.mp4compose.filter.GlBilateralFilter;
import com.daasuu.mp4compose.filter.GlBrightnessFilter;
import com.daasuu.mp4compose.filter.GlCGAColorspaceFilter;
import com.daasuu.mp4compose.filter.GlContrastFilter;
import com.daasuu.mp4compose.filter.GlCrosshatchFilter;
import com.daasuu.mp4compose.filter.GlFilter;
import com.daasuu.mp4compose.filter.GlFilterGroup;
import com.daasuu.mp4compose.filter.GlGammaFilter;
import com.daasuu.mp4compose.filter.GlGaussianBlurFilter;
import com.daasuu.mp4compose.filter.GlGrayScaleFilter;
import com.daasuu.mp4compose.filter.GlHalftoneFilter;
import com.daasuu.mp4compose.filter.GlHazeFilter;
import com.daasuu.mp4compose.filter.GlHighlightShadowFilter;
import com.daasuu.mp4compose.filter.GlHueFilter;
import com.daasuu.mp4compose.filter.GlInvertFilter;
import com.daasuu.mp4compose.filter.GlLookUpTableFilter;
import com.daasuu.mp4compose.filter.GlLuminanceFilter;
import com.daasuu.mp4compose.filter.GlLuminanceThresholdFilter;
import com.daasuu.mp4compose.filter.GlMonochromeFilter;
import com.daasuu.mp4compose.filter.GlOpacityFilter;
import com.daasuu.mp4compose.filter.GlPixelationFilter;
import com.daasuu.mp4compose.filter.GlPosterizeFilter;
import com.daasuu.mp4compose.filter.GlRGBFilter;
import com.daasuu.mp4compose.filter.GlSaturationFilter;
import com.daasuu.mp4compose.filter.GlSepiaFilter;
import com.daasuu.mp4compose.filter.GlSharpenFilter;
import com.daasuu.mp4compose.filter.GlSolarizeFilter;
import com.daasuu.mp4compose.filter.GlToneCurveFilter;
import com.daasuu.mp4compose.filter.GlVibranceFilter;
import com.daasuu.mp4compose.filter.GlVignetteFilter;
import com.google.common.util.concurrent.ListenableFuture;
import com.video.buzzy.R;
import com.video.buzzy.effect.FilterType;
import com.video.buzzy.util.Const;
import com.video.buzzy.util.VideoUtil;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;


public class VideoFilterWorker extends ListenableWorker {

    public static final String KEY_FILTER = "filter";
    public static final String KEY_INPUT = "input";
    public static final String KEY_OUTPUT = "output";
    private static final String TAG = "VideoFilterWorker";
    Context context;

    public VideoFilterWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
        this.context = context;
    }

    @NonNull
    @Override
    @SuppressWarnings("ConstantConditions")
    public ListenableFuture<Result> startWork() {
        File input = new File(getInputData().getString(KEY_INPUT));
        File output = new File(getInputData().getString(KEY_OUTPUT));
        return CallbackToFutureAdapter.getFuture(completer -> {
            doActualWork(input, output, completer);
            return null;
        });
    }

    private void doActualWork(File input, File output, CallbackToFutureAdapter.Completer<Result> completer) {
        Size size = VideoUtil.getDimensions(input.getAbsolutePath());
        int width = size.getWidth();
        int height = size.getHeight();
        if (width > Const.MAX_RESOLUTION || height > Const.MAX_RESOLUTION) {
            if (width > height) {
                height = Const.MAX_RESOLUTION * height / width;
                width = Const.MAX_RESOLUTION;
            } else {
                width = Const.MAX_RESOLUTION * width / height;
                height = Const.MAX_RESOLUTION;
            }
        }
        if (width % 2 != 0) {
            width += 1;
        }

        if (height % 2 != 0) {
            height += 1;
        }

        Log.v(TAG, "Original: " + width + "x" + height + "px; scaled: " + width + "x" + height + "px");
        Mp4Composer composer = new Mp4Composer(input.getAbsolutePath(), output.getAbsolutePath());
        composer.videoBitrate((int) (.07 * 30 * width * height));
        composer.size(width, height);

//        VideoFilter filter = VideoFilter.valueOf(getInputData().getString(KEY_FILTER));

        FilterType filter = FilterType.valueOf(getInputData().getString(KEY_FILTER));

        Log.d(TAG, "doActualWork: filter " + filter.name());
        switch (filter) {
            case DEFAULT:
                GlFilter gl = new GlFilter();
                composer.filter(gl);
                break;
            case BILATERAL_BLUR:
                Log.d(TAG, "doActualWork: blur");
                GlBilateralFilter glb = new GlBilateralFilter();
                composer.filter(glb);
                break;
            case BRIGHTNESS:
                GlBrightnessFilter glBrightnessFilter = new GlBrightnessFilter();
                glBrightnessFilter.setBrightness(0.2f);
                composer.filter(glBrightnessFilter);
                break;
            case CGA_COLORSPACE:
                composer.filter(new GlCGAColorspaceFilter());
                break;
            case CONTRAST:
                GlContrastFilter glContrastFilter = new GlContrastFilter();
                glContrastFilter.setContrast(2.5f);
                composer.filter(glContrastFilter);
                break;
            case CROSSHATCH:
                composer.filter(new GlCrosshatchFilter());
                break;
            case FILTER_GROUP_SAMPLE:
                composer.filter(new GlFilterGroup(new GlSepiaFilter(), new GlVignetteFilter()));
                break;
            case GAMMA:
                GlGammaFilter glGammaFilter = new GlGammaFilter();
                glGammaFilter.setGamma(2f);
                composer.filter(glGammaFilter);
                break;
            case GAUSSIAN_FILTER:
                composer.filter(new GlGaussianBlurFilter());
                break;
            case GRAY_SCALE:
                composer.filter(new GlGrayScaleFilter());
                break;
            case HALFTONE:
                composer.filter(new GlHalftoneFilter());
                break;
            case HAZE:
                GlHazeFilter glHazeFilter = new GlHazeFilter();
                glHazeFilter.setSlope(-0.5f);
                composer.filter(glHazeFilter);
                break;

            case HIGHLIGHT_SHADOW:
                composer.filter(new GlHighlightShadowFilter());
                break;
            case HUE:
                composer.filter(new GlHueFilter());
                break;
            case INVERT:
                composer.filter(new GlInvertFilter());
                break;
            case LOOK_UP_TABLE_SAMPLE:
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.lookup_sample);
                composer.filter(new GlLookUpTableFilter(bitmap));
                break;
            case LUMINANCE:
                composer.filter(new GlLuminanceFilter());
                break;
            case LUMINANCE_THRESHOLD:
                composer.filter(new GlLuminanceThresholdFilter());
                break;
            case MONOCHROME:
                composer.filter(new GlMonochromeFilter());
                break;
            case OPACITY:
                composer.filter(new GlOpacityFilter());
                break;
            case PIXELATION:
                composer.filter(new GlPixelationFilter());
                break;
            case POSTERIZE:
                composer.filter(new GlPosterizeFilter());
                break;
            case RGB:
                GlRGBFilter glRGBFilter = new GlRGBFilter();
                glRGBFilter.setRed(0f);
                composer.filter(glRGBFilter);
                break;
            case SATURATION:
                composer.filter(new GlSaturationFilter());
                break;
            case SEPIA:
                composer.filter(new GlSepiaFilter());
                break;
            case SHARP:
                GlSharpenFilter glSharpenFilter = new GlSharpenFilter();
                glSharpenFilter.setSharpness(4f);
                composer.filter(glSharpenFilter);
                break;
            case SOLARIZE:
                composer.filter(new GlSolarizeFilter());
                break;
            case TONE_CURVE_SAMPLE:
                try {
                    InputStream is = context.getAssets().open("acv/tone_cuver_sample.acv");
                    composer.filter(new GlToneCurveFilter(is));
                } catch (IOException e) {
                    Log.e("FilterType", "Error");
                }
                composer.filter(new GlFilter());
                break;
            case VIBRANCE:
                GlVibranceFilter glVibranceFilter = new GlVibranceFilter();
                glVibranceFilter.setVibrance(3f);
                composer.filter(glVibranceFilter);
                break;
            case VIGNETTE:
                composer.filter(new GlVignetteFilter());
                break;
            default:
                composer.filter(new GlFilter());
                break;
        }

//        switch (filter) {
//            case BRIGHTNESS: {
//                GlBrightnessFilter glf = new GlBrightnessFilter();
//                glf.setBrightness(0.2f);
//                composer.filter(glf);
//                break;
//            }
//            case EXPOSURE:
//                composer.filter(new GlExposureFilter());
//                break;
//            case GAMMA: {
//                GlGammaFilter glf = new GlGammaFilter();
//                glf.setGamma(2f);
//                composer.filter(glf);
//                break;
//            }
//            case GRAYSCALE:
//                composer.filter(new GlGrayScaleFilter());
//                break;
//            case HAZE: {
//                GlHazeFilter glf = new GlHazeFilter();
//                glf.setSlope(-0.5f);
//                composer.filter(glf);
//                break;
//            }
//            case INVERT:
//                composer.filter(new GlInvertFilter());
//                break;
//            case MONOCHROME:
//                composer.filter(new GlMonochromeFilter());
//                break;
//            case PIXELATED:
//                composer.filter(new GlPixelationFilter());
//                break;
//            case POSTERIZE:
//                composer.filter(new GlPosterizeFilter());
//                break;
//            case SEPIA:
//                composer.filter(new GlSepiaFilter());
//                break;
//            case SHARP: {
//                GlSharpenFilter glf = new GlSharpenFilter();
//                glf.setSharpness(1f);
//                composer.filter(glf);
//                break;
//            }
//            case SOLARIZE:
//                composer.filter(new GlSolarizeFilter());
//                break;
//            case VIGNETTE:
//                composer.filter(new GlVignetteFilter());
//                break;
//            default:
//                break;
//        }

        composer.listener(new Mp4Composer.Listener() {

            @Override
            public void onProgress(double progress) {
            }

            @Override
            public void onCompleted() {
                Log.d(TAG, "MP4 composition has finished.");
                completer.set(Result.success());
            }

            @Override
            public void onCanceled() {
                Log.d(TAG, "MP4 composition was cancelled.");
                completer.setCancelled();
                if (!output.delete()) {
                    Log.w(TAG, "Could not delete failed output file: " + output);
                }
            }

            @Override
            public void onFailed(Exception e) {
                Log.d(TAG, "MP4 composition failed with error.", e);
                completer.setException(e);
                if (!output.delete()) {
                    Log.w(TAG, "Could not delete failed output file: " + output);
                }
            }
        });
        composer.videoFormatMimeType(VideoFormatMimeType.AVC);
        composer.start();
    }
}
