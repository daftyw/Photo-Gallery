package ayp.aug.photogallery;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.support.v4.app.Fragment;

/**
 * Created by Rawin on 05-Sep-16.
 */
public class PhotoMapActivity extends SingleFragmentActivity {

    private static final String KEY_LOCATION = "GA1";
    private static final String KEY_GALLERY_ITEM = "GA2";
    private static final String KEY_BITMAP = "GA3";

    protected static Intent newIntent(Context c, Location location, Location galleryItemLoc, String url) {
        Intent i = new Intent(c, PhotoMapActivity.class);
        i.putExtra(KEY_LOCATION, location);
        i.putExtra(KEY_GALLERY_ITEM, galleryItemLoc);
        i.putExtra(KEY_BITMAP, url);

        return i;
    }

    @Override
    protected Fragment onCreateFragment() {
        if(getIntent() != null) {
            Location galleryLoc = getIntent().getParcelableExtra(KEY_GALLERY_ITEM);
            Location location = getIntent().getParcelableExtra(KEY_LOCATION);
            String url = getIntent().getStringExtra(KEY_BITMAP);

            return PhotoMapFragment.newInstance(location, galleryLoc, url);
        }

        return PhotoMapFragment.newInstance();
    }
}
