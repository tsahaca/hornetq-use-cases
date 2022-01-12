/*
 * JBoss, Home of Professional Open Source
 * Copyright 2014, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.as.quickstarts.jms;

import java.util.logging.Logger;
import java.util.Properties;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;

public class RateLimitConsumer {
    private static final Logger log = Logger.getLogger(RateLimitConsumer.class.getName());

    // Set up all the default values
    private static final String DEFAULT_MESSAGE = "Hello, World!";
    private static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
    private static final String DEFAULT_DESTINATION = "jms/queue/test";
    private static final String DEFAULT_MESSAGE_COUNT = "1";
    private static final String DEFAULT_USERNAME = "quickstartUser";
    private static final String DEFAULT_PASSWORD = "quickstartPwd1!";
    private static final String INITIAL_CONTEXT_FACTORY = "org.jboss.naming.remote.client.InitialContextFactory";
    private static final String PROVIDER_URL = "remote://localhost:4447";

    public static void main(String[] args) throws Exception {

        ConnectionFactory connectionFactory = null;
        Connection connection = null;
        Session session = null;
        MessageProducer producer = null;
        MessageConsumer consumer = null;
        Destination destination = null;
        TextMessage message = null;
        Context context = null;

        try {
            // Set up the context for the JNDI lookup
            final Properties env = new Properties();
            env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
            env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, PROVIDER_URL));
            env.put(Context.SECURITY_PRINCIPAL, System.getProperty("username", DEFAULT_USERNAME));
            env.put(Context.SECURITY_CREDENTIALS, System.getProperty("password", DEFAULT_PASSWORD));
            context = new InitialContext(env);

            // Perform the JNDI lookups
            String connectionFactoryString = System.getProperty("connection.factory", DEFAULT_CONNECTION_FACTORY);
            log.info("Attempting to acquire connection factory \"" + connectionFactoryString + "\"");
            connectionFactory = (ConnectionFactory) context.lookup(connectionFactoryString);
            log.info("Found connection factory \"" + connectionFactoryString + "\" in JNDI");

            String destinationString = System.getProperty("destination", DEFAULT_DESTINATION);
            log.info("Attempting to acquire destination \"" + destinationString + "\"");
            destination = (Destination) context.lookup(destinationString);
            log.info("Found destination \"" + destinationString + "\" in JNDI");

            // Create the JMS connection, session, producer, and consumer
            connection = connectionFactory.createConnection(System.getProperty("username", DEFAULT_USERNAME), System.getProperty("password", DEFAULT_PASSWORD));
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            producer = session.createProducer(destination);
            consumer = session.createConsumer(destination);
            connection.start();

            int count = Integer.parseInt(System.getProperty("message.count", DEFAULT_MESSAGE_COUNT));
            String content = System.getProperty("message.content", DEFAULT_MESSAGE);

            log.info("Sending " + count + " messages with content: " + content);

            // Send the specified number of messages
            for (int i = 0; i < count; i++) {
                message = session.createTextMessage(content + " Message# " + (i+1));
                producer.send(message);
            }
            consumeRateLimit(consumer, count);
        } catch (Exception e) {
            log.severe(e.getMessage());
            throw e;
        } finally {
            if (context != null) {
                context.close();
            }

            // closing the connection takes care of the session, producer, and consumer
            if (connection != null) {
                connection.close();
            }
        }
    }

    private static boolean consumeRateLimit(MessageConsumer consumer, int count) throws JMSException {
        log.info("Will now try and consume " + count + " messages");
        int i = 0;
        long start = System.currentTimeMillis();

        while (i < count) {
            TextMessage message = (TextMessage)consumer.receiveNoWait();
            if (message == null)
            {
                return false;
            } /**else {
                log.info("Received message with content " + message.getText());
            }**/
            i++;
        }
        long end = System.currentTimeMillis();
        double rate = 1000 * (double)i / (end - start);
        log.info("We consumed " + i + " messages in " + (end - start) + " milliseconds");
        log.info("Actual consume rate was " + rate + " messages per second");

        return true;
    }
}

