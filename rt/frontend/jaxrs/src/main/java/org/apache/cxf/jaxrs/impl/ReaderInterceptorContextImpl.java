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
name|ReaderInterceptorContextImpl
extends|extends
name|AbstractInterceptorContextImpl
implements|implements
name|ReaderInterceptorContext
block|{
specifier|private
name|List
argument_list|<
name|ReaderInterceptor
argument_list|>
name|readers
decl_stmt|;
specifier|private
name|InputStream
name|is
decl_stmt|;
specifier|public
name|ReaderInterceptorContextImpl
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
name|Annotation
index|[]
name|anns
parameter_list|,
name|InputStream
name|is
parameter_list|,
name|Message
name|message
parameter_list|,
name|List
argument_list|<
name|ReaderInterceptor
argument_list|>
name|readers
parameter_list|)
block|{
name|super
argument_list|(
name|cls
argument_list|,
name|type
argument_list|,
name|anns
argument_list|,
name|message
argument_list|)
expr_stmt|;
name|this
operator|.
name|is
operator|=
name|is
expr_stmt|;
name|this
operator|.
name|readers
operator|=
name|readers
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getHeaders
parameter_list|()
block|{
return|return
operator|new
name|MetadataMap
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|(
operator|(
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|String
argument_list|>
argument_list|>
operator|)
name|m
operator|.
name|get
argument_list|(
name|Message
operator|.
name|PROTOCOL_HEADERS
argument_list|)
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|InputStream
name|getInputStream
parameter_list|()
block|{
return|return
name|is
return|;
block|}
annotation|@
name|Override
specifier|public
name|Object
name|proceed
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|readers
operator|==
literal|null
operator|||
name|readers
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
literal|null
return|;
block|}
name|ReaderInterceptor
name|next
init|=
name|readers
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
return|return
name|next
operator|.
name|aroundReadFrom
argument_list|(
name|this
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setInputStream
parameter_list|(
name|InputStream
name|stream
parameter_list|)
block|{
name|is
operator|=
name|stream
expr_stmt|;
name|m
operator|.
name|setContent
argument_list|(
name|InputStream
operator|.
name|class
argument_list|,
name|stream
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|MediaType
name|getMediaType
parameter_list|()
block|{
return|return
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|getHeaders
argument_list|()
operator|.
name|getFirst
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setMediaType
parameter_list|(
name|MediaType
name|mt
parameter_list|)
block|{
if|if
condition|(
operator|!
name|getMediaType
argument_list|()
operator|.
name|isCompatible
argument_list|(
name|mt
argument_list|)
condition|)
block|{
name|providerSelectionPropertyChanged
argument_list|()
expr_stmt|;
block|}
name|getHeaders
argument_list|()
operator|.
name|putSingle
argument_list|(
name|HttpHeaders
operator|.
name|CONTENT_TYPE
argument_list|,
name|JAXRSUtils
operator|.
name|mediaTypeToString
argument_list|(
name|mt
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

