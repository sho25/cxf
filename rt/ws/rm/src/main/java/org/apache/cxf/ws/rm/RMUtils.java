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
name|ws
operator|.
name|rm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
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
name|endpoint
operator|.
name|Endpoint
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
name|management
operator|.
name|ManagementConstants
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
name|AddressingConstants
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|RMUtils
block|{
specifier|private
specifier|static
specifier|final
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200702
operator|.
name|ObjectFactory
name|WSRM_FACTORY
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|ObjectFactory
name|WSRM200502_FACTORY
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502wsa15
operator|.
name|ObjectFactory
name|WSRM200502_WSA200508_FACTORY
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|AddressingConstants
name|WSA_CONSTANTS
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Pattern
name|GENERATED_BUS_ID_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|Bus
operator|.
name|DEFAULT_BUS_ID
operator|+
literal|"\\d+$"
argument_list|)
decl_stmt|;
static|static
block|{
name|WSRM_FACTORY
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200702
operator|.
name|ObjectFactory
argument_list|()
expr_stmt|;
name|WSRM200502_FACTORY
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|ObjectFactory
argument_list|()
expr_stmt|;
name|WSRM200502_WSA200508_FACTORY
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502wsa15
operator|.
name|ObjectFactory
argument_list|()
expr_stmt|;
name|WSA_CONSTANTS
operator|=
operator|new
name|AddressingConstants
argument_list|()
expr_stmt|;
block|}
specifier|private
name|RMUtils
parameter_list|()
block|{     }
comment|/**      * Get the factory for the internal representation of WS-RM data (WS-ReliableMessaging 1.1).      *      * @return factory      */
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200702
operator|.
name|ObjectFactory
name|getWSRMFactory
parameter_list|()
block|{
return|return
name|WSRM_FACTORY
return|;
block|}
comment|/**      * Get the factory for WS-ReliableMessaging 1.0 using the standard 200408 WS-Addressing namespace.      *      * @return factory      */
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502
operator|.
name|ObjectFactory
name|getWSRM200502Factory
parameter_list|()
block|{
return|return
name|WSRM200502_FACTORY
return|;
block|}
comment|/**      * Get the factory for WS-ReliableMessaging 1.0 using the current 200508 WS-Addressing namespace.      *      * @return factory      */
specifier|public
specifier|static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|rm
operator|.
name|v200502wsa15
operator|.
name|ObjectFactory
name|getWSRM200502WSA200508Factory
parameter_list|()
block|{
return|return
name|WSRM200502_WSA200508_FACTORY
return|;
block|}
comment|/**      * Get the constants for a particular WS-ReliableMessaging namespace.      *      * @param uri      * @return constants      */
specifier|public
specifier|static
name|RMConstants
name|getConstants
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
if|if
condition|(
name|RM10Constants
operator|.
name|NAMESPACE_URI
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|RM10Constants
operator|.
name|INSTANCE
return|;
block|}
elseif|else
if|if
condition|(
name|RM11Constants
operator|.
name|NAMESPACE_URI
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return
name|RM11Constants
operator|.
name|INSTANCE
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|public
specifier|static
name|AddressingConstants
name|getAddressingConstants
parameter_list|()
block|{
return|return
name|WSA_CONSTANTS
return|;
block|}
specifier|public
specifier|static
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
name|createAnonymousReference
parameter_list|()
block|{
return|return
name|createReference
argument_list|(
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
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
argument_list|)
return|;
block|}
specifier|public
specifier|static
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
name|createNoneReference
parameter_list|()
block|{
return|return
name|createReference
argument_list|(
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
name|Names
operator|.
name|WSA_NONE_ADDRESS
argument_list|)
return|;
block|}
specifier|public
specifier|static
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
name|createReference
parameter_list|(
name|String
name|address
parameter_list|)
block|{
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
name|ObjectFactory
name|factory
init|=
operator|new
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
name|ObjectFactory
argument_list|()
decl_stmt|;
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
name|epr
init|=
name|factory
operator|.
name|createEndpointReferenceType
argument_list|()
decl_stmt|;
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
name|uri
init|=
name|factory
operator|.
name|createAttributedURIType
argument_list|()
decl_stmt|;
name|uri
operator|.
name|setValue
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|epr
operator|.
name|setAddress
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|epr
return|;
block|}
specifier|public
specifier|static
name|String
name|getEndpointIdentifier
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|)
block|{
return|return
name|getEndpointIdentifier
argument_list|(
name|endpoint
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getEndpointIdentifier
parameter_list|(
name|Endpoint
name|endpoint
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|String
name|busId
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|bus
operator|==
literal|null
condition|)
block|{
name|busId
operator|=
name|Bus
operator|.
name|DEFAULT_BUS_ID
expr_stmt|;
block|}
else|else
block|{
name|busId
operator|=
name|bus
operator|.
name|getId
argument_list|()
expr_stmt|;
comment|// bus-ids of form cxfnnn or artifactid-cxfnnn must drop the variable part nnn
name|Matcher
name|m
init|=
name|GENERATED_BUS_ID_PATTERN
operator|.
name|matcher
argument_list|(
name|busId
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|find
argument_list|()
condition|)
block|{
name|busId
operator|=
name|busId
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|m
operator|.
name|start
argument_list|()
operator|+
name|Bus
operator|.
name|DEFAULT_BUS_ID
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"."
operator|+
name|endpoint
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|busId
return|;
block|}
specifier|public
specifier|static
name|ObjectName
name|getManagedObjectName
parameter_list|(
name|RMManager
name|manager
parameter_list|)
throws|throws
name|JMException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|writeTypeProperty
argument_list|(
name|buffer
argument_list|,
name|manager
operator|.
name|getBus
argument_list|()
argument_list|,
literal|"WSRM.Manager"
argument_list|)
expr_stmt|;
comment|// Added the instance id to make the ObjectName unique
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|INSTANCE_ID_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|manager
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ObjectName
name|getManagedObjectName
parameter_list|(
name|RMEndpoint
name|endpoint
parameter_list|)
throws|throws
name|JMException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|writeTypeProperty
argument_list|(
name|buffer
argument_list|,
name|endpoint
operator|.
name|getManager
argument_list|()
operator|.
name|getBus
argument_list|()
argument_list|,
literal|"WSRM.Endpoint"
argument_list|)
expr_stmt|;
name|Endpoint
name|ep
init|=
name|endpoint
operator|.
name|getApplicationEndpoint
argument_list|()
decl_stmt|;
name|writeEndpointProperty
argument_list|(
name|buffer
argument_list|,
name|ep
argument_list|)
expr_stmt|;
return|return
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|ObjectName
name|getManagedObjectName
parameter_list|(
name|RMManager
name|manager
parameter_list|,
name|Endpoint
name|ep
parameter_list|)
throws|throws
name|JMException
block|{
name|StringBuilder
name|buffer
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|writeTypeProperty
argument_list|(
name|buffer
argument_list|,
name|manager
operator|.
name|getBus
argument_list|()
argument_list|,
literal|"WSRM.Endpoint"
argument_list|)
expr_stmt|;
name|writeEndpointProperty
argument_list|(
name|buffer
argument_list|,
name|ep
argument_list|)
expr_stmt|;
return|return
operator|new
name|ObjectName
argument_list|(
name|buffer
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|void
name|writeTypeProperty
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|Bus
name|bus
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|String
name|busId
init|=
name|bus
operator|.
name|getId
argument_list|()
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|DEFAULT_DOMAIN_NAME
argument_list|)
operator|.
name|append
argument_list|(
literal|':'
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|BUS_ID_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|busId
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|TYPE_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|writeEndpointProperty
parameter_list|(
name|StringBuilder
name|buffer
parameter_list|,
name|Endpoint
name|ep
parameter_list|)
block|{
name|String
name|serviceName
init|=
name|ObjectName
operator|.
name|quote
argument_list|(
name|ep
operator|.
name|getService
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|SERVICE_NAME_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|serviceName
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
name|String
name|endpointName
init|=
name|ObjectName
operator|.
name|quote
argument_list|(
name|ep
operator|.
name|getEndpointInfo
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|PORT_NAME_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|endpointName
argument_list|)
operator|.
name|append
argument_list|(
literal|','
argument_list|)
expr_stmt|;
comment|// Added the instance id to make the ObjectName unique
name|buffer
operator|.
name|append
argument_list|(
name|ManagementConstants
operator|.
name|INSTANCE_ID_PROP
argument_list|)
operator|.
name|append
argument_list|(
literal|'='
argument_list|)
operator|.
name|append
argument_list|(
name|ep
operator|.
name|hashCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Utility method to compare two (possibly-null) String values.      *      * @param aval      * @param bval      * @return<code>true</code> if equal,<code>false</code> if not      */
specifier|public
specifier|static
name|boolean
name|equalStrings
parameter_list|(
name|String
name|aval
parameter_list|,
name|String
name|bval
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|!=
name|aval
condition|)
block|{
return|return
name|aval
operator|.
name|equals
argument_list|(
name|bval
argument_list|)
return|;
block|}
return|return
literal|null
operator|==
name|bval
return|;
block|}
comment|/**      * Utility method to compare two (possibly-null) Long values.      *      * @param aval      * @param bval      * @return<code>true</code> if equal,<code>false</code> if not      */
specifier|public
specifier|static
name|boolean
name|equalLongs
parameter_list|(
name|Long
name|aval
parameter_list|,
name|Long
name|bval
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|!=
name|aval
condition|)
block|{
return|return
name|aval
operator|.
name|equals
argument_list|(
name|bval
argument_list|)
return|;
block|}
return|return
literal|null
operator|==
name|bval
return|;
block|}
block|}
end_class

end_unit

