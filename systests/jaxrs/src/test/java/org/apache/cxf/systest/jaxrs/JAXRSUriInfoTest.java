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
name|jaxrs
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|UriInfo
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
name|jaxrs
operator|.
name|client
operator|.
name|WebClient
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
name|AbstractClientServerTestBase
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
name|Ignore
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
name|JAXRSUriInfoTest
extends|extends
name|AbstractClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|SpringServer
operator|.
name|PORT
decl_stmt|;
annotation|@
name|BeforeClass
specifier|public
specifier|static
name|void
name|beforeClass
parameter_list|()
throws|throws
name|Exception
block|{
comment|// must be 'in-process' to communicate with inner class in single JVM
comment|// and to spawn class SpringServer w/o using main() method
name|launchServer
argument_list|(
name|SpringServer
operator|.
name|class
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
specifier|public
specifier|static
class|class
name|SpringServer
extends|extends
name|AbstractSpringServer
block|{
specifier|public
specifier|static
specifier|final
name|int
name|PORT
init|=
name|allocatePortAsInt
argument_list|(
name|SpringServer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|public
name|SpringServer
parameter_list|()
block|{
name|super
argument_list|(
literal|"/jaxrs_uriinfo"
argument_list|,
literal|"/app"
argument_list|,
name|PORT
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * URI          | getBaseUri          | path param -------------+---------------------+----------- /app/v1      | http://host/        | "v1" /app/v1/     | http://host/        | "v1/" /app/v1/test | http://host/app/v1/ | "test" /app/v1/     | http://host/app/v1/ | "" /app/v1      | http://host/app/v1/ | "app/v1"       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testBasePathAndPathAndPathParam
parameter_list|()
throws|throws
name|Exception
block|{
name|checkUriInfo
argument_list|(
literal|"http://127.0.0.1:"
operator|+
name|PORT
operator|+
literal|"/app/v1"
argument_list|,
literal|"\"\""
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1"
argument_list|,
literal|"\"\""
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/"
argument_list|,
literal|"\"\""
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/test"
argument_list|,
literal|"\"test\""
argument_list|,
literal|"test"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/"
argument_list|,
literal|"\"\""
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1"
argument_list|,
literal|"\"\""
argument_list|,
literal|"/"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/bar"
argument_list|,
literal|"\"bar\""
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/bar"
argument_list|,
literal|"\"bar\""
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/bar/test"
argument_list|,
literal|"\"bar/test\""
argument_list|,
literal|"bar/test"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/bar"
argument_list|,
literal|"\"bar\""
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|checkUriInfo
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/bar"
argument_list|,
literal|"\"bar\""
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|checkUriInfo
parameter_list|(
name|String
name|address
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|pathParam
parameter_list|)
block|{
name|WebClient
name|wc
init|=
name|WebClient
operator|.
name|create
argument_list|(
name|address
argument_list|)
decl_stmt|;
name|wc
operator|.
name|accept
argument_list|(
literal|"text/plain"
argument_list|)
expr_stmt|;
name|String
name|data
init|=
name|wc
operator|.
name|get
argument_list|(
name|String
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/app/v1/,"
operator|+
name|path
operator|+
literal|","
operator|+
name|pathParam
argument_list|,
name|data
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Ignore
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
specifier|static
class|class
name|Resource
block|{
annotation|@
name|Context
specifier|private
name|UriInfo
name|uriInfo
decl_stmt|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/{path:.*}"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"text/plain"
argument_list|)
specifier|public
name|String
name|getBasePathAndPathParam
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"path"
argument_list|)
name|String
name|path
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|uriInfo
operator|.
name|getBaseUri
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|",\""
operator|+
name|path
operator|+
literal|"\""
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|","
operator|+
name|uriInfo
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

