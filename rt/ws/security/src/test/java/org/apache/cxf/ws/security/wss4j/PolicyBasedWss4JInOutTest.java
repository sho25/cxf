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
name|apache
operator|.
name|cxf
operator|.
name|helpers
operator|.
name|JavaUtils
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
name|test
operator|.
name|TestUtilities
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|PolicyBasedWss4JInOutTest
extends|extends
name|AbstractPolicySecurityTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSignedElementsPolicyWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_missing_signed_header.xml"
argument_list|,
literal|"signed_elements_policy.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_ELEMENTS
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedElementsPolicyWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial.xml"
argument_list|,
literal|"signed_elements_policy.xml"
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_ELEMENTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_elements_policy.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_ELEMENTS
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
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAsymmetricBindingAlgorithmSuitePolicy
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_elements_policy.xml"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
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
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_elements_Basic256Sha256_policy.xml"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
argument_list|)
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
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
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedElementsWithIssuedSAMLToken
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runOutInterceptorAndValidateSamlTokenAttached
argument_list|(
literal|"signed_elements_with_sst_issued_token_policy.xml"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedPartsPolicyWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_missing_signed_body.xml"
argument_list|,
literal|"signed_parts_policy_body.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_missing_signed_header.xml"
argument_list|,
literal|"signed_parts_policy_header_namespace_only.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_missing_signed_header.xml"
argument_list|,
literal|"signed_parts_policy_header.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedPartsPolicyWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial.xml"
argument_list|,
literal|"signed_parts_policy_body.xml"
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_parts_policy_body.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial.xml"
argument_list|,
literal|"signed_parts_policy_header_namespace_only.xml"
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_parts_policy_header_namespace_only.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial.xml"
argument_list|,
literal|"signed_parts_policy_header.xml"
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_parts_policy_header.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial.xml"
argument_list|,
literal|"signed_parts_policy_header_and_body.xml"
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_parts_policy_header_and_body.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedElementsPolicyWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_missing_enc_header.xml"
argument_list|,
literal|"encrypted_elements_policy.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
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
literal|null
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedElementsPolicyWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_elements_policy.xml"
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
try|try
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_elements_policy3.xml"
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
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected on an algorithm mismatch"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
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
name|SoapFault
name|fault
parameter_list|)
block|{
comment|// expected
block|}
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"encrypted_elements_policy.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|QName
index|[]
block|{
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
block|}
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_element.xml"
argument_list|,
literal|"encrypted_elements_policy2.xml"
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_ELEMENTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"encrypted_elements_policy2.xml"
argument_list|,
literal|null
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentEncryptedElementsPolicyWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_element.xml"
argument_list|,
literal|"content_encrypted_elements_policy.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|CONTENT_ENCRYPTED_ELEMENTS
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testContentEncryptedElementsPolicyWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"content_encrypted_elements_policy.xml"
argument_list|,
name|SP12Constants
operator|.
name|CONTENT_ENCRYPTED_ELEMENTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"content_encrypted_elements_policy.xml"
argument_list|,
literal|null
argument_list|,
literal|null
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedPartsPolicyWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_missing_enc_body.xml"
argument_list|,
literal|"encrypted_parts_policy_body.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_element.xml"
argument_list|,
literal|"encrypted_parts_policy_body.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_missing_enc_header.xml"
argument_list|,
literal|"encrypted_parts_policy_header_namespace_only.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_missing_enc_header.xml"
argument_list|,
literal|"encrypted_parts_policy_header.xml"
argument_list|,
literal|null
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedPartsPolicyWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_parts_policy_body.xml"
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"encrypted_parts_policy_body.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_parts_policy_header_namespace_only.xml"
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"encrypted_parts_policy_header_namespace_only.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_parts_policy_header.xml"
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"encrypted_parts_policy_header.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
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
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content.xml"
argument_list|,
literal|"encrypted_parts_policy_header_and_body.xml"
argument_list|,
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
literal|null
argument_list|,
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"encrypted_parts_policy_header_and_body.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
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
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedEncryptedPartsWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_encrypted_missing_enc_header.xml"
argument_list|,
literal|"signed_parts_policy_header_and_body_encrypted.xml"
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSignedEncryptedPartsWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
operator|!
name|TestUtilities
operator|.
name|checkUnrestrictedPoliciesInstalled
argument_list|()
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"signed_x509_issuer_serial_encrypted.xml"
argument_list|,
literal|"signed_parts_policy_header_and_body_encrypted.xml"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"signed_parts_policy_header_and_body_encrypted.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedSignedPartsWithIncompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content_signed_missing_signed_header.xml"
argument_list|,
literal|"encrypted_parts_policy_header_and_body_signed.xml"
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|SIGNED_PARTS
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|CoverageType
operator|.
name|ENCRYPTED
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testEncryptedSignedPartsWithCompleteCoverage
parameter_list|()
throws|throws
name|Exception
block|{
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content_signed.xml"
argument_list|,
literal|"encrypted_parts_policy_header_and_body_signed.xml"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|runInInterceptorAndValidate
argument_list|(
literal|"encrypted_body_content_signed_with_encrypted_header.xml"
argument_list|,
literal|"encrypted_parts_policy_header_and_body_signed.xml"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|JavaUtils
operator|.
name|isJava9Compatible
argument_list|()
condition|)
block|{
comment|// CXF-7270
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"encrypted_parts_policy_header_and_body_signed.xml"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|SP12Constants
operator|.
name|ENCRYPTED_PARTS
argument_list|,
name|SP12Constants
operator|.
name|SIGNED_PARTS
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
argument_list|,
name|CoverageType
operator|.
name|SIGNED
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testProtectTokenAssertion
parameter_list|()
throws|throws
name|Exception
block|{
comment|// ////////////////////////////////////////////////////
comment|// x509 Direct Ref Tests
comment|/* REVISIT         No inbound validation is available for the PROTECT_TOKENS assertion.         We cannot yet test inbound in the standard manner.  Since we can't         test inbound, we can't test reound trip either and thus must take         a different approach for now.          this.runInInterceptorAndValidate(                 "signed_x509_direct_ref_token_prot.xml",                 "protect_token_policy_asym_x509_direct_ref.xml",                 SP12Constants.PROTECT_TOKENS,                 null,                 CoverageType.SIGNED);          this.runInInterceptorAndValidate(                 "signed_x509_direct_ref.xml",                 "protect_token_policy_asym_x509_direct_ref.xml",                 null,                 SP12Constants.PROTECT_TOKENS,                 CoverageType.SIGNED);          this.runAndValidate(                 "wsse-request-clean.xml",                 "protect_token_policy_asym_x509_direct_ref.xml",                 null,                 null,                 Arrays.asList(new QName[] {SP12Constants.PROTECT_TOKENS }),                 null,                 Arrays.asList(new CoverageType[] {CoverageType.SIGNED }));         */
comment|// REVISIT
comment|// We test using a policy with ProtectTokens enabled on
comment|// the outbound but with a policy using a SignedElements policy
comment|// on the inbound to validate that the correct thing got signed.
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"protect_token_policy_asym_x509_direct_ref.xml"
argument_list|,
literal|"protect_token_policy_asym_x509_direct_ref_complement.xml"
argument_list|,
operator|new
name|AssertionsHolder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|QName
index|[]
block|{
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
block|}
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|,
operator|new
name|AssertionsHolder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|QName
index|[]
block|{
name|SP12Constants
operator|.
name|SIGNED_ELEMENTS
block|}
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|CoverageType
index|[]
block|{
name|CoverageType
operator|.
name|SIGNED
block|}
argument_list|)
argument_list|)
expr_stmt|;
comment|// ////////////////////////////////////////////////////
comment|// x509 Issuer Serial Tests
comment|/* REVISIT         No inbound validation is available for the PROTECT_TOKENS assertion.         We cannot yet test inbound in the standard manner.  Since we can't         test inbound, we can't test reound trip either and thus must take         a different approach for now.          this.runInInterceptorAndValidate(                 "signed_x509_issuer_serial_token_prot.xml",                 "protect_token_policy_asym_x509_issuer_serial.xml",                 SP12Constants.PROTECT_TOKENS,                 null,                 CoverageType.SIGNED);          this.runInInterceptorAndValidate(                 "signed_x509_issuer_serial.xml",                 "protect_token_policy_asym_x509_issuer_serial.xml",                 null,                 SP12Constants.PROTECT_TOKENS,                 CoverageType.SIGNED);          this.runAndValidate(                 "wsse-request-clean.xml",                 "protect_token_policy_asym_x509_issuer_serial.xml",                 null,                 null,                 Arrays.asList(new QName[] { SP12Constants.PROTECT_TOKENS }),                 null,                 Arrays.asList(new CoverageType[] { CoverageType.SIGNED }));         */
comment|// REVISIT
comment|// We test using a policy with ProtectTokens enabled on
comment|// the outbound but with a policy using a SignedElements policy
comment|// on the inbound to validate that the correct thing got signed.
name|this
operator|.
name|runAndValidate
argument_list|(
literal|"wsse-request-clean.xml"
argument_list|,
literal|"protect_token_policy_asym_x509_issuer_serial.xml"
argument_list|,
literal|"protect_token_policy_asym_x509_issuer_serial_complement.xml"
argument_list|,
operator|new
name|AssertionsHolder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|QName
index|[]
block|{
name|SP12Constants
operator|.
name|ASYMMETRIC_BINDING
block|}
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|,
operator|new
name|AssertionsHolder
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|QName
index|[]
block|{
name|SP12Constants
operator|.
name|SIGNED_ELEMENTS
block|}
argument_list|)
argument_list|,
literal|null
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|CoverageType
index|[]
block|{
name|CoverageType
operator|.
name|SIGNED
block|}
argument_list|)
argument_list|)
expr_stmt|;
comment|// ////////////////////////////////////////////////////
comment|// x509 Key Identifier Tests
comment|// TODO: Tests for Key Identifier are needed but require that the
comment|// certificates used in the test cases be updated to version 3
comment|// according to WSS4J.
comment|// TODO: Tests for derived keys.
block|}
block|}
end_class

end_unit

