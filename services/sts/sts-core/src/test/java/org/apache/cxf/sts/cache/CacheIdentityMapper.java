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
name|apache
operator|.
name|cxf
operator|.
name|sts
operator|.
name|IdentityMapper
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
name|CustomTokenPrincipal
import|;
end_import

begin_class
specifier|public
class|class
name|CacheIdentityMapper
implements|implements
name|IdentityMapper
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
name|mappingTable
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|>
argument_list|()
argument_list|)
decl_stmt|;
name|CacheIdentityMapper
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|identities
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|identities
operator|.
name|put
argument_list|(
literal|"REALM_A"
argument_list|,
literal|"user_aaa"
argument_list|)
expr_stmt|;
name|identities
operator|.
name|put
argument_list|(
literal|"REALM_B"
argument_list|,
literal|"user_bbb"
argument_list|)
expr_stmt|;
name|identities
operator|.
name|put
argument_list|(
literal|"REALM_C"
argument_list|,
literal|"user_ccc"
argument_list|)
expr_stmt|;
name|identities
operator|.
name|put
argument_list|(
literal|"REALM_D"
argument_list|,
literal|"user_ddd"
argument_list|)
expr_stmt|;
name|identities
operator|.
name|put
argument_list|(
literal|"REALM_E"
argument_list|,
literal|"user_eee"
argument_list|)
expr_stmt|;
name|mappingTable
operator|.
name|put
argument_list|(
literal|"user_aaa@REALM_A"
argument_list|,
name|identities
argument_list|)
expr_stmt|;
name|mappingTable
operator|.
name|put
argument_list|(
literal|"user_bbb@REALM_B"
argument_list|,
name|identities
argument_list|)
expr_stmt|;
name|mappingTable
operator|.
name|put
argument_list|(
literal|"user_ccc@REALM_C"
argument_list|,
name|identities
argument_list|)
expr_stmt|;
name|mappingTable
operator|.
name|put
argument_list|(
literal|"user_ddd@REALM_D"
argument_list|,
name|identities
argument_list|)
expr_stmt|;
name|mappingTable
operator|.
name|put
argument_list|(
literal|"user_eee@REALM_E"
argument_list|,
name|identities
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Principal
name|mapPrincipal
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|Principal
name|sourcePrincipal
parameter_list|,
name|String
name|targetRealm
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|identities
init|=
name|mappingTable
operator|.
name|get
argument_list|(
name|sourcePrincipal
operator|.
name|getName
argument_list|()
operator|+
literal|"@"
operator|+
name|sourceRealm
argument_list|)
decl_stmt|;
if|if
condition|(
name|identities
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|String
name|targetUser
init|=
name|identities
operator|.
name|get
argument_list|(
name|targetRealm
argument_list|)
decl_stmt|;
if|if
condition|(
name|targetUser
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|CustomTokenPrincipal
argument_list|(
name|targetUser
argument_list|)
return|;
block|}
block|}
end_class

end_unit

