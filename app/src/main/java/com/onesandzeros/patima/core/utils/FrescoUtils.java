package com.onesandzeros.patima.core.utils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipeline;

public class FrescoUtils {
    public static void clearCaches() {
        ImagePipeline imagePipeline = Fresco.getImagePipeline();

        // Clear all memory caches
        imagePipeline.clearMemoryCaches();

        // Clear all disk caches
        imagePipeline.clearDiskCaches();

        // Clear all caches (memory + disk)
        imagePipeline.clearCaches();
    }
}
