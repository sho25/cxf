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
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|HttpMethod
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
name|client
operator|.
name|Entity
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
name|client
operator|.
name|SyncInvoker
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
name|GenericType
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

begin_class
specifier|public
class|class
name|SyncInvokerImpl
implements|implements
name|SyncInvoker
block|{
specifier|private
name|WebClient
name|wc
decl_stmt|;
specifier|public
name|SyncInvokerImpl
parameter_list|(
name|WebClient
name|wc
parameter_list|)
block|{
name|this
operator|.
name|wc
operator|=
name|wc
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|delete
parameter_list|()
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|delete
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|delete
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|DELETE
argument_list|,
name|genericType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|get
parameter_list|()
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|get
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|GET
argument_list|,
name|genericType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|head
parameter_list|()
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|HEAD
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|options
parameter_list|()
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|OPTIONS
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|options
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|OPTIONS
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|options
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|OPTIONS
argument_list|,
name|genericType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|post
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|post
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|post
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|POST
argument_list|,
name|entity
argument_list|,
name|genericType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|put
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|PUT
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|put
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|PUT
argument_list|,
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|put
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|PUT
argument_list|,
name|entity
argument_list|,
name|genericType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|trace
parameter_list|()
block|{
return|return
name|method
argument_list|(
literal|"TRACE"
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|trace
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|method
argument_list|(
literal|"TRACE"
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|trace
parameter_list|(
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|method
argument_list|(
literal|"TRACE"
argument_list|,
name|genericType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|method
parameter_list|(
name|String
name|method
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|method
argument_list|,
name|Response
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|wc
operator|.
name|invoke
argument_list|(
name|method
argument_list|,
literal|null
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|wc
operator|.
name|invoke
argument_list|(
name|method
argument_list|,
literal|null
argument_list|,
name|genericType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|method
argument_list|,
name|entity
argument_list|,
name|Response
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
return|return
name|wc
operator|.
name|invoke
argument_list|(
name|method
argument_list|,
name|entity
argument_list|,
name|cls
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|method
parameter_list|(
name|String
name|method
parameter_list|,
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|genericType
parameter_list|)
block|{
return|return
name|wc
operator|.
name|invoke
argument_list|(
name|method
argument_list|,
name|entity
argument_list|,
name|genericType
argument_list|)
return|;
block|}
specifier|public
name|WebClient
name|getWebClient
parameter_list|()
block|{
return|return
name|wc
return|;
block|}
annotation|@
name|Override
specifier|public
name|Response
name|patch
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|PATCH
argument_list|,
name|entity
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|patch
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|responseType
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|PATCH
argument_list|,
name|entity
argument_list|,
name|responseType
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|patch
parameter_list|(
name|Entity
argument_list|<
name|?
argument_list|>
name|entity
parameter_list|,
name|GenericType
argument_list|<
name|T
argument_list|>
name|responseType
parameter_list|)
block|{
return|return
name|method
argument_list|(
name|HttpMethod
operator|.
name|PATCH
argument_list|,
name|entity
argument_list|,
name|responseType
argument_list|)
return|;
block|}
block|}
end_class

end_unit

