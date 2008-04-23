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
operator|.
name|saaj
package|;
end_package

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
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|mail
operator|.
name|util
operator|.
name|ByteArrayDataSource
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
name|w3c
operator|.
name|dom
operator|.
name|Element
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
name|Soap12
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
name|TestBase
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
name|TestUtil
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
name|CheckFaultInterceptor
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
name|headers
operator|.
name|Header
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
name|SAAJInInterceptorTest
extends|extends
name|TestBase
block|{
specifier|private
name|ReadHeadersInterceptor
name|rhi
decl_stmt|;
specifier|private
name|StaxInInterceptor
name|staxIntc
init|=
operator|new
name|StaxInInterceptor
argument_list|()
decl_stmt|;
specifier|private
name|SAAJInInterceptor
name|saajIntc
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
literal|"phase1"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|rhi
argument_list|)
expr_stmt|;
name|saajIntc
operator|=
operator|new
name|SAAJInInterceptor
argument_list|(
literal|"phase2"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|saajIntc
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
operator|new
name|CheckFaultInterceptor
argument_list|(
literal|"phase3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHandleHeader
parameter_list|()
block|{
try|try
block|{
name|prepareSoapMessage
argument_list|(
literal|"../test-soap-header.xml"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Failed in creating soap message"
argument_list|)
expr_stmt|;
block|}
name|staxIntc
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|rhi
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|saajIntc
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
comment|// check the xmlReader should be placed on the first entry of the body
comment|// element
name|XMLStreamReader
name|xmlReader
init|=
name|soapMessage
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check the first entry of body"
argument_list|,
literal|"itinerary"
argument_list|,
name|xmlReader
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Header
argument_list|>
name|eleHeaders
init|=
name|soapMessage
operator|.
name|getHeaders
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|headerChilds
init|=
operator|new
name|ArrayList
argument_list|<
name|Element
argument_list|>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|Header
argument_list|>
name|iter
init|=
name|eleHeaders
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|Header
name|hdr
init|=
name|iter
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|hdr
operator|.
name|getObject
argument_list|()
operator|instanceof
name|Element
condition|)
block|{
name|headerChilds
operator|.
name|add
argument_list|(
operator|(
name|Element
operator|)
name|hdr
operator|.
name|getObject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|//        for (int i = 0; i< eleHeaders.getChildNodes().getLength(); i++) {
comment|//            if (eleHeaders.getChildNodes().item(i) instanceof Element) {
comment|//                Element element = (Element)eleHeaders.getChildNodes().item(i);
comment|//                headerChilds.add(element);
comment|//            }
comment|//        }
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|headerChilds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|prepareSoapMessage
parameter_list|(
name|String
name|message
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
name|ByteArrayDataSource
name|bads
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|this
operator|.
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
name|message
argument_list|)
argument_list|,
literal|"text/xml"
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|bads
operator|.
name|getInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

