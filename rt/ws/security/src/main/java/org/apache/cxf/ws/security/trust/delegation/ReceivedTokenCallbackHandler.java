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
name|trust
operator|.
name|delegation
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
name|lang
operator|.
name|ref
operator|.
name|WeakReference
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
name|UnsupportedCallbackException
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
name|common
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
name|engine
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
name|UsernameToken
import|;
end_import

begin_comment
comment|/**  * This CallbackHandler implementation obtains the previously received message from a  * DelegationCallback object, and obtains a received token  * (SAML/UsernameToken/BinarySecurityToken) from it to be used as the delegation token.  */
end_comment

begin_class
specifier|public
class|class
name|ReceivedTokenCallbackHandler
implements|implements
name|CallbackHandler
block|{
specifier|private
name|boolean
name|useTransformedToken
init|=
literal|true
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|void
name|handle
parameter_list|(
name|Callback
index|[]
name|callbacks
parameter_list|)
throws|throws
name|IOException
throws|,
name|UnsupportedCallbackException
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
name|callbacks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|callbacks
index|[
name|i
index|]
operator|instanceof
name|DelegationCallback
condition|)
block|{
name|DelegationCallback
name|callback
init|=
operator|(
name|DelegationCallback
operator|)
name|callbacks
index|[
name|i
index|]
decl_stmt|;
name|Message
name|message
init|=
name|callback
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|message
operator|!=
literal|null
operator|&&
name|message
operator|.
name|get
argument_list|(
name|PhaseInterceptorChain
operator|.
name|PREVIOUS_MESSAGE
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|WeakReference
argument_list|<
name|SoapMessage
argument_list|>
name|wr
init|=
operator|(
name|WeakReference
argument_list|<
name|SoapMessage
argument_list|>
operator|)
name|message
operator|.
name|get
argument_list|(
name|PhaseInterceptorChain
operator|.
name|PREVIOUS_MESSAGE
argument_list|)
decl_stmt|;
name|SoapMessage
name|previousSoapMessage
init|=
name|wr
operator|.
name|get
argument_list|()
decl_stmt|;
name|Element
name|token
init|=
name|getTokenFromMessage
argument_list|(
name|previousSoapMessage
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
name|callback
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedCallbackException
argument_list|(
name|callbacks
index|[
name|i
index|]
argument_list|,
literal|"Unrecognized Callback"
argument_list|)
throw|;
block|}
block|}
block|}
specifier|private
name|Element
name|getTokenFromMessage
parameter_list|(
name|SoapMessage
name|soapMessage
parameter_list|)
block|{
if|if
condition|(
name|soapMessage
operator|!=
literal|null
condition|)
block|{
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
name|soapMessage
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
condition|)
block|{
for|for
control|(
name|WSHandlerResult
name|rResult
range|:
name|results
control|)
block|{
name|Element
name|token
init|=
name|findToken
argument_list|(
name|rResult
operator|.
name|getResults
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
return|return
name|token
return|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|Element
name|findToken
parameter_list|(
name|List
argument_list|<
name|WSSecurityEngineResult
argument_list|>
name|wsSecEngineResults
parameter_list|)
block|{
for|for
control|(
name|WSSecurityEngineResult
name|wser
range|:
name|wsSecEngineResults
control|)
block|{
comment|// First check for a transformed token
name|Object
name|transformedToken
init|=
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_TRANSFORMED_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|useTransformedToken
operator|&&
name|transformedToken
operator|instanceof
name|SamlAssertionWrapper
condition|)
block|{
return|return
operator|(
operator|(
name|SamlAssertionWrapper
operator|)
name|transformedToken
operator|)
operator|.
name|getElement
argument_list|()
return|;
block|}
comment|// Otherwise check the actions
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
name|SamlAssertionWrapper
name|assertion
init|=
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
decl_stmt|;
return|return
name|assertion
operator|.
name|getElement
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|UT
operator|||
name|actInt
operator|.
name|intValue
argument_list|()
operator|==
name|WSConstants
operator|.
name|UT_NOPASSWORD
condition|)
block|{
name|UsernameToken
name|token
init|=
operator|(
name|UsernameToken
operator|)
name|wser
operator|.
name|get
argument_list|(
name|WSSecurityEngineResult
operator|.
name|TAG_USERNAME_TOKEN
argument_list|)
decl_stmt|;
return|return
name|token
operator|.
name|getElement
argument_list|()
return|;
block|}
elseif|else
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
condition|)
block|{
name|BinarySecurity
name|token
init|=
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
decl_stmt|;
return|return
name|token
operator|.
name|getElement
argument_list|()
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|boolean
name|isUseTransformedToken
parameter_list|()
block|{
return|return
name|useTransformedToken
return|;
block|}
comment|/**      * Set whether to use the transformed token if it is available from a previous security result.      * It false, it uses the original "received" token instead. The default is "true".      * @param useTransformedToken whether to use the transformed token if it is available      */
specifier|public
name|void
name|setUseTransformedToken
parameter_list|(
name|boolean
name|useTransformedToken
parameter_list|)
block|{
name|this
operator|.
name|useTransformedToken
operator|=
name|useTransformedToken
expr_stmt|;
block|}
block|}
end_class

end_unit

