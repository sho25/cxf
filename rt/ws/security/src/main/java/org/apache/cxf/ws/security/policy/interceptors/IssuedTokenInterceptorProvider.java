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
name|PolicyBasedWSS4JStaxInInterceptor
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
name|PolicyBasedWSS4JStaxOutInterceptor
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
name|wss4j
operator|.
name|common
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
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
name|dom
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
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
name|wss4j
operator|.
name|dom
operator|.
name|message
operator|.
name|token
operator|.
name|BinarySecurity
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
name|util
operator|.
name|WSSecurityUtil
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
name|SP11Constants
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
name|SP12Constants
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
name|IssuedToken
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
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|6936475570762840527L
decl_stmt|;
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
name|this
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|PolicyBasedWSS4JStaxOutInterceptor
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
name|PolicyBasedWSS4JStaxOutInterceptor
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
name|PolicyBasedWSS4JStaxInInterceptor
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
name|PolicyBasedWSS4JStaxInInterceptor
operator|.
name|INSTANCE
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
name|void
name|assertIssuedToken
parameter_list|(
name|IssuedToken
name|issuedToken
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
if|if
condition|(
name|issuedToken
operator|==
literal|null
condition|)
block|{
return|return;
block|}
comment|// Assert some policies
if|if
condition|(
name|issuedToken
operator|.
name|isRequireExternalReference
argument_list|()
condition|)
block|{
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|issuedToken
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_EXTERNAL_REFERENCE
argument_list|)
argument_list|,
name|aim
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|issuedToken
operator|.
name|isRequireInternalReference
argument_list|()
condition|)
block|{
name|assertPolicy
argument_list|(
operator|new
name|QName
argument_list|(
name|issuedToken
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|SPConstants
operator|.
name|REQUIRE_INTERNAL_REFERENCE
argument_list|)
argument_list|,
name|aim
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
name|void
name|assertPolicy
parameter_list|(
name|QName
name|n
parameter_list|,
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
name|getAssertionInfo
argument_list|(
name|n
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
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
block|}
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
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ISSUED_TOKEN
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
name|STSTokenHelper
operator|.
name|getTokenByWSPolicy
argument_list|(
name|message
argument_list|,
name|itok
argument_list|,
name|aim
argument_list|)
decl_stmt|;
if|if
condition|(
name|tok
operator|!=
literal|null
condition|)
block|{
name|assertIssuedToken
argument_list|(
name|itok
argument_list|,
name|aim
argument_list|)
expr_stmt|;
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
name|assertIssuedToken
argument_list|(
name|itok
argument_list|,
name|aim
argument_list|)
expr_stmt|;
block|}
block|}
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
name|PolicyUtils
operator|.
name|getAllAssertionsByLocalname
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|ISSUED_TOKEN
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
name|assertIssuedToken
argument_list|(
name|itok
argument_list|,
name|aim
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|isRequestor
argument_list|(
name|message
argument_list|)
condition|)
block|{
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
operator|&&
name|results
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|parseHandlerResults
argument_list|(
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|,
name|message
argument_list|,
name|ais
argument_list|)
expr_stmt|;
block|}
block|}
else|else
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
block|}
block|}
block|}
specifier|private
name|void
name|parseHandlerResults
parameter_list|(
name|WSHandlerResult
name|rResult
parameter_list|,
name|Message
name|message
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|issuedAis
parameter_list|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|signedResults
init|=
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
argument_list|)
decl_stmt|;
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
for|for
control|(
name|SamlAssertionWrapper
name|assertionWrapper
range|:
name|findSamlTokenResults
argument_list|(
name|rResult
operator|.
name|getResults
argument_list|()
argument_list|)
control|)
block|{
name|boolean
name|valid
init|=
name|issuedValidator
operator|.
name|validatePolicy
argument_list|(
name|issuedAis
argument_list|,
name|assertionWrapper
argument_list|)
decl_stmt|;
if|if
condition|(
name|valid
condition|)
block|{
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|assertionWrapper
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
return|return;
block|}
block|}
for|for
control|(
name|BinarySecurity
name|binarySecurityToken
range|:
name|findBinarySecurityTokenResults
argument_list|(
name|rResult
operator|.
name|getResults
argument_list|()
argument_list|)
control|)
block|{
name|boolean
name|valid
init|=
name|issuedValidator
operator|.
name|validatePolicy
argument_list|(
name|issuedAis
argument_list|,
name|binarySecurityToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|valid
condition|)
block|{
name|SecurityToken
name|token
init|=
name|createSecurityToken
argument_list|(
name|binarySecurityToken
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
return|return;
block|}
block|}
block|}
specifier|private
name|List
argument_list|<
name|SamlAssertionWrapper
argument_list|>
name|findSamlTokenResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|wsSecEngineResults
parameter_list|)
block|{
name|List
argument_list|<
name|SamlAssertionWrapper
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|SamlAssertionWrapper
argument_list|>
argument_list|()
decl_stmt|;
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
name|results
operator|.
name|add
argument_list|(
operator|(
name|SamlAssertionWrapper
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SAML_ASSERTION
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
specifier|private
name|List
argument_list|<
name|BinarySecurity
argument_list|>
name|findBinarySecurityTokenResults
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|wsSecEngineResults
parameter_list|)
block|{
name|List
argument_list|<
name|BinarySecurity
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|BinarySecurity
argument_list|>
argument_list|()
decl_stmt|;
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
name|BST
operator|&&
name|Boolean
operator|.
name|TRUE
operator|.
name|equals
argument_list|(
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_VALIDATED_TOKEN
argument_list|)
argument_list|)
condition|)
block|{
name|results
operator|.
name|add
argument_list|(
operator|(
name|BinarySecurity
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_BINARY_SECURITY_TOKEN
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
specifier|private
name|SecurityToken
name|createSecurityToken
parameter_list|(
name|SamlAssertionWrapper
name|assertionWrapper
parameter_list|)
block|{
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
name|token
operator|.
name|setToken
argument_list|(
name|assertionWrapper
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
specifier|private
name|SecurityToken
name|createSecurityToken
parameter_list|(
name|BinarySecurity
name|binarySecurityToken
parameter_list|)
block|{
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|binarySecurityToken
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|token
operator|.
name|setToken
argument_list|(
name|binarySecurityToken
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
name|binarySecurityToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenType
argument_list|(
name|binarySecurityToken
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
block|}
block|}
end_class

end_unit

