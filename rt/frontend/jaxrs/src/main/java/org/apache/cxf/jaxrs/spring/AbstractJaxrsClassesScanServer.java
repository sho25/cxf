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
name|jaxrs
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|annotation
operator|.
name|Annotation
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
name|Collection
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
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
name|ClasspathScanner
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
name|jaxrs
operator|.
name|JAXRSServerFactoryBean
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
name|factory
operator|.
name|ServiceConstructionException
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
name|annotation
operator|.
name|Value
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
name|config
operator|.
name|AutowireCapableBeanFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractJaxrsClassesScanServer
extends|extends
name|AbstractSpringConfigurationFactory
block|{
annotation|@
name|Value
argument_list|(
literal|"${cxf.jaxrs.classes-scan-packages}"
argument_list|)
specifier|private
name|String
name|basePackages
decl_stmt|;
specifier|protected
name|AbstractJaxrsClassesScanServer
parameter_list|()
block|{              }
specifier|protected
name|void
name|setJaxrsResources
parameter_list|(
name|JAXRSServerFactoryBean
name|factory
parameter_list|)
block|{
try|try
block|{
specifier|final
name|Map
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|Annotation
argument_list|>
argument_list|,
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
argument_list|>
name|classes
init|=
name|ClasspathScanner
operator|.
name|findClasses
argument_list|(
name|basePackages
argument_list|,
name|Provider
operator|.
name|class
argument_list|,
name|Path
operator|.
name|class
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|jaxrsServices
init|=
name|createBeansFromDiscoveredClasses
argument_list|(
name|classes
operator|.
name|get
argument_list|(
name|Path
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Object
argument_list|>
name|jaxrsProviders
init|=
name|createBeansFromDiscoveredClasses
argument_list|(
name|classes
operator|.
name|get
argument_list|(
name|Provider
operator|.
name|class
argument_list|)
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setServiceBeans
argument_list|(
name|jaxrsServices
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setProviders
argument_list|(
name|jaxrsProviders
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|ServiceConstructionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|Object
argument_list|>
name|createBeansFromDiscoveredClasses
parameter_list|(
name|Collection
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|>
name|classes
parameter_list|)
block|{
name|AutowireCapableBeanFactory
name|beanFactory
init|=
name|super
operator|.
name|applicationContext
operator|.
name|getAutowireCapableBeanFactory
argument_list|()
decl_stmt|;
specifier|final
name|List
argument_list|<
name|Object
argument_list|>
name|providers
init|=
operator|new
name|ArrayList
argument_list|<
name|Object
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
range|:
name|classes
control|)
block|{
name|Object
name|bean
init|=
literal|null
decl_stmt|;
try|try
block|{
name|bean
operator|=
name|beanFactory
operator|.
name|createBean
argument_list|(
name|clazz
argument_list|,
name|AutowireCapableBeanFactory
operator|.
name|AUTOWIRE_BY_TYPE
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|ex
parameter_list|)
block|{
name|bean
operator|=
name|beanFactory
operator|.
name|createBean
argument_list|(
name|clazz
argument_list|)
expr_stmt|;
block|}
name|providers
operator|.
name|add
argument_list|(
name|bean
argument_list|)
expr_stmt|;
block|}
return|return
name|providers
return|;
block|}
block|}
end_class

end_unit

