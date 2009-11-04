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
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|helpers
operator|.
name|CastUtils
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
name|util
operator|.
name|ClassCollector
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
name|wsdlto
operator|.
name|frontend
operator|.
name|jaxws
operator|.
name|processor
operator|.
name|WSDLToJavaProcessor
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
name|QName
argument_list|,
name|JavaModel
argument_list|>
name|map
init|=
name|CastUtils
operator|.
name|cast
argument_list|(
operator|(
name|Map
operator|)
name|penv
operator|.
name|get
argument_list|(
name|WSDLToJavaProcessor
operator|.
name|MODEL_MAP
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|JavaModel
name|javaModel
range|:
name|map
operator|.
name|values
argument_list|()
control|)
block|{
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
name|JavaServiceClass
name|service
init|=
literal|null
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
for|for
control|(
name|JavaServiceClass
name|javaservice
range|:
name|services
operator|.
name|values
argument_list|()
control|)
block|{
name|service
operator|=
name|javaservice
expr_stmt|;
for|for
control|(
name|JavaPort
name|jport
range|:
name|javaservice
operator|.
name|getPorts
argument_list|()
control|)
block|{
name|JavaInterface
name|intf
init|=
name|interfaces
operator|.
name|get
argument_list|(
name|jport
operator|.
name|getInterfaceClass
argument_list|()
argument_list|)
decl_stmt|;
name|outputImpl
argument_list|(
name|intf
argument_list|,
name|service
argument_list|,
name|jport
operator|.
name|getPortName
argument_list|()
argument_list|,
name|penv
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
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
name|outputImpl
argument_list|(
name|intf
argument_list|,
name|service
argument_list|,
literal|""
argument_list|,
name|penv
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|outputImpl
parameter_list|(
name|JavaInterface
name|intf
parameter_list|,
name|JavaServiceClass
name|service
parameter_list|,
name|String
name|port
parameter_list|,
name|ToolContext
name|penv
parameter_list|)
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
name|String
name|name
init|=
name|intf
operator|.
name|getName
argument_list|()
operator|+
literal|"Impl"
decl_stmt|;
name|name
operator|=
name|mapClassName
argument_list|(
name|intf
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|name
argument_list|,
name|penv
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"implName"
argument_list|,
name|name
argument_list|)
expr_stmt|;
name|penv
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_IMPL_CLASS
argument_list|,
name|name
argument_list|)
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
name|name
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|mapClassName
parameter_list|(
name|String
name|packageName
parameter_list|,
name|String
name|name
parameter_list|,
name|ToolContext
name|context
parameter_list|)
block|{
name|ClassCollector
name|collector
init|=
name|context
operator|.
name|get
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|)
decl_stmt|;
name|int
name|count
init|=
literal|0
decl_stmt|;
name|String
name|checkName
init|=
name|name
decl_stmt|;
while|while
condition|(
name|collector
operator|.
name|containImplClass
argument_list|(
name|packageName
argument_list|,
name|checkName
argument_list|)
condition|)
block|{
name|checkName
operator|=
name|name
operator|+
operator|(
operator|++
name|count
operator|)
expr_stmt|;
block|}
name|collector
operator|.
name|addImplClassName
argument_list|(
name|packageName
argument_list|,
name|checkName
argument_list|,
name|packageName
operator|+
literal|"."
operator|+
name|checkName
argument_list|)
expr_stmt|;
return|return
name|checkName
return|;
block|}
block|}
end_class

end_unit

