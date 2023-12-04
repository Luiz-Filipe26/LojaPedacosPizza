package com.mycompany.lojapedacospizza.objetos;

import java.util.Objects;

public class Area {
    public int x1;
    public int y1;
    public int x2;
    public int y2;
    
    public Area() {
        x1 = 0;
        y1 = 0;
        x2 = 0;
        y2 = 0;
    }
    
    public Area(int x, int y, int x2, int y2) {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x2;
        this.y2 = y2;
    }
    
    @Override
    public String toString() {
        return "(" + x1 + ", " + y1 + ", " + x2 + ", " + y2 + ")";
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Area area = (Area) obj;
        return area.x1 == x1 && area.y1 == y1 && area.x2 == x2 && area.y2 == y2;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(x1, y1, x2, y2);
    }
}
