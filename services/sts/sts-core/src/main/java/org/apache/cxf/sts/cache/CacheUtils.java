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
name|cache
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
name|java
operator|.
name|time
operator|.
name|Instant
import|;
end_import

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
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|STSConstants
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
name|request
operator|.
name|Renewing
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
name|tokenstore
operator|.
name|SecurityToken
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
name|tokenstore
operator|.
name|TokenStore
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|CacheUtils
block|{
specifier|private
name|CacheUtils
parameter_list|()
block|{
comment|// complete
block|}
specifier|public
specifier|static
name|SecurityToken
name|createSecurityTokenForStorage
parameter_list|(
name|Element
name|token
parameter_list|,
name|String
name|tokenIdentifier
parameter_list|,
name|Instant
name|expiry
parameter_list|,
name|Principal
name|principal
parameter_list|,
name|String
name|realm
parameter_list|,
name|Renewing
name|renewing
parameter_list|)
block|{
name|SecurityToken
name|securityToken
init|=
operator|new
name|SecurityToken
argument_list|(
name|tokenIdentifier
argument_list|,
literal|null
argument_list|,
name|expiry
argument_list|)
decl_stmt|;
name|securityToken
operator|.
name|setToken
argument_list|(
name|token
argument_list|)
expr_stmt|;
name|securityToken
operator|.
name|setPrincipal
argument_list|(
name|principal
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|props
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|securityToken
operator|.
name|setProperties
argument_list|(
name|props
argument_list|)
expr_stmt|;
if|if
condition|(
name|realm
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|STSConstants
operator|.
name|TOKEN_REALM
argument_list|,
name|realm
argument_list|)
expr_stmt|;
block|}
comment|// Handle Renewing logic
if|if
condition|(
name|renewing
operator|!=
literal|null
condition|)
block|{
name|props
operator|.
name|put
argument_list|(
name|STSConstants
operator|.
name|TOKEN_RENEWING_ALLOW
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|renewing
operator|.
name|isAllowRenewing
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|STSConstants
operator|.
name|TOKEN_RENEWING_ALLOW_AFTER_EXPIRY
argument_list|,
name|String
operator|.
name|valueOf
argument_list|(
name|renewing
operator|.
name|isAllowRenewingAfterExpiry
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|props
operator|.
name|put
argument_list|(
name|STSConstants
operator|.
name|TOKEN_RENEWING_ALLOW
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|props
operator|.
name|put
argument_list|(
name|STSConstants
operator|.
name|TOKEN_RENEWING_ALLOW_AFTER_EXPIRY
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
block|}
return|return
name|securityToken
return|;
block|}
specifier|public
specifier|static
name|void
name|storeTokenInCache
parameter_list|(
name|SecurityToken
name|securityToken
parameter_list|,
name|TokenStore
name|cache
parameter_list|,
name|byte
index|[]
name|signatureValue
parameter_list|)
block|{
name|int
name|hash
init|=
name|Arrays
operator|.
name|hashCode
argument_list|(
name|signatureValue
argument_list|)
decl_stmt|;
name|securityToken
operator|.
name|setTokenHash
argument_list|(
name|hash
argument_list|)
expr_stmt|;
name|String
name|identifier
init|=
name|Integer
operator|.
name|toString
argument_list|(
name|hash
argument_list|)
decl_stmt|;
name|cache
operator|.
name|add
argument_list|(
name|identifier
argument_list|,
name|securityToken
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

