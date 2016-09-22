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
name|rx
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
name|java
operator|.
name|util
operator|.
name|LinkedList
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
name|jaxrs
operator|.
name|utils
operator|.
name|ParameterizedCollectionType
import|;
end_import

begin_import
import|import
name|rx
operator|.
name|Observable
import|;
end_import

begin_class
annotation|@
name|Provider
specifier|public
class|class
name|ObservableWriter
parameter_list|<
name|T
parameter_list|>
implements|implements
name|MessageBodyWriter
argument_list|<
name|Observable
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
specifier|private
name|boolean
name|writeSingleElementAsList
decl_stmt|;
annotation|@
name|Override
specifier|public
name|long
name|getSize
parameter_list|(
name|Observable
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
comment|// TODO Auto-generated method stub
return|return
operator|-
literal|1
return|;
block|}
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
name|arg0
parameter_list|,
name|Type
name|arg1
parameter_list|,
name|Annotation
index|[]
name|arg2
parameter_list|,
name|MediaType
name|arg3
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|Observable
argument_list|<
name|T
argument_list|>
name|obs
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
name|List
argument_list|<
name|T
argument_list|>
name|entities
init|=
operator|new
name|LinkedList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
name|obs
operator|.
name|subscribe
argument_list|(
name|value
lambda|->
name|entities
operator|.
name|add
argument_list|(
name|value
argument_list|)
argument_list|,
name|throwable
lambda|->
name|throwError
argument_list|(
name|throwable
argument_list|)
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|entities
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
if|if
condition|(
name|entities
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|T
argument_list|>
name|allEntities
init|=
operator|new
name|LinkedList
argument_list|<
name|T
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|T
name|obj
range|:
name|entities
control|)
block|{
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|List
argument_list|<
name|T
argument_list|>
name|listT
init|=
operator|(
name|List
argument_list|<
name|T
argument_list|>
operator|)
name|obj
decl_stmt|;
name|allEntities
operator|.
name|addAll
argument_list|(
name|listT
argument_list|)
expr_stmt|;
block|}
name|writeToOutputStream
argument_list|(
name|allEntities
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
elseif|else
if|if
condition|(
name|entities
operator|.
name|size
argument_list|()
operator|>
literal|1
operator|||
name|writeSingleElementAsList
condition|)
block|{
name|writeToOutputStream
argument_list|(
name|entities
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
else|else
block|{
name|writeToOutputStream
argument_list|(
name|entities
operator|.
name|get
argument_list|(
literal|0
argument_list|)
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
block|}
block|}
specifier|private
name|void
name|writeToOutputStream
parameter_list|(
name|Object
name|value
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
name|Class
argument_list|<
name|?
argument_list|>
name|valueCls
init|=
name|value
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Type
name|valueType
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|value
operator|instanceof
name|List
condition|)
block|{
name|List
argument_list|<
name|?
argument_list|>
name|list
init|=
operator|(
name|List
argument_list|<
name|?
argument_list|>
operator|)
name|value
decl_stmt|;
name|valueType
operator|=
operator|new
name|ParameterizedCollectionType
argument_list|(
name|list
operator|.
name|isEmpty
argument_list|()
condition|?
name|Object
operator|.
name|class
else|:
name|list
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getClass
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|valueType
operator|=
name|valueCls
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
name|writer
init|=
operator|(
name|MessageBodyWriter
argument_list|<
name|Object
argument_list|>
operator|)
name|providers
operator|.
name|getMessageBodyWriter
argument_list|(
name|valueCls
argument_list|,
name|valueType
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|)
decl_stmt|;
if|if
condition|(
name|writer
operator|==
literal|null
condition|)
block|{
name|throwError
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|writer
operator|.
name|writeTo
argument_list|(
name|value
argument_list|,
name|valueCls
argument_list|,
name|valueType
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
catch|catch
parameter_list|(
name|IOException
name|ex
parameter_list|)
block|{
name|throwError
argument_list|(
name|ex
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|throwError
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
throw|throw
name|ExceptionUtils
operator|.
name|toInternalServerErrorException
argument_list|(
name|cause
argument_list|,
literal|null
argument_list|)
throw|;
block|}
specifier|public
name|void
name|setWriteSingleElementAsList
parameter_list|(
name|boolean
name|writeSingleElementAsList
parameter_list|)
block|{
name|this
operator|.
name|writeSingleElementAsList
operator|=
name|writeSingleElementAsList
expr_stmt|;
block|}
block|}
end_class

end_unit
