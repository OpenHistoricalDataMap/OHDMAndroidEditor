package android.ohdm.de.editor.Geometry.PolyObject;

import android.content.Context;
import android.ohdm.de.editor.Geometry.PolyObject.ExtendedOverlay.ExtendedOverlayClickListener;
import android.ohdm.de.editor.Geometry.TagDates;

import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class PolyObject implements ExtendedOverlayClickListener, PolyObjectClickPublisher, Serializable {

    PolyObjectType type = null;
    UUID internId;

    public PolyObject(PolyObjectType type){
        this.type = type;
    }

    public PolyObjectType getType(){
        return this.type;
    }

    protected abstract void create(Context context);

    public abstract OverlayWithIW getOverlay();

    public abstract void setPoints(List<GeoPoint> points);

    public abstract List<GeoPoint> getPoints();

    public abstract void removeLastPoint();

    public abstract void addPoint(GeoPoint geoPoint);

    public abstract void setClickable(boolean clickable);

    public abstract void setSelected(boolean selected);

    public abstract boolean isSelected();

    public abstract boolean isEditing();

    public abstract void setEditing(boolean editing);

    public abstract void removeSelectedCornerPoint();

    public abstract HashMap<TagDates,String> getTags();

    public abstract void setTags(HashMap<TagDates,String> tags);

    public void setId(UUID id){
        this.internId = id;
    }

    public UUID getId(){
        return this.internId;
    }
}
