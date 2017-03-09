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
name|x509
package|;
end_package

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
name|Bus
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
name|AssertionBuilderRegistry
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
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertion
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
name|builder
operator|.
name|primitive
operator|.
name|PrimitiveAssertionBuilder
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
name|policy
operator|.
name|custom
operator|.
name|AlgorithmSuiteLoader
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
name|Assertion
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
name|AssertionBuilderFactory
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
name|neethi
operator|.
name|builders
operator|.
name|xml
operator|.
name|XMLPrimitiveAssertionBuilder
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
name|SPConstants
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
name|model
operator|.
name|AbstractSecurityAssertion
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
name|model
operator|.
name|AlgorithmSuite
import|;
end_import

begin_comment
comment|/**  * This class retrieves the default AlgorithmSuites plus a custom AlgorithmSuite with the RSA SHA-512  * signature  */
end_comment

begin_class
specifier|public
class|class
name|SHA512PolicyLoader
implements|implements
name|AlgorithmSuiteLoader
block|{
specifier|public
name|SHA512PolicyLoader
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|bus
operator|.
name|setExtension
argument_list|(
name|this
argument_list|,
name|AlgorithmSuiteLoader
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AlgorithmSuite
name|getAlgorithmSuite
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|SPConstants
operator|.
name|SPVersion
name|version
parameter_list|,
name|Policy
name|nestedPolicy
parameter_list|)
block|{
name|AssertionBuilderRegistry
name|reg
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|AssertionBuilderRegistry
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|reg
operator|!=
literal|null
condition|)
block|{
name|String
name|ns
init|=
literal|"http://cxf.apache.org/custom/security-policy"
decl_stmt|;
specifier|final
name|Map
argument_list|<
name|QName
argument_list|,
name|Assertion
argument_list|>
name|assertions
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|QName
name|qName
init|=
operator|new
name|QName
argument_list|(
name|ns
argument_list|,
literal|"Basic128RsaSha512"
argument_list|)
decl_stmt|;
name|assertions
operator|.
name|put
argument_list|(
name|qName
argument_list|,
operator|new
name|PrimitiveAssertion
argument_list|(
name|qName
argument_list|)
argument_list|)
expr_stmt|;
name|reg
operator|.
name|registerBuilder
argument_list|(
operator|new
name|PrimitiveAssertionBuilder
argument_list|(
name|assertions
operator|.
name|keySet
argument_list|()
argument_list|)
block|{
specifier|public
name|Assertion
name|build
parameter_list|(
name|Element
name|element
parameter_list|,
name|AssertionBuilderFactory
name|fact
parameter_list|)
block|{
if|if
condition|(
name|XMLPrimitiveAssertionBuilder
operator|.
name|isOptional
argument_list|(
name|element
argument_list|)
operator|||
name|XMLPrimitiveAssertionBuilder
operator|.
name|isIgnorable
argument_list|(
name|element
argument_list|)
condition|)
block|{
return|return
name|super
operator|.
name|build
argument_list|(
name|element
argument_list|,
name|fact
argument_list|)
return|;
block|}
name|QName
name|q
init|=
operator|new
name|QName
argument_list|(
name|element
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|element
operator|.
name|getLocalName
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|assertions
operator|.
name|get
argument_list|(
name|q
argument_list|)
return|;
block|}
block|}
argument_list|)
expr_stmt|;
block|}
return|return
operator|new
name|SHA512AlgorithmSuite
argument_list|(
name|version
argument_list|,
name|nestedPolicy
argument_list|)
return|;
block|}
specifier|public
specifier|static
class|class
name|SHA512AlgorithmSuite
extends|extends
name|AlgorithmSuite
block|{
static|static
block|{
name|ALGORITHM_SUITE_TYPES
operator|.
name|put
argument_list|(
literal|"Basic128RsaSha512"
argument_list|,
operator|new
name|AlgorithmSuiteType
argument_list|(
literal|"Basic128RsaSha512"
argument_list|,
literal|"http://www.w3.org/2001/04/xmlenc#sha512"
argument_list|,
name|WSConstants
operator|.
name|AES_128
argument_list|,
name|SPConstants
operator|.
name|KW_AES128
argument_list|,
name|SPConstants
operator|.
name|KW_RSA_OAEP
argument_list|,
name|SPConstants
operator|.
name|P_SHA1_L128
argument_list|,
name|SPConstants
operator|.
name|P_SHA1_L128
argument_list|,
literal|128
argument_list|,
literal|128
argument_list|,
literal|128
argument_list|,
literal|512
argument_list|,
literal|1024
argument_list|,
literal|4096
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|SHA512AlgorithmSuite
parameter_list|(
name|SPConstants
operator|.
name|SPVersion
name|version
parameter_list|,
name|Policy
name|nestedPolicy
parameter_list|)
block|{
name|super
argument_list|(
name|version
argument_list|,
name|nestedPolicy
argument_list|)
expr_stmt|;
name|setAsymmetricSignature
argument_list|(
literal|"http://www.w3.org/2001/04/xmldsig-more#rsa-sha512"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|AbstractSecurityAssertion
name|cloneAssertion
parameter_list|(
name|Policy
name|nestedPolicy
parameter_list|)
block|{
return|return
operator|new
name|SHA512AlgorithmSuite
argument_list|(
name|getVersion
argument_list|()
argument_list|,
name|nestedPolicy
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|parseCustomAssertion
parameter_list|(
name|Assertion
name|assertion
parameter_list|)
block|{
name|String
name|assertionName
init|=
name|assertion
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
name|String
name|assertionNamespace
init|=
name|assertion
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
literal|"http://cxf.apache.org/custom/security-policy"
operator|.
name|equals
argument_list|(
name|assertionNamespace
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
literal|"Basic128RsaSha512"
operator|.
name|equals
argument_list|(
name|assertionName
argument_list|)
condition|)
block|{
name|setAlgorithmSuiteType
argument_list|(
name|ALGORITHM_SUITE_TYPES
operator|.
name|get
argument_list|(
literal|"Basic128RsaSha512"
argument_list|)
argument_list|)
expr_stmt|;
name|getAlgorithmSuiteType
argument_list|()
operator|.
name|setNamespace
argument_list|(
name|assertionNamespace
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

