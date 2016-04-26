package com.elfec.sgam.model;

import com.elfec.sgam.model.enums.ApiStatus;
import com.elfec.sgam.model.enums.PolicyType;

import java.util.List;

/**
 * Model for policies
 */
public class Policy {
    private PolicyType type;
    private String name;
    private String description;
    private List<Rule> rules;
    private ApiStatus status;

    //region Getters Setters

    public PolicyType getType() {
        return type;
    }

    public void setType(PolicyType type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }

    //endregion
}
