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
name|jaxrs
operator|.
name|provider
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
name|lang
operator|.
name|annotation
operator|.
name|Annotation
import|;
end_import

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Type
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
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
name|LinkedList
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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Consumes
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|WebApplicationException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MultivaluedMap
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|ext
operator|.
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|jaxrs
operator|.
name|ext
operator|.
name|MessageContext
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
name|jaxrs
operator|.
name|utils
operator|.
name|InjectionUtils
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
name|jaxrs
operator|.
name|utils
operator|.
name|schemas
operator|.
name|SchemaHandler
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
name|DelegatingXMLStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|AbstractXMLStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|Configuration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedNamespaceConvention
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLInputFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|jettison
operator|.
name|mapped
operator|.
name|MappedXMLStreamWriter
import|;
end_import

begin_class
annotation|@
name|Produces
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Consumes
argument_list|(
literal|"application/json"
argument_list|)
annotation|@
name|Provider
specifier|public
class|class
name|JSONProvider
extends|extends
name|AbstractJAXBProvider
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
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
specifier|private
name|boolean
name|serializeAsArray
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|arrayKeys
decl_stmt|;
specifier|private
name|boolean
name|unwrapped
decl_stmt|;
specifier|private
name|String
name|wrapperName
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|wrapperMap
decl_stmt|;
specifier|private
name|boolean
name|dropRootElement
decl_stmt|;
specifier|private
name|boolean
name|dropCollectionWrapperElement
decl_stmt|;
specifier|private
name|boolean
name|ignoreMixedContent
decl_stmt|;
annotation|@
name|Context
specifier|public
name|void
name|setMessageContext
parameter_list|(
name|MessageContext
name|mc
parameter_list|)
block|{
name|super
operator|.
name|setContext
argument_list|(
name|mc
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDropRootElement
parameter_list|(
name|boolean
name|drop
parameter_list|)
block|{
name|this
operator|.
name|dropRootElement
operator|=
name|drop
expr_stmt|;
block|}
specifier|public
name|void
name|setDropCollectionWrapperElement
parameter_list|(
name|boolean
name|drop
parameter_list|)
block|{
name|this
operator|.
name|dropCollectionWrapperElement
operator|=
name|drop
expr_stmt|;
block|}
specifier|public
name|void
name|setIgnoreMixedContent
parameter_list|(
name|boolean
name|ignore
parameter_list|)
block|{
name|this
operator|.
name|ignoreMixedContent
operator|=
name|ignore
expr_stmt|;
block|}
specifier|public
name|void
name|setSupportUnwrapped
parameter_list|(
name|boolean
name|unwrap
parameter_list|)
block|{
name|this
operator|.
name|unwrapped
operator|=
name|unwrap
expr_stmt|;
block|}
specifier|public
name|void
name|setWrapperName
parameter_list|(
name|String
name|wName
parameter_list|)
block|{
name|wrapperName
operator|=
name|wName
expr_stmt|;
block|}
specifier|public
name|void
name|setWrapperMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
name|wrapperMap
operator|=
name|map
expr_stmt|;
block|}
specifier|public
name|void
name|setEnableBuffering
parameter_list|(
name|boolean
name|enableBuf
parameter_list|)
block|{
name|super
operator|.
name|setEnableBuffering
argument_list|(
name|enableBuf
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setConsumeMediaTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|super
operator|.
name|setConsumeMediaTypes
argument_list|(
name|types
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setProduceMediaTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|super
operator|.
name|setProduceMediaTypes
argument_list|(
name|types
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemas
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|)
block|{
name|super
operator|.
name|setSchemaLocations
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemaHandler
parameter_list|(
name|SchemaHandler
name|handler
parameter_list|)
block|{
name|super
operator|.
name|setSchema
argument_list|(
name|handler
operator|.
name|getSchema
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setSerializeAsArray
parameter_list|(
name|boolean
name|asArray
parameter_list|)
block|{
name|this
operator|.
name|serializeAsArray
operator|=
name|asArray
expr_stmt|;
block|}
specifier|public
name|void
name|setArrayKeys
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|keys
parameter_list|)
block|{
name|this
operator|.
name|arrayKeys
operator|=
name|keys
expr_stmt|;
block|}
specifier|public
name|void
name|setNamespaceMap
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
parameter_list|)
block|{
name|this
operator|.
name|namespaceMap
operator|=
name|namespaceMap
expr_stmt|;
block|}
specifier|public
name|Object
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|type
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|theType
init|=
name|getActualType
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|createUnmarshaller
argument_list|(
name|theType
argument_list|,
name|genericType
argument_list|)
decl_stmt|;
name|InputStream
name|realStream
init|=
name|getInputStream
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xsw
init|=
name|createReader
argument_list|(
name|type
argument_list|,
name|realStream
argument_list|)
decl_stmt|;
name|Object
name|response
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|JAXBElement
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|response
operator|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|xsw
argument_list|,
name|theType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|xsw
argument_list|)
expr_stmt|;
block|}
name|response
operator|=
name|checkAdapter
argument_list|(
name|response
argument_list|,
name|anns
argument_list|,
literal|false
argument_list|)
expr_stmt|;
return|return
name|response
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|handleJAXBException
argument_list|(
name|e
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
name|WebApplicationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|WebApplicationException
name|e
parameter_list|)
block|{
throw|throw
name|e
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
comment|// unreachable
return|return
literal|null
return|;
block|}
specifier|protected
name|XMLStreamReader
name|createReader
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
name|MappedXMLInputFactory
name|factory
init|=
operator|new
name|MappedXMLInputFactory
argument_list|(
name|namespaceMap
argument_list|)
decl_stmt|;
return|return
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|protected
name|InputStream
name|getInputStream
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|unwrapped
condition|)
block|{
name|String
name|rootName
init|=
name|getRootName
argument_list|(
name|cls
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|InputStream
name|isBefore
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|rootName
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|after
init|=
literal|"}"
decl_stmt|;
name|InputStream
name|isAfter
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|after
operator|.
name|getBytes
argument_list|()
argument_list|)
decl_stmt|;
specifier|final
name|InputStream
index|[]
name|streams
init|=
operator|new
name|InputStream
index|[]
block|{
name|isBefore
block|,
name|is
block|,
name|isAfter
block|}
decl_stmt|;
name|Enumeration
argument_list|<
name|InputStream
argument_list|>
name|list
init|=
operator|new
name|Enumeration
argument_list|<
name|InputStream
argument_list|>
argument_list|()
block|{
specifier|private
name|int
name|index
decl_stmt|;
specifier|public
name|boolean
name|hasMoreElements
parameter_list|()
block|{
return|return
name|index
operator|<
name|streams
operator|.
name|length
return|;
block|}
specifier|public
name|InputStream
name|nextElement
parameter_list|()
block|{
return|return
name|streams
index|[
name|index
operator|++
index|]
return|;
block|}
block|}
decl_stmt|;
return|return
operator|new
name|SequenceInputStream
argument_list|(
name|list
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|is
return|;
block|}
block|}
specifier|protected
name|String
name|getRootName
parameter_list|(
name|Class
argument_list|<
name|Object
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|name
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|wrapperName
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|wrapperName
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|wrapperMap
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|wrapperMap
operator|.
name|get
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
name|QName
name|qname
init|=
name|getQName
argument_list|(
name|cls
argument_list|,
name|type
argument_list|,
literal|null
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|qname
operator|!=
literal|null
condition|)
block|{
name|name
operator|=
name|qname
operator|.
name|getLocalPart
argument_list|()
expr_stmt|;
name|String
name|prefix
init|=
name|qname
operator|.
name|getPrefix
argument_list|()
decl_stmt|;
if|if
condition|(
name|prefix
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|name
operator|=
name|prefix
operator|+
literal|"."
operator|+
name|name
expr_stmt|;
block|}
block|}
block|}
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
literal|500
argument_list|)
throw|;
block|}
return|return
literal|"{\""
operator|+
name|name
operator|+
literal|"\":"
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Object
name|obj
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|,
name|MediaType
name|m
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|Object
name|actualObject
init|=
name|checkAdapter
argument_list|(
name|obj
argument_list|,
name|anns
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|actualClass
init|=
name|obj
operator|!=
name|actualObject
condition|?
name|actualObject
operator|.
name|getClass
argument_list|()
else|:
name|cls
decl_stmt|;
if|if
condition|(
name|cls
operator|==
name|genericType
condition|)
block|{
name|genericType
operator|=
name|actualClass
expr_stmt|;
block|}
name|String
name|encoding
init|=
name|getEncoding
argument_list|(
name|m
argument_list|,
name|headers
argument_list|)
decl_stmt|;
if|if
condition|(
name|encoding
operator|==
literal|null
condition|)
block|{
name|encoding
operator|=
literal|"UTF-8"
expr_stmt|;
block|}
if|if
condition|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|actualClass
argument_list|)
condition|)
block|{
name|actualClass
operator|=
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
name|marshalCollection
argument_list|(
name|cls
argument_list|,
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|encoding
argument_list|,
name|os
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|marshal
argument_list|(
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|encoding
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|handleJAXBException
argument_list|(
name|e
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
name|WebApplicationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|marshalCollection
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|originalCls
parameter_list|,
name|Object
name|actualObject
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|actualClass
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|String
name|encoding
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|MediaType
name|m
parameter_list|)
throws|throws
name|Exception
block|{
name|String
name|startTag
init|=
literal|null
decl_stmt|;
name|String
name|endTag
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|dropCollectionWrapperElement
condition|)
block|{
name|QName
name|qname
init|=
name|getCollectionWrapperQName
argument_list|(
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|actualObject
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|qname
operator|==
literal|null
condition|)
block|{
name|String
name|message
init|=
operator|new
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|i18n
operator|.
name|Message
argument_list|(
literal|"NO_COLLECTION_ROOT"
argument_list|,
name|BUNDLE
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|Response
operator|.
name|serverError
argument_list|()
operator|.
name|entity
argument_list|(
name|message
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
throw|;
block|}
if|if
condition|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|startTag
operator|=
literal|"{\"ns1."
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\":["
expr_stmt|;
block|}
else|else
block|{
name|startTag
operator|=
literal|"{\""
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|"\":["
expr_stmt|;
block|}
name|endTag
operator|=
literal|"]}"
expr_stmt|;
block|}
else|else
block|{
name|startTag
operator|=
literal|"{"
expr_stmt|;
name|endTag
operator|=
literal|"}"
expr_stmt|;
block|}
name|os
operator|.
name|write
argument_list|(
name|startTag
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
name|Object
index|[]
name|arr
init|=
name|originalCls
operator|.
name|isArray
argument_list|()
condition|?
operator|(
name|Object
index|[]
operator|)
name|actualObject
else|:
operator|(
operator|(
name|Collection
operator|)
name|actualObject
operator|)
operator|.
name|toArray
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
name|arr
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|Marshaller
name|ms
init|=
name|createMarshaller
argument_list|(
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
name|marshal
argument_list|(
name|ms
argument_list|,
name|arr
index|[
name|i
index|]
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|encoding
argument_list|,
name|os
argument_list|,
literal|true
argument_list|)
expr_stmt|;
if|if
condition|(
name|i
operator|+
literal|1
operator|<
name|arr
operator|.
name|length
condition|)
block|{
name|os
operator|.
name|write
argument_list|(
literal|","
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|os
operator|.
name|write
argument_list|(
name|endTag
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|marshal
parameter_list|(
name|Marshaller
name|ms
parameter_list|,
name|Object
name|actualObject
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|actualClass
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|String
name|enc
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|boolean
name|isCollection
parameter_list|)
throws|throws
name|Exception
block|{
name|XMLStreamWriter
name|writer
init|=
name|createWriter
argument_list|(
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|enc
argument_list|,
name|os
argument_list|,
name|isCollection
argument_list|)
decl_stmt|;
if|if
condition|(
name|ignoreMixedContent
condition|)
block|{
name|writer
operator|=
operator|new
name|IgnoreMixedContentWriter
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
name|ms
operator|.
name|marshal
argument_list|(
name|actualObject
argument_list|,
name|writer
argument_list|)
expr_stmt|;
name|writer
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|XMLStreamWriter
name|createWriter
parameter_list|(
name|Object
name|actualObject
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|actualClass
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|String
name|enc
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|boolean
name|isCollection
parameter_list|)
throws|throws
name|Exception
block|{
name|QName
name|qname
init|=
name|getQName
argument_list|(
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|actualObject
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|Configuration
name|c
init|=
operator|new
name|Configuration
argument_list|(
name|namespaceMap
argument_list|)
decl_stmt|;
name|MappedNamespaceConvention
name|convention
init|=
operator|new
name|MappedNamespaceConvention
argument_list|(
name|c
argument_list|)
decl_stmt|;
name|AbstractXMLStreamWriter
name|xsw
init|=
operator|new
name|MappedXMLStreamWriter
argument_list|(
name|convention
argument_list|,
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|,
name|enc
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|serializeAsArray
condition|)
block|{
if|if
condition|(
name|arrayKeys
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|key
range|:
name|arrayKeys
control|)
block|{
name|xsw
operator|.
name|seriliazeAsArray
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|String
name|key
init|=
name|getKey
argument_list|(
name|convention
argument_list|,
name|qname
argument_list|)
decl_stmt|;
name|xsw
operator|.
name|seriliazeAsArray
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|isCollection
operator|||
name|dropRootElement
condition|?
operator|new
name|JSONCollectionWriter
argument_list|(
name|xsw
argument_list|,
name|qname
argument_list|)
else|:
name|xsw
return|;
block|}
specifier|protected
name|void
name|marshal
parameter_list|(
name|Object
name|actualObject
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|actualClass
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|String
name|enc
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
name|actualObject
operator|=
name|convertToJaxbElementIfNeeded
argument_list|(
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|)
expr_stmt|;
if|if
condition|(
name|actualObject
operator|instanceof
name|JAXBElement
operator|&&
name|actualClass
operator|!=
name|JAXBElement
operator|.
name|class
condition|)
block|{
name|actualClass
operator|=
name|JAXBElement
operator|.
name|class
expr_stmt|;
block|}
name|Marshaller
name|ms
init|=
name|createMarshaller
argument_list|(
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|enc
argument_list|)
decl_stmt|;
name|marshal
argument_list|(
name|ms
argument_list|,
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|enc
argument_list|,
name|os
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|getKey
parameter_list|(
name|MappedNamespaceConvention
name|convention
parameter_list|,
name|QName
name|qname
parameter_list|)
throws|throws
name|Exception
block|{
return|return
name|convention
operator|.
name|createKey
argument_list|(
name|qname
operator|.
name|getPrefix
argument_list|()
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|QName
name|getQName
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|type
parameter_list|,
name|Object
name|object
parameter_list|,
name|boolean
name|allocatePrefix
parameter_list|)
throws|throws
name|Exception
block|{
name|QName
name|qname
init|=
name|getJaxbQName
argument_list|(
name|cls
argument_list|,
name|type
argument_list|,
name|object
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|qname
operator|!=
literal|null
condition|)
block|{
name|String
name|prefix
init|=
name|getPrefix
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|allocatePrefix
argument_list|)
decl_stmt|;
return|return
operator|new
name|QName
argument_list|(
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
name|qname
operator|.
name|getLocalPart
argument_list|()
argument_list|,
name|prefix
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|String
name|getPrefix
parameter_list|(
name|String
name|namespace
parameter_list|,
name|boolean
name|allocatePrefix
parameter_list|)
block|{
name|String
name|prefix
init|=
name|namespaceMap
operator|.
name|get
argument_list|(
name|namespace
argument_list|)
decl_stmt|;
if|if
condition|(
name|prefix
operator|==
literal|null
condition|)
block|{
if|if
condition|(
name|allocatePrefix
operator|&&
name|namespace
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|prefix
operator|=
literal|"ns"
operator|+
operator|(
name|namespaceMap
operator|.
name|size
argument_list|()
operator|+
literal|1
operator|)
expr_stmt|;
name|namespaceMap
operator|.
name|put
argument_list|(
name|namespace
argument_list|,
name|prefix
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
block|}
return|return
name|prefix
return|;
block|}
specifier|protected
specifier|static
class|class
name|JSONCollectionWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
specifier|private
name|QName
name|ignoredQName
decl_stmt|;
specifier|public
name|JSONCollectionWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|,
name|QName
name|qname
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
name|ignoredQName
operator|=
name|qname
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|local
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|ignoredQName
operator|.
name|getLocalPart
argument_list|()
operator|.
name|equals
argument_list|(
name|local
argument_list|)
operator|&&
name|ignoredQName
operator|.
name|getNamespaceURI
argument_list|()
operator|.
name|equals
argument_list|(
name|uri
argument_list|)
condition|)
block|{
return|return;
block|}
name|super
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|local
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
specifier|static
class|class
name|IgnoreMixedContentWriter
extends|extends
name|DelegatingXMLStreamWriter
block|{
name|String
name|lastText
decl_stmt|;
name|boolean
name|isMixed
decl_stmt|;
name|List
argument_list|<
name|Boolean
argument_list|>
name|mixed
init|=
operator|new
name|LinkedList
argument_list|<
name|Boolean
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|IgnoreMixedContentWriter
parameter_list|(
name|XMLStreamWriter
name|writer
parameter_list|)
block|{
name|super
argument_list|(
name|writer
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeCharacters
parameter_list|(
name|String
name|text
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|text
operator|.
name|trim
argument_list|()
argument_list|)
condition|)
block|{
name|lastText
operator|=
name|text
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|lastText
operator|+=
name|text
expr_stmt|;
block|}
elseif|else
if|if
condition|(
operator|!
name|isMixed
condition|)
block|{
name|super
operator|.
name|writeCharacters
argument_list|(
name|text
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|local
parameter_list|,
name|String
name|uri
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|isMixed
operator|=
literal|true
expr_stmt|;
block|}
name|mixed
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|isMixed
argument_list|)
expr_stmt|;
name|lastText
operator|=
literal|null
expr_stmt|;
name|isMixed
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|writeStartElement
argument_list|(
name|prefix
argument_list|,
name|local
argument_list|,
name|uri
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|uri
parameter_list|,
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|isMixed
operator|=
literal|true
expr_stmt|;
block|}
name|mixed
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|isMixed
argument_list|)
expr_stmt|;
name|lastText
operator|=
literal|null
expr_stmt|;
name|isMixed
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|writeStartElement
argument_list|(
name|uri
argument_list|,
name|local
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|writeStartElement
parameter_list|(
name|String
name|local
parameter_list|)
throws|throws
name|XMLStreamException
block|{
if|if
condition|(
name|lastText
operator|!=
literal|null
condition|)
block|{
name|isMixed
operator|=
literal|true
expr_stmt|;
block|}
name|mixed
operator|.
name|add
argument_list|(
literal|0
argument_list|,
name|isMixed
argument_list|)
expr_stmt|;
name|lastText
operator|=
literal|null
expr_stmt|;
name|isMixed
operator|=
literal|false
expr_stmt|;
name|super
operator|.
name|writeStartElement
argument_list|(
name|local
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
name|lastText
operator|!=
literal|null
operator|&&
operator|!
name|isMixed
condition|)
block|{
name|super
operator|.
name|writeCharacters
argument_list|(
name|lastText
argument_list|)
expr_stmt|;
block|}
name|super
operator|.
name|writeEndElement
argument_list|()
expr_stmt|;
name|isMixed
operator|=
name|mixed
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|mixed
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

