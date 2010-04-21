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
name|Collection
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
name|java
operator|.
name|util
operator|.
name|Vector
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
name|Subject
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|SPConstants
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
name|UsernameToken
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
name|WSPasswordCallback
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
name|WSSecurityException
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
name|WSUsernameTokenPrincipal
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
name|ws
operator|.
name|security
operator|.
name|processor
operator|.
name|UsernameTokenProcessor
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
name|UsernameTokenInterceptor
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
operator|new
name|HashSet
argument_list|<
name|QName
argument_list|>
argument_list|()
decl_stmt|;
static|static
block|{
name|HEADERS
operator|.
name|add
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
expr_stmt|;
name|HEADERS
operator|.
name|add
argument_list|(
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE11_NS
argument_list|,
literal|"Security"
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param p      */
specifier|public
name|UsernameTokenInterceptor
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
name|PolicyBasedWSS4JOutInterceptor
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
comment|//any username token stuff, assert policies and return
name|assertUsernameTokens
argument_list|(
name|message
argument_list|,
literal|null
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
name|addUsernameToken
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
name|processUsernameToken
argument_list|(
name|message
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|processUsernameToken
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
condition|)
block|{
try|try
block|{
specifier|final
name|WSUsernameTokenPrincipal
name|princ
init|=
name|getPrincipal
argument_list|(
name|child
argument_list|,
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|princ
operator|!=
literal|null
condition|)
block|{
name|Vector
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|v
init|=
operator|new
name|Vector
argument_list|<
name|WSSecurityEngineResult
argument_list|>
argument_list|()
decl_stmt|;
name|v
operator|.
name|add
argument_list|(
literal|0
argument_list|,
operator|new
name|WSSecurityEngineResult
argument_list|(
name|WSConstants
operator|.
name|UT
argument_list|,
name|princ
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
name|Object
argument_list|>
name|results
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|List
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
name|Vector
argument_list|<
name|Object
argument_list|>
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
name|assertUsernameTokens
argument_list|(
name|message
argument_list|,
name|princ
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|WSS4JInInterceptor
operator|.
name|PRINCIPAL_RESULT
argument_list|,
name|princ
argument_list|)
expr_stmt|;
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
name|Subject
name|subject
init|=
name|createSubject
argument_list|(
name|princ
operator|.
name|getName
argument_list|()
argument_list|,
name|princ
operator|.
name|getPassword
argument_list|()
argument_list|,
name|princ
operator|.
name|isPasswordDigest
argument_list|()
argument_list|,
name|princ
operator|.
name|getNonce
argument_list|()
argument_list|,
name|princ
operator|.
name|getCreatedTime
argument_list|()
argument_list|)
decl_stmt|;
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
name|princ
argument_list|,
name|subject
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
specifier|protected
name|WSUsernameTokenPrincipal
name|getPrincipal
parameter_list|(
name|Element
name|tokenElement
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|Object
name|validateProperty
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|VALIDATE_PASSWORD
argument_list|)
decl_stmt|;
if|if
condition|(
name|validateProperty
operator|==
literal|null
operator|||
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|validateProperty
argument_list|)
condition|)
block|{
name|UsernameTokenProcessor
name|p
init|=
operator|new
name|UsernameTokenProcessor
argument_list|()
decl_stmt|;
return|return
name|p
operator|.
name|handleUsernameToken
argument_list|(
name|tokenElement
argument_list|,
name|getCallback
argument_list|(
name|message
argument_list|)
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|parseTokenAndCreatePrincipal
argument_list|(
name|tokenElement
argument_list|)
return|;
block|}
block|}
specifier|protected
name|WSUsernameTokenPrincipal
name|parseTokenAndCreatePrincipal
parameter_list|(
name|Element
name|tokenElement
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
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
name|ws
operator|.
name|security
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
argument_list|)
decl_stmt|;
name|WSUsernameTokenPrincipal
name|principal
init|=
operator|new
name|WSUsernameTokenPrincipal
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
name|principal
operator|.
name|setNonce
argument_list|(
name|ut
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
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
specifier|private
name|UsernameToken
name|assertUsernameTokens
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|WSUsernameTokenPrincipal
name|princ
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
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|SP12Constants
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
if|if
condition|(
name|princ
operator|!=
literal|null
operator|&&
name|tok
operator|.
name|isHashPassword
argument_list|()
operator|!=
name|princ
operator|.
name|isPasswordDigest
argument_list|()
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
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|SP12Constants
operator|.
name|SUPPORTING_TOKENS
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
name|ais
operator|=
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_SUPPORTING_TOKENS
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
return|return
name|tok
return|;
block|}
specifier|private
name|void
name|addUsernameToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
name|UsernameToken
name|tok
init|=
name|assertUsernameTokens
argument_list|(
name|message
argument_list|,
literal|null
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
name|aim
operator|.
name|getAssertionInfo
argument_list|(
name|SP12Constants
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
specifier|private
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
name|n
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
literal|"Security"
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
name|WSConstants
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
name|WSConstants
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
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|el
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|WSConstants
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
name|WSConstants
operator|.
name|XMLNS_NS
argument_list|,
literal|"xmlns:wsse"
argument_list|,
name|WSConstants
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
name|WSConstants
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
name|isNoPassword
argument_list|()
condition|)
block|{
name|WSSecUsernameToken
name|utBuilder
init|=
operator|new
name|WSSecUsernameToken
argument_list|()
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
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|.
name|isHashPassword
argument_list|()
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
specifier|private
name|CallbackHandler
name|getCallback
parameter_list|(
name|SoapMessage
name|message
parameter_list|)
block|{
comment|//Then try to get the password from the given callback handler
name|Object
name|o
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|)
decl_stmt|;
name|CallbackHandler
name|handler
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|o
operator|instanceof
name|CallbackHandler
condition|)
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|o
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|o
operator|instanceof
name|String
condition|)
block|{
try|try
block|{
name|handler
operator|=
operator|(
name|CallbackHandler
operator|)
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|o
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|handler
operator|=
literal|null
expr_stmt|;
block|}
block|}
return|return
name|handler
return|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|(
name|String
name|userName
parameter_list|,
name|UsernameToken
name|info
parameter_list|,
name|int
name|type
parameter_list|,
name|SoapMessage
name|message
parameter_list|)
block|{
comment|//Then try to get the password from the given callback handler
name|CallbackHandler
name|handler
init|=
name|getCallback
argument_list|(
name|message
argument_list|)
decl_stmt|;
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
name|type
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
name|UsernameToken
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
decl_stmt|;
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|assertion
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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
name|UsernameToken
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
decl_stmt|;
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|assertion
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
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

