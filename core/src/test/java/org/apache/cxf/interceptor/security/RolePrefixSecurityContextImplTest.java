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

begin_import
import|import
name|javax
operator|.
name|security
operator|.
name|auth
operator|.
name|Subject
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
name|SimplePrincipal
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
name|LoginSecurityContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|RolePrefixSecurityContextImplTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testUserNotInRole
parameter_list|()
block|{
name|Subject
name|s
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|Principal
name|p
init|=
operator|new
name|SimplePrincipal
argument_list|(
literal|"Barry"
argument_list|)
decl_stmt|;
name|s
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
operator|new
name|RolePrefixSecurityContextImpl
argument_list|(
name|s
argument_list|,
literal|""
argument_list|)
operator|.
name|isUserInRole
argument_list|(
literal|"friend"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUserInRole
parameter_list|()
block|{
name|Subject
name|s
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|Principal
name|p
init|=
operator|new
name|SimplePrincipal
argument_list|(
literal|"Barry"
argument_list|)
decl_stmt|;
name|s
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|s
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|SimplePrincipal
argument_list|(
literal|"role_friend"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|RolePrefixSecurityContextImpl
argument_list|(
name|s
argument_list|,
literal|"role_"
argument_list|)
operator|.
name|isUserInRole
argument_list|(
literal|"role_friend"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testUserInRoleWithRolePrincipal
parameter_list|()
block|{
name|Subject
name|s
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|Principal
name|p
init|=
operator|new
name|SimplePrincipal
argument_list|(
literal|"Barry"
argument_list|)
decl_stmt|;
name|s
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|s
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|RolePrincipal
argument_list|(
literal|"friend"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|new
name|RolePrefixSecurityContextImpl
argument_list|(
name|s
argument_list|,
literal|"RolePrincipal"
argument_list|,
literal|"classname"
argument_list|)
operator|.
name|isUserInRole
argument_list|(
literal|"friend"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMultipleRoles
parameter_list|()
block|{
name|Subject
name|s
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|Principal
name|p
init|=
operator|new
name|SimplePrincipal
argument_list|(
literal|"Barry"
argument_list|)
decl_stmt|;
name|s
operator|.
name|getPrincipals
argument_list|()
operator|.
name|add
argument_list|(
name|p
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
name|roles
operator|.
name|add
argument_list|(
operator|new
name|SimplePrincipal
argument_list|(
literal|"role_friend"
argument_list|)
argument_list|)
expr_stmt|;
name|roles
operator|.
name|add
argument_list|(
operator|new
name|SimplePrincipal
argument_list|(
literal|"role_admin"
argument_list|)
argument_list|)
expr_stmt|;
name|s
operator|.
name|getPrincipals
argument_list|()
operator|.
name|addAll
argument_list|(
name|roles
argument_list|)
expr_stmt|;
name|LoginSecurityContext
name|context
init|=
operator|new
name|RolePrefixSecurityContextImpl
argument_list|(
name|s
argument_list|,
literal|"role_"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|isUserInRole
argument_list|(
literal|"role_friend"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|isUserInRole
argument_list|(
literal|"role_admin"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|context
operator|.
name|isUserInRole
argument_list|(
literal|"role_bar"
argument_list|)
argument_list|)
expr_stmt|;
name|Set
argument_list|<
name|Principal
argument_list|>
name|roles2
init|=
name|context
operator|.
name|getUserRoles
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|roles2
argument_list|,
name|roles
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetSubject
parameter_list|()
block|{
name|Subject
name|s
init|=
operator|new
name|Subject
argument_list|()
decl_stmt|;
name|assertSame
argument_list|(
operator|new
name|RolePrefixSecurityContextImpl
argument_list|(
name|s
argument_list|,
literal|""
argument_list|)
operator|.
name|getSubject
argument_list|()
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|RolePrincipal
implements|implements
name|Principal
block|{
specifier|private
name|String
name|roleName
decl_stmt|;
name|RolePrincipal
parameter_list|(
name|String
name|roleName
parameter_list|)
block|{
name|this
operator|.
name|roleName
operator|=
name|roleName
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|roleName
return|;
block|}
block|}
block|}
end_class

end_unit

