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
name|MessageBodyWriter
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
name|JAXBElementProvider
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
name|GenericHandlerWriter
implements|implements
name|MessageBodyWriter
argument_list|<
name|GenericHandler
argument_list|<
name|Book
argument_list|>
argument_list|>
block|{
name|JAXBElementProvider
name|jaxb
init|=
operator|new
name|JAXBElementProvider
argument_list|()
decl_stmt|;
specifier|public
name|GenericHandlerWriter
parameter_list|()
block|{              }
specifier|public
name|long
name|getSize
parameter_list|(
name|GenericHandler
argument_list|<
name|Book
argument_list|>
name|t
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
name|mediaType
parameter_list|)
block|{
return|return
operator|-
literal|1
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
name|mediaType
parameter_list|)
block|{
return|return
name|type
operator|==
name|GenericHandler
operator|.
name|class
operator|&&
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|genericType
argument_list|)
operator|==
name|Book
operator|.
name|class
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|GenericHandler
argument_list|<
name|Book
argument_list|>
name|o
parameter_list|,
name|Class
argument_list|<
name|?
argument_list|>
name|c
parameter_list|,
name|Type
name|t
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
throws|,
name|WebApplicationException
block|{
name|jaxb
operator|.
name|writeTo
argument_list|(
name|o
operator|.
name|getEntity
argument_list|()
argument_list|,
name|o
operator|.
name|getEntity
argument_list|()
operator|.
name|getClass
argument_list|()
argument_list|,
name|InjectionUtils
operator|.
name|getActualType
argument_list|(
name|t
argument_list|)
argument_list|,
name|anns
argument_list|,
name|m
argument_list|,
name|headers
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

