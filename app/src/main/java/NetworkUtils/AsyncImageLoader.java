package NetworkUtils;

import android.graphics.drawable.Drawable;
import android.os.Handler;

import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jianyang on 10/13/14.
 */

public class AsyncImageLoader {

    public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();
    private ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final Handler handler = new Handler();

    public Drawable loadDrawable(final String imageUrl,
                                 final ImageCallback callback) {
        if (imageCache.containsKey(imageUrl)) {
            SoftReference<Drawable> softReference = imageCache.get(imageUrl);
            if (softReference.get() != null) {
                return softReference.get();
            }
        }

        executorService.submit(new Runnable() {

            public void run() {
                try {
                    final Drawable drawable = loadImageFromUrl(imageUrl);

                    imageCache.put(imageUrl, new SoftReference<Drawable>(
                            drawable));
                    System.out.println(Thread.currentThread().getId());
                    handler.post(new Runnable() {
                        public void run() {
                            callback.imageLoaded(drawable);
                            callback.returnImageLoaded(drawable);
                        }

                    });
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
        return null;
    }

    protected Drawable loadImageFromUrl(String imageUrl) {
        try {
            return Drawable.createFromStream(new URL(imageUrl).openStream(),
                    "image.png");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public interface ImageCallback {
        public void imageLoaded(Drawable imageDrawable);
        public Drawable returnImageLoaded(Drawable imageDrawable);

    }
}
