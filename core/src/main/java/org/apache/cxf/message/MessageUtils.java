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
name|message
package|;
end_package

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|util
operator|.
name|PropertyUtils
import|;
end_import

begin_comment
comment|/**  * Holder for utility methods relating to messages.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|MessageUtils
block|{
comment|/**      * Prevents instantiation.      */
specifier|private
name|MessageUtils
parameter_list|()
block|{     }
comment|/**      * Determine if message is outbound.      *       * @param message the current Message      * @return true if the message direction is outbound      */
specifier|public
specifier|static
name|boolean
name|isOutbound
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Exchange
name|exchange
init|=
name|message
operator|.
name|getExchange
argument_list|()
decl_stmt|;
return|return
name|exchange
operator|!=
literal|null
operator|&&
operator|(
name|message
operator|==
name|exchange
operator|.
name|getOutMessage
argument_list|()
operator|||
name|message
operator|==
name|exchange
operator|.
name|getOutFaultMessage
argument_list|()
operator|)
return|;
block|}
comment|/**      * Determine if message is fault.      *       * @param message the current Message      * @return true if the message is a fault      */
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
comment|/**      * Determine the fault mode for the underlying (fault) message       * (for use on server side only).      *       * @param message the fault message      * @return the FaultMode      */
specifier|public
specifier|static
name|FaultMode
name|getFaultMode
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
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
name|message
operator|==
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getOutFaultMessage
argument_list|()
condition|)
block|{
name|FaultMode
name|mode
init|=
name|message
operator|.
name|get
argument_list|(
name|FaultMode
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
literal|null
operator|!=
name|mode
condition|)
block|{
return|return
name|mode
return|;
block|}
else|else
block|{
return|return
name|FaultMode
operator|.
name|RUNTIME_FAULT
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
comment|/**      * Determine if current messaging role is that of requestor.      *       * @param message the current Message      * @return true if the current messaging role is that of requestor      */
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
name|Message
operator|.
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
comment|/**      * Determine if the current message is a partial response.      *       * @param message the current message      * @return true if the current messags is a partial response      */
specifier|public
specifier|static
name|boolean
name|isPartialResponse
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PARTIAL_RESPONSE_MESSAGE
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Determines if the current message is an empty partial response, which      * is a partial response with an empty content.      *       * @param message the current message      * @return true if the current messags is a partial empty response      */
specifier|public
specifier|static
name|boolean
name|isEmptyPartialResponse
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|Message
operator|.
name|EMPTY_PARTIAL_RESPONSE_MESSAGE
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Returns true if a value is either the String "true" (regardless of case)  or Boolean.TRUE.      * @param value      * @return true if value is either the String "true" or Boolean.TRUE      */
specifier|public
specifier|static
name|boolean
name|isTrue
parameter_list|(
name|Object
name|value
parameter_list|)
block|{
comment|// TODO - consider deprecation as this really belongs in PropertyUtils
return|return
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|value
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|getContextualBoolean
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|key
parameter_list|,
name|boolean
name|defaultValue
parameter_list|)
block|{
if|if
condition|(
name|m
operator|!=
literal|null
condition|)
block|{
name|Object
name|o
init|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|o
operator|!=
literal|null
condition|)
block|{
return|return
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|o
argument_list|)
return|;
block|}
block|}
return|return
name|defaultValue
return|;
block|}
specifier|public
specifier|static
name|Object
name|getContextualProperty
parameter_list|(
name|Message
name|m
parameter_list|,
name|String
name|propPreferred
parameter_list|,
name|String
name|propDefault
parameter_list|)
block|{
name|Object
name|prop
init|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|propPreferred
argument_list|)
decl_stmt|;
if|if
condition|(
name|prop
operator|==
literal|null
operator|&&
name|propDefault
operator|!=
literal|null
condition|)
block|{
name|prop
operator|=
name|m
operator|.
name|getContextualProperty
argument_list|(
name|propDefault
argument_list|)
expr_stmt|;
block|}
return|return
name|prop
return|;
block|}
comment|/**      * Returns true if the underlying content format is a W3C DOM or a SAAJ message.      */
specifier|public
specifier|static
name|boolean
name|isDOMPresent
parameter_list|(
name|Message
name|m
parameter_list|)
block|{
return|return
name|m
operator|.
name|getContent
argument_list|(
name|Node
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
comment|/*         for (Class c : m.getContentFormats()) {             if (c.equals(Node.class) || c.getName().equals("javax.xml.soap.SOAPMessage")) {                 return true;             }            }         return false;         */
block|}
block|}
end_class

end_unit

