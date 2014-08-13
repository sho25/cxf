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
name|jaxrs
operator|.
name|security
operator|.
name|jwt
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Security
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|Properties
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|crypto
operator|.
name|Cipher
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
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
name|oauth2
operator|.
name|jwe
operator|.
name|AesCbcHmacJweDecryption
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
name|oauth2
operator|.
name|jwe
operator|.
name|AesCbcHmacJweEncryption
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
name|oauth2
operator|.
name|jwe
operator|.
name|AesWrapKeyDecryptionAlgorithm
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
name|oauth2
operator|.
name|jwe
operator|.
name|AesWrapKeyEncryptionAlgorithm
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
name|oauth2
operator|.
name|jws
operator|.
name|HmacJwsSignatureProvider
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
name|oauth2
operator|.
name|jws
operator|.
name|JwsSignatureProvider
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
name|oauth2
operator|.
name|jwt
operator|.
name|Algorithm
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
name|oauth2
operator|.
name|jwt
operator|.
name|jaxrs
operator|.
name|JweClientResponseFilter
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
name|oauth2
operator|.
name|jwt
operator|.
name|jaxrs
operator|.
name|JweWriterInterceptor
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
name|oauth2
operator|.
name|jwt
operator|.
name|jaxrs
operator|.
name|JwsClientResponseFilter
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
name|oauth2
operator|.
name|jwt
operator|.
name|jaxrs
operator|.
name|JwsWriterInterceptor
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
name|oauth2
operator|.
name|utils
operator|.
name|crypto
operator|.
name|PrivateKeyPasswordProvider
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusClientServerTestBase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|bouncycastle
operator|.
name|jce
operator|.
name|provider
operator|.
name|BouncyCastleProvider
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|AfterClass
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
name|JAXRSJweJwsTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookServerJwt
operator|.
name|PORT
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_JWEJWS_PROPERTIES
init|=
literal|"org/apache/cxf/systest/jaxrs/security/bob.rs.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|SERVER_JWEJWS_PROPERTIES
init|=
literal|"org/apache/cxf/systest/jaxrs/security/alice.rs.properties"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ENCODED_MAC_KEY
init|=
literal|"AyM1SysPpbyDfgZld3umj1qzKObwVMkoqQ-EstJQLr_T-1qS0gZH75"
operator|+
literal|"aKtMN3Yj0iPS4hcgUuTwjAzZr1Z9CAow"
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|BookServerJwt
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|registerBouncyCastleIfNeeded
argument_list|()
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|registerBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
comment|// Java 8 apparently has it
name|Cipher
operator|.
name|getInstance
argument_list|(
name|Algorithm
operator|.
name|AES_GCM_ALGO_JAVA
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Oracle Java 7
name|Security
operator|.
name|addProvider
argument_list|(
operator|new
name|BouncyCastleProvider
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|AfterClass
specifier|public
specifier|static
name|void
name|unregisterBouncyCastleIfNeeded
parameter_list|()
throws|throws
name|Exception
block|{
name|Security
operator|.
name|removeProvider
argument_list|(
name|BouncyCastleProvider
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJweRsaJwsRsa
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/jwejwsrsa"
decl_stmt|;
name|doTestJweJwsRsa
argument_list|(
name|address
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJweRsaJwsHMac
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/jwejwshmac"
decl_stmt|;
name|HmacJwsSignatureProvider
name|hmacProvider
init|=
operator|new
name|HmacJwsSignatureProvider
argument_list|(
name|ENCODED_MAC_KEY
argument_list|)
decl_stmt|;
name|hmacProvider
operator|.
name|setDefaultJwtAlgorithm
argument_list|(
name|Algorithm
operator|.
name|HmacSHA256
operator|.
name|getJwtName
argument_list|()
argument_list|)
expr_stmt|;
name|doTestJweJwsRsa
argument_list|(
name|address
argument_list|,
name|hmacProvider
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestJweJwsRsa
parameter_list|(
name|String
name|address
parameter_list|,
name|JwsSignatureProvider
name|jwsSigProvider
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|JAXRSJweJwsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
name|JweWriterInterceptor
name|jweWriter
init|=
operator|new
name|JweWriterInterceptor
argument_list|()
decl_stmt|;
name|jweWriter
operator|.
name|setUseJweOutputStream
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|jweWriter
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JweClientResponseFilter
argument_list|()
argument_list|)
expr_stmt|;
name|JwsWriterInterceptor
name|jwsWriter
init|=
operator|new
name|JwsWriterInterceptor
argument_list|()
decl_stmt|;
if|if
condition|(
name|jwsSigProvider
operator|!=
literal|null
condition|)
block|{
name|jwsWriter
operator|.
name|setSignatureProvider
argument_list|(
name|jwsSigProvider
argument_list|)
expr_stmt|;
block|}
name|jwsWriter
operator|.
name|setUseJwsOutputStream
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|jwsWriter
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
operator|new
name|JwsClientResponseFilter
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.encryption.out.properties"
argument_list|,
name|SERVER_JWEJWS_PROPERTIES
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.signature.out.properties"
argument_list|,
name|CLIENT_JWEJWS_PROPERTIES
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.encryption.in.properties"
argument_list|,
name|CLIENT_JWEJWS_PROPERTIES
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.signature.in.properties"
argument_list|,
name|SERVER_JWEJWS_PROPERTIES
argument_list|)
expr_stmt|;
name|PrivateKeyPasswordProvider
name|provider
init|=
operator|new
name|PrivateKeyPasswordProviderImpl
argument_list|()
decl_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.signature.key.password.provider"
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|put
argument_list|(
literal|"rs.security.decryption.key.password.provider"
argument_list|,
name|provider
argument_list|)
expr_stmt|;
name|BookStore
name|bs
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|bs
operator|.
name|echoText
argument_list|(
literal|"book"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"book"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJweAesCbcHmac
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|address
init|=
literal|"https://localhost:"
operator|+
name|PORT
operator|+
literal|"/jweaescbchmac"
decl_stmt|;
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|SpringBusFactory
name|bf
init|=
operator|new
name|SpringBusFactory
argument_list|()
decl_stmt|;
name|URL
name|busFile
init|=
name|JAXRSJweJwsTest
operator|.
name|class
operator|.
name|getResource
argument_list|(
literal|"client.xml"
argument_list|)
decl_stmt|;
name|Bus
name|springBus
init|=
name|bf
operator|.
name|createBus
argument_list|(
name|busFile
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|bean
operator|.
name|setBus
argument_list|(
name|springBus
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|LinkedList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
comment|// writer
name|JweWriterInterceptor
name|jweWriter
init|=
operator|new
name|JweWriterInterceptor
argument_list|()
decl_stmt|;
comment|//jweWriter.setUseJweOutputStream(true);
specifier|final
name|String
name|cekEncryptionKey
init|=
literal|"GawgguFyGrWKav7AX4VKUg"
decl_stmt|;
name|AesWrapKeyEncryptionAlgorithm
name|keyEncryption
init|=
operator|new
name|AesWrapKeyEncryptionAlgorithm
argument_list|(
name|cekEncryptionKey
argument_list|,
name|Algorithm
operator|.
name|A128KW
operator|.
name|getJwtName
argument_list|()
argument_list|)
decl_stmt|;
name|jweWriter
operator|.
name|setEncryptionProvider
argument_list|(
operator|new
name|AesCbcHmacJweEncryption
argument_list|(
name|Algorithm
operator|.
name|A128KW
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|Algorithm
operator|.
name|A128CBC_HS256
operator|.
name|getJwtName
argument_list|()
argument_list|,
name|keyEncryption
argument_list|)
argument_list|)
expr_stmt|;
comment|// reader
name|JweClientResponseFilter
name|jweReader
init|=
operator|new
name|JweClientResponseFilter
argument_list|()
decl_stmt|;
name|jweReader
operator|.
name|setDecryptionProvider
argument_list|(
operator|new
name|AesCbcHmacJweDecryption
argument_list|(
operator|new
name|AesWrapKeyDecryptionAlgorithm
argument_list|(
name|cekEncryptionKey
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|jweWriter
argument_list|)
expr_stmt|;
name|providers
operator|.
name|add
argument_list|(
name|jweReader
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setProviders
argument_list|(
name|providers
argument_list|)
expr_stmt|;
name|BookStore
name|bs
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|text
init|=
name|bs
operator|.
name|echoText
argument_list|(
literal|"book"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"book"
argument_list|,
name|text
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|PrivateKeyPasswordProviderImpl
implements|implements
name|PrivateKeyPasswordProvider
block|{
annotation|@
name|Override
specifier|public
name|char
index|[]
name|getPassword
parameter_list|(
name|Properties
name|storeProperties
parameter_list|)
block|{
return|return
literal|"password"
operator|.
name|toCharArray
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

