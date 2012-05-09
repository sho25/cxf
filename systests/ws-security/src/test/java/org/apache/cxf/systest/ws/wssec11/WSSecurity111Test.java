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
name|systest
operator|.
name|ws
operator|.
name|wssec11
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|ws
operator|.
name|common
operator|.
name|SecurityTestUtil
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
name|systest
operator|.
name|ws
operator|.
name|wssec11
operator|.
name|server
operator|.
name|Server11
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
name|systest
operator|.
name|ws
operator|.
name|wssec11
operator|.
name|server
operator|.
name|Server11Restricted
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
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

begin_comment
comment|/**  * This class runs the first half of the tests, as having all in   * the one class causes an out of memory problem in eclipse  */
end_comment

begin_class
specifier|public
class|class
name|WSSecurity111Test
extends|extends
name|WSSecurity11Common
block|{
specifier|private
specifier|static
name|boolean
name|unrestrictedPoliciesInstalled
decl_stmt|;
static|static
block|{
name|unrestrictedPoliciesInstalled
operator|=
name|checkUnrestrictedPoliciesInstalled
argument_list|()
expr_stmt|;
block|}
empty_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServers
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|unrestrictedPoliciesInstalled
condition|)
block|{
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|Server11
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
if|if
condition|(
name|WSSecurity11Common
operator|.
name|isIBMJDK16
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Not running as there is a problem with 1.6 jdk and restricted jars"
argument_list|)
expr_stmt|;
return|return;
block|}
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|Server11Restricted
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|org
operator|.
name|junit
operator|.
name|AfterClass
specifier|public
specifier|static
name|void
name|cleanup
parameter_list|()
throws|throws
name|Exception
block|{
name|SecurityTestUtil
operator|.
name|cleanup
argument_list|()
expr_stmt|;
name|stopAllServers
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testClientServer
parameter_list|()
block|{
if|if
condition|(
operator|(
operator|!
name|unrestrictedPoliciesInstalled
operator|)
operator|&&
operator|(
name|WSSecurity11Common
operator|.
name|isIBMJDK16
argument_list|()
operator|)
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Not running as there is a problem with 1.6 jdk and restricted jars"
argument_list|)
expr_stmt|;
return|return;
block|}
name|String
index|[]
name|argv
init|=
operator|new
name|String
index|[]
block|{
literal|"A"
block|,
literal|"A-NoTimestamp"
block|,
literal|"AD"
block|,
literal|"A-ES"
block|,
literal|"AD-ES"
block|,
literal|"UX"
block|,
literal|"UX-NoTimestamp"
block|,
literal|"UXD"
block|,
literal|"UX-SEES"
block|,
literal|"UXD-SEES"
block|,          }
decl_stmt|;
name|runClientServer
argument_list|(
name|argv
argument_list|,
name|unrestrictedPoliciesInstalled
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

