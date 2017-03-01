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
name|local
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
name|Arrays
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ConcurrentMap
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
name|Logger
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
name|helpers
operator|.
name|CastUtils
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
name|workqueue
operator|.
name|WorkQueueManager
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
name|AttributedURIType
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

begin_class
annotation|@
name|NoJSR250Annotations
specifier|public
class|class
name|LocalTransportFactory
extends|extends
name|AbstractTransportFactory
implements|implements
name|DestinationFactory
implements|,
name|ConduitInitiator
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TRANSPORT_ID
init|=
literal|"http://cxf.apache.org/transports/local"
decl_stmt|;
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
name|TRANSPORT_ID
argument_list|)
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_FILTER_PROPERTIES
init|=
name|LocalTransportFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".filterProperties"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MESSAGE_INCLUDE_PROPERTIES
init|=
name|LocalTransportFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".includeProperties"
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
name|LocalTransportFactory
operator|.
name|class
argument_list|)
decl_stmt|;
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
specifier|private
specifier|static
specifier|final
name|String
name|NULL_ADDRESS
init|=
name|LocalTransportFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".nulladdress"
decl_stmt|;
static|static
block|{
name|URI_PREFIXES
operator|.
name|add
argument_list|(
literal|"local://"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Destination
argument_list|>
name|destinations
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Destination
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|messageFilterProperties
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|messageIncludeProperties
decl_stmt|;
specifier|private
name|Set
argument_list|<
name|String
argument_list|>
name|uriPrefixes
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(
name|URI_PREFIXES
argument_list|)
decl_stmt|;
specifier|private
specifier|volatile
name|Executor
name|executor
decl_stmt|;
specifier|public
name|LocalTransportFactory
parameter_list|()
block|{
name|super
argument_list|(
name|DEFAULT_NAMESPACES
argument_list|)
expr_stmt|;
name|messageFilterProperties
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|messageIncludeProperties
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
name|messageFilterProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|REQUESTOR_ROLE
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|ENCODING
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|ACCEPT_CONTENT_TYPE
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|REQUEST_URI
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
expr_stmt|;
name|messageIncludeProperties
operator|.
name|add
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|getDestination
argument_list|(
name|ei
argument_list|,
name|createReference
argument_list|(
name|ei
argument_list|)
argument_list|,
name|bus
argument_list|)
return|;
block|}
specifier|protected
name|Destination
name|getDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|reference
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
block|{
name|Destination
name|d
init|=
literal|null
decl_stmt|;
name|String
name|addr
init|=
name|reference
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|addr
operator|==
literal|null
condition|)
block|{
name|AddressType
name|tp
init|=
name|ei
operator|.
name|getExtensor
argument_list|(
name|AddressType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|tp
operator|!=
literal|null
condition|)
block|{
name|addr
operator|=
name|tp
operator|.
name|getLocation
argument_list|()
expr_stmt|;
block|}
block|}
if|if
condition|(
name|addr
operator|==
literal|null
condition|)
block|{
name|addr
operator|=
name|NULL_ADDRESS
expr_stmt|;
block|}
name|d
operator|=
name|destinations
operator|.
name|get
argument_list|(
name|addr
argument_list|)
expr_stmt|;
if|if
condition|(
name|d
operator|==
literal|null
condition|)
block|{
name|d
operator|=
name|createDestination
argument_list|(
name|ei
argument_list|,
name|reference
argument_list|,
name|bus
argument_list|)
expr_stmt|;
name|Destination
name|tmpD
init|=
name|destinations
operator|.
name|putIfAbsent
argument_list|(
name|addr
argument_list|,
name|d
argument_list|)
decl_stmt|;
if|if
condition|(
name|tmpD
operator|!=
literal|null
condition|)
block|{
name|d
operator|=
name|tmpD
expr_stmt|;
block|}
block|}
return|return
name|d
return|;
block|}
specifier|private
name|Destination
name|createDestination
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|reference
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|LOG
operator|.
name|info
argument_list|(
literal|"Creating destination for address "
operator|+
name|reference
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|LocalDestination
argument_list|(
name|this
argument_list|,
name|reference
argument_list|,
name|ei
argument_list|,
name|bus
argument_list|)
return|;
block|}
name|void
name|remove
parameter_list|(
name|LocalDestination
name|destination
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Destination
argument_list|>
name|e
range|:
name|destinations
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|e
operator|.
name|getValue
argument_list|()
operator|==
name|destination
condition|)
block|{
name|destinations
operator|.
name|remove
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|Executor
name|getExecutor
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
if|if
condition|(
name|executor
operator|==
literal|null
operator|&&
name|bus
operator|!=
literal|null
condition|)
block|{
name|WorkQueueManager
name|manager
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|WorkQueueManager
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|manager
operator|!=
literal|null
condition|)
block|{
name|Executor
name|ex
init|=
name|manager
operator|.
name|getNamedWorkQueue
argument_list|(
literal|"local-transport"
argument_list|)
decl_stmt|;
if|if
condition|(
name|ex
operator|==
literal|null
condition|)
block|{
name|ex
operator|=
name|manager
operator|.
name|getAutomaticWorkQueue
argument_list|()
expr_stmt|;
block|}
return|return
name|ex
return|;
block|}
block|}
return|return
name|executor
return|;
block|}
specifier|public
name|void
name|setExecutor
parameter_list|(
name|Executor
name|executor
parameter_list|)
block|{
name|this
operator|.
name|executor
operator|=
name|executor
expr_stmt|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|LocalConduit
argument_list|(
name|this
argument_list|,
operator|(
name|LocalDestination
operator|)
name|getDestination
argument_list|(
name|ei
argument_list|,
name|bus
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|Conduit
name|getConduit
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|,
name|EndpointReferenceType
name|target
parameter_list|,
name|Bus
name|bus
parameter_list|)
throws|throws
name|IOException
block|{
return|return
operator|new
name|LocalConduit
argument_list|(
name|this
argument_list|,
operator|(
name|LocalDestination
operator|)
name|getDestination
argument_list|(
name|ei
argument_list|,
name|target
argument_list|,
name|bus
argument_list|)
argument_list|)
return|;
block|}
name|EndpointReferenceType
name|createReference
parameter_list|(
name|EndpointInfo
name|ei
parameter_list|)
block|{
name|EndpointReferenceType
name|epr
init|=
operator|new
name|EndpointReferenceType
argument_list|()
decl_stmt|;
name|AttributedURIType
name|address
init|=
operator|new
name|AttributedURIType
argument_list|()
decl_stmt|;
name|address
operator|.
name|setValue
argument_list|(
name|ei
operator|.
name|getAddress
argument_list|()
argument_list|)
expr_stmt|;
name|epr
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
return|return
name|epr
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
name|uriPrefixes
return|;
block|}
specifier|public
name|void
name|setUriPrefixes
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|s
parameter_list|)
block|{
name|uriPrefixes
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getMessageFilterProperties
parameter_list|()
block|{
return|return
name|messageFilterProperties
return|;
block|}
specifier|public
name|void
name|setMessageFilterProperties
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|props
parameter_list|)
block|{
name|this
operator|.
name|messageFilterProperties
operator|=
name|props
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|String
argument_list|>
name|getIncludeMessageProperties
parameter_list|()
block|{
return|return
name|messageIncludeProperties
return|;
block|}
specifier|public
name|void
name|setMessageIncludeProperties
parameter_list|(
name|Set
argument_list|<
name|String
argument_list|>
name|props
parameter_list|)
block|{
name|this
operator|.
name|messageIncludeProperties
operator|=
name|props
expr_stmt|;
block|}
specifier|public
name|void
name|copy
parameter_list|(
name|Message
name|message
parameter_list|,
name|Message
name|copy
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|filter
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|MESSAGE_FILTER_PROPERTIES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|filter
operator|==
literal|null
condition|)
block|{
name|filter
operator|=
name|messageFilterProperties
expr_stmt|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|includes
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Set
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|MESSAGE_INCLUDE_PROPERTIES
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|includes
operator|==
literal|null
condition|)
block|{
name|includes
operator|=
name|messageIncludeProperties
expr_stmt|;
block|}
comment|// copy all the contents
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|e
range|:
name|message
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
name|includes
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
operator|||
name|messageIncludeProperties
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
operator|)
operator|&&
operator|!
name|filter
operator|.
name|contains
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|copy
operator|.
name|put
argument_list|(
name|e
operator|.
name|getKey
argument_list|()
argument_list|,
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

