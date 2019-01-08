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
name|binding
operator|.
name|corba
package|;
end_package

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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|interceptor
operator|.
name|Interceptor
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
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|assertNotNull
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaBindingTest
block|{
specifier|private
name|ORB
name|orb
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|java
operator|.
name|util
operator|.
name|Properties
name|props
init|=
name|System
operator|.
name|getProperties
argument_list|()
decl_stmt|;
name|props
operator|.
name|put
argument_list|(
literal|"yoko.orb.id"
argument_list|,
literal|"CXF-CORBA-Server-Binding"
argument_list|)
expr_stmt|;
name|orb
operator|=
name|ORB
operator|.
name|init
argument_list|(
operator|new
name|String
index|[
literal|0
index|]
argument_list|,
name|props
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
if|if
condition|(
name|orb
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|orb
operator|.
name|destroy
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
comment|// Do nothing.  Throw an Exception?
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCorbaBinding
parameter_list|()
block|{
name|CorbaBinding
name|binding
init|=
operator|new
name|CorbaBinding
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|in
init|=
name|binding
operator|.
name|getInInterceptors
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|out
init|=
name|binding
operator|.
name|getOutInterceptors
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|out
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|infault
init|=
name|binding
operator|.
name|getInFaultInterceptors
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|infault
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
name|outfault
init|=
name|binding
operator|.
name|getFaultOutInterceptors
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|outfault
argument_list|)
expr_stmt|;
name|Message
name|message
init|=
name|binding
operator|.
name|createMessage
argument_list|()
decl_stmt|;
name|message
operator|.
name|put
argument_list|(
name|ORB
operator|.
name|class
argument_list|,
name|orb
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|ORB
name|corbaORB
init|=
name|message
operator|.
name|get
argument_list|(
name|ORB
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|corbaORB
argument_list|)
expr_stmt|;
name|MessageImpl
name|mesage
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|mesage
operator|.
name|put
argument_list|(
name|ORB
operator|.
name|class
argument_list|,
name|orb
argument_list|)
expr_stmt|;
name|Message
name|msg
init|=
name|binding
operator|.
name|createMessage
argument_list|(
name|mesage
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|msg
argument_list|)
expr_stmt|;
name|ORB
name|corbaOrb
init|=
name|msg
operator|.
name|get
argument_list|(
name|ORB
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|corbaOrb
argument_list|)
expr_stmt|;
comment|/*List<Interceptor> infault = binding.getInFaultInterceptors();         assertEquals(1, infault.size());         List<Interceptor> outfault = binding.getOutFaultInterceptors();         assertEquals(1, fault.size());*/
block|}
block|}
end_class

end_unit

