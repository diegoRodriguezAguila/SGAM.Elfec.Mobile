package com.elfec.sgam.local_storage;

import com.elfec.sgam.model.Rule;
import com.elfec.sgam.model.enums.PolicyType;

import java.util.ArrayList;
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
                    for (Rule rule : rules) {
                        if (!mapRules.containsKey(rule.getPolicyId()))
                            mapRules.put(rule.getPolicyId(), new ArrayList<>());
                        mapRules.get(rule.getPolicyId()).add(rule);
                    }
                    Book book = mBook.getBook();
                    for (Map.Entry<PolicyType, List<Rule>> entry: mapRules.entrySet()) {
                        book.write(username
                                .concat(entry.getKey().toString()), entry.getValue());
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
        return mBook.write(username
                .concat(policyType.toString()), rules);
    }

    /**
     * Retrieves the policy rules of the specific type that applies to the user
     * @param username user's username
     * @param policyType policy type
     * @return observable with a list of rules
     */
    public Observable<List<Rule>> getUserPolicyRules(String username, PolicyType policyType) {
        return mBook.read(username
                .concat(policyType.toString()));
    }



    private Observable<List<Rule>> saveUserPolicyRules(String username, Map.Entry<PolicyType,
            List<Rule>> entry) {
        return saveUserPolicyRules(username, entry.getKey() , entry.getValue());
    }

}
