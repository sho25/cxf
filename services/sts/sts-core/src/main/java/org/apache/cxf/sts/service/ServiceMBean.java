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
operator|.
name|service
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * This MBean represents a service. It defines a single operation  * "isAddressInEndpoints(String address)". This is called by the Issue binding, passing  * through the address URL that is supplied as part of "AppliesTo". The AppliesTo address  * must match with a "known" address of the implementation of this MBean.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ServiceMBean
block|{
comment|/**      * Return true if the supplied address corresponds to a known address for this service      */
name|boolean
name|isAddressInEndpoints
parameter_list|(
name|String
name|address
parameter_list|)
function_decl|;
comment|/**      * Get the default Token Type to be issued for this Service      */
name|String
name|getTokenType
parameter_list|()
function_decl|;
comment|/**      * Set the default Token Type to be issued for this Service      */
name|void
name|setTokenType
parameter_list|(
name|String
name|tokenType
parameter_list|)
function_decl|;
comment|/**      * Get the default Key Type to be issued for this Service      */
name|String
name|getKeyType
parameter_list|()
function_decl|;
comment|/**      * Set the default Key Type to be issued for this Service      */
name|void
name|setKeyType
parameter_list|(
name|String
name|keyType
parameter_list|)
function_decl|;
comment|/**      * Set the list of endpoint addresses that correspond to this service      */
name|void
name|setEndpoints
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|endpoints
parameter_list|)
function_decl|;
comment|/**      * Get the EncryptionProperties to be used to encrypt tokens issued for this service      */
name|EncryptionProperties
name|getEncryptionProperties
parameter_list|()
function_decl|;
comment|/**      * Set the EncryptionProperties to be used to encrypt tokens issued for this service      */
name|void
name|setEncryptionProperties
parameter_list|(
name|EncryptionProperties
name|encryptionProperties
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

