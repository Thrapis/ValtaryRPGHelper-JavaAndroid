package wa.mobile.rpghelper.localstore.entity;

import java.io.Serializable;

public class User implements Serializable {

    private int lastMapSelection;
    private int lastBookSelection;

    public int getLastMapSelection() {
        return lastMapSelection;
    }

    public int getLastBookSelection() {
        return lastBookSelection;
    }

    public void setLastMapSelection(int lastMapSelection) {
        this.lastMapSelection = lastMapSelection;
    }

    public void setLastBookSelection(int lastBookSelection) {
        this.lastBookSelection = lastBookSelection;
    }
}
