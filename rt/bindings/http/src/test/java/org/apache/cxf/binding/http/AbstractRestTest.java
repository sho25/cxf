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
name|http
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
name|java
operator|.
name|net
operator|.
name|HttpURLConnection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|ProtocolException
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
name|transform
operator|.
name|TransformerException
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
name|Bus
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
name|BusException
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
name|bus
operator|.
name|spring
operator|.
name|SpringBusFactory
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
name|test
operator|.
name|AbstractCXFTest
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRestTest
extends|extends
name|AbstractCXFTest
block|{
name|boolean
name|debug
decl_stmt|;
specifier|protected
name|Bus
name|createBus
parameter_list|()
throws|throws
name|BusException
block|{
return|return
operator|new
name|SpringBusFactory
argument_list|()
operator|.
name|createBus
argument_list|()
return|;
block|}
specifier|protected
name|Document
name|get
parameter_list|(
name|String
name|urlStr
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
return|return
name|get
argument_list|(
name|urlStr
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|Document
name|get
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|Integer
name|resCode
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|c
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
if|if
condition|(
name|resCode
operator|!=
literal|null
condition|)
block|{
name|assertEquals
argument_list|(
name|resCode
operator|.
name|intValue
argument_list|()
argument_list|,
name|c
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|InputStream
name|is
decl_stmt|;
if|if
condition|(
name|c
operator|.
name|getResponseCode
argument_list|()
operator|>=
literal|400
condition|)
block|{
name|is
operator|=
name|c
operator|.
name|getErrorStream
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|is
operator|=
name|c
operator|.
name|getInputStream
argument_list|()
expr_stmt|;
block|}
return|return
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|protected
name|Document
name|post
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|String
name|message
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
return|return
name|doMethod
argument_list|(
name|urlStr
argument_list|,
name|message
argument_list|,
literal|"POST"
argument_list|)
return|;
block|}
specifier|protected
name|Document
name|put
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|String
name|message
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
return|return
name|doMethod
argument_list|(
name|urlStr
argument_list|,
name|message
argument_list|,
literal|"PUT"
argument_list|)
return|;
block|}
specifier|protected
name|Document
name|doMethod
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|method
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
return|return
name|doMethod
argument_list|(
name|urlStr
argument_list|,
name|message
argument_list|,
name|method
argument_list|,
literal|"application/xml"
argument_list|)
return|;
block|}
specifier|protected
name|Document
name|doMethod
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|ct
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
name|InputStream
name|is
init|=
name|invoke
argument_list|(
name|urlStr
argument_list|,
name|message
argument_list|,
name|method
argument_list|,
name|ct
argument_list|)
decl_stmt|;
name|Document
name|res
init|=
name|DOMUtils
operator|.
name|readXml
argument_list|(
name|is
argument_list|)
decl_stmt|;
if|if
condition|(
name|debug
condition|)
block|{
try|try
block|{
name|DOMUtils
operator|.
name|writeXml
argument_list|(
name|res
argument_list|,
name|System
operator|.
name|out
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
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
block|}
return|return
name|res
return|;
block|}
specifier|protected
name|byte
index|[]
name|doMethodBytes
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|ct
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|SAXException
throws|,
name|ParserConfigurationException
block|{
name|InputStream
name|is
init|=
name|invoke
argument_list|(
name|urlStr
argument_list|,
name|message
argument_list|,
name|method
argument_list|,
name|ct
argument_list|)
decl_stmt|;
name|ByteArrayOutputStream
name|out
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|is
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|is
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|out
operator|.
name|toByteArray
argument_list|()
return|;
block|}
specifier|private
name|InputStream
name|invoke
parameter_list|(
name|String
name|urlStr
parameter_list|,
name|String
name|message
parameter_list|,
name|String
name|method
parameter_list|,
name|String
name|ct
parameter_list|)
throws|throws
name|MalformedURLException
throws|,
name|IOException
throws|,
name|ProtocolException
block|{
name|URL
name|url
init|=
operator|new
name|URL
argument_list|(
name|urlStr
argument_list|)
decl_stmt|;
name|HttpURLConnection
name|c
init|=
operator|(
name|HttpURLConnection
operator|)
name|url
operator|.
name|openConnection
argument_list|()
decl_stmt|;
name|c
operator|.
name|setRequestMethod
argument_list|(
name|method
argument_list|)
expr_stmt|;
if|if
condition|(
name|ct
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setRequestProperty
argument_list|(
literal|"Content-Type"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|message
operator|!=
literal|null
condition|)
block|{
name|c
operator|.
name|setDoOutput
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|OutputStream
name|out
init|=
name|c
operator|.
name|getOutputStream
argument_list|()
decl_stmt|;
name|InputStream
name|msgIs
init|=
name|getResourceAsStream
argument_list|(
name|message
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|msgIs
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|msgIs
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
name|msgIs
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|c
operator|.
name|getInputStream
argument_list|()
return|;
block|}
block|}
end_class

end_unit

