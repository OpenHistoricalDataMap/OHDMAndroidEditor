package android.ohdm.de.editor.geometry.PolyObject;

import android.content.Context;
import android.ohdm.de.editor.geometry.PolyObject.ExtendedOverlay.ExtendedOverlayClickListener;
import android.ohdm.de.editor.geometry.PolyObject.JSONUtilities.JSONWriter;

import org.json.JSONObject;
import org.osmdroid.bonuspack.overlays.OverlayWithIW;
import org.osmdroid.util.GeoPoint;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public abstract class PolyObject implements ExtendedOverlayClickListener, PolyObjectClickPublisher, Serializable {

    protected transient boolean selected = false;
    protected transient boolean editing = false;
    protected transient List<PolyObjectClickListener> listeners = new ArrayList<PolyObjectClickListener>();

    protected HashMap<String,String> tags = new HashMap<String, String>();
    protected HashMap<String,String> attributes = new HashMap<String, String>();
    protected PolyObjectType type = null;
    protected UUID internId;
    //TODO: so in der art...: protected String icon;

    public PolyObject(PolyObjectType type){
        this.type = type;
    }

    public PolyObjectType getType(){
        return this.type;
    }

    protected abstract void create(Context context);

    public abstract OverlayWithIW getOverlay(); // todo nachschauen

    public abstract void setPoints(List<GeoPoint> points);

    public abstract List<GeoPoint> getPoints();

    public abstract void removeLastPoint();

    public abstract void addPoint(GeoPoint geoPoint);

    public abstract void setClickable(boolean clickable);

    public abstract void setSelected(boolean selected);

    public abstract void setEditing(boolean editing);

    public abstract boolean removeSelectedEditPoint();

    public HashMap<String, String> getTags() {
        return this.tags;
    }
    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    /**
     * Setter für attributes.
     *
     * @param attributes HashMap<String, String>
     */
    public void setAttributes(HashMap<String, String> attributes) {
        this.attributes = attributes;
    }

    /**
     * Getter für attributes.
     *
     * @return HashMap<String, String>
     */
    public HashMap<String, String> getAttributes() {
        return this.attributes;
    }

    public void setId(UUID id){
        this.internId = id;
    }

    public UUID getId(){
        return this.internId;
    }

    public void subscribe(PolyObjectClickListener listener) {
        listeners.add(listener);
    }

    public void remove(PolyObjectClickListener listener) {
        listeners.remove(listener);
    }

    public boolean isSelected() {
        return selected;
    }

    public boolean isEditing() {
        return editing;
    }

    public JSONObject getAsJSONObject() {
        return JSONWriter.createJSONObjectFromPolyObject(this);
    }

}
