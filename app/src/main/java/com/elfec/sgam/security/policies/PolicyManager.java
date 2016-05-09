package com.elfec.sgam.security.policies;


import com.elfec.sgam.local_storage.PolicyDataStorage;
import com.elfec.sgam.model.Rule;
import com.elfec.sgam.model.enums.PolicyType;
import com.elfec.sgam.security.SessionManager;

import java.util.List;

import rx.Observable;

/**
 * Manages the policies of the application,
 */
public class PolicyManager {
    /**
     * Obtiene las reglas del tipo de politica especificado
     * que aplican al usuario logeado actual. Obtiene las reglas guardadas en la BD.
     * @param policyType tipo de política
     * @return observable de lista de reglas
     */
    public static Observable<List<Rule>> getCurrentUserPolicyRules(PolicyType policyType) {
        return new PolicyDataStorage()
                .getUserPolicyRules(SessionManager.instance()
                        .getLoggedInUsername(), policyType);
    }
}
