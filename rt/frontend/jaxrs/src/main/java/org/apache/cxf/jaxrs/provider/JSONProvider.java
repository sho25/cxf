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
name|Arrays
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
name|concurrent
operator|.
name|ConcurrentHashMap
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
name|bind
operator|.
name|annotation
operator|.
name|adapters
operator|.
name|XmlJavaTypeAdapter
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
name|provider
operator|.
name|AbstractJAXBProvider
operator|.
name|CollectionWrapper
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
name|HttpUtils
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
name|JAXBUtils
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
name|SimpleConverter
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
name|TypeConverter
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
specifier|static
specifier|final
name|String
name|MAPPED_CONVENTION
init|=
literal|"mapped"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BADGER_FISH_CONVENTION
init|=
literal|"badgerfish"
decl_stmt|;
specifier|private
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|namespaceMap
init|=
operator|new
name|ConcurrentHashMap
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
specifier|private
name|boolean
name|writeXsiType
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|readXsiType
init|=
literal|true
decl_stmt|;
specifier|private
name|boolean
name|ignoreNamespaces
decl_stmt|;
specifier|private
name|String
name|convention
init|=
name|MAPPED_CONVENTION
decl_stmt|;
specifier|private
name|TypeConverter
name|typeConverter
decl_stmt|;
specifier|private
name|boolean
name|attributesToElements
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|setAttributesToElements
parameter_list|(
name|boolean
name|value
parameter_list|)
block|{
name|this
operator|.
name|attributesToElements
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|setConvention
parameter_list|(
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
operator|!
name|MAPPED_CONVENTION
operator|.
name|equals
argument_list|(
name|value
argument_list|)
operator|&&
operator|!
name|BADGER_FISH_CONVENTION
operator|.
name|equals
argument_list|(
name|value
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Unsupported convention \""
operator|+
name|value
argument_list|)
throw|;
block|}
name|convention
operator|=
name|value
expr_stmt|;
block|}
specifier|public
name|void
name|setConvertTypesToStrings
parameter_list|(
name|boolean
name|convert
parameter_list|)
block|{
if|if
condition|(
name|convert
condition|)
block|{
name|this
operator|.
name|setTypeConverter
argument_list|(
operator|new
name|SimpleConverter
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setTypeConverter
parameter_list|(
name|TypeConverter
name|converter
parameter_list|)
block|{
name|this
operator|.
name|typeConverter
operator|=
name|converter
expr_stmt|;
block|}
specifier|public
name|void
name|setIgnoreNamespaces
parameter_list|(
name|boolean
name|ignoreNamespaces
parameter_list|)
block|{
name|this
operator|.
name|ignoreNamespaces
operator|=
name|ignoreNamespaces
expr_stmt|;
block|}
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
operator|.
name|putAll
argument_list|(
name|namespaceMap
argument_list|)
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
name|boolean
name|isCollection
init|=
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|theType
init|=
name|isCollection
condition|?
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|genericType
argument_list|)
else|:
name|type
decl_stmt|;
name|theType
operator|=
name|getActualType
argument_list|(
name|theType
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|)
expr_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|createUnmarshaller
argument_list|(
name|theType
argument_list|,
name|genericType
argument_list|,
name|isCollection
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
name|xsr
init|=
name|createReader
argument_list|(
name|type
argument_list|,
name|realStream
argument_list|,
name|isCollection
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
operator|||
name|unmarshalAsJaxbElement
operator|||
name|jaxbElementClassMap
operator|!=
literal|null
operator|&&
name|jaxbElementClassMap
operator|.
name|containsKey
argument_list|(
name|theType
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|response
operator|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|xsr
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
name|xsr
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|response
operator|instanceof
name|JAXBElement
operator|&&
operator|!
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
operator|(
operator|(
name|JAXBElement
operator|)
name|response
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|isCollection
condition|)
block|{
name|response
operator|=
operator|(
operator|(
name|CollectionWrapper
operator|)
name|response
operator|)
operator|.
name|getCollectionOrArray
argument_list|(
name|theType
argument_list|,
name|type
argument_list|,
name|getAdapter
argument_list|(
name|theType
argument_list|,
name|anns
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|checkAdapter
argument_list|(
name|response
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
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
parameter_list|,
name|boolean
name|isCollection
parameter_list|)
throws|throws
name|Exception
block|{
name|XMLStreamReader
name|reader
init|=
name|createReader
argument_list|(
name|type
argument_list|,
name|is
argument_list|)
decl_stmt|;
return|return
name|isCollection
condition|?
operator|new
name|JAXBCollectionWrapperReader
argument_list|(
name|reader
argument_list|)
else|:
name|reader
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
if|if
condition|(
name|BADGER_FISH_CONVENTION
operator|.
name|equals
argument_list|(
name|convention
argument_list|)
condition|)
block|{
return|return
name|JSONUtils
operator|.
name|createBadgerFishReader
argument_list|(
name|is
argument_list|)
return|;
block|}
else|else
block|{
name|XMLStreamReader
name|reader
init|=
name|JSONUtils
operator|.
name|createStreamReader
argument_list|(
name|is
argument_list|,
name|readXsiType
argument_list|,
name|namespaceMap
argument_list|)
decl_stmt|;
return|return
name|createTransformReaderIfNeeded
argument_list|(
name|reader
argument_list|,
name|is
argument_list|)
return|;
block|}
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
name|String
name|encoding
init|=
name|HttpUtils
operator|.
name|getSetEncoding
argument_list|(
name|m
argument_list|,
name|headers
argument_list|,
literal|"UTF-8"
argument_list|)
decl_stmt|;
if|if
condition|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|cls
argument_list|)
condition|)
block|{
name|marshalCollection
argument_list|(
name|cls
argument_list|,
name|obj
argument_list|,
name|genericType
argument_list|,
name|encoding
argument_list|,
name|os
argument_list|,
name|m
argument_list|,
name|anns
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|Object
name|actualObject
init|=
name|checkAdapter
argument_list|(
name|obj
argument_list|,
name|cls
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
name|collection
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
parameter_list|,
name|Annotation
index|[]
name|anns
parameter_list|)
throws|throws
name|Exception
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|actualClass
init|=
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|genericType
argument_list|)
decl_stmt|;
name|actualClass
operator|=
name|getActualType
argument_list|(
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|)
expr_stmt|;
name|Collection
name|c
init|=
name|originalCls
operator|.
name|isArray
argument_list|()
condition|?
name|Arrays
operator|.
name|asList
argument_list|(
operator|(
name|Object
index|[]
operator|)
name|collection
argument_list|)
else|:
operator|(
name|Collection
operator|)
name|collection
decl_stmt|;
name|Iterator
name|it
init|=
name|c
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|Object
name|firstObj
init|=
name|it
operator|.
name|hasNext
argument_list|()
condition|?
name|it
operator|.
name|next
argument_list|()
else|:
literal|null
decl_stmt|;
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
literal|null
decl_stmt|;
if|if
condition|(
name|firstObj
operator|instanceof
name|JAXBElement
condition|)
block|{
name|JAXBElement
name|el
init|=
operator|(
name|JAXBElement
operator|)
name|firstObj
decl_stmt|;
name|qname
operator|=
name|el
operator|.
name|getName
argument_list|()
expr_stmt|;
name|actualClass
operator|=
name|el
operator|.
name|getDeclaredType
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|qname
operator|=
name|getCollectionWrapperQName
argument_list|(
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|firstObj
argument_list|,
literal|false
argument_list|)
expr_stmt|;
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
elseif|else
if|if
condition|(
name|serializeAsArray
condition|)
block|{
name|startTag
operator|=
literal|"["
expr_stmt|;
name|endTag
operator|=
literal|"]"
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
if|if
condition|(
name|firstObj
operator|!=
literal|null
condition|)
block|{
name|XmlJavaTypeAdapter
name|adapter
init|=
name|getAdapter
argument_list|(
name|firstObj
operator|.
name|getClass
argument_list|()
argument_list|,
name|anns
argument_list|)
decl_stmt|;
name|marshalCollectionMember
argument_list|(
name|JAXBUtils
operator|.
name|useAdapter
argument_list|(
name|firstObj
argument_list|,
name|adapter
argument_list|,
literal|true
argument_list|)
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
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
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
name|marshalCollectionMember
argument_list|(
name|JAXBUtils
operator|.
name|useAdapter
argument_list|(
name|it
operator|.
name|next
argument_list|()
argument_list|,
name|adapter
argument_list|,
literal|true
argument_list|)
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
name|marshalCollectionMember
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
name|String
name|enc
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|obj
operator|instanceof
name|JAXBElement
condition|)
block|{
name|obj
operator|=
operator|(
operator|(
name|JAXBElement
operator|)
name|obj
operator|)
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|obj
operator|=
name|convertToJaxbElementIfNeeded
argument_list|(
name|obj
argument_list|,
name|cls
argument_list|,
name|genericType
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|obj
operator|instanceof
name|JAXBElement
operator|&&
name|cls
operator|!=
name|JAXBElement
operator|.
name|class
condition|)
block|{
name|cls
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
name|obj
argument_list|,
name|cls
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
name|obj
argument_list|,
name|cls
argument_list|,
name|genericType
argument_list|,
name|enc
argument_list|,
name|os
argument_list|,
literal|true
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
if|if
condition|(
name|BADGER_FISH_CONVENTION
operator|.
name|equals
argument_list|(
name|convention
argument_list|)
condition|)
block|{
return|return
name|JSONUtils
operator|.
name|createBadgerFishWriter
argument_list|(
name|os
argument_list|)
return|;
block|}
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
name|config
init|=
name|JSONUtils
operator|.
name|createConfiguration
argument_list|(
name|namespaceMap
argument_list|,
name|writeXsiType
operator|&&
operator|!
name|ignoreNamespaces
argument_list|,
name|attributesToElements
argument_list|,
name|typeConverter
argument_list|)
decl_stmt|;
name|XMLStreamWriter
name|writer
init|=
name|JSONUtils
operator|.
name|createStreamWriter
argument_list|(
name|os
argument_list|,
name|qname
argument_list|,
name|writeXsiType
operator|&&
operator|!
name|ignoreNamespaces
argument_list|,
name|config
argument_list|,
name|serializeAsArray
argument_list|,
name|arrayKeys
argument_list|,
name|isCollection
operator|||
name|dropRootElement
argument_list|)
decl_stmt|;
name|writer
operator|=
name|JSONUtils
operator|.
name|createIgnoreMixedContentWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|ignoreMixedContent
argument_list|)
expr_stmt|;
name|writer
operator|=
name|JSONUtils
operator|.
name|createIgnoreNsWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|ignoreNamespaces
argument_list|)
expr_stmt|;
return|return
name|createTransformWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|os
argument_list|)
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
specifier|public
name|void
name|setWriteXsiType
parameter_list|(
name|boolean
name|writeXsiType
parameter_list|)
block|{
name|this
operator|.
name|writeXsiType
operator|=
name|writeXsiType
expr_stmt|;
block|}
specifier|public
name|void
name|setReadXsiType
parameter_list|(
name|boolean
name|readXsiType
parameter_list|)
block|{
name|this
operator|.
name|readXsiType
operator|=
name|readXsiType
expr_stmt|;
block|}
block|}
end_class

end_unit

