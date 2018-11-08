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
name|activation
operator|.
name|DataHandler
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
name|annotations
operator|.
name|SchemaValidation
operator|.
name|SchemaValidationType
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
name|attachment
operator|.
name|AttachmentImpl
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
name|attachment
operator|.
name|AttachmentUtil
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
name|binding
operator|.
name|soap
operator|.
name|interceptor
operator|.
name|StartBodyInterceptor
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
name|helpers
operator|.
name|DOMUtils
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
name|Fault
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
name|message
operator|.
name|Attachment
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
name|message
operator|.
name|Message
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
name|ReadHeaderInterceptorTest
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
name|StartBodyInterceptor
name|sbi
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
name|sbi
operator|=
operator|new
name|StartBodyInterceptor
argument_list|(
literal|"phase1"
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
name|sbi
argument_list|)
expr_stmt|;
name|chain
operator|.
name|add
argument_list|(
operator|new
name|CheckFaultInterceptor
argument_list|(
literal|"phase2"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadHttpVerb
parameter_list|()
throws|throws
name|Exception
block|{
name|prepareSoapMessage
argument_list|(
literal|"test-soap-header.xml"
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|HTTP_REQUEST_METHOD
argument_list|,
literal|"OPTIONS"
argument_list|)
expr_stmt|;
name|ReadHeadersInterceptor
name|r
init|=
operator|new
name|ReadHeadersInterceptor
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|r
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Fault
name|f
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|405
argument_list|,
name|f
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadSOAPEnvelopeNamespace
parameter_list|()
throws|throws
name|Exception
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
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-bad-env.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|ByteArrayDataSource
name|bads
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|in
argument_list|,
literal|"test/xml"
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
name|ReadHeadersInterceptor
name|r
init|=
operator|new
name|ReadHeadersInterceptor
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|r
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|f
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getVersionMismatch
argument_list|()
argument_list|,
name|f
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadSOAPEnvelopeName
parameter_list|()
throws|throws
name|Exception
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
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-bad-envname.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|ByteArrayDataSource
name|bads
init|=
operator|new
name|ByteArrayDataSource
argument_list|(
name|in
argument_list|,
literal|"test/xml"
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
name|ReadHeadersInterceptor
name|r
init|=
operator|new
name|ReadHeadersInterceptor
argument_list|(
name|BusFactory
operator|.
name|getDefaultBus
argument_list|()
argument_list|)
decl_stmt|;
try|try
block|{
name|r
operator|.
name|handleMessage
argument_list|(
name|soapMessage
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Did not throw exception"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|SoapFault
name|f
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|Soap11
operator|.
name|getInstance
argument_list|()
operator|.
name|getSender
argument_list|()
argument_list|,
name|f
operator|.
name|getFaultCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoClosingEnvTage
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|testNoClosingEnvTag
argument_list|(
name|Boolean
operator|.
name|TRUE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoClosingEnvTagValidationTypeBoth
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|testNoClosingEnvTag
argument_list|(
name|SchemaValidationType
operator|.
name|BOTH
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoClosingEnvTagValidationTypeIn
parameter_list|()
throws|throws
name|Exception
block|{
name|assertTrue
argument_list|(
name|testNoClosingEnvTag
argument_list|(
name|SchemaValidationType
operator|.
name|IN
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoClosingEnvTagValidationTypeOut
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|testNoClosingEnvTag
argument_list|(
name|SchemaValidationType
operator|.
name|OUT
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoClosingEnvTagValidationTypeNone
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|testNoClosingEnvTag
argument_list|(
name|SchemaValidationType
operator|.
name|NONE
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNoClosingEnvTagValidationTypeFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFalse
argument_list|(
name|testNoClosingEnvTag
argument_list|(
name|Boolean
operator|.
name|FALSE
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|testNoClosingEnvTag
parameter_list|(
name|Object
name|validationType
parameter_list|)
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
name|InputStream
name|in
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"test-no-endenv.xml"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|in
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|put
argument_list|(
name|Message
operator|.
name|SCHEMA_VALIDATION_ENABLED
argument_list|,
name|validationType
argument_list|)
expr_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|in
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
return|return
name|soapMessage
operator|.
name|getContent
argument_list|(
name|Exception
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
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
literal|"test-soap-header.xml"
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
comment|// check the xmlReader should be placed on the first entry of the body element
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
argument_list|<>
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
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|headerChilds
operator|.
name|size
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|Element
name|ele
init|=
name|headerChilds
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
literal|"reservation"
operator|.
name|equals
argument_list|(
name|ele
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|reservation
init|=
name|ele
decl_stmt|;
name|List
argument_list|<
name|Element
argument_list|>
name|reservationChilds
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Element
name|elem
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|reservation
argument_list|)
decl_stmt|;
while|while
condition|(
name|elem
operator|!=
literal|null
condition|)
block|{
name|reservationChilds
operator|.
name|add
argument_list|(
name|elem
argument_list|)
expr_stmt|;
name|elem
operator|=
name|DOMUtils
operator|.
name|getNextElement
argument_list|(
name|elem
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|reservationChilds
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"reference"
argument_list|,
name|reservationChilds
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"uuid:093a2da1-q345-739r-ba5d-pqff98fe8j7d"
argument_list|,
name|reservationChilds
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"dateAndTime"
argument_list|,
name|reservationChilds
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2001-11-29T13:20:00.000-05:00"
argument_list|,
name|reservationChilds
operator|.
name|get
argument_list|(
literal|1
argument_list|)
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
literal|"passenger"
operator|.
name|equals
argument_list|(
name|ele
operator|.
name|getLocalName
argument_list|()
argument_list|)
condition|)
block|{
name|Element
name|passenger
init|=
name|ele
decl_stmt|;
name|assertNotNull
argument_list|(
name|passenger
argument_list|)
expr_stmt|;
name|Element
name|child
init|=
name|DOMUtils
operator|.
name|getFirstElement
argument_list|(
name|passenger
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"passenger should have a child element"
argument_list|,
name|child
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"name"
argument_list|,
name|child
operator|.
name|getLocalName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Bob"
argument_list|,
name|child
operator|.
name|getTextContent
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
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
literal|"Application/xop+xml"
argument_list|)
decl_stmt|;
name|String
name|cid
init|=
name|AttachmentUtil
operator|.
name|createContentID
argument_list|(
literal|"http://cxf.apache.org"
argument_list|)
decl_stmt|;
name|soapMessage
operator|.
name|setContent
argument_list|(
name|Attachment
operator|.
name|class
argument_list|,
operator|new
name|AttachmentImpl
argument_list|(
name|cid
argument_list|,
operator|new
name|DataHandler
argument_list|(
name|bads
argument_list|)
argument_list|)
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

