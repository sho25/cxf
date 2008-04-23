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
name|processor
operator|.
name|internal
operator|.
name|simple
operator|.
name|generator
package|;
end_package

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
name|ToolConstants
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
name|ToolException
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
name|model
operator|.
name|JavaInterface
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
name|model
operator|.
name|JavaModel
import|;
end_import

begin_class
specifier|public
class|class
name|SimpleClientGenerator
extends|extends
name|AbstractSimpleGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|CLIENT_TEMPLATE
init|=
name|TEMPLATE_BASE
operator|+
literal|"/client.vm"
decl_stmt|;
specifier|public
name|SimpleClientGenerator
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
name|ToolConstants
operator|.
name|CLT_GENERATOR
expr_stmt|;
block|}
specifier|public
name|boolean
name|passthrough
parameter_list|()
block|{
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLIENT
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|public
name|void
name|generate
parameter_list|(
name|ToolContext
name|penv
parameter_list|)
throws|throws
name|ToolException
block|{
name|this
operator|.
name|env
operator|=
name|penv
expr_stmt|;
name|JavaModel
name|javaModel
init|=
name|env
operator|.
name|get
argument_list|(
name|JavaModel
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|passthrough
argument_list|()
condition|)
block|{
return|return;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|JavaInterface
argument_list|>
name|interfaces
init|=
name|javaModel
operator|.
name|getInterfaces
argument_list|()
decl_stmt|;
for|for
control|(
name|JavaInterface
name|intf
range|:
name|interfaces
operator|.
name|values
argument_list|()
control|)
block|{
name|clearAttributes
argument_list|()
expr_stmt|;
name|setAttributes
argument_list|(
literal|"intf"
argument_list|,
name|intf
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"seiClass"
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|SEI_CLASS
argument_list|)
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"address"
argument_list|,
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_ADDRESS
argument_list|)
argument_list|)
expr_stmt|;
name|setCommonAttributes
argument_list|()
expr_stmt|;
name|doWrite
argument_list|(
name|CLIENT_TEMPLATE
argument_list|,
name|parseOutputName
argument_list|(
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|intf
operator|.
name|getName
argument_list|()
operator|+
literal|"Client"
argument_list|)
argument_list|)
expr_stmt|;
name|env
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CLIENT_CLASS
argument_list|,
name|intf
operator|.
name|getFullClassName
argument_list|()
operator|+
literal|"Client"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

