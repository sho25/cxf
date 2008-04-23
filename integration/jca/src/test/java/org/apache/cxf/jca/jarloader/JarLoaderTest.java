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
name|jarloader
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
name|JarLoaderTest
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
name|JarLoaderTest
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|URL
name|exampleRarURL
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
name|exampleRarURL
operator|=
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"blackbox-notx.rar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetBytesFromImputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|byte
index|[]
name|bytes
init|=
name|JarLoader
operator|.
name|getBytesFromInputStream
argument_list|(
name|exampleRarURL
operator|.
name|openStream
argument_list|()
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"byte array must not be null"
argument_list|,
name|bytes
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"lenght must be bigger than 0"
argument_list|,
name|bytes
operator|.
name|length
operator|>
literal|0
argument_list|)
expr_stmt|;
name|LOG
operator|.
name|fine
argument_list|(
literal|"bytes length. : "
operator|+
name|bytes
operator|.
name|length
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetJarContents
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|urlPath
init|=
name|exampleRarURL
operator|.
name|toString
argument_list|()
decl_stmt|;
name|LOG
operator|.
name|info
argument_list|(
literal|"URLPath: "
operator|+
name|urlPath
argument_list|)
expr_stmt|;
name|Map
name|map
init|=
name|JarLoader
operator|.
name|getJarContents
argument_list|(
name|urlPath
operator|+
literal|"!/blackbox-notx.jar!/"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"map must not be null"
argument_list|,
name|map
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
literal|"class must be included in map "
operator|+
name|map
operator|.
name|get
argument_list|(
literal|"com/sun/connector/blackbox/JdbcDataSource.class"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

