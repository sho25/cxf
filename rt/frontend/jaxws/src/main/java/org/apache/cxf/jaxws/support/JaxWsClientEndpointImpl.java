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
name|jaxws
operator|.
name|support
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|Executor
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
name|endpoint
operator|.
name|EndpointException
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
name|jaxws
operator|.
name|ServiceImpl
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
name|Service
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
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|JaxWsClientEndpointImpl
extends|extends
name|JaxWsEndpointImpl
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|3059035442836604053L
decl_stmt|;
specifier|private
name|ServiceImpl
name|executorProvider
decl_stmt|;
specifier|public
name|JaxWsClientEndpointImpl
parameter_list|(
name|Bus
name|bus
parameter_list|,
name|Service
name|s
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|ServiceImpl
name|si
parameter_list|,
name|WebServiceFeature
modifier|...
name|wf
parameter_list|)
throws|throws
name|EndpointException
block|{
name|super
argument_list|(
name|bus
argument_list|,
name|s
argument_list|,
name|ei
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
name|wf
argument_list|)
argument_list|)
expr_stmt|;
name|executorProvider
operator|=
name|si
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Executor
name|getExecutor
parameter_list|()
block|{
name|Executor
name|e
init|=
name|executorProvider
operator|.
name|getExecutor
argument_list|()
decl_stmt|;
if|if
condition|(
literal|null
operator|==
name|e
condition|)
block|{
name|e
operator|=
name|super
operator|.
name|getExecutor
argument_list|()
expr_stmt|;
block|}
return|return
name|e
return|;
block|}
block|}
end_class

end_unit

