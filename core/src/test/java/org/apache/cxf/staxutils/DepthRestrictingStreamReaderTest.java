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
name|staxutils
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayOutputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
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
name|assertTrue
import|;
end_import

begin_class
specifier|public
class|class
name|DepthRestrictingStreamReaderTest
block|{
annotation|@
name|Test
specifier|public
name|void
name|testReaderOK
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./resources/amazon.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|DepthRestrictingStreamReader
name|dr
init|=
operator|new
name|DepthRestrictingStreamReader
argument_list|(
name|reader
argument_list|,
literal|7
argument_list|,
literal|4
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dr
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"ItemLookup"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReaderOKComplex
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./resources/wstrustReqSTRC.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|DepthRestrictingStreamReader
name|dr
init|=
operator|new
name|DepthRestrictingStreamReader
argument_list|(
name|reader
argument_list|,
operator|-
literal|1
argument_list|,
literal|8
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|bos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dr
argument_list|,
name|bos
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|bos
operator|.
name|toString
argument_list|()
operator|.
name|contains
argument_list|(
literal|"RequestSecurityTokenResponse"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DepthExceededStaxException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testElementCountExceeded
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./resources/amazon.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|DepthRestrictingStreamReader
name|dr
init|=
operator|new
name|DepthRestrictingStreamReader
argument_list|(
name|reader
argument_list|,
literal|6
argument_list|,
literal|4
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dr
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DepthExceededStaxException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testElementLevelExceeded
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./resources/amazon.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|DepthRestrictingStreamReader
name|dr
init|=
operator|new
name|DepthRestrictingStreamReader
argument_list|(
name|reader
argument_list|,
literal|7
argument_list|,
literal|3
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dr
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DepthExceededStaxException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testElementLevelExceededComplex
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./resources/wstrustReqSTRC.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|DepthRestrictingStreamReader
name|dr
init|=
operator|new
name|DepthRestrictingStreamReader
argument_list|(
name|reader
argument_list|,
operator|-
literal|1
argument_list|,
literal|7
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dr
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DepthExceededStaxException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testInnerElementCountExceeded
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./resources/amazon.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|DepthRestrictingStreamReader
name|dr
init|=
operator|new
name|DepthRestrictingStreamReader
argument_list|(
name|reader
argument_list|,
literal|7
argument_list|,
literal|4
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dr
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|DepthExceededStaxException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testInnerElementCountExceededComplex
parameter_list|()
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"./resources/wstrustReqSTRC.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|DepthRestrictingStreamReader
name|dr
init|=
operator|new
name|DepthRestrictingStreamReader
argument_list|(
name|reader
argument_list|,
operator|-
literal|1
argument_list|,
literal|7
argument_list|,
literal|2
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dr
argument_list|,
operator|new
name|ByteArrayOutputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

