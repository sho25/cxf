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
name|File
import|;
end_import

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
name|nio
operator|.
name|file
operator|.
name|FileSystems
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyFactory
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
name|security
operator|.
name|PrivateKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|PublicKey
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|InvalidKeySpecException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|PKCS8EncodedKeySpec
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|spec
operator|.
name|X509EncodedKeySpec
import|;
end_import

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
name|provider
operator|.
name|PrivateKeyProvider
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
import|;
end_import

begin_comment
comment|/**  * Some examples from the Appendix C of the spec.  */
end_comment

begin_class
specifier|public
class|class
name|SpecExamplesTest
block|{
specifier|private
specifier|static
name|PrivateKeyProvider
name|privateKeyProvider
decl_stmt|;
specifier|private
specifier|static
name|PublicKey
name|publicKey
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidKeySpecException
block|{
try|try
block|{
comment|// Load keys
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|"."
argument_list|)
operator|.
name|getCanonicalPath
argument_list|()
expr_stmt|;
block|}
name|Path
name|privateKeyPath
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|basedir
argument_list|,
literal|"/src/test/resources/private_key.der"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|keyBytes
init|=
name|Files
operator|.
name|readAllBytes
argument_list|(
name|privateKeyPath
argument_list|)
decl_stmt|;
name|PKCS8EncodedKeySpec
name|privateKeySpec
init|=
operator|new
name|PKCS8EncodedKeySpec
argument_list|(
name|keyBytes
argument_list|)
decl_stmt|;
name|PrivateKey
name|privateKey
init|=
name|KeyFactory
operator|.
name|getInstance
argument_list|(
literal|"RSA"
argument_list|)
operator|.
name|generatePrivate
argument_list|(
name|privateKeySpec
argument_list|)
decl_stmt|;
name|privateKeyProvider
operator|=
name|keyId
lambda|->
name|privateKey
expr_stmt|;
name|Path
name|publicKeyPath
init|=
name|FileSystems
operator|.
name|getDefault
argument_list|()
operator|.
name|getPath
argument_list|(
name|basedir
argument_list|,
literal|"/src/test/resources/public_key.der"
argument_list|)
decl_stmt|;
name|byte
index|[]
name|publicKeyBytes
init|=
name|Files
operator|.
name|readAllBytes
argument_list|(
name|publicKeyPath
argument_list|)
decl_stmt|;
name|X509EncodedKeySpec
name|publicKeySpec
init|=
operator|new
name|X509EncodedKeySpec
argument_list|(
name|publicKeyBytes
argument_list|)
decl_stmt|;
name|publicKey
operator|=
name|KeyFactory
operator|.
name|getInstance
argument_list|(
literal|"RSA"
argument_list|)
operator|.
name|generatePublic
argument_list|(
name|publicKeySpec
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
name|defaultTest
parameter_list|()
throws|throws
name|IOException
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
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
name|privateKeyProvider
argument_list|,
literal|"Test"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"Date"
argument_list|)
argument_list|)
decl_stmt|;
name|messageSigner
operator|.
name|sign
argument_list|(
name|headers
argument_list|,
literal|"/foo?param=value&pet=dog"
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|String
name|signatureHeader
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
name|String
name|expectedHeader
init|=
literal|"keyId=\"Test\",algorithm=\"rsa-sha256\","
operator|+
literal|"signature=\"SjWJWbWN7i0wzBvtPl8rbASWz5xQW6mcJmn+ibttBqtifLN7Sazz"
operator|+
literal|"6m79cNfwwb8DMJ5cou1s7uEGKKCs+FLEEaDV5lp7q25WqS+lavg7T8hc0GppauB"
operator|+
literal|"6hbgEKTwblDHYGEtbGmtdHgVCk9SuS13F0hZ8FD0k/5OxEPXe5WozsbM=\""
decl_stmt|;
comment|// CXF adds all headers by default, so above we explicitly only add Date just to simulate the expected header
name|assertEquals
argument_list|(
name|signatureHeader
operator|.
name|replaceAll
argument_list|(
literal|"headers=\"date\","
argument_list|,
literal|""
argument_list|)
argument_list|,
name|expectedHeader
argument_list|)
expr_stmt|;
comment|// Now check we validate the Date header as expected on an empty header list
name|headers
operator|.
name|put
argument_list|(
literal|"Signature"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|expectedHeader
argument_list|)
argument_list|)
expr_stmt|;
name|MessageVerifier
name|messageVerifier
init|=
operator|new
name|MessageVerifier
argument_list|(
operator|new
name|MockPublicKeyProvider
argument_list|(
name|publicKey
argument_list|)
argument_list|)
decl_stmt|;
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
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
literal|"POST"
argument_list|,
literal|"/foo?param=value&pet=dog"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|basicTest
parameter_list|()
throws|throws
name|IOException
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
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
name|privateKeyProvider
argument_list|,
literal|"Test"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"(request-target)"
argument_list|,
literal|"host"
argument_list|,
literal|"Date"
argument_list|)
argument_list|)
decl_stmt|;
name|messageSigner
operator|.
name|sign
argument_list|(
name|headers
argument_list|,
literal|"/foo?param=value&pet=dog"
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|String
name|signatureHeader
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
name|String
name|expectedHeader
init|=
literal|"keyId=\"Test\",algorithm=\"rsa-sha256\","
operator|+
literal|"headers=\"(request-target) host date\",signature=\"qdx+H7PHHDZgy4"
operator|+
literal|"y/Ahn9Tny9V3GP6YgBPyUXMmoxWtLbHpUnXS2mg2+SbrQDMCJypxBLSPQR2aAjn"
operator|+
literal|"7ndmw2iicw3HMbe8VfEdKFYRqzic+efkb3nndiv/x1xSHDJWeSWkx3ButlYSuBs"
operator|+
literal|"kLu6kd9Fswtemr3lgdDEmn04swr2Os0=\""
decl_stmt|;
name|assertEquals
argument_list|(
name|signatureHeader
argument_list|,
name|expectedHeader
argument_list|)
expr_stmt|;
name|MessageVerifier
name|messageVerifier
init|=
operator|new
name|MessageVerifier
argument_list|(
operator|new
name|MockPublicKeyProvider
argument_list|(
name|publicKey
argument_list|)
argument_list|)
decl_stmt|;
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
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
literal|"POST"
argument_list|,
literal|"/foo?param=value&pet=dog"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|allHeadersTest
parameter_list|()
throws|throws
name|IOException
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
name|MessageSigner
name|messageSigner
init|=
operator|new
name|MessageSigner
argument_list|(
name|privateKeyProvider
argument_list|,
literal|"Test"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"(request-target)"
argument_list|,
literal|"host"
argument_list|,
literal|"date"
argument_list|,
literal|"content-type"
argument_list|,
literal|"digest"
argument_list|,
literal|"content-length"
argument_list|)
argument_list|)
decl_stmt|;
name|messageSigner
operator|.
name|sign
argument_list|(
name|headers
argument_list|,
literal|"/foo?param=value&pet=dog"
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
name|String
name|signatureHeader
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
name|String
name|expectedHeader
init|=
literal|"keyId=\"Test\",algorithm=\"rsa-sha256\","
operator|+
literal|"headers=\"(request-target) host date content-type digest content-length\","
operator|+
literal|"signature=\"vSdrb+dS3EceC9bcwHSo4MlyKS59iFIrhgYkz8+oVLEEzmYZZvRs"
operator|+
literal|"8rgOp+63LEM3v+MFHB32NfpB2bEKBIvB1q52LaEUHFv120V01IL+TAD48XaERZF"
operator|+
literal|"ukWgHoBTLMhYS2Gb51gWxpeIq8knRmPnYePbF5MOkR0Zkly4zKH7s1dE=\""
decl_stmt|;
name|assertEquals
argument_list|(
name|signatureHeader
argument_list|,
name|expectedHeader
argument_list|)
expr_stmt|;
name|MessageVerifier
name|messageVerifier
init|=
operator|new
name|MessageVerifier
argument_list|(
operator|new
name|MockPublicKeyProvider
argument_list|(
name|publicKey
argument_list|)
argument_list|)
decl_stmt|;
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
name|messageVerifier
operator|.
name|verifyMessage
argument_list|(
name|headers
argument_list|,
literal|"POST"
argument_list|,
literal|"/foo?param=value&pet=dog"
argument_list|)
expr_stmt|;
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
literal|"example.com"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Date"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"Sun, 05 Jan 2014 21:31:40 GMT"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Content-Type"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"application/json"
argument_list|)
argument_list|)
expr_stmt|;
name|headers
operator|.
name|put
argument_list|(
literal|"Digest"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"SHA-256=X48E9qOokqqrvdts8nOJRJN3OWDUoyWxBf7kbu9DBPE="
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
return|return
name|headers
return|;
block|}
block|}
end_class

end_unit

