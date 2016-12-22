package de.gurkengewuerz.twitchbotr2.object;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by gurkengewuerz.de on 22.12.2016.
 */
public class ViewerList<Viewer> {
    private ArrayList<Viewer> elementData;
    private int size = 0;

    public ViewerList() {
        elementData = new ArrayList<>();
    }

    public void add(Viewer viewer) {
        elementData.add(viewer);
    }

    public Viewer get(int index) {
        return elementData.get(index);
    }

    public Viewer get(String name) {
        for(Viewer v : elementData){
            // TODO: If viewer equals
        }
        return null;
    }

    public Viewer remove(int index) {
        return elementData.remove(index);
    }

    public void remove(Viewer viewer) {
        elementData.remove(viewer);
    }

    public void clear(){
        elementData.clear();
    }

    public List<Viewer> toList(){
        return elementData;
    }
}
