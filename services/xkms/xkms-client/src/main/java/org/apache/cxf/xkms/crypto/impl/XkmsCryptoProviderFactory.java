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
name|xkms
operator|.
name|crypto
operator|.
name|impl
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|Message
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
name|SecurityConstants
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
name|xkms
operator|.
name|crypto
operator|.
name|CryptoProviderException
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
name|xkms
operator|.
name|crypto
operator|.
name|CryptoProviderFactory
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
name|Crypto
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
name|CryptoFactory
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
name|w3
operator|.
name|_2002
operator|.
name|_03
operator|.
name|xkms_wsdl
operator|.
name|XKMSPortType
import|;
end_import

begin_comment
comment|/**  * For usage in OSGi this factory will be published as a service.  * Outside OSGi it can be used directly   */
end_comment

begin_class
specifier|public
class|class
name|XkmsCryptoProviderFactory
implements|implements
name|CryptoProviderFactory
block|{
specifier|private
specifier|final
name|XKMSPortType
name|xkmsConsumer
decl_stmt|;
specifier|public
name|XkmsCryptoProviderFactory
parameter_list|(
name|XKMSPortType
name|xkmsConsumer
parameter_list|)
block|{
name|this
operator|.
name|xkmsConsumer
operator|=
name|xkmsConsumer
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Crypto
name|create
parameter_list|(
name|Message
name|message
parameter_list|)
block|{
name|Properties
name|keystoreProps
init|=
name|CryptoProviderUtils
operator|.
name|loadKeystoreProperties
argument_list|(
name|message
argument_list|,
name|SecurityConstants
operator|.
name|SIGNATURE_PROPERTIES
argument_list|)
decl_stmt|;
try|try
block|{
name|Crypto
name|defaultCrypto
init|=
name|CryptoFactory
operator|.
name|getInstance
argument_list|(
name|keystoreProps
argument_list|)
decl_stmt|;
return|return
operator|new
name|XkmsCryptoProvider
argument_list|(
name|xkmsConsumer
argument_list|,
name|defaultCrypto
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|WSSecurityException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|CryptoProviderException
argument_list|(
literal|"Cannot instantiate crypto factory: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Crypto
name|create
parameter_list|()
block|{
return|return
operator|new
name|XkmsCryptoProvider
argument_list|(
name|xkmsConsumer
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Crypto
name|create
parameter_list|(
name|Crypto
name|fallbackCrypto
parameter_list|)
block|{
return|return
operator|new
name|XkmsCryptoProvider
argument_list|(
name|xkmsConsumer
argument_list|,
name|fallbackCrypto
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Crypto
name|create
parameter_list|(
name|XKMSPortType
name|xkmsClient
parameter_list|,
name|Crypto
name|fallbackCrypto
parameter_list|)
block|{
return|return
operator|new
name|XkmsCryptoProvider
argument_list|(
name|xkmsClient
argument_list|,
name|fallbackCrypto
argument_list|)
return|;
block|}
block|}
end_class

end_unit

