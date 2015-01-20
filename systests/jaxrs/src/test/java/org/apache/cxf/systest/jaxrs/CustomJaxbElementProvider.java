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
name|UriInfo
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
name|annotations
operator|.
name|SchemaValidation
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

begin_class
specifier|public
class|class
name|CustomJaxbElementProvider
extends|extends
name|JAXBElementProvider
argument_list|<
name|Book
argument_list|>
block|{
annotation|@
name|Context
specifier|private
name|UriInfo
name|ui
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
name|mt
parameter_list|)
block|{
if|if
condition|(
name|ui
operator|.
name|getRequestUri
argument_list|()
operator|.
name|toString
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"/test/5/bookstorestorage/thosebooks/123"
argument_list|)
condition|)
block|{
for|for
control|(
name|Annotation
name|ann
range|:
name|anns
control|)
block|{
if|if
condition|(
name|ann
operator|.
name|annotationType
argument_list|()
operator|==
name|SchemaValidation
operator|.
name|class
condition|)
block|{
return|return
name|super
operator|.
name|isWriteable
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|)
return|;
block|}
block|}
throw|throw
operator|new
name|RuntimeException
argument_list|()
throw|;
block|}
else|else
block|{
return|return
name|super
operator|.
name|isWriteable
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|,
name|anns
argument_list|,
name|mt
argument_list|)
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|writeTo
parameter_list|(
name|Book
name|obj
parameter_list|,
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
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
name|MediaType
operator|.
name|valueOf
argument_list|(
name|m
operator|.
name|toString
argument_list|()
operator|+
literal|";a=b"
argument_list|)
argument_list|)
expr_stmt|;
name|super
operator|.
name|writeTo
argument_list|(
name|obj
argument_list|,
name|cls
argument_list|,
name|genericType
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

