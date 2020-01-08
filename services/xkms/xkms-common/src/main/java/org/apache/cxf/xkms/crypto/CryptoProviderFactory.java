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

begin_interface
specifier|public
interface|interface
name|CryptoProviderFactory
block|{
comment|/**      * Create with merlin fallback settings retrieved from cxf message      * @param message      * @return      */
name|Crypto
name|create
parameter_list|(
name|Message
name|message
parameter_list|)
function_decl|;
comment|/**      * Create without fallback crypto      *      * @return xkms crypto      */
name|Crypto
name|create
parameter_list|()
function_decl|;
comment|/**      * Create with fallback crypto      *      * @param fallbackCrypto      * @return      */
name|Crypto
name|create
parameter_list|(
name|Crypto
name|fallbackCrypto
parameter_list|)
function_decl|;
comment|/**      * Create with overridden keystoreProperties to create default Crypto      *      * @param keystoreProperties      * @return      */
name|Crypto
name|create
parameter_list|(
name|String
name|keystoreProperties
parameter_list|)
function_decl|;
comment|/**      * Create with overridden XKMSPortType and fallbackCrypto      *      * @param xkmsClient      * @param fallbackCrypto      * @return      */
name|Crypto
name|create
parameter_list|(
name|XKMSPortType
name|xkmsClient
parameter_list|,
name|Crypto
name|fallbackCrypto
parameter_list|)
function_decl|;
comment|/**      * Create with overridden XKMSPortType, fallbackCrypto and control of getting X509 from local keystore      *      * @param xkmsClient      * @param fallbackCrypto      * @param allowX509FromJKS      * @return      */
name|Crypto
name|create
parameter_list|(
name|XKMSPortType
name|xkmsClient
parameter_list|,
name|Crypto
name|fallbackCrypto
parameter_list|,
name|boolean
name|allowX509FromJKS
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

