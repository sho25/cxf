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
name|atom
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
name|logging
operator|.
name|Logger
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
name|abdera
operator|.
name|Abdera
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|model
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
name|abdera
operator|.
name|model
operator|.
name|Element
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|parser
operator|.
name|Parser
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|parser
operator|.
name|ParserOptions
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|abdera
operator|.
name|writer
operator|.
name|Writer
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

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractAtomProvider
parameter_list|<
name|T
extends|extends
name|Element
parameter_list|>
implements|implements
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
implements|,
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|AbstractAtomProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Abdera
name|ATOM_ENGINE
init|=
operator|new
name|Abdera
argument_list|()
decl_stmt|;
specifier|private
name|boolean
name|autodetectCharset
decl_stmt|;
specifier|private
name|boolean
name|formattedOutput
decl_stmt|;
specifier|public
name|long
name|getSize
parameter_list|(
name|T
name|element
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
specifier|public
name|void
name|writeTo
parameter_list|(
name|T
name|element
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|clazz
parameter_list|,
name|Type
name|type
parameter_list|,
name|Annotation
index|[]
name|a
parameter_list|,
name|MediaType
name|mt
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
name|MediaType
operator|.
name|APPLICATION_JSON_TYPE
operator|.
name|isCompatible
argument_list|(
name|mt
argument_list|)
condition|)
block|{
name|Writer
name|w
init|=
name|createWriter
argument_list|(
literal|"json"
argument_list|)
decl_stmt|;
if|if
condition|(
name|w
operator|==
literal|null
condition|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toNotSupportedException
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
throw|;
block|}
name|element
operator|.
name|writeTo
argument_list|(
name|w
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
elseif|else
if|if
condition|(
name|formattedOutput
condition|)
block|{
name|Writer
name|w
init|=
name|createWriter
argument_list|(
literal|"prettyxml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|w
operator|!=
literal|null
condition|)
block|{
name|element
operator|.
name|writeTo
argument_list|(
name|w
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|element
operator|.
name|writeTo
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|element
operator|.
name|writeTo
argument_list|(
name|os
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|Writer
name|createWriter
parameter_list|(
name|String
name|writerName
parameter_list|)
block|{
name|Writer
name|w
init|=
name|ATOM_ENGINE
operator|.
name|getWriterFactory
argument_list|()
operator|.
name|getWriter
argument_list|(
name|writerName
argument_list|)
decl_stmt|;
if|if
condition|(
name|w
operator|==
literal|null
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Atom writer \""
operator|+
name|writerName
operator|+
literal|"\" is not available"
argument_list|)
expr_stmt|;
block|}
return|return
name|w
return|;
block|}
specifier|public
name|T
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|Type
name|t
parameter_list|,
name|Annotation
index|[]
name|a
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
name|Parser
name|parser
init|=
name|ATOM_ENGINE
operator|.
name|getParser
argument_list|()
decl_stmt|;
synchronized|synchronized
init|(
name|parser
init|)
block|{
name|ParserOptions
name|options
init|=
name|parser
operator|.
name|getDefaultParserOptions
argument_list|()
decl_stmt|;
if|if
condition|(
name|options
operator|!=
literal|null
condition|)
block|{
name|options
operator|.
name|setAutodetectCharset
argument_list|(
name|autodetectCharset
argument_list|)
expr_stmt|;
block|}
block|}
name|XMLStreamReader
name|reader
init|=
name|StaxUtils
operator|.
name|createXMLStreamReader
argument_list|(
name|is
argument_list|)
decl_stmt|;
name|Document
argument_list|<
name|T
argument_list|>
name|doc
init|=
name|parser
operator|.
name|parse
argument_list|(
name|reader
argument_list|)
decl_stmt|;
return|return
name|doc
operator|.
name|getRoot
argument_list|()
return|;
block|}
specifier|public
name|void
name|setFormattedOutput
parameter_list|(
name|boolean
name|formattedOutput
parameter_list|)
block|{
name|this
operator|.
name|formattedOutput
operator|=
name|formattedOutput
expr_stmt|;
block|}
specifier|public
name|void
name|setAutodetectCharset
parameter_list|(
name|boolean
name|autodetectCharset
parameter_list|)
block|{
name|this
operator|.
name|autodetectCharset
operator|=
name|autodetectCharset
expr_stmt|;
block|}
block|}
end_class

end_unit

