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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|utils
package|;
end_package

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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Set
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpSession
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
name|MultivaluedMap
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
name|impl
operator|.
name|MetadataMap
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
name|model
operator|.
name|URITemplate
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|AuthenticationMethod
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|Client
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|ClientAccessToken
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|OAuthPermission
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|ServerAccessToken
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|common
operator|.
name|UserSubject
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
name|rs
operator|.
name|security
operator|.
name|oauth2
operator|.
name|provider
operator|.
name|OAuthServiceException
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
name|crypto
operator|.
name|CryptoUtils
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
name|LoginSecurityContext
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

begin_comment
comment|/**  * Various utility methods   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|OAuthUtils
block|{
specifier|private
name|OAuthUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|String
name|setSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|setSessionToken
argument_list|(
name|mc
argument_list|,
literal|0
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|setSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|int
name|maxInactiveInterval
parameter_list|)
block|{
return|return
name|setSessionToken
argument_list|(
name|mc
argument_list|,
name|generateRandomTokenKey
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|setSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|sessionToken
parameter_list|)
block|{
return|return
name|setSessionToken
argument_list|(
name|mc
argument_list|,
name|sessionToken
argument_list|,
literal|0
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|setSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|sessionToken
parameter_list|,
name|int
name|maxInactiveInterval
parameter_list|)
block|{
return|return
name|setSessionToken
argument_list|(
name|mc
argument_list|,
name|sessionToken
argument_list|,
literal|null
argument_list|,
literal|0
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|setSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|sessionToken
parameter_list|,
name|String
name|attribute
parameter_list|,
name|int
name|maxInactiveInterval
parameter_list|)
block|{
name|HttpSession
name|session
init|=
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getSession
argument_list|()
decl_stmt|;
if|if
condition|(
name|maxInactiveInterval
operator|>
literal|0
condition|)
block|{
name|session
operator|.
name|setMaxInactiveInterval
argument_list|(
name|maxInactiveInterval
argument_list|)
expr_stmt|;
block|}
name|String
name|theAttribute
init|=
name|attribute
operator|==
literal|null
condition|?
name|OAuthConstants
operator|.
name|SESSION_AUTHENTICITY_TOKEN
else|:
name|attribute
decl_stmt|;
name|session
operator|.
name|setAttribute
argument_list|(
name|theAttribute
argument_list|,
name|sessionToken
argument_list|)
expr_stmt|;
return|return
name|sessionToken
return|;
block|}
specifier|public
specifier|static
name|String
name|getSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
return|return
name|getSessionToken
argument_list|(
name|mc
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|attribute
parameter_list|)
block|{
return|return
name|getSessionToken
argument_list|(
name|mc
argument_list|,
name|attribute
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getSessionToken
parameter_list|(
name|MessageContext
name|mc
parameter_list|,
name|String
name|attribute
parameter_list|,
name|boolean
name|remove
parameter_list|)
block|{
name|HttpSession
name|session
init|=
name|mc
operator|.
name|getHttpServletRequest
argument_list|()
operator|.
name|getSession
argument_list|()
decl_stmt|;
name|String
name|theAttribute
init|=
name|attribute
operator|==
literal|null
condition|?
name|OAuthConstants
operator|.
name|SESSION_AUTHENTICITY_TOKEN
else|:
name|attribute
decl_stmt|;
name|String
name|sessionToken
init|=
operator|(
name|String
operator|)
name|session
operator|.
name|getAttribute
argument_list|(
name|theAttribute
argument_list|)
decl_stmt|;
if|if
condition|(
name|sessionToken
operator|!=
literal|null
operator|&&
name|remove
condition|)
block|{
name|session
operator|.
name|removeAttribute
argument_list|(
name|theAttribute
argument_list|)
expr_stmt|;
block|}
return|return
name|sessionToken
return|;
block|}
specifier|public
specifier|static
name|UserSubject
name|createSubject
parameter_list|(
name|SecurityContext
name|securityContext
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roleNames
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
if|if
condition|(
name|securityContext
operator|instanceof
name|LoginSecurityContext
condition|)
block|{
name|roleNames
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
operator|(
operator|(
name|LoginSecurityContext
operator|)
name|securityContext
operator|)
operator|.
name|getUserRoles
argument_list|()
decl_stmt|;
for|for
control|(
name|Principal
name|p
range|:
name|roles
control|)
block|{
name|roleNames
operator|.
name|add
argument_list|(
name|p
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|UserSubject
name|subject
init|=
operator|new
name|UserSubject
argument_list|(
name|securityContext
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|,
name|roleNames
argument_list|)
decl_stmt|;
name|Message
name|m
init|=
name|JAXRSUtils
operator|.
name|getCurrentMessage
argument_list|()
decl_stmt|;
if|if
condition|(
name|m
operator|!=
literal|null
operator|&&
name|m
operator|.
name|get
argument_list|(
name|AuthenticationMethod
operator|.
name|class
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|subject
operator|.
name|setAthenticationMethod
argument_list|(
name|m
operator|.
name|get
argument_list|(
name|AuthenticationMethod
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|subject
return|;
block|}
specifier|public
specifier|static
name|String
name|convertPermissionsToScope
parameter_list|(
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|OAuthPermission
name|perm
range|:
name|perms
control|)
block|{
if|if
condition|(
name|sb
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|perm
operator|.
name|getPermission
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|convertPermissionsToScopeList
parameter_list|(
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|OAuthPermission
name|perm
range|:
name|perms
control|)
block|{
name|list
operator|.
name|add
argument_list|(
name|perm
operator|.
name|getPermission
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|list
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isGrantSupportedForClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|boolean
name|canSupportPublicClients
parameter_list|,
name|String
name|grantType
parameter_list|)
block|{
if|if
condition|(
operator|!
name|client
operator|.
name|isConfidential
argument_list|()
operator|&&
operator|!
name|canSupportPublicClients
condition|)
block|{
return|return
literal|false
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|allowedGrants
init|=
name|client
operator|.
name|getAllowedGrantTypes
argument_list|()
decl_stmt|;
return|return
name|allowedGrants
operator|.
name|isEmpty
argument_list|()
operator|||
name|allowedGrants
operator|.
name|contains
argument_list|(
name|grantType
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|parseScope
parameter_list|(
name|String
name|requestedScope
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestedScope
operator|!=
literal|null
condition|)
block|{
name|String
index|[]
name|scopeValues
init|=
name|requestedScope
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|scope
range|:
name|scopeValues
control|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|scope
argument_list|)
condition|)
block|{
name|list
operator|.
name|add
argument_list|(
name|scope
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|list
return|;
block|}
specifier|public
specifier|static
name|String
name|generateRandomTokenKey
parameter_list|()
throws|throws
name|OAuthServiceException
block|{
return|return
name|generateRandomTokenKey
argument_list|(
literal|16
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|generateRandomTokenKey
parameter_list|(
name|int
name|byteSize
parameter_list|)
block|{
if|if
condition|(
name|byteSize
operator|<
literal|16
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|()
throw|;
block|}
return|return
name|StringUtils
operator|.
name|toHexString
argument_list|(
name|CryptoUtils
operator|.
name|generateSecureRandomBytes
argument_list|(
name|byteSize
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|long
name|getIssuedAt
parameter_list|()
block|{
return|return
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isExpired
parameter_list|(
name|Long
name|issuedAt
parameter_list|,
name|Long
name|lifetime
parameter_list|)
block|{
return|return
name|lifetime
operator|!=
operator|-
literal|1
operator|&&
name|issuedAt
operator|+
name|lifetime
operator|<
name|System
operator|.
name|currentTimeMillis
argument_list|()
operator|/
literal|1000
return|;
block|}
specifier|public
specifier|static
name|boolean
name|validateAudience
parameter_list|(
name|String
name|audience
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|audiences
parameter_list|)
block|{
return|return
name|audience
operator|==
literal|null
operator|||
operator|!
name|audiences
operator|.
name|isEmpty
argument_list|()
operator|&&
name|audiences
operator|.
name|contains
argument_list|(
name|audience
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|checkRequestURI
parameter_list|(
name|String
name|servletPath
parameter_list|,
name|String
name|uri
parameter_list|)
block|{
name|boolean
name|wildcard
init|=
name|uri
operator|.
name|endsWith
argument_list|(
literal|"*"
argument_list|)
decl_stmt|;
name|String
name|theURI
init|=
name|wildcard
condition|?
name|uri
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|uri
operator|.
name|length
argument_list|()
operator|-
literal|1
argument_list|)
else|:
name|uri
decl_stmt|;
try|try
block|{
name|URITemplate
name|template
init|=
operator|new
name|URITemplate
argument_list|(
name|theURI
argument_list|)
decl_stmt|;
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|template
operator|.
name|match
argument_list|(
name|servletPath
argument_list|,
name|map
argument_list|)
condition|)
block|{
name|String
name|finalGroup
init|=
name|map
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
decl_stmt|;
if|if
condition|(
name|wildcard
operator|||
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|finalGroup
argument_list|)
operator|||
literal|"/"
operator|.
name|equals
argument_list|(
name|finalGroup
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
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
literal|false
return|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|getRequestedScopes
parameter_list|(
name|Client
name|client
parameter_list|,
name|String
name|scopeParameter
parameter_list|,
name|boolean
name|partialMatchScopeValidation
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|requestScopes
init|=
name|parseScope
argument_list|(
name|scopeParameter
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|registeredScopes
init|=
name|client
operator|.
name|getRegisteredScopes
argument_list|()
decl_stmt|;
if|if
condition|(
name|requestScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|requestScopes
operator|.
name|addAll
argument_list|(
name|registeredScopes
argument_list|)
expr_stmt|;
return|return
name|requestScopes
return|;
block|}
if|if
condition|(
operator|!
name|validateScopes
argument_list|(
name|requestScopes
argument_list|,
name|registeredScopes
argument_list|,
name|partialMatchScopeValidation
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|OAuthServiceException
argument_list|(
literal|"Unexpected scope"
argument_list|)
throw|;
block|}
return|return
name|requestScopes
return|;
block|}
specifier|public
specifier|static
name|boolean
name|validateScopes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|requestScopes
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|registeredScopes
parameter_list|,
name|boolean
name|partialMatchScopeValidation
parameter_list|)
block|{
if|if
condition|(
operator|!
name|registeredScopes
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// if it is a strict validation then pre-registered scopes have to contains all
comment|// the current request scopes
if|if
condition|(
operator|!
name|partialMatchScopeValidation
condition|)
block|{
return|return
name|registeredScopes
operator|.
name|containsAll
argument_list|(
name|requestScopes
argument_list|)
return|;
block|}
else|else
block|{
for|for
control|(
name|String
name|requestScope
range|:
name|requestScopes
control|)
block|{
name|boolean
name|match
init|=
literal|false
decl_stmt|;
for|for
control|(
name|String
name|registeredScope
range|:
name|registeredScopes
control|)
block|{
if|if
condition|(
name|requestScope
operator|.
name|startsWith
argument_list|(
name|registeredScope
argument_list|)
condition|)
block|{
name|match
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|match
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
block|}
block|}
return|return
literal|true
return|;
block|}
specifier|public
specifier|static
name|ClientAccessToken
name|toClientAccessToken
parameter_list|(
name|ServerAccessToken
name|serverToken
parameter_list|,
name|boolean
name|supportOptionalParams
parameter_list|)
block|{
name|ClientAccessToken
name|clientToken
init|=
operator|new
name|ClientAccessToken
argument_list|(
name|serverToken
operator|.
name|getTokenType
argument_list|()
argument_list|,
name|serverToken
operator|.
name|getTokenKey
argument_list|()
argument_list|)
decl_stmt|;
name|clientToken
operator|.
name|setRefreshToken
argument_list|(
name|serverToken
operator|.
name|getRefreshToken
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|supportOptionalParams
condition|)
block|{
name|clientToken
operator|.
name|setExpiresIn
argument_list|(
name|serverToken
operator|.
name|getExpiresIn
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|OAuthPermission
argument_list|>
name|perms
init|=
name|serverToken
operator|.
name|getScopes
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|perms
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|clientToken
operator|.
name|setApprovedScope
argument_list|(
name|OAuthUtils
operator|.
name|convertPermissionsToScope
argument_list|(
name|perms
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|clientToken
operator|.
name|setParameters
argument_list|(
name|serverToken
operator|.
name|getParameters
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|clientToken
return|;
block|}
block|}
end_class

end_unit

