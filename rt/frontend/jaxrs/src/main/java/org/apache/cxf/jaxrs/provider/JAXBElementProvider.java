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
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|is
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
name|actualObject
operator|.
name|getClass
argument_list|()
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
name|m
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|enableStreaming
condition|)
block|{
name|XMLStreamWriter
name|writer
init|=
operator|(
name|XMLStreamWriter
operator|)
name|getContext
argument_list|()
operator|.
name|get
argument_list|(
name|XMLStreamWriter
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
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
name|ms
operator|.
name|marshal
argument_list|(
name|actualObject
argument_list|,
name|writer
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|ms
operator|.
name|marshal
argument_list|(
name|actualObject
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
block|}
end_class

end_unit

