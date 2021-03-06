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
name|feature
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
name|javax
operator|.
name|xml
operator|.
name|ws
operator|.
name|WebServiceFeature
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
name|Bus
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
name|interceptor
operator|.
name|InterceptorProvider
import|;
end_import

begin_comment
comment|/**  * A Feature is something that is able to customize a Server, Client, or Bus, typically  * adding capabilities. For instance, there may be a LoggingFeature which configures  * one of the above to log each of their messages.  *<p>  * By default the initialize methods all delegate to initializeProvider(InterceptorProvider).  * If you're simply adding interceptors to a Server, Client, or Bus, this allows you to add  * them easily.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractFeature
extends|extends
name|WebServiceFeature
implements|implements
name|AbstractPortableFeature
block|{
annotation|@
name|Override
specifier|public
name|String
name|getID
parameter_list|()
block|{
return|return
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isEnabled
parameter_list|()
block|{
return|return
name|enabled
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|doInitializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
name|initializeProvider
argument_list|(
name|provider
argument_list|,
name|bus
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|initializeProvider
parameter_list|(
name|InterceptorProvider
name|provider
parameter_list|,
name|Bus
name|bus
parameter_list|)
block|{
comment|// no-op
block|}
specifier|public
specifier|static
parameter_list|<
name|T
parameter_list|>
name|T
name|getActive
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Feature
argument_list|>
name|features
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|type
parameter_list|)
block|{
return|return
name|AbstractPortableFeature
operator|.
name|getActive
argument_list|(
name|features
argument_list|,
name|type
argument_list|)
return|;
block|}
block|}
end_class

end_unit

