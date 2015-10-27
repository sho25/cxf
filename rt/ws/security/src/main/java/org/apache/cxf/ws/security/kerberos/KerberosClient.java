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
name|kerberos
package|;
end_package

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
name|crypto
operator|.
name|SecretKey
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
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
name|configuration
operator|.
name|Configurable
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
name|wss4j
operator|.
name|common
operator|.
name|util
operator|.
name|KeyUtils
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
name|engine
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
name|message
operator|.
name|token
operator|.
name|KerberosSecurity
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

begin_comment
comment|/**  * A class that obtains a ticket from a KDC and wraps it in a SecurityToken object.  */
end_comment

begin_class
specifier|public
class|class
name|KerberosClient
implements|implements
name|Configurable
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
name|KerberosClient
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|name
init|=
literal|"default.kerberos-client"
decl_stmt|;
specifier|private
name|String
name|serviceName
decl_stmt|;
specifier|private
name|CallbackHandler
name|callbackHandler
decl_stmt|;
specifier|private
name|String
name|contextName
decl_stmt|;
specifier|private
name|WSSConfig
name|wssConfig
init|=
name|WSSConfig
operator|.
name|getNewInstance
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|requestCredentialDelegation
decl_stmt|;
specifier|private
name|boolean
name|isUsernameServiceNameForm
decl_stmt|;
specifier|private
name|boolean
name|useDelegatedCredential
decl_stmt|;
annotation|@
name|Deprecated
specifier|public
name|KerberosClient
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{     }
specifier|public
name|KerberosClient
parameter_list|()
block|{     }
specifier|public
name|String
name|getBeanName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
comment|/**      * Get the JAAS Login context name to use.      * @return the JAAS Login context name to use      */
specifier|public
name|String
name|getContextName
parameter_list|()
block|{
return|return
name|contextName
return|;
block|}
comment|/**      * Set the JAAS Login context name to use.      * @param contextName the JAAS Login context name to use      */
specifier|public
name|void
name|setContextName
parameter_list|(
name|String
name|contextName
parameter_list|)
block|{
name|this
operator|.
name|contextName
operator|=
name|contextName
expr_stmt|;
block|}
comment|/**      * @deprecated      * Get the JAAS Login module name to use.      * @return the JAAS Login module name to use      */
specifier|public
name|String
name|getJaasLoginModuleName
parameter_list|()
block|{
return|return
name|contextName
return|;
block|}
comment|/**      * @deprecated      * Set the JAAS Login module name to use.      * @param jaasLoginModuleName the JAAS Login module name to use      */
specifier|public
name|void
name|setJaasLoginModuleName
parameter_list|(
name|String
name|jaasLoginModuleName
parameter_list|)
block|{
name|this
operator|.
name|contextName
operator|=
name|jaasLoginModuleName
expr_stmt|;
block|}
comment|/**      * Get the CallbackHandler to use with the LoginContext      * @return the CallbackHandler to use with the LoginContext      */
specifier|public
name|CallbackHandler
name|getCallbackHandler
parameter_list|()
block|{
return|return
name|callbackHandler
return|;
block|}
comment|/**      * Set the CallbackHandler to use with the LoginContext. It can be null.      * @param callbackHandler the CallbackHandler to use with the LoginContext      */
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
comment|/**      * The name of the service to use when contacting the KDC.      * @param serviceName the name of the service to use when contacting the KDC      */
specifier|public
name|void
name|setServiceName
parameter_list|(
name|String
name|serviceName
parameter_list|)
block|{
name|this
operator|.
name|serviceName
operator|=
name|serviceName
expr_stmt|;
block|}
comment|/**      * Get the name of the service to use when contacting the KDC.      * @return the name of the service to use when contacting the KDC      */
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
name|serviceName
return|;
block|}
specifier|public
name|SecurityToken
name|requestSecurityToken
parameter_list|()
throws|throws
name|Exception
block|{
comment|// See if we have a delegated Credential to use
name|Message
name|message
init|=
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
name|GSSCredential
name|delegatedCredential
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|useDelegatedCredential
condition|)
block|{
name|Object
name|obj
init|=
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|DELEGATED_CREDENTIAL
argument_list|)
decl_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|GSSCredential
condition|)
block|{
name|delegatedCredential
operator|=
operator|(
name|GSSCredential
operator|)
name|obj
expr_stmt|;
block|}
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
literal|"Requesting Kerberos ticket for "
operator|+
name|serviceName
operator|+
literal|" using JAAS Login Module: "
operator|+
name|getContextName
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|KerberosSecurity
name|bst
init|=
operator|new
name|KerberosSecurity
argument_list|(
name|DOMUtils
operator|.
name|createDocument
argument_list|()
argument_list|)
decl_stmt|;
name|bst
operator|.
name|retrieveServiceTicket
argument_list|(
name|getContextName
argument_list|()
argument_list|,
name|callbackHandler
argument_list|,
name|serviceName
argument_list|,
name|isUsernameServiceNameForm
argument_list|,
name|requestCredentialDelegation
argument_list|,
name|delegatedCredential
argument_list|)
expr_stmt|;
name|bst
operator|.
name|addWSUNamespace
argument_list|()
expr_stmt|;
name|bst
operator|.
name|setID
argument_list|(
name|wssConfig
operator|.
name|getIdAllocator
argument_list|()
operator|.
name|createSecureId
argument_list|(
literal|"BST-"
argument_list|,
name|bst
argument_list|)
argument_list|)
expr_stmt|;
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|(
name|bst
operator|.
name|getID
argument_list|()
argument_list|)
decl_stmt|;
name|token
operator|.
name|setToken
argument_list|(
name|bst
operator|.
name|getElement
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setWsuId
argument_list|(
name|bst
operator|.
name|getID
argument_list|()
argument_list|)
expr_stmt|;
name|token
operator|.
name|setData
argument_list|(
name|bst
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
name|SecretKey
name|secretKey
init|=
name|bst
operator|.
name|getSecretKey
argument_list|()
decl_stmt|;
if|if
condition|(
name|secretKey
operator|!=
literal|null
condition|)
block|{
name|token
operator|.
name|setKey
argument_list|(
name|secretKey
argument_list|)
expr_stmt|;
name|token
operator|.
name|setSecret
argument_list|(
name|secretKey
operator|.
name|getEncoded
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|String
name|sha1
init|=
name|Base64
operator|.
name|encode
argument_list|(
name|KeyUtils
operator|.
name|generateDigest
argument_list|(
name|bst
operator|.
name|getToken
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|token
operator|.
name|setSHA1
argument_list|(
name|sha1
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTokenType
argument_list|(
name|bst
operator|.
name|getValueType
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|token
return|;
block|}
specifier|public
name|boolean
name|isUsernameServiceNameForm
parameter_list|()
block|{
return|return
name|isUsernameServiceNameForm
return|;
block|}
specifier|public
name|void
name|setUsernameServiceNameForm
parameter_list|(
name|boolean
name|usernameServiceNameForm
parameter_list|)
block|{
name|this
operator|.
name|isUsernameServiceNameForm
operator|=
name|usernameServiceNameForm
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRequestCredentialDelegation
parameter_list|()
block|{
return|return
name|requestCredentialDelegation
return|;
block|}
specifier|public
name|void
name|setRequestCredentialDelegation
parameter_list|(
name|boolean
name|requestCredentialDelegation
parameter_list|)
block|{
name|this
operator|.
name|requestCredentialDelegation
operator|=
name|requestCredentialDelegation
expr_stmt|;
block|}
specifier|public
name|boolean
name|isUseDelegatedCredential
parameter_list|()
block|{
return|return
name|useDelegatedCredential
return|;
block|}
specifier|public
name|void
name|setUseDelegatedCredential
parameter_list|(
name|boolean
name|useDelegatedCredential
parameter_list|)
block|{
name|this
operator|.
name|useDelegatedCredential
operator|=
name|useDelegatedCredential
expr_stmt|;
block|}
block|}
end_class

end_unit

