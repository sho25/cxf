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
name|javax
operator|.
name|activation
operator|.
name|DataHandler
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|activation
operator|.
name|DataSource
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
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|util
operator|.
name|StringUtils
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
name|helpers
operator|.
name|IOUtils
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
name|multipart
operator|.
name|InputStreamDataSource
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

begin_class
annotation|@
name|Provider
specifier|public
class|class
name|DataSourceProvider
parameter_list|<
name|T
parameter_list|>
implements|implements
name|MessageBodyReader
argument_list|<
name|T
argument_list|>
implements|,
name|MessageBodyWriter
argument_list|<
name|T
argument_list|>
block|{
specifier|private
name|boolean
name|useDataSourceContentType
decl_stmt|;
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
name|isSupported
argument_list|(
name|type
argument_list|,
name|mt
argument_list|)
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
name|cls
parameter_list|,
name|Type
name|genericType
parameter_list|,
name|Annotation
index|[]
name|annotations
parameter_list|,
name|MediaType
name|type
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
name|DataSource
name|ds
init|=
operator|new
name|InputStreamDataSource
argument_list|(
name|is
argument_list|,
name|type
operator|.
name|toString
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|cls
operator|.
name|cast
argument_list|(
name|DataSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|?
name|ds
else|:
operator|new
name|DataHandler
argument_list|(
name|ds
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|long
name|getSize
parameter_list|(
name|T
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
name|mt
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
name|mt
parameter_list|)
block|{
return|return
name|isSupported
argument_list|(
name|type
argument_list|,
name|mt
argument_list|)
return|;
block|}
specifier|private
name|boolean
name|isSupported
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|MediaType
name|mt
parameter_list|)
block|{
return|return
operator|!
name|mt
operator|.
name|getType
argument_list|()
operator|.
name|equals
argument_list|(
literal|"multipart"
argument_list|)
operator|&&
operator|(
name|DataSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|||
name|DataHandler
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|type
argument_list|)
operator|)
return|;
block|}
specifier|public
name|void
name|writeTo
parameter_list|(
name|T
name|src
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
name|annotations
parameter_list|,
name|MediaType
name|type
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
name|DataSource
name|ds
init|=
name|DataSource
operator|.
name|class
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|?
operator|(
name|DataSource
operator|)
name|src
else|:
operator|(
operator|(
name|DataHandler
operator|)
name|src
operator|)
operator|.
name|getDataSource
argument_list|()
decl_stmt|;
if|if
condition|(
name|useDataSourceContentType
condition|)
block|{
name|setContentTypeIfNeeded
argument_list|(
name|type
argument_list|,
name|headers
argument_list|,
name|ds
operator|.
name|getContentType
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|IOUtils
operator|.
name|copyAndCloseInput
argument_list|(
name|ds
operator|.
name|getInputStream
argument_list|()
argument_list|,
name|os
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|setContentTypeIfNeeded
parameter_list|(
name|MediaType
name|type
parameter_list|,
name|MultivaluedMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|headers
parameter_list|,
name|String
name|ct
parameter_list|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|ct
argument_list|)
operator|&&
operator|!
name|type
operator|.
name|equals
argument_list|(
name|JAXRSUtils
operator|.
name|toMediaType
argument_list|(
name|ct
argument_list|)
argument_list|)
condition|)
block|{
name|headers
operator|.
name|putSingle
argument_list|(
literal|"Content-Type"
argument_list|,
name|ct
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|setUseDataSourceContentType
parameter_list|(
name|boolean
name|useDataSourceContentType
parameter_list|)
block|{
name|this
operator|.
name|useDataSourceContentType
operator|=
name|useDataSourceContentType
expr_stmt|;
block|}
block|}
end_class

end_unit

