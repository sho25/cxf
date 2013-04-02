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
operator|.
name|wss4j
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|security
operator|.
name|UsernameToken
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
name|util
operator|.
name|Base64Utility
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
name|wss4j
operator|.
name|common
operator|.
name|principal
operator|.
name|UsernameTokenPrincipal
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|WSS4JTokenConverter
block|{
specifier|private
name|WSS4JTokenConverter
parameter_list|()
block|{              }
specifier|public
specifier|static
name|void
name|convertToken
parameter_list|(
name|Message
name|msg
parameter_list|,
name|Principal
name|p
parameter_list|)
block|{
if|if
condition|(
name|p
operator|instanceof
name|UsernameTokenPrincipal
condition|)
block|{
name|UsernameTokenPrincipal
name|utp
init|=
operator|(
name|UsernameTokenPrincipal
operator|)
name|p
decl_stmt|;
name|String
name|nonce
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|utp
operator|.
name|getNonce
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|nonce
operator|=
name|Base64Utility
operator|.
name|encode
argument_list|(
name|utp
operator|.
name|getNonce
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|put
argument_list|(
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|security
operator|.
name|SecurityToken
operator|.
name|class
argument_list|,
operator|new
name|UsernameToken
argument_list|(
name|utp
operator|.
name|getName
argument_list|()
argument_list|,
name|utp
operator|.
name|getPassword
argument_list|()
argument_list|,
name|utp
operator|.
name|getPasswordType
argument_list|()
argument_list|,
name|utp
operator|.
name|isPasswordDigest
argument_list|()
argument_list|,
name|nonce
argument_list|,
name|utp
operator|.
name|getCreatedTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

