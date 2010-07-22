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
name|ws
operator|.
name|security
package|;
end_package

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
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SecurityConstants
block|{
specifier|public
specifier|static
specifier|final
name|String
name|USERNAME
init|=
literal|"ws-security.username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|PASSWORD
init|=
literal|"ws-security.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|VALIDATE_PASSWORD
init|=
literal|"ws-security.validate.password"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CALLBACK_HANDLER
init|=
literal|"ws-security.callback-handler"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_USERNAME
init|=
literal|"ws-security.signature.username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_PROPERTIES
init|=
literal|"ws-security.signature.properties"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_USERNAME
init|=
literal|"ws-security.encryption.username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_PROPERTIES
init|=
literal|"ws-security.encryption.properties"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SIGNATURE_CRYPTO
init|=
literal|"ws-security.signature.crypto"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|ENCRYPT_CRYPTO
init|=
literal|"ws-security.encryption.crypto"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN
init|=
literal|"ws-security.token"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TOKEN_ID
init|=
literal|"ws-security.token.id"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STS_CLIENT
init|=
literal|"ws-security.sts.client"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STS_APPLIES_TO
init|=
literal|"ws-security.sts.applies-to"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TIMESTAMP_TTL
init|=
literal|"ws-security.timestamp.timeToLive"
decl_stmt|;
comment|//WebLogic and WCF always encrypt UsernameTokens whenever possible
comment|//See:  http://e-docs.bea.com/wls/docs103/webserv_intro/interop.html
comment|//Be default, we will encrypt as well for interop reasons.  However, this
comment|//setting can be set to false to turn that off.
specifier|public
specifier|static
specifier|final
name|String
name|ALWAYS_ENCRYPT_UT
init|=
literal|"ws-security.username-token.always.encrypted"
decl_stmt|;
comment|/**      * WCF's trust server sometimes will encrypt the token in the response IN ADDITION TO      * the full security on the message. These properties control the way the STS client      * will decrypt the EncryptedData elements in the response      *       * These are also used by the STSClient to send/process any RSA/DSAKeyValue tokens       * used if the KeyType is "PublicKey"       */
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_CRYPTO
init|=
literal|"ws-security.sts.token.crypto"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_PROPERTIES
init|=
literal|"ws-security.sts.token.properties"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_USERNAME
init|=
literal|"ws-security.sts.token.username"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_DO_CANCEL
init|=
literal|"ws-security.sts.token.do.cancel"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|STS_TOKEN_ACT_AS
init|=
literal|"ws-security.sts.token.act-as"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|Set
argument_list|<
name|String
argument_list|>
name|ALL_PROPERTIES
decl_stmt|;
static|static
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|s
init|=
operator|new
name|HashSet
argument_list|<
name|String
argument_list|>
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|USERNAME
block|,
name|PASSWORD
block|,
name|CALLBACK_HANDLER
block|,
name|SIGNATURE_USERNAME
block|,
name|SIGNATURE_PROPERTIES
block|,
name|SIGNATURE_CRYPTO
block|,
name|ENCRYPT_USERNAME
block|,
name|ENCRYPT_PROPERTIES
block|,
name|ENCRYPT_CRYPTO
block|,
name|TOKEN
block|,
name|TOKEN_ID
block|,
name|STS_CLIENT
block|,
name|STS_TOKEN_PROPERTIES
block|,
name|STS_TOKEN_CRYPTO
block|,
name|STS_TOKEN_DO_CANCEL
block|,
name|TIMESTAMP_TTL
block|,
name|ALWAYS_ENCRYPT_UT
block|,
name|STS_TOKEN_ACT_AS
block|}
argument_list|)
argument_list|)
decl_stmt|;
name|ALL_PROPERTIES
operator|=
name|Collections
operator|.
name|unmodifiableSet
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
specifier|private
name|SecurityConstants
parameter_list|()
block|{
comment|//utility class
block|}
block|}
end_class

end_unit

