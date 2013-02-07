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

begin_comment
comment|/**  * Converts Response to checked or runtime Exception  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResponseExceptionMapper
parameter_list|<
name|E
extends|extends
name|Throwable
parameter_list|>
block|{
comment|/**      * Converts Response to checked or runtime Exception      * @param r JAXRS Response       * @return mapped exception instance, can be null      */
name|E
name|fromResponse
parameter_list|(
name|Response
name|r
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

