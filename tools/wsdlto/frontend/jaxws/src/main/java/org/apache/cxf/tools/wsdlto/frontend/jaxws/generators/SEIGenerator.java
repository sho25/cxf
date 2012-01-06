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
name|Map
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jws
operator|.
name|HandlerChain
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
name|annotations
operator|.
name|DataBinding
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
name|JAnnotation
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
name|JAnnotationElement
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
name|SEIGenerator
extends|extends
name|AbstractJAXWSGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SEI_TEMPLATE
init|=
name|TEMPLATE_BASE
operator|+
literal|"/sei.vm"
decl_stmt|;
specifier|public
name|SEIGenerator
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
name|ToolConstants
operator|.
name|SEI_GENERATOR
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
name|CFG_GEN_SEI
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
name|CFG_GEN_IMPL
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
literal|false
return|;
block|}
specifier|private
name|boolean
name|hasHandlerConfig
parameter_list|(
name|JavaInterface
name|intf
parameter_list|)
block|{
comment|// TODO : enbale handler chain
return|return
name|intf
operator|.
name|getHandlerChains
argument_list|()
operator|!=
literal|null
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
argument_list|<
name|?
argument_list|,
name|?
argument_list|>
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
if|if
condition|(
name|interfaces
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
literal|"CAN_NOT_GEN_SEI"
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
if|if
condition|(
name|hasHandlerConfig
argument_list|(
name|intf
argument_list|)
condition|)
block|{
name|HandlerConfigGenerator
name|handlerGen
init|=
operator|new
name|HandlerConfigGenerator
argument_list|()
decl_stmt|;
comment|// REVISIT: find a better way to handle Handler gen, should not
comment|// pass JavaInterface around.
name|handlerGen
operator|.
name|setJavaInterface
argument_list|(
name|intf
argument_list|)
expr_stmt|;
name|handlerGen
operator|.
name|generate
argument_list|(
name|getEnvironment
argument_list|()
argument_list|)
expr_stmt|;
name|JAnnotation
name|annot
init|=
name|handlerGen
operator|.
name|getHandlerAnnotation
argument_list|()
decl_stmt|;
if|if
condition|(
name|handlerGen
operator|.
name|getHandlerAnnotation
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|boolean
name|existHandlerAnno
init|=
literal|false
decl_stmt|;
for|for
control|(
name|JAnnotation
name|jann
range|:
name|intf
operator|.
name|getAnnotations
argument_list|()
control|)
block|{
if|if
condition|(
name|jann
operator|.
name|getType
argument_list|()
operator|==
name|HandlerChain
operator|.
name|class
condition|)
block|{
name|existHandlerAnno
operator|=
literal|true
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|existHandlerAnno
condition|)
block|{
name|intf
operator|.
name|addAnnotation
argument_list|(
name|annot
argument_list|)
expr_stmt|;
name|intf
operator|.
name|addImport
argument_list|(
literal|"javax.jws.HandlerChain"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|penv
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|RUNTIME_DATABINDING_CLASS
argument_list|)
condition|)
block|{
name|JAnnotation
name|ann
init|=
operator|new
name|JAnnotation
argument_list|(
name|DataBinding
operator|.
name|class
argument_list|)
decl_stmt|;
name|JAnnotationElement
name|el
init|=
operator|new
name|JAnnotationElement
argument_list|(
literal|null
argument_list|,
name|penv
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|RUNTIME_DATABINDING_CLASS
argument_list|)
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ann
operator|.
name|addElement
argument_list|(
name|el
argument_list|)
expr_stmt|;
name|intf
operator|.
name|addAnnotation
argument_list|(
name|ann
argument_list|)
expr_stmt|;
block|}
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
name|setCommonAttributes
argument_list|()
expr_stmt|;
name|doWrite
argument_list|(
name|SEI_TEMPLATE
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
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|register
parameter_list|(
specifier|final
name|ClassCollector
name|collector
parameter_list|,
name|String
name|packageName
parameter_list|,
name|String
name|fileName
parameter_list|)
block|{
name|collector
operator|.
name|addSeiClassName
argument_list|(
name|packageName
argument_list|,
name|fileName
argument_list|,
name|packageName
operator|+
literal|"."
operator|+
name|fileName
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

