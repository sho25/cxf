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
name|systest
operator|.
name|jaxrs
package|;
end_package

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
name|Locale
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
name|WeakHashMap
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
name|HttpHeaders
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
name|MessageBodyReader
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
name|MessageBodyWriter
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
name|JAXBContext
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
name|XMLOutputFactory
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
name|codehaus
operator|.
name|jettison
operator|.
name|badgerfish
operator|.
name|BadgerFishXMLInputFactory
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
name|badgerfish
operator|.
name|BadgerFishXMLOutputFactory
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
specifier|final
class|class
name|BadgerFishProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
block|{
specifier|private
specifier|static
name|Map
argument_list|<
name|Class
argument_list|,
name|JAXBContext
argument_list|>
name|jaxbContexts
init|=
operator|new
name|WeakHashMap
argument_list|<
name|Class
argument_list|,
name|JAXBContext
argument_list|>
argument_list|()
decl_stmt|;
annotation|@
name|Context
specifier|private
name|HttpHeaders
name|requestHeaders
decl_stmt|;
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
name|annotations
parameter_list|,
name|MediaType
name|m
parameter_list|)
block|{
return|return
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|boolean
name|isWriteable
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
name|annotations
parameter_list|,
name|MediaType
name|m
parameter_list|)
block|{
return|return
name|type
operator|.
name|getAnnotation
argument_list|(
name|XmlRootElement
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|Object
name|o
parameter_list|,
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
name|annotations
parameter_list|,
name|MediaType
name|m
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
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
block|{
try|try
block|{
name|JAXBContext
name|context
init|=
name|getJAXBContext
argument_list|(
name|type
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|context
operator|.
name|createUnmarshaller
argument_list|()
decl_stmt|;
name|BadgerFishXMLInputFactory
name|factory
init|=
operator|new
name|BadgerFishXMLInputFactory
argument_list|()
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
name|obj
init|=
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|xsw
argument_list|)
decl_stmt|;
name|xsw
operator|.
name|close
argument_list|()
expr_stmt|;
return|return
name|obj
return|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
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
name|clazz
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
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
try|try
block|{
if|if
condition|(
operator|!
operator|new
name|Locale
argument_list|(
literal|"badger-fish-language"
argument_list|)
operator|.
name|equals
argument_list|(
name|requestHeaders
operator|.
name|getLanguage
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
name|JAXBContext
name|context
init|=
name|getJAXBContext
argument_list|(
name|obj
operator|.
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|Marshaller
name|marshaller
init|=
name|context
operator|.
name|createMarshaller
argument_list|()
decl_stmt|;
name|XMLOutputFactory
name|factory
init|=
operator|new
name|BadgerFishXMLOutputFactory
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|xsw
init|=
name|factory
operator|.
name|createXMLStreamWriter
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|marshaller
operator|.
name|marshal
argument_list|(
name|obj
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
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|XMLStreamException
name|e
parameter_list|)
block|{
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|private
name|JAXBContext
name|getJAXBContext
parameter_list|(
name|Class
name|type
parameter_list|)
throws|throws
name|JAXBException
block|{
synchronized|synchronized
init|(
name|jaxbContexts
init|)
block|{
name|JAXBContext
name|context
init|=
name|jaxbContexts
operator|.
name|get
argument_list|(
name|type
argument_list|)
decl_stmt|;
if|if
condition|(
name|context
operator|==
literal|null
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|jaxbContexts
operator|.
name|put
argument_list|(
name|type
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
block|}
block|}
end_class

end_unit

