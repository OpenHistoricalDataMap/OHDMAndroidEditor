package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.ohdm.de.editor.Geometry.ExtendedOverlay.ExtendedOverlayClickListener;
import android.ohdm.de.editor.Geometry.TagDates;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public abstract class PolyObject implements ExtendedOverlayClickListener, PolyObjectClickPublisher, Serializable {

    PolyObjectType type = null;

    public PolyObject(PolyObjectType type){
        this.type = type;
    }

    public PolyObjectType getType(){
        return this.type;
    }

    public void setType(PolyObjectType type){
        this.type = type;
    }

    protected abstract void create(Context context);

    public abstract OverlayWithIW getOverlay();

    public abstract void setPoints(List<GeoPoint> points);

    public abstract List<GeoPoint> getPoints();

    public abstract void removeLastPoint();

    public abstract void addPoint(GeoPoint geoPoint);

    public abstract boolean isClickable();

    public abstract void setClickable(boolean clickable);

    public abstract void setSelected(boolean selected);

    public abstract boolean isSelected();

    public abstract boolean isEditing();

    public abstract void setEditing(boolean editing);

    public abstract void removeSelectedCornerPoint();

    public abstract void setTag(TagDates tag,String value);

    public abstract Map<TagDates,String> getTags();
}
