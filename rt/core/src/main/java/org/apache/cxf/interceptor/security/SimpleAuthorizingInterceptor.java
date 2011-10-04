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
name|interceptor
operator|.
name|security
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
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
name|List
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
name|security
operator|.
name|SecurityContext
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleAuthorizingInterceptor
extends|extends
name|AbstractAuthorizingInInterceptor
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|methodRolesMap
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|userRolesMap
init|=
name|Collections
operator|.
name|emptyMap
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|globalRoles
init|=
name|Collections
operator|.
name|emptyList
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|boolean
name|isUserInRole
parameter_list|(
name|SecurityContext
name|sc
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|roles
parameter_list|,
name|boolean
name|deny
parameter_list|)
block|{
if|if
condition|(
operator|!
name|super
operator|.
name|isUserInRole
argument_list|(
name|sc
argument_list|,
name|roles
argument_list|,
name|deny
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
comment|// Additional check.
if|if
condition|(
operator|!
name|userRolesMap
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|userRoles
init|=
name|userRolesMap
operator|.
name|get
argument_list|(
name|sc
operator|.
name|getUserPrincipal
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|userRoles
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
for|for
control|(
name|String
name|role
range|:
name|roles
control|)
block|{
if|if
condition|(
name|userRoles
operator|.
name|contains
argument_list|(
name|role
argument_list|)
condition|)
block|{
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
else|else
block|{
return|return
literal|true
return|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getExpectedRoles
parameter_list|(
name|Method
name|method
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|roles
init|=
name|methodRolesMap
operator|.
name|get
argument_list|(
name|method
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|roles
operator|!=
literal|null
condition|)
block|{
return|return
name|roles
return|;
block|}
return|return
name|globalRoles
return|;
block|}
specifier|public
name|void
name|setMethodRolesMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rolesMap
parameter_list|)
block|{
name|methodRolesMap
operator|.
name|putAll
argument_list|(
name|parseRolesMap
argument_list|(
name|rolesMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setUserRolesMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rolesMap
parameter_list|)
block|{
name|userRolesMap
operator|=
name|parseRolesMap
argument_list|(
name|rolesMap
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setGlobalRoles
parameter_list|(
name|String
name|roles
parameter_list|)
block|{
name|globalRoles
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|roles
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|parseRolesMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|rolesMap
parameter_list|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
name|map
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|rolesMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|map
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
operator|.
name|split
argument_list|(
literal|" "
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|map
return|;
block|}
block|}
end_class

end_unit

