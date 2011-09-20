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

begin_class
specifier|public
specifier|final
class|class
name|STSConstants
block|{
comment|/**      * WS-Trust 1.3 namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WST_NS_05_12
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200512"
decl_stmt|;
comment|/**      * WS-Trust 1.4 namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WST_NS_08_02
init|=
literal|"http://docs.oasis-open.org/ws-sx/ws-trust/200802"
decl_stmt|;
comment|/**      * Identity namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|IDT_NS_05_05
init|=
literal|"http://schemas.xmlsoap.org/ws/2005/05/identity"
decl_stmt|;
comment|/**      * WS-Security extension namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WSSE_EXT_04_01
init|=
literal|"http://www.docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd"
decl_stmt|;
comment|/**      * WS-Security utility namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WS_UTIL_03_06
init|=
literal|"http://schemas.xmlsoap.org/ws/2003/06/utility"
decl_stmt|;
comment|/**      * Asymmetric key type (attribute of BinarySecret)      */
specifier|public
specifier|static
specifier|final
name|String
name|ASYMMETRIC_KEY_TYPE
init|=
name|WST_NS_05_12
operator|+
literal|"/AsymmetricKey"
decl_stmt|;
comment|/**      * Symmetric key type (attribute of BinarySecret)      */
specifier|public
specifier|static
specifier|final
name|String
name|SYMMETRIC_KEY_TYPE
init|=
name|WST_NS_05_12
operator|+
literal|"/SymmetricKey"
decl_stmt|;
comment|/**      * Nonce key type (attribute of BinarySecret)      */
specifier|public
specifier|static
specifier|final
name|String
name|NONCE_TYPE
init|=
name|WST_NS_05_12
operator|+
literal|"/Nonce"
decl_stmt|;
comment|/**      * WS-Policy namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WSP_NS
init|=
literal|"http://www.w3.org/ns/ws-policy"
decl_stmt|;
comment|/**      * WS-Policy 2004 namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WSP_NS_04
init|=
literal|"http://schemas.xmlsoap.org/ws/2004/09/policy"
decl_stmt|;
comment|/**      * WS-Addressing 2005 namespace      */
specifier|public
specifier|static
specifier|final
name|String
name|WSA_NS_05
init|=
literal|"http://www.w3.org/2005/08/addressing"
decl_stmt|;
comment|/**      * Symmetric key (KeyType value)      */
specifier|public
specifier|static
specifier|final
name|String
name|SYMMETRIC_KEY_KEYTYPE
init|=
name|WST_NS_05_12
operator|+
literal|"/SymmetricKey"
decl_stmt|;
comment|/**      * Public key (KeyType value)      */
specifier|public
specifier|static
specifier|final
name|String
name|PUBLIC_KEY_KEYTYPE
init|=
name|WST_NS_05_12
operator|+
literal|"/PublicKey"
decl_stmt|;
comment|/**      * Bearer key (KeyType value)      */
specifier|public
specifier|static
specifier|final
name|String
name|BEARER_KEY_KEYTYPE
init|=
name|WST_NS_05_12
operator|+
literal|"/Bearer"
decl_stmt|;
comment|/**      * ComputedKey P-SHA1 URI      */
specifier|public
specifier|static
specifier|final
name|String
name|COMPUTED_KEY_PSHA1
init|=
name|WST_NS_05_12
operator|+
literal|"/CK/PSHA1"
decl_stmt|;
comment|/**      * Status TokenType      */
specifier|public
specifier|static
specifier|final
name|String
name|STATUS
init|=
name|WST_NS_05_12
operator|+
literal|"/RSTR/Status"
decl_stmt|;
comment|/**      * Valid Status Code      */
specifier|public
specifier|static
specifier|final
name|String
name|VALID_CODE
init|=
name|WST_NS_05_12
operator|+
literal|"/status/valid"
decl_stmt|;
comment|/**      * Invalid Status Code      */
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_CODE
init|=
name|WST_NS_05_12
operator|+
literal|"/status/invalid"
decl_stmt|;
comment|/**      * Valid Status Reason      */
specifier|public
specifier|static
specifier|final
name|String
name|VALID_REASON
init|=
literal|"The Trust service successfully validated the input"
decl_stmt|;
comment|/**      * Invalid Status Reason      */
specifier|public
specifier|static
specifier|final
name|String
name|INVALID_REASON
init|=
literal|"The Trust service did not successfully validate the input"
decl_stmt|;
specifier|private
name|STSConstants
parameter_list|()
block|{
comment|// complete
block|}
block|}
end_class

end_unit

