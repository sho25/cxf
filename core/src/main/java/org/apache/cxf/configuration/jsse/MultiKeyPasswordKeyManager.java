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
name|configuration
operator|.
name|jsse
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|Socket
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|GeneralSecurityException
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
name|Principal
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
name|cert
operator|.
name|Certificate
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
name|X509Certificate
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|net
operator|.
name|ssl
operator|.
name|X509KeyManager
import|;
end_import

begin_class
specifier|public
class|class
name|MultiKeyPasswordKeyManager
implements|implements
name|X509KeyManager
block|{
specifier|private
specifier|final
name|KeyStore
name|mKeyStore
decl_stmt|;
specifier|private
specifier|final
name|String
name|mKeyAlias
decl_stmt|;
specifier|private
specifier|final
name|String
name|mKeyPassword
decl_stmt|;
specifier|public
name|MultiKeyPasswordKeyManager
parameter_list|(
name|KeyStore
name|keystore
parameter_list|,
name|String
name|keyAlias
parameter_list|,
name|String
name|keyPassword
parameter_list|)
block|{
name|mKeyStore
operator|=
name|keystore
expr_stmt|;
name|mKeyAlias
operator|=
name|keyAlias
expr_stmt|;
name|mKeyPassword
operator|=
name|keyPassword
expr_stmt|;
block|}
specifier|public
name|String
index|[]
name|getClientAliases
parameter_list|(
name|String
name|keyType
parameter_list|,
name|Principal
index|[]
name|issuers
parameter_list|)
block|{
return|return
operator|new
name|String
index|[]
block|{
name|mKeyAlias
block|}
return|;
block|}
specifier|public
name|String
index|[]
name|getServerAliases
parameter_list|(
name|String
name|keyType
parameter_list|,
name|Principal
index|[]
name|issuers
parameter_list|)
block|{
return|return
operator|new
name|String
index|[]
block|{
name|mKeyAlias
block|}
return|;
block|}
specifier|public
name|X509Certificate
index|[]
name|getCertificateChain
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
name|Certificate
index|[]
name|chain
init|=
literal|null
decl_stmt|;
try|try
block|{
name|chain
operator|=
name|mKeyStore
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|KeyStoreException
name|kse
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|kse
argument_list|)
throw|;
block|}
specifier|final
name|X509Certificate
index|[]
name|certChain
init|=
operator|new
name|X509Certificate
index|[
name|chain
operator|.
name|length
index|]
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|chain
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|certChain
index|[
name|i
index|]
operator|=
operator|(
name|X509Certificate
operator|)
name|chain
index|[
name|i
index|]
expr_stmt|;
block|}
return|return
name|certChain
return|;
block|}
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
try|try
block|{
return|return
operator|(
name|PrivateKey
operator|)
name|mKeyStore
operator|.
name|getKey
argument_list|(
name|alias
argument_list|,
name|mKeyPassword
operator|.
name|toCharArray
argument_list|()
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|GeneralSecurityException
name|gse
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|gse
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|chooseClientAlias
parameter_list|(
name|String
index|[]
name|keyType
parameter_list|,
name|Principal
index|[]
name|issuers
parameter_list|,
name|Socket
name|socket
parameter_list|)
block|{
return|return
name|mKeyAlias
return|;
block|}
specifier|public
name|String
name|chooseServerAlias
parameter_list|(
name|String
name|keyType
parameter_list|,
name|Principal
index|[]
name|issuers
parameter_list|,
name|Socket
name|socket
parameter_list|)
block|{
return|return
name|mKeyAlias
return|;
block|}
block|}
end_class

end_unit

