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
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|mock
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|CompletionStage
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
name|json
operator|.
name|JsonStructure
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
name|core
operator|.
name|Response
import|;
end_import

begin_interface
specifier|public
interface|interface
name|AsyncClientWithCompletionStage
block|{
annotation|@
name|PUT
annotation|@
name|Path
argument_list|(
literal|"/test"
argument_list|)
name|CompletionStage
argument_list|<
name|Response
argument_list|>
name|put
parameter_list|(
name|String
name|text
parameter_list|)
function_decl|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/test2"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
name|CompletionStage
argument_list|<
name|JsonStructure
argument_list|>
name|get
parameter_list|()
function_decl|;
annotation|@
name|GET
annotation|@
name|Path
argument_list|(
literal|"/test3"
argument_list|)
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
name|CompletionStage
argument_list|<
name|Collection
argument_list|<
name|JsonObject
argument_list|>
argument_list|>
name|getAll
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

