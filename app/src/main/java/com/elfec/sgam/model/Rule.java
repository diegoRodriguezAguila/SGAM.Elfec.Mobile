package com.elfec.sgam.model;

import android.support.annotation.NonNull;

import com.elfec.sgam.model.enums.ApiStatus;
import com.elfec.sgam.model.enums.PolicyType;
import com.elfec.sgam.model.enums.RuleAction;

import java.util.List;
import java.util.regex.Pattern;

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

    /**
     * Gets the value pattern
     *
     * @return pattern of the value of the rule
     */
    public Pattern getValuePattern() {
        if (value == null)
            return Pattern.compile("/.*/ig");
        return stringToPattern(value);
    }

    /**
     * Gets the exception pattern
     *
     * @return pattern of the exception of the rule
     */
    public Pattern getExceptionPattern() {
        if (exception == null)
            return Pattern.compile("(?!.*)");
        return stringToPattern(exception);
    }

    private Pattern stringToPattern(@NonNull String value) {
        String pattern = value.replace(",", "|").replace(";", "|")
                .replace(".", "\\.").replace("*", ".*").replace("%", ".*")
                .replace("/[-[\\]{}()+?\\^$|#\\s]/g", "\\$&");
        return Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
    }

    /**
     * Analiza si es que el valor esta permitido segun el tipo y
     * las condiciones de la regla,
     * depende de las variables {@link #action},
     * {@link #value} y {@link #exception}
     *
     * @param value valor a verificar
     * @return true si el valor esta permitido por la regla
     */
    public boolean isPermitted(String value) {
        if (value == null)
            value = "";
        boolean negate = action == RuleAction.DENY;
        boolean valMatch = getValuePattern().matcher(value).matches();
        boolean excMatch = !getExceptionPattern().matcher(value).matches();
        return negate ^ (valMatch && excMatch);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this)
            return true;
        if (other instanceof Rule) {
            Rule otherRule = (Rule) other;
            if (id != null && otherRule.id != null)
                return id.equals(otherRule.id);
            if (name != null && otherRule.name != null)
                return name.equals(otherRule.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (id != null)
            return id.hashCode();
        if (name != null)
            return name.hashCode();
        return super.hashCode();
    }

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
