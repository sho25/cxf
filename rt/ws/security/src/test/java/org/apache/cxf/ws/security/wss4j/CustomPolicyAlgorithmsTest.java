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
name|apache
operator|.
name|wss4j
operator|.
name|policy
operator|.
name|model
operator|.
name|AsymmetricBinding
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

begin_class
specifier|public
class|class
name|CustomPolicyAlgorithmsTest
extends|extends
name|AbstractPolicySecurityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSHA256AsymSigAlgorithm
parameter_list|()
throws|throws
name|Exception
block|{
specifier|final
name|String
name|rsaSha2SigMethod
init|=
literal|"http://www.w3.org/2001/04/xmldsig-more#rsa-sha256"
decl_stmt|;
name|String
name|policyName
init|=
literal|"signed_elements_policy.xml"
decl_stmt|;
name|Policy
name|policy
init|=
name|policyBuilder
operator|.
name|getPolicy
argument_list|(
name|this
operator|.
name|getResourceAsStream
argument_list|(
name|policyName
argument_list|)
argument_list|)
decl_stmt|;
name|AssertionInfoMap
name|aim
init|=
operator|new
name|AssertionInfoMap
argument_list|(
name|policy
argument_list|)
decl_stmt|;
name|AssertionInfo
name|assertInfo
init|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|AsymmetricBinding
name|binding
init|=
operator|(
name|AsymmetricBinding
operator|)
name|assertInfo
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
comment|// set Signature Algorithm to RSA SHA-256
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|setAsymmetricSignature
argument_list|(
name|rsaSha2SigMethod
argument_list|)
expr_stmt|;
name|String
name|sigMethod
init|=
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|.
name|getAsymmetricSignature
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|sigMethod
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|rsaSha2SigMethod
argument_list|,
name|sigMethod
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

