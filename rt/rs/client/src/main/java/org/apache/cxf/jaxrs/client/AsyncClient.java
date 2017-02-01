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
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_comment
comment|//Work in progress. May be removed once the Rx client work is finalized
end_comment

begin_interface
specifier|public
interface|interface
name|AsyncClient
block|{
name|void
name|prepareAsyncClient
parameter_list|(
name|String
name|httpMethod
parameter_list|,
name|Object
name|body
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|requestClass
parameter_list|,
name|Type
name|inType
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|respClass
parameter_list|,
name|Type
name|outType
parameter_list|,
name|JaxrsClientCallback
argument_list|<
name|?
argument_list|>
name|cb
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

