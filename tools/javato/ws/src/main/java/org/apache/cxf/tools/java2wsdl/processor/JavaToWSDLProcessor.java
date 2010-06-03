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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
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
name|BindingType
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
name|soap
operator|.
name|SOAPBinding
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
name|BusFactory
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
name|bus
operator|.
name|spring
operator|.
name|BusApplicationContext
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
name|WSDLConstants
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
name|common
operator|.
name|logging
operator|.
name|LogUtils
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
name|ServiceBuilder
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
name|EndpointInfo
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
name|Processor
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
name|java2wsdl
operator|.
name|generator
operator|.
name|AbstractGenerator
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
name|java2wsdl
operator|.
name|generator
operator|.
name|WSDLGeneratorFactory
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
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|FaultBeanGenerator
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
name|java2wsdl
operator|.
name|generator
operator|.
name|wsdl11
operator|.
name|WrapperBeanGenerator
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
name|java2wsdl
operator|.
name|processor
operator|.
name|internal
operator|.
name|ServiceBuilderFactory
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
name|AnnotationUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|BeanDefinitionStoreException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanDefinitionReader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|support
operator|.
name|GenericApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|ClassPathResource
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|core
operator|.
name|io
operator|.
name|FileSystemResource
import|;
end_import

begin_class
specifier|public
class|class
name|JavaToWSDLProcessor
implements|implements
name|Processor
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|JavaToWSDLProcessor
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_ADDRESS
init|=
literal|"http://localhost:9090/hello"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JAVA_CLASS_PATH
init|=
literal|"java.class.path"
decl_stmt|;
specifier|private
name|ToolContext
name|context
decl_stmt|;
specifier|private
specifier|final
name|List
argument_list|<
name|AbstractGenerator
argument_list|>
name|generators
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractGenerator
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|void
name|customize
parameter_list|(
name|ServiceInfo
name|service
parameter_list|)
block|{
if|if
condition|(
name|context
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_TNS
argument_list|)
condition|)
block|{
name|String
name|ns
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_TNS
argument_list|)
decl_stmt|;
name|service
operator|.
name|setTargetNamespace
argument_list|(
name|ns
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORT
argument_list|)
condition|)
block|{
name|String
name|portName
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_PORT
argument_list|)
decl_stmt|;
name|EndpointInfo
name|einfo
init|=
name|service
operator|.
name|getEndpoints
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|QName
name|qn
init|=
operator|new
name|QName
argument_list|(
name|einfo
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|portName
argument_list|)
decl_stmt|;
name|einfo
operator|.
name|setName
argument_list|(
name|qn
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVICENAME
argument_list|)
condition|)
block|{
name|String
name|svName
init|=
name|getServiceName
argument_list|()
decl_stmt|;
name|service
operator|.
name|setName
argument_list|(
operator|new
name|QName
argument_list|(
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|svName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|EndpointInfo
name|endpointInfo
init|=
name|service
operator|.
name|getEndpoints
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|address
init|=
name|ToolConstants
operator|.
name|DEFAULT_ADDRESS
operator|+
literal|"/"
operator|+
name|endpointInfo
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_ADDRESS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|address
operator|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_ADDRESS
argument_list|)
expr_stmt|;
block|}
name|endpointInfo
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|CFG_ADDRESS
argument_list|,
name|address
argument_list|)
expr_stmt|;
block|}
comment|/**      * This is factored out to permit use in a unit test.      * @param bus      * @return      */
specifier|public
specifier|static
name|ApplicationContext
name|getApplicationContext
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|additionalFilePathnames
parameter_list|)
block|{
name|BusApplicationContext
name|busApplicationContext
init|=
name|bus
operator|.
name|getExtension
argument_list|(
name|BusApplicationContext
operator|.
name|class
argument_list|)
decl_stmt|;
name|GenericApplicationContext
name|appContext
init|=
operator|new
name|GenericApplicationContext
argument_list|(
name|busApplicationContext
argument_list|)
decl_stmt|;
name|XmlBeanDefinitionReader
name|reader
init|=
operator|new
name|XmlBeanDefinitionReader
argument_list|(
name|appContext
argument_list|)
decl_stmt|;
name|reader
operator|.
name|loadBeanDefinitions
argument_list|(
operator|new
name|ClassPathResource
argument_list|(
literal|"META-INF/cxf/java2wsbeans.xml"
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|pathname
range|:
name|additionalFilePathnames
control|)
block|{
try|try
block|{
name|reader
operator|.
name|loadBeanDefinitions
argument_list|(
operator|new
name|FileSystemResource
argument_list|(
name|pathname
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|BeanDefinitionStoreException
name|bdse
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"Unable to open bean definition file "
operator|+
name|pathname
argument_list|,
name|bdse
operator|.
name|getCause
argument_list|()
argument_list|)
throw|;
block|}
block|}
return|return
name|appContext
return|;
block|}
specifier|public
name|void
name|process
parameter_list|()
throws|throws
name|ToolException
block|{
name|String
name|oldClassPath
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|JAVA_CLASS_PATH
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"OLD_CP"
argument_list|,
name|oldClassPath
argument_list|)
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSPATH
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|newCp
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSPATH
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JAVA_CLASS_PATH
argument_list|,
name|newCp
operator|+
name|File
operator|.
name|pathSeparator
operator|+
name|oldClassPath
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"NEW_CP"
argument_list|,
name|newCp
argument_list|)
expr_stmt|;
block|}
comment|// check for command line specification of data binding.
name|ServiceBuilder
name|builder
init|=
name|getServiceBuilder
argument_list|()
decl_stmt|;
name|ServiceInfo
name|service
init|=
name|builder
operator|.
name|createService
argument_list|()
decl_stmt|;
name|customize
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|File
name|wsdlFile
init|=
name|getOutputFile
argument_list|(
name|builder
operator|.
name|getOutputFile
argument_list|()
argument_list|,
name|service
operator|.
name|getName
argument_list|()
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|".wsdl"
argument_list|)
decl_stmt|;
name|File
name|outputDir
init|=
name|getOutputDir
argument_list|(
name|wsdlFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDL
argument_list|)
condition|)
block|{
name|generators
operator|.
name|add
argument_list|(
name|getWSDLGenerator
argument_list|(
name|wsdlFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|context
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_WRAPPERBEAN
argument_list|)
condition|)
block|{
name|generators
operator|.
name|add
argument_list|(
name|getWrapperBeanGenerator
argument_list|()
argument_list|)
expr_stmt|;
name|generators
operator|.
name|add
argument_list|(
name|getFaultBeanGenerator
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|generate
argument_list|(
name|service
argument_list|,
name|outputDir
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ServiceInfo
argument_list|>
name|serviceList
init|=
operator|new
name|ArrayList
argument_list|<
name|ServiceInfo
argument_list|>
argument_list|()
decl_stmt|;
name|serviceList
operator|.
name|add
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|SERVICE_LIST
argument_list|,
name|serviceList
argument_list|)
expr_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
name|JAVA_CLASS_PATH
argument_list|,
name|oldClassPath
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|log
argument_list|(
name|Level
operator|.
name|FINE
argument_list|,
literal|"RESUME_CP"
argument_list|,
name|oldClassPath
argument_list|)
expr_stmt|;
block|}
specifier|private
name|AbstractGenerator
name|getWrapperBeanGenerator
parameter_list|()
block|{
name|WrapperBeanGenerator
name|generator
init|=
operator|new
name|WrapperBeanGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setOutputBase
argument_list|(
name|getSourceDir
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setCompileToDir
argument_list|(
name|getClassesDir
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|generator
return|;
block|}
specifier|private
name|AbstractGenerator
name|getFaultBeanGenerator
parameter_list|()
block|{
name|FaultBeanGenerator
name|generator
init|=
operator|new
name|FaultBeanGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setOutputBase
argument_list|(
name|getSourceDir
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setCompileToDir
argument_list|(
name|getClassesDir
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|generator
return|;
block|}
specifier|private
name|AbstractGenerator
name|getWSDLGenerator
parameter_list|(
specifier|final
name|File
name|wsdlFile
parameter_list|)
block|{
name|WSDLGeneratorFactory
name|factory
init|=
operator|new
name|WSDLGeneratorFactory
argument_list|()
decl_stmt|;
name|factory
operator|.
name|setWSDLVersion
argument_list|(
name|getWSDLVersion
argument_list|()
argument_list|)
expr_stmt|;
name|AbstractGenerator
name|generator
init|=
name|factory
operator|.
name|newGenerator
argument_list|()
decl_stmt|;
name|generator
operator|.
name|setAllowImports
argument_list|(
name|context
operator|.
name|containsKey
argument_list|(
name|ToolConstants
operator|.
name|CFG_CREATE_XSD_IMPORTS
argument_list|)
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setOutputBase
argument_list|(
name|wsdlFile
argument_list|)
expr_stmt|;
return|return
name|generator
return|;
block|}
specifier|public
name|void
name|generate
parameter_list|(
name|ServiceInfo
name|service
parameter_list|,
name|File
name|output
parameter_list|)
throws|throws
name|ToolException
block|{
for|for
control|(
name|AbstractGenerator
name|generator
range|:
name|generators
control|)
block|{
name|generator
operator|.
name|setServiceModel
argument_list|(
name|service
argument_list|)
expr_stmt|;
name|generator
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|generator
operator|.
name|generate
argument_list|(
name|output
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|ServiceBuilder
name|getServiceBuilder
parameter_list|()
throws|throws
name|ToolException
block|{
name|Object
name|beanFilesParameter
init|=
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_BEAN_CONFIG
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|beanDefinitions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|beanFilesParameter
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|beanFilesParameter
operator|instanceof
name|String
condition|)
block|{
name|beanDefinitions
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|beanFilesParameter
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|beanFilesParameter
operator|instanceof
name|List
condition|)
block|{
comment|// is there a better way to avoid the warning?
name|beanDefinitions
operator|.
name|addAll
argument_list|(
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|beanFilesParameter
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|String
name|list
index|[]
init|=
operator|(
name|String
index|[]
operator|)
name|beanFilesParameter
decl_stmt|;
for|for
control|(
name|String
name|b
range|:
name|list
control|)
block|{
name|beanDefinitions
operator|.
name|add
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|applicationContext
operator|=
name|getApplicationContext
argument_list|(
name|getBus
argument_list|()
argument_list|,
name|beanDefinitions
argument_list|)
expr_stmt|;
name|ServiceBuilderFactory
name|builderFactory
init|=
name|ServiceBuilderFactory
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|clz
init|=
name|getServiceClass
argument_list|()
decl_stmt|;
name|context
operator|.
name|put
argument_list|(
name|Class
operator|.
name|class
argument_list|,
name|clz
argument_list|)
expr_stmt|;
if|if
condition|(
name|clz
operator|.
name|isInterface
argument_list|()
condition|)
block|{
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|GEN_FROM_SEI
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|SEI_CLASS
argument_list|,
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|IMPL_CLASS
argument_list|,
name|clz
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|clz
operator|.
name|getInterfaces
argument_list|()
operator|.
name|length
operator|==
literal|1
condition|)
block|{
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|SEI_CLASS
argument_list|,
name|clz
operator|.
name|getInterfaces
argument_list|()
index|[
literal|0
index|]
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//TODO: if it is simple frontend, and the impl class implments
comment|//multiple interfaces
name|context
operator|.
name|put
argument_list|(
name|ToolConstants
operator|.
name|GEN_FROM_SEI
argument_list|,
name|Boolean
operator|.
name|FALSE
argument_list|)
expr_stmt|;
block|}
name|builderFactory
operator|.
name|setServiceClass
argument_list|(
name|clz
argument_list|)
expr_stmt|;
name|builderFactory
operator|.
name|setDatabindingName
argument_list|(
name|getDataBindingName
argument_list|()
argument_list|)
expr_stmt|;
comment|// The service class determines the frontend, so no need to pass it in twice.
name|ServiceBuilder
name|builder
init|=
name|builderFactory
operator|.
name|newBuilder
argument_list|(
name|applicationContext
argument_list|)
decl_stmt|;
name|builder
operator|.
name|validate
argument_list|()
expr_stmt|;
if|if
condition|(
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_ADDRESS
argument_list|)
operator|!=
literal|null
condition|)
block|{
name|String
name|address
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_ADDRESS
argument_list|)
decl_stmt|;
name|builder
operator|.
name|setAddress
argument_list|(
name|address
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|setAddress
argument_list|(
name|DEFAULT_ADDRESS
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|setTransportId
argument_list|(
name|getTransportId
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBus
argument_list|(
name|getBus
argument_list|()
argument_list|)
expr_stmt|;
name|builder
operator|.
name|setBindingId
argument_list|(
name|getBindingId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
specifier|protected
name|String
name|getTransportId
parameter_list|()
block|{
if|if
condition|(
name|isSOAP12
argument_list|()
condition|)
block|{
return|return
name|WSDLConstants
operator|.
name|NS_SOAP12
return|;
block|}
return|return
name|WSDLConstants
operator|.
name|NS_SOAP11
return|;
block|}
specifier|protected
name|String
name|getBindingId
parameter_list|()
block|{
if|if
condition|(
name|isSOAP12
argument_list|()
condition|)
block|{
return|return
name|WSDLConstants
operator|.
name|NS_SOAP12
return|;
block|}
else|else
block|{
return|return
name|WSDLConstants
operator|.
name|NS_SOAP11
return|;
block|}
block|}
specifier|protected
name|boolean
name|isSOAP12
parameter_list|()
block|{
if|if
condition|(
operator|!
name|this
operator|.
name|context
operator|.
name|optionSet
argument_list|(
name|ToolConstants
operator|.
name|CFG_SOAP12
argument_list|)
condition|)
block|{
name|BindingType
name|bType
init|=
name|getServiceClass
argument_list|()
operator|.
name|getAnnotation
argument_list|(
name|BindingType
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|bType
operator|!=
literal|null
condition|)
block|{
return|return
name|SOAPBinding
operator|.
name|SOAP12HTTP_BINDING
operator|.
name|equals
argument_list|(
name|bType
operator|.
name|value
argument_list|()
argument_list|)
return|;
block|}
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|protected
name|File
name|getOutputDir
parameter_list|(
name|File
name|wsdlLocation
parameter_list|)
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|wsdlLocation
operator|==
literal|null
operator|||
name|wsdlLocation
operator|.
name|getParentFile
argument_list|()
operator|==
literal|null
operator|||
operator|!
name|wsdlLocation
operator|.
name|getParentFile
argument_list|()
operator|.
name|exists
argument_list|()
condition|)
block|{
name|dir
operator|=
literal|"./"
expr_stmt|;
block|}
else|else
block|{
name|dir
operator|=
name|wsdlLocation
operator|.
name|getParent
argument_list|()
expr_stmt|;
block|}
block|}
return|return
operator|new
name|File
argument_list|(
name|dir
argument_list|)
return|;
block|}
specifier|protected
name|File
name|getOutputFile
parameter_list|(
name|File
name|nameFromClz
parameter_list|,
name|String
name|defaultOutputFile
parameter_list|)
block|{
name|String
name|output
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTFILE
argument_list|)
decl_stmt|;
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_OUTPUTDIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|==
literal|null
condition|)
block|{
name|dir
operator|=
literal|"./"
expr_stmt|;
block|}
name|File
name|result
decl_stmt|;
if|if
condition|(
name|output
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
operator|new
name|File
argument_list|(
name|output
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|result
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|result
operator|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|)
argument_list|,
name|output
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|result
operator|=
operator|new
name|File
argument_list|(
operator|new
name|File
argument_list|(
name|dir
argument_list|)
argument_list|,
name|defaultOutputFile
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|nameFromClz
operator|!=
literal|null
condition|)
block|{
name|result
operator|=
name|nameFromClz
expr_stmt|;
block|}
comment|// rename the exising wsdl file
if|if
condition|(
name|result
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|result
operator|.
name|renameTo
argument_list|(
operator|new
name|File
argument_list|(
name|result
operator|.
name|getParent
argument_list|()
argument_list|,
name|result
operator|.
name|getName
argument_list|()
argument_list|)
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
operator|new
name|Message
argument_list|(
literal|"OUTFILE_EXISTS"
argument_list|,
name|LOG
argument_list|)
argument_list|)
throw|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|getServiceClass
parameter_list|()
block|{
return|return
name|AnnotationUtil
operator|.
name|loadClass
argument_list|(
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSNAME
argument_list|)
argument_list|,
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|WSDLConstants
operator|.
name|WSDLVersion
name|getWSDLVersion
parameter_list|()
block|{
name|String
name|version
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_WSDL_VERSION
argument_list|)
decl_stmt|;
name|WSDLConstants
operator|.
name|WSDLVersion
name|wsVersion
init|=
name|WSDLConstants
operator|.
name|getVersion
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|wsVersion
operator|==
name|WSDLConstants
operator|.
name|WSDLVersion
operator|.
name|UNKNOWN
condition|)
block|{
name|wsVersion
operator|=
name|WSDLConstants
operator|.
name|WSDLVersion
operator|.
name|WSDL11
expr_stmt|;
block|}
return|return
name|wsVersion
return|;
block|}
specifier|public
name|String
name|getServiceName
parameter_list|()
block|{
return|return
operator|(
name|String
operator|)
name|this
operator|.
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SERVICENAME
argument_list|)
return|;
block|}
name|File
name|getSourceDir
parameter_list|()
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|this
operator|.
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_SOURCEDIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|dir
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|File
argument_list|(
name|dir
argument_list|)
return|;
block|}
name|File
name|getClassesDir
parameter_list|()
block|{
name|String
name|dir
init|=
operator|(
name|String
operator|)
name|this
operator|.
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_CLASSDIR
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|dir
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
operator|new
name|File
argument_list|(
name|dir
argument_list|)
return|;
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
return|;
block|}
specifier|public
name|void
name|setEnvironment
parameter_list|(
name|ToolContext
name|env
parameter_list|)
block|{
name|this
operator|.
name|context
operator|=
name|env
expr_stmt|;
block|}
specifier|public
name|ToolContext
name|getEnvironment
parameter_list|()
block|{
return|return
name|this
operator|.
name|context
return|;
block|}
specifier|public
name|String
name|getDataBindingName
parameter_list|()
block|{
name|String
name|databindingName
init|=
operator|(
name|String
operator|)
name|context
operator|.
name|get
argument_list|(
name|ToolConstants
operator|.
name|CFG_DATABINDING
argument_list|)
decl_stmt|;
if|if
condition|(
name|databindingName
operator|==
literal|null
condition|)
block|{
name|databindingName
operator|=
name|ToolConstants
operator|.
name|DEFAULT_DATA_BINDING_NAME
expr_stmt|;
block|}
return|return
name|databindingName
return|;
block|}
block|}
end_class

end_unit

