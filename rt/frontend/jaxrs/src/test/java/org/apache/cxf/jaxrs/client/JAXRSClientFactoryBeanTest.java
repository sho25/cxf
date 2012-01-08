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
name|jaxrs
operator|.
name|client
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
name|Bus
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
name|feature
operator|.
name|AbstractFeature
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
name|Fault
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
name|InterceptorProvider
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
name|model
operator|.
name|UserOperation
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
name|model
operator|.
name|UserResource
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
name|resources
operator|.
name|Book
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
name|resources
operator|.
name|BookStore
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
name|resources
operator|.
name|BookStoreSubresourcesOnly
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
name|phase
operator|.
name|AbstractPhaseInterceptor
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
name|phase
operator|.
name|Phase
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
name|Conduit
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
name|Test
import|;
end_import

begin_class
specifier|public
class|class
name|JAXRSClientFactoryBeanTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testCreateClient
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bean
operator|.
name|create
argument_list|()
operator|instanceof
name|BookStore
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateClientWithUserResource
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|UserResource
name|r
init|=
operator|new
name|UserResource
argument_list|()
decl_stmt|;
name|r
operator|.
name|setName
argument_list|(
name|BookStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|setPath
argument_list|(
literal|"/"
argument_list|)
expr_stmt|;
name|UserOperation
name|op
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|op
operator|.
name|setName
argument_list|(
literal|"getDescription"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setVerb
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setOperations
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|op
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setModelBeans
argument_list|(
name|r
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bean
operator|.
name|create
argument_list|()
operator|instanceof
name|BookStore
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCreateClientWithTwoUserResources
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|UserResource
name|r1
init|=
operator|new
name|UserResource
argument_list|()
decl_stmt|;
name|r1
operator|.
name|setName
argument_list|(
name|BookStore
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setPath
argument_list|(
literal|"/store"
argument_list|)
expr_stmt|;
name|UserOperation
name|op
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|op
operator|.
name|setName
argument_list|(
literal|"getDescription"
argument_list|)
expr_stmt|;
name|op
operator|.
name|setVerb
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|r1
operator|.
name|setOperations
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|op
argument_list|)
argument_list|)
expr_stmt|;
name|UserResource
name|r2
init|=
operator|new
name|UserResource
argument_list|()
decl_stmt|;
name|r2
operator|.
name|setName
argument_list|(
name|Book
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setPath
argument_list|(
literal|"/book"
argument_list|)
expr_stmt|;
name|UserOperation
name|op2
init|=
operator|new
name|UserOperation
argument_list|()
decl_stmt|;
name|op2
operator|.
name|setName
argument_list|(
literal|"getName"
argument_list|)
expr_stmt|;
name|op2
operator|.
name|setVerb
argument_list|(
literal|"GET"
argument_list|)
expr_stmt|;
name|r2
operator|.
name|setOperations
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|op2
argument_list|)
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setModelBeans
argument_list|(
name|r1
argument_list|,
name|r2
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setServiceClass
argument_list|(
name|Book
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bean
operator|.
name|create
argument_list|()
operator|instanceof
name|Book
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConduit
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
expr_stmt|;
name|BookStore
name|store
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStore
operator|.
name|class
argument_list|)
decl_stmt|;
name|Conduit
name|conduit
init|=
name|WebClient
operator|.
name|getConfig
argument_list|(
name|store
argument_list|)
operator|.
name|getConduit
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|conduit
operator|instanceof
name|HTTPConduit
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTemplateInRootPathInherit
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|)
expr_stmt|;
name|BookStoreSubresourcesOnly
name|store
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|BookStoreSubresourcesOnly
name|store2
init|=
name|store
operator|.
name|getItself
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://bar/bookstore/1/2/3/sub1"
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|store2
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTemplateInRootReplace
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|)
expr_stmt|;
name|BookStoreSubresourcesOnly
name|store
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|BookStoreSubresourcesOnly
name|store2
init|=
name|store
operator|.
name|getItself2
argument_list|(
literal|"11"
argument_list|,
literal|"33"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://bar/bookstore/11/2/33/sub2"
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|store2
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testTemplateInRootAppend
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|)
expr_stmt|;
name|BookStoreSubresourcesOnly
name|store
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|BookStoreSubresourcesOnly
name|store2
init|=
name|store
operator|.
name|getItself3
argument_list|(
literal|"id4"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://bar/bookstore/1/2/3/id4/sub3"
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|store2
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAddLoggingToClient
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
name|setAddress
argument_list|(
literal|"http://bar"
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setResourceClass
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|)
expr_stmt|;
name|TestFeature
name|testFeature
init|=
operator|new
name|TestFeature
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|AbstractFeature
argument_list|>
name|features
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractFeature
argument_list|>
argument_list|()
decl_stmt|;
name|features
operator|.
name|add
argument_list|(
name|testFeature
argument_list|)
expr_stmt|;
name|bean
operator|.
name|setFeatures
argument_list|(
name|features
argument_list|)
expr_stmt|;
name|BookStoreSubresourcesOnly
name|store
init|=
name|bean
operator|.
name|create
argument_list|(
name|BookStoreSubresourcesOnly
operator|.
name|class
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"TestFeature wasn't initialized"
argument_list|,
name|testFeature
operator|.
name|isInitialized
argument_list|()
argument_list|)
expr_stmt|;
name|BookStoreSubresourcesOnly
name|store2
init|=
name|store
operator|.
name|getItself3
argument_list|(
literal|"id4"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"http://bar/bookstore/1/2/3/id4/sub3"
argument_list|,
name|WebClient
operator|.
name|client
argument_list|(
name|store2
argument_list|)
operator|.
name|getCurrentURI
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
class|class
name|TestFeature
extends|extends
name|AbstractFeature
block|{
specifier|private
name|TestInterceptor
name|testInterceptor
decl_stmt|;
annotation|@
name|Override
specifier|protected
name|void
name|initializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|testInterceptor
operator|=
operator|new
name|TestInterceptor
argument_list|()
expr_stmt|;
name|provider
operator|.
name|getOutInterceptors
argument_list|()
operator|.
name|add
argument_list|(
name|testInterceptor
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|boolean
name|isInitialized
parameter_list|()
block|{
return|return
name|testInterceptor
operator|.
name|isInitialized
argument_list|()
return|;
block|}
block|}
specifier|private
class|class
name|TestInterceptor
extends|extends
name|AbstractPhaseInterceptor
argument_list|<
name|Message
argument_list|>
block|{
specifier|private
name|boolean
name|isInitialized
decl_stmt|;
specifier|public
name|TestInterceptor
parameter_list|()
block|{
name|this
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TestInterceptor
parameter_list|(
name|String
name|s
parameter_list|)
block|{
name|super
argument_list|(
name|Phase
operator|.
name|PRE_STREAM
argument_list|)
expr_stmt|;
name|isInitialized
operator|=
literal|true
expr_stmt|;
block|}
specifier|public
name|void
name|handleMessage
parameter_list|(
name|Message
name|message
parameter_list|)
throws|throws
name|Fault
block|{         }
specifier|protected
name|boolean
name|isInitialized
parameter_list|()
block|{
return|return
name|isInitialized
return|;
block|}
block|}
block|}
end_class

end_unit

