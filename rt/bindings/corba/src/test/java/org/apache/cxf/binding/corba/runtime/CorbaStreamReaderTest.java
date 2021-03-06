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
name|binding
operator|.
name|corba
operator|.
name|runtime
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|QName
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
name|binding
operator|.
name|corba
operator|.
name|types
operator|.
name|CorbaTypeEventProducer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|CorbaStreamReaderTest
block|{
specifier|private
name|CorbaStreamReader
name|reader
decl_stmt|;
specifier|private
name|CorbaTypeEventProducer
name|mock
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
name|mock
operator|=
name|EasyMock
operator|.
name|createMock
argument_list|(
name|CorbaTypeEventProducer
operator|.
name|class
argument_list|)
expr_stmt|;
name|reader
operator|=
operator|new
name|CorbaStreamReader
argument_list|(
name|mock
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetName
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|mock
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.org"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"checking getName "
argument_list|,
operator|new
name|QName
argument_list|(
literal|"http://foo.org"
argument_list|,
literal|"test"
argument_list|)
argument_list|,
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetLocalName
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|mock
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.org"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"checking localName "
argument_list|,
literal|"test"
argument_list|,
name|reader
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetNamespaceURI
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|mock
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
operator|new
name|QName
argument_list|(
literal|"http://foo.org"
argument_list|,
literal|"test"
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"checking namespace "
argument_list|,
literal|"http://foo.org"
argument_list|,
name|reader
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetText
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|mock
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"abcdef"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"checking getText"
argument_list|,
literal|"abcdef"
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetTextCharacters
parameter_list|()
throws|throws
name|Exception
block|{
name|EasyMock
operator|.
name|expect
argument_list|(
name|mock
operator|.
name|getText
argument_list|()
argument_list|)
operator|.
name|andReturn
argument_list|(
literal|"abcdef"
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|replay
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"checking getTextCharacters"
argument_list|,
literal|"abcdef"
argument_list|,
operator|new
name|String
argument_list|(
name|reader
operator|.
name|getTextCharacters
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

