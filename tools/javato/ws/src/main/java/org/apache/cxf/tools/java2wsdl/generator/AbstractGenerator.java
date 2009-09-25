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
name|java2wsdl
operator|.
name|generator
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
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
name|Bus
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
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
name|tools
operator|.
name|common
operator|.
name|ToolContext
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractGenerator
parameter_list|<
name|T
parameter_list|>
block|{
specifier|private
name|ServiceInfo
name|service
decl_stmt|;
specifier|private
name|boolean
name|allowImports
decl_stmt|;
specifier|private
name|File
name|outputdir
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|private
name|ToolContext
name|context
decl_stmt|;
specifier|public
name|void
name|setToolContext
parameter_list|(
name|ToolContext
name|arg
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|arg
expr_stmt|;
block|}
specifier|public
name|ToolContext
name|getToolContext
parameter_list|()
block|{
return|return
name|this
operator|.
name|context
return|;
block|}
specifier|public
name|void
name|setOutputBase
parameter_list|(
name|File
name|out
parameter_list|)
block|{
name|this
operator|.
name|outputdir
operator|=
name|out
expr_stmt|;
block|}
specifier|public
name|File
name|getOutputBase
parameter_list|()
block|{
return|return
name|this
operator|.
name|outputdir
return|;
block|}
specifier|public
name|void
name|setServiceModel
parameter_list|(
name|ServiceInfo
name|s
parameter_list|)
block|{
name|this
operator|.
name|service
operator|=
name|s
expr_stmt|;
block|}
specifier|public
name|ServiceInfo
name|getServiceModel
parameter_list|()
block|{
return|return
name|this
operator|.
name|service
return|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|void
name|setAllowImports
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
name|allowImports
operator|=
name|b
expr_stmt|;
block|}
specifier|public
name|boolean
name|allowImports
parameter_list|()
block|{
return|return
name|allowImports
return|;
block|}
specifier|public
specifier|abstract
name|T
name|generate
parameter_list|(
name|File
name|file
parameter_list|)
function_decl|;
specifier|protected
name|File
name|createOutputDir
parameter_list|(
name|File
name|file
parameter_list|)
block|{
name|String
name|parent
init|=
name|file
operator|.
name|getParent
argument_list|()
decl_stmt|;
if|if
condition|(
name|parent
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|File
name|parentDir
init|=
operator|new
name|File
argument_list|(
name|parent
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|parentDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|parentDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
return|return
name|parentDir
return|;
block|}
block|}
end_class

end_unit

