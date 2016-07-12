package com.elfec.sgam.local_storage;

import com.elfec.sgam.model.Rule;
import com.elfec.sgam.model.enums.PolicyType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.paperdb.Book;
import rx.Observable;

/**
 * Local storage data layer
 */
public class PolicyDataStorage {
    public static final String POLICY_BOOK = "policy.book";
    private RxPaper mBook;

    public PolicyDataStorage(){
        mBook = RxPaper.book(POLICY_BOOK);
    }

    /**
     * Saves the policy rules that applies to the user
     * @param username user's username
     * @param rules rules
     * @return observable with a list of rules
     */
    public Observable<List<Rule>> saveUserPolicyRules(String username, List<Rule> rules) {
        return Observable.create(subscriber -> {
            if (!subscriber.isUnsubscribed()) {
                try {
                    Map<PolicyType, List<Rule>> mapRules = new HashMap<>();
                    for (PolicyType type : PolicyType.values()) {
                        mapRules.put(type, new ArrayList<>());
                    }
                    for (Rule rule : rules) {
                        mapRules.get(rule.getPolicyId()).add(rule);
                    }
                    Book book = mBook.getBook();
                    for (Map.Entry<PolicyType, List<Rule>> entry: mapRules.entrySet()) {
                        book.write(getPolicyKey(username, entry.getKey()), entry.getValue());
                    }
                    subscriber.onNext(rules);
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        });
    }

    /**
     * Saves the policy rules of the specific type that applies to the user.
     * All non matching policy types are not going to be saved
     * @param username user's username
     * @param policyType policy type
     * @param rules rules
     * @return observable with a list of rules
     */
    public Observable<List<Rule>> saveUserPolicyRules(String username, PolicyType policyType,
                                                      List<Rule> rules) {
        return mBook.write(getPolicyKey(username, policyType), rules);
    }

    /**
     * Retrieves the policy rules of the specific type that applies to the user
     * @param username user's username
     * @param policyType policy type
     * @return observable with a list of rules
     */
    public Observable<List<Rule>> getUserPolicyRules(String username, PolicyType policyType) {
        return mBook.read(getPolicyKey(username, policyType));
    }

    /**
     * Adds the policy rules of the specific type that applies to the user.
     * @param username user's username
     * @param policyType policy type
     * @param newRules rules to add
     * @return observable with a list of rules
     */
    public Observable<List<Rule>> addUserPolicyRules(String username, PolicyType policyType,
                                                      Rule... newRules) {
        final String policyKey = getPolicyKey(username, policyType);
        Observable<List<Rule>> currentRules = mBook.read(policyKey);
        if(newRules.length==0)
            return currentRules;
        return currentRules.map(rules -> {
            if(rules==null)
                rules = new ArrayList<>();
            Collections.addAll(rules, newRules);
            return rules;
        }).flatMap(rules -> mBook.write(policyKey, rules));
    }

    /**
     * Deletes the policy rules of the specific type that applies to the user.
     * @param username user's username
     * @param policyType policy type
     * @param deleteRules rules to delete
     * @return observable with a list of rules
     */
    public Observable<List<Rule>> deleteUserPolicyRules(String username, PolicyType policyType,
                                                     Rule... deleteRules) {
        final String policyKey = getPolicyKey(username, policyType);
        Observable<List<Rule>> currentRules = mBook.read(policyKey);
        if(deleteRules.length==0)
            return currentRules;
        return currentRules.map(rules -> {
            if(rules==null)
                rules = new ArrayList<>();
            else rules.removeAll(Arrays.asList(deleteRules));
            return rules;
        }).flatMap(rules -> mBook.write(policyKey, rules));
    }

    /**
     * Generates the policy key for the book
     * @param username username
     * @param policyType policy type
     * @return string key for the policy
     */
    private String getPolicyKey(String username, PolicyType policyType) {
        if(username==null)
            return null;
        return username
                .concat(policyType.toString());
    }
}
