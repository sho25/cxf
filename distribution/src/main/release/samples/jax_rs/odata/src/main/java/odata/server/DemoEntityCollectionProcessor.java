begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|odata
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|data
operator|.
name|ContextURL
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|data
operator|.
name|Entity
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|data
operator|.
name|EntityCollection
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|data
operator|.
name|Property
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|data
operator|.
name|ValueType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmEntitySet
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|edm
operator|.
name|EdmEntityType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|ex
operator|.
name|ODataRuntimeException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|format
operator|.
name|ContentType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|http
operator|.
name|HttpHeader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|commons
operator|.
name|api
operator|.
name|http
operator|.
name|HttpStatusCode
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|OData
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|ODataApplicationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|ODataRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|ODataResponse
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|ServiceMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|processor
operator|.
name|EntityCollectionProcessor
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|serializer
operator|.
name|EntityCollectionSerializerOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|serializer
operator|.
name|ODataSerializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|serializer
operator|.
name|SerializerException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|serializer
operator|.
name|SerializerResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|uri
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
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|uri
operator|.
name|UriResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|olingo
operator|.
name|server
operator|.
name|api
operator|.
name|uri
operator|.
name|UriResourceEntitySet
import|;
end_import

begin_comment
comment|/**  * This class is invoked by the Olingo framework when the the OData service is invoked order to display a list/collection of data (entities).  * This is the case if an EntitySet is requested by the user.  * Such an example URL would be:  * http://localhost:8080/ExampleService1/ExampleService1.svc/Products  */
end_comment

begin_class
specifier|public
class|class
name|DemoEntityCollectionProcessor
implements|implements
name|EntityCollectionProcessor
block|{
specifier|private
name|OData
name|odata
decl_stmt|;
specifier|private
name|ServiceMetadata
name|serviceMetadata
decl_stmt|;
comment|// our processor is initialized with the OData context object
specifier|public
name|void
name|init
parameter_list|(
name|OData
name|odata
parameter_list|,
name|ServiceMetadata
name|serviceMetadata
parameter_list|)
block|{
name|this
operator|.
name|odata
operator|=
name|odata
expr_stmt|;
name|this
operator|.
name|serviceMetadata
operator|=
name|serviceMetadata
expr_stmt|;
block|}
comment|// the only method that is declared in the EntityCollectionProcessor interface
comment|// this method is called, when the user fires a request to an EntitySet
comment|// in our example, the URL would be:
comment|// http://localhost:8080/ExampleService1/ExampleServlet1.svc/Products
specifier|public
name|void
name|readEntityCollection
parameter_list|(
name|ODataRequest
name|request
parameter_list|,
name|ODataResponse
name|response
parameter_list|,
name|UriInfo
name|uriInfo
parameter_list|,
name|ContentType
name|responseFormat
parameter_list|)
throws|throws
name|ODataApplicationException
throws|,
name|SerializerException
block|{
comment|// 1st we have retrieve the requested EntitySet from the uriInfo object (representation of the parsed service URI)
name|List
argument_list|<
name|UriResource
argument_list|>
name|resourcePaths
init|=
name|uriInfo
operator|.
name|getUriResourceParts
argument_list|()
decl_stmt|;
name|UriResourceEntitySet
name|uriResourceEntitySet
init|=
operator|(
name|UriResourceEntitySet
operator|)
name|resourcePaths
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
comment|// in our example, the first segment is the EntitySet
name|EdmEntitySet
name|edmEntitySet
init|=
name|uriResourceEntitySet
operator|.
name|getEntitySet
argument_list|()
decl_stmt|;
comment|// 2nd: fetch the data from backend for this requested EntitySetName // it has to be delivered as EntitySet object
name|EntityCollection
name|entitySet
init|=
name|getData
argument_list|(
name|edmEntitySet
argument_list|)
decl_stmt|;
comment|// 3rd: create a serializer based on the requested format (json)
name|ODataSerializer
name|serializer
init|=
name|odata
operator|.
name|createSerializer
argument_list|(
name|responseFormat
argument_list|)
decl_stmt|;
comment|// 4th: Now serialize the content: transform from the EntitySet object to InputStream
name|EdmEntityType
name|edmEntityType
init|=
name|edmEntitySet
operator|.
name|getEntityType
argument_list|()
decl_stmt|;
name|ContextURL
name|contextUrl
init|=
name|ContextURL
operator|.
name|with
argument_list|()
operator|.
name|entitySet
argument_list|(
name|edmEntitySet
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
specifier|final
name|String
name|id
init|=
name|request
operator|.
name|getRawBaseUri
argument_list|()
operator|+
literal|"/"
operator|+
name|edmEntitySet
operator|.
name|getName
argument_list|()
decl_stmt|;
name|EntityCollectionSerializerOptions
name|opts
init|=
name|EntityCollectionSerializerOptions
operator|.
name|with
argument_list|()
operator|.
name|id
argument_list|(
name|id
argument_list|)
operator|.
name|contextURL
argument_list|(
name|contextUrl
argument_list|)
operator|.
name|build
argument_list|()
decl_stmt|;
name|SerializerResult
name|serializedContent
init|=
name|serializer
operator|.
name|entityCollection
argument_list|(
name|serviceMetadata
argument_list|,
name|edmEntityType
argument_list|,
name|entitySet
argument_list|,
name|opts
argument_list|)
decl_stmt|;
comment|// Finally: configure the response object: set the body, headers and status code
name|response
operator|.
name|setContent
argument_list|(
name|serializedContent
operator|.
name|getContent
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setStatusCode
argument_list|(
name|HttpStatusCode
operator|.
name|OK
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|response
operator|.
name|setHeader
argument_list|(
name|HttpHeader
operator|.
name|CONTENT_TYPE
argument_list|,
name|responseFormat
operator|.
name|toContentTypeString
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**    * Helper method for providing some sample data    * @param edmEntitySet for which the data is requested    * @return data of requested entity set    */
specifier|private
name|EntityCollection
name|getData
parameter_list|(
name|EdmEntitySet
name|edmEntitySet
parameter_list|)
block|{
name|EntityCollection
name|productsCollection
init|=
operator|new
name|EntityCollection
argument_list|()
decl_stmt|;
comment|// check for which EdmEntitySet the data is requested
if|if
condition|(
name|DemoEdmProvider
operator|.
name|ES_PRODUCTS_NAME
operator|.
name|equals
argument_list|(
name|edmEntitySet
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|Entity
argument_list|>
name|productList
init|=
name|productsCollection
operator|.
name|getEntities
argument_list|()
decl_stmt|;
comment|// add some sample product entities
specifier|final
name|Entity
name|e1
init|=
operator|new
name|Entity
argument_list|()
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|1
argument_list|)
argument_list|)
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"Name"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|"Notebook Basic 15"
argument_list|)
argument_list|)
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"Description"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|"Notebook Basic, 1.7GHz - 15 XGA - 1024MB DDR2 SDRAM - 40GB"
argument_list|)
argument_list|)
decl_stmt|;
name|e1
operator|.
name|setId
argument_list|(
name|createId
argument_list|(
literal|"Products"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|productList
operator|.
name|add
argument_list|(
name|e1
argument_list|)
expr_stmt|;
specifier|final
name|Entity
name|e2
init|=
operator|new
name|Entity
argument_list|()
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|2
argument_list|)
argument_list|)
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"Name"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|"1UMTS PDA"
argument_list|)
argument_list|)
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"Description"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|"Ultrafast 3G UMTS/HSDPA Pocket PC, supports GSM network"
argument_list|)
argument_list|)
decl_stmt|;
name|e2
operator|.
name|setId
argument_list|(
name|createId
argument_list|(
literal|"Products"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|productList
operator|.
name|add
argument_list|(
name|e2
argument_list|)
expr_stmt|;
specifier|final
name|Entity
name|e3
init|=
operator|new
name|Entity
argument_list|()
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"ID"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|3
argument_list|)
argument_list|)
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"Name"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|"Ergo Screen"
argument_list|)
argument_list|)
operator|.
name|addProperty
argument_list|(
operator|new
name|Property
argument_list|(
literal|null
argument_list|,
literal|"Description"
argument_list|,
name|ValueType
operator|.
name|PRIMITIVE
argument_list|,
literal|"19 Optimum Resolution 1024 x 768 @ 85Hz, resolution 1280 x 960"
argument_list|)
argument_list|)
decl_stmt|;
name|e3
operator|.
name|setId
argument_list|(
name|createId
argument_list|(
literal|"Products"
argument_list|,
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|productList
operator|.
name|add
argument_list|(
name|e3
argument_list|)
expr_stmt|;
block|}
return|return
name|productsCollection
return|;
block|}
specifier|private
name|URI
name|createId
parameter_list|(
name|String
name|entitySetName
parameter_list|,
name|Object
name|id
parameter_list|)
block|{
try|try
block|{
return|return
operator|new
name|URI
argument_list|(
name|entitySetName
operator|+
literal|"("
operator|+
name|String
operator|.
name|valueOf
argument_list|(
name|id
argument_list|)
operator|+
literal|")"
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ODataRuntimeException
argument_list|(
literal|"Unable to create id for entity: "
operator|+
name|entitySetName
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

