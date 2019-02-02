package mdb;

//	Anatomy of an MDB
//-MDBs do not implement a local or remote business interface but instead implement the javax.jms.MessageListener interface.
//-Clients cannot invoke methods directly on MDBs; however, like session beans, MDBs have a rich programming model that includes
// a life cycle, callback annotations, interceptors, injection, and transactions.
// Taking advantage of this model provides applications with a high level of functionality.
//-It is important to be aware that MDBs are not part of the EJB Lite model, meaning that they cannot be deployed in
// a simple web profile application server, but still need the full Java EE stack.

//-The requirements to develop an MDB class are as follows:
// • The class must be annotated with @javax.ejb.MessageDriven or its XML equivalent in a deployment descriptor
// • The class must implement, directly or indirectly, the MessageListener interface
// • The class must be defined as public, and must not be final or abstract
// • The class must have a public no-arg constructor that the container will use to create instances of the MDB
// • The class must not define the finalize() method.

//-The MDB class is allowed to implement other methods, invoke other resources, and so on. MDBs are deployed in a container and
// can be optionally packaged with an ejb-jar.xml file. Following the “ease of use” model of Java EE 7, an MDB can be simply
// an annotated POJO, eliminating most of the configuration. However, if you still need to customize the JMS configuration,
// you can use the elements of the @MessageDriven and @ActivationConfigProperty annotations (or XML equivalent).


//	@MessageDriven
//-MDBs are one of the simplest kinds of EJBs to develop, as they support the smallest number of annotations. @MessageDriven (or DD)
// is mandatory, as it is the piece of metadata the container requires to recognize that the Java class is actually an MDB.
//-The API of the @MessageDriven annotation is very simple, and all elements are optional.
// • name element specifies the name of the MDB (which by default is the name of the class). 
// • messageListenerInterface specifies which message listener the MDB implements
//   (if the MDB implements multiple interfaces, it tells the EJB container which one is the MessageListener interface). 
// • mappedName element is the JNDI name of the destination that the MDB should be listening to. 
// • description is just a string, used to give a description of the MDB once deployed. 
// • activationConfig element is used to specify configuration properties and takes an array of @ActivationConfigProperty annotations.


//	@ActivationConfigProperty
//-JMS allows configuration of certain properties such as message selectors, acknowledgment mode, durable subscribers, and so on.
//-This optional annotation can be provided as one of the parameters for the @MessageDriven annotation.
//-The @ActivationConfigProperty is very basic, consisting of a name-value pair.
//-The activationConfig property allows you to provide standard and nonstandard (provider–specific) configuration.
//-List of some standard properties you can use.
//	Property 					Description
//	acknowledgeMode 			The acknowledgment mode (default is AUTO_ACKNOWLEDGE)
//	messageSelector 			The message selector string used by the MDB
//	destinationType 			The destination type, which can be TOPIC or QUEUE
//	destinationLookup 			The lookup name of an administratively-defined Queue or Topic
//	connectionFactoryLookup 	The lookup name of an administratively defined ConnectionFactory
//	destination 				The name of the destination.
//	subscriptionDurability 		The subscription durability (default is NON_DURABLE)
//	subscriptionName 			The subscription name of the consumer
//	shareSubscriptions 			Used if the message-driven bean is deployed into a clustered
//	clientId 					Client identifier that will be used when connecting to the JMS provider


//	MDB Context
//-The MessageDrivenContext interface provides access to the runtime context that the container provides for an MDB instance.
//-The container passes the MessageDrivenContext interface to this instance, which remains associated for the lifetime of the MDB.
// This gives the MDB the possibility to explicitly roll back a transaction, get the user principal, and so on.
//-The MessageDrivenContext interface extends the javax.ejb.EJBContext interface without adding any extra methods. 

//  Method 				Description
//  getCallerPrincipal 	Returns the java.security.Principal associated with the invocation
//  getRollbackOnly 		Tests whether the current transaction has been marked for rollback
//  getTimerService 		Returns the javax.ejb.TimerService interface
//  getUserTransaction 	Returns the javax.transaction.UserTransaction interface to use to demarcate transactions.
//							Only MDBs with bean-managed transaction (BMT) can use this method
//  isCallerInRole 		Tests whether the caller has a given security role
//  Lookup 				Enables the MDB to look up its environment entries in the JNDI naming context
//  setRollbackOnly 		Marks the current transaction as rollback. Only MDBs with BMT can use this method


//	Lifecycle
//-The MDB life cycle is identical to that of the stateless session bean:
// either the MDB exists and is ready to consume messages or it doesn’t exist. 
//-Before exiting, the container first creates an instance of the MDB and, if applicable, injects the necessary resources
// as specified by metadata annotations (@Resource, @Inject, @EJB, etc.) or DD. 
//-The container then calls the bean’s @PostConstruct callback method, if any.
//-After this, the MDB is in the ready state and waits to consume any incoming message.
//-The @PreDestroy callback occurs when the MDB is removed from the pool or destroyed.

//-This behavior is identical to that of stateless session beans (see Ch8 for more details about callback methods),
// and, like other EJBs, you can add interceptors.


//	Transactions
//-MDBs can use BMTs or container-managed transactions (CMTs); they can explicitly roll back a transaction by using
// the MessageDrivenContext.setRollbackOnly() and so on. 
//-The container will start a transaction before the onMessage() is invoked and will commit the transaction
// when the method returns (unless the transaction was marked for rollback with setRollbackOnly()).
//-Even though MDBs are transactional, they cannot execute in the client’s transaction context, as they don’t have a client.
// Nobody explicitly invokes methods on MDBs, they just listen to a destination and consume messages. There is no context passed
// from a client to an MDB, and similarly the client transaction context cannot be passed to the onMessage().

//-In CMTs, MDBs can use the @TransactionAttribute on business methods with the two following attributes:
// • REQUIRED (the default): If the MDB invokes other enterprise beans, the container passes the transaction context with
//   the invocation. The container attempts to commit the transaction when the message listener method has completed
// • NOT_SUPPORTED: If the MDB invokes other enterprise beans, the container passes no transaction context with the invocation


//	Handling Exceptions
//-The classic API defines 12 different exceptions, all inheriting from javax.jms.JMSException. 
//-The simplified API defines 10 runtime exceptions all inheriting from javax.jms.JMSRuntimeException.
//-It is important to note that JMSException is a checked exception and JMSRuntimeException is unchecked.
//-The EJB specification outlines two types of exceptions:
// • Application exceptions: Checked exceptions that extend Exception and do not cause the container to roll back
// • System exceptions: Unchecked exceptions that extend RuntimeException and cause the container to roll back
//-Throwing a JMSRuntimeException will cause the container to roll back, but throwing a JMSException will not.
//-If a rollback is needed, the setRollBackOnly() must be explicitly called or a system exception (such as EJBException) rethrown.


//	MDB as a Consumer
//-Consumers can receive a message either synchronously, by looping and waiting for a message to arrive,
// or asynchronously, by implementing the MessageListener interface. 
//-By nature, MDBs are designed to function as asynchronous message consumers. MDBs implement a message listener interface,
// which is triggered by the container when a message arrives.
//-Can an MDB be a synchronous consumer? Yes, but this is not recommended.
// Synchronous message consumers block and tie up server resources (the EJBs will be stuck looping without performing any work,
// and the container will not be able to free them).
//-MDBs, like SL SBs, live in a pool of a certain size. When the container needs an instance, it takes 1 out of the pool and uses it.
// If each instance goes into an infinite loop, the pool will eventually empty, and all the available instances will be busy
// looping. The EJB container can also start generating more MDB instances, growing the pool and eating up more and more memory.
//-For this reason, session beans and MDBs should not be used as synchronous message consumers.

//	Enterprise Beans		Producer		Synchronous Consumer		Asynchronous Consumer
//	Session beans			Yes				Not recommended				Not possible
//	MDB						Yes				Not recommended				Yes


//	MDB as a Producer
//-MDBs are capable of becoming message producers, something that occurs when they are involved in a workflow, as they receive a
// message from one destination, process it, and send it to another destination. To add this capability, the JMS API must be used.

//-A destination and a connection factory can be injected by using the @Resource and @JMSConnectionFactory annotations
// or via JNDI lookup, and then methods on the javax.jms.JMSContext object can be invoked to create and send a message. 
//-The code of the BillingMDB listens to a topic (jms/javaee7/Topic), receives messages (onMessage() method),
// and sends a new message to a queue (jms/javaee7/Queue).
//	@MessageDriven(mappedName = "jms/javaee7/Topic", activationConfig = { 
//	    @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
//	    @ActivationConfigProperty(propertyName = "messageSelector", propertyValue = "orderAmount BETWEEN 3 AND 7")
//	})
//	public class BillingMDB implements MessageListener {
//	    @Inject
//	    @JMSConnectionFactory("jms/javaee7/ConnectionFactory")
//	    @JMSSessionMode(JMSContext.AUTO_ACKNOWLEDGE)
// 	    private JMSContext context;
//  
//	    @Resource(lookup = "jms/javaee7/Queue")
//	    private Destination printingQueue;
//
//	    public void onMessage(Message message) {
//	        System.out.println("Message received: " + message.getBody(String.class));
//	        context.createProducer().send(printingQueue, "Message has been received and resent");
//	    }
//	}


public class Info {}
