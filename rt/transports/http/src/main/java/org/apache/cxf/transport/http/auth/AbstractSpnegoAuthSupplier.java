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
name|URL
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
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|NameCallback
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
name|PasswordCallback
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
specifier|public
name|String
name|getAuthorization
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
name|URL
name|currentURL
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
name|currentURL
argument_list|)
decl_stmt|;
name|boolean
name|useKerberosOid
init|=
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|message
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
catch|catch
parameter_list|(
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
comment|/**      * Create and return service ticket token      *       * @param authPolicy      * @param context      * @return      * @throws GSSException      * @throws LoginException      */
specifier|private
name|byte
index|[]
name|getToken
parameter_list|(
name|AuthorizationPolicy
name|authPolicy
parameter_list|,
specifier|final
name|GSSContext
name|context
parameter_list|)
throws|throws
name|GSSException
throws|,
name|LoginException
block|{
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
name|authPolicy
operator|==
literal|null
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|authPolicy
operator|.
name|getUserName
argument_list|()
argument_list|)
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
name|LoginContext
name|lc
init|=
operator|new
name|LoginContext
argument_list|(
name|authPolicy
operator|.
name|getAuthorization
argument_list|()
argument_list|,
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
argument_list|)
decl_stmt|;
name|lc
operator|.
name|login
argument_list|()
expr_stmt|;
try|try
block|{
return|return
operator|(
name|byte
index|[]
operator|)
name|Subject
operator|.
name|doAs
argument_list|(
name|lc
operator|.
name|getSubject
argument_list|()
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
comment|/**      * Create and return a service ticket token for a given service principal      * name      *       * @param authPolicy      * @param spn      * @return service ticket token      * @throws GSSException      * @throws LoginException      */
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
literal|null
argument_list|)
decl_stmt|;
name|GSSCredential
name|delegatedCred
init|=
operator|(
name|GSSCredential
operator|)
name|message
operator|.
name|get
argument_list|(
name|GSSCredential
operator|.
name|class
operator|.
name|getName
argument_list|()
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
return|return
name|getToken
argument_list|(
name|delegatedCred
operator|==
literal|null
condition|?
name|authPolicy
else|:
literal|null
argument_list|,
name|context
argument_list|)
return|;
block|}
specifier|protected
name|boolean
name|isCredDelegationRequired
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Object
name|prop
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|PROPERTY_REQUIRE_CRED_DELEGATION
argument_list|)
decl_stmt|;
return|return
name|prop
operator|==
literal|null
condition|?
name|credDelegation
else|:
name|MessageUtils
operator|.
name|isTrue
argument_list|(
name|prop
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getCompleteServicePrincipalName
parameter_list|(
name|URL
name|currentURL
parameter_list|)
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
name|currentURL
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
specifier|static
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
specifier|final
name|CallbackHandler
name|handler
init|=
operator|new
name|CallbackHandler
argument_list|()
block|{
specifier|public
name|void
name|handle
parameter_list|(
specifier|final
name|Callback
index|[]
name|callback
parameter_list|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|callback
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callback
index|[
name|i
index|]
operator|instanceof
name|NameCallback
condition|)
block|{
specifier|final
name|NameCallback
name|nameCallback
init|=
operator|(
name|NameCallback
operator|)
name|callback
index|[
name|i
index|]
decl_stmt|;
name|nameCallback
operator|.
name|setName
argument_list|(
name|username
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|callback
index|[
name|i
index|]
operator|instanceof
name|PasswordCallback
condition|)
block|{
specifier|final
name|PasswordCallback
name|passCallback
init|=
operator|(
name|PasswordCallback
operator|)
name|callback
index|[
name|i
index|]
decl_stmt|;
name|passCallback
operator|.
name|setPassword
argument_list|(
name|password
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
decl_stmt|;
return|return
name|handler
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
block|}
end_class

end_unit

