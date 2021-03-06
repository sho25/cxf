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
name|jaxrs
operator|.
name|security
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedActionException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PrivilegedExceptionAction
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
name|security
operator|.
name|auth
operator|.
name|login
operator|.
name|Configuration
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
name|login
operator|.
name|LoginContext
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
name|login
operator|.
name|LoginException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|ContainerRequestFilter
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|container
operator|.
name|PreMatching
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|HttpHeaders
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|SimplePrincipal
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
name|SimpleSecurityContext
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
name|Base64Exception
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
name|Base64Utility
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|jaxrs
operator|.
name|utils
operator|.
name|ExceptionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|JAXRSUtils
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ietf
operator|.
name|jgss
operator|.
name|GSSContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ietf
operator|.
name|jgss
operator|.
name|GSSException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ietf
operator|.
name|jgss
operator|.
name|GSSManager
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ietf
operator|.
name|jgss
operator|.
name|GSSName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ietf
operator|.
name|jgss
operator|.
name|Oid
import|;
end_import

begin_class
annotation|@
name|PreMatching
specifier|public
class|class
name|KerberosAuthenticationFilter
implements|implements
name|ContainerRequestFilter
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
name|KerberosAuthenticationFilter
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NEGOTIATE_SCHEME
init|=
literal|"Negotiate"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|PROPERTY_USE_KERBEROS_OID
init|=
literal|"auth.spnego.useKerberosOid"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KERBEROS_OID
init|=
literal|"1.2.840.113554.1.2.2"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SPNEGO_OID
init|=
literal|"1.3.6.1.5.5.2"
decl_stmt|;
specifier|private
name|MessageContext
name|messageContext
decl_stmt|;
specifier|private
name|CallbackHandler
name|callbackHandler
decl_stmt|;
specifier|private
name|Configuration
name|loginConfig
decl_stmt|;
specifier|private
name|String
name|loginContextName
init|=
literal|""
decl_stmt|;
specifier|private
name|String
name|servicePrincipalName
decl_stmt|;
specifier|private
name|String
name|realm
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|context
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|authHeaders
init|=
name|messageContext
operator|.
name|getHttpHeaders
argument_list|()
operator|.
name|getRequestHeader
argument_list|(
name|HttpHeaders
operator|.
name|AUTHORIZATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|authHeaders
operator|==
literal|null
operator|||
name|authHeaders
operator|.
name|size
argument_list|()
operator|!=
literal|1
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"No Authorization header is available"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|getFaultResponse
argument_list|()
argument_list|)
throw|;
block|}
name|String
index|[]
name|authPair
init|=
name|authHeaders
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
if|if
condition|(
name|authPair
operator|.
name|length
operator|!=
literal|2
operator|||
operator|!
name|NEGOTIATE_SCHEME
operator|.
name|equalsIgnoreCase
argument_list|(
name|authPair
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Negotiate Authorization scheme is expected"
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|getFaultResponse
argument_list|()
argument_list|)
throw|;
block|}
name|byte
index|[]
name|serviceTicket
init|=
name|getServiceTicket
argument_list|(
name|authPair
index|[
literal|1
index|]
argument_list|)
decl_stmt|;
try|try
block|{
name|Subject
name|serviceSubject
init|=
name|loginAndGetSubject
argument_list|()
decl_stmt|;
name|GSSContext
name|gssContext
init|=
name|createGSSContext
argument_list|()
decl_stmt|;
name|Subject
operator|.
name|doAs
argument_list|(
name|serviceSubject
argument_list|,
operator|new
name|ValidateServiceTicketAction
argument_list|(
name|gssContext
argument_list|,
name|serviceTicket
argument_list|)
argument_list|)
expr_stmt|;
name|GSSName
name|srcName
init|=
name|gssContext
operator|.
name|getSrcName
argument_list|()
decl_stmt|;
if|if
condition|(
name|srcName
operator|==
literal|null
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|getFaultResponse
argument_list|()
argument_list|)
throw|;
block|}
name|String
name|complexUserName
init|=
name|srcName
operator|.
name|toString
argument_list|()
decl_stmt|;
name|String
name|simpleUserName
init|=
name|complexUserName
decl_stmt|;
name|int
name|index
init|=
name|simpleUserName
operator|.
name|lastIndexOf
argument_list|(
literal|'@'
argument_list|)
decl_stmt|;
if|if
condition|(
name|index
operator|>
literal|0
condition|)
block|{
name|simpleUserName
operator|=
name|simpleUserName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|index
argument_list|)
expr_stmt|;
block|}
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|SecurityContext
operator|.
name|class
argument_list|,
name|createSecurityContext
argument_list|(
name|simpleUserName
argument_list|,
name|complexUserName
argument_list|,
name|gssContext
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|gssContext
operator|.
name|getCredDelegState
argument_list|()
condition|)
block|{
name|gssContext
operator|.
name|dispose
argument_list|()
expr_stmt|;
name|gssContext
operator|=
literal|null
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|LoginException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Unsuccessful JAAS login for the service principal: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
name|e
argument_list|,
name|getFaultResponse
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|GSSException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"GSS API exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
name|e
argument_list|,
name|getFaultResponse
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"PrivilegedActionException: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
name|e
argument_list|,
name|getFaultResponse
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|SecurityContext
name|createSecurityContext
parameter_list|(
name|String
name|simpleUserName
parameter_list|,
name|String
name|complexUserName
parameter_list|,
name|GSSContext
name|gssContext
parameter_list|)
block|{
return|return
operator|new
name|KerberosSecurityContext
argument_list|(
operator|new
name|KerberosPrincipal
argument_list|(
name|simpleUserName
argument_list|,
name|complexUserName
argument_list|)
argument_list|,
name|gssContext
argument_list|)
return|;
block|}
specifier|protected
name|GSSContext
name|createGSSContext
parameter_list|()
throws|throws
name|GSSException
block|{
name|boolean
name|useKerberosOid
init|=
name|PropertyUtils
operator|.
name|isTrue
argument_list|(
name|messageContext
operator|.
name|getContextualProperty
argument_list|(
name|PROPERTY_USE_KERBEROS_OID
argument_list|)
argument_list|)
decl_stmt|;
name|Oid
name|oid
init|=
operator|new
name|Oid
argument_list|(
name|useKerberosOid
condition|?
name|KERBEROS_OID
else|:
name|SPNEGO_OID
argument_list|)
decl_stmt|;
name|GSSManager
name|gssManager
init|=
name|GSSManager
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|String
name|spn
init|=
name|getCompleteServicePrincipalName
argument_list|()
decl_stmt|;
name|GSSName
name|gssService
init|=
name|gssManager
operator|.
name|createName
argument_list|(
name|spn
argument_list|,
literal|null
argument_list|)
decl_stmt|;
return|return
name|gssManager
operator|.
name|createContext
argument_list|(
name|gssService
operator|.
name|canonicalize
argument_list|(
name|oid
argument_list|)
argument_list|,
name|oid
argument_list|,
literal|null
argument_list|,
name|GSSContext
operator|.
name|DEFAULT_LIFETIME
argument_list|)
return|;
block|}
specifier|protected
name|Subject
name|loginAndGetSubject
parameter_list|()
throws|throws
name|LoginException
block|{
comment|// The login without a callback can work if
comment|// - Kerberos keytabs are used with a principal name set in the JAAS config
comment|// - Kerberos is integrated into the OS logon process
comment|//   meaning that a process which runs this code has the
comment|//   user identity
name|LoginContext
name|lc
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|loginContextName
argument_list|)
operator|||
name|loginConfig
operator|!=
literal|null
condition|)
block|{
name|lc
operator|=
operator|new
name|LoginContext
argument_list|(
name|loginContextName
argument_list|,
literal|null
argument_list|,
name|callbackHandler
argument_list|,
name|loginConfig
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"LoginContext can not be initialized"
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|LoginException
argument_list|()
throw|;
block|}
name|lc
operator|.
name|login
argument_list|()
expr_stmt|;
return|return
name|lc
operator|.
name|getSubject
argument_list|()
return|;
block|}
specifier|private
name|byte
index|[]
name|getServiceTicket
parameter_list|(
name|String
name|encodedServiceTicket
parameter_list|)
block|{
try|try
block|{
return|return
name|Base64Utility
operator|.
name|decode
argument_list|(
name|encodedServiceTicket
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|Base64Exception
name|ex
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotAuthorizedException
argument_list|(
literal|null
argument_list|,
name|getFaultResponse
argument_list|()
argument_list|)
throw|;
block|}
block|}
specifier|private
specifier|static
name|Response
name|getFaultResponse
parameter_list|()
block|{
return|return
name|JAXRSUtils
operator|.
name|toResponseBuilder
argument_list|(
literal|401
argument_list|)
operator|.
name|header
argument_list|(
name|HttpHeaders
operator|.
name|WWW_AUTHENTICATE
argument_list|,
name|NEGOTIATE_SCHEME
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getCompleteServicePrincipalName
parameter_list|()
block|{
name|String
name|name
init|=
name|servicePrincipalName
operator|==
literal|null
condition|?
literal|"HTTP/"
operator|+
name|messageContext
operator|.
name|getUriInfo
argument_list|()
operator|.
name|getBaseUri
argument_list|()
operator|.
name|getHost
argument_list|()
else|:
name|servicePrincipalName
decl_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|name
operator|+=
literal|"@"
operator|+
name|realm
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|context
parameter_list|)
block|{
name|this
operator|.
name|messageContext
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|void
name|setLoginContextName
parameter_list|(
name|String
name|contextName
parameter_list|)
block|{
name|this
operator|.
name|loginContextName
operator|=
name|contextName
expr_stmt|;
block|}
specifier|public
name|void
name|setServicePrincipalName
parameter_list|(
name|String
name|servicePrincipalName
parameter_list|)
block|{
name|this
operator|.
name|servicePrincipalName
operator|=
name|servicePrincipalName
expr_stmt|;
block|}
specifier|public
name|void
name|setRealm
parameter_list|(
name|String
name|realm
parameter_list|)
block|{
name|this
operator|.
name|realm
operator|=
name|realm
expr_stmt|;
block|}
specifier|public
name|void
name|setCallbackHandler
parameter_list|(
name|CallbackHandler
name|callbackHandler
parameter_list|)
block|{
name|this
operator|.
name|callbackHandler
operator|=
name|callbackHandler
expr_stmt|;
block|}
specifier|private
specifier|static
specifier|final
class|class
name|ValidateServiceTicketAction
implements|implements
name|PrivilegedExceptionAction
argument_list|<
name|byte
index|[]
argument_list|>
block|{
specifier|private
specifier|final
name|GSSContext
name|context
decl_stmt|;
specifier|private
specifier|final
name|byte
index|[]
name|token
decl_stmt|;
specifier|private
name|ValidateServiceTicketAction
parameter_list|(
name|GSSContext
name|context
parameter_list|,
name|byte
index|[]
name|token
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
name|this
operator|.
name|token
operator|=
name|token
expr_stmt|;
block|}
specifier|public
name|byte
index|[]
name|run
parameter_list|()
throws|throws
name|GSSException
block|{
return|return
name|context
operator|.
name|acceptSecContext
argument_list|(
name|token
argument_list|,
literal|0
argument_list|,
name|token
operator|.
name|length
argument_list|)
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|KerberosPrincipal
extends|extends
name|SimplePrincipal
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|private
name|String
name|complexName
decl_stmt|;
specifier|public
name|KerberosPrincipal
parameter_list|(
name|String
name|simpleName
parameter_list|,
name|String
name|complexName
parameter_list|)
block|{
name|super
argument_list|(
name|simpleName
argument_list|)
expr_stmt|;
name|this
operator|.
name|complexName
operator|=
name|complexName
expr_stmt|;
block|}
specifier|public
name|String
name|getKerberosName
parameter_list|()
block|{
return|return
name|complexName
return|;
block|}
block|}
specifier|public
specifier|static
class|class
name|KerberosSecurityContext
extends|extends
name|SimpleSecurityContext
block|{
specifier|private
name|GSSContext
name|context
decl_stmt|;
specifier|public
name|KerberosSecurityContext
parameter_list|(
name|KerberosPrincipal
name|principal
parameter_list|,
name|GSSContext
name|context
parameter_list|)
block|{
name|super
argument_list|(
name|principal
argument_list|)
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|context
expr_stmt|;
block|}
specifier|public
name|GSSContext
name|getGSSContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
block|}
end_class

end_unit

