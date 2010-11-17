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
name|QueryParam
import|;
end_import

begin_interface
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
annotation|@
name|Path
argument_list|(
literal|"books"
argument_list|)
specifier|public
interface|interface
name|IRestService
parameter_list|<
name|T
extends|extends
name|Book
parameter_list|,
name|PK
extends|extends
name|Long
parameter_list|>
block|{
annotation|@
name|GET
name|T
name|getById
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"id"
argument_list|)
name|PK
name|id
parameter_list|)
function_decl|;
annotation|@
name|POST
name|PK
name|postEntity
parameter_list|(
name|T
name|instance
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

