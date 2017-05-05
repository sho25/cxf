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
name|authorization
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
name|Set
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
name|saml
operator|.
name|assertion
operator|.
name|Subject
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
name|rt
operator|.
name|security
operator|.
name|claims
operator|.
name|ClaimCollection
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
name|saml
operator|.
name|claims
operator|.
name|SAMLClaim
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
name|saml
operator|.
name|claims
operator|.
name|SAMLSecurityContext
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
name|saml
operator|.
name|utils
operator|.
name|SAMLUtils
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
name|security
operator|.
name|SecurityContext
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
name|saml
operator|.
name|builder
operator|.
name|SAML2Constants
import|;
end_import

begin_class
specifier|public
class|class
name|SecurityContextProviderImpl
implements|implements
name|SecurityContextProvider
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_QUALIFIER_PROPERTY
init|=
literal|"org.apache.cxf.saml.claims.role.qualifier"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_NAMEFORMAT_PROPERTY
init|=
literal|"org.apache.cxf.saml.claims.role.nameformat"
decl_stmt|;
specifier|public
name|SecurityContext
name|getSecurityContext
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|wrapper
parameter_list|)
block|{
comment|// First check to see if we are allowed to set up a security context
comment|// The SAML Assertion must be signed, or we must explicitly allow unsigned
name|String
name|allowUnsigned
init|=
operator|(
name|String
operator|)
name|SecurityUtils
operator|.
name|getSecurityPropertyValue
argument_list|(
name|SecurityConstants
operator|.
name|ENABLE_UNSIGNED_SAML_ASSERTION_PRINCIPAL
argument_list|,
name|message
argument_list|)
decl_stmt|;
name|boolean
name|allowUnsignedSamlPrincipals
init|=
name|Boolean
operator|.
name|parseBoolean
argument_list|(
name|allowUnsigned
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
operator|(
name|wrapper
operator|.
name|isSigned
argument_list|()
operator|||
name|allowUnsignedSamlPrincipals
operator|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ClaimCollection
name|claims
init|=
name|getClaims
argument_list|(
name|wrapper
argument_list|)
decl_stmt|;
name|Subject
name|subject
init|=
name|getSubject
argument_list|(
name|message
argument_list|,
name|wrapper
argument_list|,
name|claims
argument_list|)
decl_stmt|;
name|SecurityContext
name|securityContext
init|=
name|doGetSecurityContext
argument_list|(
name|message
argument_list|,
name|subject
argument_list|,
name|claims
argument_list|)
decl_stmt|;
if|if
condition|(
name|securityContext
operator|instanceof
name|SAMLSecurityContext
condition|)
block|{
name|Element
name|assertionElement
init|=
name|wrapper
operator|.
name|getElement
argument_list|()
decl_stmt|;
operator|(
operator|(
name|SAMLSecurityContext
operator|)
name|securityContext
operator|)
operator|.
name|setAssertionElement
argument_list|(
name|assertionElement
argument_list|)
expr_stmt|;
block|}
return|return
name|securityContext
return|;
block|}
specifier|protected
name|ClaimCollection
name|getClaims
parameter_list|(
name|SamlAssertionWrapper
name|wrapper
parameter_list|)
block|{
return|return
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
name|wrapper
argument_list|)
return|;
block|}
specifier|protected
name|Subject
name|getSubject
parameter_list|(
name|Message
name|message
parameter_list|,
name|SamlAssertionWrapper
name|wrapper
parameter_list|,
name|ClaimCollection
name|claims
parameter_list|)
block|{
return|return
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
name|SAMLUtils
operator|.
name|getSubject
argument_list|(
name|message
argument_list|,
name|wrapper
argument_list|)
return|;
block|}
specifier|protected
name|SecurityContext
name|doGetSecurityContext
parameter_list|(
name|Message
name|message
parameter_list|,
name|Subject
name|subject
parameter_list|,
name|ClaimCollection
name|claims
parameter_list|)
block|{
name|String
name|defaultRoleName
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|ROLE_QUALIFIER_PROPERTY
argument_list|)
decl_stmt|;
if|if
condition|(
name|defaultRoleName
operator|==
literal|null
condition|)
block|{
name|defaultRoleName
operator|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|SecurityConstants
operator|.
name|SAML_ROLE_ATTRIBUTENAME
argument_list|)
expr_stmt|;
block|}
name|String
name|defaultNameFormat
init|=
operator|(
name|String
operator|)
name|message
operator|.
name|getContextualProperty
argument_list|(
name|ROLE_NAMEFORMAT_PROPERTY
argument_list|)
decl_stmt|;
name|String
name|subjectPrincipalName
init|=
name|getSubjectPrincipalName
argument_list|(
name|subject
argument_list|,
name|claims
argument_list|)
decl_stmt|;
name|SubjectPrincipal
name|subjectPrincipal
init|=
operator|new
name|SubjectPrincipal
argument_list|(
name|subjectPrincipalName
argument_list|,
name|subject
argument_list|)
decl_stmt|;
name|String
name|roleName
init|=
name|defaultRoleName
operator|==
literal|null
condition|?
name|SAMLClaim
operator|.
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
else|:
name|defaultRoleName
decl_stmt|;
name|String
name|nameFormat
init|=
name|defaultNameFormat
operator|==
literal|null
condition|?
name|SAML2Constants
operator|.
name|ATTRNAME_FORMAT_UNSPECIFIED
else|:
name|defaultNameFormat
decl_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
name|SAMLUtils
operator|.
name|parseRolesFromClaims
argument_list|(
name|claims
argument_list|,
name|roleName
argument_list|,
name|nameFormat
argument_list|)
decl_stmt|;
return|return
operator|new
name|SAMLSecurityContext
argument_list|(
name|subjectPrincipal
argument_list|,
name|roles
argument_list|,
name|claims
argument_list|)
return|;
block|}
comment|//TODO: This can be overridden, but consider also introducing dedicated handlers
specifier|protected
name|String
name|getSubjectPrincipalName
parameter_list|(
name|Subject
name|subject
parameter_list|,
name|ClaimCollection
name|claims
parameter_list|)
block|{
comment|// parse/decipher subject name, or check claims such as
comment|// givenName, email, firstName
comment|// and use it to authenticate with the external system if needed
comment|// Or if STS has been used to validate the SAML token on the server side then
comment|// whatever name the subject has provided can probably be used as a principal name
comment|// as IDP must've confirmed that this subject indeed got authenticated and such...
return|return
name|subject
operator|.
name|getName
argument_list|()
return|;
block|}
block|}
end_class

end_unit

