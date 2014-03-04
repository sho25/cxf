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
name|rs
operator|.
name|security
operator|.
name|saml
operator|.
name|assertion
operator|.
name|Claim
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
name|Claims
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
name|saml
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
name|Claims
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
name|Claims
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
name|Claims
name|claims
parameter_list|)
block|{
return|return
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
name|Claims
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
name|SecurityContext
name|sc
init|=
operator|new
name|JAXRSSAMLSecurityContext
argument_list|(
name|subjectPrincipal
argument_list|,
name|claims
argument_list|,
name|defaultRoleName
operator|==
literal|null
condition|?
name|Claim
operator|.
name|DEFAULT_ROLE_NAME
else|:
name|defaultRoleName
argument_list|,
name|defaultNameFormat
operator|==
literal|null
condition|?
name|Claim
operator|.
name|DEFAULT_NAME_FORMAT
else|:
name|defaultNameFormat
argument_list|)
decl_stmt|;
return|return
name|sc
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
name|Claims
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

