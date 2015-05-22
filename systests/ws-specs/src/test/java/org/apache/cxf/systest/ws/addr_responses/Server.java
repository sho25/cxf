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
name|addr_responses
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
name|BusFactory
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
name|jaxws
operator|.
name|EndpointImpl
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
name|testutil
operator|.
name|common
operator|.
name|AbstractBusTestServerBase
import|;
end_import

begin_class
specifier|public
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
name|EndpointImpl
name|ep
decl_stmt|;
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|HelloImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/wsa/responses"
decl_stmt|;
name|ep
operator|=
operator|new
name|EndpointImpl
argument_list|(
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
argument_list|,
name|implementor
argument_list|,
literal|null
argument_list|,
name|getWsdl
argument_list|()
argument_list|)
expr_stmt|;
name|ep
operator|.
name|publish
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|ep
operator|!=
literal|null
condition|)
block|{
name|ep
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
name|ep
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
specifier|static
name|void
name|main
parameter_list|(
name|String
index|[]
name|args
parameter_list|)
block|{
try|try
block|{
name|Server
name|s
init|=
operator|new
name|Server
argument_list|()
decl_stmt|;
name|s
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|ex
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
name|System
operator|.
name|exit
argument_list|(
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"done!"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getWsdl
parameter_list|()
block|{
try|try
block|{
name|java
operator|.
name|net
operator|.
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/wsdl_systest_responses/responses.wsdl"
argument_list|)
decl_stmt|;
return|return
name|wsdl
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

