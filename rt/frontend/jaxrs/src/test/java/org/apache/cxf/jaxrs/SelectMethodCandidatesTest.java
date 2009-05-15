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
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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
name|jaxrs
operator|.
name|fortest
operator|.
name|BookEntity
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
name|fortest
operator|.
name|BookEntity2
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
name|fortest
operator|.
name|GenericEntityImpl
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
name|impl
operator|.
name|MetadataMap
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
name|ClassResourceInfo
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
name|OperationResourceInfo
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
name|URITemplate
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
name|ProviderFactory
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
name|Chapter
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
name|utils
operator|.
name|JAXRSUtils
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
name|Exchange
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
name|ExchangeImpl
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
name|easymock
operator|.
name|classextension
operator|.
name|EasyMock
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
name|SelectMethodCandidatesTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testFindFromAbstractGenericClass
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGenericSuperType
argument_list|(
name|BookEntity
operator|.
name|class
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindFromAbstractGenericClass2
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGenericSuperType
argument_list|(
name|BookEntity2
operator|.
name|class
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindFromAbstractGenericInterface
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestGenericSuperType
argument_list|(
name|GenericEntityImpl
operator|.
name|class
argument_list|,
literal|"POST"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFindFromAbstractGenericClass3
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSServiceFactoryBean
name|sf
init|=
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|BookEntity
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|sf
operator|.
name|getService
argument_list|()
operator|)
operator|.
name|getClassResourceInfos
argument_list|()
decl_stmt|;
name|String
name|contentTypes
init|=
literal|"text/xml"
decl_stmt|;
name|String
name|acceptContentTypes
init|=
literal|"text/xml"
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|resource
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/books"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"PUT"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resourceMethod needs to be selected"
argument_list|,
literal|"putEntity"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|Endpoint
name|e
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|get
argument_list|(
name|ProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|String
name|value
init|=
literal|"<Chapter><title>The Book</title><id>2</id></Chapter>"
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|params
init|=
name|JAXRSUtils
operator|.
name|processParameters
argument_list|(
name|ori
argument_list|,
name|values
argument_list|,
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|params
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Chapter
name|c
init|=
operator|(
name|Chapter
operator|)
name|params
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|c
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|c
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Book"
argument_list|,
name|c
operator|.
name|getTitle
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestGenericSuperType
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|serviceClass
parameter_list|,
name|String
name|methodName
parameter_list|)
block|{
name|JAXRSServiceFactoryBean
name|sf
init|=
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|sf
operator|.
name|getService
argument_list|()
operator|)
operator|.
name|getClassResourceInfos
argument_list|()
decl_stmt|;
name|String
name|contentTypes
init|=
literal|"text/xml"
decl_stmt|;
name|String
name|acceptContentTypes
init|=
literal|"text/xml"
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|resource
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/books"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
name|methodName
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resourceMethod needs to be selected"
argument_list|,
name|methodName
operator|.
name|toLowerCase
argument_list|()
operator|+
literal|"Entity"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Message
name|m
init|=
operator|new
name|MessageImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|put
argument_list|(
name|Message
operator|.
name|CONTENT_TYPE
argument_list|,
literal|"text/xml"
argument_list|)
expr_stmt|;
name|Exchange
name|ex
init|=
operator|new
name|ExchangeImpl
argument_list|()
decl_stmt|;
name|m
operator|.
name|setExchange
argument_list|(
name|ex
argument_list|)
expr_stmt|;
name|Endpoint
name|e
init|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|)
decl_stmt|;
name|e
operator|.
name|get
argument_list|(
name|ProviderFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andReturn
argument_list|(
name|ProviderFactory
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|e
argument_list|)
expr_stmt|;
name|ex
operator|.
name|put
argument_list|(
name|Endpoint
operator|.
name|class
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|String
name|value
init|=
literal|"<Book><name>The Book</name><id>2</id></Book>"
decl_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
operator|new
name|ByteArrayInputStream
argument_list|(
name|value
operator|.
name|getBytes
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|params
init|=
name|JAXRSUtils
operator|.
name|processParameters
argument_list|(
name|ori
argument_list|,
name|values
argument_list|,
name|m
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|params
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|Book
name|book
init|=
operator|(
name|Book
operator|)
name|params
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|book
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2L
argument_list|,
name|book
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"The Book"
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
name|testFindTargetSubResource
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSServiceFactoryBean
name|sf
init|=
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
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
name|TestResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|sf
operator|.
name|getService
argument_list|()
operator|)
operator|.
name|getClassResourceInfos
argument_list|()
decl_stmt|;
name|String
name|contentTypes
init|=
literal|"*/*"
decl_stmt|;
name|String
name|acceptContentTypes
init|=
literal|"text/xml,*/*"
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|resource
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/resource"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"resourceMethod needs to be selected"
argument_list|,
literal|"resourceMethod"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
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
name|testSelectUsingQualityFactors
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSServiceFactoryBean
name|sf
init|=
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
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
name|TestResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|sf
operator|.
name|getService
argument_list|()
operator|)
operator|.
name|getClassResourceInfos
argument_list|()
decl_stmt|;
name|String
name|contentTypes
init|=
literal|"*/*"
decl_stmt|;
name|String
name|acceptContentTypes
init|=
literal|"application/xml;q=0.5,application/json"
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|resource
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/resource1"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jsonResource needs to be selected"
argument_list|,
literal|"jsonResource"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
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
name|testFindTargetResourceClassWithTemplates
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSServiceFactoryBean
name|sf
init|=
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
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
name|TestResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|sf
operator|.
name|getService
argument_list|()
operator|)
operator|.
name|getClassResourceInfos
argument_list|()
decl_stmt|;
name|String
name|contentTypes
init|=
literal|"*/*"
decl_stmt|;
name|String
name|acceptContentTypes
init|=
literal|"application/xml"
decl_stmt|;
comment|//If acceptContentTypes does not specify a specific Mime type, the
comment|//method is declared with a most specific ProduceMime type is selected.
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|resource
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"listMethod needs to be selected"
argument_list|,
literal|"listMethod"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|acceptContentTypes
operator|=
literal|"application/xml,application/json"
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/1"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|parseMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"readMethod needs to be selected"
argument_list|,
literal|"readMethod"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|contentTypes
operator|=
literal|"application/xml"
expr_stmt|;
name|acceptContentTypes
operator|=
literal|"application/xml"
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/1"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"readMethod needs to be selected"
argument_list|,
literal|"readMethod"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|contentTypes
operator|=
literal|"application/json"
expr_stmt|;
name|acceptContentTypes
operator|=
literal|"application/json"
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/1/bar/baz/baz"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"readMethod2 needs to be selected"
argument_list|,
literal|"readMethod2"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|contentTypes
operator|=
literal|"application/json"
expr_stmt|;
name|acceptContentTypes
operator|=
literal|"application/json"
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/1"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unlimitedPath needs to be selected"
argument_list|,
literal|"unlimitedPath"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/1/2"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
name|MediaType
operator|.
name|valueOf
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"limitedPath needs to be selected"
argument_list|,
literal|"limitedPath"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
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
name|testSelectBar
parameter_list|()
throws|throws
name|Exception
block|{
name|JAXRSServiceFactoryBean
name|sf
init|=
operator|new
name|JAXRSServiceFactoryBean
argument_list|()
decl_stmt|;
name|sf
operator|.
name|setResourceClasses
argument_list|(
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
name|TestResource
operator|.
name|class
argument_list|)
expr_stmt|;
name|sf
operator|.
name|create
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|ClassResourceInfo
argument_list|>
name|resources
init|=
operator|(
operator|(
name|JAXRSServiceImpl
operator|)
name|sf
operator|.
name|getService
argument_list|()
operator|)
operator|.
name|getClassResourceInfos
argument_list|()
decl_stmt|;
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|values
init|=
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|ClassResourceInfo
name|resource
init|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/custom"
argument_list|,
name|values
argument_list|)
decl_stmt|;
name|String
name|contentTypes
init|=
literal|"*/*"
decl_stmt|;
name|String
name|acceptContentTypes
init|=
literal|"application/bar,application/foo"
decl_stmt|;
name|OperationResourceInfo
name|ori
init|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"readBar"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|acceptContentTypes
operator|=
literal|"application/foo,application/bar"
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/custom"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"readFoo"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|acceptContentTypes
operator|=
literal|"application/foo;q=0.5,application/bar"
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/custom"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"readBar"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|acceptContentTypes
operator|=
literal|"application/foo,application/bar;q=0.5"
expr_stmt|;
name|resource
operator|=
name|JAXRSUtils
operator|.
name|selectResourceClass
argument_list|(
name|resources
argument_list|,
literal|"/1/2/3/d/custom"
argument_list|,
name|values
argument_list|)
expr_stmt|;
name|ori
operator|=
name|JAXRSUtils
operator|.
name|findTargetMethod
argument_list|(
name|resource
argument_list|,
name|values
operator|.
name|getFirst
argument_list|(
name|URITemplate
operator|.
name|FINAL_MATCH_GROUP
argument_list|)
argument_list|,
literal|"GET"
argument_list|,
name|values
argument_list|,
name|contentTypes
argument_list|,
name|JAXRSUtils
operator|.
name|sortMediaTypes
argument_list|(
name|acceptContentTypes
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|ori
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"readFoo"
argument_list|,
name|ori
operator|.
name|getMethodToInvoke
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

