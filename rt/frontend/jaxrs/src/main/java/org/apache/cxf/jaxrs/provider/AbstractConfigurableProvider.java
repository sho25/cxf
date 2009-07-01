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
name|util
operator|.
name|List
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
name|BusFactory
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractConfigurableProvider
block|{
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|consumeMediaTypes
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|produceMediaTypes
decl_stmt|;
specifier|private
name|boolean
name|enableBuffering
decl_stmt|;
specifier|private
name|Bus
name|bus
decl_stmt|;
specifier|public
name|void
name|setBus
parameter_list|(
name|Bus
name|b
parameter_list|)
block|{
if|if
condition|(
name|bus
operator|!=
literal|null
condition|)
block|{
name|bus
operator|=
name|b
expr_stmt|;
block|}
block|}
specifier|public
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
operator|!=
literal|null
condition|?
name|bus
else|:
name|BusFactory
operator|.
name|getThreadDefaultBus
argument_list|()
return|;
block|}
specifier|public
name|void
name|setConsumeMediaTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|consumeMediaTypes
operator|=
name|types
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getConsumeMediaTypes
parameter_list|()
block|{
return|return
name|consumeMediaTypes
return|;
block|}
specifier|public
name|void
name|setProduceMediaTypes
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|types
parameter_list|)
block|{
name|produceMediaTypes
operator|=
name|types
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getProduceMediaTypes
parameter_list|()
block|{
return|return
name|produceMediaTypes
return|;
block|}
specifier|public
name|void
name|setEnableBuffering
parameter_list|(
name|boolean
name|enableBuf
parameter_list|)
block|{
name|enableBuffering
operator|=
name|enableBuf
expr_stmt|;
block|}
specifier|public
name|boolean
name|getEnableBuffering
parameter_list|()
block|{
return|return
name|enableBuffering
return|;
block|}
block|}
end_class

end_unit

