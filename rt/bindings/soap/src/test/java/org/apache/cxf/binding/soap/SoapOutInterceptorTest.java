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
name|soap
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ByteArrayInputStream
import|;
end_import

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
name|OutputStream
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
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
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
name|BusFactory
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
name|soap
operator|.
name|interceptor
operator|.
name|ReadHeadersInterceptor
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
name|soap
operator|.
name|interceptor
operator|.
name|SoapOutInterceptor
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
name|interceptor
operator|.
name|StaxInInterceptor
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
name|staxutils
operator|.
name|StaxUtils
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
name|SoapOutInterceptorTest
extends|extends
name|TestBase
block|{
specifier|private
name|ReadHeadersInterceptor
name|rhi
decl_stmt|;
specifier|private
name|SoapOutInterceptor
name|soi
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
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|StaxInInterceptor
name|sii
init|=
operator|new
name|StaxInInterceptor
argument_list|(
literal|"phase1"
argument_list|)
decl_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|sii
argument_list|)
expr_stmt|;
name|rhi
operator|=
operator|new
name|ReadHeadersInterceptor
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|,
literal|"phase2"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|rhi
argument_list|)
expr_stmt|;
name|soi
operator|=
operator|new
name|SoapOutInterceptor
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|,
literal|"phase3"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|soi
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessage
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareSoapMessage
argument_list|(
literal|"test-soap-header.xml"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|soapMessage
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|Exception
name|oe
init|=
operator|(
name|Exception
operator|)
name|soapMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oe
operator|!=
literal|null
condition|)
block|{
throw|throw
name|oe
throw|;
block|}
name|InputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xmlReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|bis
argument_list|)
decl_stmt|;
name|assertInputStream
argument_list|(
name|xmlReader
argument_list|,
name|Soap11
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleMessage12
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareSoapMessage
argument_list|(
literal|"test-soap-12-header.xml"
argument_list|)
expr_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|out
argument_list|)
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|getInterceptorChain
argument_list|()
operator|.
name|doIntercept
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|soapMessage
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|Exception
name|oe
init|=
operator|(
name|Exception
operator|)
name|soapMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|oe
operator|!=
literal|null
condition|)
block|{
throw|throw
name|oe
throw|;
block|}
name|InputStream
name|bis
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|out
operator|.
name|toByteArray
argument_list|()
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xmlReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|bis
argument_list|)
decl_stmt|;
name|assertInputStream
argument_list|(
name|xmlReader
argument_list|,
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertInputStream
parameter_list|(
name|XMLStreamReader
name|xmlReader
parameter_list|,
name|SoapVersion
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|xmlReader
operator|.
name|nextTag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|version
operator|.
name|getEnvelope
argument_list|()
argument_list|,
name|xmlReader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|xmlReader
operator|.
name|nextTag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|version
operator|.
name|getHeader
argument_list|()
argument_list|,
name|xmlReader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|xmlReader
operator|.
name|nextTag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"reservation"
argument_list|,
name|xmlReader
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|version
operator|.
name|getAttrValueMustUnderstand
argument_list|(
literal|true
argument_list|)
argument_list|,
name|xmlReader
operator|.
name|getAttributeValue
argument_list|(
name|version
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|version
operator|.
name|getAttrNameMustUnderstand
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|XMLStreamReader
operator|.
name|START_ELEMENT
argument_list|,
name|xmlReader
operator|.
name|nextTag
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"reference"
argument_list|,
name|xmlReader
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
comment|// I don't think we're writing the body yet...
comment|//
comment|// assertEquals(XMLStreamReader.START_ELEMENT, xmlReader.nextTag());
comment|// assertEquals(Soap12.getInstance().getBody(), xmlReader.getName());
block|}
specifier|private
name|void
name|prepareSoapMessage
parameter_list|(
name|String
name|payloadFileName
parameter_list|)
throws|throws
name|IOException
block|{
name|soapMessage
operator|=
name|TestUtil
operator|.
name|createEmptySoapMessage
argument_list|(
name|Soap12
operator|.
name|getInstance
argument_list|()
argument_list|,
name|chain
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|payloadFileName
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

