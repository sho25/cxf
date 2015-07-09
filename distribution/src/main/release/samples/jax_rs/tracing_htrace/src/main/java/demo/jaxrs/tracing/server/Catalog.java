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
name|tracing
operator|.
name|server
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ExecutorService
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executors
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonArray
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|json
operator|.
name|JsonObject
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
name|NotFoundException
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
name|container
operator|.
name|AsyncResponse
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
name|container
operator|.
name|Suspended
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
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|conf
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|hadoop
operator|.
name|hbase
operator|.
name|HBaseConfiguration
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/catalog"
argument_list|)
specifier|public
class|class
name|Catalog
block|{
specifier|private
specifier|final
name|ExecutorService
name|executor
init|=
name|Executors
operator|.
name|newFixedThreadPool
argument_list|(
literal|2
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|CatalogStore
name|store
decl_stmt|;
specifier|public
name|Catalog
parameter_list|()
throws|throws
name|IOException
block|{
specifier|final
name|Configuration
name|configuration
init|=
name|HBaseConfiguration
operator|.
name|create
argument_list|()
decl_stmt|;
name|store
operator|=
operator|new
name|CatalogStore
argument_list|(
name|configuration
argument_list|,
literal|"books"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|void
name|addBook
parameter_list|(
annotation|@
name|Suspended
specifier|final
name|AsyncResponse
name|response
parameter_list|,
annotation|@
name|Context
specifier|final
name|UriInfo
name|uri
parameter_list|,
annotation|@
name|FormParam
argument_list|(
literal|"title"
argument_list|)
specifier|final
name|String
name|title
parameter_list|)
block|{
name|executor
operator|.
name|submit
argument_list|(
operator|new
name|Runnable
argument_list|()
block|{
specifier|public
name|void
name|run
parameter_list|()
block|{             }
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|JsonArray
name|getBooks
parameter_list|()
throws|throws
name|IOException
block|{
return|return
name|store
operator|.
name|scan
argument_list|()
return|;
block|}
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/{id}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|JsonObject
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
specifier|final
name|String
name|id
parameter_list|)
throws|throws
name|IOException
block|{
specifier|final
name|JsonObject
name|book
init|=
name|store
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|book
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NotFoundException
argument_list|(
literal|"Book with does not exists: "
operator|+
name|id
argument_list|)
throw|;
block|}
return|return
name|book
return|;
block|}
annotation|@
name|DELETE
annotation|@
name|Path
argument_list|(
literal|"/{id}"
argument_list|)
annotation|@
name|Produces
argument_list|(
name|MediaType
operator|.
name|APPLICATION_JSON
argument_list|)
specifier|public
name|Response
name|delete
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
specifier|final
name|String
name|id
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|store
operator|.
name|remove
argument_list|(
name|id
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|NotFoundException
argument_list|(
literal|"Book with does not exists: "
operator|+
name|id
argument_list|)
throw|;
block|}
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
