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
name|net
operator|.
name|URI
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
name|Path
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
name|ProxyHelper
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
name|model
operator|.
name|ClassResourceInfo
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
name|AnnotationUtils
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
name|ResourceUtils
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|JAXRSClientFactory
block|{
specifier|private
name|JAXRSClientFactory
parameter_list|()
block|{               }
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|URI
operator|.
name|create
argument_list|(
name|baseAddress
argument_list|)
argument_list|,
name|cls
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|baseURI
argument_list|,
name|baseURI
argument_list|,
name|cls
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|)
block|{
return|return
name|create
argument_list|(
name|baseURI
argument_list|,
name|baseURI
argument_list|,
name|cls
argument_list|,
literal|true
argument_list|,
name|inheritHeaders
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|String
name|baseAddress
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|MediaType
name|contentType
parameter_list|,
name|MediaType
modifier|...
name|acceptTypes
parameter_list|)
block|{
name|T
name|proxy
init|=
name|create
argument_list|(
name|baseAddress
argument_list|,
name|cls
argument_list|)
decl_stmt|;
name|WebClient
operator|.
name|client
argument_list|(
name|proxy
argument_list|)
operator|.
name|type
argument_list|(
name|contentType
argument_list|)
operator|.
name|accept
argument_list|(
name|acceptTypes
argument_list|)
expr_stmt|;
return|return
name|proxy
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|fromClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|fromClient
argument_list|(
name|client
argument_list|,
name|cls
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|fromClient
parameter_list|(
name|Client
name|client
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|)
block|{
if|if
condition|(
name|client
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|cls
argument_list|)
condition|)
block|{
return|return
name|cls
operator|.
name|cast
argument_list|(
name|client
argument_list|)
return|;
block|}
name|T
name|proxy
init|=
name|create
argument_list|(
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|,
name|client
operator|.
name|getCurrentURI
argument_list|()
argument_list|,
name|cls
argument_list|,
name|AnnotationUtils
operator|.
name|getClassAnnotation
argument_list|(
name|cls
argument_list|,
name|Path
operator|.
name|class
argument_list|)
operator|!=
literal|null
argument_list|,
name|inheritHeaders
argument_list|)
decl_stmt|;
if|if
condition|(
name|inheritHeaders
condition|)
block|{
name|WebClient
operator|.
name|client
argument_list|(
name|proxy
argument_list|)
operator|.
name|headers
argument_list|(
name|client
operator|.
name|getHeaders
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|proxy
return|;
block|}
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|create
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|URI
name|currentURI
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|,
name|boolean
name|root
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|)
block|{
name|ClassResourceInfo
name|classResourceInfo
init|=
name|ResourceUtils
operator|.
name|createClassResourceInfo
argument_list|(
name|cls
argument_list|,
name|cls
argument_list|,
name|root
argument_list|,
literal|true
argument_list|)
decl_stmt|;
return|return
name|cls
operator|.
name|cast
argument_list|(
name|ProxyHelper
operator|.
name|getProxy
argument_list|(
name|cls
operator|.
name|getClassLoader
argument_list|()
argument_list|,
operator|new
name|Class
index|[]
block|{
name|cls
block|,
name|Client
operator|.
name|class
block|}
argument_list|,
operator|new
name|ClientProxyImpl
argument_list|(
name|baseURI
argument_list|,
name|currentURI
argument_list|,
name|classResourceInfo
argument_list|,
name|inheritHeaders
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
block|}
end_class

end_unit

