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
name|saml
operator|.
name|sso
package|;
end_package

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
name|UUID
import|;
end_import

begin_import
import|import
name|org
operator|.
name|joda
operator|.
name|time
operator|.
name|DateTime
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|core
operator|.
name|xml
operator|.
name|XMLObjectBuilderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|core
operator|.
name|xml
operator|.
name|config
operator|.
name|XMLObjectProviderRegistrySupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|common
operator|.
name|SAMLObjectBuilder
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|common
operator|.
name|SAMLVersion
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnContextClassRef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnContextComparisonTypeEnumeration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnContextDeclRef
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|AuthnRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|Issuer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|NameIDPolicy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|opensaml
operator|.
name|saml
operator|.
name|saml2
operator|.
name|core
operator|.
name|RequestedAuthnContext
import|;
end_import

begin_comment
comment|/** * A set of utility methods to construct SAMLP Request statements */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SamlpRequestComponentBuilder
block|{
specifier|private
specifier|static
specifier|volatile
name|SAMLObjectBuilder
argument_list|<
name|AuthnRequest
argument_list|>
name|authnRequestBuilder
decl_stmt|;
specifier|private
specifier|static
specifier|volatile
name|SAMLObjectBuilder
argument_list|<
name|Issuer
argument_list|>
name|issuerBuilder
decl_stmt|;
specifier|private
specifier|static
specifier|volatile
name|SAMLObjectBuilder
argument_list|<
name|NameIDPolicy
argument_list|>
name|nameIDBuilder
decl_stmt|;
specifier|private
specifier|static
specifier|volatile
name|SAMLObjectBuilder
argument_list|<
name|RequestedAuthnContext
argument_list|>
name|requestedAuthnCtxBuilder
decl_stmt|;
specifier|private
specifier|static
specifier|volatile
name|SAMLObjectBuilder
argument_list|<
name|AuthnContextClassRef
argument_list|>
name|requestedAuthnCtxClassRefBuilder
decl_stmt|;
specifier|private
specifier|static
specifier|volatile
name|XMLObjectBuilderFactory
name|builderFactory
init|=
name|XMLObjectProviderRegistrySupport
operator|.
name|getBuilderFactory
argument_list|()
decl_stmt|;
specifier|private
name|SamlpRequestComponentBuilder
parameter_list|()
block|{              }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
comment|//CHECKSTYLE:OFF
specifier|public
specifier|static
name|AuthnRequest
name|createAuthnRequest
parameter_list|(
name|String
name|serviceURL
parameter_list|,
name|boolean
name|forceAuthn
parameter_list|,
name|boolean
name|isPassive
parameter_list|,
name|String
name|protocolBinding
parameter_list|,
name|SAMLVersion
name|version
parameter_list|,
name|Issuer
name|issuer
parameter_list|,
name|NameIDPolicy
name|nameIDPolicy
parameter_list|,
name|RequestedAuthnContext
name|requestedAuthnCtx
parameter_list|)
block|{
comment|//CHECKSTYLE:ON
if|if
condition|(
name|authnRequestBuilder
operator|==
literal|null
condition|)
block|{
name|authnRequestBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|AuthnRequest
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|AuthnRequest
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
name|AuthnRequest
name|authnRequest
init|=
name|authnRequestBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|authnRequest
operator|.
name|setAssertionConsumerServiceURL
argument_list|(
name|serviceURL
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setForceAuthn
argument_list|(
name|forceAuthn
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setID
argument_list|(
literal|"_"
operator|+
name|UUID
operator|.
name|randomUUID
argument_list|()
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setIsPassive
argument_list|(
name|isPassive
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setIssueInstant
argument_list|(
operator|new
name|DateTime
argument_list|()
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setProtocolBinding
argument_list|(
name|protocolBinding
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setIssuer
argument_list|(
name|issuer
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setNameIDPolicy
argument_list|(
name|nameIDPolicy
argument_list|)
expr_stmt|;
name|authnRequest
operator|.
name|setRequestedAuthnContext
argument_list|(
name|requestedAuthnCtx
argument_list|)
expr_stmt|;
return|return
name|authnRequest
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|Issuer
name|createIssuer
parameter_list|(
name|String
name|issuerValue
parameter_list|)
block|{
if|if
condition|(
name|issuerBuilder
operator|==
literal|null
condition|)
block|{
name|issuerBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|Issuer
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|Issuer
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
name|Issuer
name|issuer
init|=
name|issuerBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|issuer
operator|.
name|setValue
argument_list|(
name|issuerValue
argument_list|)
expr_stmt|;
return|return
name|issuer
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|NameIDPolicy
name|createNameIDPolicy
parameter_list|(
name|boolean
name|allowCreate
parameter_list|,
name|String
name|format
parameter_list|,
name|String
name|spNameQualifier
parameter_list|)
block|{
if|if
condition|(
name|nameIDBuilder
operator|==
literal|null
condition|)
block|{
name|nameIDBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|NameIDPolicy
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|NameIDPolicy
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
name|NameIDPolicy
name|nameId
init|=
name|nameIDBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|nameId
operator|.
name|setAllowCreate
argument_list|(
name|allowCreate
argument_list|)
expr_stmt|;
name|nameId
operator|.
name|setFormat
argument_list|(
name|format
argument_list|)
expr_stmt|;
name|nameId
operator|.
name|setSPNameQualifier
argument_list|(
name|spNameQualifier
argument_list|)
expr_stmt|;
return|return
name|nameId
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|RequestedAuthnContext
name|createRequestedAuthnCtxPolicy
parameter_list|(
name|AuthnContextComparisonTypeEnumeration
name|comparison
parameter_list|,
name|List
argument_list|<
name|AuthnContextClassRef
argument_list|>
name|authnCtxClassRefList
parameter_list|,
name|List
argument_list|<
name|AuthnContextDeclRef
argument_list|>
name|authnCtxDeclRefList
parameter_list|)
block|{
if|if
condition|(
name|requestedAuthnCtxBuilder
operator|==
literal|null
condition|)
block|{
name|requestedAuthnCtxBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|RequestedAuthnContext
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|RequestedAuthnContext
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
name|RequestedAuthnContext
name|authnCtx
init|=
name|requestedAuthnCtxBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|authnCtx
operator|.
name|setComparison
argument_list|(
name|comparison
argument_list|)
expr_stmt|;
if|if
condition|(
name|authnCtxClassRefList
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|AuthnContextClassRef
argument_list|>
name|classRefList
init|=
name|authnCtx
operator|.
name|getAuthnContextClassRefs
argument_list|()
decl_stmt|;
name|classRefList
operator|.
name|addAll
argument_list|(
name|authnCtxClassRefList
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|authnCtxDeclRefList
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|AuthnContextDeclRef
argument_list|>
name|declRefList
init|=
name|authnCtx
operator|.
name|getAuthnContextDeclRefs
argument_list|()
decl_stmt|;
name|declRefList
operator|.
name|addAll
argument_list|(
name|authnCtxDeclRefList
argument_list|)
expr_stmt|;
block|}
return|return
name|authnCtx
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|AuthnContextClassRef
name|createAuthnCtxClassRef
parameter_list|(
name|String
name|authnCtxClassRefValue
parameter_list|)
block|{
if|if
condition|(
name|requestedAuthnCtxClassRefBuilder
operator|==
literal|null
condition|)
block|{
name|requestedAuthnCtxClassRefBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|AuthnContextClassRef
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|AuthnContextClassRef
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
name|AuthnContextClassRef
name|authnCtxClassRef
init|=
name|requestedAuthnCtxClassRefBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|authnCtxClassRef
operator|.
name|setAuthnContextClassRef
argument_list|(
name|authnCtxClassRefValue
argument_list|)
expr_stmt|;
return|return
name|authnCtxClassRef
return|;
block|}
block|}
end_class

end_unit

