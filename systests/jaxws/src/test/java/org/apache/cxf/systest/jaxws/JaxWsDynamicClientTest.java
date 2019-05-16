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
name|jaxws
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
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
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
name|endpoint
operator|.
name|ClientCallback
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
name|ext
operator|.
name|logging
operator|.
name|LoggingInInterceptor
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
name|ext
operator|.
name|logging
operator|.
name|LoggingOutInterceptor
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
name|helpers
operator|.
name|IOUtils
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
name|endpoint
operator|.
name|dynamic
operator|.
name|JaxWsDynamicClientFactory
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
name|no_body_parts
operator|.
name|types
operator|.
name|Operation1
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
name|no_body_parts
operator|.
name|types
operator|.
name|Operation1Response
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
name|testutil
operator|.
name|common
operator|.
name|TestUtil
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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
name|JaxWsDynamicClientTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|static
specifier|final
name|String
name|PORT
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|ServerNoBodyParts
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|String
name|PORT1
init|=
name|TestUtil
operator|.
name|getPortNumber
argument_list|(
name|ArrayServiceServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|md5
parameter_list|(
name|byte
index|[]
name|bytes
parameter_list|)
block|{
name|MessageDigest
name|algorithm
decl_stmt|;
try|try
block|{
name|algorithm
operator|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
literal|"MD5"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|algorithm
operator|.
name|reset
argument_list|()
expr_stmt|;
name|algorithm
operator|.
name|update
argument_list|(
name|bytes
argument_list|)
expr_stmt|;
name|byte
index|[]
name|messageDigest
init|=
name|algorithm
operator|.
name|digest
argument_list|()
decl_stmt|;
name|StringBuilder
name|hexString
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|messageDigest
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|hexString
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toHexString
argument_list|(
literal|0xFF
operator|&
name|messageDigest
index|[
name|i
index|]
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|hexString
operator|.
name|toString
argument_list|()
return|;
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
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|ServerNoBodyParts
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"server did not launch correctly"
argument_list|,
name|launchServer
argument_list|(
name|ArrayServiceServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvocation
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsDynamicClientFactory
name|dcf
init|=
name|JaxWsDynamicClientFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|URL
name|wsdlURL
init|=
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/NoBodyParts/NoBodyPartsService?wsdl"
argument_list|)
decl_stmt|;
name|Client
name|client
init|=
name|dcf
operator|.
name|createClient
argument_list|(
name|wsdlURL
argument_list|)
decl_stmt|;
name|byte
index|[]
name|bucketOfBytes
init|=
name|IOUtils
operator|.
name|readBytesFromStream
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/wsdl/no_body_parts.wsdl"
argument_list|)
argument_list|)
decl_stmt|;
name|Operation1
name|parameters
init|=
operator|new
name|Operation1
argument_list|()
decl_stmt|;
name|parameters
operator|.
name|setOptionString
argument_list|(
literal|"opt-ion"
argument_list|)
expr_stmt|;
name|parameters
operator|.
name|setTargetType
argument_list|(
literal|"tar-get"
argument_list|)
expr_stmt|;
name|Object
index|[]
name|rparts
init|=
name|client
operator|.
name|invoke
argument_list|(
literal|"operation1"
argument_list|,
name|parameters
argument_list|,
name|bucketOfBytes
argument_list|)
decl_stmt|;
name|Operation1Response
name|r
init|=
operator|(
name|Operation1Response
operator|)
name|rparts
index|[
literal|0
index|]
decl_stmt|;
name|assertEquals
argument_list|(
name|md5
argument_list|(
name|bucketOfBytes
argument_list|)
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
name|ClientCallback
name|callback
init|=
operator|new
name|ClientCallback
argument_list|()
decl_stmt|;
name|client
operator|.
name|invoke
argument_list|(
name|callback
argument_list|,
literal|"operation1"
argument_list|,
name|parameters
argument_list|,
name|bucketOfBytes
argument_list|)
expr_stmt|;
name|rparts
operator|=
name|callback
operator|.
name|get
argument_list|()
expr_stmt|;
name|r
operator|=
operator|(
name|Operation1Response
operator|)
name|rparts
index|[
literal|0
index|]
expr_stmt|;
name|assertEquals
argument_list|(
name|md5
argument_list|(
name|bucketOfBytes
argument_list|)
argument_list|,
name|r
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArrayList
parameter_list|()
throws|throws
name|Exception
block|{
name|JaxWsDynamicClientFactory
name|dcf
init|=
name|JaxWsDynamicClientFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|dcf
operator|.
name|createClient
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/ArrayService?wsdl"
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
name|values
init|=
operator|new
name|String
index|[]
block|{
literal|"foobar"
block|,
literal|"something"
block|}
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|invoke
argument_list|(
literal|"init"
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArgfiles
parameter_list|()
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.cxf.common.util.Compiler-fork"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|JaxWsDynamicClientFactory
name|dcf
init|=
name|JaxWsDynamicClientFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|Client
name|client
init|=
name|dcf
operator|.
name|createClient
argument_list|(
operator|new
name|URL
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT1
operator|+
literal|"/ArrayService?wsdl"
argument_list|)
argument_list|)
decl_stmt|;
name|String
index|[]
name|values
init|=
operator|new
name|String
index|[]
block|{
literal|"foobar"
block|,
literal|"something"
block|}
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|list
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|values
argument_list|)
decl_stmt|;
name|client
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingOutInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|getInInterceptors
argument_list|()
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|client
operator|.
name|invoke
argument_list|(
literal|"init"
argument_list|,
name|list
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

