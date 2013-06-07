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
name|Iterator
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
name|annotation
operator|.
name|Resource
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
name|common
operator|.
name|injection
operator|.
name|NoJSR250Annotations
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
name|service
operator|.
name|Service
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
name|BindingInfo
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|ConduitInitiator
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
name|servlet
operator|.
name|ServletDestinationFactory
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
name|ws
operator|.
name|addressing
operator|.
name|EndpointReferenceType
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
name|wsdl
operator|.
name|http
operator|.
name|AddressType
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|NoJSR250Annotations
argument_list|(
name|unlessNull
operator|=
literal|"bus"
argument_list|)
specifier|public
class|class
name|HTTPTransportFactory
extends|extends
name|AbstractTransportFactory
implements|implements
name|ConduitInitiator
implements|,
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
literal|"http://cxf.apache.org/transports/http"
argument_list|,
literal|"http://cxf.apache.org/transports/http/configuration"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http"
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|)
decl_stmt|;
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
name|HTTPTransportFactory
operator|.
name|class
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
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|URI_PREFIXES
operator|.
name|add
argument_list|(
literal|"http://"
argument_list|)
expr_stmt|;
name|URI_PREFIXES
operator|.
name|add
argument_list|(
literal|"https://"
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|final
name|DestinationRegistry
name|registry
decl_stmt|;
specifier|public
name|HTTPTransportFactory
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
name|HTTPTransportFactory
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|this
argument_list|(
name|b
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|HTTPTransportFactory
parameter_list|(
name|Bus
name|b
parameter_list|,
name|DestinationRegistry
name|registry
parameter_list|)
block|{
name|super
argument_list|(
name|DEFAULT_NAMESPACES
argument_list|,
literal|null
argument_list|)
expr_stmt|;
if|if
condition|(
name|registry
operator|==
literal|null
operator|&&
name|b
operator|!=
literal|null
condition|)
block|{
name|registry
operator|=
name|b
operator|.
name|getExtension
argument_list|(
name|DestinationRegistry
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
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
name|bus
operator|=
name|b
expr_stmt|;
name|register
argument_list|()
expr_stmt|;
block|}
specifier|public
name|HTTPTransportFactory
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
name|this
operator|.
name|registry
operator|=
name|registry
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
argument_list|<
name|String
argument_list|>
argument_list|(
name|ans
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|EndpointInfo
name|createEndpointInfo
parameter_list|(
name|ServiceInfo
name|serviceInfo
parameter_list|,
name|BindingInfo
name|b
parameter_list|,
name|List
argument_list|<
name|?
argument_list|>
name|ees
parameter_list|)
block|{
if|if
condition|(
name|ees
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
argument_list|<
name|?
argument_list|>
name|itr
init|=
name|ees
operator|.
name|iterator
argument_list|()
init|;
name|itr
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Object
name|extensor
init|=
name|itr
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|extensor
operator|instanceof
name|AddressType
condition|)
block|{
specifier|final
name|AddressType
name|httpAdd
init|=
operator|(
name|AddressType
operator|)
name|extensor
decl_stmt|;
name|EndpointInfo
name|info
init|=
operator|new
name|HttpEndpointInfo
argument_list|(
name|serviceInfo
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|)
decl_stmt|;
name|info
operator|.
name|setAddress
argument_list|(
name|httpAdd
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|info
operator|.
name|addExtensor
argument_list|(
name|httpAdd
argument_list|)
expr_stmt|;
return|return
name|info
return|;
block|}
block|}
block|}
name|HttpEndpointInfo
name|hei
init|=
operator|new
name|HttpEndpointInfo
argument_list|(
name|serviceInfo
argument_list|,
literal|"http://schemas.xmlsoap.org/wsdl/http/"
argument_list|)
decl_stmt|;
name|AddressType
name|at
init|=
operator|new
name|AddressType
argument_list|()
decl_stmt|;
name|hei
operator|.
name|addExtensor
argument_list|(
name|at
argument_list|)
expr_stmt|;
return|return
name|hei
return|;
block|}
specifier|public
name|void
name|createPortExtensors
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Service
name|service
parameter_list|)
block|{
comment|// TODO
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
comment|/**      * This call uses the Configurer from the bus to configure      * a bean.      *       * @param bean      */
specifier|protected
name|void
name|configure
parameter_list|(
name|Object
name|bean
parameter_list|)
block|{
name|configure
argument_list|(
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
specifier|private
specifier|static
class|class
name|HttpEndpointInfo
extends|extends
name|EndpointInfo
block|{
name|AddressType
name|saddress
decl_stmt|;
name|HttpEndpointInfo
parameter_list|(
name|ServiceInfo
name|serv
parameter_list|,
name|String
name|trans
parameter_list|)
block|{
name|super
argument_list|(
name|serv
argument_list|,
name|trans
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setAddress
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|super
operator|.
name|setAddress
argument_list|(
name|s
argument_list|)
expr_stmt|;
if|if
condition|(
name|saddress
operator|!=
literal|null
condition|)
block|{
name|saddress
operator|.
name|setLocation
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|addExtensor
parameter_list|(
name|Object
name|el
parameter_list|)
block|{
name|super
operator|.
name|addExtensor
argument_list|(
name|el
argument_list|)
expr_stmt|;
if|if
condition|(
name|el
operator|instanceof
name|AddressType
condition|)
block|{
name|saddress
operator|=
operator|(
name|AddressType
operator|)
name|el
expr_stmt|;
block|}
block|}
block|}
comment|/**      * This call creates a new HTTPConduit for the endpoint. It is equivalent      * to calling getConduit without an EndpointReferenceType.      */
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|endpointInfo
argument_list|,
name|endpointInfo
operator|.
name|getTarget
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * This call creates a new HTTP Conduit based on the EndpointInfo and      * EndpointReferenceType.      * TODO: What are the formal constraints on EndpointInfo and       * EndpointReferenceType values?      */
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|)
throws|throws
name|IOException
block|{
name|HTTPConduitFactory
name|factory
init|=
name|findFactory
argument_list|(
name|endpointInfo
argument_list|)
decl_stmt|;
name|HTTPConduit
name|conduit
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|factory
operator|!=
literal|null
condition|)
block|{
name|conduit
operator|=
name|factory
operator|.
name|createConduit
argument_list|(
name|this
argument_list|,
name|endpointInfo
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|conduit
operator|==
literal|null
condition|)
block|{
name|conduit
operator|=
operator|new
name|URLConnectionHTTPConduit
argument_list|(
name|bus
argument_list|,
name|endpointInfo
argument_list|,
name|target
argument_list|)
expr_stmt|;
block|}
comment|// Spring configure the conduit.
name|String
name|address
init|=
name|conduit
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|address
operator|!=
literal|null
operator|&&
name|address
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
operator|!=
operator|-
literal|1
condition|)
block|{
name|address
operator|=
name|address
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|address
operator|.
name|indexOf
argument_list|(
literal|'?'
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|HTTPConduitConfigurer
name|c1
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|HTTPConduitConfigurer
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|c1
operator|!=
literal|null
condition|)
block|{
name|c1
operator|.
name|configure
argument_list|(
name|conduit
operator|.
name|getBeanName
argument_list|()
argument_list|,
name|address
argument_list|,
name|conduit
argument_list|)
expr_stmt|;
block|}
name|configure
argument_list|(
name|conduit
argument_list|,
name|conduit
operator|.
name|getBeanName
argument_list|()
argument_list|,
name|address
argument_list|)
expr_stmt|;
name|conduit
operator|.
name|finalizeConfig
argument_list|()
expr_stmt|;
return|return
name|conduit
return|;
block|}
specifier|protected
name|HTTPConduitFactory
name|findFactory
parameter_list|(
name|EndpointInfo
name|endpointInfo
parameter_list|)
block|{
name|HTTPConduitFactory
name|f
init|=
name|endpointInfo
operator|.
name|getProperty
argument_list|(
name|HTTPConduitFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|HTTPConduitFactory
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|==
literal|null
condition|)
block|{
name|f
operator|=
name|bus
operator|.
name|getExtension
argument_list|(
name|HTTPConduitFactory
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
return|return
name|f
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
name|HttpDestinationFactory
name|jettyFactory
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|HttpDestinationFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|addr
init|=
name|endpointInfo
operator|.
name|getAddress
argument_list|()
decl_stmt|;
if|if
condition|(
name|jettyFactory
operator|==
literal|null
operator|&&
name|addr
operator|!=
literal|null
operator|&&
name|addr
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
condition|)
block|{
name|String
name|m
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_HTTP_DESTINATION_FACTORY_FOUND"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
name|m
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|IOException
argument_list|(
name|m
argument_list|)
throw|;
block|}
name|HttpDestinationFactory
name|factory
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|jettyFactory
operator|!=
literal|null
operator|&&
operator|(
name|addr
operator|==
literal|null
operator|||
name|addr
operator|.
name|startsWith
argument_list|(
literal|"http"
argument_list|)
operator|)
condition|)
block|{
name|factory
operator|=
name|jettyFactory
expr_stmt|;
block|}
else|else
block|{
name|factory
operator|=
operator|new
name|ServletDestinationFactory
argument_list|()
expr_stmt|;
block|}
name|d
operator|=
name|factory
operator|.
name|createDestination
argument_list|(
name|endpointInfo
argument_list|,
name|getBus
argument_list|()
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

