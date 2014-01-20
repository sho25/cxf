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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
name|service
operator|.
name|model
operator|.
name|EndpointInfo
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
name|cxf
operator|.
name|ws
operator|.
name|security
operator|.
name|tokenstore
operator|.
name|TokenStore
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
name|TokenStoreFactory
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
name|validate
operator|.
name|Credential
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
name|validate
operator|.
name|Validator
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
class|class
name|STSTokenValidator
implements|implements
name|Validator
block|{
specifier|private
name|STSSamlAssertionValidator
name|samlValidator
init|=
operator|new
name|STSSamlAssertionValidator
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|alwaysValidateToSts
decl_stmt|;
specifier|public
name|STSTokenValidator
parameter_list|()
block|{     }
comment|/**      * Construct a new instance.      * @param alwaysValidateToSts whether to always validate the token to the STS      */
specifier|public
name|STSTokenValidator
parameter_list|(
name|boolean
name|alwaysValidateToSts
parameter_list|)
block|{
name|this
operator|.
name|alwaysValidateToSts
operator|=
name|alwaysValidateToSts
expr_stmt|;
block|}
specifier|public
name|Credential
name|validate
parameter_list|(
name|Credential
name|credential
parameter_list|,
name|RequestData
name|data
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|isValidatedLocally
argument_list|(
name|credential
argument_list|,
name|data
argument_list|)
condition|)
block|{
return|return
name|credential
return|;
block|}
return|return
name|validateWithSTS
argument_list|(
name|credential
argument_list|,
operator|(
name|SoapMessage
operator|)
name|data
operator|.
name|getMsgContext
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Credential
name|validateWithSTS
parameter_list|(
name|Credential
name|credential
parameter_list|,
name|Message
name|message
parameter_list|)
throws|throws
name|WSSecurityException
block|{
try|try
block|{
name|SecurityToken
name|token
init|=
operator|new
name|SecurityToken
argument_list|()
decl_stmt|;
name|Element
name|tokenElement
init|=
literal|null
decl_stmt|;
name|int
name|hash
init|=
literal|0
decl_stmt|;
if|if
condition|(
name|credential
operator|.
name|getSamlAssertion
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|SamlAssertionWrapper
name|assertion
init|=
name|credential
operator|.
name|getSamlAssertion
argument_list|()
decl_stmt|;
name|byte
index|[]
name|signatureValue
init|=
name|assertion
operator|.
name|getSignatureValue
argument_list|()
decl_stmt|;
if|if
condition|(
name|signatureValue
operator|!=
literal|null
operator|&&
name|signatureValue
operator|.
name|length
operator|>
literal|0
condition|)
block|{
name|hash
operator|=
name|Arrays
operator|.
name|hashCode
argument_list|(
name|signatureValue
argument_list|)
expr_stmt|;
block|}
name|tokenElement
operator|=
name|credential
operator|.
name|getSamlAssertion
argument_list|()
operator|.
name|getElement
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|credential
operator|.
name|getUsernametoken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tokenElement
operator|=
name|credential
operator|.
name|getUsernametoken
argument_list|()
operator|.
name|getElement
argument_list|()
expr_stmt|;
name|hash
operator|=
name|credential
operator|.
name|getUsernametoken
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|credential
operator|.
name|getBinarySecurityToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tokenElement
operator|=
name|credential
operator|.
name|getBinarySecurityToken
argument_list|()
operator|.
name|getElement
argument_list|()
expr_stmt|;
name|hash
operator|=
name|credential
operator|.
name|getBinarySecurityToken
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|credential
operator|.
name|getSecurityContextToken
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|tokenElement
operator|=
name|credential
operator|.
name|getSecurityContextToken
argument_list|()
operator|.
name|getElement
argument_list|()
expr_stmt|;
name|hash
operator|=
name|credential
operator|.
name|getSecurityContextToken
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
name|token
operator|.
name|setToken
argument_list|(
name|tokenElement
argument_list|)
expr_stmt|;
name|TokenStore
name|tokenStore
init|=
name|getTokenStore
argument_list|(
name|message
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenStore
operator|!=
literal|null
operator|&&
name|hash
operator|!=
literal|0
condition|)
block|{
name|SecurityToken
name|transformedToken
init|=
name|getTransformedToken
argument_list|(
name|tokenStore
argument_list|,
name|hash
argument_list|)
decl_stmt|;
if|if
condition|(
name|transformedToken
operator|!=
literal|null
condition|)
block|{
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|transformedToken
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
name|credential
operator|.
name|setTransformedToken
argument_list|(
name|assertion
argument_list|)
expr_stmt|;
return|return
name|credential
return|;
block|}
block|}
name|token
operator|.
name|setTokenHash
argument_list|(
name|hash
argument_list|)
expr_stmt|;
name|STSClient
name|c
init|=
name|STSUtils
operator|.
name|getClient
argument_list|(
name|message
argument_list|,
literal|"sts"
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|c
init|)
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"noprint"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|SecurityToken
argument_list|>
name|tokens
init|=
name|c
operator|.
name|validateSecurityToken
argument_list|(
name|token
argument_list|)
decl_stmt|;
name|SecurityToken
name|returnedToken
init|=
name|tokens
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|returnedToken
operator|!=
name|token
condition|)
block|{
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|returnedToken
operator|.
name|getToken
argument_list|()
argument_list|)
decl_stmt|;
name|credential
operator|.
name|setTransformedToken
argument_list|(
name|assertion
argument_list|)
expr_stmt|;
if|if
condition|(
name|hash
operator|!=
literal|0
condition|)
block|{
name|tokenStore
operator|.
name|add
argument_list|(
name|returnedToken
argument_list|)
expr_stmt|;
name|token
operator|.
name|setTransformedTokenIdentifier
argument_list|(
name|returnedToken
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|tokenStore
operator|.
name|add
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|hash
argument_list|)
argument_list|,
name|token
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|credential
return|;
block|}
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
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"invalidSAMLsecurity"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|static
specifier|final
name|TokenStore
name|getTokenStore
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|EndpointInfo
name|info
init|=
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
name|getEndpointInfo
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|info
init|)
block|{
name|TokenStore
name|tokenStore
init|=
operator|(
name|TokenStore
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
decl_stmt|;
if|if
condition|(
name|tokenStore
operator|==
literal|null
condition|)
block|{
name|tokenStore
operator|=
operator|(
name|TokenStore
operator|)
name|info
operator|.
name|getProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|tokenStore
operator|==
literal|null
condition|)
block|{
name|TokenStoreFactory
name|tokenStoreFactory
init|=
name|TokenStoreFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|String
name|cacheKey
init|=
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
decl_stmt|;
if|if
condition|(
name|info
operator|.
name|getName
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|cacheKey
operator|+=
literal|"-"
operator|+
name|info
operator|.
name|getName
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|hashCode
argument_list|()
expr_stmt|;
block|}
name|tokenStore
operator|=
name|tokenStoreFactory
operator|.
name|newTokenStore
argument_list|(
name|cacheKey
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|info
operator|.
name|setProperty
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_STORE_CACHE_INSTANCE
argument_list|,
name|tokenStore
argument_list|)
expr_stmt|;
block|}
return|return
name|tokenStore
return|;
block|}
block|}
specifier|protected
name|boolean
name|isValidatedLocally
parameter_list|(
name|Credential
name|credential
parameter_list|,
name|RequestData
name|data
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
operator|!
name|alwaysValidateToSts
operator|&&
name|credential
operator|.
name|getSamlAssertion
argument_list|()
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|samlValidator
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|data
argument_list|)
expr_stmt|;
return|return
name|samlValidator
operator|.
name|isTrustVerificationSucceeded
argument_list|()
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
name|WSSecurityException
argument_list|(
name|WSSecurityException
operator|.
name|ErrorCode
operator|.
name|FAILURE
argument_list|,
literal|"invalidSAMLsecurity"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|SecurityToken
name|getTransformedToken
parameter_list|(
name|TokenStore
name|tokenStore
parameter_list|,
name|int
name|hash
parameter_list|)
block|{
name|SecurityToken
name|recoveredToken
init|=
name|tokenStore
operator|.
name|getToken
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|hash
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|recoveredToken
operator|!=
literal|null
operator|&&
name|recoveredToken
operator|.
name|getTokenHash
argument_list|()
operator|==
name|hash
condition|)
block|{
name|String
name|transformedTokenId
init|=
name|recoveredToken
operator|.
name|getTransformedTokenIdentifier
argument_list|()
decl_stmt|;
if|if
condition|(
name|transformedTokenId
operator|!=
literal|null
condition|)
block|{
return|return
name|tokenStore
operator|.
name|getToken
argument_list|(
name|transformedTokenId
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

