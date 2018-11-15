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
name|bus
operator|.
name|spring
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedInputStream
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
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|XMLConstants
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
name|DocumentBuilderFactory
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
name|parsers
operator|.
name|SAXParser
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
name|SAXParserFactory
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
name|transform
operator|.
name|sax
operator|.
name|SAXSource
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
name|xml
operator|.
name|sax
operator|.
name|EntityResolver
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
name|ErrorHandler
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
name|InputSource
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
name|XMLReader
import|;
end_import

begin_import
import|import
name|com
operator|.
name|sun
operator|.
name|xml
operator|.
name|fastinfoset
operator|.
name|stax
operator|.
name|StAXDocumentParser
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
name|apache
operator|.
name|cxf
operator|.
name|staxutils
operator|.
name|W3CDOMStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|DefaultDocumentLoader
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|xml
operator|.
name|XmlBeanDefinitionReader
import|;
end_import

begin_comment
comment|/**  * A Spring DocumentLoader that uses WoodStox when we are not validating to speed up the process.  */
end_comment

begin_class
class|class
name|TunedDocumentLoader
extends|extends
name|DefaultDocumentLoader
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|TunedDocumentLoader
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
name|boolean
name|hasFastInfoSet
decl_stmt|;
static|static
block|{
try|try
block|{
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"com.sun.xml.fastinfoset.stax.StAXDocumentParser"
argument_list|,
name|TunedDocumentLoader
operator|.
name|class
argument_list|)
expr_stmt|;
name|hasFastInfoSet
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"FastInfoset not found on classpath. Disabling context load optimizations."
argument_list|)
expr_stmt|;
name|hasFastInfoSet
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|private
name|SAXParserFactory
name|saxParserFactory
decl_stmt|;
specifier|private
name|SAXParserFactory
name|nsasaxParserFactory
decl_stmt|;
name|TunedDocumentLoader
parameter_list|()
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|cls
init|=
name|ClassLoaderUtils
operator|.
name|loadClass
argument_list|(
literal|"com.ctc.wstx.sax.WstxSAXParserFactory"
argument_list|,
name|TunedDocumentLoader
operator|.
name|class
argument_list|)
decl_stmt|;
name|saxParserFactory
operator|=
operator|(
name|SAXParserFactory
operator|)
name|cls
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|nsasaxParserFactory
operator|=
operator|(
name|SAXParserFactory
operator|)
name|cls
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//woodstox not found, use any other Stax parser
name|saxParserFactory
operator|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
name|nsasaxParserFactory
operator|=
name|SAXParserFactory
operator|.
name|newInstance
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|nsasaxParserFactory
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/namespaces"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|nsasaxParserFactory
operator|.
name|setFeature
argument_list|(
literal|"http://xml.org/sax/features/namespace-prefixes"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|saxParserFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|nsasaxParserFactory
operator|.
name|setFeature
argument_list|(
name|XMLConstants
operator|.
name|FEATURE_SECURE_PROCESSING
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//ignore
block|}
block|}
specifier|public
specifier|static
name|boolean
name|hasFastInfoSet
parameter_list|()
block|{
return|return
name|hasFastInfoSet
return|;
block|}
annotation|@
name|Override
specifier|public
name|Document
name|loadDocument
parameter_list|(
name|InputSource
name|inputSource
parameter_list|,
name|EntityResolver
name|entityResolver
parameter_list|,
name|ErrorHandler
name|errorHandler
parameter_list|,
name|int
name|validationMode
parameter_list|,
name|boolean
name|namespaceAware
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|validationMode
operator|==
name|XmlBeanDefinitionReader
operator|.
name|VALIDATION_NONE
condition|)
block|{
name|SAXParserFactory
name|parserFactory
init|=
name|namespaceAware
condition|?
name|nsasaxParserFactory
else|:
name|saxParserFactory
decl_stmt|;
name|SAXParser
name|parser
init|=
name|parserFactory
operator|.
name|newSAXParser
argument_list|()
decl_stmt|;
name|XMLReader
name|reader
init|=
name|parser
operator|.
name|getXMLReader
argument_list|()
decl_stmt|;
name|reader
operator|.
name|setEntityResolver
argument_list|(
name|entityResolver
argument_list|)
expr_stmt|;
name|reader
operator|.
name|setErrorHandler
argument_list|(
name|errorHandler
argument_list|)
expr_stmt|;
name|SAXSource
name|saxSource
init|=
operator|new
name|SAXSource
argument_list|(
name|reader
argument_list|,
name|inputSource
argument_list|)
decl_stmt|;
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|saxSource
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|getDocument
argument_list|()
return|;
block|}
return|return
name|super
operator|.
name|loadDocument
argument_list|(
name|inputSource
argument_list|,
name|entityResolver
argument_list|,
name|errorHandler
argument_list|,
name|validationMode
argument_list|,
name|namespaceAware
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|DocumentBuilderFactory
name|createDocumentBuilderFactory
parameter_list|(
name|int
name|validationMode
parameter_list|,
name|boolean
name|namespaceAware
parameter_list|)
throws|throws
name|ParserConfigurationException
block|{
name|DocumentBuilderFactory
name|factory
init|=
name|super
operator|.
name|createDocumentBuilderFactory
argument_list|(
name|validationMode
argument_list|,
name|namespaceAware
argument_list|)
decl_stmt|;
try|try
block|{
name|factory
operator|.
name|setFeature
argument_list|(
literal|"http://apache.org/xml/features/dom/defer-node-expansion"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// we can get all kinds of exceptions from this
comment|// due to old copies of Xerces and whatnot.
block|}
return|return
name|factory
return|;
block|}
specifier|static
name|Document
name|loadFastinfosetDocument
parameter_list|(
name|URL
name|url
parameter_list|)
throws|throws
name|IOException
throws|,
name|ParserConfigurationException
throws|,
name|XMLStreamException
block|{
name|InputStream
name|is
init|=
name|url
operator|.
name|openStream
argument_list|()
decl_stmt|;
name|InputStream
name|in
init|=
operator|new
name|BufferedInputStream
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|staxReader
init|=
operator|new
name|StAXDocumentParser
argument_list|(
name|in
argument_list|)
decl_stmt|;
name|W3CDOMStreamWriter
name|writer
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|staxReader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|writer
operator|.
name|getDocument
argument_list|()
return|;
block|}
block|}
end_class

end_unit

