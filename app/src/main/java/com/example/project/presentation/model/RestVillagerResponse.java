package com.example.project.presentation.model;

import com.example.project.presentation.model.Villager;

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
