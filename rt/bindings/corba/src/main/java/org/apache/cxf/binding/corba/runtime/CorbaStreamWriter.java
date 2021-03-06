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
name|corba
operator|.
name|runtime
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayDeque
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Deque
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
name|HashSet
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
name|Set
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
name|binding
operator|.
name|corba
operator|.
name|CorbaBindingException
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
name|corba
operator|.
name|CorbaTypeMap
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
name|corba
operator|.
name|types
operator|.
name|CorbaHandlerUtils
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
name|corba
operator|.
name|types
operator|.
name|CorbaObjectHandler
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
name|corba
operator|.
name|types
operator|.
name|CorbaStructListener
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
name|corba
operator|.
name|types
operator|.
name|CorbaTypeListener
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
name|corba
operator|.
name|wsdl
operator|.
name|ArgType
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
name|service
operator|.
name|model
operator|.
name|ServiceInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|omg
operator|.
name|CORBA
operator|.
name|ORB
import|;
end_import

begin_class
specifier|public
class|class
name|CorbaStreamWriter
implements|implements
name|XMLStreamWriter
block|{
specifier|protected
name|String
name|defaultNS
init|=
literal|""
decl_stmt|;
specifier|protected
name|CorbaTypeListener
index|[]
name|listeners
decl_stmt|;
specifier|protected
name|ServiceInfo
name|serviceInfo
decl_stmt|;
specifier|protected
name|CorbaTypeMap
name|typeMap
decl_stmt|;
specifier|protected
name|ORB
name|orb
decl_stmt|;
specifier|protected
name|CorbaTypeListener
name|currentTypeListener
decl_stmt|;
specifier|protected
name|CorbaNamespaceContext
name|ctx
init|=
operator|new
name|CorbaNamespaceContext
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ArgType
argument_list|>
name|params
decl_stmt|;
specifier|private
name|int
name|paramCounter
decl_stmt|;
specifier|private
specifier|final
name|Deque
argument_list|<
name|QName
argument_list|>
name|elements
init|=
operator|new
name|ArrayDeque
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|QName
name|paramElement
decl_stmt|;
specifier|private
name|QName
name|wrapElementName
decl_stmt|;
specifier|private
name|boolean
name|skipWrap
decl_stmt|;
specifier|public
name|CorbaStreamWriter
parameter_list|(
name|ORB
name|orbRef
parameter_list|,
name|CorbaTypeMap
name|map
parameter_list|,
name|ServiceInfo
name|sinfo
parameter_list|)
block|{
name|orb
operator|=
name|orbRef
expr_stmt|;
name|typeMap
operator|=
name|map
expr_stmt|;
name|serviceInfo
operator|=
name|sinfo
expr_stmt|;
block|}
specifier|public
name|CorbaStreamWriter
parameter_list|(
name|ORB
name|orbRef
parameter_list|,
name|List
argument_list|<
name|ArgType
argument_list|>
name|paramTypes
parameter_list|,
name|CorbaTypeMap
name|map
parameter_list|,
name|ServiceInfo
name|sinfo
parameter_list|,
name|boolean
name|wrap
parameter_list|)
block|{
name|this
argument_list|(
name|orbRef
argument_list|,
name|map
argument_list|,
name|sinfo
argument_list|)
expr_stmt|;
name|params
operator|=
name|paramTypes
expr_stmt|;
name|skipWrap
operator|=
name|wrap
expr_stmt|;
name|listeners
operator|=
operator|new
name|CorbaTypeListener
index|[
name|paramTypes
operator|.
name|size
argument_list|()
index|]
expr_stmt|;
block|}
specifier|public
name|CorbaObjectHandler
index|[]
name|getCorbaObjects
parameter_list|()
block|{
comment|//REVISIT, we can make the CorbaTypeListener& CorbaObjectHandler the same object.
name|CorbaObjectHandler
index|[]
name|handlers
init|=
operator|new
name|CorbaObjectHandler
index|[
name|listeners
operator|.
name|length
index|]
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
name|listeners
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
if|if
condition|(
name|listeners
index|[
name|i
index|]
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|CorbaBindingException
argument_list|(
literal|"Uninitalized object for parameter "
operator|+
name|params
operator|.
name|get
argument_list|(
name|i
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|handlers
index|[
name|i
index|]
operator|=
name|listeners
index|[
name|i
index|]
operator|.
name|getCorbaObject
argument_list|()
expr_stmt|;
block|}
return|return
name|handlers
return|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|namespaceURI
parameter_list|,
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
literal|""
argument_list|,
name|localName
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeEndElement
parameter_list|()
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|elements
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|currentTypeListener
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|QName
name|name
init|=
name|elements
operator|.
name|pop
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|name
operator|.
name|equals
argument_list|(
name|paramElement
argument_list|)
operator|)
operator|||
operator|(
name|name
operator|.
name|equals
argument_list|(
name|wrapElementName
argument_list|)
operator|)
condition|)
block|{
comment|// let the struct check if all members wer processed
comment|// it is necessary for empty sequences with minOccurs=0 as last elements of struct
if|if
condition|(
name|currentTypeListener
operator|instanceof
name|CorbaStructListener
condition|)
block|{
name|currentTypeListener
operator|.
name|processEndElement
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
name|currentTypeListener
operator|=
literal|null
expr_stmt|;
block|}
else|else
block|{
name|currentTypeListener
operator|.
name|processEndElement
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
name|ctx
operator|.
name|pop
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
literal|""
argument_list|,
name|localName
argument_list|,
name|defaultNS
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|ctx
operator|.
name|push
argument_list|()
expr_stmt|;
if|if
condition|(
name|prefix
operator|!=
literal|null
condition|)
block|{
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
name|QName
name|name
init|=
operator|new
name|QName
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|prefix
argument_list|)
decl_stmt|;
name|elements
operator|.
name|push
argument_list|(
name|name
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|skipWrap
condition|)
block|{
if|if
condition|(
name|currentTypeListener
operator|==
literal|null
condition|)
block|{
name|paramElement
operator|=
name|name
expr_stmt|;
name|setCurrentTypeListener
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|currentTypeListener
operator|.
name|processStartElement
argument_list|(
name|name
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|wrapElementName
operator|=
name|name
expr_stmt|;
name|skipWrap
operator|=
literal|false
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|setCurrentTypeListener
parameter_list|(
name|QName
name|name
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|ArgType
name|param
init|=
name|params
operator|.
name|get
argument_list|(
name|paramCounter
argument_list|)
decl_stmt|;
name|QName
name|idlType
init|=
name|param
operator|.
name|getIdltype
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|skipWrap
operator|||
operator|(
name|name
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|param
operator|.
name|getName
argument_list|()
argument_list|)
operator|)
condition|)
block|{
name|currentTypeListener
operator|=
name|CorbaHandlerUtils
operator|.
name|getTypeListener
argument_list|(
name|name
argument_list|,
name|idlType
argument_list|,
name|typeMap
argument_list|,
name|orb
argument_list|,
name|serviceInfo
argument_list|)
expr_stmt|;
name|currentTypeListener
operator|.
name|setNamespaceContext
argument_list|(
name|ctx
argument_list|)
expr_stmt|;
name|listeners
index|[
name|paramCounter
index|]
operator|=
name|currentTypeListener
expr_stmt|;
name|paramCounter
operator|++
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"Expected element not found: "
operator|+
name|param
operator|.
name|getName
argument_list|()
operator|+
literal|" (Found "
operator|+
name|name
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|)
expr_stmt|;
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeEmptyElement
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeStartElement
argument_list|(
name|localName
argument_list|)
expr_stmt|;
name|writeEndElement
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|writeEndDocument
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|flush
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeAttribute
argument_list|(
literal|""
argument_list|,
name|defaultNS
argument_list|,
name|localName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|currentTypeListener
operator|.
name|processWriteAttribute
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeAttribute
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|localName
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|value
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|writeAttribute
argument_list|(
literal|""
argument_list|,
name|namespaceURI
argument_list|,
name|localName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeNamespace
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|currentTypeListener
operator|!=
literal|null
condition|)
block|{
name|currentTypeListener
operator|.
name|processWriteNamespace
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|namespaceURI
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeDefaultNamespace
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|namespaceURI
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|defaultNS
operator|=
name|namespaceURI
expr_stmt|;
block|}
specifier|public
name|void
name|writeComment
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|target
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeProcessingInstruction
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|target
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeCData
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|data
parameter_list|)
throws|throws
name|XMLStreamException
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|writeDTD
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|dtd
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeEntityRef
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|name
parameter_list|)
throws|throws
name|XMLStreamException
block|{
throw|throw
operator|new
name|XMLStreamException
argument_list|(
literal|"Not yet implemented"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|writeStartDocument
parameter_list|()
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|encoding
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|version
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeStartDocument
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|version
parameter_list|)
throws|throws
name|XMLStreamException
block|{     }
specifier|public
name|void
name|writeCharacters
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|text
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|currentTypeListener
operator|.
name|processCharacters
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeCharacters
parameter_list|(
name|char
index|[]
name|text
parameter_list|,
name|int
name|start
parameter_list|,
name|int
name|len
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|currentTypeListener
operator|.
name|processCharacters
argument_list|(
operator|new
name|String
argument_list|(
name|text
argument_list|,
name|start
argument_list|,
name|len
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|java
operator|.
name|lang
operator|.
name|String
name|getPrefix
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
return|return
name|ctx
operator|.
name|getPrefix
argument_list|(
name|uri
argument_list|)
return|;
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|prefix
parameter_list|,
name|java
operator|.
name|lang
operator|.
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|ctx
operator|.
name|setPrefix
argument_list|(
name|prefix
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDefaultNamespace
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
name|defaultNS
operator|=
name|uri
expr_stmt|;
block|}
specifier|public
name|void
name|setNamespaceContext
parameter_list|(
name|NamespaceContext
name|context
parameter_list|)
throws|throws
name|XMLStreamException
block|{
comment|//ignore
block|}
specifier|public
name|NamespaceContext
name|getNamespaceContext
parameter_list|()
block|{
return|return
name|ctx
return|;
block|}
specifier|public
name|java
operator|.
name|lang
operator|.
name|Object
name|getProperty
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|String
name|name
parameter_list|)
throws|throws
name|java
operator|.
name|lang
operator|.
name|IllegalArgumentException
block|{
return|return
literal|null
return|;
block|}
specifier|public
class|class
name|CorbaNamespaceContext
implements|implements
name|NamespaceContext
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
decl_stmt|;
specifier|private
name|CorbaNamespaceContext
name|parent
decl_stmt|;
specifier|public
name|CorbaNamespaceContext
parameter_list|()
block|{
name|this
operator|.
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|private
name|CorbaNamespaceContext
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|,
name|CorbaNamespaceContext
name|p
parameter_list|)
block|{
name|this
operator|.
name|map
operator|=
name|map
expr_stmt|;
name|this
operator|.
name|parent
operator|=
name|p
expr_stmt|;
block|}
specifier|public
name|void
name|push
parameter_list|()
block|{
name|parent
operator|=
operator|new
name|CorbaNamespaceContext
argument_list|(
name|map
argument_list|,
name|parent
argument_list|)
expr_stmt|;
name|map
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|pop
parameter_list|()
block|{
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|map
operator|=
name|parent
operator|.
name|map
expr_stmt|;
name|parent
operator|=
name|parent
operator|.
name|parent
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setPrefix
parameter_list|(
name|String
name|pfx
parameter_list|,
name|String
name|ns
parameter_list|)
block|{
name|map
operator|.
name|put
argument_list|(
name|pfx
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getNamespaceURI
parameter_list|(
name|String
name|prefix
parameter_list|)
block|{
name|String
name|answer
init|=
name|map
operator|.
name|get
argument_list|(
name|prefix
argument_list|)
decl_stmt|;
if|if
condition|(
name|answer
operator|==
literal|null
operator|&&
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getNamespaceURI
argument_list|(
name|prefix
argument_list|)
return|;
block|}
return|return
name|answer
return|;
block|}
specifier|public
name|String
name|getPrefix
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|entry
operator|.
name|getKey
argument_list|()
return|;
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
return|return
name|parent
operator|.
name|getPrefix
argument_list|(
name|namespaceURI
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Iterator
argument_list|<
name|String
argument_list|>
name|getPrefixes
parameter_list|(
name|String
name|namespaceURI
parameter_list|)
block|{
name|Set
argument_list|<
name|String
argument_list|>
name|set
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|namespaceURI
operator|.
name|equals
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|parent
operator|!=
literal|null
condition|)
block|{
name|Iterator
argument_list|<
name|?
argument_list|>
name|iter
init|=
name|parent
operator|.
name|getPrefixes
argument_list|(
name|namespaceURI
argument_list|)
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|set
operator|.
name|add
argument_list|(
operator|(
name|String
operator|)
name|iter
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|set
operator|.
name|iterator
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

