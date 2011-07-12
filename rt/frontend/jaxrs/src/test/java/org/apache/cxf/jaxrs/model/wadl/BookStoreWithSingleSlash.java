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
name|model
operator|.
name|wadl
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
name|Response
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
name|wadl
operator|.
name|jaxb
operator|.
name|Book
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/"
argument_list|)
specifier|public
class|class
name|BookStoreWithSingleSlash
block|{
annotation|@
name|QueryParam
argument_list|(
literal|"name"
argument_list|)
specifier|private
name|String
name|name
decl_stmt|;
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
specifier|public
name|void
name|setId
parameter_list|(
name|int
name|id
parameter_list|)
block|{     }
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"book"
argument_list|)
annotation|@
name|ElementClass
argument_list|(
name|response
operator|=
name|Book
operator|.
name|class
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/xml"
argument_list|)
specifier|public
name|Response
name|getBookName
parameter_list|()
block|{
return|return
name|Response
operator|.
name|ok
argument_list|()
operator|.
name|entity
argument_list|(
name|name
argument_list|)
operator|.
name|build
argument_list|()
return|;
block|}
block|}
end_class

end_unit

