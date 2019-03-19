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
name|KeyStore
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|KeyStoreException
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
name|UnrecoverableKeyException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|cert
operator|.
name|CertificateException
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
name|common
operator|.
name|classloader
operator|.
name|ClassLoaderUtils
import|;
end_import

begin_comment
comment|/**  * Just a test-class to provide a static method to easily load a PrivateKey in spring config.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PrivateKeyProvider
block|{
specifier|private
name|PrivateKeyProvider
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|PrivateKey
name|loadPrivateKey
parameter_list|()
block|{
name|KeyStore
name|keyStore
decl_stmt|;
try|try
block|{
name|keyStore
operator|=
name|KeyStore
operator|.
name|getInstance
argument_list|(
literal|"JKS"
argument_list|)
expr_stmt|;
name|keyStore
operator|.
name|load
argument_list|(
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
literal|"keys/alice.jks"
argument_list|,
name|PrivateKeyProvider
operator|.
name|class
argument_list|)
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
expr_stmt|;
return|return
operator|(
name|PrivateKey
operator|)
name|keyStore
operator|.
name|getKey
argument_list|(
literal|"alice"
argument_list|,
literal|"password"
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
decl||
name|NoSuchAlgorithmException
decl||
name|CertificateException
decl||
name|IOException
decl||
name|UnrecoverableKeyException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

