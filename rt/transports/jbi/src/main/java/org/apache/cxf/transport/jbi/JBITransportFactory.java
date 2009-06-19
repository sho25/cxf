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
name|jbi
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
name|Collection
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
name|Logger
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
name|jbi
operator|.
name|JBIException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jbi
operator|.
name|messaging
operator|.
name|DeliveryChannel
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
name|ConduitInitiatorManager
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
name|DestinationFactoryManager
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

begin_class
specifier|public
class|class
name|JBITransportFactory
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
name|String
name|TRANSPORT_ID
init|=
literal|"http://cxf.apache.org/transports/jbi"
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
name|JBITransportFactory
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|DeliveryChannel
name|deliveryChannel
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|JBIDestination
argument_list|>
name|destinationMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|JBIDestination
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Collection
argument_list|<
name|String
argument_list|>
name|activationNamespaces
decl_stmt|;
annotation|@
name|Resource
argument_list|(
name|name
operator|=
literal|"cxf"
argument_list|)
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
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
name|Collections
operator|.
name|singleton
argument_list|(
literal|"jbi"
argument_list|)
return|;
block|}
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
name|activationNamespaces
operator|=
name|ans
expr_stmt|;
block|}
annotation|@
name|PostConstruct
name|void
name|registerWithBindingManager
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
name|ConduitInitiatorManager
name|cim
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|cim
operator|&&
literal|null
operator|!=
name|activationNamespaces
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|activationNamespaces
control|)
block|{
name|cim
operator|.
name|registerConduitInitiator
argument_list|(
name|ns
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
name|DestinationFactoryManager
name|dfm
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|DestinationFactoryManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|dfm
operator|&&
literal|null
operator|!=
name|activationNamespaces
condition|)
block|{
for|for
control|(
name|String
name|ns
range|:
name|activationNamespaces
control|)
block|{
name|dfm
operator|.
name|registerDestinationFactory
argument_list|(
name|ns
argument_list|,
name|this
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|DeliveryChannel
name|getDeliveryChannel
parameter_list|()
block|{
return|return
name|deliveryChannel
return|;
block|}
specifier|public
name|void
name|setDeliveryChannel
parameter_list|(
name|DeliveryChannel
name|newDeliverychannel
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
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
literal|"CONFIG.DELIVERY.CHANNEL"
argument_list|,
name|LOG
argument_list|)
operator|.
name|toString
argument_list|()
operator|+
name|newDeliverychannel
argument_list|)
expr_stmt|;
name|deliveryChannel
operator|=
name|newDeliverychannel
expr_stmt|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|targetInfo
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getConduit
argument_list|(
name|targetInfo
argument_list|,
literal|null
argument_list|)
return|;
block|}
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
name|Conduit
name|conduit
init|=
operator|new
name|JBIConduit
argument_list|(
name|target
argument_list|,
name|getDeliveryChannel
argument_list|()
argument_list|)
decl_stmt|;
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
name|conduit
argument_list|)
expr_stmt|;
block|}
return|return
name|conduit
return|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
throws|throws
name|IOException
block|{
name|JBIDestination
name|destination
init|=
operator|new
name|JBIDestination
argument_list|(
name|ei
argument_list|,
name|JBIDispatcherUtil
operator|.
name|getInstance
argument_list|(
name|this
argument_list|,
name|getDeliveryChannel
argument_list|()
argument_list|)
argument_list|,
name|getDeliveryChannel
argument_list|()
argument_list|)
decl_stmt|;
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
name|destination
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|putDestination
argument_list|(
name|ei
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
name|ei
operator|.
name|getInterface
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JBIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|destination
return|;
block|}
specifier|public
name|void
name|putDestination
parameter_list|(
name|String
name|epName
parameter_list|,
name|JBIDestination
name|destination
parameter_list|)
throws|throws
name|JBIException
block|{
if|if
condition|(
name|destinationMap
operator|.
name|containsKey
argument_list|(
name|epName
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|JBIException
argument_list|(
literal|"JBIDestination for Endpoint "
operator|+
name|epName
operator|+
literal|" has already been created"
argument_list|)
throw|;
block|}
else|else
block|{
name|destinationMap
operator|.
name|put
argument_list|(
name|epName
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|JBIDestination
name|getDestination
parameter_list|(
name|String
name|epName
parameter_list|)
block|{
return|return
name|destinationMap
operator|.
name|get
argument_list|(
name|epName
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeDestination
parameter_list|(
name|String
name|epName
parameter_list|)
block|{
name|destinationMap
operator|.
name|remove
argument_list|(
name|epName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

