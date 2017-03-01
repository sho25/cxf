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
package|;
end_package

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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|databinding
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
name|frontend
operator|.
name|AbstractServiceFactory
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
name|processor
operator|.
name|FrontendFactory
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
name|NameUtil
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
name|FileSystemResource
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
name|UrlResource
import|;
end_import

begin_comment
comment|/**  * This class constructs ServiceBuilder objects using Spring. These objects are used to access the services  * and the data bindings to generate the wsdl.  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|SpringServiceBuilderFactory
extends|extends
name|ServiceBuilderFactory
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|beanDefinitions
decl_stmt|;
specifier|public
name|SpringServiceBuilderFactory
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|beanDefinitions
parameter_list|)
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|beanDefinitions
operator|=
name|beanDefinitions
expr_stmt|;
block|}
specifier|public
name|SpringServiceBuilderFactory
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|beanDefinitions
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
comment|/**      * Convert a parameter value to the name of a bean we'd use for a data binding.      *      * @param databindingName      * @return      */
specifier|public
specifier|static
name|String
name|databindingNameToBeanName
parameter_list|(
name|String
name|dbName
parameter_list|)
block|{
return|return
name|NameUtil
operator|.
name|capitalize
argument_list|(
name|dbName
operator|.
name|toLowerCase
argument_list|()
argument_list|)
operator|+
name|ToolConstants
operator|.
name|DATABIND_BEAN_NAME_SUFFIX
return|;
block|}
annotation|@
name|Override
specifier|public
name|ServiceBuilder
name|newBuilder
parameter_list|(
name|FrontendFactory
operator|.
name|Style
name|s
parameter_list|)
block|{
name|ApplicationContext
name|applicationContext
init|=
name|getApplicationContext
argument_list|(
name|beanDefinitions
argument_list|)
decl_stmt|;
name|DataBinding
name|dataBinding
decl_stmt|;
name|String
name|databindingBeanName
init|=
name|databindingNameToBeanName
argument_list|(
name|databindingName
argument_list|)
decl_stmt|;
try|try
block|{
name|dataBinding
operator|=
operator|(
name|DataBinding
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
name|databindingBeanName
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"Cannot get databinding bean "
operator|+
name|databindingBeanName
operator|+
literal|" for databinding "
operator|+
name|databindingName
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|String
name|beanName
init|=
name|getBuilderBeanName
argument_list|(
name|s
argument_list|)
decl_stmt|;
name|ServiceBuilder
name|builder
init|=
literal|null
decl_stmt|;
try|try
block|{
name|builder
operator|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|,
name|ServiceBuilder
operator|.
name|class
argument_list|)
expr_stmt|;
name|AbstractServiceFactory
name|serviceFactory
init|=
operator|(
name|AbstractServiceFactory
operator|)
name|builder
decl_stmt|;
name|serviceFactory
operator|.
name|setDataBinding
argument_list|(
name|dataBinding
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RuntimeException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ToolException
argument_list|(
literal|"Can not get ServiceBuilder bean "
operator|+
name|beanName
operator|+
literal|"to initialize the ServiceBuilder for style: "
operator|+
name|s
operator|+
literal|" Reason: \n"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
name|builder
operator|.
name|setServiceClass
argument_list|(
name|serviceClass
argument_list|)
expr_stmt|;
return|return
name|builder
return|;
block|}
comment|/**      * Return the name of a prototype bean from Spring that can provide the service. The use of a bean allows      * for the possibility of an override.      *      * @param s Style of service      * @return name of bean.      */
specifier|protected
name|String
name|getBuilderBeanName
parameter_list|(
name|FrontendFactory
operator|.
name|Style
name|s
parameter_list|)
block|{
return|return
name|s
operator|+
literal|"ServiceBuilderBean"
return|;
block|}
comment|/**      * This is factored out to permit use in a unit test.      *      * @param bus      * @return      */
specifier|public
specifier|static
name|ApplicationContext
name|getApplicationContext
parameter_list|(
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
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
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
name|List
argument_list|<
name|URL
argument_list|>
name|urls
init|=
name|ClassLoaderUtils
operator|.
name|getResources
argument_list|(
literal|"META-INF/cxf/java2wsbeans.xml"
argument_list|,
name|SpringServiceBuilderFactory
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|URL
name|url
range|:
name|urls
control|)
block|{
name|reader
operator|.
name|loadBeanDefinitions
argument_list|(
operator|new
name|UrlResource
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
name|appContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
return|return
name|appContext
return|;
block|}
specifier|public
name|void
name|setBeanDefinitions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|beanDefinitions
parameter_list|)
block|{
name|this
operator|.
name|beanDefinitions
operator|=
name|beanDefinitions
expr_stmt|;
block|}
block|}
end_class

end_unit

