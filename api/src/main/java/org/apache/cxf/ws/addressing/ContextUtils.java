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
name|addressing
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
name|io
operator|.
name|InputStream
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
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|UUID
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
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
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
name|jaxb
operator|.
name|JAXBContextCache
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
name|jaxb
operator|.
name|JAXBContextCache
operator|.
name|CachedContextAndSchemas
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
name|message
operator|.
name|Exchange
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
name|message
operator|.
name|MessageImpl
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
name|MessageObserver
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|ASYNC_POST_RESPONSE_DISPATCH
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
operator|.
name|REQUESTOR_ROLE
import|;
end_import

begin_import
import|import static
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
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES
import|;
end_import

begin_import
import|import static
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
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES_INBOUND
import|;
end_import

begin_import
import|import static
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
name|JAXWSAConstants
operator|.
name|CLIENT_ADDRESSING_PROPERTIES_OUTBOUND
import|;
end_import

begin_import
import|import static
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
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
import|;
end_import

begin_import
import|import static
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
name|JAXWSAConstants
operator|.
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
import|;
end_import

begin_comment
comment|/**  * Holder for utility methods relating to contexts.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ContextUtils
block|{
specifier|public
specifier|static
specifier|final
name|ObjectFactory
name|WSA_OBJECT_FACTORY
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ACTION
init|=
name|ContextUtils
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|".ACTION"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|EndpointReferenceType
name|NONE_ENDPOINT_REFERENCE
init|=
name|EndpointReferenceUtils
operator|.
name|getEndpointReference
argument_list|(
name|Names
operator|.
name|WSA_NONE_ADDRESS
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
name|ContextUtils
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Used to fabricate a Uniform Resource Name from a UUID string      */
specifier|private
specifier|static
specifier|final
name|String
name|URN_UUID
init|=
literal|"urn:uuid:"
decl_stmt|;
specifier|private
specifier|static
name|JAXBContext
name|jaxbContext
decl_stmt|;
specifier|private
specifier|static
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|jaxbContextClasses
decl_stmt|;
comment|/**      * Used by MAPAggregator to cache bad MAP fault name      */
specifier|private
specifier|static
specifier|final
name|String
name|MAP_FAULT_NAME_PROPERTY
init|=
literal|"org.apache.cxf.ws.addressing.map.fault.name"
decl_stmt|;
comment|/**      * Used by MAPAggregator to cache bad MAP fault reason      */
specifier|private
specifier|static
specifier|final
name|String
name|MAP_FAULT_REASON_PROPERTY
init|=
literal|"org.apache.cxf.ws.addressing.map.fault.reason"
decl_stmt|;
comment|/**      * Indicates a partial response has already been sent      */
specifier|private
specifier|static
specifier|final
name|String
name|PARTIAL_RESPONSE_SENT_PROPERTY
init|=
literal|"org.apache.cxf.ws.addressing.partial.response.sent"
decl_stmt|;
comment|/**     * Prevents instantiation.     */
specifier|private
name|ContextUtils
parameter_list|()
block|{     }
comment|/**     * Determine if message is outbound.     *     * @param message the current Message     * @return true iff the message direction is outbound     */
specifier|public
specifier|static
name|boolean
name|isOutbound
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|message
operator|==
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutMessage
argument_list|()
operator|||
name|message
operator|==
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutFaultMessage
argument_list|()
operator|)
return|;
block|}
comment|/**     * Determine if message is fault.     *     * @param message the current Message     * @return true iff the message is a fault     */
specifier|public
specifier|static
name|boolean
name|isFault
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
operator|&&
operator|(
name|message
operator|==
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInFaultMessage
argument_list|()
operator|||
name|message
operator|==
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutFaultMessage
argument_list|()
operator|)
return|;
block|}
comment|/**     * Determine if current messaging role is that of requestor.     *     * @param message the current Message     * @return true if the current messaging role is that of requestor     */
specifier|public
specifier|static
name|boolean
name|isRequestor
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Boolean
name|requestor
init|=
operator|(
name|Boolean
operator|)
name|message
operator|.
name|get
argument_list|(
name|REQUESTOR_ROLE
argument_list|)
decl_stmt|;
return|return
name|requestor
operator|!=
literal|null
operator|&&
name|requestor
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|/**      * Get appropriate property name for message addressing properties.      *      * @param isProviderContext true if the binding provider request context       * available to the client application as opposed to the message context       * visible to handlers      * @param isRequestor true if the current messaging role is that of      * requestor      * @param isOutbound true if the message is outbound      * @return the property name to use when caching the MAPs in the context      */
specifier|public
specifier|static
name|String
name|getMAPProperty
parameter_list|(
name|boolean
name|isRequestor
parameter_list|,
name|boolean
name|isProviderContext
parameter_list|,
name|boolean
name|isOutbound
parameter_list|)
block|{
return|return
name|isRequestor
condition|?
name|isProviderContext
condition|?
name|CLIENT_ADDRESSING_PROPERTIES
else|:
name|isOutbound
condition|?
name|CLIENT_ADDRESSING_PROPERTIES_OUTBOUND
else|:
name|CLIENT_ADDRESSING_PROPERTIES_INBOUND
else|:
name|isOutbound
condition|?
name|SERVER_ADDRESSING_PROPERTIES_OUTBOUND
else|:
name|SERVER_ADDRESSING_PROPERTIES_INBOUND
return|;
block|}
comment|/**      * Store MAPs in the message.      *      * @param message the current message      * @param isOutbound true if the message is outbound      */
specifier|public
specifier|static
name|void
name|storeMAPs
parameter_list|(
name|AddressingProperties
name|maps
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|isOutbound
parameter_list|)
block|{
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
name|isOutbound
argument_list|,
name|isRequestor
argument_list|(
name|message
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Store MAPs in the message.      *      * @param maps the MAPs to store      * @param message the current message      * @param isOutbound true if the message is outbound      * @param isRequestor true if the current messaging role is that of requestor      */
specifier|public
specifier|static
name|void
name|storeMAPs
parameter_list|(
name|AddressingProperties
name|maps
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|isOutbound
parameter_list|,
name|boolean
name|isRequestor
parameter_list|)
block|{
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
name|isOutbound
argument_list|,
name|isRequestor
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
comment|/**      * Store MAPs in the message.      *      * @param maps the MAPs to store      * @param message the current message      * @param isOutbound true if the message is outbound      * @param isRequestor true if the current messaging role is that of requestor      * @param isProviderContext true if the binding provider request context       */
specifier|public
specifier|static
name|void
name|storeMAPs
parameter_list|(
name|AddressingProperties
name|maps
parameter_list|,
name|Message
name|message
parameter_list|,
name|boolean
name|isOutbound
parameter_list|,
name|boolean
name|isRequestor
parameter_list|,
name|boolean
name|isProviderContext
parameter_list|)
block|{
if|if
condition|(
name|maps
operator|!=
literal|null
condition|)
block|{
name|String
name|mapProperty
init|=
name|getMAPProperty
argument_list|(
name|isRequestor
argument_list|,
name|isProviderContext
argument_list|,
name|isOutbound
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"associating MAPs with context property {0}"
argument_list|,
name|mapProperty
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|mapProperty
argument_list|,
name|maps
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @param message the current message      * @param isProviderContext true if the binding provider request context      * available to the client application as opposed to the message context      * visible to handlers      * @param isOutbound true if the message is outbound      * @return the current addressing properties      */
specifier|public
specifier|static
name|AddressingProperties
name|retrieveMAPs
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|isProviderContext
parameter_list|,
name|boolean
name|isOutbound
parameter_list|)
block|{
return|return
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
name|isProviderContext
argument_list|,
name|isOutbound
argument_list|,
literal|true
argument_list|)
return|;
block|}
comment|/**      * @param message the current message      * @param isProviderContext true if the binding provider request context      * available to the client application as opposed to the message context      * visible to handlers      * @param isOutbound true if the message is outbound      * @param warnIfMissing log a warning  message if properties cannot be retrieved      * @return the current addressing properties      */
specifier|public
specifier|static
name|AddressingProperties
name|retrieveMAPs
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|isProviderContext
parameter_list|,
name|boolean
name|isOutbound
parameter_list|,
name|boolean
name|warnIfMissing
parameter_list|)
block|{
name|boolean
name|isRequestor
init|=
name|ContextUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|mapProperty
init|=
name|ContextUtils
operator|.
name|getMAPProperty
argument_list|(
name|isProviderContext
argument_list|,
name|isRequestor
argument_list|,
name|isOutbound
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"retrieving MAPs from context property {0}"
argument_list|,
name|mapProperty
argument_list|)
expr_stmt|;
name|AddressingProperties
name|maps
init|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|get
argument_list|(
name|mapProperty
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|==
literal|null
operator|&&
name|isOutbound
operator|&&
operator|!
name|isRequestor
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
operator|&&
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|maps
operator|=
operator|(
name|AddressingProperties
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
operator|.
name|get
argument_list|(
name|mapProperty
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|maps
operator|!=
literal|null
condition|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"current MAPs {0}"
argument_list|,
name|maps
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isProviderContext
condition|)
block|{
name|LogUtils
operator|.
name|log
argument_list|(
name|LOG
argument_list|,
name|warnIfMissing
condition|?
name|Level
operator|.
name|WARNING
else|:
name|Level
operator|.
name|FINE
argument_list|,
literal|"MAPS_RETRIEVAL_FAILURE_MSG"
argument_list|)
expr_stmt|;
block|}
return|return
name|maps
return|;
block|}
comment|/**      * Helper method to get an attributed URI.      *      * @param uri the URI      * @return an AttributedURIType encapsulating the URI      */
specifier|public
specifier|static
name|AttributedURIType
name|getAttributedURI
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|AttributedURIType
name|attributedURI
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createAttributedURIType
argument_list|()
decl_stmt|;
name|attributedURI
operator|.
name|setValue
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|attributedURI
return|;
block|}
comment|/**      * Helper method to get a RealtesTo instance.      *      * @param uri the related URI      * @return a RelatesToType encapsulating the URI      */
specifier|public
specifier|static
name|RelatesToType
name|getRelatesTo
parameter_list|(
name|String
name|uri
parameter_list|)
block|{
name|RelatesToType
name|relatesTo
init|=
name|WSA_OBJECT_FACTORY
operator|.
name|createRelatesToType
argument_list|()
decl_stmt|;
name|relatesTo
operator|.
name|setValue
argument_list|(
name|uri
argument_list|)
expr_stmt|;
return|return
name|relatesTo
return|;
block|}
comment|/**      * Helper method to determine if an EPR address is generic (either null,      * none or anonymous).      *      * @param ref the EPR under test      * @return true if the address is generic      */
specifier|public
specifier|static
name|boolean
name|isGenericAddress
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|==
literal|null
operator|||
name|ref
operator|.
name|getAddress
argument_list|()
operator|==
literal|null
operator|||
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
operator|||
name|Names
operator|.
name|WSA_NONE_ADDRESS
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Helper method to determine if an EPR address is anon (either null,      * anonymous).      *      * @param ref the EPR under test      * @return true if the address is generic      */
specifier|public
specifier|static
name|boolean
name|isAnonymousAddress
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|==
literal|null
operator|||
name|ref
operator|.
name|getAddress
argument_list|()
operator|==
literal|null
operator|||
name|Names
operator|.
name|WSA_ANONYMOUS_ADDRESS
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Helper method to determine if an EPR address is none.      *      * @param ref the EPR under test      * @return true if the address is generic      */
specifier|public
specifier|static
name|boolean
name|isNoneAddress
parameter_list|(
name|EndpointReferenceType
name|ref
parameter_list|)
block|{
return|return
name|ref
operator|!=
literal|null
operator|&&
name|ref
operator|.
name|getAddress
argument_list|()
operator|!=
literal|null
operator|&&
name|Names
operator|.
name|WSA_NONE_ADDRESS
operator|.
name|equals
argument_list|(
name|ref
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Helper method to determine if an MAPs Action is empty (a null action      * is considered empty, whereas a zero length action suppresses      * the propagation of the Action property).      *      * @param maps the MAPs Action under test      * @return true if the Action is empty      */
specifier|public
specifier|static
name|boolean
name|hasEmptyAction
parameter_list|(
name|AddressingProperties
name|maps
parameter_list|)
block|{
name|boolean
name|empty
init|=
name|maps
operator|.
name|getAction
argument_list|()
operator|==
literal|null
decl_stmt|;
if|if
condition|(
name|maps
operator|.
name|getAction
argument_list|()
operator|!=
literal|null
operator|&&
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|maps
operator|.
name|setAction
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|empty
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|empty
return|;
block|}
comment|/**      * Propagate inbound MAPs onto full reponse& fault messages.      *       * @param inMAPs the inbound MAPs      * @param exchange the current Exchange      */
specifier|public
specifier|static
name|void
name|propogateReceivedMAPs
parameter_list|(
name|AddressingProperties
name|inMAPs
parameter_list|,
name|Exchange
name|exchange
parameter_list|)
block|{
if|if
condition|(
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setOutMessage
argument_list|(
name|createMessage
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|propogateReceivedMAPs
argument_list|(
name|inMAPs
argument_list|,
name|exchange
operator|.
name|getOutMessage
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|exchange
operator|.
name|getOutFaultMessage
argument_list|()
operator|==
literal|null
condition|)
block|{
name|exchange
operator|.
name|setOutFaultMessage
argument_list|(
name|createMessage
argument_list|(
name|exchange
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|propogateReceivedMAPs
argument_list|(
name|inMAPs
argument_list|,
name|exchange
operator|.
name|getOutFaultMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * Propogate inbound MAPs onto reponse message if applicable      * (not applicable for oneways).      *       * @param inMAPs the inbound MAPs      * @param responseMessage      */
specifier|public
specifier|static
name|void
name|propogateReceivedMAPs
parameter_list|(
name|AddressingProperties
name|inMAPs
parameter_list|,
name|Message
name|responseMessage
parameter_list|)
block|{
if|if
condition|(
name|responseMessage
operator|!=
literal|null
condition|)
block|{
name|storeMAPs
argument_list|(
name|inMAPs
argument_list|,
name|responseMessage
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Store bad MAP fault name in the message.      *      * @param faultName the fault name to store      * @param message the current message      */
specifier|public
specifier|static
name|void
name|storeMAPFaultName
parameter_list|(
name|String
name|faultName
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|put
argument_list|(
name|MAP_FAULT_NAME_PROPERTY
argument_list|,
name|faultName
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retrieve MAP fault name from the message.      *      * @param message the current message      * @return the retrieved fault name      */
specifier|public
specifier|static
name|String
name|retrieveMAPFaultName
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|MAP_FAULT_NAME_PROPERTY
argument_list|)
return|;
block|}
comment|/**      * Store MAP fault reason in the message.      *      * @param reason the fault reason to store      * @param message the current message      */
specifier|public
specifier|static
name|void
name|storeMAPFaultReason
parameter_list|(
name|String
name|reason
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|put
argument_list|(
name|MAP_FAULT_REASON_PROPERTY
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retrieve MAP fault reason from the message.      *      * @param message the current message      * @return the retrieved fault reason      */
specifier|public
specifier|static
name|String
name|retrieveMAPFaultReason
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|(
name|String
operator|)
name|message
operator|.
name|get
argument_list|(
name|MAP_FAULT_REASON_PROPERTY
argument_list|)
return|;
block|}
comment|/**      * Store an indication that a partial response has been sent.      * Relavant if *both* the replyTo& faultTo are decoupled,      * and a fault occurs, then we would already have sent the      * partial response (pre-dispatch) for the replyTo, so      * no need to send again.      *      * @param message the current message      */
specifier|public
specifier|static
name|void
name|storePartialResponseSent
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|message
operator|.
name|put
argument_list|(
name|PARTIAL_RESPONSE_SENT_PROPERTY
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retrieve indication that a partial response has been sent.      *      * @param message the current message      * @return the retrieved indication that a partial response      * has been sent      */
specifier|public
specifier|static
name|boolean
name|retrievePartialResponseSent
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Boolean
name|ret
init|=
operator|(
name|Boolean
operator|)
name|message
operator|.
name|get
argument_list|(
name|PARTIAL_RESPONSE_SENT_PROPERTY
argument_list|)
decl_stmt|;
return|return
name|ret
operator|!=
literal|null
operator|&&
name|ret
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|/**      * Store indication that a deferred uncorrelated message abort is      * supported      *      * @param message the current message      */
specifier|public
specifier|static
name|void
name|storeDeferUncorrelatedMessageAbort
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
literal|"defer.uncorrelated.message.abort"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Retrieve indication that a deferred uncorrelated message abort is      * supported      *      * @param message the current message      * @return the retrieved indication       */
specifier|public
specifier|static
name|boolean
name|retrieveDeferUncorrelatedMessageAbort
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Boolean
name|ret
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|?
operator|(
name|Boolean
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
literal|"defer.uncorrelated.message.abort"
argument_list|)
else|:
literal|null
decl_stmt|;
return|return
name|ret
operator|!=
literal|null
operator|&&
name|ret
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|/**      * Store indication that a deferred uncorrelated message abort should      * occur      *      * @param message the current message      */
specifier|public
specifier|static
name|void
name|storeDeferredUncorrelatedMessageAbort
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
literal|"deferred.uncorrelated.message.abort"
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Retrieve indication that a deferred uncorrelated message abort should      * occur.      *      * @param message the current message      * @return the retrieved indication       */
specifier|public
specifier|static
name|boolean
name|retrieveDeferredUncorrelatedMessageAbort
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Boolean
name|ret
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|!=
literal|null
condition|?
operator|(
name|Boolean
operator|)
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
literal|"deferred.uncorrelated.message.abort"
argument_list|)
else|:
literal|null
decl_stmt|;
return|return
name|ret
operator|!=
literal|null
operator|&&
name|ret
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|/**      * Retrieve indication that an async post-response service invocation      * is required.      *       * @param message the current message      * @return the retrieved indication that an async post-response service      * invocation is required.      */
specifier|public
specifier|static
name|boolean
name|retrieveAsyncPostResponseDispatch
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Boolean
name|ret
init|=
operator|(
name|Boolean
operator|)
name|message
operator|.
name|get
argument_list|(
name|ASYNC_POST_RESPONSE_DISPATCH
argument_list|)
decl_stmt|;
return|return
name|ret
operator|!=
literal|null
operator|&&
name|ret
operator|.
name|booleanValue
argument_list|()
return|;
block|}
comment|/**      * Retrieve a JAXBContext for marshalling and unmarshalling JAXB generated      * types.      *      * @return a JAXBContext       */
specifier|public
specifier|static
name|JAXBContext
name|getJAXBContext
parameter_list|()
throws|throws
name|JAXBException
block|{
synchronized|synchronized
init|(
name|ContextUtils
operator|.
name|class
init|)
block|{
if|if
condition|(
name|jaxbContext
operator|==
literal|null
operator|||
name|jaxbContextClasses
operator|==
literal|null
condition|)
block|{
name|Set
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|tmp
init|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|JAXBContextCache
operator|.
name|addPackage
argument_list|(
name|tmp
argument_list|,
name|WSA_OBJECT_FACTORY
operator|.
name|getClass
argument_list|()
operator|.
name|getPackage
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|WSA_OBJECT_FACTORY
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|JAXBContextCache
operator|.
name|scanPackages
argument_list|(
name|tmp
argument_list|)
expr_stmt|;
name|CachedContextAndSchemas
name|ccs
init|=
name|JAXBContextCache
operator|.
name|getCachedContextAndSchemas
argument_list|(
name|tmp
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|jaxbContextClasses
operator|=
name|ccs
operator|.
name|getClasses
argument_list|()
expr_stmt|;
name|jaxbContext
operator|=
name|ccs
operator|.
name|getContext
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|jaxbContext
return|;
block|}
comment|/**      * Set the encapsulated JAXBContext (used by unit tests).      *       * @param ctx JAXBContext       */
specifier|public
specifier|static
name|void
name|setJAXBContext
parameter_list|(
name|JAXBContext
name|ctx
parameter_list|)
throws|throws
name|JAXBException
block|{
synchronized|synchronized
init|(
name|ContextUtils
operator|.
name|class
init|)
block|{
name|jaxbContext
operator|=
name|ctx
expr_stmt|;
name|jaxbContextClasses
operator|=
operator|new
name|HashSet
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @return a generated UUID      */
specifier|public
specifier|static
name|String
name|generateUUID
parameter_list|()
block|{
return|return
name|URN_UUID
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
return|;
block|}
comment|/**      * Retreive Conduit from Exchange if not already available      *       * @param conduit the current value for the Conduit      * @param message the current message      * @return the Conduit if available      */
specifier|public
specifier|static
name|Conduit
name|getConduit
parameter_list|(
name|Conduit
name|conduit
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
name|conduit
operator|==
literal|null
condition|)
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
name|conduit
operator|=
name|exchange
operator|!=
literal|null
condition|?
name|exchange
operator|.
name|getConduit
argument_list|(
name|message
argument_list|)
else|:
literal|null
expr_stmt|;
block|}
return|return
name|conduit
return|;
block|}
specifier|public
specifier|static
name|EndpointReferenceType
name|getNoneEndpointReference
parameter_list|()
block|{
return|return
name|NONE_ENDPOINT_REFERENCE
return|;
block|}
specifier|public
specifier|static
name|void
name|applyReferenceParam
parameter_list|(
name|EndpointReferenceType
name|toEpr
parameter_list|,
name|Object
name|el
parameter_list|)
block|{
if|if
condition|(
literal|null
operator|==
name|toEpr
operator|.
name|getReferenceParameters
argument_list|()
condition|)
block|{
name|toEpr
operator|.
name|setReferenceParameters
argument_list|(
name|WSA_OBJECT_FACTORY
operator|.
name|createReferenceParametersType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|toEpr
operator|.
name|getReferenceParameters
argument_list|()
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a Binding specific Message.      *       * @param exchange the current exchange      * @return the Method from the BindingOperationInfo      */
specifier|public
specifier|static
name|Message
name|createMessage
parameter_list|(
name|Exchange
name|exchange
parameter_list|)
block|{
name|Endpoint
name|ep
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|Message
name|msg
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ep
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|msg
operator|.
name|setExchange
argument_list|(
name|exchange
argument_list|)
expr_stmt|;
if|if
condition|(
name|ep
operator|.
name|getBinding
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|msg
operator|=
name|ep
operator|.
name|getBinding
argument_list|()
operator|.
name|createMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|msg
return|;
block|}
specifier|public
specifier|static
name|Destination
name|createDecoupledDestination
parameter_list|(
name|Exchange
name|exchange
parameter_list|,
specifier|final
name|EndpointReferenceType
name|reference
parameter_list|)
block|{
specifier|final
name|EndpointInfo
name|ei
init|=
name|exchange
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
return|return
operator|new
name|Destination
argument_list|()
block|{
specifier|public
name|EndpointReferenceType
name|getAddress
parameter_list|()
block|{
return|return
name|reference
return|;
block|}
specifier|public
name|Conduit
name|getBackChannel
parameter_list|(
name|Message
name|inMessage
parameter_list|)
throws|throws
name|IOException
block|{
name|Bus
name|bus
init|=
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Bus
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//this is a response targeting a decoupled endpoint.   Treat it as a oneway so
comment|//we don't wait for a response.
name|inMessage
operator|.
name|getExchange
argument_list|()
operator|.
name|setOneWay
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|ConduitInitiator
name|conduitInitiator
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|ConduitInitiatorManager
operator|.
name|class
argument_list|)
operator|.
name|getConduitInitiatorForUri
argument_list|(
name|reference
operator|.
name|getAddress
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|conduitInitiator
operator|!=
literal|null
condition|)
block|{
name|Conduit
name|c
init|=
name|conduitInitiator
operator|.
name|getConduit
argument_list|(
name|ei
argument_list|,
name|reference
argument_list|,
name|bus
argument_list|)
decl_stmt|;
comment|//ensure decoupled back channel input stream is closed
name|c
operator|.
name|setMessageObserver
argument_list|(
operator|new
name|MessageObserver
argument_list|()
block|{
specifier|public
name|void
name|onMessage
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
name|InputStream
name|is
init|=
name|m
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|is
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
block|}
block|}
argument_list|)
expr_stmt|;
return|return
name|c
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|shutdown
parameter_list|()
block|{             }
specifier|public
name|void
name|setMessageObserver
parameter_list|(
name|MessageObserver
name|observer
parameter_list|)
block|{             }
block|}
return|;
block|}
block|}
end_class

end_unit

