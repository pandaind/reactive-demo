# Camel Reactive Stream

The Reactive Streams component allows you to exchange messages with reactive stream processing libraries compatible with the reactive streams standard.

The Camel module provides a reactive-streams component that allows users to define incoming and outgoing streams within Camel routes, and a direct client API that allows using Camel endpoints directly into any external reactive framework.

Camel uses an internal implementation of the reactive streams Publisher and Subscriber, so it’s not tied to any specific framework. The following reactive frameworks have been used in the integration tests: Reactor Core 3, RxJava 2.

##### Usage
- Get data from Camel routes (In-Only from Camel)
- Send data to Camel routes (In-Only towards Camel)
- Request a transformation to a Camel route (In-Out towards Camel)
- Process data flowing from a Camel route using a reactive processing step (In-Out from Camel) 

##### CONTROLLING BACKPRESSURE
- Producer Side :

    When routing Camel exchanges to an external subscriber, backpressure is handled by an internal buffer that caches exchanges before delivering them. If the subscriber is slower than the exchange rate, the buffer may become too big. In many circumstances this must be avoided.

        from("jms:queue")
            .to("reactive-streams:flow");

    If the JMS queue contains a high number of messages and the Subscriber associated with the flow stream is too slow, messages are dequeued from JMS and appended to the buffer, possibly causing a "out of memory" error. To avoid such problems, a ThrottlingInflightRoutePolicy can be set in the route.
    
    In contexts where a certain amount of data loss is acceptable, setting a backpressure strategy other than BUFFER can be a solution for dealing with fast sources.
    
        .to("reactive-streams:flow?backpressureStrategy=LATEST");

- Consumer Side :
    
    When Camel consumes items from a reactive-streams publisher, the maximum number of inflight exchanges can be set as endpoint option.
    
        from("reactive-streams:numbers?maxInflightExchanges=10")
            .to("direct:endpoint");
    The number of items that Camel requests to the source publisher (through the reactive streams backpressure mechanism) is always lower than 10. Messages are processed by a single thread in the Camel side.
    
    The number of concurrent consumers (threads) can also be set as endpoint option (concurrentConsumers).
