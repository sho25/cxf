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
name|InternalServerErrorException
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
name|Providers
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
name|StreamingResponse
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

begin_class
specifier|public
class|class
name|StreamingResponseProvider
parameter_list|<
name|T
parameter_list|>
extends|extends
name|AbstractConfigurableProvider
implements|implements
name|MessageBodyWriter
argument_list|<
name|StreamingResponse
argument_list|<
name|T
argument_list|>
argument_list|>
block|{
annotation|@
name|Context
specifier|private
name|Providers
name|providers
decl_stmt|;
annotation|@
name|Override
specifier|public
name|boolean
name|isWriteable
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
name|MediaType
name|mt
parameter_list|)
block|{
return|return
name|StreamingResponse
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|StreamingResponse
argument_list|<
name|T
argument_list|>
name|p
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|,
name|Type
name|t
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
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
throws|throws
name|IOException
throws|,
name|WebApplicationException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|actualCls
init|=
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|t
argument_list|)
decl_stmt|;
if|if
condition|(
name|cls
operator|==
name|actualCls
condition|)
block|{
name|actualCls
operator|=
name|Object
operator|.
name|class
expr_stmt|;
block|}
comment|//TODO: review the possibility of caching the providers
name|StreamingResponseWriter
name|thewriter
init|=
operator|new
name|StreamingResponseWriter
argument_list|(
name|actualCls
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
decl_stmt|;
name|p
operator|.
name|writeTo
argument_list|(
name|thewriter
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|long
name|getSize
parameter_list|(
name|StreamingResponse
argument_list|<
name|T
argument_list|>
name|arg0
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|arg1
parameter_list|,
name|Type
name|arg2
parameter_list|,
name|Annotation
index|[]
name|arg3
parameter_list|,
name|MediaType
name|arg4
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
specifier|private
class|class
name|StreamingResponseWriter
implements|implements
name|StreamingResponse
operator|.
name|Writer
argument_list|<
name|T
argument_list|>
block|{
specifier|private
specifier|volatile
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
name|writer
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|entityCls
decl_stmt|;
specifier|private
name|MediaType
name|mt
decl_stmt|;
specifier|private
name|Annotation
index|[]
name|anns
decl_stmt|;
specifier|private
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
decl_stmt|;
specifier|private
name|OutputStream
name|os
decl_stmt|;
name|StreamingResponseWriter
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityCls
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
name|Object
argument_list|>
name|headers
parameter_list|,
name|OutputStream
name|os
parameter_list|)
block|{
name|this
operator|.
name|entityCls
operator|=
name|entityCls
expr_stmt|;
name|this
operator|.
name|anns
operator|=
name|anns
expr_stmt|;
name|this
operator|.
name|mt
operator|=
name|mt
expr_stmt|;
name|this
operator|.
name|headers
operator|=
name|headers
expr_stmt|;
name|this
operator|.
name|os
operator|=
name|os
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
name|void
name|write
parameter_list|(
name|T
name|data
parameter_list|)
throws|throws
name|IOException
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|actualCls
init|=
name|entityCls
operator|!=
name|Object
operator|.
name|class
condition|?
name|entityCls
else|:
name|data
operator|.
name|getClass
argument_list|()
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
operator|(
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
operator|)
name|providers
operator|.
name|getMessageBodyWriter
argument_list|(
name|actualCls
argument_list|,
name|actualCls
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|)
expr_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InternalServerErrorException
argument_list|()
throw|;
block|}
block|}
name|writer
operator|.
name|writeTo
argument_list|(
name|data
argument_list|,
name|actualCls
argument_list|,
name|actualCls
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|OutputStream
name|getEntityStream
parameter_list|()
block|{
return|return
name|os
return|;
block|}
block|}
block|}
end_class

end_unit

