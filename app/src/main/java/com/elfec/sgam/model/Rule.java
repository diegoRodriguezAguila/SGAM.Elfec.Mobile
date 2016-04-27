package com.elfec.sgam.model;

import com.elfec.sgam.model.enums.ApiStatus;
import com.elfec.sgam.model.enums.PolicyType;
import com.elfec.sgam.model.enums.RuleAction;

import java.util.List;

/**
 * Describes a policy rule
 */
public class Rule {
    private String id;
    private PolicyType policyId;
    private RuleAction action;
    private String name;
    private String description;
    private String value;
    private String exception;
    private List<Entity> entities;
    private ApiStatus status;

    //region Getter Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public PolicyType getPolicyId() {
        return policyId;
    }

    public void setPolicyId(PolicyType policyId) {
        this.policyId = policyId;
    }

    public RuleAction getAction() {
        return action;
    }

    public void setAction(RuleAction action) {
        this.action = action;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public void setEntities(List<Entity> entities) {
        this.entities = entities;
    }

    public ApiStatus getStatus() {
        return status;
    }

    public void setStatus(ApiStatus status) {
        this.status = status;
    }
    //endregion
}
