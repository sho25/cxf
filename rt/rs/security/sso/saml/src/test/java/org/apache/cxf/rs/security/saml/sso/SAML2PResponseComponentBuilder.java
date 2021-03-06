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
name|Response
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
name|Status
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
name|StatusCode
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
name|StatusMessage
import|;
end_import

begin_comment
comment|/** * A (basic) set of utility methods to construct SAML 2.0 Protocol Response statements */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SAML2PResponseComponentBuilder
block|{
specifier|private
specifier|static
name|SAMLObjectBuilder
argument_list|<
name|Response
argument_list|>
name|responseBuilder
decl_stmt|;
specifier|private
specifier|static
name|SAMLObjectBuilder
argument_list|<
name|Issuer
argument_list|>
name|issuerBuilder
decl_stmt|;
specifier|private
specifier|static
name|SAMLObjectBuilder
argument_list|<
name|Status
argument_list|>
name|statusBuilder
decl_stmt|;
specifier|private
specifier|static
name|SAMLObjectBuilder
argument_list|<
name|StatusCode
argument_list|>
name|statusCodeBuilder
decl_stmt|;
specifier|private
specifier|static
name|SAMLObjectBuilder
argument_list|<
name|StatusMessage
argument_list|>
name|statusMessageBuilder
decl_stmt|;
specifier|private
specifier|static
name|SAMLObjectBuilder
argument_list|<
name|AuthnContextClassRef
argument_list|>
name|authnContextClassRefBuilder
decl_stmt|;
specifier|private
specifier|static
name|XMLObjectBuilderFactory
name|builderFactory
init|=
name|XMLObjectProviderRegistrySupport
operator|.
name|getBuilderFactory
argument_list|()
decl_stmt|;
specifier|private
name|SAML2PResponseComponentBuilder
parameter_list|()
block|{      }
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
specifier|static
name|Response
name|createSAMLResponse
parameter_list|(
name|String
name|inResponseTo
parameter_list|,
name|String
name|issuer
parameter_list|,
name|Status
name|status
parameter_list|)
block|{
if|if
condition|(
name|responseBuilder
operator|==
literal|null
condition|)
block|{
name|responseBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|Response
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|Response
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
name|Response
name|response
init|=
name|responseBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|response
operator|.
name|setID
argument_list|(
name|UUID
operator|.
name|randomUUID
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setIssueInstant
argument_list|(
operator|new
name|DateTime
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setInResponseTo
argument_list|(
name|inResponseTo
argument_list|)
expr_stmt|;
name|response
operator|.
name|setIssuer
argument_list|(
name|createIssuer
argument_list|(
name|issuer
argument_list|)
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatus
argument_list|(
name|status
argument_list|)
expr_stmt|;
name|response
operator|.
name|setVersion
argument_list|(
name|SAMLVersion
operator|.
name|VERSION_20
argument_list|)
expr_stmt|;
return|return
name|response
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
name|Status
name|createStatus
parameter_list|(
name|String
name|statusCodeValue
parameter_list|,
name|String
name|statusMessage
parameter_list|)
block|{
if|if
condition|(
name|statusBuilder
operator|==
literal|null
condition|)
block|{
name|statusBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|Status
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|Status
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|statusCodeBuilder
operator|==
literal|null
condition|)
block|{
name|statusCodeBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|StatusCode
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|StatusCode
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|statusMessageBuilder
operator|==
literal|null
condition|)
block|{
name|statusMessageBuilder
operator|=
operator|(
name|SAMLObjectBuilder
argument_list|<
name|StatusMessage
argument_list|>
operator|)
name|builderFactory
operator|.
name|getBuilder
argument_list|(
name|StatusMessage
operator|.
name|DEFAULT_ELEMENT_NAME
argument_list|)
expr_stmt|;
block|}
name|Status
name|status
init|=
name|statusBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|StatusCode
name|statusCode
init|=
name|statusCodeBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|statusCode
operator|.
name|setValue
argument_list|(
name|statusCodeValue
argument_list|)
expr_stmt|;
name|status
operator|.
name|setStatusCode
argument_list|(
name|statusCode
argument_list|)
expr_stmt|;
if|if
condition|(
name|statusMessage
operator|!=
literal|null
condition|)
block|{
name|StatusMessage
name|statusMessageObject
init|=
name|statusMessageBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|statusMessageObject
operator|.
name|setMessage
argument_list|(
name|statusMessage
argument_list|)
expr_stmt|;
name|status
operator|.
name|setStatusMessage
argument_list|(
name|statusMessageObject
argument_list|)
expr_stmt|;
block|}
return|return
name|status
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
name|createAuthnContextClassRef
parameter_list|(
name|String
name|newAuthnContextClassRef
parameter_list|)
block|{
if|if
condition|(
name|authnContextClassRefBuilder
operator|==
literal|null
condition|)
block|{
name|authnContextClassRefBuilder
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
name|authnContextClassRef
init|=
name|authnContextClassRefBuilder
operator|.
name|buildObject
argument_list|()
decl_stmt|;
name|authnContextClassRef
operator|.
name|setAuthnContextClassRef
argument_list|(
name|newAuthnContextClassRef
argument_list|)
expr_stmt|;
return|return
name|authnContextClassRef
return|;
block|}
block|}
end_class

end_unit

