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
name|sts
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|callback
operator|.
name|CallbackHandler
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
name|sts
operator|.
name|service
operator|.
name|EncryptionProperties
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
name|sts
operator|.
name|provider
operator|.
name|STSException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|ws
operator|.
name|security
operator|.
name|components
operator|.
name|crypto
operator|.
name|Crypto
import|;
end_import

begin_comment
comment|/**  * This MBean represents the properties associated with the STS. It contains a single operation  * "configureProperties()" which allows subclasses to perform any custom loading/processing of the   * properties.  */
end_comment

begin_interface
specifier|public
interface|interface
name|STSPropertiesMBean
block|{
comment|/**      * Load/process the CallbackHandler, Crypto objects, etc.      */
name|void
name|configureProperties
parameter_list|()
throws|throws
name|STSException
function_decl|;
comment|/**      * Set the CallbackHandler object.       * @param callbackHandler the CallbackHandler object.       */
name|void
name|setCallbackHandler
parameter_list|(
name|CallbackHandler
name|callbackHandler
parameter_list|)
function_decl|;
comment|/**      * Get the CallbackHandler object.      * @return the CallbackHandler object.      */
name|CallbackHandler
name|getCallbackHandler
parameter_list|()
function_decl|;
comment|/**      * Set the signature Crypto object      * @param signatureCrypto the signature Crypto object      */
name|void
name|setSignatureCrypto
parameter_list|(
name|Crypto
name|signatureCrypto
parameter_list|)
function_decl|;
comment|/**      * Get the signature Crypto object      * @return the signature Crypto object      */
name|Crypto
name|getSignatureCrypto
parameter_list|()
function_decl|;
comment|/**      * Set the username/alias to use to sign any issued tokens      * @param signatureUsername the username/alias to use to sign any issued tokens      */
name|void
name|setSignatureUsername
parameter_list|(
name|String
name|signatureUsername
parameter_list|)
function_decl|;
comment|/**      * Get the username/alias to use to sign any issued tokens      * @return the username/alias to use to sign any issued tokens      */
name|String
name|getSignatureUsername
parameter_list|()
function_decl|;
comment|/**      * Set the encryption Crypto object      * @param encryptionCrypto the encryption Crypto object      */
name|void
name|setEncryptionCrypto
parameter_list|(
name|Crypto
name|encryptionCrypto
parameter_list|)
function_decl|;
comment|/**      * Get the encryption Crypto object      * @return the encryption Crypto object      */
name|Crypto
name|getEncryptionCrypto
parameter_list|()
function_decl|;
comment|/**      * Set the username/alias to use to encrypt any issued tokens. This is a default value - it      * can be configured per Service in the ServiceMBean.      * @param encryptionUsername the username/alias to use to encrypt any issued tokens      */
name|void
name|setEncryptionUsername
parameter_list|(
name|String
name|encryptionUsername
parameter_list|)
function_decl|;
comment|/**      * Get the username/alias to use to encrypt any issued tokens. This is a default value - it      * can be configured per Service in the ServiceMBean      * @return the username/alias to use to encrypt any issued tokens      */
name|String
name|getEncryptionUsername
parameter_list|()
function_decl|;
comment|/**      * Set the EncryptionProperties to use.      * @param encryptionProperties the EncryptionProperties to use.      */
name|void
name|setEncryptionProperties
parameter_list|(
name|EncryptionProperties
name|encryptionProperties
parameter_list|)
function_decl|;
comment|/**      * Get the EncryptionProperties to use.      * @return the EncryptionProperties to use.      */
name|EncryptionProperties
name|getEncryptionProperties
parameter_list|()
function_decl|;
comment|/**      * Set the STS issuer name      * @param issuer the STS issuer name      */
name|void
name|setIssuer
parameter_list|(
name|String
name|issuer
parameter_list|)
function_decl|;
comment|/**      * Get the STS issuer name      * @return the STS issuer name      */
name|String
name|getIssuer
parameter_list|()
function_decl|;
comment|/**      * Set the SignatureProperties to use.      * @param signatureProperties the SignatureProperties to use.      */
name|void
name|setSignatureProperties
parameter_list|(
name|SignatureProperties
name|signatureProperties
parameter_list|)
function_decl|;
comment|/**      * Get the SignatureProperties to use.      * @return the SignatureProperties to use.      */
name|SignatureProperties
name|getSignatureProperties
parameter_list|()
function_decl|;
comment|/**      * Set the RealmParser object to use.      * @param realmParser the RealmParser object to use.      */
name|void
name|setRealmParser
parameter_list|(
name|RealmParser
name|realmParser
parameter_list|)
function_decl|;
comment|/**      * Get the RealmParser object to use.      * @return the RealmParser object to use.      */
name|RealmParser
name|getRealmParser
parameter_list|()
function_decl|;
comment|/**      * Set the IdentityMapper object to use.      * @param identityMapper the IdentityMapper object to use.      */
name|void
name|setIdentityMapper
parameter_list|(
name|IdentityMapper
name|identityMapper
parameter_list|)
function_decl|;
comment|/**      * Get the IdentityMapper object to use.      * @return the IdentityMapper object to use.      */
name|IdentityMapper
name|getIdentityMapper
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

