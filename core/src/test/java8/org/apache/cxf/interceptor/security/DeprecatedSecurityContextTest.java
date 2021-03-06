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
name|interceptor
operator|.
name|security
operator|.
name|test
operator|.
name|GroupWrapper
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
name|Test
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|DeprecatedSecurityContextTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testPrivateStaticGroup
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
comment|//create a friend group and add Barry to this group
name|GroupWrapper
name|test
init|=
operator|new
name|GroupWrapper
argument_list|(
literal|"friend"
argument_list|,
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
name|test
operator|.
name|getGroup
argument_list|()
argument_list|)
expr_stmt|;
name|LoginSecurityContext
name|context
init|=
operator|new
name|DefaultSecurityContext
argument_list|(
name|p
argument_list|,
name|s
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|context
operator|.
name|isUserInRole
argument_list|(
literal|"Barry"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

