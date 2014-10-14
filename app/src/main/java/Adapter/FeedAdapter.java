package Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jianyang.kamcordvideofeed.MainActivity;
import com.example.jianyang.kamcordvideofeed.R;

import java.util.ArrayList;
import java.util.HashMap;

import NetworkUtils.AsyncImageLoader;
import NetworkUtils.CallbackImpl;

/**
 * Created by jianyang on 10/6/14.
 */

//Build an adapter to populate our listview
public class FeedAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<HashMap<String, String>> mDataList;
    private AsyncImageLoader loader = new AsyncImageLoader();

    public FeedAdapter(Context context, ArrayList<HashMap<String, String>> dataList) {
        mContext = context;
        mDataList = dataList;

    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    //Use ViewHolder pattern to enable smooth scrolling
    static class ViewHolder {
        TextView videoTitleItem;
        TextView videoUrlItem;
        ImageView thumbnailItem;
    }

    public View getView(int position, View convertView,ViewGroup parent) {
        ViewHolder viewHolder;
        View row=convertView;
        if (row==null) {
            LayoutInflater inflater=((Activity) mContext).getLayoutInflater();
            row=inflater.inflate(R.layout.list_item, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.videoTitleItem = (TextView) row.findViewById(R.id.title);
            viewHolder.videoUrlItem = (TextView) row.findViewById(R.id.video_url);
            viewHolder.thumbnailItem = (ImageView) row.findViewById(R.id.thumbnail);
            row.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) row.getTag();
        }

        HashMap<String, String> dataItem = mDataList.get(position);
        if(dataItem != null) {
            viewHolder.videoUrlItem.setText(dataItem.get(MainActivity.TAG_VIDEO_URL));
            viewHolder.videoTitleItem.setText(dataItem.get(MainActivity.TAG_TITLE));
            loadImage(dataItem.get(MainActivity.TAG_THUMBNAIL_REGULAR), viewHolder.thumbnailItem);
            // Load image, decode it to Bitmap and return Bitmap synchronously
//            viewHolder.thumbnailItem.setImageUrl(dataItem.get(MainActivity.TAG_THUMBNAIL_REGULAR), mImageLoader);
//            Bitmap bitmap = ImageLoader.getInstance().loadImageSync(dataItem.get(MainActivity.TAG_THUMBNAIL_REGULAR));
//            viewHolder.thumbnailItem.setImageBitmap(bitmap);
//            new DownloadImageAsyncTask(viewHolder.thumbnailItem).execute(dataItem.get(MainActivity.TAG_THUMBNAIL_REGULAR));

        }
        return row;
    }

    //Use AysncTask to download image according to its url
//    private class DownloadImageAsyncTask extends AsyncTask<String, Void, Bitmap> {
//        private ImageView imageView;
//        public DownloadImageAsyncTask(ImageView imageView) {
//            super();
//            this.imageView = imageView;
//        }
//
//        @Override
//        protected Bitmap doInBackground(String... params) {
//            // TODO Auto-generated method stub
//
//            Bitmap bitmap;
//            try {
//                URL imageUrl = new URL(params[0]);
//                bitmap = BitmapFactory.decodeStream(imageUrl.openStream());
//            } catch (IOException e) {
//                // TODO: handle exception
//                Log.e("error", "Downloading Image Failed");
//                bitmap = null;
//            }
//
//            return bitmap;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            // TODO Auto-generated method stub
//            if (bitmap == null) {
//                imageView.setImageResource(R.drawable.ic_launcher);
//            } else {
//                imageView.setImageBitmap(bitmap);
//            }
//        }
//    }

    //private helper method
    private void loadImage(final String url, ImageView imageView) {
        CallbackImpl callbackImpl = new CallbackImpl(imageView);
        Drawable cacheImage =
                loader.loadDrawable(url, callbackImpl);
        if (cacheImage != null) {
            imageView.setImageDrawable(cacheImage);
        }
    }
}