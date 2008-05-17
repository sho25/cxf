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
name|jca
operator|.
name|core
operator|.
name|classloader
package|;
end_package

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
name|security
operator|.
name|ProtectionDomain
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
name|helpers
operator|.
name|IOUtils
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
name|Before
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
name|PlugInClassLoaderTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|PlugInClassLoaderTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|debug
decl_stmt|;
name|PlugInClassLoader
name|plugInClassLoader
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|plugInClassLoader
operator|=
operator|new
name|PlugInClassLoader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
name|LOG
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|INFO
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|LOG
operator|.
name|setLevel
argument_list|(
name|Level
operator|.
name|WARNING
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadClassWithPlugInClassLoader
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|resultClass
init|=
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.jca.dummy.Dummy"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"wrong class"
argument_list|,
literal|"org.apache.cxf.jca.dummy.Dummy"
argument_list|,
name|resultClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"class loader must be the plugInClassLoader"
argument_list|,
name|plugInClassLoader
argument_list|,
name|resultClass
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testInheritsClassLoaderProtectionDomain
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|resultClass
init|=
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.jca.dummy.Dummy"
argument_list|)
decl_stmt|;
name|ProtectionDomain
name|pd1
init|=
name|plugInClassLoader
operator|.
name|getClass
argument_list|()
operator|.
name|getProtectionDomain
argument_list|()
decl_stmt|;
name|ProtectionDomain
name|pd2
init|=
name|resultClass
operator|.
name|getProtectionDomain
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"PluginClassLoader protection domain: "
operator|+
name|pd1
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"resultClass protection domain: "
operator|+
name|pd2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"protection domain has to be inherited from the PluginClassLoader. "
argument_list|,
name|pd1
argument_list|,
name|pd2
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadClassWithParentClassLoader
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|resultClass
init|=
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
literal|"org.omg.CORBA.ORB"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"wrong class"
argument_list|,
literal|"org.omg.CORBA.ORB"
argument_list|,
name|resultClass
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"class loader must NOT be the plugInClassLoader"
argument_list|,
operator|!
operator|(
name|plugInClassLoader
operator|.
name|equals
argument_list|(
name|resultClass
operator|.
name|getClassLoader
argument_list|()
argument_list|)
operator|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadNonExistentClassWithPlugInClassLoader
parameter_list|()
throws|throws
name|Exception
block|{
try|try
block|{
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
literal|"org.objectweb.foo.bar"
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected ClassNotFoundException"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Exception message: "
operator|+
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"Exception message must not be null."
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"not found class must be part of the message. "
argument_list|,
name|ex
operator|.
name|getMessage
argument_list|()
operator|.
name|indexOf
argument_list|(
literal|"org.objectweb.foo.bar"
argument_list|)
operator|>
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadNonFilteredButAvailableClassWithPlugInClassLoader
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|className
init|=
literal|"javax.resource.ResourceException"
decl_stmt|;
comment|// ensure it is available
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
expr_stmt|;
try|try
block|{
name|Class
name|claz
init|=
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
name|className
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"That should be same classloader "
argument_list|,
name|claz
operator|.
name|getClassLoader
argument_list|()
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ClassNotFoundException
name|ex
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Do not Expect ClassNotFoundException"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadResourceWithPluginClassLoader
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|resultClass
init|=
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.jca.dummy.Dummy"
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
name|resultClass
operator|.
name|getResource
argument_list|(
literal|"dummy.txt"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"URL: "
operator|+
name|url
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bad url: "
operator|+
name|url
argument_list|,
name|url
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"classloader:"
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|configStream
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"stream must not be null. "
argument_list|,
name|configStream
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"unexpected stream class: "
operator|+
name|configStream
operator|.
name|getClass
argument_list|()
argument_list|,
name|configStream
operator|instanceof
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|10
index|]
decl_stmt|;
name|configStream
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"dummy.txt contents: "
operator|+
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"unexpected dummy.txt contents."
argument_list|,
literal|"blah,blah."
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadSlashResourceWithPluginClassLoader
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|resultClass
init|=
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.jca.dummy.Dummy"
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
name|resultClass
operator|.
name|getResource
argument_list|(
literal|"/META-INF/MANIFEST.MF"
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"URL: "
operator|+
name|url
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"bad url: "
operator|+
name|url
argument_list|,
name|url
operator|.
name|toString
argument_list|()
operator|.
name|startsWith
argument_list|(
literal|"classloader:"
argument_list|)
argument_list|)
expr_stmt|;
name|InputStream
name|configStream
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"stream must not be null. "
argument_list|,
name|configStream
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"unexpected stream class: "
operator|+
name|configStream
operator|.
name|getClass
argument_list|()
argument_list|,
name|configStream
operator|instanceof
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
argument_list|)
expr_stmt|;
name|byte
index|[]
name|bytes
init|=
operator|new
name|byte
index|[
literal|21
index|]
decl_stmt|;
name|configStream
operator|.
name|read
argument_list|(
name|bytes
argument_list|,
literal|0
argument_list|,
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
name|result
init|=
name|IOUtils
operator|.
name|newStringFromBytes
argument_list|(
name|bytes
argument_list|)
decl_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"dummy.txt contents: "
operator|+
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"unexpected dummy.txt contents:"
operator|+
name|result
argument_list|,
name|result
operator|.
name|indexOf
argument_list|(
literal|"Manifest-Version: 1.0"
argument_list|)
operator|!=
operator|-
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadNonExistentResourceWithPluginClassLoader
parameter_list|()
throws|throws
name|Exception
block|{
name|Class
name|resultClass
init|=
name|plugInClassLoader
operator|.
name|loadClass
argument_list|(
literal|"org.apache.cxf.jca.dummy.Dummy"
argument_list|)
decl_stmt|;
name|URL
name|url
init|=
name|resultClass
operator|.
name|getResource
argument_list|(
literal|"foo.txt"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"url must be null. "
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadNonExistentDirectory
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|plugInClassLoader
operator|.
name|findResource
argument_list|(
literal|"foo/bar/"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"url must be null. "
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadNonExistentNestedDirectory
parameter_list|()
throws|throws
name|Exception
block|{
name|URL
name|url
init|=
name|plugInClassLoader
operator|.
name|findResource
argument_list|(
literal|"foo!/bar/"
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
literal|"url must be null. "
argument_list|,
name|url
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

