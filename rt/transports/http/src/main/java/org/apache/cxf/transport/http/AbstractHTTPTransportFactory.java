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
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|http
operator|.
name|HTTPAddress
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|wsdl
operator|.
name|extensions
operator|.
name|soap
operator|.
name|SOAPAddress
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
name|wsdl
operator|.
name|http
operator|.
name|AddressType
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
name|wsdl11
operator|.
name|WSDLEndpointFactory
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractHTTPTransportFactory
extends|extends
name|AbstractTransportFactory
implements|implements
name|WSDLEndpointFactory
block|{
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
name|AbstractHTTPTransportFactory
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
name|AbstractHTTPTransportFactory
parameter_list|(
name|DestinationRegistry
name|registry
parameter_list|)
block|{
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
name|HTTPAddress
condition|)
block|{
specifier|final
name|HTTPAddress
name|httpAdd
init|=
operator|(
name|HTTPAddress
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
name|getLocationURI
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
elseif|else
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
name|HttpAddressType
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
specifier|private
specifier|static
class|class
name|HttpAddressType
extends|extends
name|AddressType
implements|implements
name|HTTPAddress
implements|,
name|SOAPAddress
block|{
specifier|public
name|HttpAddressType
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|setElementType
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://schemas.xmlsoap.org/wsdl/soap/"
argument_list|,
literal|"address"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLocationURI
parameter_list|()
block|{
return|return
name|getLocation
argument_list|()
return|;
block|}
specifier|public
name|void
name|setLocationURI
parameter_list|(
name|String
name|locationURI
parameter_list|)
block|{
name|setLocation
argument_list|(
name|locationURI
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

