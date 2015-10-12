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
name|java
operator|.
name|io
operator|.
name|IOException
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
name|Collection
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
name|Server12
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
name|Server12Restricted
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
name|StaxServer12
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
name|StaxServer12Restricted
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|Parameters
import|;
end_import

begin_comment
comment|/**  * This class runs the second half of the tests, as having all in   * the one class causes an out of memory problem in eclipse  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|Parameterized
operator|.
name|class
argument_list|)
specifier|public
class|class
name|WSSecurity112Test
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
name|SecurityTestUtil
operator|.
name|checkUnrestrictedPoliciesInstalled
argument_list|()
expr_stmt|;
block|}
empty_stmt|;
specifier|final
name|TestParam
name|test
decl_stmt|;
specifier|public
name|WSSecurity112Test
parameter_list|(
name|TestParam
name|type
parameter_list|)
block|{
name|this
operator|.
name|test
operator|=
name|type
expr_stmt|;
block|}
specifier|static
class|class
name|TestParam
block|{
specifier|final
name|String
name|prefix
decl_stmt|;
specifier|final
name|boolean
name|streaming
decl_stmt|;
specifier|final
name|String
name|port
decl_stmt|;
name|TestParam
parameter_list|(
name|String
name|p
parameter_list|,
name|String
name|port
parameter_list|,
name|boolean
name|b
parameter_list|)
block|{
name|prefix
operator|=
name|p
expr_stmt|;
name|this
operator|.
name|port
operator|=
name|port
expr_stmt|;
name|streaming
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|prefix
operator|+
literal|":"
operator|+
name|port
operator|+
literal|":"
operator|+
operator|(
name|streaming
condition|?
literal|"streaming"
else|:
literal|"dom"
operator|)
return|;
block|}
block|}
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
name|Server12
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|StaxServer12
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
name|Server12Restricted
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Server failed to launch"
argument_list|,
comment|// run the server in the same process
comment|// set this to false to fork
name|launchServer
argument_list|(
name|StaxServer12Restricted
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
name|Parameters
argument_list|(
name|name
operator|=
literal|"{0}"
argument_list|)
specifier|public
specifier|static
name|Collection
argument_list|<
name|TestParam
index|[]
argument_list|>
name|data
parameter_list|()
block|{
if|if
condition|(
name|unrestrictedPoliciesInstalled
condition|)
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|TestParam
index|[]
index|[]
block|{
block|{
operator|new
name|TestParam
argument_list|(
literal|"X"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-NoTimestamp"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-AES128"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-AES256"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-TripleDES"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-ES"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-SEES"
argument_list|,
name|Server12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-NoTimestamp"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-AES128"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-AES256"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-TripleDES"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-ES"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-SEES"
argument_list|,
name|StaxServer12
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,             }
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|TestParam
index|[]
index|[]
block|{
block|{
operator|new
name|TestParam
argument_list|(
literal|"X"
argument_list|,
name|Server12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-NoTimestamp"
argument_list|,
name|Server12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD"
argument_list|,
name|Server12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-ES"
argument_list|,
name|Server12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-SEES"
argument_list|,
name|Server12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X"
argument_list|,
name|StaxServer12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"X-NoTimestamp"
argument_list|,
name|StaxServer12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD"
argument_list|,
name|StaxServer12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-ES"
argument_list|,
name|StaxServer12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,
block|{
operator|new
name|TestParam
argument_list|(
literal|"XD-SEES"
argument_list|,
name|StaxServer12Restricted
operator|.
name|PORT
argument_list|,
literal|false
argument_list|)
block|}
block|,             }
argument_list|)
return|;
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
throws|throws
name|IOException
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
name|runClientServer
argument_list|(
name|test
operator|.
name|prefix
argument_list|,
name|test
operator|.
name|port
argument_list|,
name|unrestrictedPoliciesInstalled
argument_list|,
name|test
operator|.
name|streaming
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

