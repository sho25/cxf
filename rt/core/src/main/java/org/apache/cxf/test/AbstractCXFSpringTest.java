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
name|test
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
name|BusException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|DefaultResourceLoader
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
name|Resource
import|;
end_import

begin_comment
comment|/**  * Base class for tests that use a Spring bean specification to load up components for testing.  * Unlike the classes that come with Spring, it doesn't drag in the JUnit 3 hierarchy, and it   * doesn't inject into the test itself from the beans.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCXFSpringTest
extends|extends
name|AbstractCXFTest
block|{
comment|// subvert JUnit. We want to set up the application context ONCE, since it
comment|// is likely to include a Jetty or something else that we can't get rid of.
specifier|private
specifier|static
name|GenericApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
name|DefaultResourceLoader
name|resourceLoader
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|configContextClass
init|=
name|AbstractCXFSpringTest
operator|.
name|class
decl_stmt|;
comment|/**      * Load up all the beans from the XML files returned by the getConfigLocations method.      * @throws Exception       */
specifier|protected
name|AbstractCXFSpringTest
parameter_list|()
block|{     }
annotation|@
name|Before
specifier|public
name|void
name|setupBeans
parameter_list|()
throws|throws
name|Exception
block|{
if|if
condition|(
name|applicationContext
operator|!=
literal|null
condition|)
block|{
return|return;
block|}
name|applicationContext
operator|=
operator|new
name|GenericApplicationContext
argument_list|()
expr_stmt|;
name|resourceLoader
operator|=
operator|new
name|DefaultResourceLoader
argument_list|(
name|configContextClass
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|beanDefinitionPath
range|:
name|getConfigLocations
argument_list|()
control|)
block|{
name|XmlBeanDefinitionReader
name|reader
init|=
operator|new
name|XmlBeanDefinitionReader
argument_list|(
name|applicationContext
argument_list|)
decl_stmt|;
name|Resource
name|resource
init|=
name|resourceLoader
operator|.
name|getResource
argument_list|(
name|beanDefinitionPath
argument_list|)
decl_stmt|;
name|reader
operator|.
name|loadBeanDefinitions
argument_list|(
name|resource
argument_list|)
expr_stmt|;
block|}
name|additionalSpringConfiguration
argument_list|(
name|applicationContext
argument_list|)
expr_stmt|;
name|applicationContext
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|super
operator|.
name|setUpBus
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUpBus
parameter_list|()
throws|throws
name|Exception
block|{
comment|// override the super before method
block|}
specifier|public
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
return|return
name|getBean
argument_list|(
name|Bus
operator|.
name|class
argument_list|,
literal|"cxf"
argument_list|)
return|;
block|}
annotation|@
name|After
specifier|public
name|void
name|teardownBeans
parameter_list|()
block|{
name|applicationContext
operator|.
name|close
argument_list|()
expr_stmt|;
name|applicationContext
operator|.
name|destroy
argument_list|()
expr_stmt|;
name|applicationContext
operator|=
literal|null
expr_stmt|;
block|}
comment|/**      * Return an array of resource specifications.       * @see org.springframework.core.io.DefaultResourceLoader for the syntax.      * @return array of resource specifications.      */
specifier|protected
specifier|abstract
name|String
index|[]
name|getConfigLocations
parameter_list|()
function_decl|;
specifier|protected
name|ApplicationContext
name|getApplicationContext
parameter_list|()
block|{
return|return
name|applicationContext
return|;
block|}
comment|/**      * subclasses may override this.      * @param context      * @throws Exception       */
specifier|protected
name|void
name|additionalSpringConfiguration
parameter_list|(
name|GenericApplicationContext
name|context
parameter_list|)
throws|throws
name|Exception
block|{
comment|//default - do nothing
block|}
comment|/**      * Convenience method for the common case of retrieving a bean from the context.      * One would expect Spring to have this.       * @param<T> Type of the bean object.      * @param type Type of the bean object.      * @param beanName ID of the bean.      * @return The Bean.      */
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|getBean
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|String
name|beanName
parameter_list|)
block|{
return|return
name|type
operator|.
name|cast
argument_list|(
name|applicationContext
operator|.
name|getBean
argument_list|(
name|beanName
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|setConfigContextClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|configContextClass
parameter_list|)
block|{
name|this
operator|.
name|configContextClass
operator|=
name|configContextClass
expr_stmt|;
block|}
block|}
end_class

end_unit

