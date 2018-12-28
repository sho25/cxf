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
name|util
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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
name|assertNull
import|;
end_import

begin_class
specifier|public
class|class
name|URIParserUtilsTest
extends|extends
name|Assert
block|{
annotation|@
name|Test
specifier|public
name|void
name|testRelativize
parameter_list|()
throws|throws
name|URISyntaxException
block|{
name|assertNull
argument_list|(
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|null
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"foo"
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"fds"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"fds/"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fdsfs"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|""
argument_list|,
literal|"fdsfs"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"fdsfs/a"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|""
argument_list|,
literal|"fdsfs/a"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../de"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"ab/cd"
argument_list|,
literal|"de"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../de/fe/gh"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"ab/cd"
argument_list|,
literal|"de/fe/gh"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../../../de/fe/gh"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"/abc/def/"
argument_list|,
literal|"de/fe/gh"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"file:/c:/abc/def/"
argument_list|,
literal|"de/fe/gh"
argument_list|)
argument_list|)
expr_stmt|;
comment|// null as the URI obtained by
comment|// the 2 strings are not both
comment|// absolute or not absolute
name|assertEquals
argument_list|(
literal|"pippo2.xsd"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"/abc/def/pippo1.xsd"
argument_list|,
literal|"/abc/def/pippo2.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"../default/pippo2.xsd"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"/abc/def/pippo1.xsd"
argument_list|,
literal|"/abc/default/pippo2.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"def/pippo2.xsd"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"/abc/def"
argument_list|,
literal|"/abc/def/pippo2.xsd"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"hello_world_schema2.xsd"
argument_list|,
name|URIParserUtil
operator|.
name|relativize
argument_list|(
literal|"jar:file:/home/a.jar!/wsdl/others/"
argument_list|,
literal|"jar:file:/home/a.jar!/wsdl/others/hello_world_schema2.xsd"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

