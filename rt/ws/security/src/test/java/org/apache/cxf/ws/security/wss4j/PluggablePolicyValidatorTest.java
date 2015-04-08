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
name|wss4j
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
name|Collection
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
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|ws
operator|.
name|policy
operator|.
name|AssertionInfo
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
name|policy
operator|.
name|AssertionInfoMap
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
name|policy
operator|.
name|PolicyException
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
name|wss4j
operator|.
name|CryptoCoverageUtil
operator|.
name|CoverageType
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
name|wss4j
operator|.
name|policyvalidators
operator|.
name|PolicyValidatorParameters
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
name|wss4j
operator|.
name|policyvalidators
operator|.
name|SecurityPolicyValidator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|neethi
operator|.
name|Policy
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
name|policy
operator|.
name|SP12Constants
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_comment
comment|/**  * A test for plugging in custom SecurityPolicy Validators  */
end_comment

begin_class
specifier|public
class|class
name|PluggablePolicyValidatorTest
extends|extends
name|AbstractPolicySecurityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedElementsPolicyValidator
parameter_list|()
throws|throws
name|Exception
block|{
comment|// This should work (body content is encrypted)
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"content_encrypted_elements_policy.xml"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|CONTENT_ENCRYPTED_ELEMENTS
argument_list|)
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// This should fail (body content is encrypted, not the element)
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_elements_policy2.xml"
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
argument_list|,
literal|null
argument_list|)
expr_stmt|;
comment|// Now plug in a custom SecurityPolicyValidator to allow the EncryptedElements policy
comment|// to pass
name|Map
argument_list|<
name|QName
argument_list|,
name|SecurityPolicyValidator
argument_list|>
name|validators
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|validators
operator|.
name|put
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|,
operator|new
name|NOOpPolicyValidator
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_elements_policy2.xml"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|)
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
argument_list|,
name|validators
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|runInInterceptorAndValidate
parameter_list|(
name|String
name|document
parameter_list|,
name|String
name|policyDocument
parameter_list|,
name|List
argument_list|<
name|QName
argument_list|>
name|assertedInAssertions
parameter_list|,
name|List
argument_list|<
name|QName
argument_list|>
name|notAssertedInAssertions
parameter_list|,
name|List
argument_list|<
name|CoverageType
argument_list|>
name|types
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|SecurityPolicyValidator
argument_list|>
name|validators
parameter_list|)
throws|throws
name|Exception
block|{
specifier|final
name|Policy
name|policy
init|=
name|this
operator|.
name|policyBuilder
operator|.
name|getPolicy
argument_list|(
name|this
operator|.
name|readDocument
argument_list|(
name|policyDocument
argument_list|)
operator|.
name|getDocumentElement
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|Document
name|doc
init|=
name|this
operator|.
name|readDocument
argument_list|(
name|document
argument_list|)
decl_stmt|;
specifier|final
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|policy
argument_list|)
decl_stmt|;
name|this
operator|.
name|runInInterceptorAndValidateWss
argument_list|(
name|doc
argument_list|,
name|aim
argument_list|,
name|types
argument_list|,
name|validators
argument_list|)
expr_stmt|;
try|try
block|{
name|aim
operator|.
name|checkEffectivePolicy
argument_list|(
name|policy
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyException
name|e
parameter_list|)
block|{
comment|// Expected but not relevant
block|}
finally|finally
block|{
if|if
condition|(
name|assertedInAssertions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|QName
name|assertionType
range|:
name|assertedInAssertions
control|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|assertionType
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ais
argument_list|)
expr_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|checkAssertion
argument_list|(
name|aim
argument_list|,
name|assertionType
argument_list|,
name|ai
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|notAssertedInAssertions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|QName
name|assertionType
range|:
name|notAssertedInAssertions
control|)
block|{
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
init|=
name|aim
operator|.
name|get
argument_list|(
name|assertionType
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ais
argument_list|)
expr_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|checkAssertion
argument_list|(
name|aim
argument_list|,
name|assertionType
argument_list|,
name|ai
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
specifier|private
name|void
name|runInInterceptorAndValidateWss
parameter_list|(
name|Document
name|document
parameter_list|,
name|AssertionInfoMap
name|aim
parameter_list|,
name|List
argument_list|<
name|CoverageType
argument_list|>
name|types
parameter_list|,
name|Map
argument_list|<
name|QName
argument_list|,
name|SecurityPolicyValidator
argument_list|>
name|validators
parameter_list|)
throws|throws
name|Exception
block|{
name|PolicyBasedWSS4JInInterceptor
name|inHandler
init|=
name|this
operator|.
name|getInInterceptor
argument_list|(
name|types
argument_list|)
decl_stmt|;
name|SoapMessage
name|inmsg
init|=
name|this
operator|.
name|getSoapMessageForDom
argument_list|(
name|document
argument_list|,
name|aim
argument_list|)
decl_stmt|;
if|if
condition|(
name|validators
operator|!=
literal|null
condition|)
block|{
name|inmsg
operator|.
name|put
argument_list|(
name|SecurityConstants
operator|.
name|POLICY_VALIDATOR_MAP
argument_list|,
name|validators
argument_list|)
expr_stmt|;
block|}
name|inHandler
operator|.
name|handleMessage
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
for|for
control|(
name|CoverageType
name|type
range|:
name|types
control|)
block|{
switch|switch
condition|(
name|type
condition|)
block|{
case|case
name|SIGNED
case|:
name|this
operator|.
name|verifyWss4jSigResults
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
break|break;
case|case
name|ENCRYPTED
case|:
name|this
operator|.
name|verifyWss4jEncResults
argument_list|(
name|inmsg
argument_list|)
expr_stmt|;
break|break;
default|default:
name|fail
argument_list|(
literal|"Unsupported coverage type."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
specifier|static
class|class
name|NOOpPolicyValidator
implements|implements
name|SecurityPolicyValidator
block|{
annotation|@
name|Override
specifier|public
name|boolean
name|canValidatePolicy
parameter_list|(
name|AssertionInfo
name|assertionInfo
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|validatePolicies
parameter_list|(
name|PolicyValidatorParameters
name|parameters
parameter_list|,
name|Collection
argument_list|<
name|AssertionInfo
argument_list|>
name|ais
parameter_list|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|ai
operator|.
name|setAsserted
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
block|}
empty_stmt|;
block|}
end_class

end_unit

