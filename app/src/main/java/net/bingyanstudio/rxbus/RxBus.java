package net.bingyanstudio.rxbus;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Geeksammao on 9/16/16.
 */

public class RxBus {
    private static volatile RxBus mInstance;
    private Subject<Object, Object> subject = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getDefault() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    private RxBus() {

    }

    public void post(Object object) {
        if (object == null) {
            throw new NullPointerException("Rxbus cannot post null");
        }
        if (subject.hasObservers()) {
            subject.onNext(object);
        }
    }

    public <T extends Object> Observable<T> toObservable(final Class<T> event) {
        return subject.filter(new Func1<Object, Boolean>() {
            @Override
            public Boolean call(Object o) {
                return event.isInstance(o);
            }
        }).cast(event);
    }
}
