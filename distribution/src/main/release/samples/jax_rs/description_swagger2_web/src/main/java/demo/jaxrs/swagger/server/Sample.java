begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|demo
operator|.
name|jaxrs
operator|.
name|swagger
operator|.
name|server
package|;
end_package

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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
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
name|DELETE
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
name|DefaultValue
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
name|FormParam
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
name|HeaderParam
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
name|POST
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
name|PUT
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
name|QueryParam
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
name|MediaType
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
name|UriInfo
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|Api
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiImplicitParam
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiImplicitParams
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiOperation
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|annotations
operator|.
name|ApiParam
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/sample"
argument_list|)
annotation|@
name|Api
argument_list|(
name|value
operator|=
literal|"/sample"
argument_list|,
name|description
operator|=
literal|"Sample JAX-RS service with Swagger documentation"
argument_list|)
specifier|public
class|class
name|Sample
block|{
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|GET
annotation|@
name|ApiOperation
argument_list|(
name|value
operator|=
literal|"Get operation with Response and @Default value"
argument_list|,
name|notes
operator|=
literal|"Get operation with Response and @Default value"
argument_list|,
name|response
operator|=
name|Item
operator|.
name|class
argument_list|,
name|responseContainer
operator|=
literal|"List"
argument_list|)
specifier|public
name|Response
name|getItems
parameter_list|(
annotation|@
name|ApiParam
argument_list|(
name|value
operator|=
literal|"Page to fetch"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|QueryParam
argument_list|(
literal|"page"
argument_list|)
annotation|@
name|DefaultValue
argument_list|(
literal|"1"
argument_list|)
name|int
name|page
parameter_list|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|Item
argument_list|(
literal|"Item 1"
argument_list|,
literal|"Value 1"
argument_list|)
argument_list|,
operator|new
name|Item
argument_list|(
literal|"Item 2"
argument_list|,
literal|"Value 2"
argument_list|)
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/{name}"
argument_list|)
annotation|@
name|GET
annotation|@
name|ApiOperation
argument_list|(
name|value
operator|=
literal|"Get operation with type and headers"
argument_list|,
name|notes
operator|=
literal|"Get operation with type and headers"
argument_list|,
name|response
operator|=
name|Item
operator|.
name|class
argument_list|)
specifier|public
name|Item
name|getItem
parameter_list|(
annotation|@
name|ApiParam
argument_list|(
name|value
operator|=
literal|"language"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|HeaderParam
argument_list|(
literal|"Accept-Language"
argument_list|)
specifier|final
name|String
name|language
parameter_list|,
annotation|@
name|ApiParam
argument_list|(
name|value
operator|=
literal|"name"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|Item
argument_list|(
literal|"name"
argument_list|,
literal|"Value in "
operator|+
name|language
argument_list|)
return|;
block|}
annotation|@
name|Consumes
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|POST
annotation|@
name|ApiOperation
argument_list|(
name|value
operator|=
literal|"Post operation with entity in a body"
argument_list|,
name|notes
operator|=
literal|"Post operation with entity in a body"
argument_list|,
name|response
operator|=
name|Item
operator|.
name|class
argument_list|)
specifier|public
name|Response
name|createItem
parameter_list|(
annotation|@
name|Context
specifier|final
name|UriInfo
name|uriInfo
parameter_list|,
annotation|@
name|ApiParam
argument_list|(
name|value
operator|=
literal|"item"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
specifier|final
name|Item
name|item
parameter_list|)
block|{
return|return
name|Response
operator|.
name|created
argument_list|(
name|uriInfo
operator|.
name|getBaseUriBuilder
argument_list|()
operator|.
name|path
argument_list|(
name|item
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
operator|.
name|entity
argument_list|(
name|item
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|}
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"/{name}"
argument_list|)
annotation|@
name|PUT
annotation|@
name|ApiOperation
argument_list|(
name|value
operator|=
literal|"Put operation with form parameter"
argument_list|,
name|notes
operator|=
literal|"Put operation with form parameter"
argument_list|,
name|response
operator|=
name|Item
operator|.
name|class
argument_list|)
specifier|public
name|Item
name|updateItem
parameter_list|(
annotation|@
name|ApiParam
argument_list|(
name|value
operator|=
literal|"name"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|,
annotation|@
name|ApiParam
argument_list|(
name|value
operator|=
literal|"value"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|FormParam
argument_list|(
literal|"value"
argument_list|)
name|String
name|value
parameter_list|)
block|{
return|return
operator|new
name|Item
argument_list|(
name|name
argument_list|,
name|value
argument_list|)
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/{name}"
argument_list|)
annotation|@
name|DELETE
annotation|@
name|ApiOperation
argument_list|(
name|value
operator|=
literal|"Delete operation with implicit header"
argument_list|,
name|notes
operator|=
literal|"Delete operation with implicit header"
argument_list|)
annotation|@
name|ApiImplicitParams
argument_list|(
annotation|@
name|ApiImplicitParam
argument_list|(
name|name
operator|=
literal|"Accept-Language"
argument_list|,
name|value
operator|=
literal|"language"
argument_list|,
name|required
operator|=
literal|true
argument_list|,
name|dataType
operator|=
literal|"String"
argument_list|,
name|paramType
operator|=
literal|"header"
argument_list|)
argument_list|)
specifier|public
name|Response
name|delete
parameter_list|(
annotation|@
name|ApiParam
argument_list|(
name|value
operator|=
literal|"name"
argument_list|,
name|required
operator|=
literal|true
argument_list|)
annotation|@
name|PathParam
argument_list|(
literal|"name"
argument_list|)
name|String
name|name
parameter_list|)
block|{
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

