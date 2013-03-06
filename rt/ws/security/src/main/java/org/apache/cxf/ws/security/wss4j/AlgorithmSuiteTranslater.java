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
name|ArrayList
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
name|List
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
name|security
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
name|cxf
operator|.
name|ws
operator|.
name|security
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
name|common
operator|.
name|crypto
operator|.
name|AlgorithmSuite
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
name|policy
operator|.
name|model
operator|.
name|AbstractBinding
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
operator|.
name|AlgorithmSuiteType
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
name|SamlToken
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
name|SupportingTokens
import|;
end_import

begin_comment
comment|/**  * Translate any AlgorithmSuite policy that may be operative into a WSS4J AlgorithmSuite object  * to enforce what algorithms are allowed in a request.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|AlgorithmSuiteTranslater
block|{
specifier|public
name|void
name|translateAlgorithmSuites
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|,
name|RequestData
name|data
parameter_list|)
throws|throws
name|WSSecurityException
block|{
if|if
condition|(
name|aim
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|List
argument_list|<
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
argument_list|>
name|algorithmSuites
init|=
name|getAlgorithmSuites
argument_list|(
name|getBindings
argument_list|(
name|aim
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|algorithmSuites
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// Translate into WSS4J's AlgorithmSuite class
name|AlgorithmSuite
name|algorithmSuite
init|=
name|translateAlgorithmSuites
argument_list|(
name|algorithmSuites
argument_list|)
decl_stmt|;
name|data
operator|.
name|setAlgorithmSuite
argument_list|(
name|algorithmSuite
argument_list|)
expr_stmt|;
block|}
comment|// Now look for an AlgorithmSuite for a SAML Assertion
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
name|SP12Constants
operator|.
name|SAML_TOKEN
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
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
argument_list|>
name|samlAlgorithmSuites
init|=
operator|new
name|ArrayList
argument_list|<
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
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|SamlToken
name|samlToken
init|=
operator|(
name|SamlToken
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
decl_stmt|;
name|AbstractSecurityAssertion
name|parentAssertion
init|=
name|samlToken
operator|.
name|getParentAssertion
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|parentAssertion
operator|instanceof
name|SupportingTokens
operator|)
operator|&&
operator|(
operator|(
name|SupportingTokens
operator|)
name|parentAssertion
operator|)
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|samlAlgorithmSuites
operator|.
name|add
argument_list|(
operator|(
operator|(
name|SupportingTokens
operator|)
name|parentAssertion
operator|)
operator|.
name|getAlgorithmSuite
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|samlAlgorithmSuites
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|data
operator|.
name|setSamlAlgorithmSuite
argument_list|(
name|translateAlgorithmSuites
argument_list|(
name|samlAlgorithmSuites
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Translate a list of CXF AlgorithmSuite objects into a single WSS4J AlgorithmSuite object      */
specifier|private
name|AlgorithmSuite
name|translateAlgorithmSuites
parameter_list|(
name|List
argument_list|<
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
argument_list|>
name|algorithmSuites
parameter_list|)
block|{
name|AlgorithmSuite
name|algorithmSuite
init|=
literal|null
decl_stmt|;
for|for
control|(
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
name|cxfAlgorithmSuite
range|:
name|algorithmSuites
control|)
block|{
if|if
condition|(
name|cxfAlgorithmSuite
operator|==
literal|null
condition|)
block|{
continue|continue;
block|}
comment|// Translate into WSS4J's AlgorithmSuite class
if|if
condition|(
name|algorithmSuite
operator|==
literal|null
condition|)
block|{
name|algorithmSuite
operator|=
operator|new
name|AlgorithmSuite
argument_list|()
expr_stmt|;
block|}
name|AlgorithmSuiteType
name|algorithmSuiteType
init|=
name|cxfAlgorithmSuite
operator|.
name|getAlgorithmSuiteType
argument_list|()
decl_stmt|;
comment|// Set asymmetric key lengths
if|if
condition|(
name|algorithmSuite
operator|.
name|getMaximumAsymmetricKeyLength
argument_list|()
operator|<
name|algorithmSuiteType
operator|.
name|getMaximumAsymmetricKeyLength
argument_list|()
condition|)
block|{
name|algorithmSuite
operator|.
name|setMaximumAsymmetricKeyLength
argument_list|(
name|algorithmSuiteType
operator|.
name|getMaximumAsymmetricKeyLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|algorithmSuite
operator|.
name|getMinimumAsymmetricKeyLength
argument_list|()
operator|>
name|algorithmSuiteType
operator|.
name|getMinimumAsymmetricKeyLength
argument_list|()
condition|)
block|{
name|algorithmSuite
operator|.
name|setMinimumAsymmetricKeyLength
argument_list|(
name|algorithmSuiteType
operator|.
name|getMinimumAsymmetricKeyLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Set symmetric key lengths
if|if
condition|(
name|algorithmSuite
operator|.
name|getMaximumSymmetricKeyLength
argument_list|()
operator|<
name|algorithmSuiteType
operator|.
name|getMaximumSymmetricKeyLength
argument_list|()
condition|)
block|{
name|algorithmSuite
operator|.
name|setMaximumSymmetricKeyLength
argument_list|(
name|algorithmSuiteType
operator|.
name|getMaximumSymmetricKeyLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|algorithmSuite
operator|.
name|getMinimumSymmetricKeyLength
argument_list|()
operator|>
name|algorithmSuiteType
operator|.
name|getMinimumSymmetricKeyLength
argument_list|()
condition|)
block|{
name|algorithmSuite
operator|.
name|setMinimumSymmetricKeyLength
argument_list|(
name|algorithmSuiteType
operator|.
name|getMinimumSymmetricKeyLength
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|algorithmSuite
operator|.
name|addEncryptionMethod
argument_list|(
name|algorithmSuiteType
operator|.
name|getEncryption
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addKeyWrapAlgorithm
argument_list|(
name|algorithmSuiteType
operator|.
name|getSymmetricKeyWrap
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addKeyWrapAlgorithm
argument_list|(
name|algorithmSuiteType
operator|.
name|getAsymmetricKeyWrap
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addSignatureMethod
argument_list|(
name|cxfAlgorithmSuite
operator|.
name|getAsymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addSignatureMethod
argument_list|(
name|cxfAlgorithmSuite
operator|.
name|getSymmetricSignature
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addDigestAlgorithm
argument_list|(
name|algorithmSuiteType
operator|.
name|getDigest
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addC14nAlgorithm
argument_list|(
name|cxfAlgorithmSuite
operator|.
name|getC14n
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addTransformAlgorithm
argument_list|(
name|cxfAlgorithmSuite
operator|.
name|getC14n
argument_list|()
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addTransformAlgorithm
argument_list|(
name|SPConstants
operator|.
name|STRT10
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addTransformAlgorithm
argument_list|(
name|WSConstants
operator|.
name|NS_XMLDSIG_ENVELOPED_SIGNATURE
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addDerivedKeyAlgorithm
argument_list|(
name|SPConstants
operator|.
name|P_SHA1
argument_list|)
expr_stmt|;
name|algorithmSuite
operator|.
name|addDerivedKeyAlgorithm
argument_list|(
name|SPConstants
operator|.
name|P_SHA1_L128
argument_list|)
expr_stmt|;
block|}
return|return
name|algorithmSuite
return|;
block|}
comment|/**      * Get all of the WS-SecurityPolicy Bindings that are in operation      */
specifier|private
name|List
argument_list|<
name|AbstractBinding
argument_list|>
name|getBindings
parameter_list|(
name|AssertionInfoMap
name|aim
parameter_list|)
block|{
name|List
argument_list|<
name|AbstractBinding
argument_list|>
name|bindings
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractBinding
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|aim
operator|!=
literal|null
condition|)
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
name|SP12Constants
operator|.
name|TRANSPORT_BINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|bindings
operator|.
name|add
argument_list|(
operator|(
name|AbstractBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|bindings
operator|.
name|add
argument_list|(
operator|(
name|AbstractBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|ais
operator|=
name|aim
operator|.
name|get
argument_list|(
name|SP12Constants
operator|.
name|SYMMETRIC_BINDING
argument_list|)
expr_stmt|;
if|if
condition|(
name|ais
operator|!=
literal|null
operator|&&
operator|!
name|ais
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|AssertionInfo
name|ai
range|:
name|ais
control|)
block|{
name|bindings
operator|.
name|add
argument_list|(
operator|(
name|AbstractBinding
operator|)
name|ai
operator|.
name|getAssertion
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
return|return
name|bindings
return|;
block|}
comment|/**      * Get all of the CXF AlgorithmSuites from the bindings      */
specifier|private
name|List
argument_list|<
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
argument_list|>
name|getAlgorithmSuites
parameter_list|(
name|List
argument_list|<
name|AbstractBinding
argument_list|>
name|bindings
parameter_list|)
block|{
name|List
argument_list|<
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
argument_list|>
name|algorithmSuites
init|=
operator|new
name|ArrayList
argument_list|<
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
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|AbstractBinding
name|binding
range|:
name|bindings
control|)
block|{
if|if
condition|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|algorithmSuites
operator|.
name|add
argument_list|(
name|binding
operator|.
name|getAlgorithmSuite
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|algorithmSuites
return|;
block|}
block|}
end_class

end_unit

