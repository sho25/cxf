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
name|eventing
operator|.
name|shared
operator|.
name|handlers
package|;
end_package

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
name|xml
operator|.
name|namespace
operator|.
name|QName
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|MessageContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|soap
operator|.
name|SOAPHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|handler
operator|.
name|soap
operator|.
name|SOAPMessageContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|DOMUtils
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
name|eventing
operator|.
name|shared
operator|.
name|EventingConstants
import|;
end_import

begin_comment
comment|/**  * Subscription reference parsing handler is a SOAP handler on the Subscription Manager's side  * which takes care of parsing the reference parameters and retrieving the subscription  * ID from SOAP headers before the message is passed to the Subscription Manager itself.  * In handleMessage method, it is supposed to retrieve the UUID of the subscription and  * save it into the SOAPMessageContext as a String with the key 'uuid'  */
end_comment

begin_class
specifier|public
class|class
name|SubscriptionReferenceParsingHandler
implements|implements
name|SOAPHandler
argument_list|<
name|SOAPMessageContext
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|SubscriptionReferenceParsingHandler
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|String
name|namespace
decl_stmt|;
specifier|private
specifier|final
name|String
name|elementName
decl_stmt|;
specifier|public
name|SubscriptionReferenceParsingHandler
parameter_list|(
name|String
name|namespace
parameter_list|,
name|String
name|elementName
parameter_list|)
block|{
name|this
operator|.
name|namespace
operator|=
name|namespace
expr_stmt|;
name|this
operator|.
name|elementName
operator|=
name|elementName
expr_stmt|;
block|}
specifier|public
name|SubscriptionReferenceParsingHandler
parameter_list|()
block|{
name|this
operator|.
name|namespace
operator|=
name|EventingConstants
operator|.
name|SUBSCRIPTION_ID_DEFAULT_NAMESPACE
expr_stmt|;
name|this
operator|.
name|elementName
operator|=
name|EventingConstants
operator|.
name|SUBSCRIPTION_ID_DEFAULT_ELEMENT_NAME
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|handleMessage
parameter_list|(
name|SOAPMessageContext
name|context
parameter_list|)
block|{
comment|// we are interested only in inbound messages here
if|if
condition|(
operator|(
name|Boolean
operator|)
name|context
operator|.
name|get
argument_list|(
name|MessageContext
operator|.
name|MESSAGE_OUTBOUND_PROPERTY
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
try|try
block|{
comment|// read headers
name|LOG
operator|.
name|finer
argument_list|(
literal|"Examining header elements"
argument_list|)
expr_stmt|;
name|Element
name|el
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|context
operator|.
name|getMessage
argument_list|()
operator|.
name|getSOAPHeader
argument_list|()
argument_list|)
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|el
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
operator|&&
name|el
operator|.
name|getLocalName
argument_list|()
operator|.
name|equals
argument_list|(
name|elementName
argument_list|)
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
literal|"found UUID parameter in header, uuid={0}"
argument_list|,
name|el
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
literal|"uuid"
argument_list|,
name|el
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|el
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|SOAPException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|handleFault
parameter_list|(
name|SOAPMessageContext
name|context
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{     }
block|}
end_class

end_unit

