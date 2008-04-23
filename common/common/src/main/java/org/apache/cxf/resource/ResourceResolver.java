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
name|resource
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_comment
comment|/**  * Resolves resource.  A ResourceResolver is used to find references  * to resources that are being injected into classes  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResourceResolver
block|{
comment|/**      * Resolve a resource given its name and type.      *      * @param resourceName name of the resource to resolve.      * @param resourceType type of the resource to resolve.      * @return an instance of the resource or<code>null</code> if the      * resource cannot be resolved.      */
parameter_list|<
name|T
parameter_list|>
name|T
name|resolve
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|resourceType
parameter_list|)
function_decl|;
comment|/**      * Resolve a resource given its name and return an InputStream to it.      *      * @param resourceName name of the resource to resolve.      * @return an InputStream for the resource or null if it could not be found.      */
name|InputStream
name|getAsStream
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

