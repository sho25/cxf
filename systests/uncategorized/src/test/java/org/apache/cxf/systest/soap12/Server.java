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
name|soap12
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Endpoint
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|wsdl
operator|.
name|interceptors
operator|.
name|URIMappingInterceptor
import|;
end_import

begin_class
specifier|public
class|class
name|Server
extends|extends
name|AbstractBusTestServerBase
block|{
specifier|public
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
name|Endpoint
name|ep
decl_stmt|;
name|Endpoint
name|ep11
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"deprecation"
argument_list|)
specifier|protected
name|void
name|run
parameter_list|()
block|{
name|Object
name|implementor
init|=
operator|new
name|GreeterImpl
argument_list|()
decl_stmt|;
name|String
name|address
init|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/SoapPort"
decl_stmt|;
name|ep
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
argument_list|)
expr_stmt|;
name|EndpointImpl
name|epi
init|=
operator|(
name|EndpointImpl
operator|)
name|ep
decl_stmt|;
name|epi
operator|.
name|getService
argument_list|()
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|URIMappingInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|implementor
operator|=
operator|new
name|org
operator|.
name|apache
operator|.
name|hello_world_soap_http
operator|.
name|GreeterImpl
argument_list|()
expr_stmt|;
name|address
operator|=
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/SoapContext/Soap11Port"
expr_stmt|;
name|ep11
operator|=
name|Endpoint
operator|.
name|publish
argument_list|(
name|address
argument_list|,
name|implementor
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
name|ep
operator|.
name|stop
argument_list|()
expr_stmt|;
name|ep11
operator|.
name|stop
argument_list|()
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
block|}
end_class

end_unit

