package com.cmcewen.blurview;

import android.view.View;

import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.NativeViewHierarchyManager;
import com.facebook.react.uimanager.UIBlock;
import com.facebook.react.uimanager.UIManagerModule;

import javax.annotation.Nonnull;


@SuppressWarnings("unused")
class BlurViewManager extends SimpleViewManager<BlurringView> {
    private static final String REACT_CLASS = "BlurView";

    private static final int defaultRadius = 10;
    private static final int defaultSampling = 10;

    private static ThemedReactContext context;

    @Override
    public @Nonnull String getName() {
        return REACT_CLASS;
    }

    @Override
    public @Nonnull BlurringView createViewInstance(@Nonnull ThemedReactContext ctx) {
        context = ctx;

        BlurringView blurringView = new BlurringView(ctx);
        blurringView.setBlurRadius(defaultRadius);
        blurringView.setDownsampleFactor(defaultSampling);
        return blurringView;
    }

    @ReactProp(name = "blurRadius", defaultInt = defaultRadius)
    public void setRadius(BlurringView view, int radius) {
        view.setBlurRadius(radius);
        view.invalidate();
    }

    @ReactProp(name = "overlayColor", customType = "Color")
    public void setColor(BlurringView view, int color) {
        view.setOverlayColor(color);
        view.invalidate();
    }

    @ReactProp(name = "downsampleFactor", defaultInt = defaultSampling)
    public void setDownsampleFactor(BlurringView view, int factor) {
        view.setDownsampleFactor(factor);
    }

    @ReactProp(name = "viewRef")
    public void setViewRef(final BlurringView view, final int viewRef) {
        UIManagerModule uiManager = context.getNativeModule(UIManagerModule.class);
        uiManager.prependUIBlock(new UIBlock() {
            @Override
            public void execute(NativeViewHierarchyManager nativeViewHierarchyManager) {
                try {
                    View viewToBlur = nativeViewHierarchyManager.resolveView(viewRef);
                    view.setBlurredView(viewToBlur);
                } 
                catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
            }
        });
    }
}
