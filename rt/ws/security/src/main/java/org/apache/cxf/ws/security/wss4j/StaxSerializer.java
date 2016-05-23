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
name|ws
operator|.
name|security
operator|.
name|wss4j
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
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|SequenceInputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|UnsupportedEncodingException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Vector
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|namespace
operator|.
name|NamespaceContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|soap
operator|.
name|SOAPEnvelope
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
name|XMLInputFactory
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
name|dom
operator|.
name|DOMResult
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
name|DocumentFragment
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
name|w3c
operator|.
name|dom
operator|.
name|NamedNodeMap
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
name|Node
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
name|InputSource
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
name|saaj
operator|.
name|SAAJStreamWriter
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
name|LoadingByteArrayOutputStream
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
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|AbstractSerializer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|xml
operator|.
name|security
operator|.
name|encryption
operator|.
name|XMLEncryptionException
import|;
end_import

begin_comment
comment|/**  * Converts<code>String</code>s into<code>Node</code>s and visa versa using CXF's StaxUtils  */
end_comment

begin_class
specifier|public
class|class
name|StaxSerializer
extends|extends
name|AbstractSerializer
block|{
name|XMLInputFactory
name|factory
decl_stmt|;
name|boolean
name|validFactory
decl_stmt|;
name|boolean
name|addNamespaces
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|,
name|Node
name|ctx
parameter_list|)
block|{
try|try
block|{
name|NamespaceContext
name|nsctx
init|=
name|reader
operator|.
name|getNamespaceContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|nsctx
operator|instanceof
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|sr
operator|.
name|InputElementStack
condition|)
block|{
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|sr
operator|.
name|InputElementStack
name|ies
init|=
operator|(
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|sr
operator|.
name|InputElementStack
operator|)
name|nsctx
decl_stmt|;
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|util
operator|.
name|InternCache
name|ic
init|=
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|util
operator|.
name|InternCache
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|storedNamespaces
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Node
name|wk
init|=
name|ctx
decl_stmt|;
while|while
condition|(
name|wk
operator|!=
literal|null
condition|)
block|{
name|NamedNodeMap
name|atts
init|=
name|wk
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|atts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|atts
operator|.
name|getLength
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|Node
name|att
init|=
name|atts
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|nodeName
init|=
name|att
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|nodeName
argument_list|)
operator|||
name|nodeName
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
operator|)
operator|&&
operator|!
name|storedNamespaces
operator|.
name|containsKey
argument_list|(
name|att
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|String
name|prefix
init|=
name|att
operator|.
name|getLocalName
argument_list|()
decl_stmt|;
if|if
condition|(
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|prefix
argument_list|)
condition|)
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
name|prefix
operator|=
name|ic
operator|.
name|intern
argument_list|(
name|prefix
argument_list|)
expr_stmt|;
name|ies
operator|.
name|addNsBinding
argument_list|(
name|prefix
argument_list|,
name|att
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|storedNamespaces
operator|.
name|put
argument_list|(
name|nodeName
argument_list|,
name|att
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|wk
operator|=
name|wk
operator|.
name|getParentNode
argument_list|()
expr_stmt|;
block|}
block|}
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
name|t
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|XMLStreamReader
name|createWstxReader
parameter_list|(
name|byte
index|[]
name|source
parameter_list|,
name|Node
name|ctx
parameter_list|)
throws|throws
name|XMLEncryptionException
block|{
try|try
block|{
if|if
condition|(
name|factory
operator|==
literal|null
condition|)
block|{
name|factory
operator|=
name|StaxUtils
operator|.
name|createXMLInputFactory
argument_list|(
literal|true
argument_list|)
expr_stmt|;
try|try
block|{
name|factory
operator|.
name|setProperty
argument_list|(
literal|"com.ctc.wstx.fragmentMode"
argument_list|,
name|com
operator|.
name|ctc
operator|.
name|wstx
operator|.
name|api
operator|.
name|WstxInputProperties
operator|.
name|PARSING_MODE_FRAGMENT
argument_list|)
expr_stmt|;
name|factory
operator|.
name|setProperty
argument_list|(
name|org
operator|.
name|codehaus
operator|.
name|stax2
operator|.
name|XMLInputFactory2
operator|.
name|P_REPORT_PROLOG_WHITESPACE
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
name|validFactory
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|//ignore
name|validFactory
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
name|validFactory
condition|)
block|{
name|XMLStreamReader
name|reader
init|=
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|source
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|addNamespaces
argument_list|(
name|reader
argument_list|,
name|ctx
argument_list|)
condition|)
block|{
return|return
name|reader
return|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|//ignore
block|}
return|return
literal|null
return|;
block|}
comment|/**      * @param source      * @param ctx      * @return the Node resulting from the parse of the source      * @throws XMLEncryptionException      */
specifier|public
name|Node
name|deserialize
parameter_list|(
name|byte
index|[]
name|source
parameter_list|,
name|Node
name|ctx
parameter_list|)
throws|throws
name|XMLEncryptionException
block|{
name|XMLStreamReader
name|reader
init|=
name|createWstxReader
argument_list|(
name|source
argument_list|,
name|ctx
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|!=
literal|null
condition|)
block|{
return|return
name|deserialize
argument_list|(
name|ctx
argument_list|,
name|reader
argument_list|,
literal|false
argument_list|)
return|;
block|}
return|return
name|deserialize
argument_list|(
name|ctx
argument_list|,
operator|new
name|InputSource
argument_list|(
name|createStreamContext
argument_list|(
name|source
argument_list|,
name|ctx
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
name|InputStream
name|createStreamContext
parameter_list|(
name|byte
index|[]
name|source
parameter_list|,
name|Node
name|ctx
parameter_list|)
throws|throws
name|XMLEncryptionException
block|{
name|Vector
argument_list|<
name|InputStream
argument_list|>
name|v
init|=
operator|new
name|Vector
argument_list|<>
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|LoadingByteArrayOutputStream
name|byteArrayOutputStream
init|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|OutputStreamWriter
name|outputStreamWriter
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|byteArrayOutputStream
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|"<?xml version=\"1.0\" encoding=\"UTF-8\"?><dummy"
argument_list|)
expr_stmt|;
comment|// Run through each node up to the document node and find any xmlns: nodes
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|storedNamespaces
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|Node
name|wk
init|=
name|ctx
decl_stmt|;
while|while
condition|(
name|wk
operator|!=
literal|null
condition|)
block|{
name|NamedNodeMap
name|atts
init|=
name|wk
operator|.
name|getAttributes
argument_list|()
decl_stmt|;
if|if
condition|(
name|atts
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|atts
operator|.
name|getLength
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|Node
name|att
init|=
name|atts
operator|.
name|item
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|String
name|nodeName
init|=
name|att
operator|.
name|getNodeName
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
literal|"xmlns"
operator|.
name|equals
argument_list|(
name|nodeName
argument_list|)
operator|||
name|nodeName
operator|.
name|startsWith
argument_list|(
literal|"xmlns:"
argument_list|)
operator|)
operator|&&
operator|!
name|storedNamespaces
operator|.
name|containsKey
argument_list|(
name|att
operator|.
name|getNodeName
argument_list|()
argument_list|)
condition|)
block|{
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|" "
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
name|nodeName
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|"=\""
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
name|att
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|"\""
argument_list|)
expr_stmt|;
name|storedNamespaces
operator|.
name|put
argument_list|(
name|nodeName
argument_list|,
name|att
operator|.
name|getNodeValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|wk
operator|=
name|wk
operator|.
name|getParentNode
argument_list|()
expr_stmt|;
block|}
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|">"
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|v
operator|.
name|add
argument_list|(
name|byteArrayOutputStream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
name|v
operator|.
name|addElement
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|source
argument_list|)
argument_list|)
expr_stmt|;
name|byteArrayOutputStream
operator|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
expr_stmt|;
name|outputStreamWriter
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|byteArrayOutputStream
argument_list|,
literal|"UTF-8"
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|write
argument_list|(
literal|"</dummy>"
argument_list|)
expr_stmt|;
name|outputStreamWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|v
operator|.
name|add
argument_list|(
name|byteArrayOutputStream
operator|.
name|createInputStream
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedEncodingException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLEncryptionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|XMLEncryptionException
argument_list|(
name|e
argument_list|)
throw|;
block|}
return|return
operator|new
name|SequenceInputStream
argument_list|(
name|v
operator|.
name|elements
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * @param source      * @param ctx      * @return the Node resulting from the parse of the source      * @throws XMLEncryptionException      */
specifier|public
name|Node
name|deserialize
parameter_list|(
name|String
name|source
parameter_list|,
name|Node
name|ctx
parameter_list|)
throws|throws
name|XMLEncryptionException
block|{
name|String
name|fragment
init|=
name|createContext
argument_list|(
name|source
argument_list|,
name|ctx
argument_list|)
decl_stmt|;
return|return
name|deserialize
argument_list|(
name|ctx
argument_list|,
operator|new
name|InputSource
argument_list|(
operator|new
name|StringReader
argument_list|(
name|fragment
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|serializeToByteArray
parameter_list|(
name|Element
name|element
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|element
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|byte
index|[]
name|serializeToByteArray
parameter_list|(
name|NodeList
name|content
parameter_list|)
throws|throws
name|Exception
block|{
try|try
init|(
name|ByteArrayOutputStream
name|baos
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
init|)
block|{
name|XMLStreamWriter
name|writer
init|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|baos
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|content
operator|.
name|getLength
argument_list|()
condition|;
name|i
operator|++
control|)
block|{
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|new
name|DOMSource
argument_list|(
name|content
operator|.
name|item
argument_list|(
name|i
argument_list|)
argument_list|)
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|baos
operator|.
name|toByteArray
argument_list|()
return|;
block|}
block|}
comment|/**      * @param ctx      * @param inputSource      * @return the Node resulting from the parse of the source      * @throws XMLEncryptionException      */
specifier|private
name|Node
name|deserialize
parameter_list|(
name|Node
name|ctx
parameter_list|,
name|InputSource
name|inputSource
parameter_list|)
throws|throws
name|XMLEncryptionException
block|{
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|inputSource
argument_list|)
decl_stmt|;
return|return
name|deserialize
argument_list|(
name|ctx
argument_list|,
name|reader
argument_list|,
literal|true
argument_list|)
return|;
block|}
specifier|private
name|Node
name|deserialize
parameter_list|(
name|Node
name|ctx
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|,
name|boolean
name|wrapped
parameter_list|)
throws|throws
name|XMLEncryptionException
block|{
name|Document
name|contextDocument
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|Node
operator|.
name|DOCUMENT_NODE
operator|==
name|ctx
operator|.
name|getNodeType
argument_list|()
condition|)
block|{
name|contextDocument
operator|=
operator|(
name|Document
operator|)
name|ctx
expr_stmt|;
block|}
else|else
block|{
name|contextDocument
operator|=
name|ctx
operator|.
name|getOwnerDocument
argument_list|()
expr_stmt|;
block|}
name|XMLStreamWriter
name|writer
init|=
literal|null
decl_stmt|;
try|try
block|{
if|if
condition|(
name|ctx
operator|instanceof
name|SOAPElement
condition|)
block|{
name|SOAPElement
name|el
init|=
operator|(
name|SOAPElement
operator|)
name|ctx
decl_stmt|;
while|while
condition|(
name|el
operator|!=
literal|null
operator|&&
operator|!
operator|(
name|el
operator|instanceof
name|SOAPEnvelope
operator|)
condition|)
block|{
name|el
operator|=
name|el
operator|.
name|getParentElement
argument_list|()
expr_stmt|;
block|}
comment|//cannot load into fragment due to a ClassCastException iwthin SAAJ addChildElement
comment|//which only checks for Document as parent, not DocumentFragment
name|Element
name|element
init|=
name|ctx
operator|.
name|getOwnerDocument
argument_list|()
operator|.
name|createElementNS
argument_list|(
literal|"dummy"
argument_list|,
literal|"dummy"
argument_list|)
decl_stmt|;
name|writer
operator|=
operator|new
name|SAAJStreamWriter
argument_list|(
operator|(
name|SOAPEnvelope
operator|)
name|el
argument_list|,
name|element
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|DocumentFragment
name|result
init|=
name|contextDocument
operator|.
name|createDocumentFragment
argument_list|()
decl_stmt|;
name|Node
name|child
init|=
name|element
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|wrapped
condition|)
block|{
name|child
operator|=
name|child
operator|.
name|getFirstChild
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|child
operator|!=
literal|null
operator|&&
name|child
operator|.
name|getNextSibling
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|child
return|;
block|}
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|Node
name|nextChild
init|=
name|child
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
name|result
operator|.
name|appendChild
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|child
operator|=
name|nextChild
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
comment|// Import to a dummy fragment
name|DocumentFragment
name|dummyFragment
init|=
name|contextDocument
operator|.
name|createDocumentFragment
argument_list|()
decl_stmt|;
name|writer
operator|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
operator|new
name|DOMResult
argument_list|(
name|dummyFragment
argument_list|)
argument_list|)
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|reader
argument_list|,
name|writer
argument_list|)
expr_stmt|;
comment|// Remove the "dummy" wrapper
if|if
condition|(
name|wrapped
condition|)
block|{
name|DocumentFragment
name|result
init|=
name|contextDocument
operator|.
name|createDocumentFragment
argument_list|()
decl_stmt|;
name|Node
name|child
init|=
name|dummyFragment
operator|.
name|getFirstChild
argument_list|()
operator|.
name|getFirstChild
argument_list|()
decl_stmt|;
if|if
condition|(
name|child
operator|!=
literal|null
operator|&&
name|child
operator|.
name|getNextSibling
argument_list|()
operator|==
literal|null
condition|)
block|{
return|return
name|child
return|;
block|}
while|while
condition|(
name|child
operator|!=
literal|null
condition|)
block|{
name|Node
name|nextChild
init|=
name|child
operator|.
name|getNextSibling
argument_list|()
decl_stmt|;
name|result
operator|.
name|appendChild
argument_list|(
name|child
argument_list|)
expr_stmt|;
name|child
operator|=
name|nextChild
expr_stmt|;
block|}
name|dummyFragment
operator|=
name|result
expr_stmt|;
block|}
return|return
name|dummyFragment
return|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|XMLEncryptionException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
