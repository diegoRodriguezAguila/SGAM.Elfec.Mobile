package com.elfec.sgam.business_logic;

import com.elfec.sgam.local_storage.PolicyDataStorage;
import com.elfec.sgam.model.Rule;
import com.elfec.sgam.model.enums.PolicyType;
import com.elfec.sgam.security.SessionManager;

import java.util.List;

import rx.Observable;

/**
 * Se encarga de la logica de negocio de directivas de usuario (políticas)
 */
public class PolicyManager {
    public static Observable<List<Rule>> getCurrentUserPolicyRules(PolicyType policyType){
        return new PolicyDataStorage()
                .getUserPolicyRules(SessionManager.instance()
                        .getLoggedInUsername(), policyType);
    }
}
