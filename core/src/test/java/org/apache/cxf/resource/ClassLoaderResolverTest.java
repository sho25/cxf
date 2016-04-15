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
name|resource
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
import|;
end_import

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
name|io
operator|.
name|FileWriter
import|;
end_import

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
name|io
operator|.
name|InputStreamReader
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
name|After
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
name|ClassLoaderResolverTest
extends|extends
name|Assert
block|{
specifier|private
specifier|static
specifier|final
name|String
name|RESOURCE_DATA
init|=
literal|"this is the resource data"
decl_stmt|;
specifier|private
name|String
name|resourceName
decl_stmt|;
specifier|private
name|ClassLoaderResolver
name|clr
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|resource
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"test"
argument_list|,
literal|"resource"
argument_list|)
decl_stmt|;
name|resource
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|resourceName
operator|=
name|resource
operator|.
name|getName
argument_list|()
expr_stmt|;
name|FileWriter
name|writer
init|=
operator|new
name|FileWriter
argument_list|(
name|resource
argument_list|)
decl_stmt|;
name|writer
operator|.
name|write
argument_list|(
name|RESOURCE_DATA
argument_list|)
expr_stmt|;
name|writer
operator|.
name|write
argument_list|(
literal|"\n"
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
name|URL
index|[]
name|urls
init|=
block|{
name|resource
operator|.
name|getParentFile
argument_list|()
operator|.
name|toURI
argument_list|()
operator|.
name|toURL
argument_list|()
block|}
decl_stmt|;
name|ClassLoader
name|loader
init|=
operator|new
name|URLClassLoader
argument_list|(
name|urls
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|loader
operator|.
name|getResourceAsStream
argument_list|(
name|resourceName
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|ClassLoader
operator|.
name|getSystemResourceAsStream
argument_list|(
name|resourceName
argument_list|)
argument_list|)
expr_stmt|;
name|clr
operator|=
operator|new
name|ClassLoaderResolver
argument_list|(
name|loader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|()
block|{
name|clr
operator|=
literal|null
expr_stmt|;
name|resourceName
operator|=
literal|null
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testResolve
parameter_list|()
block|{
name|assertNull
argument_list|(
name|clr
operator|.
name|resolve
argument_list|(
name|resourceName
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|clr
operator|.
name|resolve
argument_list|(
name|resourceName
argument_list|,
name|URL
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAsStream
parameter_list|()
throws|throws
name|IOException
block|{
name|InputStream
name|in
init|=
name|clr
operator|.
name|getAsStream
argument_list|(
name|resourceName
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|BufferedReader
name|reader
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|in
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|content
init|=
name|reader
operator|.
name|readLine
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"resource content incorrect"
argument_list|,
name|RESOURCE_DATA
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|reader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

