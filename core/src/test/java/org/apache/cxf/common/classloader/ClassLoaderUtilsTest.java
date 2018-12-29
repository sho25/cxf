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
name|common
operator|.
name|classloader
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
name|net
operator|.
name|URLClassLoader
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

begin_class
specifier|public
class|class
name|ClassLoaderUtilsTest
block|{
specifier|private
specifier|static
name|void
name|setTCCL
parameter_list|(
name|ClassLoader
name|loader
parameter_list|)
block|{
name|Thread
operator|.
name|currentThread
argument_list|()
operator|.
name|setContextClassLoader
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
comment|/**      * This test confirms that the expected thread context classloader      * is returned from the getContextClassLoader method.      */
annotation|@
name|Test
specifier|public
name|void
name|getContextClassLoader
parameter_list|()
throws|throws
name|MalformedURLException
block|{
specifier|final
name|ClassLoader
name|nullLoader
init|=
literal|null
decl_stmt|;
specifier|final
name|ClassLoader
name|jvmAppLoader
init|=
name|ClassLoader
operator|.
name|getSystemClassLoader
argument_list|()
decl_stmt|;
specifier|final
name|ClassLoader
name|jvmExtLoader
init|=
name|jvmAppLoader
operator|.
name|getParent
argument_list|()
decl_stmt|;
specifier|final
name|ClassLoader
name|testClassLoader
init|=
name|ClassLoaderUtilsTest
operator|.
name|class
operator|.
name|getClassLoader
argument_list|()
decl_stmt|;
specifier|final
name|ClassLoader
name|clildLoader
init|=
operator|new
name|URLClassLoader
argument_list|(
operator|new
name|URL
index|[]
block|{
operator|new
name|URL
argument_list|(
literal|"file:/."
argument_list|)
block|}
argument_list|)
decl_stmt|;
specifier|final
name|ClassLoader
name|previousTCCL
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
comment|// TCCL = null
name|setTCCL
argument_list|(
name|nullLoader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TCCL == null; wrong loader returned; expected JVM App loader"
argument_list|,
name|jvmAppLoader
argument_list|,
name|ClassLoaderUtils
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// TCCL = JVM App CL
name|setTCCL
argument_list|(
name|jvmAppLoader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TCCL == JVM App loader; wrong loader returned; expected JVM App loader"
argument_list|,
name|jvmAppLoader
argument_list|,
name|ClassLoaderUtils
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// TCCL = JVM Ext CL
name|setTCCL
argument_list|(
name|jvmExtLoader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TCCL == JVM Ext loader; wrong loader returned; expected JVM Ext loader"
argument_list|,
name|jvmExtLoader
argument_list|,
name|ClassLoaderUtils
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// TCCL = This test class loader (which is likely also the JVM App CL)
name|setTCCL
argument_list|(
name|testClassLoader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TCCL == this test laoder; wrong loader returned; expected JVM App loader"
argument_list|,
name|testClassLoader
argument_list|,
name|ClassLoaderUtils
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
comment|// TCCL = a random child classloader
name|setTCCL
argument_list|(
name|clildLoader
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TCCL == random child loader, wrong loader returned; expected child of test class loader"
argument_list|,
name|clildLoader
argument_list|,
name|ClassLoaderUtils
operator|.
name|getContextClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|// reset the TCCL for other tests
name|setTCCL
argument_list|(
name|previousTCCL
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

