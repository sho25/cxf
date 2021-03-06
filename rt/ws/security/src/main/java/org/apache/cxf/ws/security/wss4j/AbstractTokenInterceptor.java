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
name|wss4j
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
name|Collections
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|w3c
operator|.
name|dom
operator|.
name|Document
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
name|binding
operator|.
name|soap
operator|.
name|SoapHeader
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|AbstractSoapInterceptor
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
name|i18n
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
name|headers
operator|.
name|Header
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
name|rt
operator|.
name|security
operator|.
name|utils
operator|.
name|SecurityUtils
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
name|security
operator|.
name|transport
operator|.
name|TLSSessionInfo
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
name|policy
operator|.
name|PolicyException
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
name|policy
operator|.
name|PolicyUtils
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
name|TokenStore
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
name|common
operator|.
name|WSS4JConstants
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
name|common
operator|.
name|ext
operator|.
name|WSPasswordCallback
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
name|AbstractToken
import|;
end_import

begin_comment
comment|/**  * An abstract interceptor that can be used to form the basis of an interceptor to add and process  * a specific type of security token.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTokenInterceptor
extends|extends
name|AbstractSoapInterceptor
block|{
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
name|AbstractSoapInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Set
argument_list|<
name|QName
argument_list|>
name|HEADERS
init|=
name|Collections
operator|.
name|singleton
argument_list|(
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE_NS
argument_list|,
literal|"Security"
argument_list|)
argument_list|)
decl_stmt|;
specifier|public
name|AbstractTokenInterceptor
parameter_list|()
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_PROTOCOL
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|PolicyBasedWSS4JInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|addAfter
argument_list|(
name|PolicyBasedWSS4JStaxInInterceptor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Set
argument_list|<
name|QName
argument_list|>
name|getUnderstoodHeaders
parameter_list|()
block|{
return|return
name|HEADERS
return|;
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
name|boolean
name|enableStax
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|ENABLE_STREAMING_SECURITY
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|enableStax
condition|)
block|{
return|return;
block|}
name|boolean
name|isReq
init|=
name|MessageUtils
operator|.
name|isRequestor
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|boolean
name|isOut
init|=
name|MessageUtils
operator|.
name|isOutbound
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|isReq
operator|!=
name|isOut
condition|)
block|{
comment|//outbound on server side and inbound on client side doesn't need
comment|//any specific token stuff, assert policies and return
name|assertTokens
argument_list|(
name|message
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
name|isReq
condition|)
block|{
if|if
condition|(
name|message
operator|.
name|containsKey
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|SECURITY_PROCESSED
argument_list|)
condition|)
block|{
comment|//The full policy interceptors handled this
return|return;
block|}
name|addToken
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|message
operator|.
name|containsKey
argument_list|(
name|WSS4JInInterceptor
operator|.
name|SECURITY_PROCESSED
argument_list|)
condition|)
block|{
comment|//The full policy interceptors handled this
return|return;
block|}
name|processToken
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|abstract
name|void
name|processToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|void
name|addToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
function_decl|;
specifier|protected
specifier|abstract
name|AbstractToken
name|assertTokens
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
function_decl|;
specifier|protected
name|AbstractToken
name|assertTokens
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|String
name|localname
parameter_list|,
name|boolean
name|signed
parameter_list|)
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
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|localname
argument_list|)
decl_stmt|;
name|AbstractToken
name|tok
init|=
literal|null
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|tok
operator|=
operator|(
name|AbstractToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
expr_stmt|;
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
if|if
condition|(
name|signed
operator|||
name|isTLSInUse
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|SIGNED_SUPPORTING_TOKENS
argument_list|)
expr_stmt|;
block|}
return|return
name|tok
return|;
block|}
specifier|protected
name|boolean
name|isTLSInUse
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
comment|// See whether TLS is in use or not
name|TLSSessionInfo
name|tlsInfo
init|=
name|message
operator|.
name|get
argument_list|(
name|TLSSessionInfo
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
name|tlsInfo
operator|!=
literal|null
return|;
block|}
specifier|protected
name|TokenStore
name|getTokenStore
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|EndpointInfo
name|info
init|=
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|TokenStore
name|tokenStore
init|=
operator|(
name|TokenStore
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenStore
operator|==
literal|null
condition|)
block|{
name|tokenStore
operator|=
operator|(
name|TokenStore
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
expr_stmt|;
block|}
return|return
name|tokenStore
return|;
block|}
block|}
specifier|protected
name|Header
name|findSecurityHeader
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|boolean
name|create
parameter_list|)
block|{
for|for
control|(
name|Header
name|h
range|:
name|message
operator|.
name|getHeaders
argument_list|()
control|)
block|{
name|QName
name|n
init|=
name|h
operator|.
name|getName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"Security"
operator|.
name|equals
argument_list|(
name|n
operator|.
name|getLocalPart
argument_list|()
argument_list|)
operator|&&
operator|(
name|n
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|WSS4JConstants
operator|.
name|WSSE_NS
argument_list|)
operator|||
name|n
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|WSS4JConstants
operator|.
name|WSSE11_NS
argument_list|)
operator|)
condition|)
block|{
return|return
name|h
return|;
block|}
block|}
if|if
condition|(
operator|!
name|create
condition|)
block|{
return|return
literal|null
return|;
block|}
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|getEmptyDocument
argument_list|()
decl_stmt|;
name|Element
name|el
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSS4JConstants
operator|.
name|WSSE_NS
argument_list|,
literal|"wsse:Security"
argument_list|)
decl_stmt|;
name|el
operator|.
name|setAttributeNS
argument_list|(
name|WSS4JConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:wsse"
argument_list|,
name|WSS4JConstants
operator|.
name|WSSE_NS
argument_list|)
expr_stmt|;
name|SoapHeader
name|sh
init|=
operator|new
name|SoapHeader
argument_list|(
operator|new
name|QName
argument_list|(
name|WSS4JConstants
operator|.
name|WSSE_NS
argument_list|,
literal|"Security"
argument_list|)
argument_list|,
name|el
argument_list|)
decl_stmt|;
name|sh
operator|.
name|setMustUnderstand
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|message
operator|.
name|getHeaders
argument_list|()
operator|.
name|add
argument_list|(
name|sh
argument_list|)
expr_stmt|;
return|return
name|sh
return|;
block|}
specifier|protected
name|String
name|getPassword
parameter_list|(
name|String
name|userName
parameter_list|,
name|AbstractToken
name|info
parameter_list|,
name|int
name|usage
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
comment|//Then try to get the password from the given callback handler
name|CallbackHandler
name|handler
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Object
name|o
init|=
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|handler
operator|=
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
name|o
argument_list|)
expr_stmt|;
if|if
condition|(
name|handler
operator|==
literal|null
condition|)
block|{
name|policyNotAsserted
argument_list|(
name|info
argument_list|,
literal|"No callback handler and no password available"
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|policyNotAsserted
argument_list|(
name|info
argument_list|,
literal|"No callback handler and no password available"
argument_list|,
name|message
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|WSPasswordCallback
index|[]
name|cb
init|=
block|{
operator|new
name|WSPasswordCallback
argument_list|(
name|userName
argument_list|,
name|usage
argument_list|)
block|}
decl_stmt|;
try|try
block|{
name|handler
operator|.
name|handle
argument_list|(
name|cb
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|policyNotAsserted
argument_list|(
name|info
argument_list|,
name|e
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
comment|//get the password
return|return
name|cb
index|[
literal|0
index|]
operator|.
name|getPassword
argument_list|()
return|;
block|}
specifier|protected
name|void
name|policyNotAsserted
parameter_list|(
name|AbstractToken
name|assertion
parameter_list|,
name|String
name|reason
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|assertion
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
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
if|if
condition|(
name|ai
operator|.
name|getAssertion
argument_list|()
operator|==
name|assertion
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|reason
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
operator|!
name|assertion
operator|.
name|isOptional
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|PolicyException
argument_list|(
operator|new
name|Message
argument_list|(
name|reason
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|policyNotAsserted
parameter_list|(
name|AbstractToken
name|assertion
parameter_list|,
name|Exception
name|reason
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
if|if
condition|(
name|assertion
operator|==
literal|null
condition|)
block|{
return|return;
block|}
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
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|assertion
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
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
if|if
condition|(
name|ai
operator|.
name|getAssertion
argument_list|()
operator|==
name|assertion
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
name|reason
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
throw|throw
operator|new
name|PolicyException
argument_list|(
name|reason
argument_list|)
throw|;
block|}
block|}
end_class

end_unit

