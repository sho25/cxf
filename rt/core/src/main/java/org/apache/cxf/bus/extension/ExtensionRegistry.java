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
name|bus
operator|.
name|extension
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|ConcurrentMap
import|;
end_import

begin_comment
comment|/**  * Static registry of extensions that are loaded in addition to the   * extensions the Bus will automatically detect.  Mostly used by   * the OSGi bundle activator to detect extensions in bundles outside  * the CXF bundle.   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|ExtensionRegistry
block|{
specifier|private
specifier|static
name|ConcurrentMap
argument_list|<
name|String
argument_list|,
name|Extension
argument_list|>
name|extensions
init|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|Extension
argument_list|>
argument_list|(
literal|16
argument_list|,
literal|0.75f
argument_list|,
literal|4
argument_list|)
decl_stmt|;
specifier|private
name|ExtensionRegistry
parameter_list|()
block|{
comment|//singleton
block|}
specifier|public
specifier|static
name|Map
argument_list|<
name|String
argument_list|,
name|Extension
argument_list|>
name|getRegisteredExtensions
parameter_list|()
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Extension
argument_list|>
name|exts
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Extension
argument_list|>
argument_list|(
name|extensions
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Extension
argument_list|>
name|ext
range|:
name|extensions
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|exts
operator|.
name|put
argument_list|(
name|ext
operator|.
name|getKey
argument_list|()
argument_list|,
name|ext
operator|.
name|getValue
argument_list|()
operator|.
name|cloneNoObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|exts
return|;
block|}
specifier|public
specifier|static
name|void
name|removeExtensions
parameter_list|(
name|List
argument_list|<
name|Extension
argument_list|>
name|list
parameter_list|)
block|{
for|for
control|(
name|Extension
name|e
range|:
name|list
control|)
block|{
name|extensions
operator|.
name|remove
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
specifier|static
name|void
name|addExtensions
parameter_list|(
name|List
argument_list|<
name|Extension
argument_list|>
name|list
parameter_list|)
block|{
for|for
control|(
name|Extension
name|e
range|:
name|list
control|)
block|{
name|extensions
operator|.
name|putIfAbsent
argument_list|(
name|e
operator|.
name|getName
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

