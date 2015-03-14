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
name|security
operator|.
name|Principal
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
name|java
operator|.
name|util
operator|.
name|Set
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
name|Subject
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|interceptor
operator|.
name|security
operator|.
name|DefaultSecurityContext
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimCollection
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
name|saml
operator|.
name|SAMLSecurityContext
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
name|saml
operator|.
name|SAMLUtils
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
name|SecurityContext
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
name|wss4j
operator|.
name|common
operator|.
name|cache
operator|.
name|ReplayCache
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
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
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
name|principal
operator|.
name|SAMLTokenPrincipalImpl
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
name|principal
operator|.
name|UsernameTokenPrincipal
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
name|principal
operator|.
name|WSUsernameTokenPrincipalImpl
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
name|WSDocInfo
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
name|WSSConfig
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
name|bsp
operator|.
name|BSPEnforcer
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
name|RequestData
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
name|WSSecUsernameToken
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
name|processor
operator|.
name|UsernameTokenProcessor
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
name|SP13Constants
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
name|AbstractSecurityAssertion
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
name|SupportingTokens
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
name|UsernameToken
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|exceptions
operator|.
name|Base64DecodingException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|utils
operator|.
name|Base64
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|UsernameTokenInterceptor
extends|extends
name|AbstractTokenInterceptor
block|{
specifier|public
name|UsernameTokenInterceptor
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|processToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|Header
name|h
init|=
name|findSecurityHeader
argument_list|(
name|message
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|h
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|boolean
name|utWithCallbacks
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|VALIDATE_TOKEN
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|h
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|el
argument_list|)
decl_stmt|;
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|SPConstants
operator|.
name|USERNAME_TOKEN
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSConstants
operator|.
name|WSSE_NS
operator|.
name|equals
argument_list|(
name|child
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|boolean
name|bspCompliant
init|=
name|isWsiBSPCompliant
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Principal
name|principal
init|=
literal|null
decl_stmt|;
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
name|Object
name|transformedToken
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|utWithCallbacks
condition|)
block|{
specifier|final
name|WSSecurityEngineResult
name|result
init|=
name|validateToken
argument_list|(
name|child
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|subject
operator|=
operator|(
name|Subject
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_SUBJECT
argument_list|)
expr_stmt|;
name|transformedToken
operator|=
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TRANSFORMED_TOKEN
argument_list|)
expr_stmt|;
name|principal
operator|=
operator|(
name|Principal
operator|)
name|result
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_PRINCIPAL
argument_list|)
expr_stmt|;
if|if
condition|(
name|principal
operator|==
literal|null
condition|)
block|{
name|principal
operator|=
name|parseTokenAndCreatePrincipal
argument_list|(
name|child
argument_list|,
name|bspCompliant
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|principal
operator|=
name|parseTokenAndCreatePrincipal
argument_list|(
name|child
argument_list|,
name|bspCompliant
argument_list|)
expr_stmt|;
name|WSS4JTokenConverter
operator|.
name|convertToken
argument_list|(
name|message
argument_list|,
name|principal
argument_list|)
expr_stmt|;
block|}
name|SecurityContext
name|sc
init|=
name|message
operator|.
name|get
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|sc
operator|==
literal|null
operator|||
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|transformedToken
operator|instanceof
name|SamlAssertionWrapper
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|createSecurityContext
argument_list|(
name|message
argument_list|,
operator|(
name|SamlAssertionWrapper
operator|)
name|transformedToken
argument_list|)
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|subject
operator|!=
literal|null
operator|&&
name|principal
operator|!=
literal|null
condition|)
block|{
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|createSecurityContext
argument_list|(
name|principal
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|UsernameTokenPrincipal
name|utPrincipal
init|=
operator|(
name|UsernameTokenPrincipal
operator|)
name|principal
decl_stmt|;
name|String
name|nonce
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|utPrincipal
operator|.
name|getNonce
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|nonce
operator|=
name|Base64
operator|.
name|encode
argument_list|(
name|utPrincipal
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|subject
operator|=
name|createSubject
argument_list|(
name|utPrincipal
operator|.
name|getName
argument_list|()
argument_list|,
name|utPrincipal
operator|.
name|getPassword
argument_list|()
argument_list|,
name|utPrincipal
operator|.
name|isPasswordDigest
argument_list|()
argument_list|,
name|nonce
argument_list|,
name|utPrincipal
operator|.
name|getCreatedTime
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|createSecurityContext
argument_list|(
name|utPrincipal
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|principal
operator|instanceof
name|UsernameTokenPrincipal
condition|)
block|{
name|storeResults
argument_list|(
operator|(
name|UsernameTokenPrincipal
operator|)
name|principal
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Base64DecodingException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|Fault
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
name|child
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|child
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|SecurityContext
name|createSecurityContext
parameter_list|(
name|Message
name|msg
parameter_list|,
name|SamlAssertionWrapper
name|samlAssertion
parameter_list|)
block|{
name|String
name|roleAttributeName
init|=
operator|(
name|String
operator|)
name|msg
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SAML_ROLE_ATTRIBUTENAME
argument_list|)
decl_stmt|;
if|if
condition|(
name|roleAttributeName
operator|==
literal|null
operator|||
name|roleAttributeName
operator|.
name|length
argument_list|()
operator|==
literal|0
condition|)
block|{
name|roleAttributeName
operator|=
name|WSS4JInInterceptor
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
expr_stmt|;
block|}
name|ClaimCollection
name|claims
init|=
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
operator|(
name|SamlAssertionWrapper
operator|)
name|samlAssertion
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|SAMLUtils
operator|.
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
name|roleAttributeName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SAMLSecurityContext
name|context
init|=
operator|new
name|SAMLSecurityContext
argument_list|(
operator|new
name|SAMLTokenPrincipalImpl
argument_list|(
name|samlAssertion
argument_list|)
argument_list|,
name|roles
argument_list|,
name|claims
argument_list|)
decl_stmt|;
name|context
operator|.
name|setIssuer
argument_list|(
name|SAMLUtils
operator|.
name|getIssuer
argument_list|(
name|samlAssertion
argument_list|)
argument_list|)
expr_stmt|;
name|context
operator|.
name|setAssertionElement
argument_list|(
name|SAMLUtils
operator|.
name|getAssertionElement
argument_list|(
name|samlAssertion
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|context
return|;
block|}
specifier|private
name|void
name|storeResults
parameter_list|(
name|UsernameTokenPrincipal
name|principal
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|v
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|int
name|action
init|=
name|WSConstants
operator|.
name|UT
decl_stmt|;
if|if
condition|(
name|principal
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|)
block|{
name|action
operator|=
name|WSConstants
operator|.
name|UT_NOPASSWORD
expr_stmt|;
block|}
name|v
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|WSSecurityEngineResult
argument_list|(
name|action
argument_list|,
name|principal
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
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
operator|==
literal|null
condition|)
block|{
name|results
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|WSHandlerConstants
operator|.
name|RECV_RESULTS
argument_list|,
name|results
argument_list|)
expr_stmt|;
block|}
name|WSHandlerResult
name|rResult
init|=
operator|new
name|WSHandlerResult
argument_list|(
literal|null
argument_list|,
name|v
argument_list|)
decl_stmt|;
name|results
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|rResult
argument_list|)
expr_stmt|;
name|assertTokens
argument_list|(
name|message
argument_list|,
name|principal
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|WSSecurityEngineResult
name|validateToken
parameter_list|(
name|Element
name|tokenElement
parameter_list|,
specifier|final
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
throws|,
name|Base64DecodingException
block|{
name|boolean
name|bspCompliant
init|=
name|isWsiBSPCompliant
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|boolean
name|allowNoPassword
init|=
name|isAllowNoPassword
argument_list|(
name|message
operator|.
name|get
argument_list|(
name|AssertionInfoMap
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|UsernameTokenProcessor
name|p
init|=
operator|new
name|UsernameTokenProcessor
argument_list|()
decl_stmt|;
name|WSDocInfo
name|wsDocInfo
init|=
operator|new
name|WSDocInfo
argument_list|(
name|tokenElement
operator|.
name|getOwnerDocument
argument_list|()
argument_list|)
decl_stmt|;
name|RequestData
name|data
init|=
operator|new
name|CXFRequestData
argument_list|()
decl_stmt|;
name|data
operator|.
name|setCallbackHandler
argument_list|(
name|getCallback
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
name|data
operator|.
name|setMsgContext
argument_list|(
name|message
argument_list|)
expr_stmt|;
comment|// Configure replay caching
name|ReplayCache
name|nonceCache
init|=
name|WSS4JUtils
operator|.
name|getReplayCache
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|ENABLE_NONCE_CACHE
argument_list|,
name|SecurityConstants
operator|.
name|NONCE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
name|data
operator|.
name|setNonceReplayCache
argument_list|(
name|nonceCache
argument_list|)
expr_stmt|;
name|WSSConfig
name|config
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
name|config
operator|.
name|setAllowUsernameTokenNoPassword
argument_list|(
name|allowNoPassword
argument_list|)
expr_stmt|;
name|data
operator|.
name|setWssConfig
argument_list|(
name|config
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|bspCompliant
condition|)
block|{
name|data
operator|.
name|setDisableBSPEnforcement
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|data
operator|.
name|setMsgContext
argument_list|(
name|message
argument_list|)
expr_stmt|;
try|try
block|{
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|results
init|=
name|p
operator|.
name|handleToken
argument_list|(
name|tokenElement
argument_list|,
name|data
argument_list|,
name|wsDocInfo
argument_list|)
decl_stmt|;
return|return
name|results
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|ex
parameter_list|)
block|{
throw|throw
name|WSS4JUtils
operator|.
name|createSoapFault
argument_list|(
name|message
argument_list|,
name|message
operator|.
name|getVersion
argument_list|()
argument_list|,
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|UsernameTokenPrincipal
name|parseTokenAndCreatePrincipal
parameter_list|(
name|Element
name|tokenElement
parameter_list|,
name|boolean
name|bspCompliant
parameter_list|)
throws|throws
name|WSSecurityException
throws|,
name|Base64DecodingException
block|{
name|BSPEnforcer
name|bspEnforcer
init|=
operator|new
name|BSPEnforcer
argument_list|(
operator|!
name|bspCompliant
argument_list|)
decl_stmt|;
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
name|UsernameToken
name|ut
init|=
operator|new
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
name|UsernameToken
argument_list|(
name|tokenElement
argument_list|,
literal|false
argument_list|,
name|bspEnforcer
argument_list|)
decl_stmt|;
name|WSUsernameTokenPrincipalImpl
name|principal
init|=
operator|new
name|WSUsernameTokenPrincipalImpl
argument_list|(
name|ut
operator|.
name|getName
argument_list|()
argument_list|,
name|ut
operator|.
name|isHashed
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|ut
operator|.
name|getNonce
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|principal
operator|.
name|setNonce
argument_list|(
name|Base64
operator|.
name|decode
argument_list|(
name|ut
operator|.
name|getNonce
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|principal
operator|.
name|setPassword
argument_list|(
name|ut
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|principal
operator|.
name|setCreatedTime
argument_list|(
name|ut
operator|.
name|getCreated
argument_list|()
argument_list|)
expr_stmt|;
name|principal
operator|.
name|setPasswordType
argument_list|(
name|ut
operator|.
name|getPasswordType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|principal
return|;
block|}
specifier|protected
name|boolean
name|isWsiBSPCompliant
parameter_list|(
specifier|final
name|SoapMessage
name|message
parameter_list|)
block|{
name|String
name|bspc
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
name|IS_BSP_COMPLIANT
argument_list|)
decl_stmt|;
comment|// Default to WSI-BSP compliance enabled
return|return
operator|!
operator|(
literal|"false"
operator|.
name|equals
argument_list|(
name|bspc
argument_list|)
operator|||
literal|"0"
operator|.
name|equals
argument_list|(
name|bspc
argument_list|)
operator|)
return|;
block|}
specifier|private
name|boolean
name|isAllowNoPassword
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
throws|throws
name|WSSecurityException
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
name|USERNAME_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
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
name|UsernameToken
name|policy
init|=
operator|(
name|UsernameToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|policy
operator|.
name|getPasswordType
argument_list|()
operator|==
name|UsernameToken
operator|.
name|PasswordType
operator|.
name|NoPassword
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|protected
name|SecurityContext
name|createSecurityContext
parameter_list|(
specifier|final
name|Principal
name|p
parameter_list|,
name|Subject
name|subject
parameter_list|)
block|{
return|return
operator|new
name|DefaultSecurityContext
argument_list|(
name|p
argument_list|,
name|subject
argument_list|)
return|;
block|}
comment|/**      * Create a Subject representing a current user and its roles.       * This Subject is expected to contain at least one Principal representing a user      * and optionally followed by one or more principal Groups this user is a member of.      * @param name username      * @param password password      * @param isDigest true if a password digest is used      * @param nonce optional nonce      * @param created optional timestamp      * @return subject      * @throws SecurityException      */
specifier|protected
name|Subject
name|createSubject
parameter_list|(
name|String
name|name
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|isDigest
parameter_list|,
name|String
name|nonce
parameter_list|,
name|String
name|created
parameter_list|)
throws|throws
name|SecurityException
block|{
return|return
literal|null
return|;
block|}
specifier|protected
name|UsernameToken
name|assertTokens
parameter_list|(
name|SoapMessage
name|message
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
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN10
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN11
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HASH_PASSWORD
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|NO_PASSWORD
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP13Constants
operator|.
name|NONCE
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP13Constants
operator|.
name|CREATED
argument_list|)
expr_stmt|;
return|return
operator|(
name|UsernameToken
operator|)
name|assertTokens
argument_list|(
name|message
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
name|UsernameToken
name|assertTokens
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|UsernameTokenPrincipal
name|princ
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
name|SPConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
decl_stmt|;
name|UsernameToken
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
name|UsernameToken
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
if|if
condition|(
operator|(
name|tok
operator|.
name|getPasswordType
argument_list|()
operator|==
name|UsernameToken
operator|.
name|PasswordType
operator|.
name|HashPassword
operator|)
operator|&&
operator|(
name|princ
operator|==
literal|null
operator|||
operator|!
name|princ
operator|.
name|isPasswordDigest
argument_list|()
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Password hashing policy not enforced"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|HASH_PASSWORD
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|(
name|tok
operator|.
name|getPasswordType
argument_list|()
operator|!=
name|UsernameToken
operator|.
name|PasswordType
operator|.
name|NoPassword
operator|)
operator|&&
name|isNonEndorsingSupportingToken
argument_list|(
name|tok
argument_list|)
operator|&&
operator|(
name|princ
operator|==
literal|null
operator|||
name|princ
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
operator|)
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"Username Token No Password supplied"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|NO_PASSWORD
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tok
operator|.
name|isCreated
argument_list|()
operator|&&
name|princ
operator|.
name|getCreatedTime
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"No Created Time"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP13Constants
operator|.
name|CREATED
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tok
operator|.
name|isNonce
argument_list|()
operator|&&
name|princ
operator|.
name|getNonce
argument_list|()
operator|==
literal|null
condition|)
block|{
name|ai
operator|.
name|setNotAsserted
argument_list|(
literal|"No Nonce"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SP13Constants
operator|.
name|NONCE
argument_list|)
expr_stmt|;
block|}
block|}
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN10
argument_list|)
expr_stmt|;
name|PolicyUtils
operator|.
name|assertPolicy
argument_list|(
name|aim
argument_list|,
name|SPConstants
operator|.
name|USERNAME_TOKEN11
argument_list|)
expr_stmt|;
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
comment|/**      * Return true if this UsernameToken policy is a (non-endorsing)SupportingToken. If this is      * true then the corresponding UsernameToken must have a password element.      */
specifier|private
name|boolean
name|isNonEndorsingSupportingToken
parameter_list|(
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
name|UsernameToken
name|usernameTokenPolicy
parameter_list|)
block|{
name|AbstractSecurityAssertion
name|supportingToken
init|=
name|usernameTokenPolicy
operator|.
name|getParentAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
name|supportingToken
operator|instanceof
name|SupportingTokens
operator|&&
operator|(
operator|(
name|SupportingTokens
operator|)
name|supportingToken
operator|)
operator|.
name|isEndorsing
argument_list|()
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|void
name|addToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|UsernameToken
name|tok
init|=
name|assertTokens
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|Header
name|h
init|=
name|findSecurityHeader
argument_list|(
name|message
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|WSSecUsernameToken
name|utBuilder
init|=
name|addUsernameToken
argument_list|(
name|message
argument_list|,
name|tok
argument_list|)
decl_stmt|;
if|if
condition|(
name|utBuilder
operator|==
literal|null
condition|)
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
name|SPConstants
operator|.
name|USERNAME_TOKEN
argument_list|)
decl_stmt|;
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
name|isAsserted
argument_list|()
condition|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
return|return;
block|}
name|Element
name|el
init|=
operator|(
name|Element
operator|)
name|h
operator|.
name|getObject
argument_list|()
decl_stmt|;
name|utBuilder
operator|.
name|prepare
argument_list|(
name|el
operator|.
name|getOwnerDocument
argument_list|()
argument_list|)
expr_stmt|;
name|el
operator|.
name|appendChild
argument_list|(
name|utBuilder
operator|.
name|getUsernameTokenElement
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|WSSecUsernameToken
name|addUsernameToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|UsernameToken
name|token
parameter_list|)
block|{
name|String
name|userName
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
name|USERNAME
argument_list|)
decl_stmt|;
name|WSSConfig
name|wssConfig
init|=
operator|(
name|WSSConfig
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|WSSConfig
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|wssConfig
operator|==
literal|null
condition|)
block|{
name|wssConfig
operator|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|userName
argument_list|)
condition|)
block|{
comment|// If NoPassword property is set we don't need to set the password
if|if
condition|(
name|token
operator|.
name|getPasswordType
argument_list|()
operator|==
name|UsernameToken
operator|.
name|PasswordType
operator|.
name|NoPassword
condition|)
block|{
name|WSSecUsernameToken
name|utBuilder
init|=
operator|new
name|WSSecUsernameToken
argument_list|(
name|wssConfig
argument_list|)
decl_stmt|;
name|utBuilder
operator|.
name|setUserInfo
argument_list|(
name|userName
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|utBuilder
operator|.
name|setPasswordType
argument_list|(
literal|null
argument_list|)
expr_stmt|;
return|return
name|utBuilder
return|;
block|}
name|String
name|password
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
name|PASSWORD
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|password
argument_list|)
condition|)
block|{
name|password
operator|=
name|getPassword
argument_list|(
name|userName
argument_list|,
name|token
argument_list|,
name|WSPasswordCallback
operator|.
name|USERNAME_TOKEN
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|password
argument_list|)
condition|)
block|{
comment|//If the password is available then build the token
name|WSSecUsernameToken
name|utBuilder
init|=
operator|new
name|WSSecUsernameToken
argument_list|(
name|wssConfig
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|getPasswordType
argument_list|()
operator|==
name|UsernameToken
operator|.
name|PasswordType
operator|.
name|HashPassword
condition|)
block|{
name|utBuilder
operator|.
name|setPasswordType
argument_list|(
name|WSConstants
operator|.
name|PASSWORD_DIGEST
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|utBuilder
operator|.
name|setPasswordType
argument_list|(
name|WSConstants
operator|.
name|PASSWORD_TEXT
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isCreated
argument_list|()
condition|)
block|{
name|utBuilder
operator|.
name|addCreated
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|token
operator|.
name|isNonce
argument_list|()
condition|)
block|{
name|utBuilder
operator|.
name|addNonce
argument_list|()
expr_stmt|;
block|}
name|utBuilder
operator|.
name|setUserInfo
argument_list|(
name|userName
argument_list|,
name|password
argument_list|)
expr_stmt|;
return|return
name|utBuilder
return|;
block|}
else|else
block|{
name|policyNotAsserted
argument_list|(
name|token
argument_list|,
literal|"No username available"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|policyNotAsserted
argument_list|(
name|token
argument_list|,
literal|"No username available"
argument_list|,
name|message
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

