# Reactive Basics

- Elastic : The system stays responsive under varying workload.
- Responsive : The system responds in a timely manner if at all possible.
- Resilient : The system stays responsive in the face of failure.
- Message Driven : Reactive Systems rely on asynchronous message-passing to establish a boundary between components that ensures loose coupling, isolation and location transparency.



<img src="https://pbs.twimg.com/media/CNHmOK4WoAAQpoO.png">

###### How many threads can i have ?   

-  Non Blocking
 1. if your task is computation intensive <= number of cores
 2. if you task is IO intensive <= no. of cores / 1 - blocking factor (0=<1)

- Blocking

        New thread #214170
        Exception in thread "main" java.lang.OutOfMemoryError: unable to create new native thread
            at java.lang.Thread.start0(Native Method)
            at java.lang.Thread.start(Thread.java:717)
            at com.demo.reactive.NumberOfThreads.main(NumberOfThreads.java:22)
    
It may vary system to system and time to time.

Reactive Programming is an asynchronous programming paradigm concerned with data streams and the propagation of change.

<img src="https://miro.medium.com/max/2735/1*WVP8haZ_BXxVPEzsPDZWsQ.png" />

As a first step in the direction of reactive programming, Microsoft created the Reactive Extensions (Rx) library in the .NET ecosystem. Then RxJava implemented reactive programming on the JVM. As time went on, a standardization for Java emerged through the Reactive Streams effort, a specification that defines a set of interfaces and interaction rules for reactive libraries on the JVM. Its interfaces have been integrated into Java 9 under the Flow class.

## RxJava 2

| Type | Description |
|--|--|
|Single|1 item or error. Can be treated as a reactive version of method call.|
|Maybe|Either No item or 1 item emitted. Can be treated as a reactive version of Optional.|
|Completable|No item emitted. Used as a signal for completion or error. Can be treated as a reactive version of Runnable.|
|Observable|0..N flows ,but no back-pressure.|
|Flowable|0..N flows, Emits 0 or n items. Supports Reactive-Streams and back-pressure.|


A Subject is a sort of bridge or proxy that is available in some implementations of ReactiveX that acts both 
as an observer and as an Observable. Because it is an observer, it can subscribe to one or more Observables, 
and because it is an Observable, it can pass through the items it observes by reemitting them, and it can also 
emit new items.

| Subject | Description |
|--|--|
| Publish Subject | Emits only those items which are emitted after time of subscription. |
| Replay Subject | Emits all the items emitted by source Observable regardless of when it has subscribed the Observable. |
| Behavior Subject | Upon subscription, emits the most recent item then continue to emit item emitted by the source Observable. |
| Async Subject | Emits the last item emitted by the source Observable after it's completes emission. |

## Reactor

| Reactor | |
|--|--|
| Flux | an Asynchronous Sequence of 0-N Items |
| Mono | an Asynchronous 0-1 Result |


A Flux<T> is a standard Publisher<T> that represents an asynchronous sequence of 0 to N emitted items,
optionally terminated by either a completion signal or an error. As in the Reactive Streams spec, these three types of signal translate to calls to a downstream Subscriber’s onNext, onComplete, and onError methods.

A Mono<T> is a specialized Publisher<T> that emits at most one item and then (optionally) terminates with an onComplete signal or an onError signal.
