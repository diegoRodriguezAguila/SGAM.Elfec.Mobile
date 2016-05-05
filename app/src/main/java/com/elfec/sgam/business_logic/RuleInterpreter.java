package com.elfec.sgam.business_logic;

import com.elfec.sgam.model.AppDetail;
import com.elfec.sgam.model.Rule;
import com.elfec.sgam.model.enums.PolicyType;

import java.util.ArrayList;
import java.util.List;

/**
 * Interprets rules
 */
public class RuleInterpreter {

    /**
     * Filtra las aplicaciones que cumplen todas las reglas.
     * No afecta a la lista original provista.
     *
     * @param appDetails lista de aplicaciones
     * @param rules lista de reglas de directivas de tipo {@link PolicyType#APPLICATION_CONTROL}
     * @return lista filtrada de apps que cumplen las reglas
     */
    public static List<AppDetail> filterApps(List<AppDetail> appDetails, List<Rule> rules) {
        List<AppDetail> apps = new ArrayList<>();
        for (AppDetail app : appDetails) {
            if (isPermitted(app.getPackageName(), rules, PolicyType.APPLICATION_CONTROL))
                apps.add(app);
        }
        return apps;
    }

    /**
     * Verifica si un valor cumple con al menos una de las reglas
     *
     * @param value valor
     * @param rules reglas
     * @param policyType tipo de directiva, si la regla no es del tipo de directiva se ignora
     * @return true si el valor cumple con al menos una de las reglas, false en caso contrario
     */
    public static boolean isPermitted(String value, List<Rule> rules, PolicyType policyType) {
        boolean permitted = true;
        for (Rule rule : rules) {
            if(rule.getPolicyId()==policyType) {
                permitted = rule.isPermitted(value);
                if(permitted)
                    break;
            }
        }
        return permitted;
    }
}
