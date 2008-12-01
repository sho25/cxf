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
name|io
operator|.
name|OutputStreamWriter
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
name|XmlRootElement
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
specifier|static
specifier|final
name|String
name|JAXB_DEFAULT_NAMESPACE
init|=
literal|"##default"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|JAXB_DEFAULT_NAME
init|=
literal|"##default"
decl_stmt|;
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
name|annotations
parameter_list|,
name|MediaType
name|m
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
name|MappedXMLInputFactory
name|factory
init|=
operator|new
name|MappedXMLInputFactory
argument_list|(
name|namespaceMap
argument_list|)
decl_stmt|;
name|XMLStreamReader
name|xsw
init|=
name|factory
operator|.
name|createXMLStreamReader
argument_list|(
name|is
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
literal|"UTF-8"
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
name|cls
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
name|ms
operator|.
name|marshal
argument_list|(
name|actualObject
argument_list|,
name|xsw
argument_list|)
expr_stmt|;
name|xsw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
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
block|}
specifier|private
name|String
name|getKey
parameter_list|(
name|MappedNamespaceConvention
name|convention
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|String
name|key
init|=
literal|null
decl_stmt|;
name|XmlRootElement
name|root
init|=
name|cls
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
decl_stmt|;
if|if
condition|(
name|root
operator|!=
literal|null
condition|)
block|{
name|String
name|namespace
init|=
name|root
operator|.
name|namespace
argument_list|()
decl_stmt|;
if|if
condition|(
name|JAXB_DEFAULT_NAMESPACE
operator|.
name|equals
argument_list|(
name|namespace
argument_list|)
condition|)
block|{
name|namespace
operator|=
literal|""
expr_stmt|;
block|}
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
name|prefix
operator|=
literal|""
expr_stmt|;
block|}
name|String
name|name
init|=
name|root
operator|.
name|name
argument_list|()
decl_stmt|;
if|if
condition|(
name|JAXB_DEFAULT_NAME
operator|.
name|equals
argument_list|(
name|name
argument_list|)
condition|)
block|{
name|name
operator|=
name|cls
operator|.
name|getSimpleName
argument_list|()
expr_stmt|;
block|}
name|key
operator|=
name|convention
operator|.
name|createKey
argument_list|(
name|prefix
argument_list|,
name|namespace
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|key
operator|=
name|convention
operator|.
name|createKey
argument_list|(
literal|""
argument_list|,
literal|""
argument_list|,
name|cls
operator|.
name|getSimpleName
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|key
return|;
block|}
block|}
end_class

end_unit

