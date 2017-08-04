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
name|impl
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
name|Reader
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
name|ProcessingException
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
name|ReaderInterceptor
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
name|ReaderInterceptorContext
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
name|provider
operator|.
name|ProviderFactory
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
name|JAXRSUtils
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|ReaderInterceptorMBR
implements|implements
name|ReaderInterceptor
block|{
specifier|private
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|reader
decl_stmt|;
specifier|private
name|Message
name|m
decl_stmt|;
specifier|public
name|ReaderInterceptorMBR
parameter_list|(
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|reader
parameter_list|,
name|Message
name|m
parameter_list|)
block|{
name|this
operator|.
name|reader
operator|=
name|reader
expr_stmt|;
name|this
operator|.
name|m
operator|=
name|m
expr_stmt|;
block|}
specifier|public
name|MessageBodyReader
argument_list|<
name|?
argument_list|>
name|getMBR
parameter_list|()
block|{
return|return
name|reader
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
block|{
literal|"unchecked"
block|,
literal|"rawtypes"
block|}
argument_list|)
annotation|@
name|Override
specifier|public
name|Object
name|aroundReadFrom
parameter_list|(
name|ReaderInterceptorContext
name|c
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|Class
name|entityCls
init|=
name|c
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Type
name|entityType
init|=
name|c
operator|.
name|getGenericType
argument_list|()
decl_stmt|;
name|MediaType
name|entityMt
init|=
name|c
operator|.
name|getMediaType
argument_list|()
decl_stmt|;
name|Annotation
index|[]
name|entityAnns
init|=
name|c
operator|.
name|getAnnotations
argument_list|()
decl_stmt|;
if|if
condition|(
operator|(
name|reader
operator|==
literal|null
operator|||
name|m
operator|.
name|get
argument_list|(
name|ProviderFactory
operator|.
name|PROVIDER_SELECTION_PROPERTY_CHANGED
argument_list|)
operator|==
name|Boolean
operator|.
name|TRUE
operator|&&
operator|!
name|reader
operator|.
name|isReadable
argument_list|(
name|entityCls
argument_list|,
name|entityType
argument_list|,
name|entityAnns
argument_list|,
name|entityMt
argument_list|)
operator|)
operator|&&
name|entityStreamAvailable
argument_list|(
name|c
operator|.
name|getInputStream
argument_list|()
argument_list|)
condition|)
block|{
name|reader
operator|=
name|ProviderFactory
operator|.
name|getInstance
argument_list|(
name|m
argument_list|)
operator|.
name|createMessageBodyReader
argument_list|(
name|entityCls
argument_list|,
name|entityType
argument_list|,
name|entityAnns
argument_list|,
name|entityMt
argument_list|,
name|m
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
name|String
name|errorMessage
init|=
name|JAXRSUtils
operator|.
name|logMessageHandlerProblem
argument_list|(
literal|"NO_MSG_READER"
argument_list|,
name|entityCls
argument_list|,
name|entityMt
argument_list|)
decl_stmt|;
throw|throw
operator|new
name|ProcessingException
argument_list|(
name|errorMessage
argument_list|)
throw|;
block|}
return|return
name|reader
operator|.
name|readFrom
argument_list|(
name|entityCls
argument_list|,
name|entityType
argument_list|,
name|entityAnns
argument_list|,
name|entityMt
argument_list|,
operator|new
name|HttpHeadersImpl
argument_list|(
name|m
argument_list|)
operator|.
name|getRequestHeaders
argument_list|()
argument_list|,
name|c
operator|.
name|getInputStream
argument_list|()
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|entityStreamAvailable
parameter_list|(
name|InputStream
name|entity
parameter_list|)
block|{
return|return
name|entity
operator|!=
literal|null
operator|||
name|m
operator|.
name|getContent
argument_list|(
name|XMLStreamReader
operator|.
name|class
argument_list|)
operator|!=
literal|null
operator|||
name|m
operator|.
name|getContent
argument_list|(
name|Reader
operator|.
name|class
argument_list|)
operator|!=
literal|null
return|;
block|}
block|}
end_class

end_unit

