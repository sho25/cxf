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
name|util
operator|.
name|Collection
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
name|spnego
operator|.
name|SpnegoTokenContext
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
name|Base64
import|;
end_import

begin_class
class|class
name|SpnegoContextTokenOutInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|SoapMessage
argument_list|>
block|{
specifier|public
name|SpnegoContextTokenOutInterceptor
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
name|SoapMessage
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
name|SPNEGO_CONTEXT_TOKEN
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
name|NegotiationUtils
operator|.
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
name|tok
operator|=
name|issueToken
argument_list|(
name|message
argument_list|,
name|aim
argument_list|)
expr_stmt|;
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
name|NegotiationUtils
operator|.
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
comment|// server side should be checked on the way in
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
name|SecurityToken
name|issueToken
parameter_list|(
name|SoapMessage
name|message
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
comment|//
comment|// Get a SPNEGO token
comment|//
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
name|CallbackHandler
name|callbackHandler
init|=
name|NegotiationUtils
operator|.
name|getCallbackHandler
argument_list|(
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|CALLBACK_HANDLER
argument_list|)
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|SpnegoTokenContext
name|spnegoToken
init|=
operator|new
name|SpnegoTokenContext
argument_list|()
decl_stmt|;
try|try
block|{
name|spnegoToken
operator|.
name|retrieveServiceTicket
argument_list|(
name|jaasContext
argument_list|,
name|callbackHandler
argument_list|,
name|kerberosSpn
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
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
comment|//
comment|// Now initiate WS-Trust exchange
comment|//
name|STSClient
name|client
init|=
name|STSUtils
operator|.
name|getClient
argument_list|(
name|message
argument_list|,
literal|"spnego"
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
name|String
name|s
init|=
name|SpnegoTokenInterceptorProvider
operator|.
name|setupClient
argument_list|(
name|client
argument_list|,
name|message
argument_list|,
name|aim
argument_list|)
decl_stmt|;
if|if
condition|(
name|maps
operator|!=
literal|null
condition|)
block|{
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
block|}
name|SecurityToken
name|tok
init|=
name|client
operator|.
name|requestSecurityToken
argument_list|(
name|s
argument_list|,
name|Base64
operator|.
name|encode
argument_list|(
name|spnegoToken
operator|.
name|getToken
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|byte
index|[]
name|wrappedTok
init|=
name|spnegoToken
operator|.
name|unwrapKey
argument_list|(
name|tok
operator|.
name|getSecret
argument_list|()
argument_list|)
decl_stmt|;
name|tok
operator|.
name|setSecret
argument_list|(
name|wrappedTok
argument_list|)
expr_stmt|;
name|spnegoToken
operator|.
name|clear
argument_list|()
expr_stmt|;
return|return
name|tok
return|;
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
name|setLocation
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
block|}
end_class

end_unit

