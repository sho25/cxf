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
name|MessageUtils
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
name|AddressingProperties
import|;
end_import

begin_comment
comment|/**  * Holder for utility methods relating to contexts.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|RMContextUtils
block|{
comment|/**      * Prevents instantiation.      */
specifier|protected
name|RMContextUtils
parameter_list|()
block|{     }
comment|/**      * @return a generated UUID      */
specifier|public
specifier|static
name|String
name|generateUUID
parameter_list|()
block|{
return|return
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
name|ContextUtils
operator|.
name|generateUUID
argument_list|()
return|;
block|}
comment|/**      * Determine if message is currently being processed on server side.      *       * @param message the current Message      * @return true if message is currently being processed on server side      */
specifier|public
specifier|static
name|boolean
name|isServerSide
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getDestination
argument_list|()
operator|!=
literal|null
return|;
block|}
comment|/**      * Checks if the action String belongs to an RM protocol message.      *       * @param action the action      * @return true if the action is not one of the RM protocol actions.      */
specifier|public
specifier|static
name|boolean
name|isRMProtocolMessage
parameter_list|(
name|String
name|action
parameter_list|)
block|{
return|return
name|RM10Constants
operator|.
name|ACTIONS
operator|.
name|contains
argument_list|(
name|action
argument_list|)
operator|||
name|RM11Constants
operator|.
name|ACTIONS
operator|.
name|contains
argument_list|(
name|action
argument_list|)
return|;
block|}
comment|/**      * Retrieve the RM properties from the current message.      *       * @param message the current message      * @param outbound true if the message direction is outbound      * @return the RM properties      */
specifier|public
specifier|static
name|RMProperties
name|retrieveRMProperties
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|outbound
parameter_list|)
block|{
if|if
condition|(
name|outbound
condition|)
block|{
return|return
operator|(
name|RMProperties
operator|)
name|message
operator|.
name|get
argument_list|(
name|getRMPropertiesKey
argument_list|(
literal|true
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
name|Message
name|m
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|MessageUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
condition|)
block|{
comment|// the in properties are only available on the in message
name|m
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInMessage
argument_list|()
expr_stmt|;
if|if
condition|(
literal|null
operator|==
name|m
condition|)
block|{
name|m
operator|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getInFaultMessage
argument_list|()
expr_stmt|;
block|}
block|}
else|else
block|{
name|m
operator|=
name|message
expr_stmt|;
block|}
if|if
condition|(
literal|null
operator|!=
name|m
condition|)
block|{
return|return
operator|(
name|RMProperties
operator|)
name|m
operator|.
name|get
argument_list|(
name|getRMPropertiesKey
argument_list|(
literal|false
argument_list|)
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Store the RM properties in the current message.      *       * @param message the current message      * @param rmps the RM properties      * @param outbound if the message direction is outbound      */
specifier|public
specifier|static
name|void
name|storeRMProperties
parameter_list|(
name|Message
name|message
parameter_list|,
name|RMProperties
name|rmps
parameter_list|,
name|boolean
name|outbound
parameter_list|)
block|{
name|String
name|key
init|=
name|getRMPropertiesKey
argument_list|(
name|outbound
argument_list|)
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|rmps
argument_list|)
expr_stmt|;
block|}
comment|/**      * Retrieves the addressing properties from the current message.      *       * @param message the current message      * @param isProviderContext true if the binding provider request context      *            available to the client application as opposed to the message      *            context visible to handlers      * @param isOutbound true if the message is outbound      * @return the current addressing properties      */
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
name|ContextUtils
operator|.
name|retrieveMAPs
argument_list|(
name|message
argument_list|,
name|isProviderContext
argument_list|,
name|isOutbound
argument_list|)
return|;
block|}
comment|/**      * Store MAPs in the message.      *       * @param maps the MAPs to store      * @param message the current message      * @param isOutbound true if the message is outbound      * @param isRequestor true if the current messaging role is that of      *            requestor      * @param handler true if HANDLER scope, APPLICATION scope otherwise      */
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
name|ContextUtils
operator|.
name|storeMAPs
argument_list|(
name|maps
argument_list|,
name|message
argument_list|,
name|isOutbound
argument_list|,
name|isRequestor
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|String
name|getRMPropertiesKey
parameter_list|(
name|boolean
name|outbound
parameter_list|)
block|{
return|return
name|outbound
condition|?
name|RMMessageConstants
operator|.
name|RM_PROPERTIES_OUTBOUND
else|:
name|RMMessageConstants
operator|.
name|RM_PROPERTIES_INBOUND
return|;
block|}
comment|//TODO put this key to the constant
specifier|public
specifier|static
name|ProtocolVariation
name|getProtocolVariation
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
operator|(
name|ProtocolVariation
operator|)
name|message
operator|.
name|get
argument_list|(
name|RMMessageConstants
operator|.
name|RM_PROTOCOL_VARIATION
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|setProtocolVariation
parameter_list|(
name|Message
name|message
parameter_list|,
name|ProtocolVariation
name|protocol
parameter_list|)
block|{
name|message
operator|.
name|put
argument_list|(
name|RMMessageConstants
operator|.
name|RM_PROTOCOL_VARIATION
argument_list|,
name|protocol
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

