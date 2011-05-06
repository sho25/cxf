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
name|security
operator|.
name|cert
operator|.
name|X509Certificate
import|;
end_import

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
name|Arrays
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
name|List
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
name|AbstractPolicyInterceptorProvider
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
name|policy
operator|.
name|SP11Constants
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
name|SP12Constants
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
name|model
operator|.
name|IssuedToken
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|policy
operator|.
name|model
operator|.
name|Trust13
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
name|MemoryTokenStore
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|wss4j
operator|.
name|PolicyBasedWSS4JInInterceptor
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
name|wss4j
operator|.
name|PolicyBasedWSS4JOutInterceptor
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
name|wss4j
operator|.
name|WSS4JInInterceptor
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
name|wss4j
operator|.
name|policyvalidators
operator|.
name|IssuedTokenPolicyValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
operator|.
name|WSSecurityEngineResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|handler
operator|.
name|WSHandlerConstants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|handler
operator|.
name|WSHandlerResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|SAMLKeyInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|saml
operator|.
name|ext
operator|.
name|AssertionWrapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|util
operator|.
name|WSSecurityUtil
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|IssuedTokenInterceptorProvider
extends|extends
name|AbstractPolicyInterceptorProvider
block|{
specifier|public
name|IssuedTokenInterceptorProvider
parameter_list|()
block|{
name|super
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|SP11Constants
operator|.
name|ISSUED_TOKEN
argument_list|,
name|SP12Constants
operator|.
name|ISSUED_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
comment|//issued tokens can be attached as a supporting token without
comment|//any type of binding.  Make sure we can support that.
name|this
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JOutInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JInInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JInInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|IssuedTokenOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getOutFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|IssuedTokenOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|IssuedTokenInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|getInFaultInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|IssuedTokenInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|static
specifier|final
name|TokenStore
name|getTokenStore
parameter_list|(
name|Message
name|message
parameter_list|)
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
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
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
operator|new
name|MemoryTokenStore
argument_list|()
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
name|getEndpointInfo
argument_list|()
operator|.
name|setProperty
argument_list|(
name|TokenStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|tokenStore
argument_list|)
expr_stmt|;
block|}
return|return
name|tokenStore
return|;
block|}
specifier|static
class|class
name|IssuedTokenOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|IssuedTokenOutInterceptor
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
name|Message
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
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ISSUED_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
operator|||
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
name|IssuedToken
name|itok
init|=
operator|(
name|IssuedToken
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
name|STSClient
name|client
init|=
name|STSUtils
operator|.
name|getClient
argument_list|(
name|message
argument_list|,
literal|"sts"
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
comment|// Transpose ActAs info from original request to the STS client.
name|client
operator|.
name|setActAs
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|STS_TOKEN_ACT_AS
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTrust
argument_list|(
name|getTrust10
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTrust
argument_list|(
name|getTrust13
argument_list|(
name|aim
argument_list|)
argument_list|)
expr_stmt|;
name|client
operator|.
name|setTemplate
argument_list|(
name|itok
operator|.
name|getRstTemplate
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|maps
operator|==
literal|null
condition|)
block|{
name|tok
operator|=
name|client
operator|.
name|requestSecurityToken
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|STS_APPLIES_TO
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|o
operator|==
literal|null
condition|?
literal|null
else|:
name|o
operator|.
name|toString
argument_list|()
decl_stmt|;
name|s
operator|=
name|s
operator|==
literal|null
condition|?
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
else|:
name|s
expr_stmt|;
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
name|tok
operator|=
name|client
operator|.
name|requestSecurityToken
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
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
name|setAddressingNamespace
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
comment|//renew token?
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
name|Trust10
name|getTrust10
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
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
name|SP11Constants
operator|.
name|TRUST_10
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
operator|||
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|Trust10
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
return|;
block|}
specifier|private
name|Trust13
name|getTrust13
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
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
name|SP12Constants
operator|.
name|TRUST_13
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
operator|||
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|(
name|Trust13
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
return|;
block|}
block|}
specifier|static
class|class
name|IssuedTokenInInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|public
name|IssuedTokenInInterceptor
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
name|WSS4JInInterceptor
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
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
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
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ISSUED_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|results
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|!=
literal|null
condition|)
block|{
name|parseHandlerResults
argument_list|(
name|results
argument_list|,
name|message
argument_list|,
name|aim
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
comment|//client side should be checked on the way out
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
name|void
name|parseHandlerResults
parameter_list|(
name|List
argument_list|<
name|WSHandlerResult
argument_list|>
name|results
parameter_list|,
name|Message
name|message
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
if|if
condition|(
name|results
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|WSHandlerResult
name|rResult
range|:
name|results
control|)
block|{
name|WSSecurityEngineResult
name|wser
init|=
name|findSecurityResult
argument_list|(
name|rResult
operator|.
name|getResults
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|wser
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
init|=
operator|new
name|ArrayList
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|WSSecurityUtil
operator|.
name|fetchAllActionResults
argument_list|(
name|rResult
operator|.
name|getResults
argument_list|()
argument_list|,
name|WSConstants
operator|.
name|SIGN
argument_list|,
name|signedResults
argument_list|)
expr_stmt|;
comment|//
comment|// Validate the Issued Token policy
comment|//
name|IssuedTokenPolicyValidator
name|issuedValidator
init|=
operator|new
name|IssuedTokenPolicyValidator
argument_list|(
name|signedResults
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|issuedValidator
operator|.
name|validatePolicy
argument_list|(
name|aim
argument_list|,
name|wser
argument_list|)
condition|)
block|{
break|break;
block|}
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|wser
argument_list|)
decl_stmt|;
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
name|token
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|WSSecurityEngineResult
name|findSecurityResult
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|wsSecEngineResults
parameter_list|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|wser
range|:
name|wsSecEngineResults
control|)
block|{
name|Integer
name|actInt
init|=
operator|(
name|Integer
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_ACTION
argument_list|)
decl_stmt|;
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ST_SIGNED
operator|||
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|ST_UNSIGNED
condition|)
block|{
return|return
name|wser
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|SecurityToken
name|createSecurityToken
parameter_list|(
name|WSSecurityEngineResult
name|wser
parameter_list|)
block|{
name|AssertionWrapper
name|assertionWrapper
init|=
operator|(
name|AssertionWrapper
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
decl_stmt|;
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|assertionWrapper
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
name|SAMLKeyInfo
name|subjectKeyInfo
init|=
name|assertionWrapper
operator|.
name|getSubjectKeyInfo
argument_list|()
decl_stmt|;
if|if
condition|(
name|subjectKeyInfo
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setSecret
argument_list|(
name|subjectKeyInfo
operator|.
name|getSecret
argument_list|()
argument_list|)
expr_stmt|;
name|X509Certificate
index|[]
name|certs
init|=
name|subjectKeyInfo
operator|.
name|getCerts
argument_list|()
decl_stmt|;
if|if
condition|(
name|certs
operator|!=
literal|null
operator|&&
name|certs
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|token
operator|.
name|setX509Certificate
argument_list|(
name|certs
index|[
literal|0
index|]
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|assertionWrapper
operator|.
name|getSaml1
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|assertionWrapper
operator|.
name|getSaml2
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setTokenType
argument_list|(
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
expr_stmt|;
block|}
return|return
name|token
return|;
block|}
block|}
block|}
end_class

end_unit

