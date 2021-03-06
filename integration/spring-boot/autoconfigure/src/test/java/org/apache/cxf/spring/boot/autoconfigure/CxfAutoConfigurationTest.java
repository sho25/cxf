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
name|spring
operator|.
name|boot
operator|.
name|autoconfigure
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
name|endpoint
operator|.
name|Server
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
name|endpoint
operator|.
name|ServerImpl
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
name|spring
operator|.
name|boot
operator|.
name|jaxrs
operator|.
name|CustomJaxRSServer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|hamcrest
operator|.
name|Matcher
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
name|UnsatisfiedDependencyException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|ConfigurationPropertiesBindException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|bind
operator|.
name|BindException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|context
operator|.
name|properties
operator|.
name|bind
operator|.
name|validation
operator|.
name|BindValidationException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|test
operator|.
name|util
operator|.
name|TestPropertyValues
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|boot
operator|.
name|web
operator|.
name|servlet
operator|.
name|ServletRegistrationBean
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|mock
operator|.
name|web
operator|.
name|MockServletContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|util
operator|.
name|ReflectionTestUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|web
operator|.
name|context
operator|.
name|support
operator|.
name|AnnotationConfigWebApplicationContext
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
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|ExpectedException
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|MatcherAssert
operator|.
name|assertThat
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|allOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|containsString
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|equalTo
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasEntry
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasItem
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|hasProperty
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|hamcrest
operator|.
name|Matchers
operator|.
name|instanceOf
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertTrue
import|;
end_import

begin_comment
comment|/**  * Tests for {@link CxfAutoConfiguration}.  *  * @author Vedran Pavic  */
end_comment

begin_class
specifier|public
class|class
name|CxfAutoConfigurationTest
block|{
annotation|@
name|Rule
specifier|public
name|ExpectedException
name|thrown
init|=
name|ExpectedException
operator|.
name|none
argument_list|()
decl_stmt|;
specifier|private
name|AnnotationConfigWebApplicationContext
name|context
decl_stmt|;
annotation|@
name|After
specifier|public
name|void
name|close
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|context
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|context
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|defaultConfiguration
parameter_list|()
block|{
name|load
argument_list|(
name|CxfAutoConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|this
operator|.
name|context
operator|.
name|getBeansOfType
argument_list|(
name|ServletRegistrationBean
operator|.
name|class
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|customPathMustBeginWithASlash
parameter_list|()
block|{
name|this
operator|.
name|thrown
operator|.
name|expect
argument_list|(
name|UnsatisfiedDependencyException
operator|.
name|class
argument_list|)
expr_stmt|;
name|this
operator|.
name|thrown
operator|.
name|expectCause
argument_list|(
name|allOf
argument_list|(
name|instanceOf
argument_list|(
name|ConfigurationPropertiesBindException
operator|.
name|class
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"cause"
argument_list|,
name|allOf
argument_list|(
name|instanceOf
argument_list|(
name|BindException
operator|.
name|class
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"cause"
argument_list|,
name|allOf
argument_list|(
name|instanceOf
argument_list|(
name|BindValidationException
operator|.
name|class
argument_list|)
argument_list|,
name|hasProperty
argument_list|(
literal|"message"
argument_list|,
name|containsString
argument_list|(
literal|"Path must start with /"
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|load
argument_list|(
name|CxfAutoConfiguration
operator|.
name|class
argument_list|,
literal|"cxf.path=invalid"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|customPathWithTrailingSlash
parameter_list|()
block|{
name|load
argument_list|(
name|CxfAutoConfiguration
operator|.
name|class
argument_list|,
literal|"cxf.path=/valid/"
argument_list|)
expr_stmt|;
name|ServletRegistrationBean
argument_list|<
name|?
argument_list|>
name|registrationBean
init|=
name|this
operator|.
name|context
operator|.
name|getBean
argument_list|(
name|ServletRegistrationBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Matcher
argument_list|<
name|java
operator|.
name|lang
operator|.
name|Iterable
argument_list|<
name|?
super|super
name|String
argument_list|>
argument_list|>
name|v
init|=
name|hasItem
argument_list|(
literal|"/valid/*"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|registrationBean
operator|.
name|getUrlMappings
argument_list|()
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|customPath
parameter_list|()
block|{
name|load
argument_list|(
name|CxfAutoConfiguration
operator|.
name|class
argument_list|,
literal|"cxf.path=/valid"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|this
operator|.
name|context
operator|.
name|getBeansOfType
argument_list|(
name|ServletRegistrationBean
operator|.
name|class
argument_list|)
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|ServletRegistrationBean
argument_list|<
name|?
argument_list|>
name|registrationBean
init|=
name|this
operator|.
name|context
operator|.
name|getBean
argument_list|(
name|ServletRegistrationBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Matcher
argument_list|<
name|java
operator|.
name|lang
operator|.
name|Iterable
argument_list|<
name|?
super|super
name|String
argument_list|>
argument_list|>
name|v
init|=
name|hasItem
argument_list|(
literal|"/valid/*"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|registrationBean
operator|.
name|getUrlMappings
argument_list|()
argument_list|,
name|v
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|customLoadOnStartup
parameter_list|()
block|{
name|load
argument_list|(
name|CxfAutoConfiguration
operator|.
name|class
argument_list|,
literal|"cxf.servlet.load-on-startup=1"
argument_list|)
expr_stmt|;
name|ServletRegistrationBean
argument_list|<
name|?
argument_list|>
name|registrationBean
init|=
name|this
operator|.
name|context
operator|.
name|getBean
argument_list|(
name|ServletRegistrationBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|ReflectionTestUtils
operator|.
name|getField
argument_list|(
name|registrationBean
argument_list|,
literal|"loadOnStartup"
argument_list|)
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|customInitParameters
parameter_list|()
block|{
name|load
argument_list|(
name|CxfAutoConfiguration
operator|.
name|class
argument_list|,
literal|"cxf.servlet.init.key1=value1"
argument_list|,
literal|"cxf.servlet.init.key2=value2"
argument_list|)
expr_stmt|;
name|ServletRegistrationBean
argument_list|<
name|?
argument_list|>
name|registrationBean
init|=
name|this
operator|.
name|context
operator|.
name|getBean
argument_list|(
name|ServletRegistrationBean
operator|.
name|class
argument_list|)
decl_stmt|;
name|Matcher
argument_list|<
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|String
argument_list|>
argument_list|>
name|v1
init|=
name|hasEntry
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
decl_stmt|;
name|Matcher
argument_list|<
name|Map
argument_list|<
name|?
extends|extends
name|String
argument_list|,
name|?
extends|extends
name|String
argument_list|>
argument_list|>
name|v2
init|=
name|hasEntry
argument_list|(
literal|"key2"
argument_list|,
literal|"value2"
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|registrationBean
operator|.
name|getInitParameters
argument_list|()
argument_list|,
name|v1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|registrationBean
operator|.
name|getInitParameters
argument_list|()
argument_list|,
name|v2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|customizedJaxRsServer
parameter_list|()
block|{
name|load
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|CxfAutoConfiguration
operator|.
name|class
operator|,
name|CustomJaxRSServer
operator|.
name|class
block|}
operator|,
literal|"cxf.jaxrs.classes-scan=true"
operator|,
literal|"cxf.jaxrs.classes-scan-packages=org.apache.cxf.spring.boot.jaxrs"
block|)
function|;
name|Map
argument_list|<
name|String
argument_list|,
name|Server
argument_list|>
name|beans
init|=
name|this
operator|.
name|context
operator|.
name|getBeansOfType
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|beans
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|serverInstance
init|=
name|beans
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
name|assertFalse
argument_list|(
name|serverInstance
operator|instanceof
name|ServerImpl
argument_list|)
expr_stmt|;
block|}
end_class

begin_function
annotation|@
name|Test
specifier|public
name|void
name|defaultJaxRsServer
parameter_list|()
block|{
name|load
argument_list|(
name|CxfAutoConfiguration
operator|.
name|class
argument_list|,
literal|"cxf.jaxrs.classes-scan=true"
argument_list|,
literal|"cxf.jaxrs.classes-scan-packages=org.apache.cxf.spring.boot.jaxrs"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Server
argument_list|>
name|beans
init|=
name|this
operator|.
name|context
operator|.
name|getBeansOfType
argument_list|(
name|Server
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertThat
argument_list|(
name|beans
operator|.
name|size
argument_list|()
argument_list|,
name|equalTo
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|Object
name|serverInstance
init|=
name|beans
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
name|assertTrue
argument_list|(
name|serverInstance
operator|instanceof
name|ServerImpl
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
specifier|private
name|void
name|load
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|config
parameter_list|,
name|String
modifier|...
name|environment
parameter_list|)
block|{
name|load
argument_list|(
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|config
block|}
operator|,
name|environment
block|)
function|;
end_function

begin_function
unit|}      private
name|void
name|load
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|configs
parameter_list|,
name|String
modifier|...
name|environment
parameter_list|)
block|{
name|AnnotationConfigWebApplicationContext
name|ctx
init|=
operator|new
name|AnnotationConfigWebApplicationContext
argument_list|()
decl_stmt|;
name|ctx
operator|.
name|setServletContext
argument_list|(
operator|new
name|MockServletContext
argument_list|()
argument_list|)
expr_stmt|;
name|TestPropertyValues
operator|.
name|of
argument_list|(
name|environment
argument_list|)
operator|.
name|applyTo
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|register
argument_list|(
name|configs
argument_list|)
expr_stmt|;
name|ctx
operator|.
name|refresh
argument_list|()
expr_stmt|;
name|this
operator|.
name|context
operator|=
name|ctx
expr_stmt|;
block|}
end_function

unit|}
end_unit

