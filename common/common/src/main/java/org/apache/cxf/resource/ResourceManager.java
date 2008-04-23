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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Locates resources that are used at runtime.  The  *<code>ResourceManager</code> queries registered  *<code>ResourceResolver</code> to find resources.  */
end_comment

begin_interface
specifier|public
interface|interface
name|ResourceManager
block|{
comment|/**      * Resolve a resource.  The ResourceManager will query all of the      * registered<code>ResourceResovler</code> objects until one      * manages to resolve the resource      *       * @param name name of resource to resolve.      * @param type type of resource to resolve.      * @return the resolved resource or null if nothing found.      */
parameter_list|<
name|T
parameter_list|>
name|T
name|resolveResource
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
function_decl|;
comment|/**       * Resolve a resource with via a specified list of resovlers.  This allows       * resources to be specified with a locally defined list of resolvers.      *       * @param name name of resource to resolve.      * @param type type of resource to resolve.      * @param resolvers list of<code>ResourceResolvers</codea> to search.      * @return the resolved resource or null if nothing found.      */
parameter_list|<
name|T
parameter_list|>
name|T
name|resolveResource
parameter_list|(
name|String
name|name
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|resolvers
parameter_list|)
function_decl|;
comment|/**      * Open stream to resource.        *      * @param name name of resource to resolve.       * @return the InputStream to the resource or null if the resource      * cannot be found.      */
name|InputStream
name|getResourceAsStream
parameter_list|(
name|String
name|name
parameter_list|)
function_decl|;
comment|/**       * Add a<code>ResourceResolver</code>.  The newly added resolver      * is added at the head of the list so the most recently added      * will be queried first.      * @param resolver the<code>ResourceResolver</code> to      * add. Duplicates will be ignored.      */
name|void
name|addResourceResolver
parameter_list|(
name|ResourceResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**       * Remove a<code>ResourceResolver</code>.      * @param resolver the<code>ResourceResolver</code> to remove.      * If not previously registered, it is ignored.      */
name|void
name|removeResourceResolver
parameter_list|(
name|ResourceResolver
name|resolver
parameter_list|)
function_decl|;
comment|/**      * Get all the currently registered resolvers.  This method should return       * a copy of the list of resolvers so that resolvers added after this method       * has been called will alter the list returned.      */
name|List
argument_list|<
name|ResourceResolver
argument_list|>
name|getResourceResolvers
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

