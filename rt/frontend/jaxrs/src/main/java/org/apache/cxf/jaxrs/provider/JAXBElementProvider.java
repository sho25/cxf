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
name|Collections
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
name|PropertyException
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
name|ValidationEventHandler
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
name|jaxb
operator|.
name|NamespaceMapper
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
name|DepthXMLStreamReader
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

begin_class
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
block|{
literal|"application/xml"
block|,
literal|"application/*+xml"
block|,
literal|"text/xml"
block|}
argument_list|)
annotation|@
name|Provider
specifier|public
class|class
name|JAXBElementProvider
extends|extends
name|AbstractJAXBProvider
block|{
specifier|private
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|MARSHALLER_PROPERTIES
init|=
name|Arrays
operator|.
name|asList
argument_list|(
operator|new
name|String
index|[]
block|{
name|Marshaller
operator|.
name|JAXB_ENCODING
block|,
name|Marshaller
operator|.
name|JAXB_FORMATTED_OUTPUT
block|,
name|Marshaller
operator|.
name|JAXB_FRAGMENT
block|,
name|Marshaller
operator|.
name|JAXB_NO_NAMESPACE_SCHEMA_LOCATION
block|,
name|Marshaller
operator|.
name|JAXB_SCHEMA_LOCATION
block|}
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mProperties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|enableStreaming
decl_stmt|;
specifier|private
name|ValidationEventHandler
name|eventHandler
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|jaxbElementClassNames
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|isReadable
parameter_list|(
name|Class
argument_list|<
name|?
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
parameter_list|)
block|{
if|if
condition|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|type
operator|=
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|genericType
argument_list|)
expr_stmt|;
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
name|isSupported
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|)
return|;
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
name|setJaxbElementClassNames
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|names
parameter_list|)
block|{
name|jaxbElementClassNames
operator|=
name|names
expr_stmt|;
block|}
specifier|public
name|void
name|setValidationHandler
parameter_list|(
name|ValidationEventHandler
name|handler
parameter_list|)
block|{
name|eventHandler
operator|=
name|handler
expr_stmt|;
block|}
specifier|public
name|void
name|setEnableStreaming
parameter_list|(
name|boolean
name|enableStream
parameter_list|)
block|{
name|enableStreaming
operator|=
name|enableStream
expr_stmt|;
block|}
specifier|public
name|boolean
name|getEnableStreaming
parameter_list|()
block|{
return|return
name|enableStreaming
return|;
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
name|setMarshallerProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|marshallProperties
parameter_list|)
block|{
name|mProperties
operator|=
name|marshallProperties
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemaLocation
parameter_list|(
name|String
name|schemaLocation
parameter_list|)
block|{
name|mProperties
operator|.
name|put
argument_list|(
name|Marshaller
operator|.
name|JAXB_SCHEMA_LOCATION
argument_list|,
name|schemaLocation
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
if|if
condition|(
name|eventHandler
operator|!=
literal|null
condition|)
block|{
name|unmarshaller
operator|.
name|setEventHandler
argument_list|(
name|eventHandler
argument_list|)
expr_stmt|;
block|}
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
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|,
name|theType
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|response
operator|=
name|doUnmarshal
argument_list|(
name|unmarshaller
argument_list|,
name|type
argument_list|,
name|is
argument_list|,
name|mt
argument_list|)
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
name|Object
name|doUnmarshal
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|JAXBException
block|{
name|XMLStreamReader
name|reader
init|=
name|getStreamReader
argument_list|(
name|is
argument_list|,
name|type
argument_list|,
name|mt
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
name|unmarshalFromReader
argument_list|(
name|unmarshaller
argument_list|,
name|reader
argument_list|,
name|mt
argument_list|)
return|;
block|}
return|return
name|unmarshalFromInputStream
argument_list|(
name|unmarshaller
argument_list|,
name|is
argument_list|,
name|mt
argument_list|)
return|;
block|}
specifier|protected
name|XMLStreamReader
name|getStreamReader
parameter_list|(
name|InputStream
name|is
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
name|MessageContext
name|mc
init|=
name|getContext
argument_list|()
decl_stmt|;
name|XMLStreamReader
name|reader
init|=
name|mc
operator|!=
literal|null
condition|?
name|mc
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
else|:
literal|null
decl_stmt|;
if|if
condition|(
name|InjectionUtils
operator|.
name|isSupportedCollectionOrArray
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|reader
operator|=
name|reader
operator|==
literal|null
condition|?
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
else|:
name|reader
expr_stmt|;
return|return
operator|new
name|JAXBCollectionWrapperReader
argument_list|(
name|reader
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|reader
return|;
block|}
block|}
specifier|protected
name|Object
name|unmarshalFromInputStream
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|JAXBException
block|{
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
return|;
block|}
specifier|protected
name|Object
name|unmarshalFromReader
parameter_list|(
name|Unmarshaller
name|unmarshaller
parameter_list|,
name|XMLStreamReader
name|reader
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|JAXBException
block|{
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|reader
argument_list|)
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
argument_list|,
name|m
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
name|QName
name|qname
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|arr
operator|.
name|length
operator|>
literal|0
operator|&&
name|arr
index|[
literal|0
index|]
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
name|arr
index|[
literal|0
index|]
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
name|actualObject
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
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
literal|"<ns1:"
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|" xmlns:ns1=\""
operator|+
name|qname
operator|.
name|getNamespaceURI
argument_list|()
operator|+
literal|"\">"
expr_stmt|;
name|endTag
operator|=
literal|"</ns1:"
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|">"
expr_stmt|;
block|}
else|else
block|{
name|startTag
operator|=
literal|"<"
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|">"
expr_stmt|;
name|endTag
operator|=
literal|"</"
operator|+
name|qname
operator|.
name|getLocalPart
argument_list|()
operator|+
literal|">"
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
for|for
control|(
name|Object
name|o
range|:
name|arr
control|)
block|{
name|marshalCollectionMember
argument_list|(
name|o
operator|instanceof
name|JAXBElement
condition|?
operator|(
operator|(
name|JAXBElement
operator|)
name|o
operator|)
operator|.
name|getValue
argument_list|()
else|:
name|o
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
argument_list|,
name|qname
operator|.
name|getNamespaceURI
argument_list|()
argument_list|)
expr_stmt|;
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
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|Exception
block|{
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
name|ms
operator|.
name|setProperty
argument_list|(
name|Marshaller
operator|.
name|JAXB_FRAGMENT
argument_list|,
name|Boolean
operator|.
name|TRUE
argument_list|)
expr_stmt|;
if|if
condition|(
name|ns
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|map
init|=
name|Collections
operator|.
name|singletonMap
argument_list|(
name|ns
argument_list|,
literal|"ns1"
argument_list|)
decl_stmt|;
name|NamespaceMapper
name|nsMapper
init|=
operator|new
name|NamespaceMapper
argument_list|(
name|map
argument_list|)
decl_stmt|;
try|try
block|{
name|ms
operator|.
name|setProperty
argument_list|(
literal|"com.sun.xml.bind.namespacePrefixMapper"
argument_list|,
name|nsMapper
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PropertyException
name|ex
parameter_list|)
block|{
name|ms
operator|.
name|setProperty
argument_list|(
literal|"com.sun.xml.internal.bind.namespacePrefixMapper"
argument_list|,
name|nsMapper
argument_list|)
expr_stmt|;
block|}
block|}
name|marshal
argument_list|(
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
name|mt
argument_list|,
name|ms
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|marshal
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
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|Exception
block|{
if|if
condition|(
name|jaxbElementClassNames
operator|!=
literal|null
operator|&&
name|jaxbElementClassNames
operator|.
name|contains
argument_list|(
name|cls
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|QName
name|name
init|=
name|getJaxbQName
argument_list|(
name|cls
argument_list|,
name|genericType
argument_list|,
name|obj
argument_list|,
literal|false
argument_list|)
decl_stmt|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|JAXBElement
name|el
init|=
operator|new
name|JAXBElement
argument_list|(
name|name
argument_list|,
name|cls
argument_list|,
literal|null
argument_list|,
name|obj
argument_list|)
decl_stmt|;
name|obj
operator|=
name|el
expr_stmt|;
name|cls
operator|=
name|JAXBElement
operator|.
name|class
expr_stmt|;
block|}
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
name|mt
argument_list|,
name|ms
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|marshal
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
parameter_list|,
name|MediaType
name|mt
parameter_list|,
name|Marshaller
name|ms
parameter_list|)
throws|throws
name|Exception
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|mProperties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ms
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|MessageContext
name|mc
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
comment|// check Marshaller properties which might've been set earlier on,
comment|// they'll overwrite statically configured ones
for|for
control|(
name|String
name|key
range|:
name|MARSHALLER_PROPERTIES
control|)
block|{
name|Object
name|value
init|=
name|mc
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|ms
operator|.
name|setProperty
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|XMLStreamWriter
name|writer
init|=
name|getStreamWriter
argument_list|(
name|obj
argument_list|,
name|os
argument_list|,
name|mt
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
name|mc
operator|.
name|put
argument_list|(
name|XMLStreamWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
name|marshalToWriter
argument_list|(
name|ms
argument_list|,
name|obj
argument_list|,
name|writer
argument_list|,
name|mt
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|marshalToOutputStream
argument_list|(
name|ms
argument_list|,
name|obj
argument_list|,
name|os
argument_list|,
name|mt
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|XMLStreamWriter
name|getStreamWriter
parameter_list|(
name|Object
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
name|XMLStreamWriter
name|writer
init|=
literal|null
decl_stmt|;
name|MessageContext
name|mc
init|=
name|getContext
argument_list|()
decl_stmt|;
if|if
condition|(
name|mc
operator|!=
literal|null
condition|)
block|{
name|writer
operator|=
name|mc
operator|.
name|getContent
argument_list|(
name|XMLStreamWriter
operator|.
name|class
argument_list|)
expr_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
operator|&&
name|enableStreaming
condition|)
block|{
name|writer
operator|=
name|StaxUtils
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|writer
return|;
block|}
specifier|protected
name|void
name|marshalToOutputStream
parameter_list|(
name|Marshaller
name|ms
parameter_list|,
name|Object
name|obj
parameter_list|,
name|OutputStream
name|os
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|Exception
block|{
name|ms
operator|.
name|marshal
argument_list|(
name|obj
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|marshalToWriter
parameter_list|(
name|Marshaller
name|ms
parameter_list|,
name|Object
name|obj
parameter_list|,
name|XMLStreamWriter
name|writer
parameter_list|,
name|MediaType
name|mt
parameter_list|)
throws|throws
name|Exception
block|{
name|ms
operator|.
name|marshal
argument_list|(
name|obj
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
specifier|protected
specifier|static
class|class
name|JAXBCollectionWrapperReader
extends|extends
name|DepthXMLStreamReader
block|{
specifier|private
name|boolean
name|firstName
decl_stmt|;
specifier|private
name|boolean
name|firstNs
decl_stmt|;
specifier|public
name|JAXBCollectionWrapperReader
parameter_list|(
name|XMLStreamReader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getNamespaceURI
parameter_list|()
block|{
if|if
condition|(
operator|!
name|firstNs
condition|)
block|{
name|firstNs
operator|=
literal|true
expr_stmt|;
return|return
literal|""
return|;
block|}
return|return
name|super
operator|.
name|getNamespaceURI
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getLocalName
parameter_list|()
block|{
if|if
condition|(
operator|!
name|firstName
condition|)
block|{
name|firstName
operator|=
literal|true
expr_stmt|;
return|return
literal|"collectionWrapper"
return|;
block|}
return|return
name|super
operator|.
name|getLocalName
argument_list|()
return|;
block|}
block|}
block|}
end_class

end_unit

