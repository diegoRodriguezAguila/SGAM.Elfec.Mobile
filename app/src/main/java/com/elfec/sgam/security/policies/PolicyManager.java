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
    public static Observable<List<Rule>> getCurrentUserPolicyRules(PolicyType policyType) {
        return new PolicyDataStorage()
                .getUserPolicyRules(SessionManager.instance()
                        .getLoggedInUsername(), policyType);
    }
}
