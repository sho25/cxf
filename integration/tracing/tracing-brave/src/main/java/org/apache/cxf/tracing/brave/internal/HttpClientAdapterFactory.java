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
name|tracing
operator|.
name|brave
operator|.
name|internal
package|;
end_package

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
name|brave
operator|.
name|http
operator|.
name|HttpClientAdapter
import|;
end_import

begin_interface
specifier|public
interface|interface
name|HttpClientAdapterFactory
extends|extends
name|HttpAdapterFactory
block|{
specifier|static
name|HttpClientAdapter
argument_list|<
name|Request
argument_list|,
name|Response
argument_list|>
name|create
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
operator|new
name|HttpClientAdapter
argument_list|<
name|Request
argument_list|,
name|Response
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|method
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
name|request
operator|.
name|method
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|path
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
name|request
operator|.
name|uri
argument_list|()
operator|.
name|getPath
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|url
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
return|return
name|request
operator|.
name|uri
argument_list|()
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|requestHeader
parameter_list|(
name|Request
name|request
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|value
init|=
name|request
operator|.
name|headers
argument_list|()
operator|.
name|get
argument_list|(
name|name
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
operator|&&
operator|!
name|value
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
return|return
name|value
operator|.
name|get
argument_list|(
literal|0
argument_list|)
return|;
block|}
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|statusCode
parameter_list|(
name|Response
name|response
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The operation is not supported for request adapter"
argument_list|)
throw|;
block|}
block|}
return|;
block|}
specifier|static
name|HttpClientAdapter
argument_list|<
name|Request
argument_list|,
name|Response
argument_list|>
name|create
parameter_list|(
name|Response
name|response
parameter_list|)
block|{
return|return
operator|new
name|HttpClientAdapter
argument_list|<
name|Request
argument_list|,
name|Response
argument_list|>
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|String
name|method
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The operation is not supported for response adapter"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|path
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The operation is not supported for response adapter"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|url
parameter_list|(
name|Request
name|request
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The operation is not supported for response adapter"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|requestHeader
parameter_list|(
name|Request
name|request
parameter_list|,
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The operation is not supported for response adapter"
argument_list|)
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Integer
name|statusCode
parameter_list|(
name|Response
name|response
parameter_list|)
block|{
return|return
name|response
operator|.
name|status
argument_list|()
return|;
block|}
block|}
return|;
block|}
block|}
end_interface

end_unit

