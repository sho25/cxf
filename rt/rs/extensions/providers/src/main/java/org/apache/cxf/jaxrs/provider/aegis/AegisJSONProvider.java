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
operator|.
name|aegis
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
name|aegis
operator|.
name|AegisContext
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
name|aegis
operator|.
name|AegisWriter
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
name|aegis
operator|.
name|type
operator|.
name|AegisType
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
name|json
operator|.
name|utils
operator|.
name|JSONUtils
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
name|json
operator|.
name|utils
operator|.
name|PrefixCollectingXMLStreamWriter
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
name|ExceptionUtils
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
name|cxf
operator|.
name|staxutils
operator|.
name|W3CDOMStreamWriter
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

begin_class
annotation|@
name|Provider
annotation|@
name|Produces
argument_list|(
block|{
literal|"application/json"
block|}
argument_list|)
annotation|@
name|Consumes
argument_list|(
block|{
literal|"application/json"
block|}
argument_list|)
specifier|public
specifier|final
class|class
name|AegisJSONProvider
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AegisElementProvider
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|arrayKeys
decl_stmt|;
specifier|private
name|boolean
name|serializeAsArray
decl_stmt|;
specifier|private
name|boolean
name|dropRootElement
decl_stmt|;
specifier|private
name|boolean
name|ignoreNamespaces
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
specifier|public
name|AegisJSONProvider
parameter_list|()
block|{     }
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
specifier|public
name|void
name|setDropRootElement
parameter_list|(
name|boolean
name|dropRootElement
parameter_list|)
block|{
name|this
operator|.
name|dropRootElement
operator|=
name|dropRootElement
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
name|annotations
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
literal|true
return|;
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
name|nsMap
parameter_list|)
block|{
name|this
operator|.
name|namespaceMap
operator|=
operator|new
name|ConcurrentHashMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
name|nsMap
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|T
name|obj
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
if|if
condition|(
name|type
operator|==
literal|null
condition|)
block|{
name|type
operator|=
name|obj
operator|.
name|getClass
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|genericType
operator|==
literal|null
condition|)
block|{
name|genericType
operator|=
name|type
expr_stmt|;
block|}
name|AegisContext
name|context
init|=
name|getAegisContext
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|)
decl_stmt|;
name|AegisType
name|aegisType
init|=
name|context
operator|.
name|getTypeMapping
argument_list|()
operator|.
name|getType
argument_list|(
name|genericType
argument_list|)
decl_stmt|;
name|AegisWriter
argument_list|<
name|XMLStreamWriter
argument_list|>
name|aegisWriter
init|=
name|context
operator|.
name|createXMLStreamWriter
argument_list|()
decl_stmt|;
try|try
block|{
name|W3CDOMStreamWriter
name|w3cStreamWriter
init|=
operator|new
name|W3CDOMStreamWriter
argument_list|()
decl_stmt|;
name|XMLStreamWriter
name|spyingWriter
init|=
operator|new
name|PrefixCollectingXMLStreamWriter
argument_list|(
name|w3cStreamWriter
argument_list|,
name|namespaceMap
argument_list|)
decl_stmt|;
name|spyingWriter
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
comment|// use type qname as element qname?
name|aegisWriter
operator|.
name|write
argument_list|(
name|obj
argument_list|,
name|aegisType
operator|.
name|getSchemaType
argument_list|()
argument_list|,
literal|false
argument_list|,
name|spyingWriter
argument_list|,
name|aegisType
argument_list|)
expr_stmt|;
name|spyingWriter
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|spyingWriter
operator|.
name|close
argument_list|()
expr_stmt|;
name|Document
name|dom
init|=
name|w3cStreamWriter
operator|.
name|getDocument
argument_list|()
decl_stmt|;
comment|// ok, now the namespace map has all the prefixes.
name|XMLStreamWriter
name|xmlStreamWriter
init|=
name|createStreamWriter
argument_list|(
name|aegisType
operator|.
name|getSchemaType
argument_list|()
argument_list|,
name|os
argument_list|)
decl_stmt|;
name|xmlStreamWriter
operator|.
name|writeStartDocument
argument_list|()
expr_stmt|;
name|StaxUtils
operator|.
name|copy
argument_list|(
name|dom
argument_list|,
name|xmlStreamWriter
argument_list|)
expr_stmt|;
comment|// Jettison needs, and StaxUtils.copy doesn't do it.
name|xmlStreamWriter
operator|.
name|writeEndDocument
argument_list|()
expr_stmt|;
name|xmlStreamWriter
operator|.
name|flush
argument_list|()
expr_stmt|;
name|xmlStreamWriter
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|e
argument_list|,
literal|null
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|XMLStreamWriter
name|createStreamWriter
parameter_list|(
name|QName
name|typeQName
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|Exception
block|{
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
literal|false
argument_list|,
literal|null
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
name|typeQName
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
name|dropRootElement
argument_list|)
decl_stmt|;
return|return
name|JSONUtils
operator|.
name|createIgnoreNsWriterIfNeeded
argument_list|(
name|writer
argument_list|,
name|ignoreNamespaces
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|XMLStreamReader
name|createStreamReader
parameter_list|(
name|AegisType
name|typeToRead
parameter_list|,
name|InputStream
name|is
parameter_list|)
throws|throws
name|Exception
block|{
comment|// the namespace map. Oh, the namespace map.
comment|// This is wrong, but might make unit tests pass until we redesign.
if|if
condition|(
name|typeToRead
operator|!=
literal|null
condition|)
block|{
name|namespaceMap
operator|.
name|putIfAbsent
argument_list|(
name|typeToRead
operator|.
name|getSchemaType
argument_list|()
operator|.
name|getNamespaceURI
argument_list|()
argument_list|,
literal|"ns1"
argument_list|)
expr_stmt|;
block|}
return|return
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
return|;
block|}
block|}
end_class

end_unit

