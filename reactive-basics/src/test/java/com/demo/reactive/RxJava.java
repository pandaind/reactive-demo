package com.demo.reactive;

import io.reactivex.*;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.AsyncSubject;
import io.reactivex.subjects.BehaviorSubject;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subscribers.DisposableSubscriber;
import lombok.SneakyThrows;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RxJava {

	String result = "";

	Integer a = 1;
	Integer b = 10;




	/*Observables*/


	@Ignore
	@Test
	public void single() {
		Single<String> single = Single.just("Hello");

		Disposable disposable = single.subscribe(s -> result = s, Throwable::printStackTrace);

		// single.delay(2, TimeUnit.SECONDS, Schedulers.io()).subscribe(s -> result =
		// s);
		// Thread.sleep(3000);

		disposable.dispose();

		assertEquals("Hello", result);
	}

	@Ignore
	@Test
	public void singleWithDisposable() throws InterruptedException {
		Single<String> single = Single.just("Hello");

		Disposable disposable = single.delay(2, TimeUnit.SECONDS, Schedulers.io())
				.subscribeWith(new DisposableSingleObserver<String>() {
					@Override
					public void onError(Throwable e) {
						e.printStackTrace();
					}

					@Override
					public void onSuccess(String s) {
						result = s;
					}
				});

		Thread.sleep(3000);

		disposable.dispose();

		assertEquals("Hello", result);
	}

	@Ignore
	@Test
	public void maybe() {

		Maybe<String> maybe = Maybe.empty();
		maybe.subscribe(s -> result = s, Throwable::printStackTrace, () -> System.out.println("No Item")).dispose();

		Maybe.just("Hello").subscribeWith(new DisposableMaybeObserver<String>() {
			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete() {
				System.out.println("Completable subscriber Completed");
				dispose();
			}

			@Override
			public void onSuccess(String s) {
				result = s;
			}
		});

		assertEquals("Hello", result);
	}

	@Ignore
	@Test
	public void completable() {
		
		/*Completable.complete().subscribe(() -> {
				result = "Hello";
				System.out.println("Completable  Completed");
			},
			Throwable::printStackTrace)
		.dispose();*/
		
		Disposable disposable = Completable.complete().subscribeWith(new DisposableCompletableObserver() {

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete() {
				result = "Hello";
				System.out.println("Completable subscriber Completed");
				dispose();
			}
		});

		disposable.dispose();

		assertEquals("Hello", result);
	}


	@Ignore
	@Test
	public void observable() throws InterruptedException {
		
		Observable<String> observable = Observable.just("a", "b", "c", "d", "e", "f").subscribeOn(Schedulers.computation());
		
		/*Disposable disposable = observable.subscribe(s->result+=s, Throwable::printStackTrace, () -> 
				System.out.println("Observable Observer Completed"));
		disposable.dispose();*/
		
		
		observable.subscribeWith(new DisposableObserver<String>() {

			@Override
			public void onComplete() {
				System.out.println("Observable Observer Completed");
				dispose();
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@SneakyThrows
			@Override
			public void onNext(String s) {
				Thread.sleep(200);
				System.out.println(s);
				result+=s;
			}
		});

		Thread.sleep(2000);

		assertEquals("abcdef", result);
	}
	
	@Ignore
	@Test
	public void flowable() {
		
		Flowable<Integer> flowable = Flowable.range(1, 10000);
		
		/*flowable.subscribe(System.out::println, Throwable::printStackTrace, 
				() -> System.out.println("Flowable subscriber Completed")).dispose();*/
		
		flowable.subscribeWith(new DisposableSubscriber<Integer>() {
			@Override
			public void onComplete() {
				System.out.println("Flowable subscriber Completed");
				dispose();
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@Override
			public void onNext(Integer i) {
				System.out.println(i);
			}
		});
		
		assertTrue(true);
	}


	@Test
	public void backPressure() throws InterruptedException {
		Flowable<Integer> flowable = Flowable.range(1, 10000)
				.doOnNext(s -> System.out.println("Emitting :" +s))
				.subscribeOn(Schedulers.computation());
		
		flowable.subscribeWith(new DisposableSubscriber<Integer>() {
			@Override
			public void onComplete() {
				System.out.println("Flowable subscriber Completed");
				dispose();
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@SneakyThrows
			@Override
			public void onNext(Integer i) {
				System.out.println(i);
				Thread.sleep(200);
			}

			@Override
			protected void onStart() {
				request(Long.MAX_VALUE);
			}
		});

		Thread.sleep(1000);

		assertTrue(true);
	}

	@Ignore
	@Test
	public void compositeDisposable() {
		
		CompositeDisposable compositeDisposable = new CompositeDisposable();
		
		Single<String> single = Single.just("Hello");
		Disposable singleDisposable = single.subscribe(s -> result = s, Throwable::printStackTrace);
		
		compositeDisposable.add(singleDisposable);
		
		Maybe<String> maybe = Maybe.empty();
		Disposable mayBeDisposable = maybe.subscribe(s -> result = s, Throwable::printStackTrace, () -> System.out.println("No Item"));
		
		compositeDisposable.add(mayBeDisposable);
		
		compositeDisposable.dispose();

		assertTrue(true);
	}

	@Ignore
	@Test
	public void backPressureStrategy() {
		Observable<Integer> observable = Observable.range(1,10_000_00);

		Flowable<Integer> flowable =
				observable.toFlowable(BackpressureStrategy.MISSING)
				.doOnNext(s->System.out.println("Emitting : "+s));

		flowable.subscribeWith(new DisposableSubscriber<Integer>() {
			@Override
			public void onComplete() {
				System.out.println("Flowable subscriber Completed");
				dispose();
			}

			@Override
			public void onError(Throwable e) {
				e.printStackTrace();
			}

			@SneakyThrows
			@Override
			public void onNext(Integer i) {
				//request(1);
				System.out.println(i);
				Thread.sleep(200);
			}

			@Override
			protected void onStart() {
				request(1);
			}
		});

		assertTrue(true);
	}

	@Ignore
	@Test
	public void defer() throws InterruptedException {
		//Flowable<Integer> flowable = Flowable.range(a, b);

		Flowable<Integer> flowable = Flowable.defer(()->Flowable.range(a, b)).doOnNext(s->System.out.println("Emitting : "+s));

		flowable.subscribeOn(Schedulers.newThread()).subscribe(i -> {System.out.println("first "+i); Thread.sleep(1000);});

		b = 15;

		flowable.subscribeOn(Schedulers.newThread()).subscribe(i -> {System.out.println("second "+i); Thread.sleep(1000);});

		Thread.sleep(60000);

		assertTrue(true);
	}

	@Ignore
	@Test
	public void map() {
		Flowable<String> flowable = Flowable.fromArray("a","b","c","d","e","f");

		flowable.map(String::toUpperCase).subscribe(System.out::println);

		assertTrue(true);
	}

	@Ignore
	@Test
	public void buffer() {
		Flowable<Integer> flowable = Flowable.range(1,100);

		flowable.buffer(10).subscribe(System.out::println);

		assertTrue(true);
	}

	@Test
	public void window()  {
		Flowable<Integer> flowable = Flowable.range(1,100);

		flowable.window(10).subscribe(s -> s.subscribe(System.out::println));

		assertTrue(true);
	}

	@Ignore
	@Test
	public void publishSubject() {
		PublishSubject<String> subject = PublishSubject.create();
		subject.subscribe(value -> System.out.println("first "+value));

		subject.onNext("a");
		subject.onNext("b");
		subject.onNext("c");

		subject.subscribe(value -> System.out.println("second "+value));

		subject.onNext("d");
		subject.onComplete();

		assertTrue(true);
	}


	@Ignore
	@Test
	public void behaviorSubject() {
		BehaviorSubject<String> subject = BehaviorSubject.create();
		subject.subscribe(value -> System.out.println("first "+value));

		subject.onNext("a");
		subject.onNext("b");
		subject.onNext("c");

		subject.subscribe(value -> System.out.println("second "+value));

		subject.onNext("d");
		subject.onComplete();

		assertTrue(true);
	}

	@Ignore
	@Test
	public void asyncSubject() {
		AsyncSubject<String> subject = AsyncSubject.create();
		subject.subscribe(value -> System.out.println("first "+value));

		subject.onNext("a");
		subject.onNext("b");
		subject.onNext("c");

		subject.subscribe(value -> System.out.println("second "+value));

		subject.onNext("d");
		subject.onComplete();

		assertTrue(true);
	}
}
