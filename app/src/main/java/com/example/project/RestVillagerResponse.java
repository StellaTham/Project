package com.example.project;

import java.util.List;

public class RestVillagerResponse {
    private Integer count;
    private String next;
    private List<Villager> results;

    public Integer getCount() {
        return count;
    }

    public String getNext() {
        return next;
    }

    public List<Villager> getResults() {
        return results;
    }
}
