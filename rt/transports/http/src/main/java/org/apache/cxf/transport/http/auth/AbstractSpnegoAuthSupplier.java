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
name|transport
operator|.
name|http
operator|.
name|auth
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|InetAddress
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|NamePasswordCallbackHandler
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
name|GSSCredential
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
specifier|public
specifier|abstract
class|class
name|AbstractSpnegoAuthSupplier
block|{
specifier|protected
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractSpnegoAuthSupplier
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * Can be set on the client properties. If set to true then the kerberos oid is used      * instead of the default spnego OID      */
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
name|PROPERTY_REQUIRE_CRED_DELEGATION
init|=
literal|"auth.spnego.requireCredDelegation"
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
name|String
name|servicePrincipalName
decl_stmt|;
specifier|private
name|String
name|realm
decl_stmt|;
specifier|private
name|boolean
name|credDelegation
decl_stmt|;
specifier|private
name|Configuration
name|loginConfig
decl_stmt|;
specifier|private
name|Oid
name|serviceNameType
decl_stmt|;
specifier|private
name|boolean
name|useCanonicalHostname
decl_stmt|;
specifier|public
name|String
name|getAuthorization
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
name|URI
name|currentURI
parameter_list|,
name|Message
name|message
parameter_list|)
block|{
if|if
condition|(
operator|!
name|HttpAuthHeader
operator|.
name|AUTH_TYPE_NEGOTIATE
operator|.
name|equals
argument_list|(
name|authPolicy
operator|.
name|getAuthorizationType
argument_list|()
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
try|try
block|{
name|String
name|spn
init|=
name|getCompleteServicePrincipalName
argument_list|(
name|currentURI
argument_list|)
decl_stmt|;
name|boolean
name|useKerberosOid
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|PROPERTY_USE_KERBEROS_OID
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
name|byte
index|[]
name|token
init|=
name|getToken
argument_list|(
name|authPolicy
argument_list|,
name|spn
argument_list|,
name|oid
argument_list|,
name|message
argument_list|)
decl_stmt|;
return|return
name|HttpAuthHeader
operator|.
name|AUTH_TYPE_NEGOTIATE
operator|+
literal|" "
operator|+
name|Base64Utility
operator|.
name|encode
argument_list|(
name|token
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|LoginException
decl||
name|GSSException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
comment|/**      * Create and return a service ticket token for a given service principal      * name      *      * @param authPolicy      * @param spn      * @return service ticket token      * @throws GSSException      * @throws LoginException      */
specifier|private
name|byte
index|[]
name|getToken
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
name|String
name|spn
parameter_list|,
name|Oid
name|oid
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|GSSException
throws|,
name|LoginException
block|{
name|GSSCredential
name|delegatedCred
init|=
operator|(
name|GSSCredential
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|GSSCredential
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|Subject
name|subject
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|authPolicy
operator|!=
literal|null
operator|&&
name|delegatedCred
operator|==
literal|null
condition|)
block|{
name|String
name|contextName
init|=
name|authPolicy
operator|.
name|getAuthorization
argument_list|()
decl_stmt|;
if|if
condition|(
name|contextName
operator|==
literal|null
condition|)
block|{
name|contextName
operator|=
literal|""
expr_stmt|;
block|}
if|if
condition|(
operator|!
operator|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|authPolicy
operator|.
name|getUserName
argument_list|()
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|contextName
argument_list|)
operator|&&
name|loginConfig
operator|==
literal|null
operator|)
condition|)
block|{
name|CallbackHandler
name|callbackHandler
init|=
name|getUsernamePasswordHandler
argument_list|(
name|authPolicy
operator|.
name|getUserName
argument_list|()
argument_list|,
name|authPolicy
operator|.
name|getPassword
argument_list|()
argument_list|)
decl_stmt|;
name|LoginContext
name|lc
init|=
operator|new
name|LoginContext
argument_list|(
name|contextName
argument_list|,
literal|null
argument_list|,
name|callbackHandler
argument_list|,
name|loginConfig
argument_list|)
decl_stmt|;
name|lc
operator|.
name|login
argument_list|()
expr_stmt|;
name|subject
operator|=
name|lc
operator|.
name|getSubject
argument_list|()
expr_stmt|;
block|}
block|}
name|GSSManager
name|manager
init|=
name|GSSManager
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|GSSName
name|serverName
init|=
name|manager
operator|.
name|createName
argument_list|(
name|spn
argument_list|,
name|serviceNameType
argument_list|)
decl_stmt|;
name|GSSContext
name|context
init|=
name|manager
operator|.
name|createContext
argument_list|(
name|serverName
operator|.
name|canonicalize
argument_list|(
name|oid
argument_list|)
argument_list|,
name|oid
argument_list|,
name|delegatedCred
argument_list|,
name|GSSContext
operator|.
name|DEFAULT_LIFETIME
argument_list|)
decl_stmt|;
name|context
operator|.
name|requestCredDeleg
argument_list|(
name|isCredDelegationRequired
argument_list|(
name|message
argument_list|)
argument_list|)
expr_stmt|;
comment|// If the delegated cred is not null then we only need the context to
comment|// immediately return a ticket based on this credential without attempting
comment|// to log on again
specifier|final
name|byte
index|[]
name|token
init|=
operator|new
name|byte
index|[
literal|0
index|]
decl_stmt|;
if|if
condition|(
name|delegatedCred
operator|!=
literal|null
condition|)
block|{
return|return
name|context
operator|.
name|initSecContext
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
name|decorateSubject
argument_list|(
name|subject
argument_list|)
expr_stmt|;
try|try
block|{
return|return
name|Subject
operator|.
name|doAs
argument_list|(
name|subject
argument_list|,
operator|new
name|CreateServiceTicketAction
argument_list|(
name|context
argument_list|,
name|token
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|PrivilegedActionException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getCause
argument_list|()
operator|instanceof
name|GSSException
condition|)
block|{
throw|throw
operator|(
name|GSSException
operator|)
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|SEVERE
argument_list|,
literal|"initSecContext"
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
comment|// Allow subclasses to decorate the Subject if required.
specifier|protected
name|void
name|decorateSubject
parameter_list|(
name|Subject
name|subject
parameter_list|)
block|{      }
specifier|protected
name|boolean
name|isCredDelegationRequired
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
return|return
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|PROPERTY_REQUIRE_CRED_DELEGATION
argument_list|,
name|credDelegation
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getCompleteServicePrincipalName
parameter_list|(
name|URI
name|currentURI
parameter_list|)
block|{
name|String
name|name
decl_stmt|;
if|if
condition|(
name|servicePrincipalName
operator|==
literal|null
condition|)
block|{
name|String
name|host
init|=
name|currentURI
operator|.
name|getHost
argument_list|()
decl_stmt|;
if|if
condition|(
name|useCanonicalHostname
condition|)
block|{
name|host
operator|=
name|getCanonicalHostname
argument_list|(
name|host
argument_list|)
expr_stmt|;
block|}
name|name
operator|=
literal|"HTTP/"
operator|+
name|host
expr_stmt|;
block|}
else|else
block|{
name|name
operator|=
name|servicePrincipalName
expr_stmt|;
block|}
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
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Service Principal Name is "
operator|+
name|name
argument_list|)
expr_stmt|;
block|}
return|return
name|name
return|;
block|}
specifier|private
name|String
name|getCanonicalHostname
parameter_list|(
name|String
name|hostname
parameter_list|)
block|{
name|String
name|canonicalHostname
init|=
name|hostname
decl_stmt|;
try|try
block|{
name|InetAddress
name|in
init|=
name|InetAddress
operator|.
name|getByName
argument_list|(
name|hostname
argument_list|)
decl_stmt|;
name|canonicalHostname
operator|=
name|in
operator|.
name|getCanonicalHostName
argument_list|()
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"resolved hostname="
operator|+
name|hostname
operator|+
literal|" to canonicalHostname="
operator|+
name|canonicalHostname
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|,
literal|"unable to resolve canonical hostname"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|canonicalHostname
return|;
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
specifier|private
specifier|static
specifier|final
class|class
name|CreateServiceTicketAction
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
name|CreateServiceTicketAction
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
name|initSecContext
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
name|CallbackHandler
name|getUsernamePasswordHandler
parameter_list|(
specifier|final
name|String
name|username
parameter_list|,
specifier|final
name|String
name|password
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|username
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|NamePasswordCallbackHandler
argument_list|(
name|username
argument_list|,
name|password
argument_list|)
return|;
block|}
specifier|public
name|void
name|setCredDelegation
parameter_list|(
name|boolean
name|delegation
parameter_list|)
block|{
name|this
operator|.
name|credDelegation
operator|=
name|delegation
expr_stmt|;
block|}
specifier|public
name|void
name|setLoginConfig
parameter_list|(
name|Configuration
name|config
parameter_list|)
block|{
name|this
operator|.
name|loginConfig
operator|=
name|config
expr_stmt|;
block|}
specifier|public
name|Oid
name|getServiceNameType
parameter_list|()
block|{
return|return
name|serviceNameType
return|;
block|}
specifier|public
name|void
name|setServiceNameType
parameter_list|(
name|Oid
name|serviceNameType
parameter_list|)
block|{
name|this
operator|.
name|serviceNameType
operator|=
name|serviceNameType
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUseCanonicalHostname
parameter_list|()
block|{
return|return
name|useCanonicalHostname
return|;
block|}
specifier|public
name|void
name|setUseCanonicalHostname
parameter_list|(
name|boolean
name|useCanonicalHostname
parameter_list|)
block|{
name|this
operator|.
name|useCanonicalHostname
operator|=
name|useCanonicalHostname
expr_stmt|;
block|}
block|}
end_class

end_unit

