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
name|bus
operator|.
name|extension
package|;
end_package

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
name|assertNotNull
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
name|assertNull
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
name|ExtensionTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testMutators
parameter_list|()
block|{
name|Extension
name|e
init|=
operator|new
name|Extension
argument_list|()
decl_stmt|;
name|String
name|className
init|=
literal|"org.apache.cxf.bindings.soap.SoapBinding"
decl_stmt|;
name|e
operator|.
name|setClassname
argument_list|(
name|className
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected class name."
argument_list|,
name|className
argument_list|,
name|e
operator|.
name|getClassname
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Unexpected interface name."
argument_list|,
name|e
operator|.
name|getInterfaceName
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|interfaceName
init|=
literal|"org.apache.cxf.bindings.Binding"
decl_stmt|;
name|e
operator|.
name|setInterfaceName
argument_list|(
name|interfaceName
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected interface name."
argument_list|,
name|interfaceName
argument_list|,
name|e
operator|.
name|getInterfaceName
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Extension is deferred."
argument_list|,
name|e
operator|.
name|isDeferred
argument_list|()
argument_list|)
expr_stmt|;
name|e
operator|.
name|setDeferred
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Extension is not deferred."
argument_list|,
name|e
operator|.
name|isDeferred
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Unexpected size of namespace list."
argument_list|,
literal|0
argument_list|,
name|e
operator|.
name|getNamespaces
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoad
parameter_list|()
throws|throws
name|Exception
block|{
name|Extension
name|e
init|=
operator|new
name|Extension
argument_list|()
decl_stmt|;
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|e
operator|.
name|setClassname
argument_list|(
literal|"no.such.Extension"
argument_list|)
expr_stmt|;
try|try
block|{
name|e
operator|.
name|load
argument_list|(
name|cl
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExtensionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"ExtensionException does not wrap ClassNotFoundException"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ClassNotFoundException
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setClassname
argument_list|(
literal|"java.lang.System"
argument_list|)
expr_stmt|;
try|try
block|{
name|e
operator|.
name|load
argument_list|(
name|cl
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExtensionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"ExtensionException does not wrap NoSuchMethodException "
operator|+
name|ex
operator|.
name|getCause
argument_list|()
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|NoSuchMethodException
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setClassname
argument_list|(
name|MyServiceConstructorThrowsException
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|e
operator|.
name|load
argument_list|(
name|cl
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExtensionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"ExtensionException does not wrap IllegalArgumentException"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|IllegalArgumentException
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setClassname
argument_list|(
literal|"java.lang.String"
argument_list|)
expr_stmt|;
name|Object
name|obj
init|=
name|e
operator|.
name|load
argument_list|(
name|cl
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Object is not type String"
argument_list|,
name|obj
operator|instanceof
name|String
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadInterface
parameter_list|()
block|{
name|Extension
name|e
init|=
operator|new
name|Extension
argument_list|()
decl_stmt|;
name|ClassLoader
name|cl
init|=
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|getContextClassLoader
argument_list|()
decl_stmt|;
name|e
operator|.
name|setInterfaceName
argument_list|(
literal|"no.such.Extension"
argument_list|)
expr_stmt|;
try|try
block|{
name|e
operator|.
name|loadInterface
argument_list|(
name|cl
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Failure expected"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ExtensionException
name|ex
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|"ExtensionException does not wrap ClassNotFoundException"
argument_list|,
name|ex
operator|.
name|getCause
argument_list|()
operator|instanceof
name|ClassNotFoundException
argument_list|)
expr_stmt|;
block|}
name|e
operator|.
name|setInterfaceName
argument_list|(
name|Assert
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|e
operator|.
name|loadInterface
argument_list|(
name|cl
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|cls
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
class|class
name|MyServiceConstructorThrowsException
block|{
specifier|public
name|MyServiceConstructorThrowsException
parameter_list|()
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|()
throw|;
block|}
block|}
block|}
end_class

end_unit

