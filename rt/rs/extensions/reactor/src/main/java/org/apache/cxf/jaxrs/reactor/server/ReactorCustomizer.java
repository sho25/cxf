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
name|reactor
operator|.
name|server
package|;
end_package

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
name|JAXRSServerFactoryBean
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
name|AbstractStreamingResponseExtension
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
name|service
operator|.
name|invoker
operator|.
name|Invoker
import|;
end_import

begin_class
specifier|public
class|class
name|ReactorCustomizer
extends|extends
name|AbstractStreamingResponseExtension
block|{
annotation|@
name|Override
specifier|protected
name|Invoker
name|createInvoker
parameter_list|(
name|JAXRSServerFactoryBean
name|bean
parameter_list|)
block|{
name|Boolean
name|useStreamingSubscriber
init|=
operator|(
name|Boolean
operator|)
name|bean
operator|.
name|getProperties
argument_list|(
literal|true
argument_list|)
operator|.
name|getOrDefault
argument_list|(
literal|"useStreamingSubscriber"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|ReactorInvoker
name|invoker
init|=
operator|new
name|ReactorInvoker
argument_list|()
decl_stmt|;
if|if
condition|(
name|useStreamingSubscriber
operator|!=
literal|null
condition|)
block|{
name|invoker
operator|.
name|setUseStreamingSubscriberIfPossible
argument_list|(
name|useStreamingSubscriber
argument_list|)
expr_stmt|;
block|}
return|return
name|invoker
return|;
block|}
block|}
end_class

end_unit

