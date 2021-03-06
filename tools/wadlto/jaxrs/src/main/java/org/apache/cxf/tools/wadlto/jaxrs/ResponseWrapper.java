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
name|tools
operator|.
name|wadlto
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
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
name|common
operator|.
name|util
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * Wraps response into the container if necessary (for example, when code is generated   * using the JAX-RS 2.1 Reactive Extensions).  */
end_comment

begin_interface
interface|interface
name|ResponseWrapper
block|{
comment|/**      * Wraps response into the container if necessary      * @param type the response type      * @param imports current set of import statements to enrich      * @return response wrapped into the container if necessary      */
name|String
name|wrap
parameter_list|(
name|String
name|type
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|imports
parameter_list|)
function_decl|;
comment|/**      * Wraps response into the container if necessary      * @param type the response type      * s@param imports current set of import statements to enrich      * @return response wrapped into the container if necessary      */
specifier|default
name|String
name|wrap
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|imports
parameter_list|)
block|{
return|return
name|wrap
argument_list|(
name|type
operator|.
name|getSimpleName
argument_list|()
argument_list|,
name|imports
argument_list|)
return|;
block|}
comment|/**      * Creates a new instance of the response wrapper      * @param library the reactive library to use (or null if none)       * @return the instance of the response wrapper      */
specifier|static
name|ResponseWrapper
name|create
parameter_list|(
name|String
name|library
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|library
argument_list|)
condition|)
block|{
return|return
operator|new
name|NoopResponseWrapper
argument_list|()
return|;
block|}
elseif|else
if|if
condition|(
literal|"java8"
operator|.
name|equalsIgnoreCase
argument_list|(
name|library
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|Java8ResponseWrapper
argument_list|()
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"The Reactive Extensions library is not supported: "
operator|+
name|library
argument_list|)
throw|;
block|}
block|}
block|}
end_interface

begin_comment
comment|/**  * Noop response wrapper, returns the response as-is.  */
end_comment

begin_class
class|class
name|NoopResponseWrapper
implements|implements
name|ResponseWrapper
block|{
annotation|@
name|Override
specifier|public
name|String
name|wrap
parameter_list|(
name|String
name|type
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|imports
parameter_list|)
block|{
return|return
name|type
return|;
block|}
block|}
end_class

begin_comment
comment|/**  * Response wrapper for java.util.concurrent.CompletableFuture, returns   * the response wrapped into CompletableFuture<?> container.  */
end_comment

begin_class
class|class
name|Java8ResponseWrapper
implements|implements
name|ResponseWrapper
block|{
annotation|@
name|Override
specifier|public
name|String
name|wrap
parameter_list|(
name|String
name|type
parameter_list|,
name|Set
argument_list|<
name|String
argument_list|>
name|imports
parameter_list|)
block|{
if|if
condition|(
name|imports
operator|!=
literal|null
condition|)
block|{
name|imports
operator|.
name|add
argument_list|(
literal|"java.util.concurrent.CompletableFuture"
argument_list|)
expr_stmt|;
block|}
return|return
literal|"CompletableFuture<"
operator|+
name|type
operator|+
literal|">"
return|;
block|}
block|}
end_class

end_unit

