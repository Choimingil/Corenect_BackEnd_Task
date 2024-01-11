package com.corenect.task.models;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public final class Edge {
    private String nodeId; // 노드의 아이디
    private double weight;

    Edge(){}
    public Edge(String nodeId, double weight) {
        this.nodeId = nodeId;
        this.weight = weight;
    }
}
