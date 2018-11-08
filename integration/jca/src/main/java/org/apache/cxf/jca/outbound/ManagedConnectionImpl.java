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
name|jca
operator|.
name|outbound
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|resource
operator|.
name|NotSupportedException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|ResourceException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ConnectionEvent
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ConnectionEventListener
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ConnectionRequestInfo
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|LocalTransaction
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ManagedConnection
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|resource
operator|.
name|spi
operator|.
name|ManagedConnectionMetaData
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|transaction
operator|.
name|xa
operator|.
name|XAResource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|BindingProvider
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|configuration
operator|.
name|Configurer
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
name|endpoint
operator|.
name|Client
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
name|frontend
operator|.
name|ClientProxy
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
name|frontend
operator|.
name|ClientProxyFactoryBean
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
name|jaxws
operator|.
name|EndpointUtils
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
name|jaxws
operator|.
name|JaxWsProxyFactoryBean
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
name|jca
operator|.
name|core
operator|.
name|logging
operator|.
name|LoggerHelper
import|;
end_import

begin_comment
comment|/**  * Represents a "physical" connection to EIS, which provides access to target  * web service.  ManagedConnectionImpl creates connection handles for  * applications to use the connection backed by this object.  */
end_comment

begin_class
specifier|public
class|class
name|ManagedConnectionImpl
implements|implements
name|ManagedConnection
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
name|ManagedConnectionImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|ConnectionEventListener
argument_list|>
name|listeners
init|=
name|Collections
operator|.
name|synchronizedSet
argument_list|(
operator|new
name|HashSet
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|Object
argument_list|,
name|Subject
argument_list|>
name|handles
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|HashMap
argument_list|<
name|Object
argument_list|,
name|Subject
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
name|PrintWriter
name|printWriter
decl_stmt|;
specifier|private
name|ManagedConnectionFactoryImpl
name|mcf
decl_stmt|;
specifier|private
name|ConnectionRequestInfo
name|connReqInfo
decl_stmt|;
specifier|private
name|boolean
name|isClosed
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|Object
name|associatedHandle
decl_stmt|;
specifier|private
name|Object
name|clientProxy
decl_stmt|;
specifier|public
name|ManagedConnectionImpl
parameter_list|(
name|ManagedConnectionFactoryImpl
name|mcf
parameter_list|,
name|ConnectionRequestInfo
name|connReqInfo
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
name|this
operator|.
name|mcf
operator|=
name|mcf
expr_stmt|;
name|this
operator|.
name|connReqInfo
operator|=
name|connReqInfo
expr_stmt|;
block|}
comment|/* -------------------------------------------------------------------      * ManagedConnection Methods      */
specifier|public
name|void
name|addConnectionEventListener
parameter_list|(
name|ConnectionEventListener
name|listener
parameter_list|)
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"add listener : "
operator|+
name|listener
argument_list|)
expr_stmt|;
block|}
name|listeners
operator|.
name|add
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|associateConnection
parameter_list|(
name|Object
name|connection
parameter_list|)
throws|throws
name|ResourceException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"associate handle : "
operator|+
name|connection
argument_list|)
expr_stmt|;
block|}
name|associatedHandle
operator|=
name|connection
expr_stmt|;
comment|// nothing needs to be done as app gets a copy of client proxy
block|}
specifier|public
name|void
name|cleanup
parameter_list|()
throws|throws
name|ResourceException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"cleanup"
argument_list|)
expr_stmt|;
block|}
name|handles
operator|.
name|clear
argument_list|()
expr_stmt|;
name|isClosed
operator|=
literal|false
expr_stmt|;
block|}
specifier|public
name|void
name|destroy
parameter_list|()
throws|throws
name|ResourceException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"destroy"
argument_list|)
expr_stmt|;
block|}
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|clientProxy
argument_list|)
decl_stmt|;
name|client
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|handles
operator|.
name|clear
argument_list|()
expr_stmt|;
name|isClosed
operator|=
literal|false
expr_stmt|;
name|bus
operator|=
literal|null
expr_stmt|;
name|connReqInfo
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|Object
name|getConnection
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|ConnectionRequestInfo
name|cxRequestInfo
parameter_list|)
throws|throws
name|ResourceException
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINER
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finer
argument_list|(
literal|"get handle for subject="
operator|+
name|subject
operator|+
literal|" cxRequestInfo="
operator|+
name|cxRequestInfo
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|isClosed
condition|)
block|{
throw|throw
operator|new
name|ResourceException
argument_list|(
literal|"connection has been closed"
argument_list|)
throw|;
block|}
comment|// check request info
if|if
condition|(
operator|!
name|connReqInfo
operator|.
name|equals
argument_list|(
name|cxRequestInfo
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ResourceException
argument_list|(
literal|"connection request info: "
operator|+
name|cxRequestInfo
operator|+
literal|" does not match "
operator|+
name|connReqInfo
argument_list|)
throw|;
block|}
name|CXFConnectionSpec
name|spec
init|=
operator|(
name|CXFConnectionSpec
operator|)
name|cxRequestInfo
decl_stmt|;
name|Object
name|handle
init|=
name|createConnectionHandle
argument_list|(
name|spec
argument_list|)
decl_stmt|;
name|handles
operator|.
name|put
argument_list|(
name|handle
argument_list|,
name|subject
argument_list|)
expr_stmt|;
name|associatedHandle
operator|=
name|handle
expr_stmt|;
return|return
name|handle
return|;
block|}
specifier|public
name|LocalTransaction
name|getLocalTransaction
parameter_list|()
throws|throws
name|ResourceException
block|{
throw|throw
operator|new
name|NotSupportedException
argument_list|(
literal|"LocalTransaction is not supported."
argument_list|)
throw|;
block|}
specifier|public
name|PrintWriter
name|getLogWriter
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
name|printWriter
return|;
block|}
specifier|public
name|ManagedConnectionMetaData
name|getMetaData
parameter_list|()
throws|throws
name|ResourceException
block|{
return|return
operator|new
name|CXFManagedConnectionMetaData
argument_list|(
name|getUserName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|XAResource
name|getXAResource
parameter_list|()
throws|throws
name|ResourceException
block|{
throw|throw
operator|new
name|NotSupportedException
argument_list|(
literal|"XAResource is not supported."
argument_list|)
throw|;
block|}
specifier|public
name|void
name|removeConnectionEventListener
parameter_list|(
name|ConnectionEventListener
name|listener
parameter_list|)
block|{
name|listeners
operator|.
name|remove
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setLogWriter
parameter_list|(
name|PrintWriter
name|out
parameter_list|)
throws|throws
name|ResourceException
block|{
name|printWriter
operator|=
name|out
expr_stmt|;
if|if
condition|(
name|printWriter
operator|!=
literal|null
condition|)
block|{
name|LoggerHelper
operator|.
name|initializeLoggingOnWriter
argument_list|(
name|printWriter
argument_list|)
expr_stmt|;
block|}
block|}
comment|/* -------------------------------------------------------------------      * Public Methods      */
specifier|public
name|ConnectionRequestInfo
name|getRequestInfo
parameter_list|()
block|{
return|return
name|connReqInfo
return|;
block|}
specifier|public
name|ManagedConnectionFactoryImpl
name|getManagedConnectionFactoryImpl
parameter_list|()
block|{
return|return
name|mcf
return|;
block|}
comment|/* -------------------------------------------------------------------      * Private Methods      */
specifier|private
name|void
name|sendEvent
parameter_list|(
specifier|final
name|ConnectionEvent
name|coEvent
parameter_list|)
block|{
synchronized|synchronized
init|(
name|listeners
init|)
block|{
name|Iterator
argument_list|<
name|ConnectionEventListener
argument_list|>
name|iterator
init|=
name|listeners
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iterator
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|sendEventToListener
argument_list|(
name|iterator
operator|.
name|next
argument_list|()
argument_list|,
name|coEvent
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|sendEventToListener
parameter_list|(
name|ConnectionEventListener
name|listener
parameter_list|,
name|ConnectionEvent
name|coEvent
parameter_list|)
block|{
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|CONNECTION_CLOSED
condition|)
block|{
name|listener
operator|.
name|connectionClosed
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_COMMITTED
condition|)
block|{
name|listener
operator|.
name|localTransactionCommitted
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_ROLLEDBACK
condition|)
block|{
name|listener
operator|.
name|localTransactionRolledback
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|LOCAL_TRANSACTION_STARTED
condition|)
block|{
name|listener
operator|.
name|localTransactionStarted
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|coEvent
operator|.
name|getId
argument_list|()
operator|==
name|ConnectionEvent
operator|.
name|CONNECTION_ERROR_OCCURRED
condition|)
block|{
name|listener
operator|.
name|connectionErrorOccurred
argument_list|(
name|coEvent
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getUserName
parameter_list|()
block|{
if|if
condition|(
name|associatedHandle
operator|!=
literal|null
condition|)
block|{
name|Subject
name|subject
init|=
name|handles
operator|.
name|get
argument_list|(
name|associatedHandle
argument_list|)
decl_stmt|;
if|if
condition|(
name|subject
operator|!=
literal|null
condition|)
block|{
return|return
name|subject
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Object
name|createConnectionHandle
parameter_list|(
specifier|final
name|CXFConnectionSpec
name|spec
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|interfaces
init|=
block|{
name|CXFConnection
operator|.
name|class
block|,
name|BindingProvider
operator|.
name|class
block|,
name|spec
operator|.
name|getServiceClass
argument_list|()
block|}
decl_stmt|;
return|return
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|spec
operator|.
name|getServiceClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|interfaces
argument_list|,
operator|new
name|ConnectionInvocationHandler
argument_list|(
name|createClientProxy
argument_list|(
name|spec
argument_list|)
argument_list|,
name|spec
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|synchronized
name|Object
name|createClientProxy
parameter_list|(
specifier|final
name|CXFConnectionSpec
name|spec
parameter_list|)
block|{
if|if
condition|(
name|clientProxy
operator|==
literal|null
condition|)
block|{
name|validateConnectionSpec
argument_list|(
name|spec
argument_list|)
expr_stmt|;
name|ClientProxyFactoryBean
name|factory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|EndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|spec
operator|.
name|getServiceClass
argument_list|()
argument_list|)
condition|)
block|{
name|factory
operator|=
operator|new
name|JaxWsProxyFactoryBean
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|=
operator|new
name|ClientProxyFactoryBean
argument_list|()
expr_stmt|;
block|}
name|factory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|(
name|spec
operator|.
name|getBusConfigURL
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setServiceClass
argument_list|(
name|spec
operator|.
name|getServiceClass
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setEndpointName
argument_list|(
name|spec
operator|.
name|getEndpointName
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setServiceName
argument_list|(
name|spec
operator|.
name|getServiceName
argument_list|()
argument_list|)
expr_stmt|;
name|factory
operator|.
name|getServiceFactory
argument_list|()
operator|.
name|setWsdlURL
argument_list|(
name|spec
operator|.
name|getWsdlURL
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|spec
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setAddress
argument_list|(
name|spec
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|configureObject
argument_list|(
name|spec
operator|.
name|getEndpointName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|".jaxws-client.proxyFactory"
argument_list|,
name|factory
argument_list|)
expr_stmt|;
name|clientProxy
operator|=
name|factory
operator|.
name|create
argument_list|()
expr_stmt|;
block|}
return|return
name|clientProxy
return|;
block|}
specifier|private
name|void
name|validateConnectionSpec
parameter_list|(
name|CXFConnectionSpec
name|spec
parameter_list|)
block|{
if|if
condition|(
name|spec
operator|.
name|getServiceClass
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no serviceClass in connection spec"
argument_list|)
throw|;
block|}
if|if
condition|(
name|spec
operator|.
name|getEndpointName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no endpointName in connection spec"
argument_list|)
throw|;
block|}
if|if
condition|(
name|spec
operator|.
name|getServiceName
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no serviceName in connection spec"
argument_list|)
throw|;
block|}
if|if
condition|(
name|spec
operator|.
name|getWsdlURL
argument_list|()
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"no wsdlURL in connection spec"
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|configureObject
parameter_list|(
name|String
name|name
parameter_list|,
name|Object
name|instance
parameter_list|)
block|{
name|Configurer
name|configurer
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|Configurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|configurer
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|name
argument_list|,
name|instance
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|synchronized
name|Bus
name|getBus
parameter_list|(
name|URL
name|busConfigLocation
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|busConfigLocation
operator|!=
literal|null
condition|)
block|{
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
name|fine
argument_list|(
literal|"Create bus from location "
operator|+
name|busConfigLocation
argument_list|)
expr_stmt|;
block|}
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|busConfigLocation
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|mcf
operator|.
name|getBusConfigURL
argument_list|()
operator|!=
literal|null
condition|)
block|{
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
name|fine
argument_list|(
literal|"Create bus from URL "
operator|+
name|mcf
operator|.
name|getBusConfigURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|URL
name|url
init|=
literal|null
decl_stmt|;
try|try
block|{
name|url
operator|=
operator|new
name|URL
argument_list|(
name|mcf
operator|.
name|getBusConfigURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"Malformed URL "
operator|+
name|mcf
operator|.
name|getBusConfigURL
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|url
operator|!=
literal|null
condition|)
block|{
name|bus
operator|=
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|url
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
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
name|fine
argument_list|(
literal|"Create default bus"
argument_list|)
expr_stmt|;
block|}
name|bus
operator|=
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|bus
return|;
block|}
specifier|private
class|class
name|ConnectionInvocationHandler
implements|implements
name|InvocationHandler
block|{
specifier|private
name|Object
name|target
decl_stmt|;
specifier|private
name|CXFConnectionSpec
name|spec
decl_stmt|;
name|ConnectionInvocationHandler
parameter_list|(
name|Object
name|target
parameter_list|,
name|CXFConnectionSpec
name|spec
parameter_list|)
block|{
name|this
operator|.
name|target
operator|=
name|target
expr_stmt|;
name|this
operator|.
name|spec
operator|=
name|spec
expr_stmt|;
block|}
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINEST
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|finest
argument_list|(
literal|"invoke connection spec:"
operator|+
name|spec
operator|+
literal|" method="
operator|+
name|method
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"hashCode"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|method
operator|.
name|invoke
argument_list|(
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|proxy
argument_list|)
argument_list|,
name|args
argument_list|)
return|;
block|}
if|if
condition|(
literal|"equals"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
comment|// These are proxies.  We don't really care if their targets are equal.
comment|// We do care if these are the same proxy instances that we created.
comment|// Therefore, if their proxy and invocation handler are consistent,
comment|// we believe they are equal.
name|boolean
name|result
init|=
literal|false
decl_stmt|;
try|try
block|{
name|result
operator|=
name|proxy
operator|==
name|args
index|[
literal|0
index|]
operator|&&
name|this
operator|==
name|Proxy
operator|.
name|getInvocationHandler
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// ignore and assume not equal
block|}
return|return
name|result
return|;
block|}
if|if
condition|(
literal|"toString"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|"ManagedConnection: "
operator|+
name|spec
return|;
block|}
if|if
condition|(
operator|!
name|handles
operator|.
name|containsKey
argument_list|(
name|proxy
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Stale connection"
argument_list|)
throw|;
block|}
if|if
condition|(
literal|"getService"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|handleGetServiceMethod
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"close"
operator|.
name|equals
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|handleCloseMethod
argument_list|(
name|proxy
argument_list|,
name|method
argument_list|,
name|args
argument_list|)
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unhandled method "
operator|+
name|method
argument_list|)
throw|;
block|}
block|}
specifier|private
name|Object
name|handleGetServiceMethod
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
block|{
if|if
condition|(
operator|!
name|spec
operator|.
name|getServiceClass
argument_list|()
operator|.
name|equals
argument_list|(
name|args
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"serviceClass "
operator|+
name|args
index|[
literal|0
index|]
operator|+
literal|" does not match "
operator|+
name|spec
operator|.
name|getServiceClass
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|target
return|;
block|}
specifier|private
name|Object
name|handleCloseMethod
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
block|{
name|handles
operator|.
name|remove
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|associatedHandle
operator|=
literal|null
expr_stmt|;
name|ConnectionEvent
name|event
init|=
operator|new
name|ConnectionEvent
argument_list|(
name|ManagedConnectionImpl
operator|.
name|this
argument_list|,
name|ConnectionEvent
operator|.
name|CONNECTION_CLOSED
argument_list|)
decl_stmt|;
name|event
operator|.
name|setConnectionHandle
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
name|sendEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

