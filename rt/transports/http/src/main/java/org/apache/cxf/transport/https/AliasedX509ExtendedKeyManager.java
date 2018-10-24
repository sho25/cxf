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
name|transport
operator|.
name|https
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
name|SSLEngine
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
name|X509ExtendedKeyManager
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

begin_comment
comment|/* ------------------------------------------------------------ */
end_comment

begin_comment
comment|/**  * KeyManager to select a key with desired alias while delegating processing to specified KeyManager Can be  * used both with server and client sockets  */
end_comment

begin_class
specifier|public
class|class
name|AliasedX509ExtendedKeyManager
extends|extends
name|X509ExtendedKeyManager
block|{
specifier|private
name|String
name|keyAlias
decl_stmt|;
specifier|private
name|X509KeyManager
name|keyManager
decl_stmt|;
comment|/* ------------------------------------------------------------ */
comment|/**      * Construct KeyManager instance      *      * @param keyAlias Alias of the key to be selected      * @param keyManager Instance of KeyManager to be wrapped      * @throws Exception      */
specifier|public
name|AliasedX509ExtendedKeyManager
parameter_list|(
name|String
name|keyAlias
parameter_list|,
name|X509KeyManager
name|keyManager
parameter_list|)
throws|throws
name|Exception
block|{
name|this
operator|.
name|keyAlias
operator|=
name|keyAlias
expr_stmt|;
name|this
operator|.
name|keyManager
operator|=
name|keyManager
expr_stmt|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509KeyManager#chooseClientAlias(java.lang.String[], java.security.Principal[],      *      java.net.Socket)      */
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
name|keyAlias
operator|==
literal|null
condition|?
name|keyManager
operator|.
name|chooseClientAlias
argument_list|(
name|keyType
argument_list|,
name|issuers
argument_list|,
name|socket
argument_list|)
else|:
name|keyAlias
return|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509KeyManager#chooseServerAlias(java.lang.String, java.security.Principal[],      *      java.net.Socket)      */
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
name|keyAlias
operator|==
literal|null
condition|?
name|keyManager
operator|.
name|chooseServerAlias
argument_list|(
name|keyType
argument_list|,
name|issuers
argument_list|,
name|socket
argument_list|)
else|:
name|keyAlias
return|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509KeyManager#getClientAliases(java.lang.String, java.security.Principal[])      */
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
name|keyManager
operator|.
name|getClientAliases
argument_list|(
name|keyType
argument_list|,
name|issuers
argument_list|)
return|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509KeyManager#getServerAliases(java.lang.String, java.security.Principal[])      */
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
name|keyManager
operator|.
name|getServerAliases
argument_list|(
name|keyType
argument_list|,
name|issuers
argument_list|)
return|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509KeyManager#getCertificateChain(java.lang.String)      */
specifier|public
name|X509Certificate
index|[]
name|getCertificateChain
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
return|return
name|keyManager
operator|.
name|getCertificateChain
argument_list|(
name|alias
argument_list|)
return|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509KeyManager#getPrivateKey(java.lang.String)      */
specifier|public
name|PrivateKey
name|getPrivateKey
parameter_list|(
name|String
name|alias
parameter_list|)
block|{
return|return
name|keyManager
operator|.
name|getPrivateKey
argument_list|(
name|alias
argument_list|)
return|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509ExtendedKeyManager#chooseEngineServerAlias(java.lang.String,      *      java.security.Principal[], javax.net.ssl.SSLEngine)      */
annotation|@
name|Override
specifier|public
name|String
name|chooseEngineServerAlias
parameter_list|(
name|String
name|keyType
parameter_list|,
name|Principal
index|[]
name|issuers
parameter_list|,
name|SSLEngine
name|engine
parameter_list|)
block|{
return|return
name|keyAlias
operator|==
literal|null
condition|?
name|super
operator|.
name|chooseEngineServerAlias
argument_list|(
name|keyType
argument_list|,
name|issuers
argument_list|,
name|engine
argument_list|)
else|:
name|keyAlias
return|;
block|}
comment|/* ------------------------------------------------------------ */
comment|/**      * @see javax.net.ssl.X509ExtendedKeyManager#chooseEngineClientAlias(String[], Principal[], SSLEngine)      */
annotation|@
name|Override
specifier|public
name|String
name|chooseEngineClientAlias
parameter_list|(
name|String
index|[]
name|keyType
parameter_list|,
name|Principal
index|[]
name|issuers
parameter_list|,
name|SSLEngine
name|engine
parameter_list|)
block|{
return|return
name|keyAlias
operator|==
literal|null
condition|?
name|super
operator|.
name|chooseEngineClientAlias
argument_list|(
name|keyType
argument_list|,
name|issuers
argument_list|,
name|engine
argument_list|)
else|:
name|keyAlias
return|;
block|}
block|}
end_class

end_unit

