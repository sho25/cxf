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
name|http
operator|.
name|netty
operator|.
name|server
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
name|util
operator|.
name|ArrayList
import|;
end_import

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
name|Collection
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
name|List
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
name|AbstractTransportFactory
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
name|AbstractHTTPDestination
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
name|DestinationRegistry
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
name|DestinationRegistryImpl
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
name|HttpDestinationFactory
import|;
end_import

begin_class
specifier|public
class|class
name|NettyHttpTransportFactory
extends|extends
name|AbstractTransportFactory
implements|implements
name|DestinationFactory
block|{
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_NAMESPACES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"http://cxf.apache.org/transports/http/netty/server"
argument_list|)
decl_stmt|;
comment|/**      * This constant holds the prefixes served by this factory.      */
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|URI_PREFIXES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
static|static
block|{
name|URI_PREFIXES
operator|.
name|add
argument_list|(
literal|"netty://"
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|final
name|DestinationRegistry
name|registry
decl_stmt|;
specifier|protected
specifier|final
name|HttpDestinationFactory
name|factory
init|=
operator|new
name|NettyHttpDestinationFactory
argument_list|()
decl_stmt|;
specifier|public
name|NettyHttpTransportFactory
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|DestinationRegistryImpl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|NettyHttpTransportFactory
parameter_list|(
name|DestinationRegistry
name|registry
parameter_list|)
block|{
name|super
argument_list|(
name|DEFAULT_NAMESPACES
argument_list|)
expr_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
condition|)
block|{
name|registry
operator|=
operator|new
name|DestinationRegistryImpl
argument_list|()
expr_stmt|;
block|}
name|this
operator|.
name|registry
operator|=
name|registry
expr_stmt|;
block|}
specifier|public
name|DestinationRegistry
name|getRegistry
parameter_list|()
block|{
return|return
name|registry
return|;
block|}
comment|/**      * This call is used by CXF ExtensionManager to inject the activationNamespaces      * @param ans The transport ids.      */
specifier|public
name|void
name|setActivationNamespaces
parameter_list|(
name|Collection
argument_list|<
name|String
argument_list|>
name|ans
parameter_list|)
block|{
name|setTransportIds
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|ans
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getUriPrefixes
parameter_list|()
block|{
return|return
name|URI_PREFIXES
return|;
block|}
comment|/**      * This call uses the Configurer from the bus to configure      * a bean.      *      * @param bean      */
specifier|protected
name|void
name|configure
parameter_list|(
name|Bus
name|b
parameter_list|,
name|Object
name|bean
parameter_list|)
block|{
name|configure
argument_list|(
name|b
argument_list|,
name|bean
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|configure
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Object
name|bean
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|extraName
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
name|bean
argument_list|)
expr_stmt|;
if|if
condition|(
name|extraName
operator|!=
literal|null
condition|)
block|{
name|configurer
operator|.
name|configureBean
argument_list|(
name|extraName
argument_list|,
name|bean
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|protected
name|String
name|getAddress
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
name|String
name|address
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|.
name|startsWith
argument_list|(
literal|"netty://"
argument_list|)
condition|)
block|{
name|address
operator|=
name|address
operator|.
name|substring
argument_list|(
literal|8
argument_list|)
expr_stmt|;
block|}
return|return
name|address
return|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|endpointInfo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"EndpointInfo cannot be null"
argument_list|)
throw|;
block|}
synchronized|synchronized
init|(
name|registry
init|)
block|{
name|AbstractHTTPDestination
name|d
init|=
name|registry
operator|.
name|getDestinationForPath
argument_list|(
name|endpointInfo
operator|.
name|getAddress
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|d
operator|=
name|factory
operator|.
name|createDestination
argument_list|(
name|endpointInfo
argument_list|,
name|bus
argument_list|,
name|registry
argument_list|)
expr_stmt|;
name|registry
operator|.
name|addDestination
argument_list|(
name|d
argument_list|)
expr_stmt|;
name|configure
argument_list|(
name|bus
argument_list|,
name|d
argument_list|)
expr_stmt|;
name|d
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
block|}
return|return
name|d
return|;
block|}
block|}
block|}
end_class

end_unit

