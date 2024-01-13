package com.corenect.task.models;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Edge {
    private long nodeId; // 노드의 아이디
    private double weight;

    Edge(){}
    public Edge(long nodeId, double weight) {
        this.nodeId = nodeId;
        this.weight = weight;
    }
}
