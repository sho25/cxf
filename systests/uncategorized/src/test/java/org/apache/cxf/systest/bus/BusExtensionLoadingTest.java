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
name|systest
operator|.
name|bus
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
name|io
operator|.
name|InputStream
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
name|Enumeration
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
name|CXFBusFactory
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
name|extension
operator|.
name|ExtensionManagerBus
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
name|ServerRegistry
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
name|headers
operator|.
name|HeaderManager
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
name|wsdl
operator|.
name|WSDLManager
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

begin_class
specifier|public
class|class
name|BusExtensionLoadingTest
extends|extends
name|Assert
block|{
comment|/**      * Tests the ExtensionManagerBus can be built using a given classloader      *      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testBusConstructionWithoutTCCL
parameter_list|()
throws|throws
name|Exception
block|{
name|ClassLoader
name|origClassLoader
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
try|try
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
operator|new
name|TestClassLoader
argument_list|()
argument_list|)
expr_stmt|;
name|BusFactory
name|factory
init|=
operator|new
name|CXFBusFactory
argument_list|()
block|{
specifier|public
name|Bus
name|createBus
parameter_list|(
name|Map
argument_list|<
name|Class
argument_list|<
name|?
argument_list|>
argument_list|,
name|Object
argument_list|>
name|e
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|properties
parameter_list|)
block|{
return|return
operator|new
name|ExtensionManagerBus
argument_list|(
name|e
argument_list|,
name|properties
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
return|;
block|}
block|}
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|assertNotNullExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|origClassLoader
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Test for checking the ExtensionManagerBus is built using the TCCL by default      *      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testDefaultBusConstruction
parameter_list|()
throws|throws
name|Exception
block|{
name|BusFactory
name|factory
init|=
operator|new
name|CXFBusFactory
argument_list|()
decl_stmt|;
name|Bus
name|bus
init|=
name|factory
operator|.
name|createBus
argument_list|()
decl_stmt|;
name|assertNotNullExtensions
argument_list|(
name|bus
argument_list|)
expr_stmt|;
name|bus
operator|.
name|shutdown
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|assertNotNullExtensions
parameter_list|(
name|Bus
name|bus
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|WSDLManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|ServerRegistry
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|bus
operator|.
name|getExtension
argument_list|(
name|HeaderManager
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
class|class
name|TestClassLoader
extends|extends
name|ClassLoader
block|{
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
name|loadClass
parameter_list|(
specifier|final
name|String
name|className
parameter_list|)
throws|throws
name|ClassNotFoundException
block|{
if|if
condition|(
name|className
operator|.
name|contains
argument_list|(
literal|"cxf"
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ClassNotFoundException
argument_list|(
literal|"TestClassLoader does not load CXF classes: "
operator|+
name|className
argument_list|)
throw|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|URL
name|getResource
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"cxf"
argument_list|)
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"bus"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|super
operator|.
name|getResource
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|URL
argument_list|>
name|getResources
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"cxf"
argument_list|)
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"bus"
argument_list|)
condition|)
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|()
argument_list|)
return|;
block|}
return|return
name|super
operator|.
name|getResources
argument_list|(
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getResourceAsStream
parameter_list|(
specifier|final
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|.
name|contains
argument_list|(
literal|"cxf"
argument_list|)
operator|||
name|name
operator|.
name|contains
argument_list|(
literal|"bus"
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|super
operator|.
name|getResourceAsStream
argument_list|(
name|name
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

