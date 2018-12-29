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
name|tools
operator|.
name|common
operator|.
name|toolspec
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
name|tools
operator|.
name|common
operator|.
name|ToolException
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

begin_class
specifier|public
class|class
name|ToolSpecTest
block|{
name|ToolSpec
name|toolSpec
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testConstruct
parameter_list|()
block|{
name|toolSpec
operator|=
literal|null
expr_stmt|;
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|()
expr_stmt|;
name|assertNotNull
argument_list|(
name|toolSpec
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConstructFromInputStream
parameter_list|()
block|{
name|String
name|tsSource
init|=
literal|"parser/resources/testtool.xml"
decl_stmt|;
try|try
block|{
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ToolException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|assertNull
argument_list|(
name|toolSpec
operator|.
name|getAnnotation
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetParameterDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tsSource
init|=
literal|"parser/resources/testtool.xml"
decl_stmt|;
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|toolSpec
operator|.
name|getAnnotation
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|toolSpec
operator|.
name|getParameterDefault
argument_list|(
literal|"namespace"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|toolSpec
operator|.
name|getParameterDefault
argument_list|(
literal|"wsdlurl"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStreamRefName1
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tsSource
init|=
literal|"parser/resources/testtool1.xml"
decl_stmt|;
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test getStreamRefName failed"
argument_list|,
name|toolSpec
operator|.
name|getStreamRefName
argument_list|(
literal|"streamref"
argument_list|)
argument_list|,
literal|"namespace"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetStreamRefName2
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tsSource
init|=
literal|"parser/resources/testtool2.xml"
decl_stmt|;
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test getStreamRefName2 failed"
argument_list|,
name|toolSpec
operator|.
name|getStreamRefName
argument_list|(
literal|"streamref"
argument_list|)
argument_list|,
literal|"wsdlurl"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsValidInputStream
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tsSource
init|=
literal|"parser/resources/testtool1.xml"
decl_stmt|;
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|toolSpec
operator|.
name|isValidInputStream
argument_list|(
literal|"testID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
operator|!
name|toolSpec
operator|.
name|isValidInputStream
argument_list|(
literal|"dummyID"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|toolSpec
operator|.
name|getInstreamIds
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetHandler
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tsSource
init|=
literal|"parser/resources/testtool1.xml"
decl_stmt|;
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|toolSpec
operator|.
name|getHandler
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|toolSpec
operator|.
name|getHandler
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetOutstreamIds
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|tsSource
init|=
literal|"parser/resources/testtool2.xml"
decl_stmt|;
name|toolSpec
operator|=
operator|new
name|ToolSpec
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|tsSource
argument_list|)
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|toolSpec
operator|.
name|getOutstreamIds
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

