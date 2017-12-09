package com.hieuapp.lunch.map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.google.maps.android.ui.IconGenerator;
import com.hieuapp.lunch.R;
import com.hieuapp.lunch.util.ImageUtils;

/**
 * Created by hieuapp on 30/04/2017.
 */

public class MapUtils {

    public static MarkerOptions createPinMarker(Context context, String id, LatLng position) {
        Bitmap bitmap = ImageUtils.getBitmap(context, R.drawable.map_marker_unselected);
        final BitmapDescriptor icon =
                BitmapDescriptorFactory.fromBitmap(bitmap);
        return new MarkerOptions()
                .position(position)
                .title(id)
                .icon(icon)
                .anchor(0.5f, 0.85526f);
    }

    public static MarkerOptions createPinMarker(Context context, String title,
                                                String snippet, LatLng position) {
        Bitmap bitmap = ImageUtils.getBitmap(context, R.drawable.map_marker_selected);
        final BitmapDescriptor icon =
                BitmapDescriptorFactory.fromBitmap(bitmap);
        return new MarkerOptions()
                .position(position)
                .title(title)
                .snippet(snippet)
                .icon(icon)
                .anchor(0.5f, 0.85526f);
    }

    /**
     * Creates a marker for a label.
     *
     * @param iconFactory Reusable IconFactory
     * @param id          Id to be embedded as the title
     * @param label       Text to be shown on the label
     */
    public static MarkerOptions createLabelMarker(IconGenerator iconFactory, String id,
                                                  LatLng position, String label) {
        final BitmapDescriptor icon =
                BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(label));

        return new MarkerOptions().position(position).title(id).icon(icon)
                .anchor(0.5f, 0.5f)
                .visible(false);
    }

    /**
     * Creates a new IconGenerator for labels on the map.
     */
    public static IconGenerator getLabelIconGenerator(Context c) {
        IconGenerator iconFactory = new IconGenerator(c);
        iconFactory.setTextAppearance(R.style.MapLabel);
        iconFactory.setBackground(null);

        return iconFactory;
    }
}
