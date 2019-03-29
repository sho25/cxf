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
name|nio
operator|.
name|charset
operator|.
name|StandardCharsets
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
name|List
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
name|namespace
operator|.
name|QName
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
name|XMLStreamConstants
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
name|stream
operator|.
name|StreamSource
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
name|junit
operator|.
name|Assert
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|TransformTestUtils
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getLogger
argument_list|(
name|TransformTestUtils
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|TransformTestUtils
parameter_list|()
block|{     }
comment|// test utilities methods
specifier|static
name|void
name|transformInStreamAndCompare
parameter_list|(
name|String
name|inname
parameter_list|,
name|String
name|outname
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|dropElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformAttributes
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLStreamReader
name|reader
init|=
name|createInTransformedStreamReader
argument_list|(
name|inname
argument_list|,
name|transformElements
argument_list|,
name|appendElements
argument_list|,
name|dropElements
argument_list|,
name|transformAttributes
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|teacher
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|TransformTestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|outname
argument_list|)
argument_list|)
decl_stmt|;
name|verifyReaders
argument_list|(
name|teacher
argument_list|,
name|reader
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|static
name|void
name|transformOutStreamAndCompare
parameter_list|(
name|String
name|inname
parameter_list|,
name|String
name|outname
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|appendElements
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|dropElements
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|transformAttributes
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|XMLStreamReader
name|reader
init|=
name|createOutTransformedStreamReader
argument_list|(
name|inname
argument_list|,
name|transformElements
argument_list|,
name|appendElements
argument_list|,
name|dropElements
argument_list|,
name|transformAttributes
argument_list|,
literal|false
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|teacher
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|TransformTestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|outname
argument_list|)
argument_list|)
decl_stmt|;
name|verifyReaders
argument_list|(
name|teacher
argument_list|,
name|reader
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|static
name|XMLStreamReader
name|createInTransformedStreamReader
parameter_list|(
name|String
name|file
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|emap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|eappend
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|dropEls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|amap
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
operator|new
name|InTransformReader
argument_list|(
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|TransformTestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|,
name|emap
argument_list|,
name|eappend
argument_list|,
name|dropEls
argument_list|,
name|amap
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|static
name|XMLStreamReader
name|createOutTransformedStreamReader
parameter_list|(
name|String
name|file
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|emap
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|append
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|dropEls
parameter_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|amap
parameter_list|,
name|boolean
name|attributesToElements
parameter_list|,
name|String
name|defaultNamespace
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|ByteArrayOutputStream
name|os
init|=
operator|new
name|ByteArrayOutputStream
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
operator|new
name|OutTransformWriter
argument_list|(
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|,
name|StandardCharsets
operator|.
name|UTF_8
operator|.
name|name
argument_list|()
argument_list|)
argument_list|,
name|emap
argument_list|,
name|append
argument_list|,
name|dropEls
argument_list|,
name|amap
argument_list|,
name|attributesToElements
argument_list|,
name|defaultNamespace
argument_list|)
decl_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|TransformTestUtils
operator|.
name|class
operator|.
name|getResourceAsStream
argument_list|(
name|file
argument_list|)
argument_list|)
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|flush
argument_list|()
expr_stmt|;
return|return
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
operator|new
name|ByteArrayInputStream
argument_list|(
name|os
operator|.
name|toByteArray
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Verifies the two stream events are equivalent and throws an assertion      * exception at the first mismatch.      * @param teacher      * @param reader      * @param eec      * @throws XMLStreamException      */
specifier|static
name|void
name|verifyReaders
parameter_list|(
name|XMLStreamReader
name|teacher
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|,
name|boolean
name|eec
parameter_list|,
name|boolean
name|pfx
parameter_list|)
throws|throws
name|XMLStreamException
block|{
comment|// compare the elements and attributes while ignoring comments, line breaks, etc
for|for
control|(
init|;
condition|;
control|)
block|{
name|int
name|revent
init|=
name|getNextEvent
argument_list|(
name|reader
argument_list|)
decl_stmt|;
name|int
name|tevent
init|=
name|getNextEvent
argument_list|(
name|teacher
argument_list|)
decl_stmt|;
if|if
condition|(
name|revent
operator|==
operator|-
literal|1
operator|&&
name|tevent
operator|==
operator|-
literal|1
condition|)
block|{
break|break;
block|}
name|LOG
operator|.
name|fine
argument_list|(
literal|"Event: "
operator|+
name|tevent
operator|+
literal|" ? "
operator|+
name|revent
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"parsing event"
argument_list|,
name|tevent
argument_list|,
name|revent
argument_list|)
expr_stmt|;
switch|switch
condition|(
name|revent
condition|)
block|{
case|case
name|XMLStreamConstants
operator|.
name|START_ELEMENT
case|:
name|LOG
operator|.
name|fine
argument_list|(
literal|"Start Element "
operator|+
name|teacher
operator|.
name|getName
argument_list|()
operator|+
literal|" ? "
operator|+
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"wrong start element."
argument_list|,
name|teacher
operator|.
name|getName
argument_list|()
argument_list|,
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|pfx
condition|)
block|{
comment|// verify if the namespace prefix are preserved
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"wrong start element prefix."
argument_list|,
name|teacher
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|reader
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|verifyNamespaceDeclarations
argument_list|(
name|teacher
argument_list|,
name|reader
argument_list|)
expr_stmt|;
block|}
name|verifyAttributes
argument_list|(
name|teacher
argument_list|,
name|reader
argument_list|)
expr_stmt|;
break|break;
case|case
name|XMLStreamConstants
operator|.
name|END_ELEMENT
case|:
name|LOG
operator|.
name|fine
argument_list|(
literal|"End Element "
operator|+
name|teacher
operator|.
name|getName
argument_list|()
operator|+
literal|" ? "
operator|+
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|eec
condition|)
block|{
comment|// perform end-element-check
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"wrong end element qname."
argument_list|,
name|teacher
operator|.
name|getName
argument_list|()
argument_list|,
name|reader
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
break|break;
case|case
name|XMLStreamConstants
operator|.
name|CHARACTERS
case|:
name|LOG
operator|.
name|fine
argument_list|(
literal|"Characters "
operator|+
name|teacher
operator|.
name|getText
argument_list|()
operator|+
literal|" ? "
operator|+
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"wrong characteres."
argument_list|,
name|teacher
operator|.
name|getText
argument_list|()
argument_list|,
name|reader
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
break|break;
default|default:
block|}
block|}
block|}
specifier|private
specifier|static
name|void
name|verifyAttributes
parameter_list|(
name|XMLStreamReader
name|teacher
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|int
name|acount
init|=
name|teacher
operator|.
name|getAttributeCount
argument_list|()
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
name|acount
argument_list|,
name|reader
operator|.
name|getAttributeCount
argument_list|()
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|QName
argument_list|,
name|String
argument_list|>
name|attributesMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
comment|// temporarily store all the attributes
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|acount
condition|;
name|i
operator|++
control|)
block|{
name|attributesMap
operator|.
name|put
argument_list|(
name|reader
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
argument_list|,
name|reader
operator|.
name|getAttributeValue
argument_list|(
name|i
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// compares each attribute
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|acount
condition|;
name|i
operator|++
control|)
block|{
name|String
name|avalue
init|=
name|attributesMap
operator|.
name|remove
argument_list|(
name|teacher
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"attribute "
operator|+
name|teacher
operator|.
name|getAttributeName
argument_list|(
name|i
argument_list|)
operator|+
literal|" has wrong value."
argument_list|,
name|teacher
operator|.
name|getAttributeValue
argument_list|(
name|i
argument_list|)
argument_list|,
name|avalue
argument_list|)
expr_stmt|;
block|}
comment|// attributes must be exhausted
name|Assert
operator|.
name|assertTrue
argument_list|(
literal|"attributes must be exhausted."
argument_list|,
name|attributesMap
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|verifyNamespaceDeclarations
parameter_list|(
name|XMLStreamReader
name|teacher
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|int
name|dcount
init|=
name|teacher
operator|.
name|getNamespaceCount
argument_list|()
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
name|dcount
condition|;
name|i
operator|++
control|)
block|{
name|String
name|p
init|=
name|teacher
operator|.
name|getNamespacePrefix
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"nsdecl prefix "
operator|+
name|p
operator|+
literal|" is incorrectly bound."
argument_list|,
name|teacher
operator|.
name|getNamespaceURI
argument_list|(
name|i
argument_list|)
argument_list|,
name|reader
operator|.
name|getNamespaceURI
argument_list|(
name|p
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Returns the next relevant reader event.      *      * @param reader      * @return      * @throws XMLStreamException      */
specifier|private
specifier|static
name|int
name|getNextEvent
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
throws|throws
name|XMLStreamException
block|{
while|while
condition|(
name|reader
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|int
name|e
init|=
name|reader
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|e
operator|==
name|XMLStreamConstants
operator|.
name|END_DOCUMENT
condition|)
block|{
return|return
name|e
return|;
block|}
if|if
condition|(
name|e
operator|==
name|XMLStreamConstants
operator|.
name|START_ELEMENT
operator|||
name|e
operator|==
name|XMLStreamConstants
operator|.
name|END_ELEMENT
condition|)
block|{
return|return
name|e
return|;
block|}
elseif|else
if|if
condition|(
name|e
operator|==
name|XMLStreamConstants
operator|.
name|CHARACTERS
condition|)
block|{
name|String
name|text
init|=
name|reader
operator|.
name|getText
argument_list|()
decl_stmt|;
if|if
condition|(
name|text
operator|.
name|trim
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
continue|continue;
block|}
return|return
name|e
return|;
block|}
block|}
return|return
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

