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
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|databinding
operator|.
name|DataBinding
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
name|interceptor
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
name|jaxrs
operator|.
name|client
operator|.
name|JAXRSClientFactoryBean
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
name|jaxrs
operator|.
name|provider
operator|.
name|aegis
operator|.
name|AegisElementProvider
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
name|provider
operator|.
name|json
operator|.
name|DataBindingJSONProvider
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
name|jibx
operator|.
name|JibxDataBinding
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
name|sdo
operator|.
name|SDODataBinding
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
name|jaxrs
operator|.
name|jibx
operator|.
name|JibxResource
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
name|jaxrs
operator|.
name|sdo
operator|.
name|SDOResource
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
name|jaxrs
operator|.
name|sdo
operator|.
name|Structure
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
name|JAXRSDataBindingTest
extends|extends
name|AbstractBusClientServerTestBase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|PORT
init|=
name|BookDataBindingServer
operator|.
name|PORT
decl_stmt|;
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
name|BookDataBindingServer
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
name|testGetBookJAXB
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/databinding/jaxb/bookstore/books/123"
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|client
operator|.
name|accept
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
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookJIBX
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setDataBinding
argument_list|(
operator|new
name|JibxDataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/databinding/jibx"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|JibxResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|JibxResource
name|client
init|=
name|bean
operator|.
name|create
argument_list|(
name|JibxResource
operator|.
name|class
argument_list|)
decl_stmt|;
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|systest
operator|.
name|jaxrs
operator|.
name|codegen
operator|.
name|jibx
operator|.
name|Book
name|b
init|=
name|client
operator|.
name|getBook
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"JIBX"
argument_list|,
name|b
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBookAegis
parameter_list|()
throws|throws
name|Exception
block|{
name|WebClient
name|client
init|=
name|WebClient
operator|.
name|create
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/databinding/aegis/bookstore/books/123"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
operator|new
name|AegisElementProvider
argument_list|<
name|Book
argument_list|>
argument_list|()
argument_list|)
argument_list|)
decl_stmt|;
name|Book
name|book
init|=
name|client
operator|.
name|accept
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
name|assertEquals
argument_list|(
literal|123L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"CXF in Action"
argument_list|,
name|book
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSDOStructure
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestSDOStructure
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/databinding/sdo"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSDOStructureWithAnnotation
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestSDOStructure
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/databinding/sdo2"
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestSDOStructure
parameter_list|(
name|String
name|address
parameter_list|)
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setDataBinding
argument_list|(
operator|new
name|SDODataBinding
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|SDOResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|SDOResource
name|client
init|=
name|bean
operator|.
name|create
argument_list|(
name|SDOResource
operator|.
name|class
argument_list|)
decl_stmt|;
name|Structure
name|struct
init|=
name|client
operator|.
name|getStructure
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sdo"
argument_list|,
name|struct
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123.5
argument_list|,
name|struct
operator|.
name|getDbl
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|struct
operator|.
name|getInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testSDOStructureJSON
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSClientFactoryBean
name|bean
init|=
operator|new
name|JAXRSClientFactoryBean
argument_list|()
decl_stmt|;
name|DataBinding
name|db
init|=
operator|new
name|SDODataBinding
argument_list|()
decl_stmt|;
name|bean
operator|.
name|setDataBinding
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|DataBindingJSONProvider
argument_list|<
name|Structure
argument_list|>
name|provider
init|=
operator|new
name|DataBindingJSONProvider
argument_list|<
name|Structure
argument_list|>
argument_list|()
decl_stmt|;
name|provider
operator|.
name|setNamespaceMap
argument_list|(
name|Collections
operator|.
name|singletonMap
argument_list|(
literal|"http://apache.org/structure/types"
argument_list|,
literal|"p0"
argument_list|)
argument_list|)
expr_stmt|;
name|provider
operator|.
name|setDataBinding
argument_list|(
name|db
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setProvider
argument_list|(
name|provider
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setAddress
argument_list|(
literal|"http://localhost:"
operator|+
name|PORT
operator|+
literal|"/databinding/sdo"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|SDOResource
operator|.
name|class
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
name|list
init|=
operator|new
name|ArrayList
argument_list|<
name|Interceptor
argument_list|<
name|?
extends|extends
name|Message
argument_list|>
argument_list|>
argument_list|()
decl_stmt|;
name|list
operator|.
name|add
argument_list|(
operator|new
name|LoggingInInterceptor
argument_list|()
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setInInterceptors
argument_list|(
name|list
argument_list|)
expr_stmt|;
name|SDOResource
name|client
init|=
name|bean
operator|.
name|create
argument_list|(
name|SDOResource
operator|.
name|class
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|client
argument_list|)
operator|.
name|accept
argument_list|(
literal|"application/json"
argument_list|)
expr_stmt|;
name|Structure
name|struct
init|=
name|client
operator|.
name|getStructure
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"sdo"
argument_list|,
name|struct
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|123.5
argument_list|,
name|struct
operator|.
name|getDbl
argument_list|()
argument_list|,
literal|0.01
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|struct
operator|.
name|getInt
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

