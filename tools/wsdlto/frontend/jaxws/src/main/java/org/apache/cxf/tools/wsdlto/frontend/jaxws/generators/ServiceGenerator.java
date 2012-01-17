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
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|Service
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
name|ServiceGenerator
extends|extends
name|AbstractJAXWSGenerator
block|{
specifier|private
specifier|static
specifier|final
name|String
name|SERVICE_TEMPLATE
init|=
name|TEMPLATE_BASE
operator|+
literal|"/service.vm"
decl_stmt|;
specifier|public
name|ServiceGenerator
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
name|ToolConstants
operator|.
name|SERVICE_GENERATOR
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
name|CFG_GEN_SERVICE
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
name|ClassCollector
name|collector
init|=
name|penv
operator|.
name|get
argument_list|(
name|ClassCollector
operator|.
name|class
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|JavaServiceClass
argument_list|>
name|serviceClasses
init|=
name|javaModel
operator|.
name|getServiceClasses
argument_list|()
decl_stmt|;
if|if
condition|(
name|serviceClasses
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
literal|"CAN_NOT_GEN_SERVICE"
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
name|JavaServiceClass
name|js
range|:
name|serviceClasses
operator|.
name|values
argument_list|()
control|)
block|{
if|if
condition|(
name|js
operator|.
name|getHandlerChains
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|HandlerConfigGenerator
name|handlerGen
init|=
operator|new
name|HandlerConfigGenerator
argument_list|()
decl_stmt|;
name|handlerGen
operator|.
name|setJavaInterface
argument_list|(
name|js
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
name|js
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
name|js
operator|.
name|addAnnotation
argument_list|(
name|annot
argument_list|)
expr_stmt|;
name|js
operator|.
name|addImport
argument_list|(
literal|"javax.jws.HandlerChain"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
for|for
control|(
name|JavaPort
name|port
range|:
name|js
operator|.
name|getPorts
argument_list|()
control|)
block|{
if|if
condition|(
operator|!
name|port
operator|.
name|getPackageName
argument_list|()
operator|.
name|equals
argument_list|(
name|js
operator|.
name|getPackageName
argument_list|()
argument_list|)
operator|&&
operator|!
name|port
operator|.
name|getInterfaceClass
argument_list|()
operator|.
name|equals
argument_list|(
name|js
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|js
operator|.
name|addImport
argument_list|(
name|port
operator|.
name|getFullClassName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|String
name|url
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLURL
argument_list|)
decl_stmt|;
name|String
name|location
init|=
operator|(
name|String
operator|)
name|env
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDLLOCATION
argument_list|)
decl_stmt|;
if|if
condition|(
name|location
operator|==
literal|null
condition|)
block|{
name|location
operator|=
name|url
expr_stmt|;
block|}
name|String
name|serviceSuperclass
init|=
literal|"Service"
decl_stmt|;
for|for
control|(
name|String
name|s
range|:
name|collector
operator|.
name|getGeneratedFileInfo
argument_list|()
control|)
block|{
if|if
condition|(
name|s
operator|.
name|equals
argument_list|(
name|js
operator|.
name|getPackageName
argument_list|()
operator|+
literal|".Service"
argument_list|)
condition|)
block|{
name|serviceSuperclass
operator|=
literal|"javax.xml.ws.Service"
expr_stmt|;
block|}
block|}
name|clearAttributes
argument_list|()
expr_stmt|;
name|boolean
name|useGetResource
init|=
literal|false
decl_stmt|;
try|try
block|{
operator|new
name|URL
argument_list|(
name|location
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MalformedURLException
name|e
parameter_list|)
block|{
name|useGetResource
operator|=
literal|true
expr_stmt|;
block|}
name|setAttributes
argument_list|(
literal|"service"
argument_list|,
name|js
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"wsdlLocation"
argument_list|,
name|location
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"useGetResource"
argument_list|,
name|useGetResource
argument_list|)
expr_stmt|;
name|setAttributes
argument_list|(
literal|"serviceSuperclass"
argument_list|,
name|serviceSuperclass
argument_list|)
expr_stmt|;
if|if
condition|(
literal|"Service"
operator|.
name|equals
argument_list|(
name|serviceSuperclass
argument_list|)
condition|)
block|{
name|js
operator|.
name|addImport
argument_list|(
literal|"javax.xml.ws.Service"
argument_list|)
expr_stmt|;
block|}
name|setAttributes
argument_list|(
literal|"wsdlUrl"
argument_list|,
name|url
argument_list|)
expr_stmt|;
name|setCommonAttributes
argument_list|()
expr_stmt|;
if|if
condition|(
name|isJaxws22
argument_list|()
condition|)
block|{
name|setAttributes
argument_list|(
literal|"jaxws22"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
name|doWrite
argument_list|(
name|SERVICE_TEMPLATE
argument_list|,
name|parseOutputName
argument_list|(
name|js
operator|.
name|getPackageName
argument_list|()
argument_list|,
name|js
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
name|boolean
name|isJaxws22
parameter_list|()
block|{
return|return
name|Service
operator|.
name|class
operator|.
name|getDeclaredConstructors
argument_list|()
operator|.
name|length
operator|==
literal|2
return|;
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
name|addServiceClassName
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

