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
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|TokenStoreUtils
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
name|WSS4JConstants
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
name|common
operator|.
name|util
operator|.
name|XMLUtils
import|;
end_import

begin_class
specifier|public
class|class
name|DefaultSTSTokenCacher
implements|implements
name|STSTokenCacher
block|{
specifier|public
name|SecurityToken
name|retrieveToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|boolean
name|retrieveTokenFromEndpoint
parameter_list|)
block|{
name|SecurityToken
name|tok
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|retrieveTokenFromEndpoint
condition|)
block|{
name|tok
operator|=
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
expr_stmt|;
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
name|TokenStoreUtils
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
block|}
else|else
block|{
name|tok
operator|=
operator|(
name|SecurityToken
operator|)
name|message
operator|.
name|get
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
expr_stmt|;
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
name|get
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
name|TokenStoreUtils
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
block|}
return|return
name|tok
return|;
block|}
specifier|public
name|SecurityToken
name|retrieveToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|Element
name|delegationToken
parameter_list|,
name|String
name|cacheKey
parameter_list|)
block|{
if|if
condition|(
name|delegationToken
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|TokenStore
name|tokenStore
init|=
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
decl_stmt|;
comment|// See if the token corresponding to the delegation Token is stored in the cache
comment|// and if it points to an issued token
name|String
name|id
init|=
name|getIdFromToken
argument_list|(
name|delegationToken
argument_list|)
decl_stmt|;
name|SecurityToken
name|cachedToken
init|=
name|tokenStore
operator|.
name|getToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedToken
operator|!=
literal|null
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|cachedToken
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|!=
literal|null
operator|&&
name|properties
operator|.
name|containsKey
argument_list|(
name|cacheKey
argument_list|)
condition|)
block|{
name|String
name|associatedToken
init|=
operator|(
name|String
operator|)
name|properties
operator|.
name|get
argument_list|(
name|cacheKey
argument_list|)
decl_stmt|;
name|SecurityToken
name|issuedToken
init|=
name|tokenStore
operator|.
name|getToken
argument_list|(
name|associatedToken
argument_list|)
decl_stmt|;
if|if
condition|(
name|issuedToken
operator|!=
literal|null
condition|)
block|{
return|return
name|issuedToken
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|storeToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|SecurityToken
name|securityToken
parameter_list|,
name|boolean
name|storeTokenInEndpoint
parameter_list|)
block|{
if|if
condition|(
name|storeTokenInEndpoint
operator|&&
operator|!
name|isOneTimeUse
argument_list|(
name|securityToken
argument_list|)
condition|)
block|{
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
name|securityToken
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
name|TOKEN
argument_list|,
name|securityToken
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
name|securityToken
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
name|getEndpoint
argument_list|()
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|securityToken
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|message
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|,
name|securityToken
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|,
name|securityToken
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|message
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ELEMENT
argument_list|,
name|securityToken
operator|.
name|getToken
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|add
argument_list|(
name|securityToken
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|storeToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|Element
name|delegationToken
parameter_list|,
name|String
name|secTokenId
parameter_list|,
name|String
name|cacheKey
parameter_list|)
block|{
if|if
condition|(
name|secTokenId
operator|==
literal|null
operator|||
name|delegationToken
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|TokenStore
name|tokenStore
init|=
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|String
name|id
init|=
name|getIdFromToken
argument_list|(
name|delegationToken
argument_list|)
decl_stmt|;
name|SecurityToken
name|cachedToken
init|=
name|tokenStore
operator|.
name|getToken
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|cachedToken
operator|==
literal|null
condition|)
block|{
name|cachedToken
operator|=
operator|new
name|SecurityToken
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|cachedToken
operator|.
name|setToken
argument_list|(
name|delegationToken
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
init|=
name|cachedToken
operator|.
name|getProperties
argument_list|()
decl_stmt|;
if|if
condition|(
name|properties
operator|==
literal|null
condition|)
block|{
name|properties
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|cachedToken
operator|.
name|setProperties
argument_list|(
name|properties
argument_list|)
expr_stmt|;
block|}
name|properties
operator|.
name|put
argument_list|(
name|cacheKey
argument_list|,
name|secTokenId
argument_list|)
expr_stmt|;
name|tokenStore
operator|.
name|add
argument_list|(
name|cachedToken
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeToken
parameter_list|(
name|Message
name|message
parameter_list|,
name|SecurityToken
name|securityToken
parameter_list|)
block|{
comment|// Remove token from cache
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|getEndpoint
argument_list|()
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN_ID
argument_list|)
expr_stmt|;
name|message
operator|.
name|getExchange
argument_list|()
operator|.
name|remove
argument_list|(
name|SecurityConstants
operator|.
name|TOKEN
argument_list|)
expr_stmt|;
name|message
operator|.
name|resetContextCache
argument_list|()
expr_stmt|;
if|if
condition|(
name|securityToken
operator|!=
literal|null
condition|)
block|{
name|TokenStoreUtils
operator|.
name|getTokenStore
argument_list|(
name|message
argument_list|)
operator|.
name|remove
argument_list|(
name|securityToken
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Check to see if the received token is a SAML2 Token with "OneTimeUse" set. If so,
comment|// it should not be cached on the endpoint, but only on the message.
specifier|private
specifier|static
name|boolean
name|isOneTimeUse
parameter_list|(
name|SecurityToken
name|issuedToken
parameter_list|)
block|{
name|Element
name|token
init|=
name|issuedToken
operator|.
name|getToken
argument_list|()
decl_stmt|;
if|if
condition|(
name|token
operator|!=
literal|null
operator|&&
literal|"Assertion"
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSS4JConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
try|try
block|{
name|SamlAssertionWrapper
name|assertion
init|=
operator|new
name|SamlAssertionWrapper
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|!=
literal|null
operator|&&
name|assertion
operator|.
name|getSaml2
argument_list|()
operator|.
name|getConditions
argument_list|()
operator|.
name|getOneTimeUse
argument_list|()
operator|!=
literal|null
condition|)
block|{
return|return
literal|true
return|;
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
return|return
literal|false
return|;
block|}
comment|// Get an id from the token that is unique to that token
specifier|private
specifier|static
name|String
name|getIdFromToken
parameter_list|(
name|Element
name|token
parameter_list|)
block|{
if|if
condition|(
name|token
operator|!=
literal|null
condition|)
block|{
comment|// For SAML tokens get the ID/AssertionID
if|if
condition|(
literal|"Assertion"
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSS4JConstants
operator|.
name|SAML2_NS
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|token
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
literal|"Assertion"
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSS4JConstants
operator|.
name|SAML_NS
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|token
operator|.
name|getAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"AssertionID"
argument_list|)
return|;
block|}
comment|// For UsernameTokens get the username
if|if
condition|(
name|WSS4JConstants
operator|.
name|USERNAME_TOKEN_LN
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSS4JConstants
operator|.
name|WSSE_NS
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|usernameElement
init|=
name|XMLUtils
operator|.
name|getDirectChildElement
argument_list|(
name|token
argument_list|,
name|WSS4JConstants
operator|.
name|USERNAME_LN
argument_list|,
name|WSS4JConstants
operator|.
name|WSSE_NS
argument_list|)
decl_stmt|;
if|if
condition|(
name|usernameElement
operator|!=
literal|null
condition|)
block|{
return|return
name|XMLUtils
operator|.
name|getElementText
argument_list|(
name|usernameElement
argument_list|)
return|;
block|}
block|}
comment|// For BinarySecurityTokens take the hash of the value
if|if
condition|(
name|WSS4JConstants
operator|.
name|BINARY_TOKEN_LN
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getLocalName
argument_list|()
argument_list|)
operator|&&
name|WSS4JConstants
operator|.
name|WSSE_NS
operator|.
name|equals
argument_list|(
name|token
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|text
init|=
name|XMLUtils
operator|.
name|getElementText
argument_list|(
name|token
argument_list|)
decl_stmt|;
if|if
condition|(
name|text
operator|!=
literal|null
operator|&&
operator|!
literal|""
operator|.
name|equals
argument_list|(
name|text
argument_list|)
condition|)
block|{
try|try
block|{
name|MessageDigest
name|digest
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"SHA-256"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bytes
init|=
name|digest
operator|.
name|digest
argument_list|(
name|text
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
return|return
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
name|XMLUtils
operator|.
name|encodeToString
argument_list|(
name|bytes
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
comment|// SHA-256 must be supported so not going to happen...
block|}
block|}
block|}
block|}
return|return
literal|""
return|;
block|}
block|}
end_class

end_unit

