package NetworkUtils;

import android.graphics.drawable.Drawable;
import android.widget.ImageView;

/**
 * Created by jianyang on 10/13/14.
 */

public class CallbackImpl implements AsyncImageLoader.ImageCallback{
    private ImageView imageView ;

    public CallbackImpl(ImageView imageView) {
        super();
        this.imageView = imageView;
    }

    @Override
    public void imageLoaded(Drawable imageDrawable) {
        imageView.setImageDrawable(imageDrawable);
    }

    @Override
    public Drawable returnImageLoaded(Drawable imageDrawable) {
        return null;
    }

}
