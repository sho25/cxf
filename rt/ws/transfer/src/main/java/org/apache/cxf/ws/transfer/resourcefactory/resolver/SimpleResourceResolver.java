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
name|ws
operator|.
name|transfer
operator|.
name|resourcefactory
operator|.
name|resolver
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|ws
operator|.
name|transfer
operator|.
name|Create
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
name|ws
operator|.
name|transfer
operator|.
name|manager
operator|.
name|ResourceManager
import|;
end_import

begin_comment
comment|/**  * The simple ResourceResolver, which always returns a predefined resource  * location.  */
end_comment

begin_class
specifier|public
class|class
name|SimpleResourceResolver
implements|implements
name|ResourceResolver
block|{
specifier|protected
name|ResourceManager
name|resourceManager
decl_stmt|;
specifier|protected
name|String
name|resourceURL
decl_stmt|;
specifier|public
name|SimpleResourceResolver
parameter_list|()
block|{              }
specifier|public
name|SimpleResourceResolver
parameter_list|(
name|String
name|resourceURL
parameter_list|)
block|{
name|this
operator|.
name|resourceURL
operator|=
name|resourceURL
expr_stmt|;
block|}
specifier|public
name|SimpleResourceResolver
parameter_list|(
name|String
name|resourceURL
parameter_list|,
name|ResourceManager
name|resourceManager
parameter_list|)
block|{
name|this
operator|.
name|resourceURL
operator|=
name|resourceURL
expr_stmt|;
name|this
operator|.
name|resourceManager
operator|=
name|resourceManager
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ResourceReference
name|resolve
parameter_list|(
name|Create
name|body
parameter_list|)
block|{
return|return
operator|new
name|ResourceReference
argument_list|(
name|resourceURL
argument_list|,
name|resourceManager
argument_list|)
return|;
block|}
specifier|public
name|String
name|getResourceURL
parameter_list|()
block|{
return|return
name|resourceURL
return|;
block|}
specifier|public
name|void
name|setResourceURL
parameter_list|(
name|String
name|resourceURL
parameter_list|)
block|{
name|this
operator|.
name|resourceURL
operator|=
name|resourceURL
expr_stmt|;
block|}
specifier|public
name|ResourceManager
name|getResourceManager
parameter_list|()
block|{
return|return
name|resourceManager
return|;
block|}
specifier|public
name|void
name|setResourceManager
parameter_list|(
name|ResourceManager
name|resourceManager
parameter_list|)
block|{
name|this
operator|.
name|resourceManager
operator|=
name|resourceManager
expr_stmt|;
block|}
block|}
end_class

end_unit
