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
name|http
operator|.
name|auth
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|configuration
operator|.
name|security
operator|.
name|AuthorizationPolicy
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
name|endpoint
operator|.
name|Client
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
name|frontend
operator|.
name|ClientProxy
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
name|AbstractBusClientServerTestBase
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
name|transport
operator|.
name|http
operator|.
name|HTTPConduit
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
name|transport
operator|.
name|http
operator|.
name|HTTPException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|Greeter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hello_world
operator|.
name|services
operator|.
name|SOAPService
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

begin_class
specifier|public
class|class
name|DigestAuthTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|allocatePort
argument_list|(
name|DigestServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|serviceName
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"SOAPService"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|QName
name|mortimerQ
init|=
operator|new
name|QName
argument_list|(
literal|"http://apache.org/hello_world"
argument_list|,
literal|"Mortimer"
argument_list|)
decl_stmt|;
specifier|public
name|DigestAuthTest
parameter_list|()
block|{     }
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|startServer
parameter_list|()
block|{
name|launchServer
argument_list|(
name|DigestServer
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDigestAuth
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"../resources/greeting.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|mortimer
init|=
name|service
operator|.
name|getPort
argument_list|(
name|mortimerQ
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|mortimer
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|mortimer
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/digestauth/greeter"
argument_list|)
expr_stmt|;
name|Client
name|client
init|=
name|ClientProxy
operator|.
name|getClient
argument_list|(
name|mortimer
argument_list|)
decl_stmt|;
name|HTTPConduit
name|http
init|=
operator|(
name|HTTPConduit
operator|)
name|client
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|AuthorizationPolicy
name|authPolicy
init|=
operator|new
name|AuthorizationPolicy
argument_list|()
decl_stmt|;
name|authPolicy
operator|.
name|setAuthorizationType
argument_list|(
literal|"Digest"
argument_list|)
expr_stmt|;
name|authPolicy
operator|.
name|setUserName
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|authPolicy
operator|.
name|setPassword
argument_list|(
literal|"bar"
argument_list|)
expr_stmt|;
name|http
operator|.
name|setAuthorization
argument_list|(
name|authPolicy
argument_list|)
expr_stmt|;
name|String
name|answer
init|=
name|mortimer
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected answer: "
operator|+
name|answer
argument_list|,
literal|"Hi"
argument_list|,
name|answer
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoAuth
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|wsdl
init|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"../resources/greeting.wsdl"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"WSDL is null"
argument_list|,
name|wsdl
argument_list|)
expr_stmt|;
name|SOAPService
name|service
init|=
operator|new
name|SOAPService
argument_list|(
name|wsdl
argument_list|,
name|serviceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Service is null"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|Greeter
name|mortimer
init|=
name|service
operator|.
name|getPort
argument_list|(
name|mortimerQ
argument_list|,
name|Greeter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Port is null"
argument_list|,
name|mortimer
argument_list|)
expr_stmt|;
name|setAddress
argument_list|(
name|mortimer
argument_list|,
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/digestauth/greeter"
argument_list|)
expr_stmt|;
try|try
block|{
name|String
name|answer
init|=
name|mortimer
operator|.
name|sayHi
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|fail
argument_list|(
literal|"Unexpected reply ("
operator|+
name|answer
operator|+
literal|"). Should throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|Throwable
name|cause
init|=
name|e
operator|.
name|getCause
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|HTTPException
operator|.
name|class
argument_list|,
name|cause
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
name|HTTPException
name|he
init|=
operator|(
name|HTTPException
operator|)
name|cause
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|401
argument_list|,
name|he
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

