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
name|httpsignature
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPair
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyPairGenerator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZoneOffset
import|;
end_import

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
name|Collections
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
name|httpsignature
operator|.
name|exception
operator|.
name|DifferentAlgorithmsException
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
name|httpsignature
operator|.
name|exception
operator|.
name|InvalidDataToVerifySignatureException
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
name|httpsignature
operator|.
name|exception
operator|.
name|InvalidSignatureException
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
name|httpsignature
operator|.
name|exception
operator|.
name|InvalidSignatureHeaderException
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
name|httpsignature
operator|.
name|exception
operator|.
name|MissingSignatureHeaderException
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
name|httpsignature
operator|.
name|exception
operator|.
name|MultipleSignatureHeaderException
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
name|httpsignature
operator|.
name|provider
operator|.
name|MockAlgorithmProvider
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
name|httpsignature
operator|.
name|provider
operator|.
name|MockPublicKeyProvider
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
name|httpsignature
operator|.
name|provider
operator|.
name|MockSecurityProvider
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
name|httpsignature
operator|.
name|utils
operator|.
name|SignatureHeaderUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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
name|MessageVerifierTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|KEY_ID
init|=
literal|"testVerifier"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_BODY
init|=
literal|"Hello"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|METHOD
init|=
literal|"GET"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|URI
init|=
literal|"/test/signature"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEY_PAIR_GENERATOR_ALGORITHM
init|=
literal|"RSA"
decl_stmt|;
specifier|private
specifier|static
name|MessageSigner
name|messageSigner
decl_stmt|;
specifier|private
specifier|static
name|MessageVerifier
name|messageVerifier
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setup
parameter_list|()
block|{
try|try
block|{
specifier|final
name|KeyPair
name|keyPair
init|=
name|KeyPairGenerator
operator|.
name|getInstance
argument_list|(
name|KEY_PAIR_GENERATOR_ALGORITHM
argument_list|)
operator|.
name|generateKeyPair
argument_list|()
decl_stmt|;
name|messageVerifier
operator|=
operator|new
name|MessageVerifier
argument_list|(
operator|new
name|MockPublicKeyProvider
argument_list|(
name|keyPair
operator|.
name|getPublic
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|setSecurityProvider
argument_list|(
operator|new
name|MockSecurityProvider
argument_list|()
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|setAlgorithmProvider
argument_list|(
operator|new
name|MockAlgorithmProvider
argument_list|()
argument_list|)
expr_stmt|;
name|messageSigner
operator|=
operator|new
name|MessageSigner
argument_list|(
name|keyPair
operator|.
name|getPrivate
argument_list|()
argument_list|,
name|KEY_ID
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|validUnalteredRequest
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|,
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|validUnalteredRequestWithoutBody
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|validUnalteredRequestWithExtraHeaders
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|,
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Test"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"value"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Test2"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"value2"
argument_list|)
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DifferentAlgorithmsException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|differentAlgorithmsFails
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|,
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|String
name|signature
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|signature
operator|=
name|signature
operator|.
name|replaceFirst
argument_list|(
literal|"algorithm=\"rsa-sha256"
argument_list|,
literal|"algorithm=\"hmac-sha256"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|replace
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|signature
argument_list|)
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidDataToVerifySignatureException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|invalidDataToVerifySignatureFails
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|,
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|headers
operator|.
name|remove
argument_list|(
literal|"Content-Length"
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidSignatureException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|invalidSignatureFails
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|,
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|String
name|signature
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|signature
operator|=
name|signature
operator|.
name|replaceFirst
argument_list|(
literal|"signature=\"[\\w][\\w]"
argument_list|,
literal|"signature=\"AA"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|replace
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|signature
argument_list|)
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidSignatureHeaderException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|invalidSignatureHeaderFails
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|,
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|String
name|signature
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|signature
operator|=
name|signature
operator|.
name|replaceFirst
argument_list|(
literal|",signature"
argument_list|,
literal|"signature"
argument_list|)
expr_stmt|;
name|headers
operator|.
name|replace
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|signature
argument_list|)
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MissingSignatureHeaderException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|missingSignatureHeaderFails
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|MultipleSignatureHeaderException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|multipleSignatureHeaderFails
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
name|createMockHeaders
argument_list|()
decl_stmt|;
name|createAndAddSignature
argument_list|(
name|headers
argument_list|,
name|MESSAGE_BODY
argument_list|)
expr_stmt|;
name|String
name|signature
init|=
name|headers
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|signatureList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|headers
operator|.
name|get
argument_list|(
literal|"Signature"
argument_list|)
argument_list|)
decl_stmt|;
name|signatureList
operator|.
name|add
argument_list|(
name|signature
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Signature"
argument_list|,
name|signatureList
argument_list|)
expr_stmt|;
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
name|METHOD
argument_list|,
name|URI
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|createAndAddSignature
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|,
name|String
name|messageBody
parameter_list|)
block|{
try|try
block|{
name|messageSigner
operator|.
name|sign
argument_list|(
name|headers
argument_list|,
name|URI
argument_list|,
name|METHOD
argument_list|,
name|messageBody
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|createAndAddSignature
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
parameter_list|)
block|{
try|try
block|{
name|messageSigner
operator|.
name|sign
argument_list|(
name|headers
argument_list|,
name|URI
argument_list|,
name|METHOD
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|createMockHeaders
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|headers
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Host"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"example.org"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Accept"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Length"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"18"
argument_list|)
argument_list|)
expr_stmt|;
name|SignatureHeaderUtils
operator|.
name|addDateHeader
argument_list|(
name|headers
argument_list|,
name|ZoneOffset
operator|.
name|UTC
argument_list|)
expr_stmt|;
return|return
name|headers
return|;
block|}
block|}
end_class

end_unit

