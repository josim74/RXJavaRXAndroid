package josim74.github.com.rxjavarxandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    public final String TAG = this.getClass().getSimpleName();
    private Disposable disposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Observable<String> animalObservable = getObservable();

        Observer<String> animalObserver = getAnimalsObserver();

        animalObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
          //1      .subscribe(animalObserver);
                .filter(new Predicate<String>() {
                    @Override
                    public boolean test(String s) throws Exception {
                        return s.toLowerCase().startsWith("b");
                    }
                }).subscribeWith(animalObserver);

    }

    private Observer<String> getAnimalsObserver() {
        return new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                Log.d(TAG, "onSubscribe: Just Subscribed");
                disposable = d;
            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, "Name: "+s);
            }

            @Override
            public void onError(Throwable e) {
                Log.d(TAG, "onError: "+e.getMessage());
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete: All items are emitted");
            }
        };
    }

    private Observable<String> getObservable(){
        return Observable.just("Ant", "Bee", "Cat", "Dog", "Fox");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.dispose();
    }
}
