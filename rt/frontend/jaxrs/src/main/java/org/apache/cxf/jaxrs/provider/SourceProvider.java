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
name|parsers
operator|.
name|DocumentBuilder
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|parsers
operator|.
name|DocumentBuilderFactory
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
name|Transformer
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
name|TransformerException
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
name|TransformerFactory
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
name|javax
operator|.
name|xml
operator|.
name|transform
operator|.
name|stream
operator|.
name|StreamResult
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
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|ext
operator|.
name|xml
operator|.
name|XMLSource
import|;
end_import

begin_class
annotation|@
name|Provider
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
specifier|public
class|class
name|SourceProvider
implements|implements
name|MessageBodyReader
argument_list|<
name|Object
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|Source
argument_list|>
block|{
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
name|mt
parameter_list|)
block|{
return|return
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
return|;
block|}
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
name|mt
parameter_list|)
block|{
return|return
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|XMLSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
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
name|source
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
if|if
condition|(
name|DOMSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|source
argument_list|)
condition|)
block|{
name|Document
name|doc
init|=
literal|null
decl_stmt|;
name|DocumentBuilderFactory
name|factory
init|=
name|DocumentBuilderFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
name|DocumentBuilder
name|builder
decl_stmt|;
try|try
block|{
name|builder
operator|=
name|factory
operator|.
name|newDocumentBuilder
argument_list|()
expr_stmt|;
name|doc
operator|=
name|builder
operator|.
name|parse
argument_list|(
name|is
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|IOException
name|ioex
init|=
operator|new
name|IOException
argument_list|(
literal|"Problem creating a Source object"
argument_list|)
decl_stmt|;
name|ioex
operator|.
name|setStackTrace
argument_list|(
name|e
operator|.
name|getStackTrace
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
name|ioex
throw|;
block|}
return|return
operator|new
name|DOMSource
argument_list|(
name|doc
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|StreamSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|source
argument_list|)
operator|||
name|Source
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|source
argument_list|)
condition|)
block|{
return|return
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
return|;
block|}
elseif|else
if|if
condition|(
name|XMLSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|source
argument_list|)
condition|)
block|{
return|return
operator|new
name|XMLSource
argument_list|(
name|is
argument_list|)
return|;
block|}
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unrecognized source"
argument_list|)
throw|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Source
name|source
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
throws|throws
name|IOException
block|{
name|StreamResult
name|result
init|=
operator|new
name|StreamResult
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|TransformerFactory
name|tf
init|=
name|TransformerFactory
operator|.
name|newInstance
argument_list|()
decl_stmt|;
try|try
block|{
name|Transformer
name|t
init|=
name|tf
operator|.
name|newTransformer
argument_list|()
decl_stmt|;
name|t
operator|.
name|transform
argument_list|(
name|source
argument_list|,
name|result
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TransformerException
name|te
parameter_list|)
block|{
name|te
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|te
argument_list|)
throw|;
block|}
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|Source
name|source
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
name|mt
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
end_class

end_unit

