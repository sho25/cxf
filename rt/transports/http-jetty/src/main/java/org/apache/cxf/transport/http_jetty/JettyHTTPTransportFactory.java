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
name|http_jetty
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|Resource
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|imageio
operator|.
name|IIOException
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
name|Destination
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
name|DestinationFactory
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
name|http
operator|.
name|AbstractHTTPTransportFactory
import|;
end_import

begin_class
specifier|public
class|class
name|JettyHTTPTransportFactory
extends|extends
name|AbstractHTTPTransportFactory
implements|implements
name|DestinationFactory
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|JettyHTTPDestination
argument_list|>
name|destinations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|JettyHTTPDestination
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|JettyHTTPTransportFactory
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Resource
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|super
operator|.
name|setBus
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|finalizeConfig
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|bus
condition|)
block|{
return|return;
block|}
comment|// This call will register the server engine factory
comment|// with the Bus.
name|getJettyHTTPServerEngineFactory
argument_list|()
expr_stmt|;
block|}
comment|/**      * This method returns the Jetty HTTP Server Engine Factory.      */
specifier|protected
name|JettyHTTPServerEngineFactory
name|getJettyHTTPServerEngineFactory
parameter_list|()
block|{
comment|// We have got to *always* get this off the bus, because it may have
comment|// been overridden by Spring Configuration initially.
comment|// Spring Configuration puts it on the correct bus.
name|JettyHTTPServerEngineFactory
name|serverEngineFactory
init|=
name|getBus
argument_list|()
operator|.
name|getExtension
argument_list|(
name|JettyHTTPServerEngineFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// If it's not there, then create it and register it.
comment|// Spring may override it later, but we need it here for default
comment|// with no spring configuration.
if|if
condition|(
name|serverEngineFactory
operator|==
literal|null
condition|)
block|{
name|serverEngineFactory
operator|=
operator|new
name|JettyHTTPServerEngineFactory
argument_list|()
expr_stmt|;
name|serverEngineFactory
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|serverEngineFactory
return|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|addr
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|JettyHTTPDestination
name|destination
init|=
name|addr
operator|==
literal|null
condition|?
literal|null
else|:
name|destinations
operator|.
name|get
argument_list|(
name|addr
argument_list|)
decl_stmt|;
if|if
condition|(
name|destination
operator|==
literal|null
condition|)
block|{
name|destination
operator|=
name|createDestination
argument_list|(
name|endpointInfo
argument_list|)
expr_stmt|;
block|}
return|return
name|destination
return|;
block|}
specifier|private
specifier|synchronized
name|JettyHTTPDestination
name|createDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|addr
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
name|JettyHTTPDestination
name|destination
init|=
name|addr
operator|==
literal|null
condition|?
literal|null
else|:
name|destinations
operator|.
name|get
argument_list|(
name|addr
argument_list|)
decl_stmt|;
if|if
condition|(
name|destination
operator|==
literal|null
condition|)
block|{
name|destination
operator|=
operator|new
name|JettyHTTPDestination
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|this
argument_list|,
name|endpointInfo
argument_list|)
expr_stmt|;
name|destinations
operator|.
name|put
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|,
name|destination
argument_list|)
expr_stmt|;
name|configure
argument_list|(
name|destination
argument_list|)
expr_stmt|;
try|try
block|{
name|destination
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|IIOException
argument_list|(
literal|"JSSE Security Exception "
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
return|return
name|destination
return|;
block|}
comment|/**      * This function removes the destination for a particular endpoint.      */
name|void
name|removeDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|destinations
operator|.
name|remove
argument_list|(
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

