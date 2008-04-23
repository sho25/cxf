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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|generators
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
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
name|JavaPort
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
name|JavaServiceClass
import|;
end_import

begin_class
specifier|public
class|class
name|ImplGenerator
extends|extends
name|AbstractJAXWSGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|IMPL_TEMPLATE
init|=
name|TEMPLATE_BASE
operator|+
literal|"/impl.vm"
decl_stmt|;
specifier|public
name|ImplGenerator
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
name|ToolConstants
operator|.
name|IMPL_GENERATOR
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
name|CFG_GEN_IMPL
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_IMPL
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_ALL
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_ANT
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_TYPES
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_CLIENT
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SEI
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SERVER
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_SERVICE
argument_list|)
operator|||
name|env
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_GEN_FAULT
argument_list|)
condition|)
block|{
return|return
literal|true
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
name|Map
argument_list|<
name|String
argument_list|,
name|JavaServiceClass
argument_list|>
name|services
init|=
name|javaModel
operator|.
name|getServiceClasses
argument_list|()
decl_stmt|;
if|if
condition|(
name|services
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|ServiceInfo
name|serviceInfo
init|=
operator|(
name|ServiceInfo
operator|)
name|env
operator|.
name|get
argument_list|(
name|ServiceInfo
operator|.
name|class
argument_list|)
decl_stmt|;
name|String
name|wsdl
init|=
name|serviceInfo
operator|.
name|getDescription
argument_list|()
operator|.
name|getBaseURI
argument_list|()
decl_stmt|;
name|Message
name|msg
init|=
operator|new
name|Message
argument_list|(
literal|"CAN_NOT_GEN_IMPL"
argument_list|,
name|LOG
argument_list|,
name|wsdl
argument_list|)
decl_stmt|;
if|if
condition|(
name|penv
operator|.
name|isVerbose
argument_list|()
condition|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return;
block|}
name|JavaServiceClass
name|service
init|=
literal|null
decl_stmt|;
name|String
name|port
init|=
literal|""
decl_stmt|;
if|if
condition|(
operator|!
name|services
operator|.
name|values
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|JavaServiceClass
name|javaservice
init|=
name|services
operator|.
name|values
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|service
operator|=
name|javaservice
expr_stmt|;
if|if
condition|(
name|javaservice
operator|.
name|getPorts
argument_list|()
operator|.
name|size
argument_list|()
operator|!=
literal|0
condition|)
block|{
name|JavaPort
name|jport
init|=
operator|(
name|JavaPort
operator|)
name|javaservice
operator|.
name|getPorts
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|port
operator|=
name|jport
operator|.
name|getPortName
argument_list|()
expr_stmt|;
block|}
block|}
for|for
control|(
name|Iterator
name|iter
init|=
name|interfaces
operator|.
name|keySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iter
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|String
name|interfaceName
init|=
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
name|JavaInterface
name|intf
init|=
name|interfaces
operator|.
name|get
argument_list|(
name|interfaceName
argument_list|)
decl_stmt|;
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
literal|"service"
argument_list|,
name|service
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"port"
argument_list|,
name|port
argument_list|)
expr_stmt|;
name|setCommonAttributes
argument_list|()
expr_stmt|;
name|doWrite
argument_list|(
name|IMPL_TEMPLATE
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
literal|"Impl"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

