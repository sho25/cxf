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
name|microprofile
operator|.
name|client
operator|.
name|proxy
package|;
end_package

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|core
operator|.
name|Response
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
name|client
operator|.
name|ClientProxyImpl
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
name|client
operator|.
name|ClientState
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
name|message
operator|.
name|Message
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
name|microprofile
operator|.
name|client
operator|.
name|MicroProfileClientProviderFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|microprofile
operator|.
name|rest
operator|.
name|client
operator|.
name|ext
operator|.
name|ResponseExceptionMapper
import|;
end_import

begin_class
specifier|public
class|class
name|MicroProfileClientProxyImpl
extends|extends
name|ClientProxyImpl
block|{
specifier|public
name|MicroProfileClientProxyImpl
parameter_list|(
name|URI
name|baseURI
parameter_list|,
name|ClassLoader
name|loader
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|,
name|boolean
name|isRoot
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|,
name|Object
modifier|...
name|varValues
parameter_list|)
block|{
name|super
argument_list|(
name|baseURI
argument_list|,
name|loader
argument_list|,
name|cri
argument_list|,
name|isRoot
argument_list|,
name|inheritHeaders
argument_list|,
name|varValues
argument_list|)
expr_stmt|;
block|}
specifier|public
name|MicroProfileClientProxyImpl
parameter_list|(
name|ClientState
name|initialState
parameter_list|,
name|ClassLoader
name|loader
parameter_list|,
name|ClassResourceInfo
name|cri
parameter_list|,
name|boolean
name|isRoot
parameter_list|,
name|boolean
name|inheritHeaders
parameter_list|,
name|Object
modifier|...
name|varValues
parameter_list|)
block|{
name|super
argument_list|(
name|initialState
argument_list|,
name|loader
argument_list|,
name|cri
argument_list|,
name|isRoot
argument_list|,
name|inheritHeaders
argument_list|,
name|varValues
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|checkResponse
parameter_list|(
name|Method
name|m
parameter_list|,
name|Response
name|r
parameter_list|,
name|Message
name|inMessage
parameter_list|)
throws|throws
name|Throwable
block|{
name|MicroProfileClientProviderFactory
name|factory
init|=
name|MicroProfileClientProviderFactory
operator|.
name|getInstance
argument_list|(
name|inMessage
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
argument_list|>
name|mappers
init|=
name|factory
operator|.
name|createResponseExceptionMapper
argument_list|(
name|inMessage
argument_list|,
name|Throwable
operator|.
name|class
argument_list|)
decl_stmt|;
for|for
control|(
name|ResponseExceptionMapper
argument_list|<
name|?
argument_list|>
name|mapper
range|:
name|mappers
control|)
block|{
if|if
condition|(
name|mapper
operator|.
name|handles
argument_list|(
name|r
operator|.
name|getStatus
argument_list|()
argument_list|,
name|r
operator|.
name|getHeaders
argument_list|()
argument_list|)
condition|)
block|{
name|Throwable
name|t
init|=
name|mapper
operator|.
name|toThrowable
argument_list|(
name|r
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|instanceof
name|RuntimeException
condition|)
block|{
throw|throw
name|t
throw|;
block|}
elseif|else
if|if
condition|(
name|t
operator|!=
literal|null
operator|&&
name|m
operator|.
name|getExceptionTypes
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// its a checked exception, make sure its declared
for|for
control|(
name|Class
argument_list|<
name|?
argument_list|>
name|c
range|:
name|m
operator|.
name|getExceptionTypes
argument_list|()
control|)
block|{
if|if
condition|(
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|isAssignableFrom
argument_list|(
name|c
argument_list|)
condition|)
block|{
throw|throw
name|t
throw|;
block|}
block|}
comment|// TODO Log the unhandled declarable
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

