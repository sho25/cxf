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
name|sts
operator|.
name|rest
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
name|HashMap
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
name|Map
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
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
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
name|Document
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
name|sts
operator|.
name|QNameConstants
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
name|sts
operator|.
name|STSConstants
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
name|sts
operator|.
name|token
operator|.
name|provider
operator|.
name|jwt
operator|.
name|JWTTokenProvider
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
name|sts
operator|.
name|provider
operator|.
name|SecurityTokenServiceImpl
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|ClaimsType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|ObjectFactory
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenResponseType
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
name|sts
operator|.
name|provider
operator|.
name|model
operator|.
name|RequestSecurityTokenType
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
name|wss4j
operator|.
name|dom
operator|.
name|WSConstants
import|;
end_import

begin_class
specifier|public
class|class
name|RESTSecurityTokenServiceImpl
extends|extends
name|SecurityTokenServiceImpl
implements|implements
name|RESTSecurityTokenService
block|{
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|DEFAULT_CLAIM_TYPE_MAP
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|DEFAULT_TOKEN_TYPE_MAP
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLAIM_TYPE
init|=
literal|"ClaimType"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLAIM_TYPE_NS
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity"
decl_stmt|;
static|static
block|{
name|DEFAULT_CLAIM_TYPE_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|DEFAULT_CLAIM_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"emailaddress"
argument_list|,
name|CLAIM_TYPE_NS
operator|+
literal|"/claims/emailaddress"
argument_list|)
expr_stmt|;
name|DEFAULT_CLAIM_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"role"
argument_list|,
name|CLAIM_TYPE_NS
operator|+
literal|"/claims/role"
argument_list|)
expr_stmt|;
name|DEFAULT_CLAIM_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"surname"
argument_list|,
name|CLAIM_TYPE_NS
operator|+
literal|"/claims/surname"
argument_list|)
expr_stmt|;
name|DEFAULT_CLAIM_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"givenname"
argument_list|,
name|CLAIM_TYPE_NS
operator|+
literal|"/claims/givenname"
argument_list|)
expr_stmt|;
name|DEFAULT_CLAIM_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"name"
argument_list|,
name|CLAIM_TYPE_NS
operator|+
literal|"/claims/name"
argument_list|)
expr_stmt|;
name|DEFAULT_CLAIM_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"upn"
argument_list|,
name|CLAIM_TYPE_NS
operator|+
literal|"/claims/upn"
argument_list|)
expr_stmt|;
name|DEFAULT_CLAIM_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"nameidentifier"
argument_list|,
name|CLAIM_TYPE_NS
operator|+
literal|"/claims/nameidentifier"
argument_list|)
expr_stmt|;
name|DEFAULT_TOKEN_TYPE_MAP
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|DEFAULT_TOKEN_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"saml"
argument_list|,
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
expr_stmt|;
name|DEFAULT_TOKEN_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"saml2.0"
argument_list|,
name|WSConstants
operator|.
name|WSS_SAML2_TOKEN_TYPE
argument_list|)
expr_stmt|;
name|DEFAULT_TOKEN_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"saml1.1"
argument_list|,
name|WSConstants
operator|.
name|WSS_SAML_TOKEN_TYPE
argument_list|)
expr_stmt|;
name|DEFAULT_TOKEN_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"jwt"
argument_list|,
name|JWTTokenProvider
operator|.
name|JWT_TOKEN_TYPE
argument_list|)
expr_stmt|;
name|DEFAULT_TOKEN_TYPE_MAP
operator|.
name|put
argument_list|(
literal|"sct"
argument_list|,
name|STSUtils
operator|.
name|TOKEN_TYPE_SCT_05_12
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Context
specifier|private
name|MessageContext
name|messageContext
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|claimTypeMap
init|=
name|DEFAULT_CLAIM_TYPE_MAP
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tokenTypeMap
init|=
name|DEFAULT_TOKEN_TYPE_MAP
decl_stmt|;
specifier|private
name|String
name|defaultKeyType
init|=
name|STSConstants
operator|.
name|BEARER_KEY_KEYTYPE
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|defaultClaims
decl_stmt|;
specifier|private
name|boolean
name|requestClaimsOptional
init|=
literal|true
decl_stmt|;
annotation|@
name|Override
specifier|public
name|Response
name|getToken
parameter_list|(
name|String
name|tokenType
parameter_list|,
name|String
name|keyType
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|requestedClaims
parameter_list|)
block|{
if|if
condition|(
name|tokenTypeMap
operator|!=
literal|null
operator|&&
name|tokenTypeMap
operator|.
name|containsKey
argument_list|(
name|tokenType
argument_list|)
condition|)
block|{
name|tokenType
operator|=
name|tokenTypeMap
operator|.
name|get
argument_list|(
name|tokenType
argument_list|)
expr_stmt|;
block|}
name|ObjectFactory
name|of
init|=
operator|new
name|ObjectFactory
argument_list|()
decl_stmt|;
name|RequestSecurityTokenType
name|request
init|=
name|of
operator|.
name|createRequestSecurityTokenType
argument_list|()
decl_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|of
operator|.
name|createTokenType
argument_list|(
name|tokenType
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|of
operator|.
name|createRequestType
argument_list|(
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512/Issue"
argument_list|)
argument_list|)
expr_stmt|;
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|of
operator|.
name|createKeyType
argument_list|(
name|keyType
operator|!=
literal|null
condition|?
name|keyType
else|:
name|defaultKeyType
argument_list|)
argument_list|)
expr_stmt|;
comment|// Claims
if|if
condition|(
name|requestedClaims
operator|==
literal|null
condition|)
block|{
name|requestedClaims
operator|=
name|defaultClaims
expr_stmt|;
block|}
if|if
condition|(
name|requestedClaims
operator|!=
literal|null
condition|)
block|{
name|ClaimsType
name|claimsType
init|=
name|of
operator|.
name|createClaimsType
argument_list|()
decl_stmt|;
name|claimsType
operator|.
name|setDialect
argument_list|(
name|CLAIM_TYPE_NS
argument_list|)
expr_stmt|;
name|JAXBElement
argument_list|<
name|ClaimsType
argument_list|>
name|claims
init|=
name|of
operator|.
name|createClaims
argument_list|(
name|claimsType
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|claim
range|:
name|requestedClaims
control|)
block|{
if|if
condition|(
name|claimTypeMap
operator|!=
literal|null
operator|&&
name|claimTypeMap
operator|.
name|containsKey
argument_list|(
name|claim
argument_list|)
condition|)
block|{
name|claim
operator|=
name|claimTypeMap
operator|.
name|get
argument_list|(
name|claim
argument_list|)
expr_stmt|;
block|}
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|createDocument
argument_list|()
decl_stmt|;
name|Element
name|claimElement
init|=
name|doc
operator|.
name|createElementNS
argument_list|(
name|CLAIM_TYPE_NS
argument_list|,
name|CLAIM_TYPE
argument_list|)
decl_stmt|;
name|claimElement
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Uri"
argument_list|,
name|claim
argument_list|)
expr_stmt|;
name|claimElement
operator|.
name|setAttributeNS
argument_list|(
literal|null
argument_list|,
literal|"Optional"
argument_list|,
name|Boolean
operator|.
name|toString
argument_list|(
name|requestClaimsOptional
argument_list|)
argument_list|)
expr_stmt|;
name|claimsType
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|claimElement
argument_list|)
expr_stmt|;
block|}
name|request
operator|.
name|getAny
argument_list|()
operator|.
name|add
argument_list|(
name|claims
argument_list|)
expr_stmt|;
block|}
comment|// OnBehalfOf
comment|// User Authentication done with JWT or SAML?
comment|//if (securityContext != null&& securityContext.getUserPrincipal() != null) {
comment|//TODO
comment|//            if (onBehalfOfToken != null) {
comment|//                OnBehalfOfType onBehalfOfType = of.createOnBehalfOfType();
comment|//                onBehalfOfType.setAny(onBehalfOfToken);
comment|//                JAXBElement<OnBehalfOfType> onBehalfOfElement = of.createOnBehalfOf(onBehalfOfType);
comment|//                request.getAny().add(onBehalfOfElement);
comment|//            }
comment|//  }
comment|// request.setContext(null);
return|return
name|getToken
argument_list|(
name|Action
operator|.
name|ISSUE
argument_list|,
name|request
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|getToken
parameter_list|(
name|Action
name|action
parameter_list|,
name|RequestSecurityTokenType
name|request
parameter_list|)
block|{
name|RequestSecurityTokenResponseType
name|response
decl_stmt|;
switch|switch
condition|(
name|action
condition|)
block|{
case|case
name|VALIDATE
case|:
name|response
operator|=
name|validate
argument_list|(
name|request
argument_list|)
expr_stmt|;
break|break;
case|case
name|RENEW
case|:
name|response
operator|=
name|renew
argument_list|(
name|request
argument_list|)
expr_stmt|;
break|break;
case|case
name|CANCEL
case|:
name|response
operator|=
name|cancel
argument_list|(
name|request
argument_list|)
expr_stmt|;
break|break;
case|case
name|ISSUE
case|:
default|default:
name|response
operator|=
name|issueSingle
argument_list|(
name|request
argument_list|)
expr_stmt|;
break|break;
block|}
name|JAXBElement
argument_list|<
name|RequestSecurityTokenResponseType
argument_list|>
name|jaxbResponse
init|=
name|QNameConstants
operator|.
name|WS_TRUST_FACTORY
operator|.
name|createRequestSecurityTokenResponse
argument_list|(
name|response
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|jaxbResponse
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|removeToken
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|)
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|cancel
argument_list|(
name|request
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|response
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|getKeyExchangeToken
parameter_list|(
name|RequestSecurityTokenType
name|request
parameter_list|)
block|{
name|RequestSecurityTokenResponseType
name|response
init|=
name|keyExchangeToken
argument_list|(
name|request
argument_list|)
decl_stmt|;
return|return
name|Response
operator|.
name|ok
argument_list|(
name|response
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getTokenTypeMap
parameter_list|()
block|{
return|return
name|tokenTypeMap
return|;
block|}
specifier|public
name|void
name|setTokenTypeMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|tokenTypeMap
parameter_list|)
block|{
name|this
operator|.
name|tokenTypeMap
operator|=
name|tokenTypeMap
expr_stmt|;
block|}
specifier|public
name|String
name|getDefaultKeyType
parameter_list|()
block|{
return|return
name|defaultKeyType
return|;
block|}
specifier|public
name|void
name|setDefaultKeyType
parameter_list|(
name|String
name|defaultKeyType
parameter_list|)
block|{
name|this
operator|.
name|defaultKeyType
operator|=
name|defaultKeyType
expr_stmt|;
block|}
specifier|public
name|boolean
name|isRequestClaimsOptional
parameter_list|()
block|{
return|return
name|requestClaimsOptional
return|;
block|}
specifier|public
name|void
name|setRequestClaimsOptional
parameter_list|(
name|boolean
name|requestClaimsOptional
parameter_list|)
block|{
name|this
operator|.
name|requestClaimsOptional
operator|=
name|requestClaimsOptional
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getClaimTypeMap
parameter_list|()
block|{
return|return
name|claimTypeMap
return|;
block|}
specifier|public
name|void
name|setClaimTypeMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|claimTypeMap
parameter_list|)
block|{
name|this
operator|.
name|claimTypeMap
operator|=
name|claimTypeMap
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|Principal
name|getPrincipal
parameter_list|()
block|{
return|return
name|messageContext
operator|.
name|getSecurityContext
argument_list|()
operator|.
name|getUserPrincipal
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|getMessageContext
parameter_list|()
block|{
return|return
name|PhaseInterceptorChain
operator|.
name|getCurrentMessage
argument_list|()
return|;
block|}
block|}
end_class

end_unit

