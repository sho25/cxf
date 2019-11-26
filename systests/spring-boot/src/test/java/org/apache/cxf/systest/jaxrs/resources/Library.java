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
operator|.
name|resources
package|;
end_package

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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
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
name|Response
operator|.
name|Status
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Component
import|;
end_import

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|Parameter
import|;
end_import

begin_class
annotation|@
name|Component
annotation|@
name|Path
argument_list|(
literal|"library"
argument_list|)
specifier|public
class|class
name|Library
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Book
argument_list|>
name|books
init|=
name|Collections
operator|.
name|synchronizedMap
argument_list|(
operator|new
name|TreeMap
argument_list|<
name|String
argument_list|,
name|Book
argument_list|>
argument_list|(
name|String
operator|.
name|CASE_INSENSITIVE_ORDER
argument_list|)
argument_list|)
decl_stmt|;
specifier|public
name|Library
parameter_list|()
block|{
name|books
operator|.
name|put
argument_list|(
literal|"1"
argument_list|,
operator|new
name|Book
argument_list|(
literal|"Book #1"
argument_list|,
literal|"John Smith"
argument_list|)
argument_list|)
expr_stmt|;
name|books
operator|.
name|put
argument_list|(
literal|"2"
argument_list|,
operator|new
name|Book
argument_list|(
literal|"Book #2"
argument_list|,
literal|"Tom Tommyknocker"
argument_list|)
argument_list|)
expr_stmt|;
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
name|GET
specifier|public
name|Response
name|getBooks
parameter_list|(
annotation|@
name|Parameter
argument_list|(
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
name|books
operator|.
name|values
argument_list|()
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
literal|"{id}"
argument_list|)
annotation|@
name|GET
specifier|public
name|Response
name|getBook
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id
parameter_list|)
block|{
return|return
name|books
operator|.
name|containsKey
argument_list|(
name|id
argument_list|)
condition|?
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|entity
argument_list|(
name|books
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
operator|.
name|build
argument_list|()
else|:
name|Response
operator|.
name|status
argument_list|(
name|Status
operator|.
name|NOT_FOUND
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit
