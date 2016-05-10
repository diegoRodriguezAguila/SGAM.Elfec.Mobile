package com.elfec.sgam.helpers.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

/**
 * Utils for rx observables
 */
public class ObservableUtils {

    /**
     * Creates an observable with the structure:<br/>
     * <code>
     *     if(!subs.isUnsubscribed()){<br/>
     *     &emsp;&emsp;try {<br/>
     *     &emsp;&emsp;&emsp;&emsp;T res = method.call();<br/>
     *     &emsp;&emsp;&emsp;&emsp;subs.onNext(res);<br/>
     *     &emsp;&emsp;}<br/>
     *     &emsp;&emsp;catch (Throwable e){<br/>
     *     &emsp;&emsp;&emsp;&emsp;subs.onError(e);<br/>
     *     &emsp;&emsp;}<br/>
     *     &emsp;&emsp;subs.onCompleted();<br/>
     *     }<br/>
     * </code>
     * @param method method to call
     * @param <T> return type of the method and observable
     * @return {@link Observable}&lt;T&gt;
     */
    public static <T> Observable<T> from(Func0<T> method){
        return Observable.create(subs -> {
            if(!subs.isUnsubscribed()){
                try {
                    T res = method.call();
                    subs.onNext(res);
                }
                catch (Throwable e){
                    subs.onError(e);
                }
                subs.onCompleted();
            }
        });
    }

    /**
     * Applies the schedulers to the observable.
     * By default it uses subscribe on io and observe on mainThread
     * @param <T> type
     * @return observable with applied schedulers
     */
    public static <T> Observable.Transformer<T, T> applySchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
