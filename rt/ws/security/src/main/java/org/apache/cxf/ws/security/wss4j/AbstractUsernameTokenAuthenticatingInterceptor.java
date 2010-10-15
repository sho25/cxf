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
name|io
operator|.
name|IOException
import|;
end_import

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
name|HashMap
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
name|Callback
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
name|security
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
name|common
operator|.
name|security
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
name|phase
operator|.
name|PhaseInterceptorChain
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
name|WSSecurityEngine
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
name|RequestData
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
name|Processor
import|;
end_import

begin_comment
comment|/**  * Base class providing an extensibility point for populating   * javax.security.auth.Subject from a current UsernameToken.  *   * WSS4J requires a password for validating digests which may not be available  * when external security systems provide for the authentication. This class  * implements WSS4J Processor interface so that it can delegate a UsernameToken  * validation to an external system.  *   * In order to handle digests, this class currently creates a new WSS4J Security Engine for  * every request. If clear text passwords are expected then a supportDigestPasswords boolean  * property with a false value can be used to disable creating security engines.  *   * Note that if a UsernameToken containing a clear text password has been encrypted then  * an application is expected to provide a password callback handler for decrypting the token only.       *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractUsernameTokenAuthenticatingInterceptor
extends|extends
name|WSS4JInInterceptor
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
name|AbstractUsernameTokenAuthenticatingInterceptor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|boolean
name|supportDigestPasswords
decl_stmt|;
specifier|public
name|AbstractUsernameTokenAuthenticatingInterceptor
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractUsernameTokenAuthenticatingInterceptor
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
name|super
argument_list|(
name|properties
argument_list|)
expr_stmt|;
name|getAfter
argument_list|()
operator|.
name|add
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
name|setSupportDigestPasswords
parameter_list|(
name|boolean
name|support
parameter_list|)
block|{
name|supportDigestPasswords
operator|=
name|support
expr_stmt|;
block|}
specifier|public
name|boolean
name|getSupportDigestPasswords
parameter_list|()
block|{
return|return
name|supportDigestPasswords
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handleMessage
parameter_list|(
name|SoapMessage
name|msg
parameter_list|)
throws|throws
name|Fault
block|{
name|SecurityToken
name|token
init|=
name|msg
operator|.
name|get
argument_list|(
name|SecurityToken
operator|.
name|class
argument_list|)
decl_stmt|;
name|SecurityContext
name|context
init|=
name|msg
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
name|token
operator|==
literal|null
operator|||
name|context
operator|==
literal|null
operator|||
name|context
operator|.
name|getUserPrincipal
argument_list|()
operator|==
literal|null
condition|)
block|{
name|super
operator|.
name|handleMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
return|return;
block|}
name|UsernameToken
name|ut
init|=
operator|(
name|UsernameToken
operator|)
name|token
decl_stmt|;
name|Subject
name|subject
init|=
name|createSubject
argument_list|(
name|ut
operator|.
name|getName
argument_list|()
argument_list|,
name|ut
operator|.
name|getPassword
argument_list|()
argument_list|,
name|ut
operator|.
name|isHashed
argument_list|()
argument_list|,
name|ut
operator|.
name|getNonce
argument_list|()
argument_list|,
name|ut
operator|.
name|getCreatedTime
argument_list|()
argument_list|)
decl_stmt|;
name|SecurityContext
name|sc
init|=
name|doCreateSecurityContext
argument_list|(
name|context
operator|.
name|getUserPrincipal
argument_list|()
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|msg
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|sc
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|SecurityContext
name|createSecurityContext
parameter_list|(
specifier|final
name|Principal
name|p
parameter_list|)
block|{
name|Message
name|msg
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Current message is not available"
argument_list|)
throw|;
block|}
return|return
name|doCreateSecurityContext
argument_list|(
name|p
argument_list|,
name|msg
operator|.
name|get
argument_list|(
name|Subject
operator|.
name|class
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Creates default SecurityContext which implements isUserInRole using the      * following approach : skip the first Subject principal, and then check optional      * Groups the principal is a member of. Subclasses can override this method and implement      * a custom strategy instead      *         * @param p principal      * @param subject subject       * @return security context      */
specifier|protected
name|SecurityContext
name|doCreateSecurityContext
parameter_list|(
specifier|final
name|Principal
name|p
parameter_list|,
specifier|final
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
specifier|protected
name|void
name|setSubject
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
name|WSSecurityException
block|{
name|Message
name|msg
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|msg
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Current message is not available"
argument_list|)
throw|;
block|}
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
try|try
block|{
name|subject
operator|=
name|createSubject
argument_list|(
name|name
argument_list|,
name|password
argument_list|,
name|isDigest
argument_list|,
name|nonce
argument_list|,
name|created
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|String
name|errorMessage
init|=
literal|"Failed Authentication : Subject has not been created"
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|errorMessage
argument_list|,
name|ex
argument_list|)
throw|;
block|}
if|if
condition|(
name|subject
operator|==
literal|null
operator|||
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
operator|||
operator|!
name|subject
operator|.
name|getPrincipals
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
operator|.
name|getName
argument_list|()
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|String
name|errorMessage
init|=
literal|"Failed Authentication : Invalid Subject"
decl_stmt|;
name|LOG
operator|.
name|severe
argument_list|(
name|errorMessage
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|errorMessage
argument_list|)
throw|;
block|}
name|msg
operator|.
name|put
argument_list|(
name|Subject
operator|.
name|class
argument_list|,
name|subject
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create a Subject representing a current user and its roles.       * This Subject is expected to contain at least one Principal representing a user      * and optionally followed by one or more principal Groups this user is a member of.      * It will also be available in doCreateSecurityContext.         * @param name username      * @param password password      * @param isDigest true if a password digest is used      * @param nonce optional nonce      * @param created optional timestamp      * @return subject      * @throws SecurityException      */
specifier|protected
specifier|abstract
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
function_decl|;
comment|/**      * {@inheritDoc}      *       */
annotation|@
name|Override
specifier|protected
name|CallbackHandler
name|getCallback
parameter_list|(
name|RequestData
name|reqData
parameter_list|,
name|int
name|doAction
parameter_list|,
name|boolean
name|utNoCallbacks
parameter_list|)
throws|throws
name|WSSecurityException
block|{
comment|// Given that a custom UT processor is used for dealing with digests
comment|// no callback handler is required when the request UT contains a digest;
comment|// however a custom callback may still be needed for decrypting the encrypted UT
if|if
condition|(
operator|(
name|doAction
operator|&
name|WSConstants
operator|.
name|UT
operator|)
operator|!=
literal|0
condition|)
block|{
name|CallbackHandler
name|pwdCallback
init|=
literal|null
decl_stmt|;
try|try
block|{
name|pwdCallback
operator|=
name|super
operator|.
name|getCallback
argument_list|(
name|reqData
argument_list|,
name|doAction
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// ignore
block|}
return|return
operator|new
name|SubjectCreatingCallbackHandler
argument_list|(
name|pwdCallback
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getCallback
argument_list|(
name|reqData
argument_list|,
name|doAction
argument_list|,
literal|false
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|WSSecurityEngine
name|getSecurityEngine
parameter_list|(
name|boolean
name|utNoCallbacks
parameter_list|)
block|{
if|if
condition|(
operator|!
name|supportDigestPasswords
condition|)
block|{
return|return
name|super
operator|.
name|getSecurityEngine
argument_list|(
literal|true
argument_list|)
return|;
block|}
name|Map
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
name|profiles
init|=
operator|new
name|HashMap
argument_list|<
name|QName
argument_list|,
name|Object
argument_list|>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
name|Processor
name|processor
init|=
operator|new
name|CustomUsernameTokenProcessor
argument_list|()
decl_stmt|;
name|profiles
operator|.
name|put
argument_list|(
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE_NS
argument_list|,
name|WSConstants
operator|.
name|USERNAME_TOKEN_LN
argument_list|)
argument_list|,
name|processor
argument_list|)
expr_stmt|;
name|profiles
operator|.
name|put
argument_list|(
operator|new
name|QName
argument_list|(
name|WSConstants
operator|.
name|WSSE11_NS
argument_list|,
name|WSConstants
operator|.
name|USERNAME_TOKEN_LN
argument_list|)
argument_list|,
name|processor
argument_list|)
expr_stmt|;
return|return
name|createSecurityEngine
argument_list|(
name|profiles
argument_list|)
return|;
block|}
specifier|protected
class|class
name|SubjectCreatingCallbackHandler
extends|extends
name|DelegatingCallbackHandler
block|{
specifier|public
name|SubjectCreatingCallbackHandler
parameter_list|(
name|CallbackHandler
name|pwdHandler
parameter_list|)
block|{
name|super
argument_list|(
name|pwdHandler
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|handleCallback
parameter_list|(
name|Callback
name|c
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|c
operator|instanceof
name|WSPasswordCallback
condition|)
block|{
name|WSPasswordCallback
name|pc
init|=
operator|(
name|WSPasswordCallback
operator|)
name|c
decl_stmt|;
if|if
condition|(
name|WSConstants
operator|.
name|PASSWORD_TEXT
operator|.
name|equals
argument_list|(
name|pc
operator|.
name|getPasswordType
argument_list|()
argument_list|)
operator|&&
name|pc
operator|.
name|getUsage
argument_list|()
operator|==
name|WSPasswordCallback
operator|.
name|USERNAME_TOKEN_UNKNOWN
condition|)
block|{
name|AbstractUsernameTokenAuthenticatingInterceptor
operator|.
name|this
operator|.
name|setSubject
argument_list|(
name|pc
operator|.
name|getIdentifier
argument_list|()
argument_list|,
name|pc
operator|.
name|getPassword
argument_list|()
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
comment|/**      * Custom UsernameTokenProcessor      * Unfortunately, WSS4J UsernameTokenProcessor makes it impossible to      * override its handleUsernameToken only.       *      */
specifier|protected
class|class
name|CustomUsernameTokenProcessor
extends|extends
name|UsernameTokenProcessorWithoutCallbacks
block|{
annotation|@
name|Override
specifier|protected
name|WSUsernameTokenPrincipal
name|createPrincipal
parameter_list|(
name|String
name|user
parameter_list|,
name|String
name|password
parameter_list|,
name|boolean
name|isHashed
parameter_list|,
name|String
name|nonce
parameter_list|,
name|String
name|createdTime
parameter_list|,
name|String
name|pwType
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|AbstractUsernameTokenAuthenticatingInterceptor
operator|.
name|this
operator|.
name|setSubject
argument_list|(
name|user
argument_list|,
name|password
argument_list|,
name|isHashed
argument_list|,
name|nonce
argument_list|,
name|createdTime
argument_list|)
expr_stmt|;
return|return
name|super
operator|.
name|createPrincipal
argument_list|(
name|user
argument_list|,
name|password
argument_list|,
name|isHashed
argument_list|,
name|nonce
argument_list|,
name|createdTime
argument_list|,
name|pwType
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

