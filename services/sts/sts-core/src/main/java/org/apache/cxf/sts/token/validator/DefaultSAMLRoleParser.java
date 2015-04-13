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
name|token
operator|.
name|validator
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
name|javax
operator|.
name|security
operator|.
name|auth
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
name|wss4j
operator|.
name|common
operator|.
name|saml
operator|.
name|SamlAssertionWrapper
import|;
end_import

begin_comment
comment|/**  * A default implementation to extract roles from a SAML Assertion  */
end_comment

begin_class
specifier|public
class|class
name|DefaultSAMLRoleParser
extends|extends
name|DefaultSubjectRoleParser
implements|implements
name|SAMLRoleParser
block|{
comment|/**      * This configuration tag specifies the default attribute name where the roles are present      * The default is "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role".      */
specifier|public
specifier|static
specifier|final
name|String
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role"
decl_stmt|;
specifier|private
name|boolean
name|useJaasSubject
init|=
literal|true
decl_stmt|;
specifier|private
name|String
name|roleAttributeName
init|=
name|SAML_ROLE_ATTRIBUTENAME_DEFAULT
decl_stmt|;
comment|/**      * Return the set of User/Principal roles from the Assertion.      * @param principal the Principal associated with the Assertion      * @param subject the JAAS Subject associated with a successful validation of the Assertion      * @param assertion The Assertion object      * @return the set of User/Principal roles from the Assertion.      */
specifier|public
name|Set
argument_list|<
name|Principal
argument_list|>
name|parseRolesFromAssertion
parameter_list|(
name|Principal
name|principal
parameter_list|,
name|Subject
name|subject
parameter_list|,
name|SamlAssertionWrapper
name|assertion
parameter_list|)
block|{
if|if
condition|(
name|subject
operator|!=
literal|null
operator|&&
name|useJaasSubject
condition|)
block|{
return|return
name|super
operator|.
name|parseRolesFromSubject
argument_list|(
name|principal
argument_list|,
name|subject
argument_list|)
return|;
block|}
name|ClaimCollection
name|claims
init|=
name|SAMLUtils
operator|.
name|getClaims
argument_list|(
name|assertion
argument_list|)
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
name|roleAttributeName
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|SAMLSecurityContext
name|context
init|=
operator|new
name|SAMLSecurityContext
argument_list|(
name|principal
argument_list|,
name|roles
argument_list|,
name|claims
argument_list|)
decl_stmt|;
return|return
name|context
operator|.
name|getUserRoles
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isUseJaasSubject
parameter_list|()
block|{
return|return
name|useJaasSubject
return|;
block|}
comment|/**      * Whether to get roles from the JAAS Subject (if not null) returned from SAML Assertion      * Validation or not. The default is true.      * @param useJaasSubject whether to get roles from the JAAS Subject or not      */
specifier|public
name|void
name|setUseJaasSubject
parameter_list|(
name|boolean
name|useJaasSubject
parameter_list|)
block|{
name|this
operator|.
name|useJaasSubject
operator|=
name|useJaasSubject
expr_stmt|;
block|}
specifier|public
name|String
name|getRoleAttributeName
parameter_list|()
block|{
return|return
name|roleAttributeName
return|;
block|}
comment|/**      * Set the attribute URI of the SAML AttributeStatement where the role information is stored.      * The default is "http://schemas.xmlsoap.org/ws/2005/05/identity/claims/role".      * @param roleAttributeName the Attribute URI where role information is stored      */
specifier|public
name|void
name|setRoleAttributeName
parameter_list|(
name|String
name|roleAttributeName
parameter_list|)
block|{
name|this
operator|.
name|roleAttributeName
operator|=
name|roleAttributeName
expr_stmt|;
block|}
block|}
end_class

end_unit

