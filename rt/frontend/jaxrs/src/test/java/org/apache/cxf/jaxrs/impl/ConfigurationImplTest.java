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
name|impl
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|InvocationHandler
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Proxy
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
name|Collections
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
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Handler
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
name|LogRecord
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
name|ConstrainedTo
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
name|RuntimeType
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
name|client
operator|.
name|Client
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
name|client
operator|.
name|ClientRequestContext
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
name|client
operator|.
name|ClientRequestFilter
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
name|client
operator|.
name|ClientResponseContext
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
name|client
operator|.
name|ClientResponseFilter
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
name|container
operator|.
name|ContainerRequestContext
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
name|container
operator|.
name|ContainerRequestFilter
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
name|container
operator|.
name|ContainerResponseContext
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
name|container
operator|.
name|ContainerResponseFilter
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
name|core
operator|.
name|Configurable
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
name|core
operator|.
name|Configuration
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
name|core
operator|.
name|Feature
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
name|core
operator|.
name|FeatureContext
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
name|MessageBodyReader
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
name|junit
operator|.
name|Assert
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertEquals
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

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|fail
import|;
end_import

begin_class
specifier|public
class|class
name|ConfigurationImplTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testIsRegistered
parameter_list|()
throws|throws
name|Exception
block|{
comment|//        ConfigurationImpl c = new ConfigurationImpl(RuntimeType.SERVER);
comment|//        ContainerResponseFilter filter = new ContainerResponseFilterImpl();
comment|//        assertTrue(c.register(filter,
comment|//                              Collections.<Class<?>, Integer>singletonMap(ContainerResponseFilter.class, 1000)));
comment|//        assertTrue(c.isRegistered(filter));
comment|//        assertFalse(c.isRegistered(new ContainerResponseFilterImpl()));
comment|//        assertTrue(c.isRegistered(ContainerResponseFilterImpl.class));
comment|//        assertFalse(c.isRegistered(ContainerResponseFilter.class));
comment|//        assertFalse(c.register(filter,
comment|//                               Collections.<Class<?>, Integer>singletonMap(ContainerResponseFilter.class, 1000)));
comment|//        assertFalse(c.register(ContainerResponseFilterImpl.class,
comment|//                               Collections.<Class<?>, Integer>singletonMap(ContainerResponseFilter.class, 1000)));
name|doTestIsFilterRegistered
argument_list|(
operator|new
name|ContainerResponseFilterImpl
argument_list|()
argument_list|,
name|ContainerResponseFilterImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsRegisteredSubClass
parameter_list|()
throws|throws
name|Exception
block|{
name|doTestIsFilterRegistered
argument_list|(
operator|new
name|ContainerResponseFilterSubClassImpl
argument_list|()
argument_list|,
name|ContainerResponseFilterSubClassImpl
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|doTestIsFilterRegistered
parameter_list|(
name|Object
name|provider
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|providerClass
parameter_list|)
throws|throws
name|Exception
block|{
name|ConfigurationImpl
name|c
init|=
operator|new
name|ConfigurationImpl
argument_list|(
name|RuntimeType
operator|.
name|SERVER
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|register
argument_list|(
name|provider
argument_list|,
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|,
name|Integer
operator|>
name|singletonMap
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|,
literal|1000
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isRegistered
argument_list|(
name|provider
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|isRegistered
argument_list|(
name|providerClass
operator|.
name|newInstance
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|c
operator|.
name|isRegistered
argument_list|(
name|providerClass
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|isRegistered
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|register
argument_list|(
name|provider
argument_list|,
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|,
name|Integer
operator|>
name|singletonMap
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|,
literal|1000
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|register
argument_list|(
name|providerClass
argument_list|,
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|,
name|Integer
operator|>
name|singletonMap
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|,
literal|1000
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|ConstrainedTo
argument_list|(
name|RuntimeType
operator|.
name|SERVER
argument_list|)
specifier|public
specifier|static
class|class
name|ContainerResponseFilterImpl
implements|implements
name|ContainerResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|requestContext
parameter_list|,
name|ContainerResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{         }
block|}
specifier|public
specifier|static
class|class
name|ContainerResponseFilterSubClassImpl
extends|extends
name|ContainerResponseFilterImpl
block|{ }
annotation|@
name|ConstrainedTo
argument_list|(
name|RuntimeType
operator|.
name|CLIENT
argument_list|)
specifier|public
specifier|static
class|class
name|ClientResponseFilterImpl
implements|implements
name|ClientResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|requestContext
parameter_list|,
name|ClientResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{         }
block|}
specifier|static
class|class
name|TestHandler
extends|extends
name|Handler
block|{
name|List
argument_list|<
name|String
argument_list|>
name|messages
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|publish
parameter_list|(
name|LogRecord
name|record
parameter_list|)
block|{
name|messages
operator|.
name|add
argument_list|(
name|record
operator|.
name|getLevel
argument_list|()
operator|.
name|toString
argument_list|()
operator|+
literal|": "
operator|+
name|record
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|flush
parameter_list|()
block|{
comment|// no-op
block|}
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|SecurityException
block|{
comment|// no-op
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInvalidContract
parameter_list|()
block|{
name|TestHandler
name|handler
init|=
operator|new
name|TestHandler
argument_list|()
decl_stmt|;
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ConfigurationImpl
operator|.
name|class
argument_list|)
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
name|ConfigurationImpl
name|c
init|=
operator|new
name|ConfigurationImpl
argument_list|(
name|RuntimeType
operator|.
name|SERVER
argument_list|)
decl_stmt|;
name|ContainerResponseFilter
name|filter
init|=
operator|new
name|ContainerResponseFilterImpl
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|c
operator|.
name|register
argument_list|(
name|filter
argument_list|,
name|Collections
operator|.
expr|<
name|Class
argument_list|<
name|?
argument_list|>
operator|,
name|Integer
operator|>
name|singletonMap
argument_list|(
name|MessageBodyReader
operator|.
name|class
argument_list|,
literal|1000
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|message
range|:
name|handler
operator|.
name|messages
control|)
block|{
if|if
condition|(
name|message
operator|.
name|startsWith
argument_list|(
literal|"WARN"
argument_list|)
operator|&&
name|message
operator|.
name|contains
argument_list|(
literal|"does not implement specified contract"
argument_list|)
condition|)
block|{
return|return;
comment|// success
block|}
block|}
name|fail
argument_list|(
literal|"did not log expected message"
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|TestFilter
implements|implements
name|ContainerRequestFilter
implements|,
name|ContainerResponseFilter
implements|,
name|ClientRequestFilter
implements|,
name|ClientResponseFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|paramClientRequestContext
parameter_list|,
name|ClientResponseContext
name|paramClientResponseContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no-op
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|paramClientRequestContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no-op
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|paramContainerRequestContext
parameter_list|,
name|ContainerResponseContext
name|paramContainerResponseContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no-op
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ContainerRequestContext
name|paramContainerRequestContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no-op
block|}
block|}
specifier|public
interface|interface
name|MyClientFilter
extends|extends
name|ClientRequestFilter
extends|,
name|ClientResponseFilter
block|{
comment|// reduced to just the intermediate layer. Could contain user code
block|}
specifier|public
specifier|static
class|class
name|NestedInterfaceTestFilter
implements|implements
name|MyClientFilter
block|{
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|requestContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no-op
block|}
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|requestContext
parameter_list|,
name|ClientResponseContext
name|responseContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no-op
block|}
block|}
specifier|private
name|Client
name|createClientProxy
parameter_list|()
block|{
return|return
operator|(
name|Client
operator|)
name|Proxy
operator|.
name|newProxyInstance
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
argument_list|<
name|?
argument_list|>
index|[]
block|{
name|Client
operator|.
name|class
block|}
operator|,
operator|new
name|InvocationHandler
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|Object
name|invoke
parameter_list|(
name|Object
name|proxy
parameter_list|,
name|Method
name|method
parameter_list|,
name|Object
index|[]
name|args
parameter_list|)
throws|throws
name|Throwable
block|{
return|return
literal|null
return|;
comment|//no-op
block|}
block|}
block|)
function|;
block|}
end_class

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testSubClassIsRegisteredOnConfigurable
parameter_list|()
block|{
name|FeatureContextImpl
name|featureContext
init|=
operator|new
name|FeatureContextImpl
argument_list|()
decl_stmt|;
name|Configurable
argument_list|<
name|FeatureContext
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|featureContext
argument_list|,
name|RuntimeType
operator|.
name|SERVER
argument_list|)
decl_stmt|;
name|featureContext
operator|.
name|setConfigurable
argument_list|(
name|configurable
argument_list|)
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
name|ContainerResponseFilterSubClassImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
init|=
name|config
operator|.
name|getContracts
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|contracts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testServerFilterContractsOnClientIsRejected
parameter_list|()
block|{
try|try
init|(
name|ConfigurableImpl
argument_list|<
name|Client
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|createClientProxy
argument_list|()
argument_list|,
name|RuntimeType
operator|.
name|CLIENT
argument_list|)
init|)
block|{
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configurable
operator|.
name|register
argument_list|(
name|TestFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
init|=
name|config
operator|.
name|getContracts
argument_list|(
name|TestFilter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ClientRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ClientResponseFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ContainerRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testClientFilterWithNestedInterfacesIsAccepted
parameter_list|()
block|{
try|try
init|(
name|ConfigurableImpl
argument_list|<
name|Client
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|createClientProxy
argument_list|()
argument_list|,
name|RuntimeType
operator|.
name|CLIENT
argument_list|)
init|)
block|{
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configurable
operator|.
name|register
argument_list|(
name|NestedInterfaceTestFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
init|=
name|config
operator|.
name|getContracts
argument_list|(
name|NestedInterfaceTestFilter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ClientRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ClientResponseFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testClientFilterContractsOnServerFeatureIsRejected
parameter_list|()
block|{
name|FeatureContextImpl
name|featureContext
init|=
operator|new
name|FeatureContextImpl
argument_list|()
decl_stmt|;
name|Configurable
argument_list|<
name|FeatureContext
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|featureContext
argument_list|,
name|RuntimeType
operator|.
name|SERVER
argument_list|)
decl_stmt|;
name|featureContext
operator|.
name|setConfigurable
argument_list|(
name|configurable
argument_list|)
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
name|TestFilter
operator|.
name|class
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Integer
argument_list|>
name|contracts
init|=
name|config
operator|.
name|getContracts
argument_list|(
name|TestFilter
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ClientRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ClientResponseFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ContainerRequestFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|contracts
operator|.
name|containsKey
argument_list|(
name|ContainerResponseFilter
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_class
specifier|public
specifier|static
class|class
name|DisablableFeature
implements|implements
name|Feature
block|{
name|boolean
name|enabled
decl_stmt|;
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|boolean
name|configure
parameter_list|(
name|FeatureContext
name|context
parameter_list|)
block|{
return|return
name|enabled
return|;
block|}
block|}
end_class

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testFeatureDisabledClass
parameter_list|()
block|{
name|FeatureContextImpl
name|featureContext
init|=
operator|new
name|FeatureContextImpl
argument_list|()
decl_stmt|;
name|Configurable
argument_list|<
name|FeatureContext
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|featureContext
argument_list|,
name|RuntimeType
operator|.
name|SERVER
argument_list|)
decl_stmt|;
name|featureContext
operator|.
name|setConfigurable
argument_list|(
name|configurable
argument_list|)
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
name|DisablableFeature
operator|.
name|class
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|config
operator|.
name|isEnabled
argument_list|(
name|DisablableFeature
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testFeatureDisabledInstance
parameter_list|()
block|{
name|FeatureContextImpl
name|featureContext
init|=
operator|new
name|FeatureContextImpl
argument_list|()
decl_stmt|;
name|Configurable
argument_list|<
name|FeatureContext
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|featureContext
argument_list|,
name|RuntimeType
operator|.
name|SERVER
argument_list|)
decl_stmt|;
name|featureContext
operator|.
name|setConfigurable
argument_list|(
name|configurable
argument_list|)
expr_stmt|;
name|Feature
name|feature
init|=
operator|new
name|DisablableFeature
argument_list|()
decl_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
name|feature
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
name|config
operator|.
name|isEnabled
argument_list|(
name|feature
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testIsEnabledWithMultipleFeaturesOfSameType
parameter_list|()
block|{
name|FeatureContextImpl
name|featureContext
init|=
operator|new
name|FeatureContextImpl
argument_list|()
decl_stmt|;
name|Configurable
argument_list|<
name|FeatureContext
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|featureContext
argument_list|,
name|RuntimeType
operator|.
name|SERVER
argument_list|)
decl_stmt|;
name|featureContext
operator|.
name|setConfigurable
argument_list|(
name|configurable
argument_list|)
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
operator|new
name|DisablableFeature
argument_list|()
argument_list|)
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
operator|new
name|DisablableFeature
argument_list|()
argument_list|)
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
operator|new
name|DisablableFeature
argument_list|()
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|config
operator|.
name|getInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|config
operator|.
name|isEnabled
argument_list|(
name|DisablableFeature
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|DisablableFeature
name|enabledFeature
init|=
operator|new
name|DisablableFeature
argument_list|()
decl_stmt|;
name|enabledFeature
operator|.
name|enabled
operator|=
literal|true
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
name|enabledFeature
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|4
argument_list|,
name|config
operator|.
name|getInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|config
operator|.
name|isEnabled
argument_list|(
name|DisablableFeature
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|featureContext
operator|.
name|register
argument_list|(
operator|new
name|DisablableFeature
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|5
argument_list|,
name|config
operator|.
name|getInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|config
operator|.
name|isEnabled
argument_list|(
name|DisablableFeature
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
end_function

begin_class
annotation|@
name|ConstrainedTo
argument_list|(
name|RuntimeType
operator|.
name|SERVER
argument_list|)
specifier|public
specifier|static
class|class
name|ClientFilterConstrainedToServer
implements|implements
name|ClientRequestFilter
block|{
comment|/** {@inheritDoc}*/
annotation|@
name|Override
specifier|public
name|void
name|filter
parameter_list|(
name|ClientRequestContext
name|paramClientRequestContext
parameter_list|)
throws|throws
name|IOException
block|{
comment|// no-op
block|}
block|}
end_class

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testInvalidConstraintOnProvider
parameter_list|()
block|{
name|TestHandler
name|handler
init|=
operator|new
name|TestHandler
argument_list|()
decl_stmt|;
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ConfigurableImpl
operator|.
name|class
argument_list|)
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
try|try
init|(
name|ConfigurableImpl
argument_list|<
name|Client
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|createClientProxy
argument_list|()
argument_list|,
name|RuntimeType
operator|.
name|CLIENT
argument_list|)
init|)
block|{
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configurable
operator|.
name|register
argument_list|(
name|ClientFilterConstrainedToServer
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|config
operator|.
name|getInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|message
range|:
name|handler
operator|.
name|messages
control|)
block|{
if|if
condition|(
name|message
operator|.
name|startsWith
argument_list|(
literal|"WARN"
argument_list|)
operator|&&
name|message
operator|.
name|contains
argument_list|(
literal|"cannot be registered in "
argument_list|)
condition|)
block|{
return|return;
comment|// success
block|}
block|}
block|}
name|fail
argument_list|(
literal|"did not log expected message"
argument_list|)
expr_stmt|;
block|}
end_function

begin_function
annotation|@
name|Test
specifier|public
name|void
name|testChecksConstrainedToAnnotationDuringRegistration
parameter_list|()
block|{
name|TestHandler
name|handler
init|=
operator|new
name|TestHandler
argument_list|()
decl_stmt|;
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|ConfigurableImpl
operator|.
name|class
argument_list|)
operator|.
name|addHandler
argument_list|(
name|handler
argument_list|)
expr_stmt|;
try|try
init|(
name|ConfigurableImpl
argument_list|<
name|Client
argument_list|>
name|configurable
init|=
operator|new
name|ConfigurableImpl
argument_list|<>
argument_list|(
name|createClientProxy
argument_list|()
argument_list|,
name|RuntimeType
operator|.
name|CLIENT
argument_list|)
init|)
block|{
name|Configuration
name|config
init|=
name|configurable
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|configurable
operator|.
name|register
argument_list|(
name|ContainerResponseFilterImpl
operator|.
name|class
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|config
operator|.
name|getInstances
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|message
range|:
name|handler
operator|.
name|messages
control|)
block|{
if|if
condition|(
name|message
operator|.
name|startsWith
argument_list|(
literal|"WARN"
argument_list|)
operator|&&
name|message
operator|.
name|contains
argument_list|(
literal|"Null, empty or invalid contracts specified"
argument_list|)
condition|)
block|{
return|return;
comment|// success
block|}
block|}
block|}
name|fail
argument_list|(
literal|"did not log expected message"
argument_list|)
expr_stmt|;
block|}
end_function

unit|}
end_unit

