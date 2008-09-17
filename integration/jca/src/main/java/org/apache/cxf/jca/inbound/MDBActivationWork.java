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
name|inbound
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|spi
operator|.
name|UnavailableException
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
name|endpoint
operator|.
name|MessageEndpoint
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
name|endpoint
operator|.
name|MessageEndpointFactory
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
name|work
operator|.
name|Work
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|endpoint
operator|.
name|Server
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
name|ServerFactoryBean
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
name|JaxWsServerFactoryBean
import|;
end_import

begin_comment
comment|/**  *  * MDBActivationWork is a type of {@link Work} that is executed by   * {@link javax.resource.spi.work.WorkManager}.  MDBActivationWork  * starts an CXF service endpoint to accept inbound calls for  * the JCA connector.  *   */
end_comment

begin_class
specifier|public
class|class
name|MDBActivationWork
implements|implements
name|Work
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
name|MDBActivationWork
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|MAX_ATTEMPTS
init|=
literal|5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|RETRY_SLEEP
init|=
literal|5000
decl_stmt|;
specifier|private
name|MDBActivationSpec
name|spec
decl_stmt|;
specifier|private
name|MessageEndpointFactory
name|endpointFactory
decl_stmt|;
specifier|private
name|boolean
name|released
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|InboundEndpoint
argument_list|>
name|endpoints
decl_stmt|;
specifier|public
name|MDBActivationWork
parameter_list|(
name|MDBActivationSpec
name|spec
parameter_list|,
name|MessageEndpointFactory
name|endpointFactory
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|InboundEndpoint
argument_list|>
name|endpoints
parameter_list|)
block|{
name|this
operator|.
name|spec
operator|=
name|spec
expr_stmt|;
name|this
operator|.
name|endpointFactory
operator|=
name|endpointFactory
expr_stmt|;
name|this
operator|.
name|endpoints
operator|=
name|endpoints
expr_stmt|;
block|}
specifier|public
name|void
name|release
parameter_list|()
block|{
name|released
operator|=
literal|true
expr_stmt|;
block|}
comment|/**      * Performs the work      */
specifier|public
name|void
name|run
parameter_list|()
block|{
comment|// get message driven bean proxy
name|MessageEndpoint
name|endpoint
init|=
name|getMesssageEndpoint
argument_list|()
decl_stmt|;
if|if
condition|(
name|endpoint
operator|==
literal|null
condition|)
block|{
comment|// error has been logged.
return|return;
block|}
comment|// get class loader
name|ClassLoader
name|classLoader
init|=
name|endpoint
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
name|ClassLoader
name|savedClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|classLoader
argument_list|)
expr_stmt|;
name|activate
argument_list|(
name|endpoint
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|savedClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @param endpoint      * @param classLoader       */
specifier|private
name|void
name|activate
parameter_list|(
name|MessageEndpoint
name|endpoint
parameter_list|,
name|ClassLoader
name|classLoader
parameter_list|)
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|spec
operator|.
name|getServiceInterfaceClass
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|serviceClass
operator|=
name|Class
operator|.
name|forName
argument_list|(
name|spec
operator|.
name|getServiceInterfaceClass
argument_list|()
argument_list|,
literal|false
argument_list|,
name|classLoader
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Failed to activate service endpoint "
operator|+
name|spec
operator|.
name|getDisplayName
argument_list|()
operator|+
literal|" due to unable to endpoint listener."
argument_list|)
expr_stmt|;
return|return;
block|}
block|}
comment|// create server bean factory
name|ServerFactoryBean
name|factory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|serviceClass
operator|!=
literal|null
operator|&&
name|EndpointUtils
operator|.
name|hasWebServiceAnnotation
argument_list|(
name|serviceClass
argument_list|)
condition|)
block|{
name|factory
operator|=
operator|new
name|JaxWsServerFactoryBean
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|=
operator|new
name|ServerFactoryBean
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|serviceClass
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spec
operator|.
name|getWsdlLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setWsdlLocation
argument_list|(
name|spec
operator|.
name|getWsdlLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
if|if
condition|(
name|spec
operator|.
name|getBusConfigLocation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setBus
argument_list|(
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|(
name|classLoader
operator|.
name|getResource
argument_list|(
name|spec
operator|.
name|getBusConfigLocation
argument_list|()
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spec
operator|.
name|getEndpointName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setEndpointName
argument_list|(
name|QName
operator|.
name|valueOf
argument_list|(
name|spec
operator|.
name|getEndpointName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spec
operator|.
name|getSchemaLocations
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setSchemaLocations
argument_list|(
name|getListOfString
argument_list|(
name|spec
operator|.
name|getSchemaLocations
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|spec
operator|.
name|getServiceName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|factory
operator|.
name|setServiceName
argument_list|(
name|QName
operator|.
name|valueOf
argument_list|(
name|spec
operator|.
name|getServiceName
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|MDBInvoker
name|invoker
init|=
name|createInvoker
argument_list|(
name|endpoint
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setInvoker
argument_list|(
name|invoker
argument_list|)
expr_stmt|;
comment|// create and start the server
name|factory
operator|.
name|setStart
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|Server
name|server
init|=
name|factory
operator|.
name|create
argument_list|()
decl_stmt|;
comment|// save the server for clean up later
name|endpoints
operator|.
name|put
argument_list|(
name|spec
operator|.
name|getDisplayName
argument_list|()
argument_list|,
operator|new
name|InboundEndpoint
argument_list|(
name|server
argument_list|,
name|invoker
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param str      * @return      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|getListOfString
parameter_list|(
name|String
name|str
parameter_list|)
block|{
if|if
condition|(
name|str
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|str
operator|.
name|split
argument_list|(
literal|","
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * @param endpoint      * @return      */
specifier|private
name|MDBInvoker
name|createInvoker
parameter_list|(
name|MessageEndpoint
name|endpoint
parameter_list|)
block|{
name|MDBInvoker
name|answer
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|spec
operator|instanceof
name|DispatchMDBActivationSpec
condition|)
block|{
name|answer
operator|=
operator|new
name|DispatchMDBInvoker
argument_list|(
name|endpoint
argument_list|,
operator|(
operator|(
name|DispatchMDBActivationSpec
operator|)
name|spec
operator|)
operator|.
name|getTargetBeanJndiName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|answer
operator|=
operator|new
name|MDBInvoker
argument_list|(
name|endpoint
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
comment|/**      * Invokes endpoint factory to create message endpoint (event driven bean).      * It will retry if the event driven bean is not yet available.      */
specifier|private
name|MessageEndpoint
name|getMesssageEndpoint
parameter_list|()
block|{
name|MessageEndpoint
name|answer
init|=
literal|null
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|MAX_ATTEMPTS
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|released
condition|)
block|{
name|LOG
operator|.
name|warning
argument_list|(
literal|"CXF service activation has been stopped."
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
try|try
block|{
name|answer
operator|=
name|endpointFactory
operator|.
name|createEndpoint
argument_list|(
literal|null
argument_list|)
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|UnavailableException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Target endpoint activation in progress.  Will retry."
argument_list|)
expr_stmt|;
try|try
block|{
name|Thread
operator|.
name|sleep
argument_list|(
name|RETRY_SLEEP
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InterruptedException
name|e1
parameter_list|)
block|{
comment|// ignore
block|}
block|}
block|}
if|if
condition|(
name|answer
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|severe
argument_list|(
literal|"Failed to activate  service endpoint "
operator|+
name|spec
operator|.
name|getDisplayName
argument_list|()
operator|+
literal|" due to unable to endpoint listener."
argument_list|)
expr_stmt|;
block|}
return|return
name|answer
return|;
block|}
block|}
end_class

end_unit

