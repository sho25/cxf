begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Connection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|ExceptionListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|JMSException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|MessageListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Session
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|BusFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
operator|.
name|ClassLoaderHolder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|continuations
operator|.
name|ContinuationProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|continuations
operator|.
name|SuspendedInvocationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|OneWayProcessorInterceptor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|AbstractMultiplexDestination
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|Conduit
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
operator|.
name|continuations
operator|.
name|JMSContinuationProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|JMSListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|JMSUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|MessageListenerContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|jms
operator|.
name|util
operator|.
name|ResourceCloser
import|;
end_import

begin_class
specifier|public
class|class
name|JMSDestination
extends|extends
name|AbstractMultiplexDestination
implements|implements
name|MessageListener
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JMSDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|JMSConfiguration
name|jmsConfig
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
specifier|private
name|EndpointInfo
name|ei
decl_stmt|;
specifier|private
name|JMSListenerContainer
name|jmsListener
decl_stmt|;
specifier|private
name|ThrottlingCounter
name|suspendedContinuations
decl_stmt|;
specifier|private
name|ClassLoader
name|loader
decl_stmt|;
specifier|private
name|Connection
name|connection
decl_stmt|;
specifier|private
name|boolean
name|shutdown
decl_stmt|;
specifier|public
name|JMSDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|info
parameter_list|,
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
name|super
argument_list|(
name|b
argument_list|,
name|getTargetReference
argument_list|(
name|info
argument_list|,
name|b
argument_list|)
argument_list|,
name|info
argument_list|)
expr_stmt|;
name|this
operator|.
name|bus
operator|=
name|b
expr_stmt|;
name|this
operator|.
name|ei
operator|=
name|info
expr_stmt|;
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
name|info
operator|.
name|setProperty
argument_list|(
name|OneWayProcessorInterceptor
operator|.
name|USE_ORIGINAL_THREAD
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|loader
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ClassLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param inMessage the incoming message      * @return the inbuilt backchannel      */
specifier|protected
name|Conduit
name|getInbuiltBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|)
block|{
return|return
operator|new
name|BackChannelConduit
argument_list|(
name|inMessage
argument_list|,
name|jmsConfig
argument_list|,
name|connection
argument_list|)
return|;
block|}
comment|/**      * Initialize jmsTemplate and jmsListener from jms configuration data in jmsConfig {@inheritDoc}      */
specifier|public
name|void
name|activate
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"JMSDestination activate().... "
argument_list|)
expr_stmt|;
name|jmsConfig
operator|.
name|ensureProperlyConfigured
argument_list|()
expr_stmt|;
name|jmsListener
operator|=
name|createTargetDestinationListener
argument_list|()
expr_stmt|;
name|int
name|restartLimit
init|=
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
operator|*
name|jmsConfig
operator|.
name|getReconnectPercentOfMax
argument_list|()
operator|/
literal|100
decl_stmt|;
name|this
operator|.
name|suspendedContinuations
operator|=
operator|new
name|ThrottlingCounter
argument_list|(
name|this
operator|.
name|jmsListener
argument_list|,
name|restartLimit
argument_list|,
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|JMSListenerContainer
name|createTargetDestinationListener
parameter_list|()
block|{
name|Session
name|session
init|=
literal|null
decl_stmt|;
try|try
block|{
name|connection
operator|=
name|JMSFactory
operator|.
name|createConnection
argument_list|(
name|jmsConfig
argument_list|)
expr_stmt|;
name|connection
operator|.
name|setExceptionListener
argument_list|(
operator|new
name|ExceptionListener
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|onException
parameter_list|(
name|JMSException
name|exception
parameter_list|)
block|{
name|restartConnection
argument_list|(
name|exception
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|connection
operator|.
name|start
argument_list|()
expr_stmt|;
name|session
operator|=
name|connection
operator|.
name|createSession
argument_list|(
name|jmsConfig
operator|.
name|isSessionTransacted
argument_list|()
argument_list|,
name|Session
operator|.
name|AUTO_ACKNOWLEDGE
argument_list|)
expr_stmt|;
name|Destination
name|destination
init|=
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|(
name|session
argument_list|)
decl_stmt|;
name|MessageListenerContainer
name|container
init|=
operator|new
name|MessageListenerContainer
argument_list|(
name|connection
argument_list|,
name|destination
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|container
operator|.
name|setMessageSelector
argument_list|(
name|jmsConfig
operator|.
name|getMessageSelector
argument_list|()
argument_list|)
expr_stmt|;
name|Executor
name|executor
init|=
name|JMSFactory
operator|.
name|createExecutor
argument_list|(
name|bus
argument_list|,
literal|"jms-destination"
argument_list|)
decl_stmt|;
name|container
operator|.
name|setExecutor
argument_list|(
name|executor
argument_list|)
expr_stmt|;
name|container
operator|.
name|start
argument_list|()
expr_stmt|;
return|return
name|container
return|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
name|JMSUtil
operator|.
name|convertJmsException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|ResourceCloser
operator|.
name|close
argument_list|(
name|session
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|restartConnection
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"Exception on JMS connection. Trying to reconnect"
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|int
name|tries
init|=
literal|0
decl_stmt|;
do|do
block|{
name|tries
operator|++
expr_stmt|;
try|try
block|{
name|deactivate
argument_list|()
expr_stmt|;
name|activate
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|INFO
argument_list|,
literal|"Reestablished JMS connection"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e1
parameter_list|)
block|{
name|jmsListener
operator|=
literal|null
expr_stmt|;
name|String
name|message
init|=
literal|"Exception on reconnect. Trying again, attempt num "
operator|+
name|tries
decl_stmt|;
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|message
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
literal|5000
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e2
parameter_list|)
block|{
comment|// Ignore
block|}
block|}
block|}
do|while
condition|(
name|jmsListener
operator|==
literal|null
operator|&&
operator|!
name|shutdown
condition|)
do|;
block|}
specifier|public
name|void
name|deactivate
parameter_list|()
block|{
if|if
condition|(
name|jmsListener
operator|!=
literal|null
condition|)
block|{
name|jmsListener
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|suspendedContinuations
operator|=
literal|null
expr_stmt|;
name|connection
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|this
operator|.
name|shutdown
operator|=
literal|true
expr_stmt|;
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"JMSDestination shutdown()"
argument_list|)
expr_stmt|;
name|this
operator|.
name|deactivate
argument_list|()
expr_stmt|;
block|}
comment|/**      * Convert JMS message received by ListenerThread to CXF message and inform incomingObserver that a      * message was received. The observer will call the service and then send the response CXF message by      * using the BackChannelConduit      *       * @param message      * @throws IOException      */
specifier|public
name|void
name|onMessage
parameter_list|(
name|javax
operator|.
name|jms
operator|.
name|Message
name|message
parameter_list|)
block|{
name|ClassLoaderHolder
name|origLoader
init|=
literal|null
decl_stmt|;
name|Bus
name|origBus
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|loader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|=
name|ClassLoaderUtils
operator|.
name|setThreadContextClassloader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"JMS destination received message "
operator|+
name|message
operator|+
literal|" on "
operator|+
name|jmsConfig
operator|.
name|getTargetDestination
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|inMessage
init|=
name|JMSMessageUtils
operator|.
name|asCXFMessage
argument_list|(
name|message
argument_list|,
name|JMSConstants
operator|.
name|JMS_SERVER_REQUEST_HEADERS
argument_list|)
decl_stmt|;
name|SecurityContext
name|securityContext
init|=
name|JMSMessageUtils
operator|.
name|buildSecurityContext
argument_list|(
name|message
argument_list|,
name|jmsConfig
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|securityContext
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_SERVER_RESPONSE_HEADERS
argument_list|,
operator|new
name|JMSMessageHeadersType
argument_list|()
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|JMSConstants
operator|.
name|JMS_REQUEST_MESSAGE
argument_list|,
name|message
argument_list|)
expr_stmt|;
operator|(
operator|(
name|MessageImpl
operator|)
name|inMessage
operator|)
operator|.
name|setDestination
argument_list|(
name|this
argument_list|)
expr_stmt|;
if|if
condition|(
name|jmsConfig
operator|.
name|getMaxSuspendedContinuations
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|JMSContinuationProvider
name|cp
init|=
operator|new
name|JMSContinuationProvider
argument_list|(
name|bus
argument_list|,
name|inMessage
argument_list|,
name|incomingObserver
argument_list|,
name|suspendedContinuations
argument_list|)
decl_stmt|;
name|inMessage
operator|.
name|put
argument_list|(
name|ContinuationProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|cp
argument_list|)
expr_stmt|;
block|}
name|origBus
operator|=
name|BusFactory
operator|.
name|getAndSetThreadDefaultBus
argument_list|(
name|bus
argument_list|)
expr_stmt|;
comment|// FIXME
comment|// JCATransactionalMessageListenerContainer.setMessageEndpoint(inMessage);
comment|// handle the incoming message
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
if|if
condition|(
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
operator|&&
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|inMessage
operator|=
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
expr_stmt|;
block|}
comment|// need to propagate any exceptions back so transactions can occur
if|if
condition|(
name|inMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|Exception
name|ex
init|=
name|inMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|ex
operator|instanceof
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|Fault
operator|)
condition|)
block|{
if|if
condition|(
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
operator|(
name|RuntimeException
operator|)
name|ex
operator|.
name|getCause
argument_list|()
throw|;
block|}
else|else
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|SuspendedInvocationException
name|ex
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"Request message has been suspended"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|ex
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"can't get the right encoding information. "
operator|+
name|ex
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JMSException
name|e
parameter_list|)
block|{
throw|throw
name|JMSUtil
operator|.
name|convertJmsException
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
if|if
condition|(
name|origBus
operator|!=
name|bus
condition|)
block|{
name|BusFactory
operator|.
name|setThreadDefaultBus
argument_list|(
name|origBus
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|origLoader
operator|!=
literal|null
condition|)
block|{
name|origLoader
operator|.
name|reset
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
specifier|public
name|JMSConfiguration
name|getJmsConfig
parameter_list|()
block|{
return|return
name|jmsConfig
return|;
block|}
specifier|public
name|void
name|setJmsConfig
parameter_list|(
name|JMSConfiguration
name|jmsConfig
parameter_list|)
block|{
name|this
operator|.
name|jmsConfig
operator|=
name|jmsConfig
expr_stmt|;
block|}
block|}
end_class

end_unit

