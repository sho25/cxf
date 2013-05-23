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
name|security
operator|.
name|policy
operator|.
name|interceptors
package|;
end_package

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
name|Map
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
name|binding
operator|.
name|soap
operator|.
name|SoapMessage
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
name|interceptor
operator|.
name|Fault
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|policy
operator|.
name|AssertionInfo
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
name|policy
operator|.
name|AssertionInfoMap
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
name|security
operator|.
name|SecurityConstants
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
name|security
operator|.
name|tokenstore
operator|.
name|SecurityToken
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
name|security
operator|.
name|trust
operator|.
name|STSClient
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
name|security
operator|.
name|trust
operator|.
name|STSUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|SPConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|SecureConversationToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|Trust10
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|Trust13
import|;
end_import

begin_class
class|class
name|SecureConversationOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|public
name|SecureConversationOutInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PREPARE_SEND
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|Fault
block|{
name|AssertionInfoMap
name|aim
init|=
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
decl_stmt|;
comment|// extract Assertion information
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|NegotiationUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SECURE_CONVERSATION_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|SecureConversationToken
name|itok
init|=
operator|(
name|SecureConversationToken
operator|)
name|ais
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|SecurityToken
name|tok
init|=
operator|(
name|SecurityToken
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|tok
operator|==
literal|null
condition|)
block|{
name|String
name|tokId
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokId
operator|!=
literal|null
condition|)
block|{
name|tok
operator|=
name|NegotiationUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|getToken
argument_list|(
name|tokId
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|tok
operator|==
literal|null
condition|)
block|{
name|tok
operator|=
name|issueToken
argument_list|(
name|message
argument_list|,
name|aim
argument_list|,
name|itok
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|tok
operator|=
name|renewToken
argument_list|(
name|message
argument_list|,
name|aim
argument_list|,
name|tok
argument_list|,
name|itok
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tok
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
name|tok
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|tok
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|tok
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
name|tok
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|add
argument_list|(
name|tok
argument_list|)
expr_stmt|;
block|}
name|NegotiationUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|BOOTSTRAP_POLICY
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//server side should be checked on the way in
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|SecurityToken
name|renewToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|SecurityToken
name|tok
parameter_list|,
name|SecureConversationToken
name|itok
parameter_list|)
block|{
if|if
condition|(
operator|!
name|tok
operator|.
name|isExpired
argument_list|()
condition|)
block|{
return|return
name|tok
return|;
block|}
comment|// Remove the old token
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|get
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
expr_stmt|;
name|NegotiationUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|remove
argument_list|(
name|tok
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|STSClient
name|client
init|=
name|STSUtils
operator|.
name|getClient
argument_list|(
name|message
argument_list|,
literal|"sct"
argument_list|)
decl_stmt|;
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
literal|"javax.xml.ws.addressing.context.outbound"
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|==
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
name|get
argument_list|(
literal|"javax.xml.ws.addressing.context"
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|maps
operator|.
name|getAction
argument_list|()
operator|.
name|getValue
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"Renew"
argument_list|)
condition|)
block|{
return|return
name|tok
return|;
block|}
synchronized|synchronized
init|(
name|client
init|)
block|{
try|try
block|{
name|SecureConversationTokenInterceptorProvider
operator|.
name|setupClient
argument_list|(
name|client
argument_list|,
name|message
argument_list|,
name|aim
argument_list|,
name|itok
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|String
name|s
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|Message
operator|.
name|ENDPOINT_ADDRESS
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|client
operator|.
name|setLocation
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|ctx
init|=
name|client
operator|.
name|getRequestContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|tok
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|maps
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|setAddressingNamespace
argument_list|(
name|maps
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|client
operator|.
name|renewSecurityToken
argument_list|(
name|tok
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|client
operator|.
name|setTrust
argument_list|(
operator|(
name|Trust10
operator|)
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTrust
argument_list|(
operator|(
name|Trust13
operator|)
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTemplate
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setLocation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setAddressingNamespace
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|SecurityToken
name|issueToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|SecureConversationToken
name|itok
parameter_list|)
block|{
name|STSClient
name|client
init|=
name|STSUtils
operator|.
name|getClient
argument_list|(
name|message
argument_list|,
literal|"sct"
argument_list|)
decl_stmt|;
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
literal|"javax.xml.ws.addressing.context.outbound"
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|==
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
name|get
argument_list|(
literal|"javax.xml.ws.addressing.context"
argument_list|)
expr_stmt|;
block|}
synchronized|synchronized
init|(
name|client
init|)
block|{
try|try
block|{
name|String
name|s
init|=
name|SecureConversationTokenInterceptorProvider
operator|.
name|setupClient
argument_list|(
name|client
argument_list|,
name|message
argument_list|,
name|aim
argument_list|,
name|itok
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|SecurityToken
name|tok
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|maps
operator|!=
literal|null
condition|)
block|{
name|client
operator|.
name|setAddressingNamespace
argument_list|(
name|maps
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|tok
operator|=
name|client
operator|.
name|requestSecurityToken
argument_list|(
name|s
argument_list|)
expr_stmt|;
name|String
name|tokenType
init|=
name|tok
operator|.
name|getTokenType
argument_list|()
decl_stmt|;
name|tok
operator|.
name|setTokenType
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
if|if
condition|(
name|tokenType
operator|==
literal|null
operator|||
literal|""
operator|.
name|equals
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
name|tok
operator|.
name|setTokenType
argument_list|(
name|WSConstants
operator|.
name|WSC_SCT
argument_list|)
expr_stmt|;
block|}
return|return
name|tok
return|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|e
argument_list|)
throw|;
block|}
finally|finally
block|{
name|client
operator|.
name|setTrust
argument_list|(
operator|(
name|Trust10
operator|)
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTrust
argument_list|(
operator|(
name|Trust13
operator|)
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTemplate
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setLocation
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|client
operator|.
name|setAddressingNamespace
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

