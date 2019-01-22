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
name|osgi
operator|.
name|itests
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
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
name|client
operator|.
name|ClientBuilder
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
name|client
operator|.
name|Entity
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
name|client
operator|.
name|WebTarget
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
name|Response
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
name|Response
operator|.
name|Status
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
name|osgi
operator|.
name|itests
operator|.
name|AbstractServerActivator
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
name|osgi
operator|.
name|itests
operator|.
name|CXFOSGiTestSupport
import|;
end_import

begin_import
import|import
name|org
operator|.
name|osgi
operator|.
name|framework
operator|.
name|Constants
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
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|Option
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|junit
operator|.
name|PaxExam
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|LogLevelOption
operator|.
name|LogLevel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|ExamReactorStrategy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|spi
operator|.
name|reactors
operator|.
name|PerClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|tinybundles
operator|.
name|core
operator|.
name|TinyBundles
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
name|assertNotNull
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|CoreOptions
operator|.
name|provision
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|features
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|ops4j
operator|.
name|pax
operator|.
name|exam
operator|.
name|karaf
operator|.
name|options
operator|.
name|KarafDistributionOption
operator|.
name|logLevel
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|PaxExam
operator|.
name|class
argument_list|)
annotation|@
name|ExamReactorStrategy
argument_list|(
name|PerClass
operator|.
name|class
argument_list|)
specifier|public
class|class
name|JaxRsServiceTest
extends|extends
name|CXFOSGiTestSupport
block|{
specifier|private
specifier|static
specifier|final
name|String
name|BASE_URL
init|=
literal|"http://localhost:8181/cxf/jaxrs/bookstore"
decl_stmt|;
specifier|private
specifier|final
name|WebTarget
name|wt
init|=
name|ClientBuilder
operator|.
name|newClient
argument_list|()
operator|.
name|target
argument_list|(
name|BASE_URL
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testJaxRsGet
parameter_list|()
throws|throws
name|Exception
block|{
name|Book
name|book
init|=
name|wt
operator|.
name|path
argument_list|(
literal|"/books/123"
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|get
argument_list|(
name|Book
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|book
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxRsPost
parameter_list|()
throws|throws
name|Exception
block|{
name|Book
name|book
init|=
operator|new
name|Book
argument_list|(
literal|"New Book"
argument_list|,
literal|321
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|wt
operator|.
name|path
argument_list|(
literal|"/books/"
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|post
argument_list|(
name|Entity
operator|.
name|xml
argument_list|(
name|book
argument_list|)
argument_list|)
decl_stmt|;
name|assertStatus
argument_list|(
name|Status
operator|.
name|CREATED
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|postWithValidationError
parameter_list|()
throws|throws
name|Exception
block|{
name|Book
name|book
init|=
operator|new
name|Book
argument_list|(
literal|null
argument_list|,
operator|-
literal|1
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|wt
operator|.
name|path
argument_list|(
literal|"/books-validate/"
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|post
argument_list|(
name|Entity
operator|.
name|xml
argument_list|(
name|book
argument_list|)
argument_list|)
decl_stmt|;
name|assertStatus
argument_list|(
name|Status
operator|.
name|BAD_REQUEST
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|postWithValidation
parameter_list|()
throws|throws
name|Exception
block|{
name|Book
name|book
init|=
operator|new
name|Book
argument_list|(
literal|"A Book"
argument_list|,
literal|3212
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|wt
operator|.
name|path
argument_list|(
literal|"/books-validate/"
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|post
argument_list|(
name|Entity
operator|.
name|xml
argument_list|(
name|book
argument_list|)
argument_list|)
decl_stmt|;
name|assertStatus
argument_list|(
name|Status
operator|.
name|CREATED
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|response
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxRsDelete
parameter_list|()
throws|throws
name|Exception
block|{
name|Response
name|response
init|=
name|wt
operator|.
name|path
argument_list|(
literal|"/books/123"
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|delete
argument_list|()
decl_stmt|;
name|assertStatus
argument_list|(
name|Status
operator|.
name|OK
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testJaxRsPut
parameter_list|()
throws|throws
name|Exception
block|{
name|Book
name|book
init|=
operator|new
name|Book
argument_list|(
literal|"Updated Book"
argument_list|,
literal|123
argument_list|)
decl_stmt|;
name|Response
name|response
init|=
name|wt
operator|.
name|path
argument_list|(
literal|"/books/123"
argument_list|)
operator|.
name|request
argument_list|(
literal|"application/xml"
argument_list|)
operator|.
name|put
argument_list|(
name|Entity
operator|.
name|xml
argument_list|(
name|book
argument_list|)
argument_list|)
decl_stmt|;
name|assertStatus
argument_list|(
name|Status
operator|.
name|OK
argument_list|,
name|response
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|assertStatus
parameter_list|(
name|Status
name|expectedStatus
parameter_list|,
name|Response
name|response
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedStatus
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|response
operator|.
name|getStatus
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Configuration
specifier|public
name|Option
index|[]
name|config
parameter_list|()
block|{
return|return
operator|new
name|Option
index|[]
block|{
name|cxfBaseConfig
argument_list|()
block|,
name|features
argument_list|(
name|cxfUrl
argument_list|,
literal|"cxf-core"
argument_list|,
literal|"cxf-wsdl"
argument_list|,
literal|"cxf-jaxrs"
argument_list|,
literal|"cxf-bean-validation-core"
argument_list|,
literal|"cxf-bean-validation"
argument_list|)
block|,
name|logLevel
argument_list|(
name|LogLevel
operator|.
name|INFO
argument_list|)
block|,
name|provision
argument_list|(
name|serviceBundle
argument_list|()
argument_list|)
block|}
return|;
block|}
specifier|private
specifier|static
name|InputStream
name|serviceBundle
parameter_list|()
block|{
return|return
name|TinyBundles
operator|.
name|bundle
argument_list|()
operator|.
name|add
argument_list|(
name|AbstractServerActivator
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|JaxRsTestActivator
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|Book
operator|.
name|class
argument_list|)
operator|.
name|add
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
operator|.
name|set
argument_list|(
name|Constants
operator|.
name|BUNDLE_ACTIVATOR
argument_list|,
name|JaxRsTestActivator
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|(
name|TinyBundles
operator|.
name|withBnd
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

