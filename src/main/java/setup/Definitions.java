package setup;

import javax.jms.JMSConnectionFactoryDefinition;
import javax.jms.JMSDestinationDefinition;
import javax.jms.JMSDestinationDefinitions;


@JMSConnectionFactoryDefinition(
	name="java:global/jms/testConnectionFactory", interfaceName = "javax.jms.ConnectionFactory"
) 



@JMSDestinationDefinitions({
	@JMSDestinationDefinition(name = "java:global/jms/TopicTest", interfaceName = "javax.jms.Topic",
			destinationName = "TopicTest"),
	
	@JMSDestinationDefinition(name = "java:global/jms/QueueTest", interfaceName = "javax.jms.Queue",
			destinationName = "QueueTest"),
	
	
	@JMSDestinationDefinition(name = "java:global/jms/TopicSimple", interfaceName = "javax.jms.Topic",
			destinationName = "TopicSimple"),

	@JMSDestinationDefinition(name = "java:global/jms/QueueSimple", interfaceName = "javax.jms.Queue",
			destinationName = "QueueSimple"),
	
	
	@JMSDestinationDefinition(name = "java:global/jms/FilteringMessagesQueue", interfaceName = "javax.jms.Queue",
			destinationName = "FilteringMessagesQueue")
})

public class Definitions {}