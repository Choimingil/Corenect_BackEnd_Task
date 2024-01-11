package com.corenect.task.algorithms;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public final class UnionFind {
    private final Map<Long, Long> parent; // 각 역의 바로 이전 레벨의 노드를 저장
    private final Map<Long, Long> rank;   // 각 역의 rank를 저장
    private final Map<String, Long> lineMapping; // 각 호선을 문자로 매핑

    public UnionFind() {
        parent = new HashMap<>();
        rank = new HashMap<>();
        lineMapping = new HashMap<>();
    }

    // 새로운 역을 추가
    public void addNode(long node) {
        if (!parent.containsKey(node)) {
            parent.put(node, node);
            rank.put(node, 0L);
        }
    }

    // 두 역을 같은 노선으로 연결
    public void union(long x, long y, String line) {
        long rootX = find(x);
        long rootY = find(y);

        // 두 노선이 이미 연결되어 있지 않다면 연결
        if (rootX != rootY) {
            if (rank.get(rootX) > rank.get(rootY)) {
                parent.put(rootY, rootX);
            } else if (rank.get(rootX) < rank.get(rootY)) {
                parent.put(rootX, rootY);
            } else {
                parent.put(rootY, rootX);
                rank.put(rootX, rank.get(rootX) + 1);
            }
        }

        // 호선을 문자로 매핑
        lineMapping.put(line, rootX);
    }

    // 해당 역이 속한 노선의 바로 이전 레벨의 대표 역을 찾음
    public long find(long x) {
        long currentNode = x;
        while (parent.get(currentNode) != currentNode) {
            currentNode = parent.get(currentNode);
        }
        return currentNode;
    }

    // 특정 호선의 특정 역의 대표 역을 찾음
    public long findRepresentative(String line, long x) {
        if (lineMapping.containsKey(line)) {
            long rootLine = lineMapping.get(line);
            long rootX = find(x);

            if (rootLine == rootX) {
                return rootX;
            }
        }
        return -1; // 호선 정보나 역 정보가 잘못된 경우 -1 반환
    }
}
