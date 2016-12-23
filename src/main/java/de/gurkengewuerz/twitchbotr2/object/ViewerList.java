package de.gurkengewuerz.twitchbotr2.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by gurkengewuerz.de on 22.12.2016.
 */
public class ViewerList {

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
        Optional<Viewer> first = elementData.stream().filter(viewer -> viewer.getName().equalsIgnoreCase(name)).findFirst();
        return first.orElse(null);
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
