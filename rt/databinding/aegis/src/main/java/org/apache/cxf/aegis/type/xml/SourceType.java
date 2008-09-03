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
name|aegis
operator|.
name|type
operator|.
name|xml
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|FactoryConfigurationError
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
name|Source
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
name|dom
operator|.
name|DOMSource
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
name|Element
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
name|xml
operator|.
name|sax
operator|.
name|XMLReader
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
name|helpers
operator|.
name|XMLReaderFactory
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
name|aegis
operator|.
name|Context
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
name|aegis
operator|.
name|DatabindingException
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
name|aegis
operator|.
name|type
operator|.
name|Type
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
name|aegis
operator|.
name|xml
operator|.
name|MessageReader
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
name|aegis
operator|.
name|xml
operator|.
name|MessageWriter
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
name|aegis
operator|.
name|xml
operator|.
name|stax
operator|.
name|ElementWriter
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

begin_comment
comment|/**  * Reads and writes<code>javax.xml.transform.Source</code> types.  *<p>  * The XML stream is converted DOMSource and sent off.  *   * @author<a href="mailto:dan@envoisolutions.com">Dan Diephouse</a>  * @see javanet.staxutils.StAXSource  * @see javax.xml.stream.XMLInputFactory  * @see org.apache.cxf.aegis.util.STAXUtils  */
end_comment

begin_class
specifier|public
class|class
name|SourceType
extends|extends
name|Type
block|{
specifier|public
name|SourceType
parameter_list|()
block|{
name|setTypeClass
argument_list|(
name|Source
operator|.
name|class
argument_list|)
expr_stmt|;
name|setWriteOuter
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|readObject
parameter_list|(
name|MessageReader
name|mreader
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
name|DocumentType
name|dt
init|=
operator|(
name|DocumentType
operator|)
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|Document
operator|.
name|class
argument_list|)
decl_stmt|;
return|return
operator|new
name|DOMSource
argument_list|(
operator|(
name|Document
operator|)
name|dt
operator|.
name|readObject
argument_list|(
name|mreader
argument_list|,
name|context
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeObject
parameter_list|(
name|Object
name|object
parameter_list|,
name|MessageWriter
name|writer
parameter_list|,
name|Context
name|context
parameter_list|)
throws|throws
name|DatabindingException
block|{
try|try
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|write
argument_list|(
operator|(
name|Source
operator|)
name|object
argument_list|,
operator|(
operator|(
name|ElementWriter
operator|)
name|writer
operator|)
operator|.
name|getXMLStreamWriter
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Could not write xml."
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|write
parameter_list|(
name|Source
name|object
parameter_list|,
name|XMLStreamWriter
name|writer
parameter_list|)
throws|throws
name|FactoryConfigurationError
throws|,
name|XMLStreamException
throws|,
name|DatabindingException
block|{
if|if
condition|(
name|object
operator|==
literal|null
condition|)
block|{
return|return;
block|}
if|if
condition|(
name|object
operator|instanceof
name|DOMSource
condition|)
block|{
name|DOMSource
name|ds
init|=
operator|(
name|DOMSource
operator|)
name|object
decl_stmt|;
name|Element
name|element
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|ds
operator|.
name|getNode
argument_list|()
operator|instanceof
name|Element
condition|)
block|{
name|element
operator|=
operator|(
name|Element
operator|)
name|ds
operator|.
name|getNode
argument_list|()
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|ds
operator|.
name|getNode
argument_list|()
operator|instanceof
name|Document
condition|)
block|{
name|element
operator|=
operator|(
operator|(
name|Document
operator|)
name|ds
operator|.
name|getNode
argument_list|()
operator|)
operator|.
name|getDocumentElement
argument_list|()
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|DatabindingException
argument_list|(
literal|"Node type "
operator|+
name|ds
operator|.
name|getNode
argument_list|()
operator|.
name|getClass
argument_list|()
operator|+
literal|" was not understood."
argument_list|)
throw|;
block|}
name|StaxUtils
operator|.
name|writeElement
argument_list|(
name|element
argument_list|,
name|writer
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|(
name|Source
operator|)
name|object
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|XMLReader
name|createXMLReader
parameter_list|()
throws|throws
name|SAXException
block|{
comment|// In JDK 1.4, the xml reader factory does not look for META-INF
comment|// services
comment|// If the org.xml.sax.driver system property is not defined, and
comment|// exception will be thrown.
comment|// In these cases, default to xerces parser
try|try
block|{
return|return
name|XMLReaderFactory
operator|.
name|createXMLReader
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
return|return
name|XMLReaderFactory
operator|.
name|createXMLReader
argument_list|(
literal|"org.apache.xerces.parsers.SAXParser"
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

