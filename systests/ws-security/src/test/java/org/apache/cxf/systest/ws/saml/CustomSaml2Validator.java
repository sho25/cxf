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
name|systest
operator|.
name|ws
operator|.
name|saml
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
name|SamlAssertionValidator
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
name|Assertion
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
name|AttributeStatement
import|;
end_import

begin_comment
comment|/**  * This class does some trivial validation of a received SAML Assertion. It checks that it is  * a SAML 2 Assertion, and checks the issuer name and that it has an Attribute Statement.  */
end_comment

begin_class
specifier|public
class|class
name|CustomSaml2Validator
extends|extends
name|SamlAssertionValidator
block|{
annotation|@
name|Override
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
name|Credential
name|validatedCredential
init|=
name|super
operator|.
name|validate
argument_list|(
name|credential
argument_list|,
name|data
argument_list|)
decl_stmt|;
name|SamlAssertionWrapper
name|assertion
init|=
name|validatedCredential
operator|.
name|getSamlAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"sts"
operator|.
name|equals
argument_list|(
name|assertion
operator|.
name|getIssuerString
argument_list|()
argument_list|)
condition|)
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
argument_list|)
throw|;
block|}
name|Assertion
name|saml2Assertion
init|=
name|assertion
operator|.
name|getSaml2
argument_list|()
decl_stmt|;
if|if
condition|(
name|saml2Assertion
operator|==
literal|null
condition|)
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
argument_list|)
throw|;
block|}
name|List
argument_list|<
name|AttributeStatement
argument_list|>
name|attributeStatements
init|=
name|saml2Assertion
operator|.
name|getAttributeStatements
argument_list|()
decl_stmt|;
if|if
condition|(
name|attributeStatements
operator|==
literal|null
operator|||
name|attributeStatements
operator|.
name|isEmpty
argument_list|()
condition|)
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
argument_list|)
throw|;
block|}
return|return
name|validatedCredential
return|;
block|}
block|}
end_class

end_unit

