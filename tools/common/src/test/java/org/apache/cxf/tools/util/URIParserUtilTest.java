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
name|util
package|;
end_package

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
name|net
operator|.
name|URI
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
name|URIParserUtilTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetPackageName
parameter_list|()
block|{
name|String
name|packageName
init|=
name|URIParserUtil
operator|.
name|getPackageName
argument_list|(
literal|"http://www.cxf.iona.com"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|packageName
argument_list|,
literal|"com.iona.cxf"
argument_list|)
expr_stmt|;
name|packageName
operator|=
name|URIParserUtil
operator|.
name|getPackageName
argument_list|(
literal|"urn://www.class.iona.com"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|packageName
argument_list|,
literal|"com.iona._class"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNormalize
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
literal|"wsdl/hello_world.wsdl"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"file:wsdl/hello_world.wsdl"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"\\src\\wsdl/hello_world.wsdl"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/src/wsdl/hello_world.wsdl"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"wsdl\\hello_world.wsdl"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:wsdl/hello_world.wsdl"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"http://hello.com"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://hello.com"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"file:///c:\\hello.wsdl"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/c:/hello.wsdl"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"c:\\hello.wsdl"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/c:/hello.wsdl"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"/c:\\hello.wsdl"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/c:/hello.wsdl"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"file:/home/john/test/all/../../alltest"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/home/john/alltest"
argument_list|,
name|URIParserUtil
operator|.
name|normalize
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetAbsoluteURI
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|uri
init|=
literal|"wsdl/hello_world.wsdl"
decl_stmt|;
name|String
name|uri2
init|=
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|uri
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|uri2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uri2
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uri2
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uri2
operator|.
name|contains
argument_list|(
operator|new
name|java
operator|.
name|io
operator|.
name|File
argument_list|(
literal|""
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/schemas/wsdl/test.xsd"
argument_list|)
operator|.
name|toString
argument_list|()
expr_stmt|;
name|uri2
operator|=
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|uri2
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uri2
operator|.
name|startsWith
argument_list|(
literal|"file"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uri2
operator|.
name|contains
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|uri2
operator|.
name|contains
argument_list|(
operator|new
name|java
operator|.
name|io
operator|.
name|File
argument_list|(
literal|""
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"c:\\wsdl\\hello_world.wsdl"
expr_stmt|;
name|uri2
operator|=
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|uri2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/c:/wsdl/hello_world.wsdl"
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"/c:\\wsdl\\hello_world.wsdl"
expr_stmt|;
name|uri2
operator|=
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|uri
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|uri2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/c:/wsdl/hello_world.wsdl"
argument_list|,
name|uri2
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"http://hello/world.wsdl"
expr_stmt|;
name|assertEquals
argument_list|(
name|uri
argument_list|,
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
name|uri
operator|=
literal|"file:/home/john/test/all/../../alltest"
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file:/home/john/alltest"
argument_list|,
name|URIParserUtil
operator|.
name|getAbsoluteURI
argument_list|(
name|uri
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCXF3855
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|orig
init|=
operator|new
name|String
argument_list|(
operator|new
name|byte
index|[]
block|{
operator|-
literal|47
block|,
operator|-
literal|122
block|}
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|orig
operator|=
literal|"/foo"
operator|+
name|orig
operator|+
literal|".txt"
expr_stmt|;
name|String
name|s
init|=
name|URIParserUtil
operator|.
name|escapeChars
argument_list|(
name|orig
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|orig
argument_list|,
operator|new
name|File
argument_list|(
operator|new
name|URI
argument_list|(
literal|"file:"
operator|+
name|s
argument_list|)
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

