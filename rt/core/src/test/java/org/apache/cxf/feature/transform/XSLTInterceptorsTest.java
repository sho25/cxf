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
name|feature
operator|.
name|transform
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
name|io
operator|.
name|InputStreamReader
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
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Writer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|ParserConfigurationException
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
name|XMLStreamException
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|TransformerConfigurationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamSource
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
name|Document
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
name|NodeList
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
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
name|classloader
operator|.
name|ClassLoaderUtils
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
name|helpers
operator|.
name|IOUtils
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
name|io
operator|.
name|CachedOutputStream
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
name|io
operator|.
name|CachedWriter
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
name|message
operator|.
name|MessageImpl
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

begin_comment
comment|/* Provides XSLT transformation of incoming message.  * Interceptor breaks streaming (can be fixed in further versions when XSLT engine supports XML stream)  */
end_comment

begin_class
specifier|public
class|class
name|XSLTInterceptorsTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TRANSFORMATION_XSL
init|=
literal|"transformation.xsl"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|MESSAGE_FILE
init|=
literal|"message.xml"
decl_stmt|;
specifier|private
name|InputStream
name|messageIS
decl_stmt|;
specifier|private
name|Message
name|message
decl_stmt|;
specifier|private
name|XSLTInInterceptor
name|inInterceptor
decl_stmt|;
specifier|private
name|XSLTOutInterceptor
name|outInterceptor
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|TransformerConfigurationException
block|{
name|messageIS
operator|=
name|ClassLoaderUtils
operator|.
name|getResourceAsStream
argument_list|(
name|MESSAGE_FILE
argument_list|,
name|this
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|messageIS
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot load message from path: "
operator|+
name|MESSAGE_FILE
argument_list|)
throw|;
block|}
name|message
operator|=
operator|new
name|MessageImpl
argument_list|()
expr_stmt|;
name|inInterceptor
operator|=
operator|new
name|XSLTInInterceptor
argument_list|(
name|TRANSFORMATION_XSL
argument_list|)
expr_stmt|;
name|outInterceptor
operator|=
operator|new
name|XSLTOutInterceptor
argument_list|(
name|TRANSFORMATION_XSL
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|inStreamTest
parameter_list|()
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|message
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|messageIS
argument_list|)
expr_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|InputStream
name|transformedIS
init|=
name|message
operator|.
name|getContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|transformedIS
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Message was not transformed"
argument_list|,
name|checkTransformedXML
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|inXMLStreamTest
parameter_list|()
throws|throws
name|XMLStreamException
block|{
name|XMLStreamReader
name|xReader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|messageIS
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|,
name|xReader
argument_list|)
expr_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|XMLStreamReader
name|transformedXReader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|StaxUtils
operator|.
name|read
argument_list|(
name|transformedXReader
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Message was not transformed"
argument_list|,
name|checkTransformedXML
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|inReaderTest
parameter_list|()
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|Reader
name|reader
init|=
operator|new
name|InputStreamReader
argument_list|(
name|messageIS
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|,
name|reader
argument_list|)
expr_stmt|;
name|inInterceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Reader
name|transformedReader
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
decl_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|transformedReader
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Message was not transformed"
argument_list|,
name|checkTransformedXML
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|outStreamTest
parameter_list|()
throws|throws
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|CachedOutputStream
name|cos
init|=
operator|new
name|CachedOutputStream
argument_list|()
decl_stmt|;
name|cos
operator|.
name|holdTempFile
argument_list|()
expr_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|,
name|cos
argument_list|)
expr_stmt|;
name|outInterceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|OutputStream
name|os
init|=
name|message
operator|.
name|getContent
argument_list|(
name|OutputStream
operator|.
name|class
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|messageIS
argument_list|,
name|os
argument_list|)
expr_stmt|;
name|os
operator|.
name|close
argument_list|()
expr_stmt|;
name|cos
operator|.
name|releaseTempFileHold
argument_list|()
expr_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|cos
operator|.
name|getInputStream
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Message was not transformed"
argument_list|,
name|checkTransformedXML
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|outXMLStreamTest
parameter_list|()
throws|throws
name|XMLStreamException
throws|,
name|SAXException
throws|,
name|IOException
throws|,
name|ParserConfigurationException
block|{
name|CachedWriter
name|cWriter
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
name|cWriter
operator|.
name|holdTempFile
argument_list|()
expr_stmt|;
name|XMLStreamWriter
name|xWriter
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|cWriter
argument_list|)
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|,
name|xWriter
argument_list|)
expr_stmt|;
name|outInterceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|XMLStreamWriter
name|tXWriter
init|=
name|message
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|messageIS
argument_list|)
argument_list|,
name|tXWriter
argument_list|)
expr_stmt|;
name|tXWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|cWriter
operator|.
name|releaseTempFileHold
argument_list|()
expr_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|cWriter
operator|.
name|getReader
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Message was not transformed"
argument_list|,
name|checkTransformedXML
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|outWriterStreamTest
parameter_list|()
throws|throws
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
name|CachedWriter
name|cWriter
init|=
operator|new
name|CachedWriter
argument_list|()
decl_stmt|;
name|message
operator|.
name|setContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|,
name|cWriter
argument_list|)
expr_stmt|;
name|outInterceptor
operator|.
name|handleMessage
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|Writer
name|tWriter
init|=
name|message
operator|.
name|getContent
argument_list|(
name|Writer
operator|.
name|class
argument_list|)
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
operator|new
name|InputStreamReader
argument_list|(
name|messageIS
argument_list|)
argument_list|,
name|tWriter
argument_list|,
name|IOUtils
operator|.
name|DEFAULT_BUFFER_SIZE
argument_list|)
expr_stmt|;
name|tWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|Document
name|doc
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|cWriter
operator|.
name|getReader
argument_list|()
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"Message was not transformed"
argument_list|,
name|checkTransformedXML
argument_list|(
name|doc
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|boolean
name|checkTransformedXML
parameter_list|(
name|Document
name|doc
parameter_list|)
block|{
name|NodeList
name|list
init|=
name|doc
operator|.
name|getDocumentElement
argument_list|()
operator|.
name|getElementsByTagNameNS
argument_list|(
literal|"http://customerservice.example.com/"
argument_list|,
literal|"getCustomersByName1"
argument_list|)
decl_stmt|;
return|return
name|list
operator|.
name|getLength
argument_list|()
operator|==
literal|1
return|;
block|}
block|}
end_class

end_unit

