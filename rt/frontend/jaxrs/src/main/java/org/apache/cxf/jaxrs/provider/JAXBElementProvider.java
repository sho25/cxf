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
name|java
operator|.
name|util
operator|.
name|HashMap
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
name|Provider
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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

begin_class
annotation|@
name|Provider
specifier|public
class|class
name|JAXBElementProvider
extends|extends
name|AbstractJAXBProvider
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|mProperties
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|setSchemas
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|locations
parameter_list|)
block|{
name|super
operator|.
name|setSchemas
argument_list|(
name|locations
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setMarshallerProperties
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|marshallProperties
parameter_list|)
block|{
name|mProperties
operator|=
name|marshallProperties
expr_stmt|;
block|}
specifier|public
name|void
name|setSchemaLocation
parameter_list|(
name|String
name|schemaLocation
parameter_list|)
block|{
name|mProperties
operator|.
name|put
argument_list|(
name|Marshaller
operator|.
name|JAXB_SCHEMA_LOCATION
argument_list|,
name|schemaLocation
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Object
name|readFrom
parameter_list|(
name|Class
argument_list|<
name|Object
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
try|try
block|{
name|Class
argument_list|<
name|?
argument_list|>
name|theType
init|=
name|getActualType
argument_list|(
name|type
argument_list|,
name|genericType
argument_list|)
decl_stmt|;
name|Unmarshaller
name|unmarshaller
init|=
name|createUnmarshaller
argument_list|(
name|theType
argument_list|,
name|genericType
argument_list|)
decl_stmt|;
if|if
condition|(
name|JAXBElement
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
condition|)
block|{
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
operator|new
name|StreamSource
argument_list|(
name|is
argument_list|)
argument_list|,
name|theType
argument_list|)
return|;
block|}
else|else
block|{
return|return
name|unmarshaller
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
return|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|Object
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
try|try
block|{
name|Object
name|actualObject
init|=
name|checkAdapter
argument_list|(
name|obj
argument_list|,
name|anns
argument_list|)
decl_stmt|;
name|Class
argument_list|<
name|?
argument_list|>
name|actualClass
init|=
name|actualObject
operator|.
name|getClass
argument_list|()
decl_stmt|;
if|if
condition|(
name|cls
operator|==
name|genericType
condition|)
block|{
name|genericType
operator|=
name|actualClass
expr_stmt|;
block|}
name|Marshaller
name|ms
init|=
name|createMarshaller
argument_list|(
name|actualObject
argument_list|,
name|actualClass
argument_list|,
name|genericType
argument_list|,
name|m
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|entry
range|:
name|mProperties
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ms
operator|.
name|setProperty
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|ms
operator|.
name|marshal
argument_list|(
name|actualObject
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WebApplicationException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

