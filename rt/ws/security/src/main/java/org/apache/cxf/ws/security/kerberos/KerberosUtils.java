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
name|wss4j
operator|.
name|common
operator|.
name|ext
operator|.
name|WSSecurityException
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|KerberosUtils
block|{
specifier|private
name|KerberosUtils
parameter_list|()
block|{
comment|//utility class
block|}
specifier|public
specifier|static
name|KerberosClient
name|getClient
parameter_list|(
name|Message
name|message
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|WSSecurityException
block|{
name|KerberosClient
name|client
init|=
operator|(
name|KerberosClient
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|KERBEROS_CLIENT
argument_list|)
decl_stmt|;
if|if
condition|(
name|client
operator|==
literal|null
condition|)
block|{
name|client
operator|=
operator|new
name|KerberosClient
argument_list|()
expr_stmt|;
name|String
name|jaasContext
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
name|KERBEROS_JAAS_CONTEXT_NAME
argument_list|)
decl_stmt|;
name|String
name|kerberosSpn
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
name|KERBEROS_SPN
argument_list|)
decl_stmt|;
try|try
block|{
name|CallbackHandler
name|callbackHandler
init|=
name|SecurityUtils
operator|.
name|getCallbackHandler
argument_list|(
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
argument_list|)
decl_stmt|;
name|client
operator|.
name|setCallbackHandler
argument_list|(
name|callbackHandler
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
name|ex
argument_list|)
throw|;
block|}
name|boolean
name|useCredentialDelegation
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|KERBEROS_USE_CREDENTIAL_DELEGATION
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|isInServiceNameForm
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|KERBEROS_IS_USERNAME_IN_SERVICENAME_FORM
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|boolean
name|requestCredentialDelegation
init|=
name|MessageUtils
operator|.
name|getContextualBoolean
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|KERBEROS_REQUEST_CREDENTIAL_DELEGATION
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|client
operator|.
name|setContextName
argument_list|(
name|jaasContext
argument_list|)
expr_stmt|;
name|client
operator|.
name|setServiceName
argument_list|(
name|kerberosSpn
argument_list|)
expr_stmt|;
name|client
operator|.
name|setUseDelegatedCredential
argument_list|(
name|useCredentialDelegation
argument_list|)
expr_stmt|;
name|client
operator|.
name|setUsernameServiceNameForm
argument_list|(
name|isInServiceNameForm
argument_list|)
expr_stmt|;
name|client
operator|.
name|setRequestCredentialDelegation
argument_list|(
name|requestCredentialDelegation
argument_list|)
expr_stmt|;
block|}
return|return
name|client
return|;
block|}
block|}
end_class

end_unit

