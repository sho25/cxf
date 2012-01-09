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
name|client
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
name|core
operator|.
name|Response
operator|.
name|ResponseBuilder
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
name|message
operator|.
name|Message
import|;
end_import

begin_class
specifier|public
class|class
name|ResponseReader
implements|implements
name|MessageBodyReader
argument_list|<
name|Response
argument_list|>
block|{
annotation|@
name|Context
specifier|private
name|MessageContext
name|context
decl_stmt|;
specifier|private
name|Class
argument_list|<
name|?
argument_list|>
name|entityCls
decl_stmt|;
specifier|private
name|Type
name|entityGenericType
decl_stmt|;
specifier|public
name|ResponseReader
parameter_list|()
block|{              }
specifier|public
name|ResponseReader
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|entityCls
parameter_list|)
block|{
name|this
operator|.
name|entityCls
operator|=
name|entityCls
expr_stmt|;
block|}
specifier|public
name|boolean
name|isReadable
parameter_list|(
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
name|mt
parameter_list|)
block|{
return|return
name|cls
operator|.
name|isAssignableFrom
argument_list|(
name|Response
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|Response
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|Response
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
throws|,
name|WebApplicationException
block|{
name|int
name|status
init|=
name|Integer
operator|.
name|valueOf
argument_list|(
name|getContext
argument_list|()
operator|.
name|get
argument_list|(
name|Message
operator|.
name|RESPONSE_CODE
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
name|ResponseBuilder
name|rb
init|=
name|Response
operator|.
name|status
argument_list|(
name|status
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|header
range|:
name|headers
operator|.
name|keySet
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|values
init|=
name|headers
operator|.
name|get
argument_list|(
name|header
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|value
range|:
name|values
control|)
block|{
name|rb
operator|.
name|header
argument_list|(
name|header
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|entityCls
operator|!=
literal|null
condition|)
block|{
name|Object
name|entity
init|=
name|readFrom
argument_list|(
name|entityCls
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|,
name|headers
argument_list|,
name|is
argument_list|)
decl_stmt|;
name|rb
operator|.
name|entity
argument_list|(
name|entity
argument_list|)
expr_stmt|;
block|}
return|return
name|rb
operator|.
name|build
argument_list|()
return|;
block|}
specifier|private
parameter_list|<
name|T
parameter_list|>
name|T
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|Annotation
name|anns
index|[]
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
throws|,
name|WebApplicationException
block|{
name|Providers
name|providers
init|=
name|getContext
argument_list|()
operator|.
name|getProviders
argument_list|()
decl_stmt|;
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
name|reader
init|=
name|providers
operator|.
name|getMessageBodyReader
argument_list|(
name|type
argument_list|,
name|getEntityGenericType
argument_list|()
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|)
decl_stmt|;
if|if
condition|(
name|reader
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|ClientWebApplicationException
argument_list|(
literal|"No reader for Response entity "
operator|+
name|entityCls
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|reader
operator|.
name|readFrom
argument_list|(
name|type
argument_list|,
name|getEntityGenericType
argument_list|()
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|,
name|headers
argument_list|,
name|is
argument_list|)
return|;
block|}
specifier|public
name|void
name|setEntityClass
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|cls
parameter_list|)
block|{
name|entityCls
operator|=
name|cls
expr_stmt|;
block|}
specifier|private
name|Type
name|getEntityGenericType
parameter_list|()
block|{
return|return
name|entityGenericType
operator|==
literal|null
condition|?
name|entityCls
else|:
name|entityGenericType
return|;
block|}
specifier|protected
name|MessageContext
name|getContext
parameter_list|()
block|{
return|return
name|context
return|;
block|}
block|}
end_class

end_unit

