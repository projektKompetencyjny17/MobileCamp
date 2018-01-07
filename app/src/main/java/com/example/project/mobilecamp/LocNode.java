package com.example.project.mobilecamp;

/**
 * Created by adino on 07.01.2018.
 */

import java.util.ArrayList;
import java.util.List;

public class LocNode {
    Integer idNode;
    List<LocNode> neighbors = new ArrayList<>();
    String description;
    boolean isVisited = false;
    public boolean isVisited() {
        return isVisited;
    }
    public boolean isContaining(LocNode ln) {
        for(LocNode l : neighbors) {
            if(l.idNode.equals(ln.idNode))
                return true;
        }
        return false;
    }
    public void setVisited(boolean isVisited) {
        this.isVisited = isVisited;
    }
    public LocNode(Integer idNode, String description) {
        super();
        this.idNode = idNode;
        this.description = description;
    }
    public void addNeighbor(LocNode neigh) {
        neighbors.add(neigh);
    }
    public Integer getIdNode() {
        return idNode;
    }
    public void setIdNode(Integer idNode) {
        this.idNode = idNode;
    }
    public List<LocNode> getNeighbors() {
        return neighbors;
    }
    public void setNeighbors(List<LocNode> neighbors) {
        this.neighbors = neighbors;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
