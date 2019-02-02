package jms;

//	Java Messaging Service API
//-JMS is a standard Java API that allows apps to create, send, receive, and read messages asynchronously. 
//-It defines a common set of interfaces and classes that allow programs to communicate with other message providers.
// JMS is analogous to JDBC: the latter connects to several DBs (Derby, MySQL, Oracle, DB2, etc.),
// and JMS connects to several providers (OpenMQ, MQSeries, SonicMQ, etc.).
//-The JMS API has evolved ever since its creation. For historical reasons JMS offers three alternative sets of interfaces
// for producing and consuming messages. These very different interfaces evolved in JMS 1.0, 1.1 and 2.0 and are referred
// to as legacy API, classic API and simplified API.

//-JMS 1.0 (legacy API) made a clear difference between the point-to-point and publish-subscribe model. It defined two
// domain-specific APIs, one for point-to-point (queues) and one for pub-sub (topics). That’s why you can find
// QueueConnectionFactory and TopicConnectionFactory API instead of the generic ConnectionFactory fe.
// Note also the different vocabulary; a consumer is called a receiver in P2P and a subscriber in pub-sub.
//-The JMS 1.1 API (the classic API) provided a unified set of interfaces that can be used with both P2P and pub-sub messaging.
// Table shows the generic name of an interface (e.g., Session) and the legacy names for each model (QueueSession, TopicSession).
// But JMS 1.1 was still a verbose and low-level API compared to JPA or EJB. 
// -JMS 2.0 introduces a simplified API that offers all the features of the classic API but requires fewer interfaces and is simpler to use. 

//-Table highlights the differences between these APIs (all located under the javax.jms package).

//	Classic API				Simplified API			Legacy API (P2P)		Legacy API (Pub-Sub)
//	ConnectionFactory		ConnectionFactory		QueueConnectionFactory	TopicConnectionFactory
//	Connection				JMSContext				QueueConnection			TopicConnection
//	Session					JMSContext				QueueSession			TopicSession
//	Destination				Destination				Queue					Topic
//	Message					Message					Message					Message
//	MessageConsumer			JMSConsumer				QueueReceiver			TopicSubscriber
//	MessageProducer			JMSProducer				QueueSender				TopicPublisher
//	JMSException			JMSRuntimeException		JMSException			JMSException



public class Info_JMS_APIs {}
