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
name|wsn
operator|.
name|services
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
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
name|Endpoint
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
name|soap
operator|.
name|SOAPBinding
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
name|spi
operator|.
name|Provider
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
name|wsaddressing
operator|.
name|W3CEndpointReference
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
name|wsn
operator|.
name|AbstractEndpoint
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
name|wsn
operator|.
name|EndpointManager
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
name|wsn
operator|.
name|EndpointRegistrationException
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
name|wsn
operator|.
name|util
operator|.
name|WSNHelper
import|;
end_import

begin_class
specifier|public
class|class
name|JaxwsEndpointManager
implements|implements
name|EndpointManager
block|{
specifier|protected
name|MBeanServer
name|mbeanServer
decl_stmt|;
specifier|public
name|void
name|setMBeanServer
parameter_list|(
name|MBeanServer
name|s
parameter_list|)
block|{
name|mbeanServer
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|Endpoint
name|register
parameter_list|(
name|String
name|address
parameter_list|,
name|Object
name|service
parameter_list|)
throws|throws
name|EndpointRegistrationException
block|{
name|ClassLoader
name|cl
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
if|if
condition|(
name|WSNHelper
operator|.
name|setClassLoader
argument_list|()
condition|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|JaxwsEndpointManager
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|bindingId
init|=
name|SOAPBinding
operator|.
name|SOAP11HTTP_BINDING
decl_stmt|;
if|if
condition|(
name|isCXF
argument_list|()
condition|)
block|{
name|bindingId
operator|=
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
expr_stmt|;
block|}
name|Endpoint
name|endpoint
init|=
name|Endpoint
operator|.
name|create
argument_list|(
name|bindingId
argument_list|,
name|service
argument_list|)
decl_stmt|;
name|endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
name|mbeanServer
operator|!=
literal|null
operator|&&
name|service
operator|instanceof
name|AbstractEndpoint
condition|)
block|{
name|ObjectName
name|on
init|=
operator|(
operator|(
name|AbstractEndpoint
operator|)
name|service
operator|)
operator|.
name|getMBeanName
argument_list|()
decl_stmt|;
if|if
condition|(
name|on
operator|!=
literal|null
condition|)
block|{
name|mbeanServer
operator|.
name|registerMBean
argument_list|(
name|service
argument_list|,
name|on
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore for now
block|}
return|return
name|endpoint
return|;
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
name|cl
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|isCXF
parameter_list|()
block|{
return|return
name|Provider
operator|.
name|provider
argument_list|()
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|contains
argument_list|(
literal|".cxf"
argument_list|)
return|;
block|}
specifier|public
name|void
name|unregister
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Object
name|service
parameter_list|)
throws|throws
name|EndpointRegistrationException
block|{
try|try
block|{
if|if
condition|(
name|mbeanServer
operator|!=
literal|null
operator|&&
name|service
operator|instanceof
name|AbstractEndpoint
condition|)
block|{
name|ObjectName
name|on
init|=
operator|(
operator|(
name|AbstractEndpoint
operator|)
name|service
operator|)
operator|.
name|getMBeanName
argument_list|()
decl_stmt|;
if|if
condition|(
name|on
operator|!=
literal|null
condition|)
block|{
name|mbeanServer
operator|.
name|unregisterMBean
argument_list|(
name|on
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|//ignore for now
block|}
name|endpoint
operator|.
name|stop
argument_list|()
expr_stmt|;
block|}
specifier|public
name|W3CEndpointReference
name|getEpr
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|endpoint
operator|.
name|getEndpointReference
argument_list|(
name|W3CEndpointReference
operator|.
name|class
argument_list|)
return|;
block|}
block|}
end_class

end_unit

